<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

inc1 start:

<%
request.getRequestDispatcher("forward.jsp").forward(request, response);
%>

inc1 end.
