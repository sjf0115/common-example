package com.bitmap.example.bsi.longlong;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 功能：
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/6/16 10:21
 */
public class Test {

    private static List<Long> scores = Lists.newArrayList(48L, 80L, 75L, 19L, 1L, 57L, 63L, 22L, 96L, 34L);

    public static void main(String[] args) {
        long maxValue = 392;
        int sliceSize = 64 - Long.numberOfLeadingZeros(maxValue);
        System.out.println(sliceSize);

        for (Long score : scores) {
            String binary = Long.toBinaryString(score);
            System.out.println(score + ": " + binary);
        }
    }
}
