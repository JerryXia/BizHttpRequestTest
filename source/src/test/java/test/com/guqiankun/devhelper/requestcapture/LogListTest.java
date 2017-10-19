package test.com.guqiankun.devhelper.requestcapture;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.guqiankun.devhelper.requestcapture.log.LogEntry;
import com.guqiankun.devhelper.requestcapture.log.LogEntryList;

/**
 * @author guqiankun
 *
 */
public class LogListTest {

    @Before
    public void init() {
        //
    }

    @Test
    public void testLogEntryListCtorCapacityIs1024() {
        LogEntryList logEntryList = new LogEntryList();
        assertEquals(10240, logEntryList.getCapacity());
    }

    @Test
    public void testLogEntryListSetCapacityIs1024() {
        LogEntryList logEntryList = new LogEntryList();
        logEntryList.setCapacity(1);
        assertEquals(10240, logEntryList.getCapacity());
    }

    @Test
    public void testLogEntryListSetCapacityIsGreatThan1024() {
        LogEntryList logEntryList = new LogEntryList();
        logEntryList.setCapacity(10240);
        assertEquals(10240, logEntryList.getCapacity());
    }

    @Test
    public void testLogEntryListInsertIsLessThan1024() {
        LogEntryList logEntryList = new LogEntryList();
        assertEquals(0, logEntryList.getCurrentOffset());
        for (int i = 0; i < 10239; i++) {
            assertEquals(i, logEntryList.getCurrentOffset());
            logEntryList.insert(buildLogEntry(i + 11111));
            assertEquals(i + 1, logEntryList.getCurrentOffset());
        }
        assertEquals(10239, logEntryList.getCurrentOffset());
    }

    @Test
    public void testLogEntryListInsertIsEquals1024() {
        LogEntryList logEntryList = new LogEntryList();
        for (int i = 0; i < 10240; i++) {
        	logEntryList.insert(buildLogEntry(i + 11111));
        }
        assertEquals(0, logEntryList.getCurrentOffset());
    }

    @Test
    public void testLogEntryListInsertIsGreatThan1024AndLessThan2048() {
        LogEntryList logEntryList = new LogEntryList();
        for (int i = 10240; i < 20479; i++) {
            assertEquals(i - 10240, logEntryList.getCurrentOffset());
            logEntryList.insert(buildLogEntry(i + 11111));
            assertEquals(i + 1 - 10240, logEntryList.getCurrentOffset());
        }
        assertEquals(10239, logEntryList.getCurrentOffset());
    }

    @Test
    public void testLogEntryListInsertIsEquals2048() {
        LogEntryList logEntryList = new LogEntryList();
        for (int i = 10240; i < 20480; i++) {
        	logEntryList.insert(buildLogEntry(i + 11111));
        }
        assertEquals(0, logEntryList.getCurrentOffset());
    }

    @Test
    public void testLogEntryListInsertIsGreatThan2048() {
        LogEntryList logEntryList = new LogEntryList();
        int count = new java.util.Random().nextInt(12352);
        for (int i = 10240 * count; i < (10240 * (count + 1) - 1); i++) {
            assertEquals(i - 10240 * count, logEntryList.getCurrentOffset());
            logEntryList.insert(buildLogEntry(i + 11111));
            assertEquals(i + 1 - 10240 * count, logEntryList.getCurrentOffset());
        }
        assertEquals((10240 * (count + 1) - 1) % 10240, logEntryList.getCurrentOffset());
    }

    @Test
    public void testLogEntryListGetAllIsLessThan1024() {
        LogEntryList logEntryList = new LogEntryList();
        List<LogEntry> allList0 = logEntryList.getAll();
        assertEquals(0, allList0.size());

        for (int i = 0; i < 10239; i++) {
        	logEntryList.insert(buildLogEntry(i));
        }
        assertEquals(10239, logEntryList.getAll().size());
        assertEquals(10238, logEntryList.getAll().get(10238).getId());
    }

    @Test
    public void testLogEntryListGetAllIsGreatThan1024() {
        LogEntryList logEntryList = new LogEntryList();
        List<LogEntry> allList0 = logEntryList.getAll();
        assertEquals(0, allList0.size());

        for (int i = 0; i < 10239; i++) {
        	logEntryList.insert(buildLogEntry(i));
        }
        
        allList0 = logEntryList.getAll();
        assertEquals(10239, allList0.size());
        assertEquals(10238, allList0.get(10238).getId());

        logEntryList.insert(buildLogEntry(10239));
        allList0 = logEntryList.getAll();
        assertEquals(10240, allList0.size());
        assertEquals(10239, allList0.get(10239).getId());

        logEntryList.insert(buildLogEntry(10240));
        allList0 = logEntryList.getAll();
        assertEquals(10240, allList0.size());
        assertEquals(10240, allList0.get(0).getId());
    }

    @Test
    public void testLogEntryListGetAllIsEquals2048() {
        LogEntryList logEntryList = new LogEntryList();
        List<LogEntry> allList0 = logEntryList.getAll();
        assertEquals(0, allList0.size());

        for (int i = 0; i < 20480; i++) {
        	logEntryList.insert(buildLogEntry(i));
        }
        assertEquals(10240, allList0.size());
        assertEquals(10240, allList0.get(0).getId());

        logEntryList.insert(buildLogEntry(2048));
        assertEquals(10240, allList0.size());
        assertEquals(20480, allList0.get(0).getId());
    }

    @Test
    public void testLogEntryListGetListFromIsLessThan1024() {
        LogEntryList logEntryList = new LogEntryList();
        for (int i = 0; i < 10239; i++) {
        	logEntryList.insert(buildLogEntry(i));
        }
        List<LogEntry> pagedList = logEntryList.getListFrom(0, 10);
        pagedList.remove(0);
        assertEquals(1, logEntryList.getListFrom(0, 1).get(0).getId());
    }

    @Test
    public void testLogEntryListGetListFromIsGreatThan1024() {
        LogEntryList logEntryList = new LogEntryList();
        for (int i = 0; i < 10240; i++) {
        	logEntryList.insert(buildLogEntry(i));
        }
        List<LogEntry> pagedList = logEntryList.getListFrom(0, 10);
        for (int j = 0; j < 10; j++) {
            assertEquals(j, pagedList.get(j).getId());
        }

        logEntryList.insert(buildLogEntry(1024));
        List<LogEntry> pagedList2 = logEntryList.getListFrom(0, 10);
        for (int j = 0; j < 10; j++) {
            assertEquals(j + 1, pagedList2.get(j).getId());
        }
    }

    @Test
    public void testLogEntryListGetListFromIsEquals2048() {
        LogEntryList logEntryList = new LogEntryList();
        for (int i = 0; i < 20480; i++) {
        	logEntryList.insert(buildLogEntry(i));
        }
        List<LogEntry> pagedList = logEntryList.getListFrom(0, 10);
        for (int j = 0; j < 10; j++) {
            assertEquals(j + 10240, pagedList.get(j).getId());
        }
    }


    private LogEntry buildLogEntry(long id) {
        LogEntry logEntry = new LogEntry(id, "", "");
        return logEntry;
    }
}
