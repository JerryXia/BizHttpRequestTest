package com.github.jerryxia.devhelper.log;

import java.lang.ref.WeakReference;

/**
 * @author Administrator
 * @date 2021/08/17
 */
public class LogEntryWeakReference extends WeakReference<LogEntry> {

    public LogEntryWeakReference(LogEntry referent) {
        super(referent);
    }
}
