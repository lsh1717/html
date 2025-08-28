<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
    --brand:#4F46E5;
    --brand-2:#7C3AED;
    --ink:#0f172a;
    --muted:#6b7280;
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

  .page-shell{max-width:1120px; margin:32px auto 56px; padding:0 16px;}
  @media (min-width:1200px){ .page-shell{margin-top:40px;} }

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

  /* 검색바 */
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

  /* 추천 도서 히어로 캐러셀 */
  .reco{
    border-radius:18px; overflow:hidden; box-shadow:var(--ring);
    border:1px solid var(--bd);
  }
  .reco .carousel-item{ height:340px; }
  @media (max-width:576px){ .reco .carousel-item{ height:300px; } }

  .reco-bg{
    position:absolute; inset:0;
    background-size:cover; background-position:center;
    filter: blur(20px) saturate(120%) brightness(95%);
    transform: scale(1.1);
  }
  .reco-vignette{
    position:absolute; inset:0;
    background:
      radial-gradient(110% 90% at 60% 20%, rgba(0,0,0,.0) 0%, rgba(0,0,0,.35) 70%),
      linear-gradient(0deg, rgba(0,0,0,.10), rgba(0,0,0,.10));
  }
  /* 오버레이는 클릭 통과 */
.reco-vignette,
.reco-bg { pointer-events: none; }

/* 내비 버튼이 가장 위 */
.reco .carousel-control-prev,
.reco .carousel-control-next { z-index: 10; pointer-events: auto; }

/* (선택) 콘텐츠는 버튼 위로 안 올라오게 */
.reco-content { z-index: 2; } /* 그대로 두되, 위 버튼이 10이니 OK */
  .reco-content{
    position:relative; z-index:2; height:100%;
    display:flex; align-items:center; gap:28px;
    padding:22px;
  }
  .reco-card{
    display:flex; align-items:center; gap:18px;
    background:rgba(255,255,255,.08);
    border:1px solid rgba(255,255,255,.22);
    border-radius:16px; padding:14px;
    backdrop-filter: blur(6px);
    box-shadow:0 10px 28px rgba(0,0,0,.28);
  }
  .reco-cover{
    width:160px; height:220px; object-fit:cover;
    border-radius:12px; background:#fff;
    box-shadow:0 16px 30px rgba(0,0,0,.35);
  }
  @media (max-width:576px){
    .reco-cover{ width:120px; height:168px; }
  }
  .reco-meta .badge{
    background:rgba(255,255,255,.22); color:#fff; font-weight:700;
    border:1px solid rgba(255,255,255,.35);
    border-radius:999px; padding:.25rem .6rem; font-size:.75rem;
    letter-spacing:.2px;
  }
  .reco-meta h3{
    color:#fff; font-size:1.25rem; font-weight:800; margin:.35rem 0 .25rem;
    text-shadow:0 2px 8px rgba(0,0,0,.25);
  }
  .reco-meta p{ color:#e6e6e6; margin:0 0 .6rem; }
  .btn-brand{
    color:#fff; font-weight:800; border:none;
    background:linear-gradient(135deg, var(--brand), var(--brand-2));
    box-shadow:0 10px 20px rgba(79,70,229,.35);
  }
  .btn-brand:hover{ color:#fff; transform:translateY(-1px); }

  .reco .carousel-control-prev, .reco .carousel-control-next{
    filter: drop-shadow(0 6px 10px rgba(0,0,0,.35));
  }
  .reco .carousel-indicators li{
    width:8px; height:8px; border-radius:50%; margin:0 4px;
    background:rgba(255,255,255,.55);
  }
  .reco .carousel-indicators .active{ background:#fff; }

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

  /* 페이지네이션 */
  .pagination .page-link{
    border-radius:999px; margin:0 4px; color:var(--brand);
    border:1px solid var(--bd); height:36px; min-width:36px; padding:.4rem .75rem;
  }
  .pagination .active .page-link{
    color:#fff; background:linear-gradient(135deg, var(--brand), var(--brand-2)); border-color:transparent;
    box-shadow:0 10px 18px rgba(79,70,229,.25);
  }

  .hint{ color:var(--muted); font-size:.9rem; margin-top:4px; }
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
      <div id="reco" class="carousel slide reco mb-4"
           data-ride="carousel" data-interval="5000" data-touch="true" data-pause="hover" data-keyboard="true">

        <ol class="carousel-indicators">
          <c:forEach var="b" items="${recommendedBooks}" varStatus="s">
            <li data-target="#reco" data-slide-to="${s.index}" class="${s.first ? 'active' : ''}"></li>
          </c:forEach>
        </ol>

        <div class="carousel-inner">
          <c:forEach var="b" items="${recommendedBooks}" varStatus="s">
            <c:set var="coverUrl" value="${empty b.cover_image ? ctx.concat('/resources/img/noimg.png') : b.cover_image}" />
            <div class="carousel-item ${s.first ? 'active' : ''}">
              <div class="reco-bg" style="background-image:url('${coverUrl}');"></div>
              <div class="reco-vignette"></div>

              <div class="reco-content container">
                <div class="reco-card">
                  <img class="reco-cover"
                       src="${coverUrl}"
                       alt="${fn:escapeXml(b.title)}"
                       onerror="this.src='${ctx}/resources/img/noimg.png'">
                  <div class="reco-meta">
                    <span class="badge">추천 도서</span>
                    <h3>${b.title}</h3>
                    <p>저자 · ${b.author}</p>
                    <a class="btn btn-brand btn-sm px-3"
                       href="${ctx}/user/bookDetail/${b.bookId}">
                      바로 보기
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </c:forEach>
        </div>

        <a class="carousel-control-prev" href="#reco" role="button" data-slide="prev" aria-label="이전">
          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        </a>
        <a class="carousel-control-next" href="#reco" role="button" data-slide="next" aria-label="다음">
          <span class="carousel-control-next-icon" aria-hidden="true"></span>
        </a>
      </div>
    </c:if>

    <!-- 도서 그리드 -->
    <div id="bookContainer" class="row book-grid">
      <c:forEach var="b" items="${books}">
        <c:set var="coverUrl" value="${empty b.cover_image ? ctx.concat('/resources/img/noimg.png') : b.cover_image}" />
        <div class="col-6 col-sm-4 col-md-3 mb-4 d-flex">
          <div class="card w-100">
            <a class="link" href="${ctx}/user/bookDetail/${b.bookId}">
              <img class="card-img-top"
                   src="${coverUrl}"
                   alt="${fn:escapeXml(b.title)}"
                   onerror="this.src='${ctx}/resources/img/noimg.png'">
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
<!-- Bootstrap 4.x → Popper 1.x 필수 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.1/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

<script>
  // 디바운스 검색(AJAX)
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
      $('#keyword').trigger('keyup');
    });
  })();

  // 키보드 ←/→ 로 캐러셀 이동
  $(document).on('keydown', function(e){
    if (e.key === 'ArrowRight') $('#reco').carousel('next');
    if (e.key === 'ArrowLeft')  $('#reco').carousel('prev');
  });

  // 터치 스와이프(모바일 UX)
  (function(){
    var $carousel = $('#reco');
    var startX = 0;
    $carousel.on('touchstart', function(e){ startX = e.originalEvent.touches[0].clientX; });
    $carousel.on('touchmove', function(e){
      if(!startX) return;
      var curX = e.originalEvent.touches[0].clientX;
      var diff = startX - curX;
      if(Math.abs(diff) > 40){
        $(this).carousel(diff > 0 ? 'next' : 'prev');
        startX = 0;
      }
    });
  })();
</script>
</body>
</html>