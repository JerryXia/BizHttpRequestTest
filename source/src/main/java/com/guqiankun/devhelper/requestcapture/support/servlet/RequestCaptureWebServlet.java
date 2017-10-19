package com.guqiankun.devhelper.requestcapture.support.servlet;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.guqiankun.devhelper.requestcapture.log.LogEntry;
import com.guqiankun.devhelper.requestcapture.HttpRequestRecord;
import com.guqiankun.devhelper.requestcapture.RecordManager;
import com.guqiankun.devhelper.requestcapture.support.json.JSONWriter;

@SuppressWarnings("serial")
public class RequestCaptureWebServlet extends ResourceServlet {

    private final static int RESULT_CODE_SUCCESS = 1;
    private final static int RESULT_CODE_ERROR   = -1;

    public RequestCaptureWebServlet() {
        super("requestcapture/support/resources");
    }

    @Override
    public void init() throws ServletException {
        //
    }

    @Override
    protected String process(String url) {
        long startTime = System.nanoTime();

        String resp = null;
        Map<String, String> parameters = getParameters(url);
        if (url.equals("/alllogs.json")) {
            List<LogEntry> alllogs = RecordManager.getInstance().currentRecordLogList().getAll();
            resp = returnJSONResult(RESULT_CODE_SUCCESS, alllogs, getServerStat(startTime));
        }
        if (url.equals("/apirecords.json")) {
            List<HttpRequestRecord> allRecords = RecordManager.getInstance().currentRecordStorage().queryAll();
            resp = returnJSONResult(RESULT_CODE_SUCCESS, allRecords, getServerStat(startTime));
        }
        if (url.startsWith("/apirecordlogs.json")) {
            List<LogEntry> alllogs = RecordManager.getInstance().currentRecordLogList().getAll();
            String id = parameters.get("id");
            if (id != null && id.length() > 0) {
                resp = returnJSONResult(RESULT_CODE_SUCCESS, filterById(alllogs, id), getServerStat(startTime));
            } else {
                resp = returnJSONResult(RESULT_CODE_SUCCESS, alllogs, getServerStat(startTime));
            }
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

    private Map<String, Object> getServerStat(long startTime) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("time", System.currentTimeMillis());
        map.put("generated", System.nanoTime() - startTime);
        return map;
    }

    private String returnJSONResult(int resultCode, Object content, Map<String, Object> serverStat) {
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
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

    // private Object parse(String text) {
    // JSONParser parser = new JSONParser(text);
    // return parser.parse();
    // }

    private List<LogEntry> filterById(List<LogEntry> alllogs, String id) {
        List<LogEntry> linkedList = new LinkedList<LogEntry>();
        for (LogEntry log : alllogs) {
            if (log.getHttpRequestRecordId() != null && log.getHttpRequestRecordId().equalsIgnoreCase(id)) {
                linkedList.add(log);
            }
        }
        return linkedList;
    }
}