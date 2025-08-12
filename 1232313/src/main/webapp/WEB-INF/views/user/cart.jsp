<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 부트스트랩 CDN -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<div class="container my-5">
  <h2 class="mb-4">🛒 장바구니</h2>

  <c:if test="${empty cartItems}">
    <div class="alert alert-info text-center">
      장바구니에 담긴 상품이 없습니다.
    </div>
  </c:if>

  <c:if test="${not empty cartItems}">
    <table class="table table-bordered align-middle text-center">
      <thead class="thead-light">
        <tr>
          <th>표지</th>
          <th>제목</th>
          <th>수량</th>
          <th>가격</th>
          <th>합계</th>
          <th>삭제</th>
        </tr>
      </thead>
      <tbody>
        <c:set var="total" value="0" />
        <c:forEach var="item" items="${cartItems}">
          <tr>
            <td>
              <img src="${item.book.cover_image}" alt="표지" style="height: 80px;">
            </td>
            <td>${item.book.title}</td>
            <td>${item.quantity}</td>
            <td>${item.book.price}원</td>
            <td>${item.book.price * item.quantity}원</td>
            <td>
              <form action="${ctx}/cart/remove" method="post">
                <input type="hidden" name="bookId" value="${item.book.bookId}" />
                <button type="submit" class="btn btn-sm btn-danger">삭제</button>
              </form>
            </td>
          </tr>
          <c:set var="total" value="${total + (item.book.price * item.quantity)}" />
        </c:forEach>
      </tbody>
    </table>

    <div class="text-right">
      <h4>총 합계: <strong>${total}원</strong></h4>
      <form action="${ctx}/order/checkout" method="post" class="mt-3">
        <button type="submit" class="btn btn-success btn-lg">결제하기</button>
      </form>
    </div>
  </c:if>
</div>

<!-- Bootstrap JS (옵션) -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>