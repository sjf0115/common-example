package com.bitmap.example.bitmap;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.roaringbitmap.RoaringBitmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

/**
 * 功能：
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/6/16 21:07
 */
public class EqualEncodedRangeBitmapTest {
    private List<Integer> scores = Lists.newArrayList(48, 80, 75, 19, 1, 57, 63, 22, 96, 34);
    private Map<Integer, Integer> userScore = new HashMap<>();
    private EqualEncodedRangeBitmap rbm;

    @BeforeEach
    public void setup() {
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;
        for (int i = 0;i < scores.size();i++) {
            int score = scores.get(i);
            userScore.put(i+1, score);
            if (score > maxValue) {
                maxValue = score;
            }
            if (score < minValue) {
                minValue = score;
            }
        }
        rbm = new EqualEncodedRangeBitmap(minValue, maxValue);
        userScore.forEach((k, v) -> {
            rbm.setValue(k, v);
        });
    }

    @Test
    public void testSetAndGet() {
        IntStream.range(1, 11).forEach(index -> {
            int value = rbm.getValue(index);
            System.out.println("user: " + index + ", score: " + value);
        });
    }

    @Test
    public void testOperationBetween() {
        int minValue = 10;
        int maxValue = 50;
        RoaringBitmap bitmap = rbm.between(minValue, maxValue);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationEq() {
        int value = 19;
        RoaringBitmap bitmap = rbm.eq(value);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationNeq() {
        int value = 19;
        RoaringBitmap bitmap = rbm.neq(value);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationLt() {
        int value = 57;
        RoaringBitmap bitmap = rbm.lt(value);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationLte() {
        int value = 57;
        RoaringBitmap bitmap = rbm.lte(value);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationGt() {
        int value = 57;
        RoaringBitmap bitmap = rbm.gt(value);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationGte() {
        int value = 57;
        RoaringBitmap bitmap = rbm.gte(value);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }
}
