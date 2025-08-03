<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<sec:authentication property="principal" var="user"/>

<header style="padding:10px; background:#f0f0f0;">
  <h1><a href="${ctx}/bookList">BookShop</a></h1>
  <nav>
    <a href="${ctx}/bookList">전체보기</a>
    <sec:authorize access="isAuthenticated()">
      <a href="#">[<sec:authentication property="name"/>]</a>
      <form action="/logout" method="post" style="display: inline;">
        <input type="submit" value="로그아웃">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
      </form>
    </sec:authorize>
    
	<!--
    <sec:authorize access="isAnonymous()">
      <a href="${ctx}/auth/login">로그인</a>
      <a href="${ctx}/auth/register">회원가입</a>
    </sec:authorize>
    
    <c:choose>
      <c:when test="${not empty sessionScope.loginUser}">
        <span>${sessionScope.loginUser.username}님</span>
        <a href="${ctx}/auth/logout">로그아웃</a>
      </c:when>
      <c:otherwise>-->
      	<!--<sec:authentication property="principal" var="user"/>
		<a href="#">[${user.username}]</a>
		<form action="/logout" method="post">
		<input type="submit" value="로그아웃">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token }">
		</form>
        <a href="${ctx}/auth/login">로그인</a>
        <a href="${ctx}/auth/register">회원가입</a>
      </c:otherwise>
    </c:choose>-->
  </nav>
</header>
