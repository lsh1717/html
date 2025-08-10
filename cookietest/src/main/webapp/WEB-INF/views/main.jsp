<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>도서 목록</title>
    <!-- 부트스트랩 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4">도서 목록</h1>

    <div class="row row-cols-1 row-cols-md-3 g-4">
        <c:forEach var="book" items="${bookList}">
            <div class="col">
                <div class="card h-100">
                    <div class="card-body">
                        <h5 class="card-title">${book.name}</h5>
                        <p class="card-text">가격: ${book.price}원</p>
                        <a href="addCart?bookId=${book.id}" class="btn btn-primary">장바구니에 담기</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <div class="mt-4">
        <a href="cart" class="btn btn-success">장바구니 보기</a>
    </div>
</div>

<!-- 부트스트랩 JS (필요하면) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>