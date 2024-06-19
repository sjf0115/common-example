package com.bitmap.example.bsi.intint;

import com.bitmap.example.bsi.core.Operation;
import com.bitmap.example.bsi.core.Pair;
import org.junit.jupiter.api.*;
import org.roaringbitmap.RoaringBitmap;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * RBBsiTest
 * created by haihuang@alibaba-inc.com on 2021/6/6
 */
public class Rbm32SliceIndexTest {
    private Map<Integer, Integer> testDataSet = new HashMap<>();

    private Rbm32SliceIndex bsi;

    @BeforeEach
    public void setup() {
        IntStream.range(1, 100).forEach(x -> testDataSet.put(x, x));
        bsi = new Rbm32SliceIndex(1, 99);
        testDataSet.forEach((k, v) -> {
            bsi.setValue(k, v);
        });
    }

    @Test
    public void testSetAndGet() {
        IntStream.range(1, 100).forEach(x -> {
            int value = bsi.getValue(x);
            Assertions.assertTrue(value == x);
        });
    }

    @Test
    public void testMerge() {
//        Rbm32SliceIndex bsiA = new Rbm32SliceIndex();
//        IntStream.range(1, 100).forEach(x -> bsiA.setValue(x, x));
//        Rbm32SliceIndex bsiB = new Rbm32SliceIndex();
//        IntStream.range(100, 199).forEach(x -> bsiB.setValue(x, x));
//        Assertions.assertEquals(bsiA.getExistenceBitmap().getLongCardinality(), 99);
//        Assertions.assertEquals(bsiB.getExistenceBitmap().getLongCardinality(), 99);
//        bsiA.merge(bsiB);
//        IntStream.range(1, 199).forEach(x -> {
//            Pair<Integer, Boolean> bsiValue = bsiA.getValue(x);
//            Assertions.assertTrue(bsiValue.getRight());
//            Assertions.assertEquals((int) bsiValue.getKey(), x);
//        });
    }


    @Test
    public void testClone() {
//        Rbm32SliceIndex bsi = new Rbm32SliceIndex(1, 99);
//        List<Pair<Integer, Integer>> collect = testDataSet.entrySet()
//                .stream().map(x -> Pair.newPair(x.getKey(), x.getValue())).collect(Collectors.toList());
//
//        bsi.setValues(collect);
//
//        Assertions.assertEquals(bsi.getExistenceBitmap().getLongCardinality(), 99);
//        final Rbm32SliceIndex clone = bsi.clone();
//
//        IntStream.range(1, 100).forEach(x -> {
//            Pair<Integer, Boolean> bsiValue = clone.getValue(x);
//            Assertions.assertTrue(bsiValue.getRight());
//            Assertions.assertEquals((int) bsiValue.getKey(), x);
//        });
    }


    @Test
    public void testAdd() {
        Rbm32SliceIndex bsiA = new Rbm32SliceIndex();
        IntStream.range(1, 100).forEach(x -> bsiA.setValue(x, x));
        Rbm32SliceIndex bsiB = new Rbm32SliceIndex();
        IntStream.range(1, 120).forEach(x -> bsiB.setValue(x, x));

        bsiA.add(bsiB);

        IntStream.range(1, 120).forEach(x -> {
            int value = bsiA.getValue(x);
            if (x < 100) {
                Assertions.assertEquals(value, x * 2);
            } else {
                Assertions.assertEquals(value, x);
            }

        });
    }

    @Test
    public void testAddAndEvaluate() {
        Rbm32SliceIndex bsiA = new Rbm32SliceIndex();
        IntStream.range(1, 100).forEach(x -> bsiA.setValue(x, x));
        Rbm32SliceIndex bsiB = new Rbm32SliceIndex();
        IntStream.range(1, 120).forEach(x -> bsiB.setValue(120 - x, x));

        bsiA.add(bsiB);

        RoaringBitmap result = bsiA.compare(Operation.EQ, 120);
        Assertions.assertTrue(result.getLongCardinality() == 99);
        Assertions.assertArrayEquals(result.toArray(), IntStream.range(1, 100).toArray());

        result = bsiA.compareRange(1, 20);
        Assertions.assertTrue(result.getLongCardinality() == 20);
        Assertions.assertArrayEquals(result.toArray(), IntStream.range(100, 120).toArray());
    }


    @Test
    public void TestIO4Stream() throws IOException {
//        Rbm32SliceIndex bsi = new Rbm32SliceIndex(1, 99);
//        IntStream.range(1, 100).forEach(x -> bsi.setValue(x, x));
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        DataOutputStream bdo = new DataOutputStream(bos);
//        bsi.serialize(bdo);
//        byte[] data = bos.toByteArray();
//
//        Rbm32SliceIndex newBsi = new Rbm32SliceIndex();
//
//        ByteArrayInputStream bis = new ByteArrayInputStream(data);
//        DataInputStream bdi = new DataInputStream(bis);
//        newBsi.deserialize(bdi);
//
//        Assertions.assertEquals(newBsi.getExistenceBitmap().getLongCardinality(), 99);
//
//        IntStream.range(1, 100).forEach(x -> {
//            Pair<Integer, Boolean> bsiValue = newBsi.getValue(x);
//            Assertions.assertTrue(bsiValue.getRight());
//            Assertions.assertEquals((int) bsiValue.getKey(), x);
//        });
    }

    @Test
    public void testIO4Buffer() throws IOException {
//        Rbm32SliceIndex bsi = new Rbm32SliceIndex(1, 99);
//        IntStream.range(1, 100).forEach(x -> bsi.setValue(x, x));
//        ByteBuffer buffer = ByteBuffer.allocate(bsi.serializedSizeInBytes());
//        bsi.serialize(buffer);
//
//        byte[] data = buffer.array();
//        Rbm32SliceIndex newBsi = new Rbm32SliceIndex();
//        newBsi.deserialize(ByteBuffer.wrap(data));
//        Assertions.assertEquals(newBsi.getExistenceBitmap().getLongCardinality(), 99);
//
//        IntStream.range(1, 100).forEach(x -> {
//            Pair<Integer, Boolean> bsiValue = newBsi.getValue(x);
//            Assertions.assertTrue(bsiValue.getRight());
//            Assertions.assertEquals((int) bsiValue.getKey(), x);
//        });
    }


    @Test
    public void testIOFromExternal() {
        Rbm32SliceIndex bsi = new Rbm32SliceIndex(1, 99);
        IntStream.range(1, 100).forEach(x -> bsi.setValue(x, x));

        IntStream.range(1, 100).forEach(x -> {
            int value = bsi.getValue(x);
            Assertions.assertEquals(value, x);
        });
    }


    @Test
    public void testEQ() {
        Rbm32SliceIndex bsi = new Rbm32SliceIndex(1, 99);
        IntStream.range(1, 100).forEach(x -> {
            if (x <= 50) {
                bsi.setValue(x, 1);
            } else {
                bsi.setValue(x, x);
            }

        });

        RoaringBitmap bitmap = bsi.compare(Operation.EQ, 1);
        Assertions.assertTrue(bitmap.getLongCardinality() == 50L);

    }

    @Test
    public void testNotEQ() {
        bsi = new Rbm32SliceIndex();
        bsi.setValue(1, 99);
        bsi.setValue(2, 1);
        bsi.setValue(3, 50);

        RoaringBitmap result = bsi.compare(Operation.NEQ, 99);
        Assertions.assertTrue(result.getLongCardinality() == 2);
        Assertions.assertArrayEquals(new int[]{2, 3}, result.toArray());

        result = bsi.compare(Operation.NEQ, 100);
        Assertions.assertTrue(result.getLongCardinality() == 3);
        Assertions.assertArrayEquals(new int[]{1, 2, 3}, result.toArray());

        bsi = new Rbm32SliceIndex();
        bsi.setValue(1, 99);
        bsi.setValue(2, 99);
        bsi.setValue(3, 99);

        result = bsi.compare(Operation.NEQ, 99);
        Assertions.assertTrue(result.isEmpty());

        result = bsi.compare(Operation.NEQ, 1);
        Assertions.assertTrue(result.getLongCardinality() == 3);
        Assertions.assertArrayEquals(new int[]{1, 2, 3}, result.toArray());
    }


    // parallel operation test

    @Test
    public void testGT() {
        RoaringBitmap result = bsi.compare(Operation.GT, 50);
        Assertions.assertTrue(result.getLongCardinality() == 49);
        Assertions.assertArrayEquals(IntStream.range(51, 100).toArray(), result.toArray());

        result = bsi.compare(Operation.GT, 0);
        Assertions.assertTrue(result.getLongCardinality() == 99);
        Assertions.assertArrayEquals(IntStream.range(1, 100).toArray(), result.toArray());

        result = bsi.compare(Operation.GT, 99);
        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    public void testGE() {
        RoaringBitmap result = bsi.compare(Operation.GE, 50);
        Assertions.assertTrue(result.getLongCardinality() == 50);
        Assertions.assertArrayEquals(IntStream.range(50, 100).toArray(), result.toArray());

        result = bsi.compare(Operation.GE, 1);
        Assertions.assertTrue(result.getLongCardinality() == 99);
        Assertions.assertArrayEquals(IntStream.range(1, 100).toArray(), result.toArray());

        result = bsi.compare(Operation.GE, 100);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testLT() {
        RoaringBitmap result = bsi.compare(Operation.LT, 50);
        Assertions.assertTrue(result.getLongCardinality() == 49);
        Assertions.assertArrayEquals(IntStream.range(1, 50).toArray(), result.toArray());

        result = bsi.compare(Operation.LT, Integer.MAX_VALUE);
        Assertions.assertTrue(result.getLongCardinality() == 99);
        Assertions.assertArrayEquals(IntStream.range(1, 100).toArray(), result.toArray());

        result = bsi.compare(Operation.LT, 1);
        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    public void testLE() {
        RoaringBitmap result = bsi.compare(Operation.LE, 50);
        Assertions.assertTrue(result.getLongCardinality() == 50);
        Assertions.assertArrayEquals(IntStream.range(1, 51).toArray(), result.toArray());

        result = bsi.compare(Operation.LE, Integer.MAX_VALUE);
        Assertions.assertTrue(result.getLongCardinality() == 99);
        Assertions.assertArrayEquals(IntStream.range(1, 100).toArray(), result.toArray());

        result = bsi.compare(Operation.LE, 0);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testRANGE() {
        RoaringBitmap result = bsi.compareRange(10, 20);
        Assertions.assertTrue(result.getLongCardinality() == 11);
        Assertions.assertArrayEquals(IntStream.range(10, 21).toArray(), result.toArray());

        result = bsi.compareRange(1, 200);
        Assertions.assertTrue(result.getLongCardinality() == 99);
        Assertions.assertArrayEquals(IntStream.range(1, 100).toArray(), result.toArray());

        result = bsi.compareRange(1000, 2000);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testSum() {
        Rbm32SliceIndex bsi = new Rbm32SliceIndex(1, 99);
        IntStream.range(1, 100).forEach(x -> bsi.setValue(x, x));

        RoaringBitmap foundSet = RoaringBitmap.bitmapOf(IntStream.range(1, 51).toArray());

        Pair<Long, Long> sumPair = bsi.sum(foundSet);

        System.out.println("sum:" + sumPair.toString());

        int sum = IntStream.range(1, 51).sum();
        long count = IntStream.range(1, 51).count();

        Assertions.assertTrue(sumPair.getLeft().intValue() == sum && sumPair.getRight() == count);
    }

    @Test
    public void testValueZero() {
        bsi = new Rbm32SliceIndex();
        bsi.setValue(0, 0);
        bsi.setValue(1, 0);
        bsi.setValue(2, 1);

        RoaringBitmap result = bsi.compare(Operation.EQ, 0);
        Assertions.assertTrue(result.getLongCardinality() == 2);
        Assertions.assertArrayEquals(new int[]{0, 1}, result.toArray());

        result = bsi.compare(Operation.EQ, 1);
        Assertions.assertTrue(result.getLongCardinality() == 1);
        Assertions.assertArrayEquals(new int[]{2}, result.toArray());
    }
}

