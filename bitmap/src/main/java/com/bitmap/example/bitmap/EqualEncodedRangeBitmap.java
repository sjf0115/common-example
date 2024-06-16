package com.bitmap.example.bitmap;

import org.roaringbitmap.RoaringBitmap;

/**
 * 功能：Equal-Encode RangeBitmap
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/6/16 14:18
 */
public class EqualEncodedRangeBitmap {
    private int maxValue;
    private int minValue;
    private RoaringBitmap[] rbm;

    public EqualEncodedRangeBitmap(int minValue, int maxValue) {
        if (minValue < 0) {
            throw new IllegalArgumentException("Values should be non-negative");
        }
        // Bitmap个数
        int size = maxValue;
        this.rbm = new RoaringBitmap[size];
        for (int i = 0; i < size; i++) {
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
    public RoaringBitmap between(int minValue, int maxValue) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = minValue-1; i < maxValue; i += 1) {
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 等于
    public RoaringBitmap eq(int value) {
        return this.rbm[value-1];
    }

    // 不等于
    public RoaringBitmap neq(int value) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = 0; i < this.size(); i += 1) {
            if (i != value-1) {
                continue;
            }
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 小于
    public RoaringBitmap lt(int value) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = 0; i < value; i += 1) {
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 小于等于
    public RoaringBitmap lte(int value) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = 0; i <= value; i += 1) {
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 大于
    public RoaringBitmap gt(int value) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = value+1; i < this.size(); i += 1) {
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 大于等于
    public RoaringBitmap gte(int value) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = value; i < this.size(); i += 1) {
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }

    // 分配Bitmap空间
    private void ensureCapacityInternal(int minValue, int maxValue) {
        if (rbm == null || rbm.length == 0) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            grow(maxValue);
        } else if (this.minValue > minValue) {
            this.minValue = minValue;
        } else if (this.maxValue < maxValue) {
            this.maxValue = maxValue;
            grow(maxValue);
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
        rbm[value].add(index);
    }

    private int valueAt(int index) {
        int value = 0;
        for (int i = 0; i < this.size(); i += 1) {
            // 查找位于哪个Bitmap中 位于Bitmap数组的下标即为value
            if (this.rbm[i].contains(index)) {
                value |= (1 << i);
            }
        }
        return value;
    }

    private RoaringBitmap computeRange(int minValue, int maxValue, boolean negate) {
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = minValue; i <= maxValue; i += 1) {
            if (negate) {
                continue;
            }
            bitmap.or(this.rbm[i]);
        }
        return bitmap;
    }
}
