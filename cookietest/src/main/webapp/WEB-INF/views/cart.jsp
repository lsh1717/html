<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>장바구니</title>
    <!-- 부트스트랩 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">장바구니</h2>

    <c:choose>
        <c:when test="${not empty cartList}">
            <table class="table table-bordered">
                <thead class="table-light">
                    <tr>
                        <th>책 제목</th>
                        <th>가격</th>
                        <th>수량</th>
                        <th>합계</th>
                        <th>수정</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${cartList}">
                    <tr>
                        <td>${item.book.name}</td>
                        <td>${item.book.price} 원</td>
                        <td>${item.quantity}</td>
                        <td>${item.book.price * item.quantity} 원</td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="decreaseCart?bookId=${item.book.id}" class="btn btn-sm btn-outline-secondary">-</a>
                                <a href="addCart?bookId=${item.book.id}" class="btn btn-sm btn-outline-secondary">+</a>
                                <a href="removeCart?bookId=${item.book.id}" class="btn btn-sm btn-danger">삭제</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <th colspan="3" class="text-end">총 합계:</th>
                        <th colspan="2">
                            <c:set var="total" value="0" />
                            <c:forEach var="item" items="${cartList}">
                                <c:set var="total" value="${total + item.book.price * item.quantity}" />
                            </c:forEach>
                            ${total} 원
                        </th>
                    </tr>
                </tfoot>
            </table>
        </c:when>
        <c:otherwise>
            <p>장바구니에 담긴 상품이 없습니다.</p>
        </c:otherwise>
    </c:choose>

    <a href="main" class="btn btn-primary">계속 쇼핑하기</a>
</div>

<!-- 부트스트랩 JS CDN (필요 시) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>