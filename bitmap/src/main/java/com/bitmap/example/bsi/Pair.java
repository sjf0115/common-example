package com.bitmap.example.bsi;

import java.io.Serializable;

/**
 * 功能：Pair-键值对
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/6/16 00:29
 */
public class Pair<T1, T2> implements Serializable {
    private static final long serialVersionUID = -3986244606585552569L;

    // 左边元素
    protected T1 left = null;
    // 右边元素
    protected T2 right = null;

    public Pair() {
    }

    public Pair(T1 a, T2 b) {
        this.left = a;
        this.right = b;
    }

    public static <T1, T2> Pair<T1, T2> newPair(T1 a, T2 b) {
        return new Pair<T1, T2>(a, b);
    }

    private static boolean equals(Object x, Object y) {
        return (x == null && y == null) || (x != null && x.equals(y));
    }

    // 获取左边元素
    public T1 getLeft() {
        return left;
    }

    // 获取右边元素
    public T2 getRight() {
        return right;
    }

    // 获取键-左边元素
    public T1 getKey() {
        return left;
    }

    // 获取值-右边元素
    public T2 getValue() {
        return right;
    }

    // 赋值左边元素
    public void setFirst(T1 a) {
        this.left = a;
    }

    // 赋值右边元素
    public void setSecond(T2 b) {
        this.right = b;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object other) {
        return other instanceof Pair && equals(left, ((Pair) other).left) && equals(right, ((Pair) other).right);
    }

    @Override
    public int hashCode() {
        if (left == null)
            return (right == null) ? 0 : right.hashCode() + 1;
        else if (right == null)
            return left.hashCode() + 2;
        else
            return left.hashCode() * 17 + right.hashCode();
    }

    @Override
    public String toString() {
        return "{" + getLeft() + "," + getRight() + "}";
    }
}
