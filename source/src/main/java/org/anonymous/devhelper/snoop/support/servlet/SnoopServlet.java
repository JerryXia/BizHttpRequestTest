package org.anonymous.devhelper.snoop.support.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.anonymous.devhelper.snoop.JvmMemoryInfo;
import org.anonymous.devhelper.snoop.MemoryPoolMXBeanInfo;
import org.anonymous.devhelper.snoop.Monitor;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class SnoopServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JvmMemoryInfo jvmMemoryInfo = Monitor.currentMonitor().run();
        RequestInformation requestInfo = buildRequestInfo(req);
        StringBuffer sb = new StringBuffer(1024 * 64);
        sb.append("<html><head><title>");
        sb.append("Servlet Snoop Page");
        sb.append("</title>");
        sb.append("<link href=\"https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css\" rel=\"stylesheet\">");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h2>JVM Memory Monitor</h2>");

        sb.append("<table>");
        sb.append("<thead><tr>");
        sb.append("<th colspan=\"2\">");
        sb.append("Memory MXBean");
        sb.append("</th>");
        sb.append("</tr></thead>");
        sb.append("<tbody>");
        sb.append("<tr>");
        sb.append("<td>Heap Memory Usage</td>");
        sb.append("<td>");
        sb.append(jvmMemoryInfo.getMemoryMXBeanInfo().getHeapMemoryUsag());
        sb.append("</td></tr>");
        sb.append("<tr>");
        sb.append("<td>Non-Heap Memory Usage</td>");
        sb.append("<td>");
        sb.append(jvmMemoryInfo.getMemoryMXBeanInfo().getNonHeapMemoryUsag());
        sb.append("</td></tr>");
        sb.append("</tbody>");
        sb.append("</table>");

        sb.append("<h3>Memory Pool MXBeans</h3>");
        for(MemoryPoolMXBeanInfo memoryPoolMXBeanInfo:  jvmMemoryInfo.getMemoryPoolMXBeansInfo()) {
            sb.append("<table>");
            sb.append("<thead><tr>");
            sb.append("<th colspan=\"2\">");
            sb.append(memoryPoolMXBeanInfo.getName());
            sb.append("</th>");
            sb.append("</tr></thead>");
            sb.append("<tbody>");
            sb.append("<tr>");
            sb.append("<td>Type</td>");
            sb.append("<td>");
            sb.append(memoryPoolMXBeanInfo.getType());
            sb.append("</td></tr>");
            sb.append("<tr>");
            sb.append("<td>Memory Usage</td>");
            sb.append("<td>");
            sb.append(memoryPoolMXBeanInfo.getMemoryUsage());
            sb.append("</td></tr>");
            sb.append("<tr>");
            sb.append("<td>Peak Usage</td>");
            sb.append("<td>");
            sb.append(memoryPoolMXBeanInfo.getPeakMemoryUsage());
            sb.append("</td></tr>");
            sb.append("<tr>");
            sb.append("<td>Collection Usage</td>");
            sb.append("<td>");
            sb.append(memoryPoolMXBeanInfo.getCollectionUsage());
            sb.append("</td></tr>");
            sb.append("</tbody>");
            sb.append("</table>");
        }

        sb.append("<h2>Request information</h2>");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Requested URL");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getRequestURL());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Request Method");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getMethod());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Request URI");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getRequestURI());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Request protocol");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getProtocol());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Servlet path");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getServletPath());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Path info");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getPathInfo());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Path translated");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getPathTranslated());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Query string");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getQueryString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Content length");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getContentLength());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Content type");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getContentType());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Server name");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getServerName());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Server port");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getServerPort());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Remote user");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getRemoteUser());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Remote address");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getRemoteAddr());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Remote host");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getRemoteHost());sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>");sb.append("Authorization scheme");sb.append("</td>");
        sb.append("<td>");sb.append(requestInfo.getAuthType());sb.append("</td>");
        sb.append("</tr>");
        sb.append("</table>");

        sb.append("<h2>Request headers</h2>");
        sb.append("<table>");
        for(Entry<String, String[]> item : requestInfo.getRequestHeaders().entrySet()) {
            for(String val : item.getValue()) {
                sb.append("<tr>");
                sb.append("<td>");sb.append(item.getKey());sb.append("</td>");
                sb.append("<td>");sb.append(val);sb.append("</td>");
                sb.append("</tr>");
            }
        }
        sb.append("</table>");

        sb.append("<h2>Request parameters</h2>");
        sb.append("<table>");
        for(Entry<String, String[]> item : requestInfo.getRequestParameters().entrySet()) {
            for(String val : item.getValue()) {
                sb.append("<tr>");
                sb.append("<td>");sb.append(item.getKey().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));sb.append("</td>");
                sb.append("<td>");sb.append(val.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));sb.append("</td>");
                sb.append("</tr>");
            }
        }
        sb.append("</table>");

        sb.append("<h2>Request Attributes</h2>");
        sb.append("<table>");
        for(Entry<String, Object> item : requestInfo.getRequestAttributes().entrySet()) {
            sb.append("<tr>");
            sb.append("<td>");sb.append(item.getKey().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));sb.append("</td>");
            sb.append("<td>");sb.append(item.getValue().toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");

        sb.append("<h2>Init parameters</h2>");
        sb.append("<table>");
        for(Entry<String, String> item : requestInfo.getInitParameters().entrySet()) {
            sb.append("<tr>");
            sb.append("<td>");sb.append(item.getKey());sb.append("</td>");
            sb.append("<td>");sb.append(item.getValue());sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");

        sb.append("<script src=\"https://cdn.bootcss.com/jquery/2.2.4/jquery.min.js\"></script>");
        sb.append("<script>$('table').addClass('table').addClass('table-bordered').addClass('table-condensed');");
        sb.append("$('table tr th').css({'text-align':'center'});");
        sb.append("</script>");
        sb.append("</body></html>");
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().print(sb.toString());
    }

    private RequestInformation buildRequestInfo(HttpServletRequest req) {
        RequestInformation requestInfo = new RequestInformation();
        requestInfo.setRequestURL(req.getRequestURL().toString());
        requestInfo.setMethod(req.getMethod());
        requestInfo.setRequestURI(req.getRequestURI());
        requestInfo.setProtocol(req.getProtocol());
        requestInfo.setServletPath(req.getServletPath());
        requestInfo.setPathTranslated(req.getPathTranslated());
        requestInfo.setQueryString(req.getQueryString() != null ? req.getQueryString() : "");
        requestInfo.setContentLength(req.getContentLength());
        requestInfo.setContentType(req.getContentType());
        requestInfo.setServerName(req.getServerName());
        requestInfo.setServerPort(req.getServerPort());
        requestInfo.setRemoteUser(req.getRemoteUser());
        requestInfo.setRemoteAddr(req.getRemoteAddr());
        requestInfo.setRemoteHost(req.getRemoteHost());
        requestInfo.setAuthType(req.getAuthType());

        Map<String, String[]> requestHeaders = new LinkedHashMap<String, String[]>();
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headName = headerNames.nextElement();
            LinkedList<String> arrayVals = new LinkedList<String>();
            Enumeration<String> vals = req.getHeaders(headName);
            while(vals.hasMoreElements()) {
                arrayVals.add(vals.nextElement());
            }
            requestHeaders.put(headName, arrayVals.toArray(new String[arrayVals.size()]));
        }
        requestInfo.setRequestHeaders(requestHeaders);

        Map<String, String[]> requestParameters = new LinkedHashMap<String, String[]>(req.getParameterMap());
        requestInfo.setRequestParameters(requestParameters);

        Map<String, Object> requestAttributes = new LinkedHashMap<String, Object>();
        Enumeration<String> attributeNames = req.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object val = req.getAttribute(attributeName);
            requestAttributes.put(attributeName, val);
        }
        requestInfo.setRequestAttributes(requestAttributes);

        Map<String, String> initParameters = new LinkedHashMap<String, String>();
        Enumeration<String> initParameterNames = getServletConfig().getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String initParameterName = initParameterNames.nextElement();
            String val = getServletConfig().getInitParameter(initParameterName);
            initParameters.put(initParameterName, val);
        }
        requestInfo.setInitParameters(initParameters);
        return requestInfo;
    }

}
