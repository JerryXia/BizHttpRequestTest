package com.github.jerryxia.devhelper.requestcapture;

import java.lang.ref.WeakReference;

/**
 * @author Administrator
 * @date 2021/08/17
 */
public class HttpRequestRecordWeakReference extends WeakReference<HttpRequestRecord> {

    public HttpRequestRecordWeakReference(HttpRequestRecord referent) {
        super(referent);
    }
}
