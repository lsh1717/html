<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- Google Fonts -->
<link href="https://fonts.googleapis.com/css2?family=Pretendard:wght@400;600;700&display=swap" rel="stylesheet">
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

<style>
/* 전체 공통 */
body {
  background: #f4f6fb;
  font-family: 'Pretendard', 'Noto Sans KR', sans-serif;
  color: #333;
}
h2 {
  font-weight: 700;
  margin-bottom: 25px;
  color: #1a1a1a;
  letter-spacing: -1px;
  text-align: center;
}

/* 검색창 */
.input-group {
  max-width: 600px;
  margin: 0 auto 40px auto;
}
.input-group .form-control {
  border-radius: 30px 0 0 30px;
  border: 1px solid #4A6CF7;
  box-shadow: inset 0 1px 3px rgba(0,0,0,0.08);
  font-size: 1rem;
  height: 48px;
}
.input-group-append .btn {
  border-radius: 0 30px 30px 0;
  background: linear-gradient(135deg, #4A6CF7, #8E9CFA);
  border: none;
  color: #fff;
  font-weight: 600;
  padding: 0 28px;
  font-size: 1rem;
  transition: 0.25s;
}
.input-group-append .btn:hover {
  background: linear-gradient(135deg, #3a56c8, #6e7df5);
}

/* 캐러셀 */
.carousel-item img {
  height: 320px;
  object-fit: cover;
  border-radius: 14px;
}
.carousel-caption {
  background: rgba(0,0,0,0.45);
  backdrop-filter: blur(6px);
  border-radius: 10px;
  padding: 12px 18px;
}
.carousel-caption h5 {
  font-size: 1.2rem;
  font-weight: 600;
  color: #fff;
}

/* 도서 카드 */
.card {
  border: none;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 3px 10px rgba(0,0,0,0.08);
  transition: transform 0.25s, box-shadow 0.25s;
}
.card:hover {
  transform: translateY(-6px);
  box-shadow: 0 8px 22px rgba(0,0,0,0.15);
}
.card-img-top {
  height: 240px;
  object-fit: cover;
}
.card-body {
  padding: 16px;
}
.card-title {
  font-size: 1.05rem;
  font-weight: 600;
  color: #222;
  margin-bottom: 6px;
}
.card-title a {
  text-decoration: none;
  color: inherit;
}
.card-title a:hover {
  color: #4A6CF7;
}
.card-text {
  font-size: 0.95rem;
  color: #666;
}
.card-text strong {
  color: #4A6CF7;
  font-size: 1.05rem;
}

/* 페이지네이션 */
.pagination .page-link {
  border-radius: 50px;
  margin: 0 4px;
  color: #4A6CF7;
}
.pagination .active .page-link {
  background: #4A6CF7;
  border-color: #4A6CF7;
  color: #fff;
}
</style>

<div class="container py-5">
  <h2>${pageTitle}</h2>

  <!-- 검색 폼 -->
  <form id="searchForm" class="mb-3">
    <div class="input-group">
      <input type="text" id="keyword" name="keyword" value="${keyword}" class="form-control" placeholder="제목으로 책 검색" />
      <div class="input-group-append">
        <button type="submit" class="btn">검색</button>
      </div>
    </div>
  </form>

  <!-- 추천 도서 Carousel -->
  <c:if test="${not empty recommendedBooks}">
    <div id="recommendedCarousel" class="carousel slide mb-5" data-ride="carousel">
      <div class="carousel-inner">
        <c:forEach var="book" items="${recommendedBooks}" varStatus="status">
          <div class="carousel-item ${status.first ? 'active' : ''}">
            <img src="${book.cover_image}" class="d-block w-100" alt="${book.title}">
            <div class="carousel-caption">
              <h5>${book.title}</h5>
            </div>
          </div>
        </c:forEach>
      </div>
      <a class="carousel-control-prev" href="#recommendedCarousel" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon"></span>
      </a>
      <a class="carousel-control-next" href="#recommendedCarousel" role="button" data-slide="next">
        <span class="carousel-control-next-icon"></span>
      </a>
    </div>
  </c:if>

  <!-- 도서 목록 -->
  <div id="bookContainer" class="row">
    <c:forEach var="book" items="${books}">
      <div class="col-md-3 mb-4 d-flex">
        <div class="card w-100">
          <a href="${ctx}/user/bookDetail/${book.bookId}">
            <img src="${book.cover_image}" class="card-img-top" alt="Cover">
          </a>
          <div class="card-body">
            <h5 class="card-title">
              <a href="${ctx}/user/bookDetail/${book.bookId}">${book.title}</a>
            </h5>
            <p class="card-text">저자: ${book.author}</p>
            <p class="card-text"><strong>${book.price}원</strong></p>
          </div>
        </div>
      </div>
    </c:forEach>

    <c:if test="${empty books}">
      <div class="col-12 text-center">
        <p>검색 결과가 없습니다.</p>
      </div>
    </c:if>
  </div>
</div>

<!-- 페이지네이션 -->
<nav aria-label="Page navigation">
  <ul class="pagination justify-content-center">
    <c:if test="${startPage > 1}">
      <li class="page-item"><a class="page-link" href="?page=${startPage-1}&keyword=${keyword}">이전</a></li>
    </c:if>
    <c:forEach var="i" begin="${startPage}" end="${endPage}">
      <li class="page-item ${i == page ? 'active' : ''}">
        <a class="page-link" href="?page=${i}&keyword=${keyword}">${i}</a>
      </li>
    </c:forEach>
    <c:if test="${endPage < totalPage}">
      <li class="page-item"><a class="page-link" href="?page=${endPage+1}&keyword=${keyword}">다음</a></li>
    </c:if>
  </ul>
</nav>
<!-- jQuery 먼저 -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.9.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

<!-- Ajax 검색 -->
<script>
$(document).ready(function(){
  let debounceTimer;

  // 키워드 입력 시 디바운스로 Ajax 요청
  $('#keyword').on('keyup', function() {
    clearTimeout(debounceTimer); // 이전 타이머 제거
    const keyword = $(this).val();

    debounceTimer = setTimeout(function() {
      $.ajax({
        url: '${ctx}/user/bookList',
        type: 'get',
        data: { keyword: keyword, page: 1 }, // 항상 1페이지부터
        success: function(data) {
          const newContent = $(data).find('#bookContainer').html();
          $('#bookContainer').html(newContent);
        }
      });
    }, 300); // 0.3초 입력 멈추면 실행
  });

  // 검색 버튼 클릭 시 submit 막기 (Ajax만 동작하도록)
  $('#searchForm').submit(function(e){
    e.preventDefault();
  });
});
</script>