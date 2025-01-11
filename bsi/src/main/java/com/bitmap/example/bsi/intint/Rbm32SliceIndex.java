package com.bitmap.example.bsi.intint;

import com.bitmap.example.bsi.core.Operation;
import com.bitmap.example.bsi.core.Pair;
import com.bitmap.example.bsi.core.WritableUtils;
import org.roaringbitmap.RoaringBitmap;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * 功能：Rbm32SliceIndex Base 2
 *         每一个切片对应一个 RoaringBitmap
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/6/16 00:52
 */
public class Rbm32SliceIndex implements BitmapSliceIndex {
    private int maxValue = -1;
    private int minValue = -1;
    private int sliceSize = 0;
    private RoaringBitmap[] slices;
    private RoaringBitmap ebm;
    private Boolean runOptimized = false;

    // 构造器
    public Rbm32SliceIndex(int minValue, int maxValue) {
        if (minValue < 0) {
            throw new IllegalArgumentException("Value should be non-negative");
        }
        // 索引切片个数等于最大整数二进制位数，即32减去最大整数二进制填充0个数
        sliceSize = 32 - Integer.numberOfLeadingZeros(maxValue);
        this.slices = new RoaringBitmap[sliceSize];
        for (int i = 0; i < slices.length; i++) {
            this.slices[i] = new RoaringBitmap();
        }
        this.ebm = new RoaringBitmap();
    }

    public Rbm32SliceIndex() {
        this(0, 0);
    }

    /**
     * 切片个数
     *      最大值二进制位数
     * @return
     */
    @Override
    public int sliceSize() {
        return sliceSize;
    }

    /**
     * 基数基数
     * @return
     */
    @Override
    public long getLongCardinality() {
        return this.ebm.getLongCardinality();
    }

    /**
     * 如果 BSI 不包含 key-value 映射，返回 true
     */
    @Override
    public boolean isEmpty() {
        return this.getLongCardinality() == 0;
    }

    /**
     * 从 BSI 中删除所有的映射，BSI 变空
     */
    public void clear() {
        this.maxValue = -1;
        this.minValue = -1;
        this.ebm = new RoaringBitmap();
        this.slices = null;
        this.sliceSize = 0;
    }

    /**
     * 指定的 key 是否关联指定的 value
     * @param key
     * @return
     */
    public boolean containsKey(int key) {
        return this.ebm.contains(key);
    }

    /**
     * 指定的 value 是否关联指定的 key
     * @param value
     * @return
     */
    public boolean containsValue(int value) {
        return false;
    }

    /**
     * 为指定的 Key 关联指定的 Value
     * @param key
     * @param value
     */
    @Override
    public void put(int key, int value) {
        // 更新最大值和最小值
        if (this.isEmpty()) {
            this.minValue = value;
            this.maxValue = value;
        } else if (this.minValue > value) {
            this.minValue = value;
        } else if (this.maxValue < value) {
            this.maxValue = value;
        }
        // 调整切片个数
        int newSliceSize = Integer.toBinaryString(value).length();
        resize(newSliceSize);
        // 为指定 Key 关联指定 Value
        putValueInternal(key, value);
    }

    @Override
    public void putAll(BitmapSliceIndex otherBsi) {
        if (null == otherBsi || otherBsi.isEmpty()) {
            return;
        }

        this.ebm.or(otherBsi.ebm);
        if (otherBsi.sliceSize() > this.sliceSize()) {
            resize(otherBsi.sliceSize());
        }

        for (int i = 0; i < otherBsi.sliceSize(); i++) {
            this.addDigit(otherBsi.slices[i], i);
        }

        this.minValue = minValue();
        this.maxValue = maxValue();
    }

    /**
     * 获取 key 对应的 value
     * @param key
     * @return
     */
    @Override
    public int get(int key) {
        if (!this.containsKey(key)) {
            return -1;
        }
        return getValueInternal(key);
    }

    @Override
    public int remove(int key) {
        return 0;
    }

    @Override
    public boolean remove(int key, int value) {
        return false;
    }

    @Override
    public Collection<Integer> keys() {
        return null;
    }

    @Override
    public Collection<Integer> values() {
        return null;
    }

    @Override
    public int replace(int key, int value) {
        return 0;
    }

    @Override
    public int replace(int key, int oldValue, int newValue) {
        return 0;
    }


    public void add(Rbm32SliceIndex otherBsi) {

    }

    private void addDigit(RoaringBitmap foundSet, int i) {
        RoaringBitmap carry = RoaringBitmap.and(this.slices[i], foundSet);
        this.slices[i].xor(foundSet);
        if (!carry.isEmpty()) {
            if (i + 1 >= this.sliceSize()) {
                resize(this.sliceSize + 1);
            }
            this.addDigit(carry, i + 1);
        }

    }

    private int minValue() {
        if (ebm.isEmpty()) {
            return 0;
        }

        RoaringBitmap minValuesId = ebm;
        for (int i = slices.length - 1; i >= 0; i -= 1) {
            RoaringBitmap tmp = RoaringBitmap.andNot(minValuesId, slices[i]);
            if (!tmp.isEmpty()) {
                minValuesId = tmp;
            }
        }

        return getValueInternal(minValuesId.first());
    }

    private int maxValue() {
        if (ebm.isEmpty()) {
            return 0;
        }

        RoaringBitmap maxValuesId = ebm;
        for (int i = slices.length - 1; i >= 0; i -= 1) {
            RoaringBitmap tmp = RoaringBitmap.and(maxValuesId, slices[i]);
            if (!tmp.isEmpty()) {
                maxValuesId = tmp;
            }
        }

        return getValueInternal(maxValuesId.first());
    }



    public void runOptimize() {
        this.ebm.runOptimize();

        for (RoaringBitmap integers : this.slices) {
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
        WritableUtils.writeVInt(output, this.slices.length);
        for (RoaringBitmap rb : this.slices) {
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
        this.slices = ba;
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
        buffer.putInt(this.slices.length);
        for (RoaringBitmap rb : this.slices) {
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
        this.slices = ba;
    }

    @Override
    public int serializedSizeInBytes() {
        int size = 0;
        for (RoaringBitmap rb : this.slices) {
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

        int bitDepth = Integer.max(this.sliceSize(), otherBsi.sliceSize());
        RoaringBitmap[] newBA = new RoaringBitmap[bitDepth];
        for (int i = 0; i < bitDepth; i++) {
            RoaringBitmap current = i < this.slices.length ? this.slices[i] : new RoaringBitmap();
            RoaringBitmap other = i < otherBsi.slices.length ? otherBsi.slices[i] : new RoaringBitmap();
            newBA[i] = RoaringBitmap.or(current, other);
            if (this.runOptimized || otherBsi.runOptimized) {
                newBA[i].runOptimize();
            }
        }
        this.slices = newBA;
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
        RoaringBitmap[] cloneBA = new RoaringBitmap[this.sliceSize()];
        for (int i = 0; i < cloneBA.length; i++) {
            cloneBA[i] = this.slices[i].clone();
        }
        bitSliceIndex.slices = cloneBA;
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

        Long sum = IntStream.range(0, this.sliceSize())
                .mapToLong(x -> (long) (1 << x) * RoaringBitmap.andCardinality(this.slices[x], foundSet))
                .sum();

        return Pair.newPair(sum, count);
    }

    //------------------------------------------------------------------------------------------

    /**
     * 调整切片个数
     */
    private void resize(int newSliceSize) {
        if (newSliceSize <= this.sliceSize) {
            // 小于等于之前切片个数不需要调整
            return;
        }
        RoaringBitmap[] newSlices = new RoaringBitmap[newSliceSize];
        // 复制旧切片
        if (this.sliceSize != 0) {
            System.arraycopy(this.slices, 0, newSlices, 0, this.sliceSize);
        }
        // 增加新切片
        for (int i = newSliceSize - 1; i >= this.sliceSize; i--) {
            newSlices[i] = new RoaringBitmap();
            if (this.runOptimized) {
                newSlices[i].runOptimize();
            }
        }
        this.slices = newSlices;
        this.sliceSize = newSliceSize;
    }

    /**
     * 设置值
     * @param key
     * @param value
     */
    private void putValueInternal(int key, int value) {
        // 为 value 的每个切片 bitmap 添加 x
        for (int i = 0; i < this.sliceSize(); i += 1) {
            if ((value & (1 << i)) > 0) {
                this.slices[i].add(key);
            } else {
                this.slices[i].remove(key);
            }
        }
        this.ebm.add(key);
    }

    /**
     * 获取指定 Key 关联的 value
     * @param key
     * @return
     */
    private int getValueInternal(int key) {
        int value = 0;
        for (int i = 0; i < this.sliceSize(); i += 1) {
            if (this.slices[i].contains(key)) {
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

        for (int i = this.sliceSize() - 1; i >= 0; i--) {
            int bit = (value >> i) & 1;
            if (bit == 1) {
                LT = RoaringBitmap.or(LT, RoaringBitmap.andNot(EQ, this.slices[i]));
                EQ = RoaringBitmap.and(EQ, this.slices[i]);
            } else {
                GT = RoaringBitmap.or(GT, RoaringBitmap.and(EQ, this.slices[i]));
                EQ = RoaringBitmap.andNot(EQ, this.slices[i]);
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
