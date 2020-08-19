package com.github.jerryxia.devhelper.task;

/**
 * @author Administrator
 *
 */
public interface TaskRunRecordStorage {

    /**
     * save one record
     * 
     * @param record
     * @return
     */
    boolean save(TaskRunRecord record);

    /**
     * queryAll
     * 
     * @return
     */
    TaskRunRecordStorageQueryResult queryAll();

    /**
     * query next page list
     * 
     * @param startIndex
     * @param endIndex
     * @return
     */
    TaskRunRecordStorageQueryResult queryNextList(long startIndex, long endIndex);
}
