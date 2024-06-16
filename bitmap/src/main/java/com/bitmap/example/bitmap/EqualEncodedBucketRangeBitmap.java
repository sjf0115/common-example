package com.bitmap.example.bitmap;

import org.roaringbitmap.RoaringBitmap;

/**
 * 功能：Equal-Encode 分桶 RangeBitmap
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/6/16 14:18
 */
public class EqualEncodedBucketRangeBitmap {
    private int maxValue;
    private int minValue;
    private RoaringBitmap[] rbm;

    public EqualEncodedBucketRangeBitmap(int minValue, int maxValue) {
        if (minValue < 0) {
            throw new IllegalArgumentException("Values should be non-negative");
        }
        // 分桶个数
        int bucketSize = bucketIndex(maxValue) + 1;
        this.rbm = new RoaringBitmap[bucketSize];
        for (int i = 0; i < bucketSize; i++) {
            this.rbm[i] = new RoaringBitmap();
        }
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public void setValue(int index, int value) {
        ensureCapacityInternal(value, value);
        setValueInternal(index, value);
    }

    public int getValue(int index) {
        return valueAt(index);
    }

    public int size() {
        return this.rbm.length;
    }

    // between
    public RoaringBitmap between(int minBucketIndex, int maxBucketIndex) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = minBucketIndex; i <= maxBucketIndex; i += 1) {
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 等于
    public RoaringBitmap eq(int bucketIndex) {
        return this.rbm[bucketIndex];
    }

    // 不等于
    public RoaringBitmap neq(int bucketIndex) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = 0; i < this.size(); i += 1) {
            if (i == bucketIndex) {
                continue;
            }
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 小于
    public RoaringBitmap lt(int bucketIndex) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = 0; i < bucketIndex; i += 1) {
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 小于等于
    public RoaringBitmap lte(int bucketIndex) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = 0; i <= bucketIndex; i += 1) {
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 大于
    public RoaringBitmap gt(int bucketIndex) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = bucketIndex+1; i < this.size(); i += 1) {
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 大于等于
    public RoaringBitmap gte(int bucketIndex) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = bucketIndex; i < this.size(); i += 1) {
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 分配Bitmap空间
    private void ensureCapacityInternal(int minValue, int maxValue) {
        if (rbm == null || rbm.length == 0) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            grow(bucketIndex(maxValue));
        } else if (this.minValue > minValue) {
            this.minValue = minValue;
        } else if (this.maxValue < maxValue) {
            this.maxValue = maxValue;
            grow(bucketIndex(maxValue));
        }
    }

    // 增大容量
    private void grow(int capacity) {
        // 现有容量
        int oldCapacity = this.rbm.length;
        if (oldCapacity >= capacity) {
            // 不需要增加
            return;
        }

        RoaringBitmap[] newRbm = new RoaringBitmap[capacity];
        if (oldCapacity != 0) {
            System.arraycopy(this.rbm, 0, capacity, 0, oldCapacity);
        }
        for (int i = capacity - 1; i >= oldCapacity; i--) {
            newRbm[i] = new RoaringBitmap();
        }
        this.rbm = newRbm;
    }

    // 设置值
    private void setValueInternal(int index, int value) {
        rbm[bucketIndex(value)].add(index);
    }

    private int valueAt(int index) {
        for (int i = 0; i < this.size(); i += 1) {
            // 查找位于哪个Bitmap中 位于Bitmap数组的下标即为value
            if (this.rbm[i].contains(index)) {
                return i;
            }
        }
        return -1;
    }

    private int bucketIndex(int value) {
        return value / 10;
    }
}
