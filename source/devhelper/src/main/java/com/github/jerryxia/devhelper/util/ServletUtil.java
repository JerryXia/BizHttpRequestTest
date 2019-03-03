/**
 * 
 */
package com.github.jerryxia.devhelper.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * @author guqk
 *
 */
public class ServletUtil {
    public static String[] toStringArray(Enumeration<String> enumeration) {
        if (enumeration != null) {
            // List<String> lists = EnumerationUtils.toList(enumeration);
            final ArrayList<String> list = new ArrayList<String>(1);
            while (enumeration.hasMoreElements()) {
                list.add(enumeration.nextElement());
            }
            String[] arr = new String[list.size()];
            return list.toArray(arr);
        } else {
            return null;
        }
    }

    public static String[] toStringArray(ArrayList<String> list) {
        if (list != null) {
            String[] arr = new String[list.size()];
            return list.toArray(arr);
        } else {
            return null;
        }
    }

    public static LinkedHashMap<String, String[]> parseFormParameters(HttpServletRequest request, String encoding) {
        LinkedHashMap<String, String[]> formParameters = new LinkedHashMap<String, String[]>();
        LinkedHashMap<String, ArrayList<String>> queryParameters = parseQueryParameters(request, encoding);
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String[] parameterValues = request.getParameterValues(parameterName);
            ArrayList<String> queryParameterValues = queryParameters.get(parameterName);
            // from parameterValues exclude queryParameterValues
            ArrayList<String> formParameterValues = new ArrayList<String>(1);
            for (String parameterValue : parameterValues) {
                if (queryParameterValues == null) {
                    formParameterValues.add(parameterValue);
                } else {
                    int queryParameterIndex = queryParameterValues.indexOf(parameterValue);
                    if (queryParameterIndex > -1) {
                        queryParameterValues.remove(queryParameterIndex);
                    } else {
                        formParameterValues.add(parameterValue);
                    }
                }
            }
            // if formParameterValues is not empty
            if (formParameterValues.size() > 0) {
                formParameters.put(parameterName, toStringArray(formParameterValues));
            }
        }
        return formParameters;
    }

    public static LinkedHashMap<String, ArrayList<String>> parseQueryParameters(HttpServletRequest request, String encoding) {
        LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<String, ArrayList<String>>();
        String queryString = request.getQueryString();
        if (queryString == null || queryString.length() == 0) {
            return map;
        }
        String[] entries = queryString.split("&");
        if (entries.length == 0) {
            return map;
        }
        for (String entry : entries) {
            try {
                int splitterIndex = entry.indexOf('=');
                String key = null;
                String value = null;
                if (splitterIndex < 0) {
                    // sample: ?aaa  request.getParameterValues("aaa") == [""];
                    key = URLDecoder.decode(entry, encoding);
                    value = "";
                } else {
                    String encodedKey = entry.substring(0, splitterIndex);
                    key = URLDecoder.decode(encodedKey, encoding);
                    String encodedValue = entry.substring(splitterIndex + 1);
                    value = URLDecoder.decode(encodedValue, encoding);
                }
                ArrayList<String> values = map.get(key);
                if (values == null) {
                    values = new ArrayList<String>(1);
                    map.put(key, values);
                }
                values.add(value);
            } catch (UnsupportedEncodingException error) {
                // String message = String.format("failed to read query parameter (entry=%s)", entry);
            }
        }
        return map;
    }


    public static LinkedHashMap<String, String[]> parseHeaders(HttpServletRequest request) {
        LinkedHashMap<String, String[]> headers = new LinkedHashMap<String, String[]>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String[] headerValues = ServletUtil.toStringArray(request.getHeaders(headerName));
            headers.put(headerName, headerValues);
        }
        // headerNames = null;
        return headers;
    }
}
