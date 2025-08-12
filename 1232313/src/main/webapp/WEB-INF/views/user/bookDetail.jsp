<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 부트스트랩 CDN -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<div class="container my-5">
  <div class="row">
    <!-- 책 이미지 -->
    <div class="col-md-4 text-center">
      <img src="${book.cover_image}" class="img-fluid rounded" alt="표지" style="max-height: 300px;">
    </div>

    <!-- 책 정보 -->
    <div class="col-md-8">
      <h3 class="mb-3">${book.title}</h3>
      <p><strong>저자:</strong> ${book.author}</p>
      <p><strong>가격:</strong> ${book.price}원</p>
      <p><strong>재고:</strong> ${book.stock}권</p>

      <form action="${ctx}/cart/add" method="post" class="mt-4">
        <input type="hidden" name="bookId" value="${book.bookId}" />
        <div class="form-group row">
          <label for="quantity" class="col-sm-2 col-form-label">수량</label>
          <div class="col-sm-4">
            <input type="number" name="quantity" id="quantity" value="1" min="1" max="${book.stock}" class="form-control" required>
          </div>
        </div>
        <button type="submit" class="btn btn-primary">장바구니 담기</button>
        <a href="${ctx}/user/bookList" class="btn btn-secondary ml-2">목록으로</a>
      </form>
    </div>
  </div>
</div>