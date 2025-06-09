<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
request:<%=request.getAttribute("id")%>
session:<%=request.getSession().getAttribute("id")%><br>
context(application):
context:<%=request.getServletContext().getAttribute("id") %><br>
<a href="/login.jsp">로그인페이지이동</a>"