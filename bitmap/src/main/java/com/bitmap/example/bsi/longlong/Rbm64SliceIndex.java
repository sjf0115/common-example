package com.bitmap.example.bsi.longlong;

import com.bitmap.example.bsi.core.Pair;
import org.roaringbitmap.longlong.Roaring64NavigableMap;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * 功能：64位 RBM
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/6/16 01:18
 */
public class Rbm64SliceIndex implements Bitmap64SliceIndex {

    private long maxValue;
    private long minValue;
    // Bitmap 切片
    private Roaring64NavigableMap[] rbmSlices;
    // 判断 Bitmap 是否有值
    private Roaring64NavigableMap ebm;
    private Boolean runOptimized = false;


    public Rbm64SliceIndex(long minValue, long maxValue) {
        if (minValue < 0) {
            throw new IllegalArgumentException("Values should be non-negative");
        }
        // 切片个数 - 最大值二进制位数
        int sliceSize = 64 - Long.numberOfLeadingZeros(maxValue);
        this.rbmSlices = new Roaring64NavigableMap[sliceSize];
        for (int i = 0; i < sliceSize; i++) {
            this.rbmSlices[i] = new Roaring64NavigableMap();
        }
        this.ebm = new Roaring64NavigableMap();
    }

    // Bit位数
    @Override
    public int bitCount() {
        return this.rbmSlices.length;
    }

    // 基数
    @Override
    public long getLongCardinality() {
        return this.ebm.getLongCardinality();
    }

    // 设置值
    @Override
    public void setValue(long index, long value) {
        ensureCapacityInternal(value, value);
        setValueInternal(index, value);
    }

    // 获取值
    @Override
    public Pair<Long, Boolean> getValue(long index) {
        boolean exists = this.ebm.contains(index);
        if (!exists) {
            return Pair.newPair(0L, false);
        }

        return Pair.newPair(valueAt(index), true);
    }

    // 设置多个值
    @Override
    public void setValues(List<Pair<Long, Long>> values) {

    }

    @Override
    public void serialize(ByteBuffer buffer) throws IOException {

    }

    @Override
    public void serialize(DataOutput output) throws IOException {

    }

    @Override
    public int serializedSizeInBytes() {
        return 0;
    }

    // 切片空间
    private void ensureCapacityInternal(Long minValue, Long maxValue) {
        if (ebm.isEmpty()) {
            // 初始化
            this.minValue = minValue;
            this.maxValue = maxValue;
            grow(Long.toBinaryString(maxValue).length());
        } else if (this.minValue > minValue) {
            this.minValue = minValue;
        } else if (this.maxValue < maxValue) {
            // 如果新的最大值大于当前最大值则需要增加切片
            this.maxValue = maxValue;
            grow(Long.toBinaryString(maxValue).length());
        }
    }

    // 增加切片Bitmap
    private void grow(int newBitNum) {
        // 现在Bit位
        int bitNum = this.rbmSlices.length;
        if (bitNum >= newBitNum) {
            // 不需要增加
            return;
        }

        Roaring64NavigableMap[] newRbmSlices = new Roaring64NavigableMap[newBitNum];
        if (bitNum != 0) {
            System.arraycopy(this.rbmSlices, 0, newRbmSlices, 0, bitNum);
        }

        for (int i = newBitNum - 1; i >= bitNum; i--) {
            newRbmSlices[i] = new Roaring64NavigableMap();
            if (this.runOptimized) {
                newRbmSlices[i].runOptimize();
            }
        }
        this.rbmSlices = newRbmSlices;
    }

    // 设置值
    private void setValueInternal(Long index, Long value) {
        for (int i = 0; i < this.bitCount(); i += 1) {
            if ((value & (1 << i)) > 0) {
                this.rbmSlices[i].add(index);
            } else {
                this.rbmSlices[i].removeLong(index);
            }
        }
        this.ebm.add(index);
    }

    private long valueAt(long columnId) {
        long value = 0;
        for (int i = 0; i < this.bitCount(); i += 1) {
            if (this.rbmSlices[i].contains(columnId)) {
                value |= (1 << i);
            }
        }

        return value;
    }
}
