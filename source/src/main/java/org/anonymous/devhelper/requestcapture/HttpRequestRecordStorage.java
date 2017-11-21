package org.anonymous.devhelper.requestcapture;

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

    List<HttpRequestRecord> queryAll();

    List<HttpRequestRecord> queryPagedList(int start, int end);
}
