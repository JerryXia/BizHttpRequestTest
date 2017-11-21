package org.anonymous.devhelper.requestcapture.log;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Administrator
 *
 */
class LogEntryMemoryStorageList {
    private volatile long        offset         = 0;
    private int                  capacity       = 1024 * 1024;
    private boolean              hasSetCapacity = false;
    private LinkedList<LogEntry> linkedList     = new LinkedList<LogEntry>();

    public LogEntryMemoryStorageList() {
        //
    }

    public void insert(LogEntry element) {
        if (offset >= capacity) {
            int index = getCurrentOffset();
            linkedList.set(index, element);
        } else {
            linkedList.add(element);
        }
        offset++;
    }

    public List<LogEntry> getAll() {
        if (offset >= capacity) {
            int index = getCurrentOffset();
            if (index == 0) {
                return new LinkedList<LogEntry>(linkedList);
            } else if (index == 1) {
                List<LogEntry> newCloneList = new LinkedList<LogEntry>(linkedList.subList(index, capacity));
                newCloneList.add(linkedList.getFirst());
                return newCloneList;
            } else {
                List<LogEntry> newCloneList = new LinkedList<LogEntry>(linkedList.subList(index, capacity));
                newCloneList.addAll(linkedList.subList(0, index));
                return newCloneList;
            }
        } else {
            return new LinkedList<LogEntry>(linkedList);
        }
    }

    public List<LogEntry> getListFrom(int start, int end) {
        if (offset >= capacity) {
            int index = getCurrentOffset();
            if (index == 0) {
                return new LinkedList<LogEntry>(linkedList).subList(start, end);
            } else if (index == 1) {
                List<LogEntry> newCloneList = new LinkedList<LogEntry>(linkedList.subList(index, capacity));
                newCloneList.add(linkedList.getFirst());
                return newCloneList.subList(start, end);
            } else {
                List<LogEntry> newCloneList = new LinkedList<LogEntry>(linkedList.subList(index, capacity));
                newCloneList.addAll(linkedList.subList(0, index));
                return newCloneList.subList(start, end);
            }
        } else {
            return new LinkedList<LogEntry>(linkedList).subList(start, end);
        }
    }

    public int getCurrentOffset() {
        return (int) (offset % capacity);
    }

    public void setCapacity(int capacity) {
        if (hasSetCapacity) {
            // return;
        } else {
            if (capacity > 16384) {
                this.capacity = capacity;
            } else {
                this.capacity = 16384;
            }
            hasSetCapacity = true;
        }
    }

    public int getCapacity() {
        return this.capacity;
    }
}
