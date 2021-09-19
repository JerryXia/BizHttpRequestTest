/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecordMemoryStorageTest {

    private HttpRequestRecordMemoryStorage storage = null;

    @Before
    public void init() {
        storage = new HttpRequestRecordMemoryStorage();
    }
    @Test
    public void ctorIsOk() {
        Assert.assertEquals(1024, storage.getCapacity());
        Assert.assertEquals(1023, storage.getMask());
        Assert.assertEquals(1024, storage.getBuffer().length);
        Assert.assertEquals(0, storage.getTail());
    }

    @Test
    public void saveOneItemIsOk() {
        HttpRequestRecord record = buildRecord(1);
        boolean saveSuccess = storage.save(record);
        Assert.assertTrue(saveSuccess);
        Assert.assertEquals("1", storage.getBuffer()[0].get().getId());
    }

    @Test
    public void save1025ItemsIsOk() {
        int count = 1025;
        for(int i = 1; i <= count; i++) {
            HttpRequestRecord record = buildRecord(i);
            boolean saveSuccess = storage.save(record);
            Assert.assertTrue(saveSuccess);
        }

        HttpRequestRecordStorageQueryResult allResult = storage.queryAll();
        // 1025
        Assert.assertEquals(String.valueOf(count), storage.getBuffer()[0].get().getId());
        Assert.assertEquals("2", storage.getBuffer()[1].get().getId());
        Assert.assertEquals(count, storage.getTail());
        Assert.assertEquals(count, allResult.getLastIndex());

        for(int i = 0; i < allResult.getList().size(); i++) {
            Assert.assertEquals(String.valueOf(i + 2), allResult.getList().get(i).getId());
        }

        HttpRequestRecordStorageQueryResult pagedResult = storage.queryNextList(0, 10);
        Assert.assertEquals(10, pagedResult.getLastIndex());
        Assert.assertEquals(9, pagedResult.getList().size());
        Assert.assertEquals("2", pagedResult.getList().get(0).getId());
    }

    @Test
    public void save2050ItemsIsOk() {
        int count = 2050;
        for(int i = 1; i <= count; i++) {
            HttpRequestRecord record = buildRecord(i);
            boolean saveSuccess = storage.save(record);
            Assert.assertTrue(saveSuccess);
        }

        HttpRequestRecordStorageQueryResult allResult = storage.queryAll();
        // 1025
        Assert.assertEquals(String.valueOf(2049), storage.getBuffer()[0].get().getId());
        Assert.assertEquals("1027", storage.getBuffer()[2].get().getId());
        Assert.assertEquals(count, storage.getTail());
        Assert.assertEquals(count, allResult.getLastIndex());

        for(int i = 0; i < allResult.getList().size(); i++) {
            Assert.assertEquals(String.valueOf(i + 1027), allResult.getList().get(i).getId());
        }

        HttpRequestRecordStorageQueryResult pagedResult = storage.queryNextList(0, 10);
        Assert.assertEquals(2050, pagedResult.getLastIndex());
        Assert.assertEquals(0, pagedResult.getList().size());

        // 
        HttpRequestRecordStorageQueryResult pagedResult2 = storage.queryNextList(1025, 1035);
        Assert.assertTrue(pagedResult2 != null);
        Assert.assertEquals(1035, pagedResult2.getLastIndex());
        Assert.assertEquals(9, pagedResult2.getList().size());
        
        // 
        HttpRequestRecordStorageQueryResult pagedResult3 = storage.queryNextList(1026, 1036);
        Assert.assertTrue(pagedResult3 != null);
        Assert.assertEquals(1036, pagedResult3.getLastIndex());
        Assert.assertEquals(10, pagedResult3.getList().size());
    }

    private HttpRequestRecord buildRecord(int id) {
        HttpRequestRecord record = new HttpRequestRecord(String.format("%s", id), HttpRequestRecordType.NORMAL, System.currentTimeMillis());
        return record;
    }

}
