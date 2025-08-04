<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인 페이지</title>
    <!-- Bootstrap CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg">
                    <div class="card-header bg-primary text-white text-center">
                        <h3>로그인</h3>
                    </div>
                    <div class="card-body"> 
                        <form action="<c:url value='/login'/>" method="post">
                        	<div class="mb-3">
                                <label for="username" class="form-label">아이디</label>
                                <input type="text" class="form-control" id="username" name="username" placeholder="아이디를 입력하세요" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">비밀번호</label>
                                <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호를 입력하세요" required>
                            </div>
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">로그인</button>
                                <a href="/login/register" class="btn btn-secondary">회원가입</a>
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token }" />
                            <input type="hidden" name="role" value="USER" />
                        </form>
                    </div>
                    <div class="card-footer text-muted text-center">
                        ⓒ 2025 Your Company
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
