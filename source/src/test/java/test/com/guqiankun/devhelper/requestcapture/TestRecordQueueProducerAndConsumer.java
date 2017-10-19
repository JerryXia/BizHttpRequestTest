package test.com.guqiankun.devhelper.requestcapture;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.guqiankun.devhelper.requestcapture.HttpRequestRecord;
import com.guqiankun.devhelper.requestcapture.HttpRequestRecordEventProducer;
import com.guqiankun.devhelper.requestcapture.HttpRequestRecordType;
import com.guqiankun.devhelper.requestcapture.RecordManager;

public class TestRecordQueueProducerAndConsumer {

    @Before
    public void init() {
        RecordManager.getInstance().init();
    }

    @Test
    public void test_disruptor_is_ok() {
        // 1 threads  13.710s
        HttpRequestRecordEventProducer producer =  RecordManager.getInstance().allocEventProducer();
        int count = 10000000;
        for (long l = 0; l < count; l++) {
            producer.publish(new HttpRequestRecord(HttpRequestRecordType.NORMAL, null));
        }
        assertEquals(count, RecordManager.getInstance().viewRecordStat().getRecordQueueConsumerSuccessCount());
    }

    @Test
    public void test_disruptor_muti_is_ok() {
        // 2 threads  26.292s
        // 3 threads  37.764s
        // 4 threads  49.750s
        int threadCount = 8;
        final int count = 10000000;
        Thread[] workers = new Thread[threadCount];
        for(int i = 0; i < threadCount; i++) {
            workers[i] = new Thread() {
                @Override
                public void run() {
                    HttpRequestRecordEventProducer producer =  RecordManager.getInstance().allocEventProducer();
                    for (long l = 0; l < count; l++) {
                        producer.publish(new HttpRequestRecord(HttpRequestRecordType.NORMAL, null));
                    }
                }
            };
            workers[i].start();
        }
        for(int i = 0; i < threadCount; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(threadCount * count, RecordManager.getInstance().viewRecordStat().getRecordQueueConsumerSuccessCount());
    }

    @After
    public void shutdown() {
        RecordManager.getInstance().shutdown();
    }
}
