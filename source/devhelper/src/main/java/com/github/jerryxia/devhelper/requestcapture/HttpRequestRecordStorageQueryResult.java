/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecordStorageQueryResult {
    private final long                    lastIndex;
    private final List<HttpRequestRecord> list;

    public HttpRequestRecordStorageQueryResult(long lastIndex, List<HttpRequestRecord> data) {
        this.lastIndex = lastIndex;
        this.list = data;
    }

    public long getLastIndex() {
        return lastIndex;
    }

    public List<HttpRequestRecord> getList() {
        return list;
    }
}
