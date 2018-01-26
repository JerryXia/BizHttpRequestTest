/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.support.servlet;

/**
 * @author Administrator
 *
 */
public class DefaultContentOverflowListener implements ContentOverflowListener {

    /**
     * Template method for handling a content overflow: specifically, a request body being read that exceeds the
     * specified content cache limit.
     * <p>
     * The default implementation is empty. Subclasses may override this to throw a payload-too-large exception or the
     * like.
     * 
     * @param contentCacheLimit
     *            the maximum number of bytes to cache per request which has just been exceeded
     */
    @Override
    public void handleContentOverflow(int contentCacheLimit) {

    }

}
