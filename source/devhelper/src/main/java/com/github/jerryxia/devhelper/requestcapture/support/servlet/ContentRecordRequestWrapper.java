/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.support.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Administrator
 *
 */
public class ContentRecordRequestWrapper extends HttpServletRequestWrapper {

    private static final String FORM_CONTENT_TYPE          = "application/x-www-form-urlencoded";
    private static final String HTTP_METHOD_POST           = "POST";
    private static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";

    private final ByteArrayOutputStream cachedContent;

    private final Integer contentCacheLimit;

    private ServletInputStream inputStream;

    private BufferedReader reader;

    private ContentOverflowListener contentOverflowListener;
    private ContentEndListener contentEndListener;

    /**
     * Create a new ContentCachingRequestWrapper for the given servlet request.
     * 
     * @param request
     *            the original servlet request
     * @param contentOverflowListener
     * @param contentEndListener
     */
    public ContentRecordRequestWrapper(HttpServletRequest request, ContentOverflowListener contentOverflowListener,
            ContentEndListener contentEndListener) {
        super(request);
        int contentLength = request.getContentLength();
        this.cachedContent = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
        this.contentCacheLimit = null;
        this.contentOverflowListener = contentOverflowListener;
        this.contentEndListener = contentEndListener;
    }

    /**
     * Create a new ContentCachingRequestWrapper for the given servlet request.
     * 
     * @param request
     *            the original servlet request
     * @param contentCacheLimit
     *            the maximum number of bytes to cache per request
     * @param contentOverflowListener
     * @param contentEndListener
     * @since 4.3.6
     * @see #handleContentOverflow(int)
     */
    public ContentRecordRequestWrapper(HttpServletRequest request, int contentCacheLimit,
            ContentOverflowListener contentOverflowListener, ContentEndListener contentEndListener) {
        super(request);
        this.cachedContent = new ByteArrayOutputStream(contentCacheLimit);
        this.contentCacheLimit = contentCacheLimit;
        this.contentOverflowListener = contentOverflowListener;
        this.contentEndListener = contentEndListener;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = new ContentRecordInputStream(getRequest().getInputStream());
        }
        return this.inputStream;
    }

    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return (enc != null ? enc : DEFAULT_CHARACTER_ENCODING);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return this.reader;
    }

    @Override
    public String getParameter(String name) {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterMap();
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterValues(name);
    }

    private boolean isFormPost() {
        String contentType = getContentType();
        return (contentType != null && contentType.contains(FORM_CONTENT_TYPE) && HTTP_METHOD_POST.equals(getMethod()));
    }

    private void writeRequestParametersToCachedContent() {
        try {
            if (this.cachedContent.size() == 0) {
                String requestEncoding = getCharacterEncoding();
                Map<String, String[]> form = super.getParameterMap();
                for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext();) {
                    String name = nameIterator.next();
                    List<String> values = Arrays.asList(form.get(name));
                    for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext();) {
                        String value = valueIterator.next();
                        this.cachedContent.write(URLEncoder.encode(name, requestEncoding).getBytes());
                        if (value != null) {
                            this.cachedContent.write('=');
                            this.cachedContent.write(URLEncoder.encode(value, requestEncoding).getBytes());
                            if (valueIterator.hasNext()) {
                                this.cachedContent.write('&');
                            }
                        }
                    }
                    if (nameIterator.hasNext()) {
                        this.cachedContent.write('&');
                    }
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write request parameters to cached content", ex);
        }
    }

    /**
     * Return the cached request content as a byte array.
     * <p>
     * The returned array will never be larger than the content cache limit.
     * 
     * @see #ContentCachingRequestWrapper(HttpServletRequest, int)
     */
    public byte[] getContentAsByteArray() {
        return this.cachedContent.toByteArray();
    }

    private class ContentRecordInputStream extends ServletInputStream {
        private final ServletInputStream is;

        private boolean overflow = false;
        private boolean end      = false;

        public ContentRecordInputStream(ServletInputStream is) {
            this.is = is;
        }

        @Override
        public int read() throws IOException {
            int ch = this.is.read();
            if (this.overflow) {
                return ch;
            } else {
                if (ch == -1) {
                    if (this.end == false) {
                        this.end = true;
                        if(contentEndListener != null) {
                            contentEndListener.handleContentEnd(cachedContent, getCharacterEncoding());
                            contentEndListener = null;
                        }
                    }
                } else {
                    if (contentCacheLimit != null && cachedContent.size() == contentCacheLimit) {
                        this.overflow = true;
                        if(contentOverflowListener != null) {
                            contentOverflowListener.handleContentOverflow(contentCacheLimit);
                            contentOverflowListener = null;
                        }
                    } else {
                        cachedContent.write(ch);
                    }
                }
                return ch;
            }
        }
    }
}