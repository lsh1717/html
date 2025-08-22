<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<sec:authentication property="principal" var="user"/>

<!-- Bootstrap 5 CSS (전역 포함 시 중복 로드 금지) -->
<link rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
<!-- Bootstrap Icons -->
<link rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

<style>
  /* 헤더는 화이트바 + 얇은 보더 + 살짝 라운드 느낌 */
  .site-header{ background:#ffffff; border-bottom:1px solid #e9edf3; }
  .site-header .brand a{ color:#0f172a; text-decoration:none; }
  .site-header .brand a:hover{ opacity:.85; }

  /* 네비 링크/버튼 톤(메인과 통일: 인디고) */
  .site-header a.btn-link{ color:#4F46E5 !important; font-weight:600; }
  .site-header .btn-primary{
    background:#4F46E5; border-color:#4F46E5;
    box-shadow:0 6px 18px rgba(79,70,229,.25);
  }
  .site-header .btn-primary:hover{ background:#4338CA; border-color:#4338CA; }
  .site-header .btn-outline-secondary{ border-color:#cfd6e4; color:#475569; }
  .site-header .btn-outline-secondary:hover{ background:#f8fafc; }

  /* 상단 컨테이너 패딩/정렬 통일 */
  .site-header .container{ padding-top:10px; padding-bottom:10px; }
</style>
<header class="site-header">
  <div class="container py-2">
    <div class="d-flex align-items-center justify-content-between">
      <h1 class="m-0 fs-3 brand">
        <a href="${ctx}/user/bookList">BookShop</a>
      </h1>

      <nav class="d-flex align-items-center gap-3">
        <!-- 마이페이지 -->
        <a href="${ctx}/user/mypage" class="btn btn-link p-0">마이페이지</a>

        <!-- 장바구니 -->
        <a href="${ctx}/cart/view" class="btn btn-primary d-flex align-items-center px-3 py-1">
          <i class="bi bi-cart-fill me-1"></i> 장바구니
        </a>

        <!-- 로그인 상태 -->
        <sec:authorize access="isAuthenticated()">
          <span class="me-3">[<sec:authentication property="name" />]</span>
          <form action="${ctx}/logout" method="post" class="m-0 d-inline">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <button type="submit" class="btn btn-outline-secondary btn-sm">로그아웃</button>
          </form>
        </sec:authorize>

        <!-- 비로그인 상태 -->
        <sec:authorize access="!isAuthenticated()">
          <a href="${ctx}/login/login" class="btn btn-outline-primary btn-sm">로그인</a>
        </sec:authorize>
      </nav>
    </div>
  </div>
</header>