package com.github.jerryxia.devhelper.requestcapture.support.json;

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
        if (o instanceof LogEntry) {
            writeLogEntryData((LogEntry) o);
            return;
        }
        if (o instanceof HttpRequestRecord) {
            writeHttpRequestRecordData((HttpRequestRecord) o);
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
        writeObject(httpRequestRecordStorageQueryResultData.getLastIndex());
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
        writeObject(logEntryStorageQueryResult.getLastIndex());
        write(',');

        writeString("list");
        write(':');
        writeObject(logEntryStorageQueryResult.getList());

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
        writeObject(logEntryData.getId());
        write(',');

        writeString("httpRequestRecordId");
        write(':');
        writeObject(logEntryData.getHttpRequestRecordId());
        write(',');

        writeString("host");
        write(':');
        writeObject(logEntryData.getHost());
        write(',');

        writeString("ip");
        write(':');
        writeObject(logEntryData.getIp());
        write(',');

        writeString("loggerName");
        write(':');
        writeObject(logEntryData.getLoggerName());
        write(',');

        writeString("message");
        write(':');
        writeObject(logEntryData.getMessage());
        write(',');

        writeString("threadName");
        write(':');
        writeObject(logEntryData.getThreadName());
        write(',');

        writeString("timeStamp");
        write(':');
        writeObject(logEntryData.getTimeStamp());
        write(',');

        writeString("level");
        write(':');
        writeObject(logEntryData.getLevel());

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
        writeObject(httpRequestRecordData.getId());
        write(',');

        writeString("type");
        write(':');
        writeObject(httpRequestRecordData.getType().getValue());
        write(',');

        writeString("timeStamp");
        write(':');
        writeObject(httpRequestRecordData.getTimeStamp());
        write(',');

        writeString("method");
        write(':');
        writeObject(httpRequestRecordData.getMethod());
        write(',');

        writeString("requestURL");
        write(':');
        writeObject(httpRequestRecordData.getRequestURL());
        write(',');

        writeString("requestURI");
        write(':');
        writeObject(httpRequestRecordData.getRequestURI());
        write(',');

        writeString("queryString");
        write(':');
        writeObject(httpRequestRecordData.getQueryString());
        write(',');

        writeString("contentType");
        write(':');
        writeObject(httpRequestRecordData.getContentType());
        write(',');

        writeString("parameterMap");
        write(':');
        writeObject(httpRequestRecordData.getParameterMap());
        write(',');

        writeString("payload");
        write(':');
        writeObject(httpRequestRecordData.getPayload());
        write(',');

        writeString("headers");
        write(':');
        writeObject(httpRequestRecordData.getHeaders());

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