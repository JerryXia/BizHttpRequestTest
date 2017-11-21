/**
 * 
 */
package org.anonymous.devhelper.requestcapture;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecordMemoryStorage implements HttpRequestRecordStorage {

    private final HttpRequestRecordMemoryStorageList list;

    public HttpRequestRecordMemoryStorage() {
        list = new HttpRequestRecordMemoryStorageList(1024);
    }

    @Override
    public boolean save(HttpRequestRecord record) {
        list.insert(record);
        return true;
    }

    @Override
    public List<HttpRequestRecord> queryAll() {
        return list.getAll();
    }

    @Override
    public List<HttpRequestRecord> queryPagedList(int start, int end) {
        return list.getListFrom(start, end);
    }

}
