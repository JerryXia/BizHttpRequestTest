/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.log;

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
    LogEntryStorageQueryResult queryAll();

    /**
     * query next page list
     * 
     * @param startIndex
     * @param endIndex
     * @return
     */
    LogEntryStorageQueryResult queryNextList(long startIndex, long endIndex);
}
