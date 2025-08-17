<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 구글 폰트 (Noto Sans KR) -->
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR:400,700&display=swap" rel="stylesheet">
<!-- 부트스트랩 CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

<!-- 커스텀 스타일: 카드, 폼, 레이아웃 등 -->
<style>
/* body, 제목, 폼, 카드 스타일 정의 */
body { background-color: #f8f9fa; font-family: 'Noto Sans KR', Arial, sans-serif; }
h2 { font-weight: 700; margin-bottom: 30px; color: #222; letter-spacing: -1.5px; }
form { max-width: 600px; margin: 0 auto 40px auto; }

/* 검색창 스타일 */
.input-group .form-control { border-radius: 30px 0 0 30px; border-right: none; background: #fff; font-size: 1.05rem; height: 48px; }
.input-group-append .btn { border-radius: 0 30px 30px 0; background: #36c; border: none; color: #fff; font-weight: 600; padding: 0 32px; font-size: 1.07rem; height: 48px; transition: background 0.2s; }
.input-group-append .btn:hover { background: #2546a6; }

/* 카드 디자인 */
.card { border: none; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.07); transition: box-shadow 0.2s, transform 0.2s; background: #fff; height: 100%; display: flex; flex-direction: column; }
.card:hover { box-shadow: 0 6px 20px rgba(54, 102, 204, 0.15); transform: translateY(-2px) scale(1.02); }
.card-img-top { border-radius: 12px 12px 0 0; background: #f0f0f0; height: 220px; object-fit: cover; }
.card-title { font-size: 1.1rem; font-weight: 600; margin-bottom: 8px; color: #222; line-height: 1.3; }
.card-title a { color: #222; text-decoration: none; transition: color 0.2s; }
.card-title a:hover { color: #36c; text-decoration: underline; }
.card-text { font-size: 0.98rem; color: #555; margin-bottom: 4px; }
.card-text strong { color: #36c; font-size: 1.08rem; }
.card-body { padding: 18px 14px 14px 14px; display: flex; flex-direction: column; flex: 1 1 auto; }

/* 레이아웃 조정 */
.row { margin-left: -10px; margin-right: -10px; }
.col-md-3 { padding-left: 10px; padding-right: 10px; }
.text-center p { color: #888; font-size: 1.1rem; margin-top: 40px; }

/* 반응형 */
@media (max-width: 991px) { .col-md-3 { flex: 0 0 50%; max-width: 50%; } }
@media (max-width: 575px) { .col-md-3 { flex: 0 0 100%; max-width: 100%; } .card-img-top { height: 180px; } }
</style>

<div class="container py-4">
  <h2>${pageTitle}</h2>

  <!-- =========================
       검색 폼
       - id="searchForm" : JS에서 실시간 검색 이벤트를 잡기 위함
       - #keyword : 사용자가 입력하는 검색어 input
  ============================ -->
  <form id="searchForm" class="mb-3">
    <div class="input-group">
      <!-- 검색어 입력 -->
      <input type="text" id="keyword" name="keyword" value="${keyword}" class="form-control" placeholder="제목 검색" />
      <div class="input-group-append">
        <button type="submit" class="btn btn-primary">검색</button>
      </div>
    </div>
  </form>

  <!-- =========================
       추천 도서 Carousel
       - 검색폼 바로 아래 표시
       - 옆으로 넘기면서 여러 책을 보여줌 (Bootstrap Carousel 사용)
       - active 클래스: 첫 번째 아이템만 초기 선택
       - 좌우 버튼으로 이전/다음 책 보기 가능
  ============================ -->
  <c:if test="${not empty recommendedBooks}">
    <div id="recommendedCarousel" class="carousel slide mb-4" data-ride="carousel">
    <!-- data-ride="carousel"여기에 자동으로 넘어가는 속성이 포함되어있음 -->
      <div class="carousel-inner">
        <c:forEach var="book" items="${recommendedBooks}" varStatus="status">
          <div class="carousel-item ${status.first ? 'active' : ''}">
            <img src="${book.cover_image}" class="d-block w-100" alt="${book.title}" style="height:300px; object-fit:cover;">
            <div class="carousel-caption d-none d-md-block">
              <h5>${book.title}</h5>
            </div>
          </div>
        </c:forEach>
      </div>
      <!-- 이전/다음 버튼 -->
      <a class="carousel-control-prev" href="#recommendedCarousel" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
      </a>
      <a class="carousel-control-next" href="#recommendedCarousel" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
      </a>
    </div>
  </c:if>

  <!-- =========================
       도서 목록
       - id="bookContainer" : 실시간 검색 후 결과를 여기만 갱신
  ============================ -->
  <div id="bookContainer" class="row">
    <c:forEach var="book" items="${books}">
      <div class="col-md-3 mb-4 d-flex">
        <div class="card w-100">
          <a href="${ctx}/user/bookDetail/${book.bookId}">
            <img src="${book.cover_image}" class="card-img-top" alt="Cover">
          </a>
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">
              <a href="${ctx}/user/bookDetail/${book.bookId}">${book.title}</a>
            </h5>
            <p class="card-text mb-1">저자: ${book.author}</p>
            <p class="card-text mt-auto"><strong>${book.price}원</strong></p>
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

<!-- =========================
     페이지네이션
============================ -->
<nav aria-label="Page navigation">
  <ul class="pagination justify-content-center">
    <c:if test="${startPage > 1}">
      <li class="page-item">
        <a class="page-link" href="?page=${startPage-1}&keyword=${keyword}">이전</a>
      </li>
    </c:if>
    <c:forEach var="i" begin="${startPage}" end="${endPage}">
      <li class="page-item ${i == page ? 'active' : ''}">
        <a class="page-link" href="?page=${i}&keyword=${keyword}">${i}</a>
      </li>
    </c:forEach>
    <c:if test="${endPage < totalPage}">
      <li class="page-item">
        <a class="page-link" href="?page=${endPage+1}&keyword=${keyword}">다음</a>
      </li>
    </c:if>
  </ul>
</nav>

<!-- =========================
     부트스트랩 JS
============================ -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.9.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

<!-- =========================
     실시간 검색 Ajax
     - 사용자가 글자를 입력할 때마다 서버로 검색 요청을 보냄
     - 서버에서 검색 결과 HTML 반환 후 #bookContainer만 갱신
     - 폼 제출시 페이지 새로고침 방지
============================ -->
<script>
$(document).ready(function() {
    // 1. 글자를 입력할 때마다 서버에 GET 요청
    $('#keyword').on('input', function() {
        const keyword = $(this).val(); // 입력한 검색어 가져오기
        $.ajax({
            url: '${ctx}/user/bookList',  // 검색 Controller URL
            type: 'get',
            data: { keyword: keyword },
            success: function(data) {
                // 서버에서 반환된 HTML에서 #bookContainer 부분만 갱신
                $('#bookContainer').html($(data).find('#bookContainer').html());
            }
        });
    });

    // 2. 폼 제출 시 기본 동작(페이지 새로고침) 막기
    $('#searchForm').submit(function(e){
        e.preventDefault();
    });
});
</script>