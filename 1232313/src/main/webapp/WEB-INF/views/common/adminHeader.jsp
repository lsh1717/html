<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="<c:url value='/admin/dashboard' />">관리자 페이지</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#adminNavbar"
                aria-controls="adminNavbar" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="adminNavbar">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/admin/dashboard' />">대시보드</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/admin/bookManage' />">도서 관리</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/admin/users' />">회원 관리</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/admin/orders' />">주문 관리</a>
                </li>
            </ul>
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/logout' />">로그아웃</a>
                </li>
            </ul>
        </div>
    </nav>
</header>