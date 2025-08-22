<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ttl" value="${empty pageTitle ? '도서 목록' : pageTitle}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>${ttl} | BookShop</title>

  <!-- Fonts / Bootstrap -->
  <link href="https://fonts.googleapis.com/css2?family=Pretendard:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

  <style>
    :root{
      --ink:#0f172a; --muted:#6b7280; --brand:#4F46E5; --ring:0 14px 36px rgba(16,24,40,.10);
      --card:#ffffff; --bg:#f5f7fb;
    }
    html,body{height:100%;}
    body{background:var(--bg); color:var(--ink); font-family:'Pretendard','Noto Sans KR',sans-serif;}

    /* ========= 헤더와 콘텐츠 간 ‘똑 떨어짐’ 해소 ========= */
    .wrap{
      min-height:100%;
      padding:24px 16px 64px;          /* 상단 여백 줄임 */
      position:relative; z-index:1;     /* 상단 그라데이션보다 위 */
      display:block;
    }
    body::before{                       /* 헤더-콘텐츠 자연스러운 블렌딩 */
      content:"";
      position:fixed; left:0; right:0; top:0; height:120px;
      background:linear-gradient(180deg,#ffffff 0%, rgba(255,255,255,.85) 42%, rgba(255,255,255,0) 100%);
      pointer-events:none; z-index:0;
    }
    @media (min-width:992px){ .wrap{ padding-top:20px; } }

    /* ========= '책 모양' 컨테이너 ========= */
    .book{
      position:relative; max-width:1120px; margin:0 auto; background:var(--card);
      border-radius:22px; border:1px solid #e9eef6; box-shadow:0 18px 44px rgba(23,36,58,.07), 0 -6px 14px rgba(23,36,58,.03) inset;
      overflow:hidden; margin-top:-6px;  /* 헤더와 살짝 맞물리게 */
    }
    /* Spine(책등) */
    .book::before{
      content:""; position:absolute; left:0; top:-2px; bottom:-2px; width:14px;
      background:linear-gradient(180deg,#eef3ff 0%, #e6ecff 100%);
      border-right:1px solid #dbe3f6;
    }
    /* Page edges */
    .book::after{
      content:""; position:absolute; right:0; top:4px; bottom:4px; width:10px;
      background:repeating-linear-gradient(180deg,#f8fafc 0 3px,#eef2f7 3px 6px);
      opacity:.65;
    }

    /* ========= 헤더(타이틀/도구) ========= */
    .head{ padding:18px 22px 10px 26px; display:flex; align-items:center; justify-content:space-between; }
    .head h2{ font-size:1.25rem; margin:0; font-weight:800; letter-spacing:-.02em; }
    .head .hint{ font-size:.85rem; color:var(--muted); }

    /* ========= 검색 바 ========= */
    .searchbar{ padding:8px 22px 16px 26px; }
    .searchbar .input-group{ max-width:560px; }
    .searchbar .form-control{
      height:44px; border:1px solid #dbe3f1; border-radius:999px 0 0 999px;
      box-shadow:0 6px 20px rgba(79,70,229,.06) inset;
    }
    .searchbar .btn{
      height:44px; border-radius:0 999px 999px 0; background:var(--brand); border-color:var(--brand);
      box-shadow:0 8px 18px rgba(79,70,229,.22); color:#fff; font-weight:700; padding:0 22px;
    }
    .searchbar .btn:hover{ background:#4338CA; border-color:#4338CA; }

    /* ========= 추천 캐러셀 ========= */
    .hero{ padding:0 22px 8px 26px; }
    .carousel-item img{ height:300px; object-fit:cover; border-radius:16px; }
    .carousel-caption{
      background:rgba(15,23,42,.45); backdrop-filter:blur(6px); border-radius:10px; padding:10px 14px;
    }
    .carousel-caption h5{ font-weight:700; color:#fff; margin:0; font-size:1rem; }

    /* ========= 그리드(카드) ========= */
    .grid{ padding:12px 22px 26px 26px; }
    .book-card{
      border:1px solid #e9edf3; border-radius:16px; overflow:hidden; background:#fff;
      box-shadow:0 12px 30px rgba(16,24,40,.06); transition:transform .15s ease, box-shadow .15s ease;
      height:100%; display:flex; flex-direction:column;
    }
    .book-card:hover{ transform:translateY(-2px); box-shadow:0 16px 36px rgba(16,24,40,.10); }
    .book-card img{ height:220px; object-fit:cover; }
    .book-card .card-body{ padding:14px; }
    .book-card .title{ font-weight:700; color:var(--ink); font-size:1rem; margin-bottom:4px; display:block; text-decoration:none; }
    .book-card .title:hover{ color:var(--brand); text-decoration:none; }
    .book-card .author{ color:var(--muted); font-size:.9rem; margin:0 0 6px; }
    .book-card .price{ color:var(--brand); font-weight:800; margin:0; }

    /* ========= 페이지네이션 ========= */
    .pagi{ padding:0 22px 28px 26px; }
    .pagination .page-link{ border-radius:999px; margin:0 4px; color:var(--brand); }
    .pagination .active .page-link{ background:var(--brand); border-color:var(--brand); color:#fff; }
  </style>
</head>
<body>

<div class="wrap">
  <div class="book">

    <!-- 헤더 -->
    <div class="head">
      <h2>${ttl}</h2>
      <div class="hint">원하는 책을 찾아보세요.</div>
    </div>

    <!-- 검색 -->
    <div class="searchbar">
      <form id="searchForm" class="mb-0">
        <div class="input-group">
          <input type="text" id="keyword" name="keyword" value="${keyword}" class="form-control" placeholder="제목으로 책 검색" />
          <div class="input-group-append">
            <button type="submit" class="btn">검색</button>
          </div>
        </div>
      </form>
    </div>

    <!-- 추천 도서 캐러셀 -->
    <c:if test="${not empty recommendedBooks}">
      <div class="hero">
        <div id="recommendedCarousel" class="carousel slide mb-3" data-ride="carousel">
          <div class="carousel-inner">
            <c:forEach var="book" items="${recommendedBooks}" varStatus="st">
              <div class="carousel-item ${st.first ? 'active' : ''}">
                <img src="${book.cover_image}" class="d-block w-100" alt="${book.title}">
                <div class="carousel-caption"><h5>${book.title}</h5></div>
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
      </div>
    </c:if>

    <!-- 도서 그리드 -->
    <div class="grid">
      <div id="bookContainer" class="row">
        <c:forEach var="book" items="${books}">
          <div class="col-sm-6 col-md-4 col-lg-3 mb-4 d-flex">
            <div class="book-card w-100">
              <a href="${ctx}/user/bookDetail/${book.bookId}">
                <img src="${book.cover_image}" class="w-100" alt="${book.title}">
              </a>
              <div class="card-body">
                <a class="title" href="${ctx}/user/bookDetail/${book.bookId}">${book.title}</a>
                <p class="author">저자: ${book.author}</p>
                <p class="price"><fmt:formatNumber value="${book.price}" pattern="#,###"/>원</p>
              </div>
            </div>
          </div>
        </c:forEach>

        <c:if test="${empty books}">
          <div class="col-12 text-center text-muted py-5">검색 결과가 없습니다.</div>
        </c:if>
      </div>
    </div>

    <!-- 페이지네이션 -->
    <div class="pagi">
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

  </div><!-- /.book -->
</div><!-- /.wrap -->

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.9.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

<script>
  // 디바운스 검색 (JSP EL 충돌 없도록 백틱 사용 안함)
  $(function(){
    var debounceTimer;

    $('#keyword').on('keyup', function(){
      clearTimeout(debounceTimer);
      var kw = $(this).val();

      debounceTimer = setTimeout(function(){
        $.ajax({
          url: '${ctx}/user/bookList',
          type: 'get',
          data: { keyword: kw, page: 1 },
          success: function(html){
            var $html = $(html);
            var newList = $html.find('#bookContainer').html() || '';
            $('#bookContainer').html(newList);
          }
        });
      }, 300);
    });

    $('#searchForm').on('submit', function(e){ e.preventDefault(); });
  });
</script>
</body>
</html>