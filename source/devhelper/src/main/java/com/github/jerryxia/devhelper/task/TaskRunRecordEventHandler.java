/**
 * 
 */
package com.github.jerryxia.devhelper.task;

import com.github.jerryxia.devhelper.event.ValueWrapperEvent;
import com.lmax.disruptor.EventHandler;

/**
 * @author Administrator
 *
 */
public class TaskRunRecordEventHandler implements EventHandler<ValueWrapperEvent> {

    private final TaskRunRecordStorage taskRunRecordStorage;

    public TaskRunRecordEventHandler(TaskRunRecordStorage taskRunRecordStorage) {
        this.taskRunRecordStorage = taskRunRecordStorage;
    }

    @Override
    public void onEvent(ValueWrapperEvent event, long sequence, boolean endOfBatch) {
        TaskRunRecord record = (TaskRunRecord) event.getValue();
        this.taskRunRecordStorage.save(record);
    }
}
