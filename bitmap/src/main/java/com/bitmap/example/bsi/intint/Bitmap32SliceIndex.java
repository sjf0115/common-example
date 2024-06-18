package com.bitmap.example.bsi.intint;

import com.bitmap.example.bsi.Pair;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public interface Bitmap32SliceIndex {
    int bitCount();

    long getLongCardinality();


    /**
     * set value for bsi, setValue will set each bit slice according to the input value
     * given that we have bsi as follow
     * ebm:  RB[1 1 1 1]
     * slice0: RB[0 1 0 1]   -
     * slice1: RB[1 0 1 0]  |
     * slice2: RB[1 0 0 1]  |--bA:bit slice Array
     * slice3: RB[0 0 0 1]   -
     *      1 2 3 4
     *      |
     *      ---------------- columnId or rowId
     *  for columnId 1, the value is 110 that is :6
     *  for columnId 2, the value is 1 that is :1
     *  for columnId 3, the value is 10 that is :2
     *  for columnId 4, the value is 1101 that is :11
     *
     * @param columnId   columnId or rowId
     * @param value    value for this columnId or rowId
     */
    void setValue(int columnId, int value);

    /**
     *
     * @param columnId columnId or rowId
     * @return the value of this columnId
     */
    Pair<Integer, Boolean> getValue(int columnId);

    /**
     * Set a batch of values.
     * @param values
     */
    void setValues(List<Pair<Integer, Integer>> values);

    void serialize(ByteBuffer buffer) throws IOException;

    void serialize(DataOutput output) throws IOException;

    int serializedSizeInBytes();
}
