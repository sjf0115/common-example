package com.bitmap.example.bsi.intint;

import com.bitmap.example.bsi.core.Operation;
import com.bitmap.example.bsi.core.Pair;
import org.roaringbitmap.RoaringBitmap;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public interface Bitmap32SliceIndex {
    int bitCount();
    long getLongCardinality();
    void setValue(int index, int value);
    void setValues(List<Pair<Integer, Integer>> values);
    int getValue(int index);
    boolean valueExist(int index);

    RoaringBitmap compare(Operation operation, int value);
    RoaringBitmap compareRange(int start, int end);

    void serialize(ByteBuffer buffer) throws IOException;
    void serialize(DataOutput output) throws IOException;
    void deserialize(DataInput in) throws IOException;
    void deserialize(ByteBuffer buffer) throws IOException;
    int serializedSizeInBytes();

}
