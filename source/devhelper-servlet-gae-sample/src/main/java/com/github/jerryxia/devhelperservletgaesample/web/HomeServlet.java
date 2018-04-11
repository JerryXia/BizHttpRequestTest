/**
 * 
 */
package com.github.jerryxia.devhelperservletgaesample.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 *
 */
@Slf4j
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = -7201198165652905457L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestFirst = "requestFirst";
        HttpSession session = req.getSession();
        // session test
        if (session.getAttribute(requestFirst) == null) {
            String now = DateTime.now(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss");
            log.info("first request time: {}", now);
            log.info("session: {}", session.getClass().toGenericString());
            session.setAttribute(requestFirst, now);
        } else {
            log.info("session storage data: {}", session.getAttribute(requestFirst));
        }

        String appName = query();

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        resp.getWriter().print("<html>");
        resp.getWriter().print("<head>");
        resp.getWriter().print("<title>devhelper-servlet-gae-sample</title>");
        resp.getWriter().print("</head>");
        resp.getWriter().print("<h1>devhelper-servlet-gae-sample</h1>");
        resp.getWriter().print("<p>first request: ");
        resp.getWriter().print(session.getAttribute(requestFirst));
        resp.getWriter().print("</p>");

        resp.getWriter().print("<p>appName: ");
        resp.getWriter().print(appName);
        resp.getWriter().print("</p>");

        resp.getWriter().print("<p>now time: <span id=\"now\"></span></p>");
        resp.getWriter().print("<p><a href=\"/requestcapture/\" target=\"_blank\">requestcapture</a></p>");
        resp.getWriter()
                .print("<iframe src=\"/requestcapture/\" style=\"border:0px;width:100%;height:500px;\"></iframe>");
        resp.getWriter()
                .print("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>");

        resp.getWriter().print("<script>");
        resp.getWriter()
                .print("var now = function(){ $.post('/now.json', {}, function(res){ $('#now').html(res.t); });   };");
        resp.getWriter().print("setTimeout(now, 1234);");
        resp.getWriter().print("</script>");
        resp.getWriter().print("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().print("{\"t\":\"" + DateTime.now(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss") + "\"}");
    }

    // @Cacheable("time")
    public String query() {
        String appName = "devhelper-servlet-gae-sample";
        // Const.AppProperties.getProperty("app.name");
        log.debug("appName: {}, now: {}", appName, DateTime.now(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss"));
        return appName;
    }

}
