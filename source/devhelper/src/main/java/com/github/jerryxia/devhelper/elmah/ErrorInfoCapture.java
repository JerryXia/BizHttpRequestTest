/**
 * 
 */
package com.github.jerryxia.devhelper.elmah;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jerryxia.devhelper.support.spring.SpringTools;
import com.github.jerryxia.devhelper.util.ServletUtil;
import com.github.jerryxia.devutil.ObjectId;
import com.github.jerryxia.devutil.SystemClock;
import com.vip.vjtools.vjkit.net.NetUtil;

/**
 * @author guqk
 *
 */
public class ErrorInfoCapture {
    private static ErrorRecordStorage defaultErrorRecordStorage;

    private static Class<?>          errorRecordStorageClass;
    public static ErrorRecordStorage errorRecordStorage;

    public static void setDefaultErrorRecordStorage(ErrorRecordStorage s) {
        if (defaultErrorRecordStorage == null) {
            defaultErrorRecordStorage = s;
        }
    }

    public static void setErrorRecordStorage(ErrorRecordStorage s) {
        if (errorRecordStorage == null) {
            errorRecordStorage = s;
        }
    }

    public static void setErrorRecordStorage(Class<?> c) {
        if (errorRecordStorageClass == null) {
            errorRecordStorageClass = c;
        }
    }

    private static void tryFindErrorRecordStorageImpl() {
        if (errorRecordStorageClass != null) {
            ErrorRecordStorage errorRecordStorageImpl = null;
            Object errorRecordStorageInstance = SpringTools.getBean(errorRecordStorageClass);
            if (errorRecordStorageInstance != null) {
                errorRecordStorageImpl = (ErrorRecordStorage) errorRecordStorageInstance;
            }
            if (errorRecordStorageImpl == null) {
                errorRecordStorageImpl = defaultErrorRecordStorage;
            }
            errorRecordStorage = errorRecordStorageImpl;
        }
    }

    public static ErrorInfo capture(Throwable t, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        if (errorRecordStorage == null) {
            synchronized (ErrorInfoCapture.class) {
                if (errorRecordStorage == null) {
                    tryFindErrorRecordStorageImpl();
                }
            }
        }
        if (t == null) {
            return null;
        }
        ErrorInfo errorInfo = new ErrorInfo(ObjectId.get().toString(), SystemClock.now());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        t.printStackTrace(ps);

        String detail = null;
        try {
            detail = baos.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            detail = "";
        }
        errorInfo.setMessage(t.getMessage());
        errorInfo.setDetail(detail);
        errorInfo.setType(t.getClass().getName());
        Throwable cause = t.getCause();
        errorInfo.setSource(cause != null ? cause.getClass().getName() : "");

        errorInfo.setHost(NetUtil.getLocalAddress().getHostName());
        errorInfo.setIp(NetUtil.getLocalHost());

        if (httpResponse != null) {
            errorInfo.setStatusCode(httpResponse.getStatus());
        }

        if (httpRequest != null) {
            String method = httpRequest.getMethod();
            String requestURI = httpRequest.getRequestURI();
            String requestURL = httpRequest.getRequestURL().toString();
            String queryString = httpRequest.getQueryString();
            String contentType = httpRequest.getContentType();
            // deepClone org.apache.catalina.util.ParameterMap
            String encoding = httpRequest.getCharacterEncoding();
            if (encoding == null) {
                encoding = "UTF-8";
            }
            LinkedHashMap<String, String[]> formParameters = ServletUtil.parseFormParameters(httpRequest, encoding);
            LinkedHashMap<String, String[]> headers = ServletUtil.parseHeaders(httpRequest);

            Principal userPrincipal = httpRequest.getUserPrincipal();
            String user = userPrincipal == null ? null : userPrincipal.getName();

            errorInfo.setMethod(method);
            errorInfo.setRequestURL(requestURL);
            errorInfo.setRequestURI(requestURI);
            errorInfo.setQueryString(queryString);
            errorInfo.setContentType(contentType);
            errorInfo.setParameterMap(formParameters);
            errorInfo.setHeaders(headers);
            errorInfo.setUser(user);
        }

        if (errorRecordStorage != null) {
            errorRecordStorage.save(errorInfo);
        }
        return errorInfo;

    }

}
