<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 페이지</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f4f6fb;
        }
        .sidebar {
            height: 100vh;
            background: #343a40;
            color: white;
            padding-top: 20px;
        }
        .sidebar a, .sidebar form button {
            color: white;
            display: block;
            padding: 10px 15px;
            text-decoration: none;
            width: 100%;
            text-align: left;
            border: none;
            background: none;
        }
        .sidebar a:hover, .sidebar form button:hover {
            background: #495057;
            border-radius: 4px;
        }
        .content {
            padding: 20px;
        }
        footer {
            padding: 10px;
            background: #f0f0f0;
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">

        <!-- 사이드바 -->
        <nav class="col-md-2 sidebar">
            <h4 class="text-center">관리자 페이지</h4>
            <a href="<c:url value='/admin/dashboard'/>">대시보드</a>
            <a href="<c:url value='/admin/bookManage'/>">도서 관리</a>
            <a href="<c:url value='/admin/users'/>">회원 관리</a>
            <a href="<c:url value='/admin/orders'/>">주문 관리</a>
            <hr>
            
            <!-- ✅ 로그아웃: POST 요청 + CSRF -->
            <form action="<c:url value='/logout'/>" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">로그아웃</button>
            </form>
        </nav>

        <!-- 메인 콘텐츠 -->
        <main class="col-md-10 content">
            <jsp:include page="${contentPage}" />
        </main>
    </div>
</div>

<footer>
    <p>&copy; 2025 BookShop Admin. All rights reserved.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</body>
</html>