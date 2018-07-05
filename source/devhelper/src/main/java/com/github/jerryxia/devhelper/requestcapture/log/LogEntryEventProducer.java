package com.github.jerryxia.devhelper.requestcapture.log;

public interface LogEntryEventProducer {

    void publish(LogEntry record);

    void tryPublish(LogEntry record);
}
