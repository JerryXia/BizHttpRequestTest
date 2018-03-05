/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import java.util.ArrayList;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecordMemoryStorage implements HttpRequestRecordStorage {
    private static final int          DEFAULT_CAPACITY = 1024;
    private final int                 capacity;
    private final int                 mask;
    private final HttpRequestRecord[] buffer;

    private volatile long tail = 0;

    public HttpRequestRecordMemoryStorage() {
        this.capacity = findNextPositivePowerOfTwo(DEFAULT_CAPACITY);
        this.mask = this.capacity - 1;
        this.buffer = new HttpRequestRecord[this.capacity];
    }

    private int findNextPositivePowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    @Override
    public boolean save(HttpRequestRecord record) {
        final long currentTail = tail;
        buffer[(int) (currentTail & mask)] = record;
        tail = currentTail + 1;
        return true;
    }

    @Override
    public HttpRequestRecordStorageQueryResult queryAll() {
        final long currentTail = tail;
        ArrayList<HttpRequestRecord> list = new ArrayList<HttpRequestRecord>();
        if (currentTail >= capacity) {
            int index = (int) (currentTail & mask);
            if (index == 0) {
                for (int i = 0; i < capacity; i++) {
                    list.add(buffer[i]);
                }
            } else if (index == 1) {
                for (int i = index; i < capacity; i++) {
                    list.add(buffer[i]);
                }
                list.add(buffer[0]);
            } else {
                for (int i = index; i < capacity; i++) {
                    list.add(buffer[i]);
                }
                for (int i = 0; i < index; i++) {
                    list.add(buffer[i]);
                }
            }
        } else {
            for (int i = 0; i < currentTail; i++) {
                list.add(buffer[i]);
            }
        }
        HttpRequestRecordStorageQueryResult result = new HttpRequestRecordStorageQueryResult(currentTail, list);
        return result;
    }

    @Override
    public HttpRequestRecordStorageQueryResult queryNextList(long startIndex, long endIndex) {
        final long currentTail = tail;

        HttpRequestRecordStorageQueryResult result = null;
        ArrayList<HttpRequestRecord> list = new ArrayList<HttpRequestRecord>();
        if(endIndex <= startIndex) {
            result = null;
        } else {
            if (currentTail <= startIndex) {
                result = new HttpRequestRecordStorageQueryResult(currentTail, list);
            } else if(currentTail - startIndex <= this.capacity) {
                if (currentTail > endIndex) {
                    // 以endIndex坐标结束
                    for (long i = startIndex; i < endIndex; i++) {
                        int index = (int) (i & mask);
                        list.add(buffer[index]);
                    }
                    result = new HttpRequestRecordStorageQueryResult(endIndex, list);
                } else {
                    // 以currentTail坐标结束
                    for (long i = startIndex; i < currentTail; i++) {
                        int index = (int) (i & mask);
                        list.add(buffer[index]);
                    }
                    result = new HttpRequestRecordStorageQueryResult(currentTail, list);
                }
            } else {
                // 初始数据已被覆盖
                if(currentTail - endIndex > this.capacity) {
                    // 整个段的数据都被覆盖
                    result = new HttpRequestRecordStorageQueryResult(currentTail, list);
                } else {
                    for (long i = (currentTail - this.capacity); i < endIndex; i++) {
                        int index = (int) (i & mask);
                        list.add(buffer[index]);
                    }
                    result = new HttpRequestRecordStorageQueryResult(endIndex, list);
                }
            }
        }
        return result;
    }

    public int getCapacity() {
        return capacity;
    }
    public int getMask() {
        return mask;
    }
    public HttpRequestRecord[] getBuffer() {
        return buffer;
    }
    public long getTail() {
        return tail;
    }
}
