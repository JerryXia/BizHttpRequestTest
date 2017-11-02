package com.guqiankun.ssmtemplate.common;

import org.springframework.core.NamedThreadLocal;

public class SysThreadLocal {

    public static final NamedThreadLocal<String> RequestId = new NamedThreadLocal<String>("X-Call-RequestId");


}
