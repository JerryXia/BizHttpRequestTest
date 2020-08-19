package com.github.jerryxia.devhelper.support.json;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.github.jerryxia.devhelper.log.LogEntry;
import com.github.jerryxia.devhelper.log.LogEntryStorageQueryResult;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordStorageQueryResult;
import com.github.jerryxia.devhelper.task.TaskRunRecord;
import com.github.jerryxia.devhelper.task.TaskRunRecordStorageQueryResult;

/**
 * @author Administrator
 *
 */
public class JSONWriter {

    private static final Object EVAL_VALUE_NULL  = new Object();

    private StringBuilder    out;



    public JSONWriter(){
        this.out = new StringBuilder();
    }

    public void writeArrayStart() {
        write('[');
    }

    public void writeComma() {
        write(',');
    }

    public void writeArrayEnd() {
        write(']');
    }
    
    public void writeNull() {
        write("null");
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void writeObject(Object o) {
        if (o == null) {
            writeNull();
            return;
        }

        if (o instanceof String) {
            writeString((String) o);
            return;
        }

        if (o instanceof Number) {
            write(o.toString());
            return;
        }

        if (o instanceof Boolean) {
            write(o.toString());
            return;
        }

        if (o instanceof Date) {
            writeDate((Date) o);
            return;
        }

        if (o instanceof Collection) {
            writeArray((Collection) o);
            return;
        }

        if (o instanceof Throwable) {
            writeError((Throwable) o);
            return;
        }

        if (o instanceof int[]) {
            int[] array = (int[]) o;
            write('[');
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    write(',');
                }
                write(array[i]);
            }
            write(']');
            return;
        }

        if (o instanceof long[]) {
            long[] array = (long[]) o;
            write('[');
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    write(',');
                }
                write(array[i]);
            }
            write(']');
            return;
        }

        if (o instanceof String[]) {
            String[] array = (String[]) o;
            write('[');
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    write(',');
                }
                writeString(array[i]);
            }
            write(']');
            return;
        }

//        if (o instanceof TabularData) {
//            writeTabularData((TabularData) o);
//            return;
//        }
        if (o instanceof HttpRequestRecordStorageQueryResult) {
            writeHttpRequestRecordStorageQueryResultData((HttpRequestRecordStorageQueryResult) o);
            return;
        }
        if (o instanceof LogEntryStorageQueryResult) {
            writeLogEntryStorageQueryResultData((LogEntryStorageQueryResult) o);
            return;
        }
        if (o instanceof TaskRunRecordStorageQueryResult) {
            writeTaskRunRecordStorageQueryResultData((TaskRunRecordStorageQueryResult) o);
            return;
        }
        if (o instanceof LogEntry) {
            writeLogEntryData((LogEntry) o);
            return;
        }
        if (o instanceof HttpRequestRecord) {
            writeHttpRequestRecordData((HttpRequestRecord) o);
            return;
        }
        if (o instanceof TaskRunRecord) {
            writeTaskRunRecordData((TaskRunRecord) o);
            return;
        }

        if (o instanceof Map) {
            writeMap((Map) o);
            return;
        }
        
        if (o == EVAL_VALUE_NULL) {
            write("null");
            return;
        }

        throw new IllegalArgumentException("not support type : " + o.getClass());
    }

    public void writeDate(Date date) {
        if (date == null) {
            writeNull();
            return;
        }
        //SimpleDataFormat is not thread-safe, we need to make it local.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        writeString(dateFormat.format(date));
    }

    public void writeError(Throwable error) {
        if (error == null) {
            writeNull();
            return;
        }

        write("{\"Class\":");
        writeString(error.getClass().getName());
        write(",\"Message\":");
        writeString(error.getMessage());
        write(",\"StackTrace\":");
        writeString(getStackTrace(error));
        write('}');
    }

    public void writeArray(Object[] array) {
        if (array == null) {
            writeNull();
            return;
        }

        write('[');

        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                write(',');
            }
            writeObject(array[i]);
        }

        write(']');
    }

    public void writeArray(Collection<Object> list) {
        if (list == null) {
            writeNull();
            return;
        }

        int entryIndex = 0;
        write('[');

        for (Object entry : list) {
            if (entryIndex != 0) {
                write(',');
            }
            writeObject(entry);
            entryIndex++;
        }

        write(']');
    }

    public void writeString(String text) {
        if (text == null) {
            writeNull();
            return;
        }
        
        write('"');
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == '"') {
                write("\\\"");
            } else if (c == '\n') {
                write("\\n");
            } else if (c == '\r') {
                write("\\r");
            } else if (c == '\\') {
                write("\\\\");
            } else if (c == '\t') {
                write("\\t");
            } else if (c < 16) {
                write("\\u000");
                write(Integer.toHexString(c));
            } else if (c < 32) {
                write("\\u00");
                write(Integer.toHexString(c));
            } else if (c >= 0x7f && c <= 0xA0) {
                write("\\u00");
                write(Integer.toHexString(c));
            } else {
                write(c);
            }
        }
        write('"');
    }

//    public void writeTabularData(TabularData tabularData) {
//        if (tabularData == null) {
//            writeNull();
//            return;
//        }
//
//        int entryIndex = 0;
//        write('[');
//
//        for (Object item : tabularData.values()) {
//            if (entryIndex != 0) {
//                write(',');
//            }
//            CompositeData row = (CompositeData) item;
//            writeCompositeData(row);
//
//            entryIndex++;
//        }
//        write(']');
//    }

    public void writeHttpRequestRecordStorageQueryResultData(HttpRequestRecordStorageQueryResult httpRequestRecordStorageQueryResultData) {
        if (httpRequestRecordStorageQueryResultData == null) {
            writeNull();
            return;
        }

        write('{');

        writeString("lastIndex");
        write(':');
        write(httpRequestRecordStorageQueryResultData.getLastIndex());
        write(',');

        writeString("list");
        write(':');
        writeObject(httpRequestRecordStorageQueryResultData.getList());

        write('}');
    }

    public void writeLogEntryStorageQueryResultData(LogEntryStorageQueryResult logEntryStorageQueryResult) {
        if (logEntryStorageQueryResult == null) {
            writeNull();
            return;
        }

        write('{');

        writeString("lastIndex");
        write(':');
        write(logEntryStorageQueryResult.getLastIndex());
        write(',');

        writeString("list");
        write(':');
        writeObject(logEntryStorageQueryResult.getList());

        write('}');
    }

    public void writeTaskRunRecordStorageQueryResultData(TaskRunRecordStorageQueryResult taskRunRecordStorageQueryResult) {
        if (taskRunRecordStorageQueryResult == null) {
            writeNull();
            return;
        }

        write('{');

        writeString("lastIndex");
        write(':');
        write(taskRunRecordStorageQueryResult.getLastIndex());
        write(',');

        writeString("list");
        write(':');
        writeObject(taskRunRecordStorageQueryResult.getList());

        write('}');
    }

    public void writeLogEntryData(LogEntry logEntryData) {
        if (logEntryData == null) {
            writeNull();
            return;
        }

        write('{');

        writeString("id");
        write(':');
        write(logEntryData.getId());
        write(',');

        writeString("httpRequestRecordId");
        write(':');
        writeString(logEntryData.getHttpRequestRecordId());
        write(',');

        writeString("host");
        write(':');
        writeString(logEntryData.getHost());
        write(',');

        writeString("ip");
        write(':');
        writeString(logEntryData.getIp());
        write(',');

        writeString("loggerName");
        write(':');
        writeString(logEntryData.getLoggerName());
        write(',');

        writeString("message");
        write(':');
        writeString(logEntryData.getMessage());
        write(',');

        writeString("threadName");
        write(':');
        writeString(logEntryData.getThreadName());
        write(',');

        writeString("timeStamp");
        write(':');
        write(logEntryData.getTimeStamp());
        write(',');

        writeString("level");
        write(':');
        writeString(logEntryData.getLevel());

        write('}');
    }

    public void writeHttpRequestRecordData(HttpRequestRecord httpRequestRecordData) {
        if (httpRequestRecordData == null) {
            writeNull();
            return;
        }

        write('{');

        writeString("id");
        write(':');
        writeString(httpRequestRecordData.getId());
        write(',');

        writeString("type");
        write(':');
        write(httpRequestRecordData.getType().getValue());
        write(',');

        writeString("timeStamp");
        write(':');
        write(httpRequestRecordData.getTimeStamp());
        write(',');

        writeString("method");
        write(':');
        writeObject(httpRequestRecordData.getMethod());
        write(',');

        writeString("requestURL");
        write(':');
        writeString(httpRequestRecordData.getRequestURL());
        write(',');

        writeString("requestURI");
        write(':');
        writeString(httpRequestRecordData.getRequestURI());
        write(',');

        writeString("queryString");
        write(':');
        writeString(httpRequestRecordData.getQueryString());
        write(',');

        writeString("contentType");
        write(':');
        writeString(httpRequestRecordData.getContentType());
        write(',');

        writeString("parameterMap");
        write(':');
        writeObject(httpRequestRecordData.getParameterMap());
        write(',');

        writeString("payload");
        write(':');
        writeString(httpRequestRecordData.getPayload());
        write(',');

        writeString("headers");
        write(':');
        writeObject(httpRequestRecordData.getHeaders());

        write('}');
    }

    public void writeTaskRunRecordData(TaskRunRecord taskRunRecordData) {
        if (taskRunRecordData == null) {
            writeNull();
            return;
        }

        write('{');

        writeString("id");
        write(':');
        writeString(taskRunRecordData.getId());
        write(',');

        writeString("type");
        write(':');
        write(taskRunRecordData.getType().getCode());
        write(',');

        writeString("startTimeStamp");
        write(':');
        write(taskRunRecordData.getStartTimeStamp());
        write(',');

        writeString("endTimeStamp");
        write(':');
        write(taskRunRecordData.getEndTimeStamp());
        write(',');

        writeString("declaringClass");
        write(':');
        writeString(taskRunRecordData.getDeclaringClass());
        write(',');

        writeString("method");
        write(':');
        writeString(taskRunRecordData.getMethod());
        write(',');

        writeString("parameterMap");
        write(':');
        writeObject(taskRunRecordData.getParameterMap());
        write(',');

        writeString("hostName");
        write(':');
        writeString(taskRunRecordData.getHostName());
        write(',');

        writeString("ip");
        write(':');
        writeString(taskRunRecordData.getIp());

        writeString("ip");
        write(':');
        writeString(taskRunRecordData.getInstanceName());

        write('}');
    }

    public void writeMap(Map<String, Object> map) {
        if (map == null) {
            writeNull();
            return;
        }

        int entryIndex = 0;
        write('{');
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entryIndex != 0) {
                write(',');
            }

            writeString(entry.getKey());
            write(':');
            writeObject(entry.getValue());

            entryIndex++;
        }

        write('}');
    }

    protected void write(String text) {
        out.append(text);
    }

    protected void write(char c) {
        out.append(c);
    }

    protected void write(int c) {
        out.append(c);
    }

    protected void write(long c) {
        out.append(c);
    }

    @Override
    public String toString() {
        return out.toString();
    }

    private String getStackTrace(Throwable ex) {
        StringWriter buf = new StringWriter();
        ex.printStackTrace(new PrintWriter(buf));

        return buf.toString();
    }
}