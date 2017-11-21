package org.anonymous.devhelper.requestcapture.support;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.anonymous.devhelper.requestcapture.support.Constants;
import org.anonymous.devhelper.requestcapture.HttpRequestRecord;
import org.anonymous.devhelper.requestcapture.HttpRequestRecordEventProducer;
import org.anonymous.devhelper.requestcapture.HttpRequestRecordType;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author guqiankun
 *
 */
public class RecordQueueProducerAndConsumerTest {

    @Before
    public void init() {
        Constants.RECORD_MANAGER.init();
    }

    @Test
    public void testDisruptorIsOk() {
        // 1 threads 13.710s
        HttpRequestRecordEventProducer producer = Constants.RECORD_MANAGER.allocEventProducer();
        int count = 10000000;
        for (long l = 0; l < count; l++) {
            producer.publish(new HttpRequestRecord(HttpRequestRecordType.NORMAL, null));
        }
        assertEquals(count, Constants.RECORD_MANAGER.viewHttpRequestRecordEventStat().getConsumerSuccessCount());
    }

    ArrayList<Thread> workers = new ArrayList<Thread>();

    @Test
    public void testDisruptorMutiThreadIsOk() {
        // 2 threads 26.292s
        // 3 threads 37.764s
        // 4 threads 49.750s
        int threadCount = 2;
        final int count = 10000000;

        ThreadPoolExecutor executorService = new ThreadPoolExecutor(threadCount, threadCount, 1, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new BasicThreadFactory.Builder().namingPattern("example-pool-%d").daemon(true).build());

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    workers.add(Thread.currentThread());
                    HttpRequestRecordEventProducer producer = Constants.RECORD_MANAGER.allocEventProducer();
                    for (long l = 0; l < count; l++) {
                        producer.publish(new HttpRequestRecord(HttpRequestRecordType.NORMAL, null));
                    }
                }
            });
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(threadCount * count,
                Constants.RECORD_MANAGER.viewHttpRequestRecordEventStat().getConsumerSuccessCount());
    }

    @After
    public void shutdown() {
        Constants.RECORD_MANAGER.shutdown();
    }
}
