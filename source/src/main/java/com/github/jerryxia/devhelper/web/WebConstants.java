/**
 * 
 */
package com.github.jerryxia.devhelper.web;

import org.springframework.core.NamedThreadLocal;

/**
 * @author guqk
 *
 */
public class WebConstants {

    public static final NamedThreadLocal<String> X_CALL_REQUEST_ID = new NamedThreadLocal<String>("X-Call-RequestId");

}
