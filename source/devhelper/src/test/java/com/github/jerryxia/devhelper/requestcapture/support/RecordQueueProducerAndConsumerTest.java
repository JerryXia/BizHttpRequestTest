package com.github.jerryxia.devhelper.requestcapture.support;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.github.jerryxia.devhelper.requestcapture.support.RequestCaptureConstants;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordEventProducer;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordType;
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
        RequestCaptureConstants.RECORD_MANAGER.init();
    }

    @Test
    public void testDisruptorIsOk() {
        // 1 threads 13.710s
        HttpRequestRecordEventProducer producer = RequestCaptureConstants.RECORD_MANAGER.allocEventProducer();
        int count = 10000000;
        for (long l = 0; l < count; l++) {
            producer.publish(new HttpRequestRecord(UUID.randomUUID().toString(), HttpRequestRecordType.NORMAL, System.currentTimeMillis()));
        }
        assertEquals(count, RequestCaptureConstants.RECORD_MANAGER.viewHttpRequestRecordEventStat().getConsumerSuccessCount());
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
                    HttpRequestRecordEventProducer producer = RequestCaptureConstants.RECORD_MANAGER.allocEventProducer();
                    for (long l = 0; l < count; l++) {
                        producer.publish(new HttpRequestRecord(UUID.randomUUID().toString(), HttpRequestRecordType.NORMAL, System.currentTimeMillis()));
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
                RequestCaptureConstants.RECORD_MANAGER.viewHttpRequestRecordEventStat().getConsumerSuccessCount());
    }

    @After
    public void shutdown() {
        RequestCaptureConstants.RECORD_MANAGER.shutdown();
    }
}
