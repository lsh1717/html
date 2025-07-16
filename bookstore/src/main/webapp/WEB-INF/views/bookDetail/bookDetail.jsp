<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 구글 폰트 (Noto Sans KR) -->
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR:400,700&display=swap" rel="stylesheet">
<!-- 부트스트랩 CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
  background: #f8f9fa;
  font-family: 'Noto Sans KR', Arial, sans-serif;
}
.container {
  max-width: 980px;
}
.detail-wrapper {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 3px 16px rgba(54,102,204,0.07), 0 1.5px 4px rgba(0,0,0,0.04);
  padding: 40px 36px 32px 36px;
  margin-top: 40px;
  margin-bottom: 40px;
}
.book-cover-box {
  background: #f4f6fa;
  border-radius: 12px;
  box-shadow: 0 4px 18px rgba(54,102,204,0.08);
  padding: 20px;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  min-height: 360px;
}
.book-cover-box img {
  max-width: 260px;
  max-height: 340px;
  width: auto;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.09);
  background: #e9ecef;
}
@media (max-width: 767px) {
  .detail-wrapper { padding: 18px 10px; }
  .book-cover-box { padding: 10px; min-height: 200px;}
  .book-cover-box img { max-width: 100%; max-height: 220px;}
}
.detail-title {
  font-size: 2rem;
  font-weight: 700;
  color: #222;
  margin-bottom: 18px;
  line-height: 1.22;
  letter-spacing: -1.2px;
}
.detail-info-list p {
  font-size: 1.08rem;
  margin-bottom: 7px;
  color: #444;
}
.detail-info-list strong {
  color: #2546a6;
  font-weight: 700;
  margin-right: 8px;
}
.detail-price {
  font-size: 1.18rem;
  color: #36c;
  font-weight: 700;
  margin-bottom: 10px;
}
.detail-stock {
  color: #1d7a45;
  font-weight: 600;
}
.detail-desc-title {
  font-size: 1.15rem;
  font-weight: 600;
  margin-top: 30px;
  margin-bottom: 10px;
  color: #2546a6;
}
.detail-desc {
  font-size: 1.03rem;
  color: #444;
  background: #f8fafc;
  border-radius: 8px;
  padding: 18px 14px;
  white-space: pre-wrap;
  min-height: 80px;
}
.back-btn {
  margin-top: 36px;
  padding: 11px 36px;
  border-radius: 30px;
  font-size: 1.09rem;
  font-weight: 600;
  background: #f3f6fa;
  color: #2546a6;
  border: 1.5px solid #c7d0e6;
  transition: background 0.2s, color 0.2s;
}
.back-btn:hover {
  background: #2546a6;
  color: #fff;
  border-color: #2546a6;
  text-decoration: none;
}
</style>

<div class="container">
  <div class="detail-wrapper">
    <div class="row">
      <!-- 왼쪽: 커버 이미지 -->
      <div class="col-md-4 mb-4 mb-md-0">
        <div class="book-cover-box">
         <img src="${book.coverImage}" alt="Cover of ${book.title}">
        </div>
      </div>
      <!-- 오른쪽: 도서 정보 -->
      <div class="col-md-8">
        <div class="detail-title">${book.title}</div>
        <div class="detail-info-list">
          <p><strong>저자</strong> ${book.author}</p>
          <p class="detail-price"><strong>가격</strong> ${book.price}원</p>
          <p class="detail-stock"><strong>재고</strong> ${book.stock}권</p>
        </div>
        <div class="detail-desc-title">설명</div>
        <div class="detail-desc">${book.description}</div>
        <a href="${ctx}/bookList" class="back-btn d-inline-block">
          ← 목록으로 돌아가기
        </a>
      </div>
    </div>
  </div>
</div>

<!-- 부트스트랩 JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.9.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
