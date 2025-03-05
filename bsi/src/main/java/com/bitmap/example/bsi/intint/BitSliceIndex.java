package com.bitmap.example.bsi.intint;

import com.bitmap.example.bsi.core.Operation;
import org.roaringbitmap.RoaringBitmap;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;

public interface BitSliceIndex {
    int sliceSize();
    long getLongCardinality();
    void clear();
    boolean isEmpty();
    void put(int key, int value);
    void putAll(BitSliceIndex otherBsi);
    int get(int key);
    boolean containsKey(int key);
    boolean containsValue(int value);
    int remove(int key);
    boolean remove(int key, int value);
    Collection<Integer> keys();
    Collection<Integer> values();
    int replace(int key, int value);
    int replace(int key, int oldValue, int newValue);


    RoaringBitmap compare(Operation operation, int value);
    RoaringBitmap compareRange(int start, int end);

    void serialize(ByteBuffer buffer) throws IOException;
    void serialize(DataOutput output) throws IOException;
    void deserialize(DataInput in) throws IOException;
    void deserialize(ByteBuffer buffer) throws IOException;
    int serializedSizeInBytes();

}
