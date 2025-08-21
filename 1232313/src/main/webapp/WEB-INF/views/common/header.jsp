<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<sec:authentication property="principal" var="user"/>

<header style="padding:10px; background:#f0f0f0;">
  <div class="container d-flex justify-content-between align-items-center">
    <h1 class="m-0">
      <a href="${ctx}/user/bookList" class="text-dark text-decoration-none">BookShop</a>
    </h1>

    <nav class="d-flex align-items-center gap-2">
      <!-- 🔹 마이페이지 버튼 -->
      <a href="${ctx}/user/mypage" class="btn btn-link p-0 mr-3">마이페이지</a>

      <!-- 장바구니 버튼 -->
      <a href="${ctx}/cart/view" class="btn btn-primary d-flex align-items-center px-3 py-1 mr-3">
        <i class="bi bi-cart-fill mr-1"></i> 장바구니
      </a>

      <!-- ✅ 로그인 상태일 때만 이름 + 로그아웃 표시 -->
      <sec:authorize access="isAuthenticated()">
        <span class="mr-3">[<sec:authentication property="name" />]</span>
        <form action="${ctx}/logout" method="post" class="m-0">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          <button type="submit" class="btn btn-outline-secondary btn-sm">로그아웃</button>
        </form>
      </sec:authorize>

      <!-- ✅ 비로그인 상태일 땐 로그인 버튼 -->
      <sec:authorize access="!isAuthenticated()">
        <a href="${ctx}/login/login" class="btn btn-outline-primary btn-sm">로그인</a>
      </sec:authorize>
    </nav>
  </div>
</header>

<!-- 부트스트랩 아이콘 -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" />