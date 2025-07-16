<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<header style="padding:10px; background:#f0f0f0;">
  <h1><a href="${ctx}/bookList">BookShop</a></h1>
  <nav>
    <a href="${ctx}/bookList">전체보기</a>
    <c:choose>
      <c:when test="${not empty sessionScope.loginUser}">
        <span>${sessionScope.loginUser.username}님</span>
        <a href="${ctx}/auth/logout">로그아웃</a>
      </c:when>
      <c:otherwise>
        <a href="${ctx}/auth/login">로그인</a>
        <a href="${ctx}/auth/register">회원가입</a>
      </c:otherwise>
    </c:choose>
  </nav>
</header>
