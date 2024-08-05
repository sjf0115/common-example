package com.bitmap.example.bsi.longlong;

import com.bitmap.example.bsi.core.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

/**
 * 功能：
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/6/16 01:27
 */
public class Rbm64SliceIndexTest {
    private Map<Long, Long> randomValueSet = new HashMap<>();
    private Rbm64SliceIndex bsi;

    @BeforeEach
    public void setup() {
        LongStream.range(1L, 1000L).forEach(x -> randomValueSet.put(x, x));
        bsi = new Rbm64SliceIndex(1L, 1000L);
        randomValueSet.forEach((k, v) -> {
            System.out.println("k: " + k + ", v: " + v);
            bsi.setValue(k, v);
        });
    }

    @Test
    public void testSetAndGet() {
        LongStream.range(1L, 1000L).forEach(x -> {
            Pair<Long, Boolean> pair = bsi.getValue(x);
            System.out.println(pair.getKey() + ":" + pair.getValue());
            Assertions.assertTrue(pair.getRight());
            Assertions.assertTrue(pair.getKey() == x);
        });
    }
}
