package com.github.jerryxia.devhelper.requestcapture;

import java.util.List;

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
    List<HttpRequestRecord> queryAll();

    /**
     * queryPagedList
     * 
     * @param start
     * @param end
     * @return
     */
    List<HttpRequestRecord> queryPagedList(int start, int end);
}
