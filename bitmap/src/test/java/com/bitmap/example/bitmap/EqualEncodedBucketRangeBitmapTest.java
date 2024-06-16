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
 * 功能：EqualEncodedBucketRangeBitmap
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/6/16 21:07
 */
public class EqualEncodedBucketRangeBitmapTest {
    private List<Integer> scores = Lists.newArrayList(48, 80, 75, 19, 1, 57, 63, 22, 96, 34);
    private Map<Integer, Integer> userScore = new HashMap<>();
    private EqualEncodedBucketRangeBitmap rbm;

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
        rbm = new EqualEncodedBucketRangeBitmap(minValue, maxValue);
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
        int minBucketIndex = 1;
        int maxBucketIndex = 5;
        RoaringBitmap bitmap = rbm.between(minBucketIndex, maxBucketIndex);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationEq() {
        int bucketIndex = 1;
        RoaringBitmap bitmap = rbm.eq(bucketIndex);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationNeq() {
        int bucketIndex = 1;
        RoaringBitmap bitmap = rbm.neq(bucketIndex);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationLt() {
        int bucketIndex = 5;
        RoaringBitmap bitmap = rbm.lt(bucketIndex);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationLte() {
        int bucketIndex = 5;
        RoaringBitmap bitmap = rbm.lte(bucketIndex);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationGt() {
        int bucketIndex = 5;
        RoaringBitmap bitmap = rbm.gt(bucketIndex);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }

    @Test
    public void testOperationGte() {
        int bucketIndex = 5;
        RoaringBitmap bitmap = rbm.gte(bucketIndex);
        PrimitiveIterator.OfInt ite = bitmap.stream().iterator();
        while (ite.hasNext()) {
            Integer index = ite.next();
            System.out.println("User " + index);
        }
    }
}
