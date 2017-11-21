/**
 * 
 */
package org.anonymous.devhelper.requestcapture.log;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class LogEntryMemoryStorage implements LogEntryStorage {

    private final LogEntryMemoryStorageList list;

    public LogEntryMemoryStorage() {
        list = new LogEntryMemoryStorageList();
    }

    /* (non-Javadoc)
     * @see org.anonymous.devhelper.requestcapture.log.LogEntryStorage#save(org.anonymous.devhelper.requestcapture.log.LogEntry)
     */
    @Override
    public boolean save(LogEntry record) {
        list.insert(record);
        return true;
    }

    /* (non-Javadoc)
     * @see org.anonymous.devhelper.requestcapture.log.LogEntryStorage#queryAll()
     */
    @Override
    public List<LogEntry> queryAll() {
        return list.getAll();
    }

    /* (non-Javadoc)
     * @see org.anonymous.devhelper.requestcapture.log.LogEntryStorage#queryPagedList(int, int)
     */
    @Override
    public List<LogEntry> queryPagedList(int start, int end) {
        return list.getListFrom(start, end);
    }
}
