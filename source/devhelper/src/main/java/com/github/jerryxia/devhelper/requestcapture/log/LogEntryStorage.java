/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.log;

import java.util.List;

/**
 * @author Administrator
 *
 */
public interface LogEntryStorage {

    /**
     * save
     * 
     * @param record
     * @return
     */
    boolean save(LogEntry record);

    /**
     * queryAll
     * 
     * @return
     */
    List<LogEntry> queryAll();

    /**
     * queryPagedList
     * 
     * @param start
     * @param end
     * @return
     */
    List<LogEntry> queryPagedList(int start, int end);
}
