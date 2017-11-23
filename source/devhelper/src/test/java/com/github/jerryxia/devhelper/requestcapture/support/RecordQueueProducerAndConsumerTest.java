package com.github.jerryxia.devhelper.requestcapture.support;

import static org.junit.Assert.*;

import com.github.jerryxia.devhelper.util.SystemClock;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordEventProducer;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordManager;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author guqiankun
 *
 */
public class RecordQueueProducerAndConsumerTest {

    private HttpRequestRecordManager recordManager;
    
    @Before
    public void init() {
        recordManager = new HttpRequestRecordManager();
        recordManager.init();
    }

    @Test
    public void testDisruptorOneThreadIsOk() {
        // 1 threads 13.710s
        HttpRequestRecordEventProducer producer = recordManager.allocEventProducer();
        int count = 100 * 10000;
        String threadName = Thread.currentThread().getName();
        for (long l = 0; l < count; l++) {
            producer.publish(new HttpRequestRecord(String.format("%s_%s", threadName, l), HttpRequestRecordType.NORMAL, SystemClock.now()));
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(count, recordManager.viewHttpRequestRecordEventStat().getConsumerSuccessCount());
    }

    @Test
    public void testDisruptorTwoThreadIsOk() {
        // 2 threads 26.292s
        // 3 threads 37.764s
        // 4 threads 49.750s
        int threadCount = 2;
        final int count = 100 * 10000;

        Thread[] workers = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    for(int l = 0; l < count; l++) {
                        HttpRequestRecordEventProducer producer = recordManager.allocEventProducer();
                        producer.publish(new HttpRequestRecord(String.format("%s_%s", threadName, l), HttpRequestRecordType.NORMAL, SystemClock.now()));
                    }
                }
            }, String.format("%s", i));
            workers[i] = t;
        }
        workers[0].start();
        workers[1].start();
        try {
            workers[0].join();
            workers[1].join();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(threadCount * count, recordManager.viewHttpRequestRecordEventStat().getConsumerSuccessCount());
    }

    @After
    public void shutdown() {
        recordManager.shutdown();
    }
}
