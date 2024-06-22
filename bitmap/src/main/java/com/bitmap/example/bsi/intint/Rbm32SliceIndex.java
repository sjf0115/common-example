package com.bitmap.example.bsi.intint;

import com.bitmap.example.bsi.core.Operation;
import com.bitmap.example.bsi.core.Pair;
import com.bitmap.example.bsi.core.WritableUtils;
import org.roaringbitmap.RoaringBitmap;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * 功能：Rbm32SliceIndex  Decimal
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/6/16 00:52
 */
public class Rbm32SliceIndex implements Bitmap32SliceIndex {
    private int maxValue;
    private int minValue;
    private RoaringBitmap[] rbm;
    private RoaringBitmap ebm;
    private Boolean runOptimized = false;

    // 构造器
    public Rbm32SliceIndex(int minValue, int maxValue) {
        if (minValue < 0) {
            throw new IllegalArgumentException("Values should be non-negative");
        }

        this.rbm = new RoaringBitmap[32 - Integer.numberOfLeadingZeros(maxValue)];
        for (int i = 0; i < rbm.length; i++) {
            this.rbm[i] = new RoaringBitmap();
        }

        this.ebm = new RoaringBitmap();
    }

    public Rbm32SliceIndex() {
        this(0, 0);
    }

    // 切片个数-最大值二进制位数
    @Override
    public int bitCount() {
        return this.rbm.length;
    }

    // 基数
    @Override
    public long getLongCardinality() {
        return this.ebm.getLongCardinality();
    }

    // 设置 index 的 value 值
    @Override
    public void setValue(int index, int value) {
        ensureCapacityInternal(value, value);
        setValueInternal(index, value);
    }

    // 批量设置 index 的 value 值
    @Override
    public void setValues(List<Pair<Integer, Integer>> values) {
        int maxValue = values.stream().mapToInt(Pair::getRight).filter(Objects::nonNull).max().getAsInt();
        int minValue = values.stream().mapToInt(Pair::getRight).filter(Objects::nonNull).min().getAsInt();
        ensureCapacityInternal(minValue, maxValue);
        for (Pair<Integer, Integer> pair : values) {
            setValueInternal(pair.getKey(), pair.getValue());
        }
    }

    // 计算 index 对应的值 value
    @Override
    public int getValue(int index) {
        boolean exists = this.ebm.contains(index);
        if (!exists) {
            return -1;
        }
        return valueAt(index);
    }

    // 是否存在指定的 index
    @Override
    public boolean valueExist(int index) {
        return this.ebm.contains(index);
    }

    public void add(Rbm32SliceIndex otherBsi) {
        if (null == otherBsi || otherBsi.ebm.isEmpty()) {
            return;
        }

        this.ebm.or(otherBsi.ebm);
        if (otherBsi.bitCount() > this.bitCount()) {
            grow(otherBsi.bitCount());
        }

        for (int i = 0; i < otherBsi.bitCount(); i++) {
            this.addDigit(otherBsi.rbm[i], i);
        }

        this.minValue = minValue();
        this.maxValue = maxValue();
    }

    private void addDigit(RoaringBitmap foundSet, int i) {
        RoaringBitmap carry = RoaringBitmap.and(this.rbm[i], foundSet);
        this.rbm[i].xor(foundSet);
        if (!carry.isEmpty()) {
            if (i + 1 >= this.bitCount()) {
                grow(this.bitCount() + 1);
            }
            this.addDigit(carry, i + 1);
        }

    }

    private int minValue() {
        if (ebm.isEmpty()) {
            return 0;
        }

        RoaringBitmap minValuesId = ebm;
        for (int i = rbm.length - 1; i >= 0; i -= 1) {
            RoaringBitmap tmp = RoaringBitmap.andNot(minValuesId, rbm[i]);
            if (!tmp.isEmpty()) {
                minValuesId = tmp;
            }
        }

        return valueAt(minValuesId.first());
    }

    private int maxValue() {
        if (ebm.isEmpty()) {
            return 0;
        }

        RoaringBitmap maxValuesId = ebm;
        for (int i = rbm.length - 1; i >= 0; i -= 1) {
            RoaringBitmap tmp = RoaringBitmap.and(maxValuesId, rbm[i]);
            if (!tmp.isEmpty()) {
                maxValuesId = tmp;
            }
        }

        return valueAt(maxValuesId.first());
    }



    private void clear() {
        this.maxValue = 0;
        this.minValue = 0;
        this.ebm = null;
        this.rbm = null;
    }

    public void runOptimize() {
        this.ebm.runOptimize();

        for (RoaringBitmap integers : this.rbm) {
            integers.runOptimize();
        }
        this.runOptimized = true;
    }

    public boolean hasRunCompression() {
        return this.runOptimized;
    }

    @Override
    public void serialize(DataOutput output) throws IOException {
        // write meta
        WritableUtils.writeVInt(output, minValue);
        WritableUtils.writeVInt(output, maxValue);
        output.writeBoolean(this.runOptimized);

        // write ebm
        this.ebm.serialize(output);

        // write ba
        WritableUtils.writeVInt(output, this.rbm.length);
        for (RoaringBitmap rb : this.rbm) {
            rb.serialize(output);
        }
    }

    @Override
    public void deserialize(DataInput in) throws IOException {
        this.clear();

        // read meta
        this.minValue = WritableUtils.readVInt(in);
        this.maxValue = WritableUtils.readVInt(in);
        this.runOptimized = in.readBoolean();

        // read ebm
        RoaringBitmap ebm = new RoaringBitmap();
        ebm.deserialize(in);
        this.ebm = ebm;

        // read ba
        int bitDepth = WritableUtils.readVInt(in);
        RoaringBitmap[] ba = new RoaringBitmap[bitDepth];
        for (int i = 0; i < bitDepth; i++) {
            RoaringBitmap rb = new RoaringBitmap();
            rb.deserialize(in);
            ba[i] = rb;
        }
        this.rbm = ba;
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        // write meta
        buffer.putInt(this.minValue);
        buffer.putInt(this.maxValue);
        buffer.put(this.runOptimized ? (byte) 1 : (byte) 0);
        // write ebm
        this.ebm.serialize(buffer);

        // write ba
        buffer.putInt(this.rbm.length);
        for (RoaringBitmap rb : this.rbm) {
            rb.serialize(buffer);
        }

    }

    @Override
    public void deserialize(ByteBuffer buffer) throws IOException {
        this.clear();
        // read meta
        this.minValue = buffer.getInt();
        this.maxValue = buffer.getInt();
        this.runOptimized = buffer.get() == (byte) 1;

        // read ebm
        RoaringBitmap ebm = new RoaringBitmap();
        ebm.deserialize(buffer);
        this.ebm = ebm;
        // read ba
        buffer.position(buffer.position() + ebm.serializedSizeInBytes());
        int bitDepth = buffer.getInt();
        RoaringBitmap[] ba = new RoaringBitmap[bitDepth];
        for (int i = 0; i < bitDepth; i++) {
            RoaringBitmap rb = new RoaringBitmap();
            rb.deserialize(buffer);
            ba[i] = rb;
            buffer.position(buffer.position() + rb.serializedSizeInBytes());
        }
        this.rbm = ba;
    }

    @Override
    public int serializedSizeInBytes() {
        int size = 0;
        for (RoaringBitmap rb : this.rbm) {
            size += rb.serializedSizeInBytes();
        }
        return 4 + 4 + 1 + 4 + this.ebm.serializedSizeInBytes() + size;
    }

    public void merge(Rbm32SliceIndex otherBsi) {

        if (null == otherBsi || otherBsi.ebm.isEmpty()) {
            return;
        }

        // todo whether we need this
        if (RoaringBitmap.intersects(this.ebm, otherBsi.ebm)) {
            throw new IllegalArgumentException("merge can be used only in bsiA  bsiB  is null");
        }

        int bitDepth = Integer.max(this.bitCount(), otherBsi.bitCount());
        RoaringBitmap[] newBA = new RoaringBitmap[bitDepth];
        for (int i = 0; i < bitDepth; i++) {
            RoaringBitmap current = i < this.rbm.length ? this.rbm[i] : new RoaringBitmap();
            RoaringBitmap other = i < otherBsi.rbm.length ? otherBsi.rbm[i] : new RoaringBitmap();
            newBA[i] = RoaringBitmap.or(current, other);
            if (this.runOptimized || otherBsi.runOptimized) {
                newBA[i].runOptimize();
            }
        }
        this.rbm = newBA;
        this.ebm.or(otherBsi.ebm);
        this.runOptimized = this.runOptimized || otherBsi.runOptimized;
        this.maxValue = Integer.max(this.maxValue, otherBsi.maxValue);
        this.minValue = Integer.min(this.minValue, otherBsi.minValue);
    }

    public Rbm32SliceIndex clone() {
        Rbm32SliceIndex bitSliceIndex = new Rbm32SliceIndex();
        bitSliceIndex.minValue = this.minValue;
        bitSliceIndex.maxValue = this.maxValue;
        bitSliceIndex.ebm = this.ebm.clone();
        RoaringBitmap[] cloneBA = new RoaringBitmap[this.bitCount()];
        for (int i = 0; i < cloneBA.length; i++) {
            cloneBA[i] = this.rbm[i].clone();
        }
        bitSliceIndex.rbm = cloneBA;
        bitSliceIndex.runOptimized = this.runOptimized;

        return bitSliceIndex;
    }

    @Override
    public RoaringBitmap compare(Operation operation, int value) {
        RoaringBitmap empty = new RoaringBitmap();
        switch (operation) {
            case EQ:
                if (minValue == maxValue && minValue == value) {
                    return this.ebm;
                } else if (value < minValue || value > maxValue) {
                    return empty;
                }
                return oNeilCompare(Operation.EQ, value);
            case NEQ:
                if (minValue == maxValue) {
                    return minValue == value ? empty : this.ebm;
                }
                return oNeilCompare(Operation.NEQ, value);
            case GE:
                if (value <= minValue) {
                    return this.ebm;
                } else if (value > maxValue) {
                    return empty;
                }
                return oNeilCompare(Operation.GE, value);
            case GT: {
                if (value < minValue) {
                    return this.ebm;
                } else if (value >= maxValue) {
                    return empty;
                }
                return oNeilCompare(Operation.GT, value);
            }
            case LT:
                if (value > maxValue) {
                    return this.ebm;
                } else if (value <= minValue) {
                    return empty;
                }
                return oNeilCompare(Operation.LT, value);
            case LE:
                if (value >= maxValue) {
                    return this.ebm;
                } else if (value < minValue) {
                    return empty;
                }
                return oNeilCompare(Operation.LE, value);
            case RANGE:

            default:
                throw new IllegalArgumentException("not support operation!");
        }
    }

    @Override
    public RoaringBitmap compareRange(int start, int end) {
        if (start <= minValue && end >= maxValue) {
            // 查询范围包含[minValue, maxValue] 返回全部用户
            return this.ebm;
        } else if (start > maxValue || end < minValue) {
            // 查询不在[minValue, maxValue]范围内 返回空
            return new RoaringBitmap();
        }

        RoaringBitmap geStart = oNeilCompare(Operation.GE, start);
        RoaringBitmap leEnd = oNeilCompare(Operation.LE, end);
        return RoaringBitmap.and(geStart, leEnd);
    }

    public Pair<Long, Long> sum(RoaringBitmap foundSet) {
        if (null == foundSet || foundSet.isEmpty()) {
            return Pair.newPair(0L, 0L);
        }
        long count = foundSet.getLongCardinality();

        Long sum = IntStream.range(0, this.bitCount())
                .mapToLong(x -> (long) (1 << x) * RoaringBitmap.andCardinality(this.rbm[x], foundSet))
                .sum();

        return Pair.newPair(sum, count);
    }

    //------------------------------------------------------------------------------------------

    // 切片空间
    private void ensureCapacityInternal(int minValue, int maxValue) {
        if (ebm.isEmpty()) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            grow(Integer.toBinaryString(maxValue).length());
        } else if (this.minValue > minValue) {
            this.minValue = minValue;
        } else if (this.maxValue < maxValue) {
            this.maxValue = maxValue;
            grow(Integer.toBinaryString(maxValue).length());
        }
    }

    // 增加切片空看-增加Bitmap个数
    private void grow(int newBitNum) {
        int bitNum = this.rbm.length;

        if (bitNum >= newBitNum) {
            return;
        }

        // 拷贝
        RoaringBitmap[] newRbm = new RoaringBitmap[newBitNum];
        if (bitNum != 0) {
            System.arraycopy(this.rbm, 0, newRbm, 0, bitNum);
        }

        for (int i = newBitNum - 1; i >= bitNum; i--) {
            newRbm[i] = new RoaringBitmap();
            if (this.runOptimized) {
                newRbm[i].runOptimize();
            }
        }
        this.rbm = newRbm;
    }

    // 设置值
    private void setValueInternal(int index, int value) {
        // 为 value 的每个切片 bitmap 添加 index
        for (int i = 0; i < this.bitCount(); i += 1) {
            if ((value & (1 << i)) > 0) {
                this.rbm[i].add(index);
            } else {
                this.rbm[i].remove(index);
            }
        }
        this.ebm.add(index);
    }

    // 计算 index 的 value 值
    private int valueAt(int index) {
        int value = 0;
        for (int i = 0; i < this.bitCount(); i += 1) {
            if (this.rbm[i].contains(index)) {
                value |= (1 << i);
            }
        }
        return value;
    }

    // oNeil 比较算法实现
    private RoaringBitmap oNeilCompare(Operation operation, int value) {
        RoaringBitmap GT = new RoaringBitmap();
        RoaringBitmap LT = new RoaringBitmap();
        RoaringBitmap EQ = this.ebm;

        for (int i = this.bitCount() - 1; i >= 0; i--) {
            int bit = (value >> i) & 1;
            if (bit == 1) {
                LT = RoaringBitmap.or(LT, RoaringBitmap.andNot(EQ, this.rbm[i]));
                EQ = RoaringBitmap.and(EQ, this.rbm[i]);
            } else {
                GT = RoaringBitmap.or(GT, RoaringBitmap.and(EQ, this.rbm[i]));
                EQ = RoaringBitmap.andNot(EQ, this.rbm[i]);
            }
        }

        switch (operation) {
            case EQ:
                return EQ;
            case NEQ:
                return RoaringBitmap.andNot(this.ebm, EQ);
            case GT:
                return GT;
            case LT:
                return LT;
            case LE:
                return RoaringBitmap.or(LT, EQ);
            case GE:
                return RoaringBitmap.or(GT, EQ);
            default:
                throw new IllegalArgumentException("");
        }
    }
}
