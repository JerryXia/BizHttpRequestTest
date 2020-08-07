package com.github.jerryxia.devhelper.log;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class LogEntryStorageQueryResult {
    private final long           lastIndex;
    private final List<LogEntry> list;

    public LogEntryStorageQueryResult(long lastIndex, List<LogEntry> data) {
        this.lastIndex = lastIndex;
        this.list = data;
    }

    public long getLastIndex() {
        return lastIndex;
    }

    public List<LogEntry> getList() {
        return list;
    }
}
