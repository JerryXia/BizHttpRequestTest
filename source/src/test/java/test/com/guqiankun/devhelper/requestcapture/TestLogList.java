package test.com.guqiankun.devhelper.requestcapture;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.guqiankun.devhelper.requestcapture.log.LogEntry;
import com.guqiankun.devhelper.requestcapture.log.LogEntryList;

public class TestLogList {

    @Before
    public void init() {
        //
    }

    @Test
    public void test_LogEntryList_ctor_capacity_is1024() {
        LogEntryList LogEntryList = new LogEntryList();
        assertEquals(10240, LogEntryList.getCapacity());
    }

    @Test
    public void test_LogEntryList_setCapacity_is1024() {
        LogEntryList LogEntryList = new LogEntryList();
        LogEntryList.setCapacity(1);
        assertEquals(10240, LogEntryList.getCapacity());
    }

    @Test
    public void test_LogEntryList_setCapacity_isGreatThan1024() {
        LogEntryList LogEntryList = new LogEntryList();
        LogEntryList.setCapacity(10240);
        assertEquals(10240, LogEntryList.getCapacity());
    }

    @Test
    public void test_LogEntryList_insert_isLessThan1024() {
        LogEntryList LogEntryList = new LogEntryList();
        assertEquals(0, LogEntryList.getCurrentOffset());
        for (int i = 0; i < 10239; i++) {
            assertEquals(i, LogEntryList.getCurrentOffset());
            LogEntryList.insert(buildLogEntry(i + 11111));
            assertEquals(i + 1, LogEntryList.getCurrentOffset());
        }
        assertEquals(10239, LogEntryList.getCurrentOffset());
    }

    @Test
    public void test_LogEntryList_insert_isEquals1024() {
        LogEntryList LogEntryList = new LogEntryList();
        for (int i = 0; i < 10240; i++) {
            LogEntryList.insert(buildLogEntry(i + 11111));
        }
        assertEquals(0, LogEntryList.getCurrentOffset());
    }

    @Test
    public void test_LogEntryList_insert_isGreatThan1024_and_LessThan2048() {
        LogEntryList LogEntryList = new LogEntryList();
        for (int i = 10240; i < 20479; i++) {
            assertEquals(i - 10240, LogEntryList.getCurrentOffset());
            LogEntryList.insert(buildLogEntry(i + 11111));
            assertEquals(i + 1 - 10240, LogEntryList.getCurrentOffset());
        }
        assertEquals(10239, LogEntryList.getCurrentOffset());
    }

    @Test
    public void test_LogEntryList_insert_isEquals2048() {
        LogEntryList LogEntryList = new LogEntryList();
        for (int i = 10240; i < 20480; i++) {
            LogEntryList.insert(buildLogEntry(i + 11111));
        }
        assertEquals(0, LogEntryList.getCurrentOffset());
    }

    @Test
    public void test_LogEntryList_insert_isGreatThan2048() {
        LogEntryList LogEntryList = new LogEntryList();
        int count = new java.util.Random().nextInt(12352);
        for (int i = 10240 * count; i < (10240 * (count + 1) - 1); i++) {
            assertEquals(i - 10240 * count, LogEntryList.getCurrentOffset());
            LogEntryList.insert(buildLogEntry(i + 11111));
            assertEquals(i + 1 - 10240 * count, LogEntryList.getCurrentOffset());
        }
        assertEquals((10240 * (count + 1) - 1) % 10240, LogEntryList.getCurrentOffset());
    }

    @Test
    public void test_LogEntryList_getAll_isLessThan1024() {
        LogEntryList LogEntryList = new LogEntryList();
        List<LogEntry> allList0 = LogEntryList.getAll();
        assertEquals(0, allList0.size());

        for (int i = 0; i < 10239; i++) {
            LogEntryList.insert(buildLogEntry(i));
        }
        assertEquals(10239, LogEntryList.getAll().size());
        assertEquals(10238, LogEntryList.getAll().get(10238).getId());
    }

    @Test
    public void test_LogEntryList_getAll_isGreatThan1024() {
        LogEntryList LogEntryList = new LogEntryList();
        List<LogEntry> allList0 = LogEntryList.getAll();
        assertEquals(0, allList0.size());

        for (int i = 0; i < 10239; i++) {
            LogEntryList.insert(buildLogEntry(i));
        }
        
        allList0 = LogEntryList.getAll();
        assertEquals(10239, allList0.size());
        assertEquals(10238, allList0.get(10238).getId());

        LogEntryList.insert(buildLogEntry(10239));
        allList0 = LogEntryList.getAll();
        assertEquals(10240, allList0.size());
        assertEquals(10239, allList0.get(10239).getId());

        LogEntryList.insert(buildLogEntry(10240));
        allList0 = LogEntryList.getAll();
        assertEquals(10240, allList0.size());
        assertEquals(10240, allList0.get(0).getId());
    }

    @Test
    public void test_LogEntryList_getAll_isEquals2048() {
        LogEntryList LogEntryList = new LogEntryList();
        List<LogEntry> allList0 = LogEntryList.getAll();
        assertEquals(0, allList0.size());

        for (int i = 0; i < 20480; i++) {
            LogEntryList.insert(buildLogEntry(i));
        }
        assertEquals(10240, allList0.size());
        assertEquals(10240, allList0.get(0).getId());

        LogEntryList.insert(buildLogEntry(2048));
        assertEquals(10240, allList0.size());
        assertEquals(20480, allList0.get(0).getId());
    }

    @Test
    public void test_LogEntryList_getListFrom_isLessThan1024() {
        LogEntryList LogEntryList = new LogEntryList();
        for (int i = 0; i < 10239; i++) {
            LogEntryList.insert(buildLogEntry(i));
        }
        List<LogEntry> pagedList = LogEntryList.getListFrom(0, 10);
        pagedList.remove(0);
        assertEquals(1, LogEntryList.getListFrom(0, 1).get(0).getId());
    }

    @Test
    public void test_LogEntryList_getListFrom_isGreatThan1024() {
        LogEntryList LogEntryList = new LogEntryList();
        for (int i = 0; i < 10240; i++) {
            LogEntryList.insert(buildLogEntry(i));
        }
        List<LogEntry> pagedList = LogEntryList.getListFrom(0, 10);
        for (int j = 0; j < 10; j++) {
            assertEquals(j, pagedList.get(j).getId());
        }

        LogEntryList.insert(buildLogEntry(1024));
        List<LogEntry> pagedList2 = LogEntryList.getListFrom(0, 10);
        for (int j = 0; j < 10; j++) {
            assertEquals(j + 1, pagedList2.get(j).getId());
        }
    }

    @Test
    public void test_LogEntryList_getListFrom_isEquals2048() {
        LogEntryList LogEntryList = new LogEntryList();
        for (int i = 0; i < 20480; i++) {
            LogEntryList.insert(buildLogEntry(i));
        }
        List<LogEntry> pagedList = LogEntryList.getListFrom(0, 10);
        for (int j = 0; j < 10; j++) {
            assertEquals(j + 10240, pagedList.get(j).getId());
        }
    }


    private LogEntry buildLogEntry(long id) {
        LogEntry logEntry = new LogEntry(id, "", "");
        return logEntry;
    }
}
