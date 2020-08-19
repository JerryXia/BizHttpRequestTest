/**
 * 
 */
package com.github.jerryxia.devhelper.task;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class TaskRunRecordStorageQueryResult {
    private final long                lastIndex;
    private final List<TaskRunRecord> list;

    public TaskRunRecordStorageQueryResult(long lastIndex, List<TaskRunRecord> data) {
        this.lastIndex = lastIndex;
        this.list = data;
    }

    public long getLastIndex() {
        return lastIndex;
    }

    public List<TaskRunRecord> getList() {
        return list;
    }
}
