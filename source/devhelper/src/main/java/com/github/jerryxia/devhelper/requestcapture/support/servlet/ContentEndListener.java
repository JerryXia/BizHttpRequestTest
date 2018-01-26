/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.support.servlet;

import java.io.ByteArrayOutputStream;

/**
 * @author Administrator
 *
 */
public interface ContentEndListener {
    void handleContentEnd(ByteArrayOutputStream cachedContent, String charsetName);
}
