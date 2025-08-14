<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>결제 페이지</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<div class="container my-5">
    <h2 class="text-center mb-4">결제하기</h2>

    <div class="row">
        <!-- 주문 상품 요약 -->
        <div class="col-md-6 mb-4">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    주문 상품
                </div>
                <ul class="list-group list-group-flush">
                    <c:forEach var="item" items="${cartItems}">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <strong>${item.bookTitle}</strong><br>
                                <small class="text-muted">수량: ${item.quantity}</small>
                            </div>
                            <span>${item.price}원</span>
                        </li>
                    </c:forEach>
                </ul>
                <div class="card-footer d-flex justify-content-between">
                    <strong>총 금액</strong>
                    <strong class="text-danger">${totalPrice}원</strong>
                </div>
            </div>
        </div>

        <!-- 결제 정보 입력 -->
        <div class="col-md-6 mb-4">
            <div class="card shadow-sm">
                <div class="card-header bg-success text-white">
                    결제 정보
                </div>
                <div class="card-body">
                    <form action="/user/payment/complete" method="post">
                        <div class="mb-3">
                            <label class="form-label">이름</label>
                            <input type="text" name="name" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">주소</label>
                            <input type="text" name="address" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">결제 수단</label>
                            <select name="paymentMethod" class="form-select" required>
                                <option value="">-- 결제 수단 선택 --</option>
                                <option value="card">신용카드</option>
                                <option value="bank">계좌이체</option>
                                <option value="mobile">휴대폰 결제</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">카드/계좌번호</label>
                            <input type="text" name="accountNumber" class="form-control" required>
                        </div>

                        <button type="submit" class="btn btn-success w-100">결제하기</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>