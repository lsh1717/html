<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>도서 목록 · BookShop</title>

<link href="https://fonts.googleapis.com/css2?family=Pretendard:wght@400;600;700&display=swap" rel="stylesheet">
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

<style>
  :root{
    --brand:#4F46E5;          /* 인디고 */
    --brand-2:#7C3AED;        /* 보조 보라 */
    --ink:#0f172a;            /* 진한 텍스트 */
    --muted:#6b7280;          /* 보조 텍스트 */
    --card:#ffffff;
    --bg:#f5f7fb;
    --ring:0 10px 30px rgba(15,23,42,.08);
    --ring-soft:0 6px 18px rgba(15,23,42,.06);
    --bd:#e9edf3;
  }

  html,body{height:100%}
  body{
    background:
      radial-gradient(1200px 400px at 85% -80px, rgba(124,58,237,.06), transparent 60%),
      radial-gradient(800px 360px at -10% -40px, rgba(79,70,229,.06), transparent 60%),
      var(--bg);
    font-family:'Pretendard','Noto Sans KR',system-ui,-apple-system,Segoe UI,Roboto,Arial,sans-serif;
    color:var(--ink);
  }

  /* 헤더 아래 적당한 호흡 */
  .page-shell{max-width:1120px; margin:32px auto 56px; padding:0 16px;}
  @media (min-width:1200px){ .page-shell{margin-top:40px;} }

  /* 섹션 카드 */
  .section{
    background:var(--card);
    border:1px solid var(--bd);
    border-radius:18px;
    box-shadow:var(--ring);
    padding:20px 18px 24px;
  }

  .section-title{
    font-weight:800; letter-spacing:-.2px;
    font-size:1.25rem; margin:2px 0 16px;
    display:flex; align-items:center; gap:10px;
  }
  .section-title::before{
    content:""; width:6px; height:22px; border-radius:6px;
    background:linear-gradient(180deg, var(--brand), var(--brand-2));
    box-shadow:0 0 0 3px rgba(79,70,229,.12);
  }

  /* 검색바(필 형태) */
  .searchbar .form-control{
    height:46px; border-radius:999px 0 0 999px; border:1px solid var(--bd);
    box-shadow: inset 0 1px 2px rgba(15,23,42,.04);
  }
  .searchbar .btn{
    height:46px; border-radius:0 999px 999px 0; min-width:88px; font-weight:700;
    color:#fff; border:none;
    background:linear-gradient(135deg, var(--brand), var(--brand-2));
    box-shadow:0 10px 22px rgba(79,70,229,.25);
    transition:.2s ease;
  }
  .searchbar .btn:hover{ transform:translateY(-1px); box-shadow:0 14px 28px rgba(79,70,229,.28); }

  /* 캐러셀 */
  .hero{
    border-radius:16px; overflow:hidden; box-shadow:var(--ring-soft);
    border:1px solid var(--bd);
  }
  .hero .carousel-item{ height:280px; }
  .hero .carousel-item img{ width:100%; height:100%; object-fit:cover; }
  .hero .caption{
    position:absolute; left:50%; bottom:18px; transform:translateX(-50%);
    background:rgba(15,23,42,.55); color:#fff; padding:10px 18px;
    border-radius:999px; font-weight:700; letter-spacing:.1px;
    backdrop-filter: blur(4px);
    box-shadow:0 6px 16px rgba(0,0,0,.25);
  }
  .carousel-control-prev, .carousel-control-next{
    filter: drop-shadow(0 4px 10px rgba(0,0,0,.25));
  }

  /* 도서 카드 그리드 */
  .book-grid .card{
    border:1px solid var(--bd); border-radius:16px;
    overflow:hidden; background:var(--card); box-shadow:var(--ring-soft);
    transition:transform .18s ease, box-shadow .18s ease;
  }
  .book-grid .card:hover{ transform:translateY(-2px); box-shadow:0 16px 36px rgba(16,24,40,.10); }
  .book-grid .card-img-top{ height:220px; object-fit:cover; }
  .book-grid .title{
    font-weight:700; color:var(--ink); margin-bottom:4px;
    display:-webkit-box; -webkit-line-clamp:2; -webkit-box-orient:vertical; overflow:hidden;
  }
  .book-grid .author{ color:var(--muted); font-size:.92rem; }
  .book-grid .price{ color:var(--brand); font-weight:800; }

  /* 페이지네이션(필칩) */
  .pagination .page-link{
    border-radius:999px; margin:0 4px; color:var(--brand);
    border:1px solid var(--bd); height:36px; min-width:36px; padding:.4rem .75rem;
  }
  .pagination .active .page-link{
    color:#fff; background:linear-gradient(135deg, var(--brand), var(--brand-2)); border-color:transparent;
    box-shadow:0 10px 18px rgba(79,70,229,.25);
  }

  /* 오른쪽 미니 안내 */
  .hint{ color:var(--muted); font-size:.9rem; margin-top:4px; }

  /* 유틸 */
  a.link{ text-decoration:none; color:inherit; }
  a.link:hover{ color:var(--brand); }
</style>
</head>
<body>

<div class="page-shell">
  <div class="section">
    <div class="d-flex justify-content-between align-items-start">
      <h1 class="section-title m-0">도서 목록</h1>
      <div class="hint d-none d-md-block">원하는 책을 찾아보세요.</div>
    </div>

    <!-- 검색 -->
    <form id="searchForm" class="searchbar mt-2 mb-4" onsubmit="return false;">
      <div class="input-group">
        <input type="text" id="keyword" name="keyword" value="${keyword}" class="form-control" placeholder="제목으로 책 검색" />
        <div class="input-group-append">
          <button type="submit" class="btn">검색</button>
        </div>
      </div>
    </form>

    <!-- 추천 도서 캐러셀 -->
    <c:if test="${not empty recommendedBooks}">
      <div id="recommendedCarousel" class="carousel slide hero mb-4" data-ride="carousel" data-interval="5000">
        <div class="carousel-inner">
          <c:forEach var="book" items="${recommendedBooks}" varStatus="s">
            <div class="carousel-item ${s.first ? 'active' : ''}">
              <img src="${book.cover_image}" alt="${book.title}">
              <div class="caption">${book.title}</div>
            </div>
          </c:forEach>
        </div>
        <a class="carousel-control-prev" href="#recommendedCarousel" role="button" data-slide="prev">
          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
          <span class="sr-only">이전</span>
        </a>
        <a class="carousel-control-next" href="#recommendedCarousel" role="button" data-slide="next">
          <span class="carousel-control-next-icon" aria-hidden="true"></span>
          <span class="sr-only">다음</span>
        </a>
      </div>
    </c:if>

    <!-- 그리드 -->
    <div id="bookContainer" class="row book-grid">
      <c:forEach var="b" items="${books}">
        <div class="col-6 col-sm-4 col-md-3 mb-4 d-flex">
          <div class="card w-100">
            <a class="link" href="${ctx}/user/bookDetail/${b.bookId}">
              <img class="card-img-top" src="${b.cover_image}" alt="${b.title}">
            </a>
            <div class="card-body">
              <div class="title"><a class="link" href="${ctx}/user/bookDetail/${b.bookId}">${b.title}</a></div>
              <div class="author mb-1">저자 · ${b.author}</div>
              <div class="price"><fmt:formatNumber value="${b.price}" pattern="#,###" />원</div>
            </div>
          </div>
        </div>
      </c:forEach>

      <c:if test="${empty books}">
        <div class="col-12 text-center py-5 text-muted">검색 결과가 없습니다.</div>
      </c:if>
    </div>

    <!-- 페이지네이션 -->
    <nav aria-label="Page navigation">
      <ul class="pagination justify-content-center mb-0">
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
  </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.9.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

<script>
  /* 디바운스 검색(AJAX) */
  (function(){
    var t;
    $('#keyword').on('keyup', function(){
      clearTimeout(t);
      var kw = this.value;
      t = setTimeout(function(){
        $.get('${ctx}/user/bookList', {keyword: kw, page: 1}, function(html){
          var $html = $(html);
          $('#bookContainer').html($html.find('#bookContainer').html());
        });
      }, 280);
    });

    $('#searchForm').on('submit', function(e){
      e.preventDefault();
      $('#keyword').trigger('keyup'); // 같은 로직 재활용
    });
  })();
</script>
</body>
</html>