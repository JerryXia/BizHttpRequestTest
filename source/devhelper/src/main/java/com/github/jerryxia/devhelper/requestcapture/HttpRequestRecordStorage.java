package com.github.jerryxia.devhelper.requestcapture;

/**
 * @author Administrator
 *
 */
public interface HttpRequestRecordStorage {

    /**
     * save one record
     * 
     * @param record
     * @return
     */
    boolean save(HttpRequestRecord record);

    /**
     * queryAll
     * 
     * @return
     */
    HttpRequestRecordStorageQueryResult queryAll();

    /**
     * query next page list
     * 
     * @param startIndex
     * @param endIndex
     * @return
     */
    HttpRequestRecordStorageQueryResult queryNextList(long startIndex, long endIndex);
}
