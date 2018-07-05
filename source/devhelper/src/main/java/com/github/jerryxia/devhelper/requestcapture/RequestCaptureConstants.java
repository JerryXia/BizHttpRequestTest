/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

/**
 * @author Administrator
 *
 */
class RequestCaptureConstants {
    public static final HttpRequestRecordEventExceptionHandler DEFAULT_EXCEPTION_HANDLER                 = new HttpRequestRecordEventExceptionHandler();
    public static final int                                    DEFAULT_RINGBUFFER_SIZE                   = 1024;
    public static final long                                   DEFAULT_RINGBUFFER_SHUTDOWN_TIMEOUT_MS    = 50;
    public static final NopHttpRequestRecordEventProducer      DEFAULT_NOPHTTPREQUESTRECORDEVENTPRODUCER = new NopHttpRequestRecordEventProducer();
}
