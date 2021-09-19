package com.github.jerryxia.devhelper.log;

import java.util.ArrayList;

/**
 * @author Administrator
 *
 */
public class LogEntryMemoryStorage implements LogEntryStorage {

    private static final int DEFAULT_CAPACITY = 1024 * 8;
    private final int        capacity;
    private final int        mask;
    private final LogEntryWeakReference[] buffer;

    private volatile long tail = 0;

    public LogEntryMemoryStorage() {
        this.capacity = findNextPositivePowerOfTwo(DEFAULT_CAPACITY);
        this.mask = this.capacity - 1;
        this.buffer = new LogEntryWeakReference[this.capacity];
    }

    private int findNextPositivePowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    @Override
    public boolean save(LogEntry record) {
        final long currentTail = tail;
        buffer[(int) (currentTail & mask)] = new LogEntryWeakReference(record);
        tail = currentTail + 1;
        return true;
    }

    @Override
    public LogEntryStorageQueryResult queryAll() {
        final long currentTail = tail;
        ArrayList<LogEntry> list = new ArrayList<LogEntry>();
        if (currentTail >= capacity) {
            int index = (int) (currentTail & mask);
            if (index == 0) {
                for (int i = 0; i < capacity; i++) {
                    add(list, buffer[i]);
                }
            } else if (index == 1) {
                for (int i = index; i < capacity; i++) {
                    add(list, buffer[i]);
                }
                add(list, buffer[0]);
            } else {
                for (int i = index; i < capacity; i++) {
                    add(list, buffer[i]);
                }
                for (int i = 0; i < index; i++) {
                    add(list, buffer[i]);
                }
            }
        } else {
            for (int i = 0; i < currentTail; i++) {
                add(list, buffer[i]);
            }
        }
        LogEntryStorageQueryResult result = new LogEntryStorageQueryResult(currentTail, list);
        return result;
    }

    @Override
    public LogEntryStorageQueryResult queryNextList(long startIndex, long endIndex) {
        final long currentTail = tail;

        LogEntryStorageQueryResult result = null;
        ArrayList<LogEntry> list = new ArrayList<LogEntry>();
        if (endIndex <= startIndex) {
            result = null;
        } else {
            if (currentTail <= startIndex) {
                result = new LogEntryStorageQueryResult(currentTail, list);
            } else if (currentTail - startIndex <= this.capacity) {
                if (currentTail > endIndex) {
                    // 以endIndex坐标结束
                    for (long i = startIndex; i < endIndex; i++) {
                        int index = (int) (i & mask);
                        add(list, buffer[index]);
                    }
                    result = new LogEntryStorageQueryResult(endIndex, list);
                } else {
                    // 以currentTail坐标结束
                    for (long i = startIndex; i < currentTail; i++) {
                        int index = (int) (i & mask);
                        add(list, buffer[index]);
                    }
                    result = new LogEntryStorageQueryResult(currentTail, list);
                }
            } else {
                // 初始数据已被覆盖
                if (currentTail - endIndex > this.capacity) {
                    // 整个段的数据都被覆盖
                    result = new LogEntryStorageQueryResult(currentTail, list);
                } else {
                    for (long i = (currentTail - this.capacity); i < endIndex; i++) {
                        int index = (int) (i & mask);
                        add(list, buffer[index]);
                    }
                    result = new LogEntryStorageQueryResult(endIndex, list);
                }
            }
        }
        return result;
    }

    private void add(ArrayList<LogEntry> list, LogEntryWeakReference item) {
        LogEntry record = item.get();
        if(record != null) {
            list.add(record);
        }
    }
}
