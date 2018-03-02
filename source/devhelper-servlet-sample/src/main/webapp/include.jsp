<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

===============
include container
===============

<%
request.getRequestDispatcher("inc1.jsp").include(request, response);
%>
