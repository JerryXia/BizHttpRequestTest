package com.guqiankun.devhelper.requestcapture.storage;

import java.util.List;

import com.guqiankun.devhelper.requestcapture.HttpRequestRecord;
import com.guqiankun.devhelper.requestcapture.RecordStorage;

public class MemoryStorage implements RecordStorage {

    private final HttpRequestRecordList list;

    public MemoryStorage() {
        list = new HttpRequestRecordList(1024);
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
