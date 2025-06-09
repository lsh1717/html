<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.getSession().invalidate();
//request.getSession().setMaxInactiveInterval(0);
response.sendRedirect("/login.jsp");
%>