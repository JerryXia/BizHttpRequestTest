package com.github.jerryxia.devhelper.requestcapture.support.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jerryxia.devhelper.Constants;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordStorageQueryResult;
import com.github.jerryxia.devhelper.requestcapture.log.LogEntry;
import com.github.jerryxia.devhelper.requestcapture.log.LogEntryStorageQueryResult;
import com.github.jerryxia.devhelper.requestcapture.support.RequestCaptureConstants;
import com.github.jerryxia.devhelper.requestcapture.support.json.JSONWriter;
import com.github.jerryxia.devhelper.snoop.JvmMemoryInfo;
import com.github.jerryxia.devhelper.snoop.MemoryPoolMXBeanInfo;
import com.github.jerryxia.devhelper.snoop.Monitor;
import com.github.jerryxia.devhelper.web.WebConstants;

/**
 * @author guqiankun
 *
 */
@SuppressWarnings("serial")
public class RequestCaptureWebServlet extends AbstractResourceServlet {

    private final static int RESULT_CODE_SUCCESS = 1;
    private final static int RESULT_CODE_ERROR   = -1;

    public RequestCaptureWebServlet() {
        super("com/github/jerryxia/devhelper/requestcapture/support/servlet/resources");
    }

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response)
            throws ServletException, IOException {

        String filePath = getFilePath(fileName);

        if (fileName.endsWith(".jpg")) {
            byte[] bytes = super.readByteArrayFromResource(filePath);
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }

            return;
        }

        String text = super.readFromResource(filePath);
        if (text == null) {
            filePath = getFilePath("/index.html");
            text = super.readFromResource(filePath);
        }

        if (filePath.endsWith(".html")) {
            response.setContentType("text/html;charset=utf-8");
        } else if (fileName.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else if (fileName.endsWith(".js")) {
            response.setContentType("text/javascript;charset=utf-8");
        }
        response.getWriter().write(text);

        if (filePath.endsWith("index.html")) {
            response.getWriter().write("<script type=\"text/javascript\">const PATH_PREFIX = '");
            response.getWriter().write(uri);
            response.getWriter().write("';</script>");
            response.getWriter().write("<script src=\"");
            response.getWriter().write(uri);
            response.getWriter().write("/js/app.js?v=");
            response.getWriter().write(Constants.VERSION);
            response.getWriter().write("\"></script> </body></html>");
        }
    }

    @Override
    protected String process(String url, HttpServletRequest req) {
        long startTime = System.nanoTime();
        Map<String, String> parameters = getParameters(url);
        String jsonpCallback = parameters.get("callback");
        String resp = null;

        if (url.startsWith("/snoop.json")) {
            resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS, buildSnoopResult(req),
                    getServerStat(startTime));
        }

        if (url.startsWith("/allapirecords.json")) {
            HttpRequestRecordStorageQueryResult result = RequestCaptureConstants.RECORD_MANAGER
                    .currentHttpRequestRecordStorage().queryAll();
            resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS, result, getServerStat(startTime));
        }
        if (url.startsWith("/apirecords.json")) {
            int startIndex = Integer.parseInt(parameters.get("startIndex"));
            int endIndex = Integer.parseInt(parameters.get("endIndex"));
            HttpRequestRecordStorageQueryResult result = RequestCaptureConstants.RECORD_MANAGER
                    .currentHttpRequestRecordStorage().queryNextList(startIndex, endIndex);
            resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS, result, getServerStat(startTime));
        }
        if (url.startsWith("/apirecord.json")) {
            String apiRecordId = parameters.get("id");
            if (apiRecordId != null && apiRecordId.length() > 0) {
                HttpRequestRecordStorageQueryResult result = RequestCaptureConstants.RECORD_MANAGER
                        .currentHttpRequestRecordStorage().queryAll();
                resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS,
                        filterRecordsById(result.getList(), apiRecordId), getServerStat(startTime));
            } else {
                resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS, Collections.emptyList(),
                        getServerStat(startTime));
            }
        }
        if (url.startsWith("/apirecordlogs.json")) {
            String apiRecordId = parameters.get("id");
            if (apiRecordId != null && apiRecordId.length() > 0) {
                LogEntryStorageQueryResult result = RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager()
                        .currentLogEntryStorage().queryAll();
                resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS,
                        filterLogsById(result.getList(), apiRecordId), getServerStat(startTime));
            } else {
                resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS, Collections.emptyList(),
                        getServerStat(startTime));
            }
        }
        if (url.startsWith("/exceptionRecords.json")) {
            String encLevels = parameters.get("levels");
            if (encLevels != null && encLevels.length() > 0) {
                String decLevels = null;
                try {
                    decLevels = URLDecoder.decode(encLevels, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    //
                }
                if (decLevels != null) {
                    String[] levels = decLevels.split(",");
                    LogEntryStorageQueryResult allLogs = RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager()
                            .currentLogEntryStorage().queryAll();
                    HashMap<String, ArrayList<String>> result = filterRecordsByLogLevel(levels, allLogs.getList());
                    resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS, result, getServerStat(startTime));
                } else {
                    resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS, Collections.emptyMap(),
                            getServerStat(startTime));
                }
            } else {
                resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS, Collections.emptyMap(),
                        getServerStat(startTime));
            }
        }

        if (url.startsWith("/alllogs.json")) {
            LogEntryStorageQueryResult result = RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager()
                    .currentLogEntryStorage().queryAll();
            resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS, result, getServerStat(startTime));
        }
        if (url.startsWith("/logs.json")) {
            int startIndex = Integer.parseInt(parameters.get("startIndex"));
            int endIndex = Integer.parseInt(parameters.get("endIndex"));
            LogEntryStorageQueryResult result = RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager()
                    .currentLogEntryStorage().queryNextList(startIndex, endIndex);
            resp = returnJsonpResult(jsonpCallback, RESULT_CODE_SUCCESS, result, getServerStat(startTime));
        }

        if (resp == null) {
            resp = returnJSONResult(RESULT_CODE_ERROR, null, getServerStat(startTime));
        }
        return resp;
    }

    private Map<String, String> getParameters(String url) {
        if (url == null || (url = url.trim()).length() == 0) {
            return Collections.<String, String> emptyMap();
        }

        String parametersStr = subString(url, "?", null);
        if (parametersStr == null || parametersStr.length() == 0) {
            return Collections.<String, String> emptyMap();
        }

        String[] parametersArray = parametersStr.split("&");
        Map<String, String> parameters = new LinkedHashMap<String, String>();

        for (String parameterStr : parametersArray) {
            int index = parameterStr.indexOf("=");
            if (index <= 0) {
                continue;
            }

            String name = parameterStr.substring(0, index);
            String value = parameterStr.substring(index + 1);
            parameters.put(name, value);
        }
        return parameters;
    }

    private String subString(String src, String start, String to) {
        int indexFrom = start == null ? 0 : src.indexOf(start);
        int indexTo = to == null ? src.length() : src.indexOf(to);
        if (indexFrom < 0 || indexTo < 0 || indexFrom > indexTo) {
            return null;
        }

        if (null != start) {
            indexFrom += start.length();
        }

        return src.substring(indexFrom, indexTo);
    }

    private Map<String, Long> getServerStat(long startTime) {
        Map<String, Long> map = new HashMap<String, Long>(2);
        map.put("time", System.currentTimeMillis());
        map.put("generated", System.nanoTime() - startTime);
        return map;
    }

    private String returnJsonpResult(String jsonpCallback, int resultCode, Object content,
            Map<String, Long> serverStat) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("code", resultCode);
        dataMap.put("data", content);
        dataMap.put("serverstat", serverStat);
        return jsonpCallback + "(" + toJSONString(dataMap) + ")";
    }

    private String returnJSONResult(int resultCode, Object content, Map<String, Long> serverStat) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("code", resultCode);
        dataMap.put("data", content);
        dataMap.put("serverstat", serverStat);
        return toJSONString(dataMap);
    }

    private String toJSONString(Object o) {
        JSONWriter writer = new JSONWriter();
        writer.writeObject(o);
        return writer.toString();
    }

    private List<HttpRequestRecord> filterRecordsById(List<HttpRequestRecord> apiRecords, String id) {
        ArrayList<HttpRequestRecord> arrayList = new ArrayList<HttpRequestRecord>(1);
        Iterator<HttpRequestRecord> iterator = apiRecords.iterator();
        while (iterator.hasNext()) {
            HttpRequestRecord item = iterator.next();
            if (id.equalsIgnoreCase(item.getId())) {
                arrayList.add(item);
            }
        }
        return arrayList;
    }

    private List<LogEntry> filterLogsById(List<LogEntry> alllogs, String id) {
        ArrayList<LogEntry> arrayList = new ArrayList<LogEntry>(8);
        Iterator<LogEntry> iterator = alllogs.iterator();
        while (iterator.hasNext()) {
            LogEntry item = iterator.next();
            if (id.equalsIgnoreCase(item.getHttpRequestRecordId())) {
                arrayList.add(item);
            }
        }
        return arrayList;
    }

    private HashMap<String, ArrayList<String>> filterRecordsByLogLevel(String[] levels, List<LogEntry> logs) {
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>(2);
        if (levels.length > 0) {
            ArrayList<String> distinctedLevels = new ArrayList<String>(levels.length);
            for (String level : levels) {
                level = level.toUpperCase();
                switch (level) {
                case "TRACE":
                case "DEBUG":
                case "INFO":
                case "WARN":
                case "ERROR":
                case "FATAL":
                    if (distinctedLevels.indexOf(level) == -1) {
                        distinctedLevels.add(level);
                    }
                    break;
                default:
                    break;
                }
            }

            if (distinctedLevels.size() > 0) {
                for (String level : distinctedLevels) {
                    if (map.containsKey(level) == false) {
                        map.put(level, new ArrayList<String>());
                    }
                }

                Iterator<LogEntry> logIterator = logs.iterator();
                while (logIterator.hasNext()) {
                    LogEntry log = logIterator.next();
                    ArrayList<String> recordIds = map.get(log.getLevel());
                    if (recordIds != null) {
                        if (log.getHttpRequestRecordId() != null && log.getHttpRequestRecordId().length() > 0
                                && recordIds.indexOf(log.getHttpRequestRecordId()) == -1) {
                            recordIds.add(log.getHttpRequestRecordId());
                        }
                    }
                }
            }
        }
        return map;
    }

    private HashMap<String, Object> buildSnoopResult(HttpServletRequest req) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        HashMap<String, Object> libInfo = new HashMap<String, Object>();
        libInfo.put("version", Constants.VERSION);
        libInfo.put("serverOsName", Constants.SERVER_OS_NAME);
        libInfo.put("javaVMName", Constants.JAVA_VM_NAME);
        libInfo.put("javaVersion", Constants.JAVA_VERSION);
        libInfo.put("javaHome", Constants.JAVA_HOME);
        libInfo.put("javaClassPath", Constants.JAVA_CLASS_PATH);
        libInfo.put("startTime", Constants.START_TIME);

        libInfo.put("requestIdInitFilterEnabled", WebConstants.REQUEST_ID_INIT_FILTER_ENABLED);
        libInfo.put("requestCaptureFilterEnabled", WebConstants.REQUEST_CAPTURE_FILTER_ENABLED);
        libInfo.put("requestResponseLogInterceptorEnabled", WebConstants.REQUEST_RESPONSE_LOG_INTERCEPTOR_ENABLED);

        libInfo.put("logExtEnabled", RequestCaptureConstants.LOG_EXT_ENABLED);
        libInfo.put("logExtEnabledComponent", RequestCaptureConstants.LOG_EXT_ENABLED_MAP);

        libInfo.put("reqProducerSuccessCount", RequestCaptureConstants.RECORD_MANAGER.viewHttpRequestRecordEventStat()
                .getProducerSuccessCount().get());
        libInfo.put("reqProducerFailCount",
                RequestCaptureConstants.RECORD_MANAGER.viewHttpRequestRecordEventStat().getProducerFailCount().get());
        libInfo.put("reqConsumerSuccessCount",
                RequestCaptureConstants.RECORD_MANAGER.viewHttpRequestRecordEventStat().getConsumerSuccessCount());
        libInfo.put("reqConsumerFailCount",
                RequestCaptureConstants.RECORD_MANAGER.viewHttpRequestRecordEventStat().getConsumerFailCount());

        libInfo.put("logProducerSuccessCount", RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager()
                .viewLogEntryEventStat().getProducerSuccessCount().get());
        libInfo.put("logProducerFailCount", RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager()
                .viewLogEntryEventStat().getProducerFailCount().get());
        libInfo.put("logConsumerSuccessCount", RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager()
                .viewLogEntryEventStat().getConsumerSuccessCount());
        libInfo.put("logConsumerFailCount", RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager()
                .viewLogEntryEventStat().getConsumerFailCount());

        result.put("libInfo", libInfo);

        JvmMemoryInfo jvmMemoryInfo = Monitor.currentMonitor().jvmMemoryInfo();

        HashMap<String, String> memoryMXBean = new HashMap<String, String>();
        memoryMXBean.put("heapMemoryUsag", jvmMemoryInfo.getMemoryMXBeanInfo().getHeapMemoryUsag().toString());
        memoryMXBean.put("nonHeapMemoryUsag", jvmMemoryInfo.getMemoryMXBeanInfo().getNonHeapMemoryUsag().toString());
        result.put("memoryMXBean", memoryMXBean);

        ArrayList<HashMap<String, String>> beans = new ArrayList<HashMap<String, String>>(
                jvmMemoryInfo.getMemoryPoolMXBeansInfo().size());
        for (MemoryPoolMXBeanInfo memoryPoolMXBeanInfo : jvmMemoryInfo.getMemoryPoolMXBeansInfo()) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("name", memoryPoolMXBeanInfo.getName());
            item.put("type", memoryPoolMXBeanInfo.getType().toString());
            item.put("memoryUsage", memoryPoolMXBeanInfo.getMemoryUsage() == null ? null
                    : memoryPoolMXBeanInfo.getMemoryUsage().toString());
            item.put("peakMemoryUsage", memoryPoolMXBeanInfo.getPeakMemoryUsage() == null ? null
                    : memoryPoolMXBeanInfo.getPeakMemoryUsage().toString());
            item.put("collectionUsage", memoryPoolMXBeanInfo.getCollectionUsage() == null ? null
                    : memoryPoolMXBeanInfo.getCollectionUsage().toString());
            beans.add(item);
        }
        result.put("memoryPoolMXBeans", beans);

        LinkedHashMap<String, String> requestAttributes = new LinkedHashMap<String, String>();
        Enumeration<String> attributeNames = req.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object val = req.getAttribute(attributeName);
            requestAttributes.put(attributeName, val.toString());
        }
        result.put("requestAttributes", requestAttributes);
        return result;
    }
}
