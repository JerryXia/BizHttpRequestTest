/**
 * 
 */
package org.anonymous.devhelper.requestcapture.log;

import java.util.List;

/**
 * @author Administrator
 *
 */
public interface LogEntryStorage {

    boolean save(LogEntry record);

    List<LogEntry> queryAll();

    List<LogEntry> queryPagedList(int start, int end);
}
