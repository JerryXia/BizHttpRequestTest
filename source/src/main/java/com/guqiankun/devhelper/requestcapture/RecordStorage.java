package com.guqiankun.devhelper.requestcapture;

import java.util.List;

/**
 * @author Administrator
 *
 */
public interface RecordStorage {

    /**
     * save one record
     * 
     * @param record
     * @return
     */
    boolean save(HttpRequestRecord record);

    List<HttpRequestRecord> queryAll();

    List<HttpRequestRecord> queryPagedList(int start, int end);
}
