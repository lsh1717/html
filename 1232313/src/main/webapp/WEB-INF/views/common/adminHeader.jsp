<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<sec:authentication property="principal" var="user"/>

<header style="padding:10px; background:#f0f0f0;">
  <h1><a href="${ctx}/admin/bookManage">BookShop</a></h1>
  <nav>
    <a href="${ctx}/admin/bookManage">전체보기</a>
    <sec:authorize access="isAuthenticated()">
      <a href="#">[<sec:authentication property="name"/>]</a>
      <form action="/logout" method="post" style="display: inline;">
        <input type="submit" value="로그아웃">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
      </form>
    </sec:authorize>
    </nav>
</header>