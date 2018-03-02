<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

forward.jsp start:

<%
request.getRequestDispatcher("inc2.jsp").include(request, response);
%>

<%@ include file="inc3.jsp" %>

forward.jsp end.


