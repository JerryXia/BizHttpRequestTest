/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Administrator
 *
 */
class HttpRequestRecordMemoryStorageList {
    private final int capacity;
    private final int mask;
    private final HttpRequestRecord[] buffer;

    private volatile long tail = 0;

    public HttpRequestRecordMemoryStorageList(int capacity) {
        this.capacity = findNextPositivePowerOfTwo(capacity);
        mask = this.capacity - 1;
        buffer = new HttpRequestRecord[this.capacity];
    }

    private int findNextPositivePowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    public void insert(HttpRequestRecord e) {
        final long currentTail = tail;
        buffer[(int) (currentTail & mask)] = e;
        tail = currentTail + 1;
    }

    public List<HttpRequestRecord> getAll() {
        final long currentTail = tail;
        LinkedList<HttpRequestRecord> list = new LinkedList<HttpRequestRecord>();
        if (currentTail >= capacity) {
            int index = (int) (currentTail & mask);
            if (index == 0) {
                for(int i = 0; i < capacity; i++) {
                    list.add(buffer[i]);
                }
                return list;
            } else if (index == 1) {
                for(int i = index; i < capacity; i++) {
                    list.add(buffer[i]);
                }
                list.add(buffer[0]);
                return list;
            } else {
                for(int i = index; i < capacity; i++) {
                    list.add(buffer[i]);
                }
                for(int i = 0; i < index; i++) {
                    list.add(buffer[i]);
                }
                return list;
            }
        } else {
            for(int i = 0; i < currentTail; i++) {
                list.add(buffer[i]);
            }
            return list;
        }
    }

    public List<HttpRequestRecord> getListFrom(int start, int end) {
        final long currentTail = tail;
        LinkedList<HttpRequestRecord> list = new LinkedList<HttpRequestRecord>();
        if (currentTail >= capacity) {
            int index = (int) (currentTail & mask);
            if (index == 0) {
                for(int i = 0; i < capacity; i++) {
                    list.add(buffer[i]);
                }
                return list.subList(start, end);
            } else if (index == 1) {
                for(int i = index; i < capacity; i++) {
                    list.add(buffer[i]);
                }
                list.add(buffer[0]);
                return list.subList(start, end);
            } else {
                for(int i = index; i < capacity; i++) {
                    list.add(buffer[i]);
                }
                for(int i = 0; i < index; i++) {
                    list.add(buffer[i]);
                }
                return list.subList(start, end);
            }
        } else {
            for(int i = 0; i < currentTail; i++) {
                list.add(buffer[i]);
            }
            return list.subList(start, end);
        }
    }


    public int getCapacity() {
        return this.capacity;
    }
}
