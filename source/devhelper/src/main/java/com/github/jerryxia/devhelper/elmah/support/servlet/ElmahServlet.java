/**
 * 
 */
package com.github.jerryxia.devhelper.elmah.support.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jerryxia.devhelper.elmah.*;
import org.apache.commons.lang3.StringUtils;

import com.github.jerryxia.devhelper.Constants;
import com.github.jerryxia.devhelper.support.json.RuntimeJsonComponentProviderFactory;
import com.github.jerryxia.devhelper.util.ClassUtil;
import com.github.jerryxia.devutil.SystemClock;
import com.vip.vjtools.vjkit.io.FilePathUtil;
import com.vip.vjtools.vjkit.io.ResourceUtil;
import com.vip.vjtools.vjkit.net.NetUtil;

/**
 * require jackson component
 * 
 * @author guqk
 *
 */
@SuppressWarnings("serial")
public class ElmahServlet extends HttpServlet {
    public static final String PARAM_NAME_ERROR_RECORD_STORAGE           = "errorRecordStorage";
    public static final String PARAM_NAME_ERROR_RECORD_FILE_STORAGE_PATH = "errorRecordFileStoragePath";

    @Override
    public void init() throws ServletException {
        getServletContext().log("ElmahServlet init");
        String errorRecordStorage = getServletConfig().getInitParameter(PARAM_NAME_ERROR_RECORD_STORAGE);
        if (errorRecordStorage == null) {
            errorRecordStorage = "";
        }
        ErrorRecordNoOpStorage defaultErrorRecordStorage = new ErrorRecordNoOpStorage();
        ErrorInfoCapture.setDefaultErrorRecordStorage(defaultErrorRecordStorage);

        switch (errorRecordStorage) {
        case "ErrorRecordFileStorage":
            String errorRecordFileStoragePath = getServletConfig().getInitParameter(PARAM_NAME_ERROR_RECORD_FILE_STORAGE_PATH);
            if (errorRecordFileStoragePath == null || errorRecordFileStoragePath.length() == 0) {
                errorRecordFileStoragePath = FilePathUtil.concat(Constants.JAVA_IO_TMPDIR, "elmah");
            }
            try {
                ErrorRecordFileStorage errorRecordFileStorage = new ErrorRecordFileStorage(errorRecordFileStoragePath);
                ErrorInfoCapture.setErrorRecordStorage(errorRecordFileStorage);
            } catch (IOException e) {
                throw new ServletException(e.getMessage(), e);
            }
            break;
        case "":
            getServletContext().log("errorRecordStorage use the defaultErrorRecordStorage.");
            ErrorInfoCapture.setErrorRecordStorage(defaultErrorRecordStorage);
            break;
        default:
            try {
                Class<?> errorRecordStorageClass = ClassUtil.classForName(errorRecordStorage);
                if (ErrorRecordStorage.class.isAssignableFrom(errorRecordStorageClass)) {
                    ErrorInfoCapture.setErrorRecordStorage(errorRecordStorageClass);
                } else {
                    ErrorInfoCapture.setErrorRecordStorage(defaultErrorRecordStorage);
                }
            } catch (ClassNotFoundException e) {
                getServletContext().log("errorRecordStorage not found, use the defaultErrorRecordStorage.", e);
                ErrorInfoCapture.setErrorRecordStorage(defaultErrorRecordStorage);
            }
            break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");

        String contextPath = req.getContextPath();
        String servletPath = req.getServletPath();
        String requestURI = req.getRequestURI();
        if (contextPath == null) {
            // root context
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(uri.length());

        if ("/".equals(path)) {
            String fileContent = ResourceUtil
                    .toString("com/github/jerryxia/devhelper/elmah/support/servlet/resources/index.html");
            String finalContent = StringUtils.replace(fileContent, "<script>const PATH_PREFIX='';</script>",
                    "<script>const PATH_PREFIX='" + uri + "';</script>");
            resp.getWriter().write(finalContent);
        }

        if ("/detail".equals(path)) {
            String fileContent = ResourceUtil
                    .toString("com/github/jerryxia/devhelper/elmah/support/servlet/resources/detail.html");
            String finalContent = StringUtils.replace(fileContent, "<script>const PATH_PREFIX='';</script>",
                    "<script>const PATH_PREFIX='" + uri + "';</script>");
            resp.getWriter().write(finalContent);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String path = getPath(req);

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("contextPath", req.getContextPath());
        data.put("server", NetUtil.getLocalAddress().getHostName());
        data.put("serverDate", SystemClock.now());
        data.put("libVersion", Constants.VERSION);

        if ("/errors.json".equals(path)) {
            int offset = 0;
            int limit = 0;
            try {
                offset = Integer.parseInt(req.getParameter("offset"));
                limit = Integer.parseInt(req.getParameter("limit"));
            } catch (NumberFormatException e) {
                log("解析limit参数失败", e);
            }
            PageInfo<ErrorInfo> pagedErrorInfo = ErrorInfoCapture.errorRecordStorage.queryLimitData(offset, limit);
            data.put("pagedErrorInfo", pagedErrorInfo);
            resp.getWriter().print(RuntimeJsonComponentProviderFactory.tryFindImplementation().toJson(data));
        }

        if ("/detail.json".equals(path)) {
            String id = req.getParameter("id");
            ErrorInfo errorInfo = ErrorInfoCapture.errorRecordStorage.detail(id);
            data.put("errorInfo", errorInfo);
            resp.getWriter().print(RuntimeJsonComponentProviderFactory.tryFindImplementation().toJson(data));
        }
    }

    private String getPath(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        String servletPath = req.getServletPath();
        String requestURI = req.getRequestURI();

        if (contextPath == null) {
            // root context
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(uri.length());
        return path;
    }
}
