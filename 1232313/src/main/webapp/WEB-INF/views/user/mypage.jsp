<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container py-5">

    <!-- 마이페이지 헤더 -->
    <div class="text-center mb-5">
        <h2 class="fw-bold">마이페이지</h2>
        <p class="text-muted">회원님의 정보를 확인하고 주문 내역을 조회할 수 있습니다.</p>
    </div>

    <div class="row">
        <!-- 좌측 사이드바 -->
        <div class="col-md-3">
            <div class="list-group shadow-sm">
                <a href="#" class="list-group-item list-group-item-action active">내 정보</a>
                <a href="#" class="list-group-item list-group-item-action">비밀번호 변경</a>
                <a href="#" class="list-group-item list-group-item-action">회원 탈퇴</a>
                <a href="/user/mypage" class="list-group-item list-group-item-action">주문 내역</a>
            </div>
        </div>

        <!-- 우측 컨텐츠 -->
        <div class="col-md-9">
            <!-- 내 정보 카드 -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-primary text-white">
                    내 정보
                </div>
                <div class="card-body">
                    <p><strong>아이디:</strong> ${member.loginId}</p>
                    <p><strong>이메일:</strong> ${member.email}</p>
                </div>
            </div>

            <!-- 주문 내역 카드 -->
            <div class="card shadow-sm">
                <div class="card-header bg-success text-white">
                    주문 내역
                </div>
                <div class="card-body">
                    <c:if test="${empty orders}">
    <p class="text-muted">아직 주문 내역이 없습니다.</p>
</c:if>

<c:if test="${not empty orders}">
    <table class="table">
    <thead>
        <tr>
            <th>주문번호</th>
            <th>주문일자</th>
            <th>상품명</th>
            <th>수량</th>
            <th>가격</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="order" items="${orders}">
            <c:forEach var="item" items="${order.items}">
                <tr>
                    <td>${order.orderId}</td>
                    <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd"/></td>
                    <td>${item.book.title}</td>
                    <td>${item.quantity}</td>
                    <td>${item.price}</td>
                </tr>
            </c:forEach>
        </c:forEach>
    </tbody>
</table>
</c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>