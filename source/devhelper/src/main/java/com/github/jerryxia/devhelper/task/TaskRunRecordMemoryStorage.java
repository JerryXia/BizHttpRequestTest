/**
 * 
 */
package com.github.jerryxia.devhelper.task;

import java.util.ArrayList;

/**
 * @author Administrator
 *
 */
public class TaskRunRecordMemoryStorage implements TaskRunRecordStorage {
    private static final int      DEFAULT_CAPACITY = 1024;
    private final int             capacity;
    private final int             mask;
    private final TaskRunRecord[] buffer;

    private volatile long tail = 0;

    public TaskRunRecordMemoryStorage() {
        this.capacity = findNextPositivePowerOfTwo(DEFAULT_CAPACITY);
        this.mask = this.capacity - 1;
        this.buffer = new TaskRunRecord[this.capacity];
    }

    private int findNextPositivePowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    @Override
    public boolean save(TaskRunRecord record) {
        final long currentTail = tail;
        buffer[(int) (currentTail & mask)] = record;
        tail = currentTail + 1;
        return true;
    }

    @Override
    public TaskRunRecordStorageQueryResult queryAll() {
        final long currentTail = tail;
        ArrayList<TaskRunRecord> list = new ArrayList<TaskRunRecord>();
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
        TaskRunRecordStorageQueryResult result = new TaskRunRecordStorageQueryResult(currentTail, list);
        return result;
    }

    @Override
    public TaskRunRecordStorageQueryResult queryNextList(long startIndex, long endIndex) {
        final long currentTail = tail;

        TaskRunRecordStorageQueryResult result = null;
        ArrayList<TaskRunRecord> list = new ArrayList<TaskRunRecord>();
        if (endIndex <= startIndex) {
            result = null;
        } else {
            if (currentTail <= startIndex) {
                result = new TaskRunRecordStorageQueryResult(currentTail, list);
            } else if (currentTail - startIndex <= this.capacity) {
                if (currentTail > endIndex) {
                    // 以endIndex坐标结束
                    for (long i = startIndex; i < endIndex; i++) {
                        int index = (int) (i & mask);
                        list.add(buffer[index]);
                    }
                    result = new TaskRunRecordStorageQueryResult(endIndex, list);
                } else {
                    // 以currentTail坐标结束
                    for (long i = startIndex; i < currentTail; i++) {
                        int index = (int) (i & mask);
                        list.add(buffer[index]);
                    }
                    result = new TaskRunRecordStorageQueryResult(currentTail, list);
                }
            } else {
                // 初始数据已被覆盖
                if (currentTail - endIndex > this.capacity) {
                    // 整个段的数据都被覆盖
                    result = new TaskRunRecordStorageQueryResult(currentTail, list);
                } else {
                    for (long i = (currentTail - this.capacity); i < endIndex; i++) {
                        int index = (int) (i & mask);
                        list.add(buffer[index]);
                    }
                    result = new TaskRunRecordStorageQueryResult(endIndex, list);
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

    public TaskRunRecord[] getBuffer() {
        return buffer;
    }

    public long getTail() {
        return tail;
    }
}