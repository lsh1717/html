<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>BookShop Admin</title>

  <!-- CSRF -->
  <c:if test="${not empty _csrf}">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
  </c:if>

  <!-- CSS -->
  <link href="https://fonts.googleapis.com/css2?family=Pretendard:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

  <style>
    :root{
      --bg:#f5f7fb; --card:#ffffff; --text:#0f172a; --muted:#64748b;
      --primary:#60a5fa; --ring:0 2px 10px rgba(0,0,0,.06);
      --chip:#eef2ff; --chip-text:#334155; --table-head:#f8fafc; --border:rgba(15,23,42,.08);
      --grid: rgba(100,116,139,.25);
    }
    body.dark{
      --bg:#0b1220; --card:#0f172a; --text:#e5e7eb; --muted:#94a3b8; --primary:#60a5fa;
      --ring:0 2px 16px rgba(0,0,0,.35); --chip:#17213a; --chip-text:#cbd5e1;
      --table-head:#111827; --border:rgba(148,163,184,.15); --grid: rgba(148,163,184,.25);
    }
    html,body{height:100%}
    body{background:var(--bg); color:var(--text); font-family:'Pretendard','Noto Sans KR',sans-serif;}
    a{color:var(--text)}
    .navbar{background:var(--card)!important; box-shadow:var(--ring)}
    .brand{font-weight:700}.brand i{color:var(--primary)}
    .container-xl{max-width:1320px;margin:0 auto;padding:0 24px}
    .nav-pills .nav-link{color:var(--muted);background:transparent;border-radius:10px;margin-right:.5rem}
    .nav-pills .nav-link.active{background:rgba(96,165,250,.15);color:var(--text);font-weight:700;border:1px solid var(--border)}
    .card,.kpi{background:var(--card);border:none;border-radius:14px;box-shadow:var(--ring)}
    .kpi .label{font-size:.85rem;color:var(--muted)} .kpi .value{font-size:1.4rem;font-weight:700}
    .table{color:var(--text)}
    .table thead th{background:var(--table-head);border-top:none;color:var(--muted);border-bottom:1px solid var(--border)}
    .table td,.table th{border-color:var(--border)}
    .table tbody tr:hover{background:rgba(96,165,250,.06)}
    .btn-soft{background:var(--chip);color:var(--chip-text);border:none}
    .btn-soft:hover{background:rgba(96,165,250,.18)}
    .btn-outline-secondary{border-color:var(--border);color:var(--muted)}
    .btn-outline-secondary:hover{background:rgba(96,165,250,.15);color:var(--text)}
    .mini{font-size:.9rem;color:var(--muted)}
    .cover{width:42px;height:56px;object-fit:cover;border-radius:6px;box-shadow:0 1px 4px rgba(0,0,0,.25)}
    footer{color:var(--muted)}
    .badge-danger{background:#ef4444}
    .text-truncate-2{display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden;max-width:520px}
    .chip{display:inline-block;padding:6px 10px;border-radius:999px;font-weight:700;font-size:.8rem;border:1px solid transparent;line-height:1}
    .chip-paid{background:#111827;color:#e5e7eb;border-color:#1f2937}
    .chip-ship{background:#172554;color:#60a5fa;border-color:#1d3a7a}
    .chip-done{background:#052e1b;color:#34d399;border-color:#115e3b}
    .chip-cancel{background:#3b0a0a;color:#fca5a5;border-color:#7f1d1d}
    .chip-vis{background:#0a1f12;color:#34d399;border-color:#115e3b}
    .chip-hid{background:#2f0b0b;color:#fca5a5;border-color:#7f1d1d}
    .btn-mini{padding:.25rem .5rem;font-size:.8rem}
  </style>
</head>

<body class="dark">
<nav class="navbar navbar-expand navbar-dark">
  <div class="container-xl d-flex align-items-center w-100">
    <a class="navbar-brand brand mr-auto" href="${ctx}/admin/dashboard">
      <i class="bi bi-bar-chart-fill mr-2"></i>BookShop Admin
    </a>
    <div class="d-flex align-items-center ml-auto">
      <button id="themeBtn" type="button" class="btn btn-soft btn-sm mr-2">
        <i class="bi bi-moon-stars mr-1"></i><span>다크</span>
      </button>
      <form action="${ctx}/logout" method="post" class="m-0">
        <c:if test="${not empty _csrf}">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </c:if>
        <button class="btn btn-outline-secondary btn-sm">로그아웃</button>
      </form>
    </div>
  </div>
</nav>

<div class="container-xl py-4">
  <ul class="nav nav-pills mb-4">
    <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#tab-dashboard">대시보드</a></li>
    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#tab-books">도서 관리</a></li>
    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#tab-orders">주문 관리</a></li>
    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#tab-users">유저 관리</a></li>
    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#tab-reviews">리뷰 관리</a></li>
  </ul>

  <div class="tab-content">

    <!-- DASHBOARD -->
    <div class="tab-pane fade show active" id="tab-dashboard">
      <div class="row">
        <div class="col-md-2 mb-3"><div class="kpi p-3"><div class="label"><i class="bi bi-people mr-1"></i>총 회원 수</div><div class="value">${totalUsers}</div></div></div>
        <div class="col-md-2 mb-3"><div class="kpi p-3"><div class="label"><i class="bi bi-book mr-1"></i>총 도서 수</div><div class="value">${totalBooks}</div></div></div>
        <div class="col-md-2 mb-3"><div class="kpi p-3"><div class="label"><i class="bi bi-bag-check mr-1"></i>오늘 주문 건수</div><div class="value">${todayOrders}</div></div></div>
        <div class="col-md-2 mb-3"><div class="kpi p-3"><div class="label"><i class="bi bi-cash-coin mr-1"></i>오늘 매출</div><div class="value"><fmt:formatNumber value="${todayRevenue}" pattern="#,###원"/></div></div></div>
        <div class="col-md-2 mb-3"><div class="kpi p-3"><div class="label"><i class="bi bi-box-seam mr-1"></i>출고 대기</div><div class="value"><c:out value="${pendingCount != null ? pendingCount : 1}"/></div></div></div>
        <div class="col-md-2 mb-3"><div class="kpi p-3"><div class="label"><i class="bi bi-truck mr-1"></i>배송 중</div><div class="value"><c:out value="${shippingCount != null ? shippingCount : 1}"/></div></div></div>
      </div>

      <div class="row">
        <!-- sales line -->
        <div class="col-lg-8 mb-3">
          <div class="card p-3">
            <h6 class="mb-3">일자별 매출</h6>
            <canvas id="salesChart" height="110"></canvas>
          </div>
        </div>

        <!-- recent orders -->
        <div class="col-lg-4 mb-3">
          <div class="card p-3">
            <h6 class="mb-3">최근 주문</h6>
            <div class="table-responsive">
              <table class="table table-sm mb-0">
                <thead><tr><th>주문번호</th><th>회원</th><th>총액</th></tr></thead>
                <tbody>
                  <c:forEach var="o" items="${recentOrders}">
                    <tr>
                      <td>${o.orderId}</td>
                      <td>${o.userId}</td>
                      <td><fmt:formatNumber value="${o.totalAmount}" pattern="#,###"/>원</td>
                    </tr>
                  </c:forEach>
                  <c:if test="${empty recentOrders}">
                    <tr><td colspan="3" class="text-center mini">데이터 없음</td></tr>
                  </c:if>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <!-- top5 -->
        <div class="col-lg-4 mb-3">
          <div class="card p-3">
            <h6 class="mb-3">최근 30일 인기 도서 TOP5</h6>
            <canvas id="topBooksChart" height="160"></canvas>
          </div>
        </div>

        <!-- status bar -->
        <div class="col-lg-4 mb-3">
          <div class="card p-3">
            <h6 class="mb-3">주문 상태</h6>
            <canvas id="statusChart" height="160"></canvas>
          </div>
        </div>

        <!-- low stock -->
        <div class="col-lg-4 mb-3">
          <div class="card p-3">
            <h6 class="mb-3">재고 임박</h6>
            <div class="table-responsive">
              <table class="table table-sm mb-0">
                <thead><tr><th>표지</th><th>제목</th><th>저자</th><th>재고</th></tr></thead>
                <tbody>
                  <c:forEach var="b" items="${lowStockBooks}">
                    <tr>
                      <td><img class="cover" src="${b.cover_image}" alt=""></td>
                      <td class="mini">${b.title}</td>
                      <td class="mini">${b.author}</td>
                      <td><span class="badge badge-danger">${b.stock}</span></td>
                    </tr>
                  </c:forEach>
                  <c:if test="${empty lowStockBooks}">
                    <tr><td colspan="4" class="text-center mini">임박 도서 없음</td></tr>
                  </c:if>
                </tbody>
              </table>
            </div>
          </div>
        </div>

      </div>
    </div>

    <!-- BOOKS TAB -->
    <div class="tab-pane fade" id="tab-books">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="m-0">도서 관리</h5>
        <div class="d-flex">
          <form id="bookSearchForm" class="form-inline mr-2">
            <input type="text" class="form-control form-control-sm mr-2" id="bkKeyword" placeholder="제목/저자 검색">
            <button class="btn btn-outline-primary btn-sm">검색</button>
          </form>
          <button id="btnNewBook" class="btn btn-primary btn-sm">
            <i class="bi bi-plus-lg mr-1"></i>도서 등록
          </button>
        </div>
      </div>

      <div class="card">
        <div class="table-responsive">
          <table class="table table-dark align-middle mb-0" id="booksTable">
            <thead>
              <tr>
                <th style="width:84px">표지</th>
                <th>제목</th>
                <th style="width:220px">저자</th>
                <th style="width:120px" class="text-right">가격</th>
                <th style="width:100px" class="text-right">재고</th>
                <th style="width:170px" class="text-right">관리</th>
              </tr>
            </thead>
            <tbody></tbody>
          </table>
        </div>
      </div>

      <nav class="mt-3">
        <ul class="pagination pagination-sm justify-content-center" id="booksPager"></ul>
      </nav>

      <!-- Book modal -->
      <div class="modal fade" id="bookModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
          <div class="modal-content" style="background:var(--card); border:1px solid var(--border);">
            <div class="modal-header">
              <h6 class="modal-title">도서 등록</h6>
              <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
            </div>
            <div class="modal-body">
              <form id="bookForm">
                <input type="hidden" id="bookId">
                <div class="form-row">
                  <div class="form-group col-md-8">
                    <label>제목</label>
                    <input type="text" class="form-control" id="title" required>
                  </div>
                  <div class="form-group col-md-4">
                    <label>저자</label>
                    <input type="text" class="form-control" id="author" required>
                  </div>
                  <div class="form-group col-md-8">
                    <label>표지 이미지 URL</label>
                    <input type="text" class="form-control" id="cover_image" placeholder="https://...">
                    <small class="form-text text-muted">S3/이미지 서버 주소 또는 외부 URL</small>
                  </div>
                  <div class="form-group col-md-4">
                    <label>재고</label>
                    <input type="number" class="form-control" id="stock" min="0" required>
                  </div>
                  <div class="form-group col-md-12">
                    <label>설명</label>
                    <textarea class="form-control" id="description" rows="4"></textarea>
                  </div>
                  <div class="form-group col-md-4">
                    <label>가격(원)</label>
                    <input type="number" class="form-control" id="price" min="0" required>
                  </div>
                  <div class="form-group col-md-8 d-flex align-items-end">
                    <img id="coverPreview" src="" alt="" style="width:96px;height:96px;object-fit:cover;border-radius:12px;border:1px solid #2a3340;display:none">
                  </div>
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button class="btn btn-outline-secondary" data-dismiss="modal">닫기</button>
              <button id="btnSaveBook" class="btn btn-primary">저장</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ORDERS TAB -->
    <div class="tab-pane fade" id="tab-orders">
      <div class="d-flex flex-wrap justify-content-between align-items-center mb-3">
        <h5 class="m-0">주문 관리</h5>
        <div class="d-flex align-items-center">
          <form id="orderSearchForm" class="form-inline mr-2">
            <select id="ordStatus" class="form-control form-control-sm mr-2">
              <option value="">전체 상태</option>
              <option value="PAID">결제 완료</option>
              <option value="SHIPPING">배송중</option>
              <option value="DELIVERED">배송완료</option>
              <option value="CANCELLED">취소</option>
            </select>
            <input type="text" id="ordKeyword" class="form-control form-control-sm mr-2" placeholder="주문번호/회원ID 검색">
            <button class="btn btn-outline-primary btn-sm">검색</button>
          </form>
        </div>
      </div>

      <div class="card">
        <div class="table-responsive">
          <table class="table table-dark align-middle mb-0" id="ordersTable">
            <thead>
              <tr>
                <th style="width:110px">주문번호</th>
                <th style="width:120px">주문일</th>
                <th style="width:100px">회원ID</th>
                <th>품목(요약)</th>
                <th style="width:130px" class="text-right">총액</th>
                <th style="width:130px" class="text-center">배송상태</th>
                <th style="width:210px" class="text-right">관리</th>
              </tr>
            </thead>
            <tbody></tbody>
          </table>
        </div>
      </div>

      <nav class="mt-3">
        <ul class="pagination pagination-sm justify-content-center" id="ordersPager"></ul>
      </nav>

      <!-- items template -->
      <script type="text/template" id="tpl-items">
        <tr class="row-items">
          <td colspan="7" class="p-0">
            <div class="p-3" style="background:#0b1220;">
              <div class="table-responsive">
                <table class="table table-sm mb-0">
                  <thead>
                    <tr><th style="width:60px">#</th><th>도서</th><th style="width:100px" class="text-right">단가</th><th style="width:60px" class="text-right">수량</th><th style="width:120px" class="text-right">금액</th></tr>
                  </thead>
                  <tbody>{{rows}}</tbody>
                </table>
              </div>
            </div>
          </td>
        </tr>
      </script>
    </div>

    <!-- USERS TAB -->
    <div class="tab-pane fade" id="tab-users">
      <div class="d-flex flex-wrap justify-content-between align-items-center mb-3">
        <h5 class="m-0">유저 관리</h5>
        <div class="d-flex align-items-center">
          <form id="userSearchForm" class="form-inline">
            <select id="userRoleFilter" class="form-control form-control-sm mr-2">
              <option value="">전체 권한</option>
              <option value="ADMIN">admin</option>
              <option value="CUSTOMER">customer</option>
            </select>
            <select id="userBlockedFilter" class="form-control form-control-sm mr-2">
              <option value="">차단 여부</option>
              <option value="N">정상(N)</option>
              <option value="Y">차단(Y)</option>
            </select>
            <input type="text" id="userKeyword" class="form-control form-control-sm mr-2" placeholder="ID/로그인/이름">
            <button class="btn btn-outline-primary btn-sm">검색</button>
          </form>
        </div>
      </div>

      <div class="card">
        <div class="table-responsive">
          <table class="table table-dark align-middle mb-0" id="usersTable">
            <thead>
              <tr>
                <th style="width:90px">회원ID</th>
                <th style="width:160px">로그인</th>
                <th>이름</th>
                <th style="width:140px" class="text-center">권한</th>
                <th style="width:120px" class="text-center">상태</th>
                <th style="width:240px" class="text-right">관리</th>
              </tr>
            </thead>
            <tbody></tbody>
          </table>
        </div>
      </div>

      <nav class="mt-3">
        <ul class="pagination pagination-sm justify-content-center" id="usersPager"></ul>
      </nav>
    </div>

    <!-- REVIEWS TAB -->
    <div class="tab-pane fade" id="tab-reviews">
      <div class="d-flex flex-wrap justify-content-between align-items-center mb-3">
        <h5 class="m-0">리뷰 관리</h5>
        <form id="rvSearchForm" class="form-inline">
          <select id="rvRating" class="form-control form-control-sm mr-2">
            <option value="">전체 평점</option>
            <option value="5">★5</option><option value="4">★4</option><option value="3">★3</option>
            <option value="2">★2</option><option value="1">★1</option>
          </select>
          <select id="rvStatus" class="form-control form-control-sm mr-2">
            <option value="">전체 상태</option>
            <option value="VISIBLE">노출</option>
            <option value="HIDDEN">숨김</option>
          </select>
          <input type="text" id="rvBookId" class="form-control form-control-sm mr-2" placeholder="도서ID">
          <input type="text" id="rvKeyword" class="form-control form-control-sm mr-2" placeholder="작성자/내용/도서 검색">
          <button class="btn btn-outline-primary btn-sm">검색</button>
        </form>
      </div>

      <div class="card">
        <div class="table-responsive">
          <table class="table table-dark align-middle mb-0" id="reviewsTable">
            <thead>
              <tr>
                <th style="width:90px">리뷰ID</th>
                <th style="width:220px">도서</th>
                <th style="width:160px">작성자</th>
                <th style="width:100px" class="text-center">평점</th>
                <th>내용</th>
                <th style="width:140px">작성일</th>
                <th style="width:110px" class="text-center">상태</th>
                <th style="width:210px" class="text-right">관리</th>
              </tr>
            </thead>
            <tbody></tbody>
          </table>
        </div>
      </div>

      <nav class="mt-3">
        <ul class="pagination pagination-sm justify-content-center" id="reviewsPager"></ul>
      </nav>
    </div>

  </div>

  <footer class="text-center mt-4">&copy; 2025 BookShop. All rights reserved.</footer>
</div>

<!-- 리뷰 상세 모달 -->
<div class="modal fade" id="rvModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content" style="background:var(--card); border:1px solid var(--border);">
      <div class="modal-header">
        <h6 class="modal-title">리뷰 상세</h6>
        <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
      </div>
      <div class="modal-body">
        <div class="mb-2"><b>도서</b>: <span id="rvmBook"></span></div>
        <div class="mb-2"><b>작성자</b>: <span id="rvmUser"></span></div>
        <div class="mb-2"><b>평점</b>: <span id="rvmRating"></span></div>
        <div class="mb-2"><b>작성일</b>: <span id="rvmDate"></span></div>
        <hr/>
        <div id="rvmContent" class="white-space-prewrap"></div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-outline-secondary" data-dismiss="modal">닫기</button>
      </div>
    </div>
  </div>
</div>

<!-- JS (Bootstrap4 + Popper v1, Chart.js v4) -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.1/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>

<script>
  /* Theme toggle */
  (function(){
    var saved = localStorage.getItem('theme') || 'dark';
    if(saved === 'dark') document.body.classList.add('dark'); else document.body.classList.remove('dark');
    document.querySelector('#themeBtn span').textContent = document.body.classList.contains('dark') ? '다크' : '라이트';
    document.getElementById('themeBtn').addEventListener('click', function(){
      document.body.classList.toggle('dark');
      localStorage.setItem('theme', document.body.classList.contains('dark') ? 'dark' : 'light');
      document.querySelector('#themeBtn span').textContent = document.body.classList.contains('dark') ? '다크' : '라이트';
      recolorCharts();
    });
  })();

  /* Dashboard data (server -> fallback) */
  const labelSales = ${revenueLabels != null ? revenueLabels : '["3월","4월","5월","6월","7월","8월"]'};
  const dataSales  = ${revenueData   != null ? revenueData   : '[120000,95000,110000,150000,170000,140000]'};

  // 상태 차트용 (서버값 없으면 안전한 기본값 사용)
  const statusLbl  = ${statusLabels != null ? statusLabels : '["SHIPPING","DELIVERED","CANCELLED","PAID"]'};
  const statusDat  = ${statusData   != null ? statusData   : '[4,2,1,1]'};

  /* Chart helpers */
  function cssVar(name){ return getComputedStyle(document.body).getPropertyValue(name).trim(); }
  function gridColor(){ return cssVar('--grid'); }
  function textColor(){ return cssVar('--muted'); }
  function primary(){ return cssVar('--primary'); }

  const STATUS_PALETTE = {
    PAID:      { bg:'rgba(23,37,84,0.6)',    border:'rgba(29,58,122,0.9)' },
    SHIPPING:  { bg:'rgba(96,165,250,0.6)',  border:'rgba(96,165,250,0.9)' },
    SHIPPED:   { bg:'rgba(23,37,84,0.6)',    border:'rgba(29,58,122,0.9)' },
    DELIVERED: { bg:'rgba(5,46,27,0.6)',     border:'rgba(17,94,59,0.9)'  },
    CANCELLED: { bg:'rgba(59,10,10,0.6)',    border:'rgba(127,29,29,0.9)' },
    PENDING:   { bg:'rgba(100,116,139,0.6)', border:'rgba(71,85,105,0.9)' }
  };

  let salesChart, topChart, statChart;

  function buildCharts(){
    Chart.defaults.color = textColor();
    Chart.defaults.borderColor = gridColor();

    // 매출 라인
    const ctx = document.getElementById('salesChart').getContext('2d');
    const grad = ctx.createLinearGradient(0,0,0,220);
    grad.addColorStop(0, primary()+'66'); grad.addColorStop(1, primary()+'00');
    salesChart = new Chart(ctx, {
      type:'line',
      data:{labels:labelSales, datasets:[{data:dataSales, fill:true, tension:.35, borderWidth:2, borderColor:primary(), backgroundColor:grad, pointRadius:2}]},
      options:{plugins:{legend:{display:false}}, scales:{x:{grid:{color:gridColor()}}, y:{grid:{color:gridColor()}, ticks:{callback:(v)=>v.toLocaleString()}}}}
    });

    // TOP5
    buildTopBooksChart();

    // 상태 바
    const statBg     = statusLbl.map(s => (STATUS_PALETTE[s]?.bg)     || primary());
    const statBorder = statusLbl.map(s => (STATUS_PALETTE[s]?.border) || primary());
    statChart = new Chart(document.getElementById('statusChart'), {
      type:'bar',
      data:{labels:statusLbl, datasets:[{data:statusDat, backgroundColor:statBg, borderColor:statBorder, borderWidth:1}]},
      options:{
        plugins:{legend:{display:false}},
        scales:{
          x:{grid:{color:gridColor()}, ticks:{color:textColor()}},
          y:{beginAtZero:true, grid:{color:gridColor()}, ticks:{stepSize:1, color:textColor()}}
        }
      }
    });
  }

//길면 두 줄(최대)로 감싸고 너무 길면 … 처리
  function wrapLabel(label, maxPerLine = 6) {
    const s = String(label || '');
    // 우선 공백 기준으로 줄 바꿈, 한 단어가 너무 길면 문자 단위로 쪼갬
    const words = s.split(/\s+/);
    const lines = [];
    let line = '';

    function pushChunks(word) {
      if (word.length <= maxPerLine) { lines.push(word); return; }
      for (let i = 0; i < word.length; i += maxPerLine) {
        lines.push(word.slice(i, i + maxPerLine));
        if (lines.length >= 2) break;
      }
    }

    for (const w of words) {
      const next = line ? (line + ' ' + w) : w;
      if (next.length <= maxPerLine) {
        line = next;
      } else {
        if (line) lines.push(line);
        if (lines.length >= 2) break;
        if (w.length <= maxPerLine) {
          line = w;
        } else {
          pushChunks(w);
          line = '';
          if (lines.length >= 2) break;
        }
      }
    }
    if (lines.length < 2 && line) lines.push(line);
    if (lines.length > 2) lines.length = 2;
    // 너무 많으면 마지막 줄 말줄임
    if (lines.length === 2 && (lines[0] + lines[1]).length < s.length && lines[1].length >= maxPerLine) {
      lines[1] = lines[1].slice(0, maxPerLine - 1) + '…';
    }
    return lines.length ? lines : [s];
  }

  // 🔁 기존 함수 교체
  function buildTopBooksChart(){
    const el = document.getElementById('topBooksChart');
    if(!el) return;

    $.get('${ctx}/admin/api/books/top?days=30&limit=5', function(res){
      const rawLabels = res.labels || [];
      const units     = res.units  || [];
      const wrapped   = rawLabels.map(l => wrapLabel(l, 6)); // 글자수 조정하려면 6을 바꿔줘

      topChart = new Chart(el, {
        type:'bar',
        data:{
          labels: wrapped, // 멀티라인 라벨
          datasets:[{
            data: units,
            backgroundColor: 'rgba(96,165,250,0.35)',
            borderColor: primary(),
            borderWidth: 1,
            categoryPercentage: 0.7, // 막대/카테고리 폭 줄여 간격 확보
            barPercentage: 0.8
          }]
        },
        options:{
          layout: { padding: { bottom: 10 } }, // 아래쪽 여유
          plugins:{
            legend:{ display:false },
            // 툴팁 제목은 원본 전체 제목으로
            tooltip:{ callbacks:{ title: (items)=> rawLabels[items[0].dataIndex] } }
          },
          scales:{
            x:{
              offset:true,
              grid:{ color:gridColor() },
              ticks:{
                color:textColor(),
                autoSkip:false,  // 5개 모두 표시
                maxRotation:0,
                minRotation:0,
                // (labels에 이미 줄바꿈이 들어있어서 callback 불필요)
                font:{ size: 11 }
              }
            },
            y:{
              beginAtZero:true,
              grid:{ color:gridColor() },
              ticks:{ color:textColor(), precision:0, stepSize:1 }
            }
          }
        }
      });
    }).fail(function(){
      topChart = new Chart(el, {
        type:'bar',
        data:{ labels:[['-']], datasets:[{ data:[0], backgroundColor:'rgba(96,165,250,0.35)', borderColor:primary(), borderWidth:1 }]},
        options:{ plugins:{legend:{display:false}}, scales:{ x:{grid:{color:gridColor()}}, y:{beginAtZero:true, grid:{color:gridColor()}} } }
      });
    });
  }

  function recolorCharts(){
    if(salesChart) salesChart.destroy();
    if(topChart)   topChart.destroy();
    if(statChart)  statChart.destroy();
    buildCharts();
  }
  buildCharts();

  /* 공통 유틸 */
  function fmtWon(n){ if(n==null) return '-'; try{return Number(n).toLocaleString('ko-KR')+'원';}catch(e){return n;} }
  function fmtDate(s){ if(!s) return ''; var d=new Date(s); if(isNaN(d)) return s; var z=n=>String(n).padStart(2,'0'); return d.getFullYear()+'.'+z(d.getMonth()+1)+'.'+z(d.getDate()); }
  function escapeHtml(s){ return String(s==null?'':s).replace(/[&<>\"']/g,m=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m])); }
  function stars(n){ n=Number(n)||0; if(n<0)n=0; if(n>5)n=5; return '★'.repeat(n)+'☆'.repeat(5-n); }

  /* Orders (AJAX) */
  var ORD={api:'${ctx}/admin/api/orders',page:1,size:10,status:'',keyword:'',csrf:(function(){var t=document.querySelector('meta[name="_csrf"]');var h=document.querySelector('meta[name="_csrf_header"]');return{token:t?t.content:null,header:h?h.content:null};})()};
  function ordHeaders(){var h={'Content-Type':'application/json'};if(ORD.csrf.token&&ORD.csrf.header)h[ORD.csrf.header]=ORD.csrf.token;return h;}
  function statusLabel(s){return s==='PAID'?'결제 완료':s==='SHIPPING'?'배송중':s==='DELIVERED'?'배송완료':s==='CANCELLED'?'취소':s;}
  function loadOrders(p){if(!p)p=1;ORD.page=p;$.get(ORD.api,{page:ORD.page,size:ORD.size,status:ORD.status,keyword:ORD.keyword},function(res){renderOrders(res.items||[]);renderOPager(res.page||1,res.totalPage||1);}).fail(function(x){alert('주문 목록을 불러오지 못했습니다.');console.error(x.responseText);});}
  function renderOrders(items){
    var $tb=$('#ordersTable tbody').empty();
    if(!items.length){$tb.append('<tr><td colspan="7" class="text-center mini">데이터 없음</td></tr>');return;}
    items.forEach(function(o){
      var chip=(o.status==='PAID')?'chip-paid':(o.status==='SHIPPING')?'chip-ship':(o.status==='DELIVERED')?'chip-done':(o.status==='CANCELLED')?'chip-cancel':'';
      var canDel=(o.status==='PAID');
      var st=['PAID','SHIPPING','DELIVERED','CANCELLED']; var opts='';
      for(var i=0;i<st.length;i++){var s=st[i];opts+='<option value="'+s+'"'+(s===o.status?' selected':'')+'>'+statusLabel(s)+'</option>';}
      var row='<tr id="ord-'+o.orderId+'">'+
              '<td><div class="fw-bold">#'+o.orderId+'</div><div class="mini">'+fmtDate(o.orderDate)+'</div></td>'+
              '<td>'+fmtDate(o.orderDate)+'</td>'+
              '<td>'+o.userId+'</td>'+
              '<td><div class="text-truncate" style="max-width:520px;">'+escapeHtml(o.summary||'')+'</div><button class="btn btn-mini btn-soft mt-1" onclick="toggleItems('+o.orderId+')"><i class="bi bi-chevron-expand"></i> 상세</button></td>'+
              '<td class="text-right">'+fmtWon(o.totalAmount)+'</td>'+
              '<td class="text-center"><span class="chip '+chip+'">'+statusLabel(o.status)+'</span></td>'+
              '<td class="text-right"><select id="sel-'+o.orderId+'" class="form-control form-control-sm d-inline-block" style="width:120px">'+opts+'</select> '+
              '<button class="btn btn-primary btn-sm btn-mini" onclick="applyStatus('+o.orderId+')">적용</button> '+
              '<button class="btn btn-outline-danger btn-sm btn-mini" '+(canDel?'':'disabled')+' onclick="delOrder('+o.orderId+')">삭제</button></td>'+
              '</tr>';
      $tb.append(row);
    });
  }
  function renderOPager(p,t){var $pg=$('#ordersPager').empty();if(t<=1)return;for(var i=1;i<=t;i++){var a=(i===p)?'active':'';$pg.append('<li class="page-item '+a+'"><a class="page-link" href="#" onclick="loadOrders('+i+');return false;">'+i+'</a></li>');}}
  function applyStatus(id){var v=$('#sel-'+id).val();$.ajax({url:ORD.api+'/'+id+'/status',method:'PUT',headers:ordHeaders(),data:JSON.stringify({status:v}),success:function(){loadOrders(ORD.page);},error:function(x){alert('상태 변경 실패');console.error(x.responseText);}});}
  function delOrder(id){if(!confirm('정말 삭제할까요? (결제 완료 단계만 가능)'))return;$.ajax({url:ORD.api+'/'+id,method:'DELETE',headers:ordHeaders(),success:function(){loadOrders(ORD.page);},error:function(x){if(x.status===409)alert('결제 완료 상태에서만 삭제할 수 있습니다.');else alert('삭제 실패');console.error(x.responseText);}});}
  function toggleItems(id){
    var $r=$('#ord-'+id);var opened=$r.next().hasClass('row-items');if(opened){$r.next().remove();return;}
    $.get(ORD.api+'/'+id,function(o){
      var items=o.items||[];var rows='';
      for(var i=0;i<items.length;i++){
        var it=items[i];var price=fmtWon(it.price);var qty=(it.quantity!=null?it.quantity:0);
        var total=fmtWon((Number(it.price||0)*Number(qty||0)));
        var title=escapeHtml((it.book&&it.book.title)?it.book.title:('#'+it.bookId));
        rows+='<tr><td class="text-muted">'+(i+1)+'</td><td>'+title+'</td><td class="text-right">'+price+'</td><td class="text-right">'+qty+'</td><td class="text-right">'+total+'</td></tr>';
      }
      if(rows===''){rows='<tr><td colspan="5" class="text-center mini">아이템 없음</td></tr>';}
      var html=document.getElementById('tpl-items').innerHTML.replace('{{rows}}',rows);$r.after(html);
    });
  }
  $('#orderSearchForm').on('submit',function(e){e.preventDefault();ORD.status=$('#ordStatus').val();ORD.keyword=$('#ordKeyword').val().trim();loadOrders(1);});
  var ordersLoadedOnce=false;$('a[href="#tab-orders"]').on('shown.bs.tab',function(){if(!ordersLoadedOnce){ordersLoadedOnce=true;loadOrders(1);}});

  /* Books (AJAX) */
  var BK={ctx:'${ctx}',api:'${ctx}/admin/api/books',page:1,size:10,keyword:'',csrf:(function(){var t=document.querySelector('meta[name="_csrf"]');var h=document.querySelector('meta[name="_csrf_header"]');return{token:t?t.content:null,header:h?h.content:null};})()};
  function bkHeaders(){var h={'Content-Type':'application/json'};if(BK.csrf.token&&BK.csrf.header)h[BK.csrf.header]=BK.csrf.token;return h;}
  function loadBooks(p){if(!p)p=1;BK.page=p;$.ajax({url:BK.api,method:'GET',data:{page:BK.page,size:BK.size,keyword:BK.keyword},success:function(res){renderBooks(res.items||[]);renderPager(res.page||1,res.totalPage||1);},error:function(x){alert('도서 목록을 불러오지 못했습니다.');console.error(x.responseText);}});}
  function renderBooks(items){
    var $tb=$('#booksTable tbody').empty();
    if(!items.length){$tb.append('<tr><td colspan="6" class="text-center mini">검색 결과가 없습니다.</td></tr>');return;}
    items.forEach(function(b){
      var cover=(b.cover_image||b.coverImage)?(b.cover_image||b.coverImage):(BK.ctx+'/resources/img/noimg.png');
      var row='<tr>'+
        '<td><img src="'+cover+'" style="width:52px;height:52px;object-fit:cover;border-radius:10px;border:1px solid #2a3340;"></td>'+
        '<td><div class="fw-bold">'+escapeHtml(b.title||'')+'</div><div class="text-muted small text-truncate-2">'+escapeHtml(b.description||'')+'</div></td>'+
        '<td>'+escapeHtml(b.author||'')+'</td>'+
        '<td class="text-right">'+fmtWon(b.price)+'</td>'+
        '<td class="text-right">'+((b.stock!=null)?b.stock:0)+'</td>'+
        '<td class="text-right"><button class="btn btn-sm btn-outline-secondary" data-action="edit" data-id="'+b.bookId+'">수정</button> '+
        '<button class="btn btn-sm btn-outline-danger" data-action="del" data-id="'+b.bookId+'">삭제</button></td>'+
      '</tr>';
      $tb.append(row);
    });
  }
  function renderPager(p,t){var $pg=$('#booksPager').empty();if(t<=1)return;for(var i=1;i<=t;i++){var a=(i===p)?'active':'';$pg.append('<li class="page-item '+a+'"><a class="page-link" href="#" data-page="'+i+'">'+i+'</a></li>');}}
  function openNew(){ $('#bookModal .modal-title').text('도서 등록'); $('#bookForm')[0].reset(); $('#bookId').val(''); $('#coverPreview').hide(); $('#bookModal').modal('show');}
  function openEdit(id){
    $.get(BK.api+'/'+id,function(b){
      $('#bookModal .modal-title').text('도서 수정');
      $('#bookId').val(b.bookId); $('#title').val(b.title); $('#author').val(b.author);
      $('#description').val(b.description); $('#price').val(b.price); $('#stock').val(b.stock);
      $('#cover_image').val(b.cover_image||b.coverImage||'');
      if(b.cover_image||b.coverImage){$('#coverPreview').attr('src',(b.cover_image||b.coverImage)).show();}else{$('#coverPreview').hide();}
      $('#bookModal').modal('show');
    });
  }
  function saveBook(){
    var id=$('#bookId').val();
    var payload={title:$('#title').val(),author:$('#author').val(),description:$('#description').val(),
                 price:Number($('#price').val()||0),stock:Number($('#stock').val()||0),cover_image:$('#cover_image').val()};
    var method=id?'PUT':'POST'; var url=id?(BK.api+'/'+id):BK.api;
    $.ajax({url:url,method:method,headers:bkHeaders(),data:JSON.stringify(payload),
      success:function(){$('#bookModal').modal('hide');loadBooks(BK.page);},
      error:function(x){alert('저장에 실패했습니다.');console.error(x.responseText);}
    });
  }
  function delBook(id){
    if(!confirm('정말 삭제할까요?'))return;
    $.ajax({url:BK.api+'/'+id,method:'DELETE',headers:bkHeaders(),
      success:function(){loadBooks(BK.page);},
      error:function(x){alert('삭제에 실패했습니다.');console.error(x.responseText);}
    });
  }
  $('#btnNewBook').on('click', openNew);
  $('#btnSaveBook').on('click', saveBook);
  $('#cover_image').on('input', function(){ var v=$(this).val().trim(); if(v){ $('#coverPreview').attr('src', v).show(); } else { $('#coverPreview').hide(); } });
  $('#booksTable').on('click', 'button[data-action="edit"]', function(){ openEdit($(this).data('id')); });
  $('#booksTable').on('click', 'button[data-action="del"]',  function(){ delBook($(this).data('id')); });
  $('#booksPager').on('click', 'a.page-link', function(e){ e.preventDefault(); var p=parseInt($(this).data('page'),10); if(p) loadBooks(p); });
  $('#bookSearchForm').on('submit', function(e){ e.preventDefault(); BK.keyword=$('#bkKeyword').val().trim(); loadBooks(1); });
  var booksLoadedOnce=false;$('a[href="#tab-books"]').on('shown.bs.tab', function(){ if(!booksLoadedOnce){ booksLoadedOnce=true; loadBooks(1);} });

  /* Users (AJAX) */
  var U={api:'${ctx}/admin/api/users',page:1,size:10,role:'',blocked:'',keyword:'',csrf:(function(){var t=document.querySelector('meta[name="_csrf"]');var h=document.querySelector('meta[name="_csrf_header"]');return{token:t?t.content:null,header:h?h.content:null};})()};
  function uHeaders(){var h={'Content-Type':'application/json'};if(U.csrf.token&&U.csrf.header)h[U.csrf.header]=U.csrf.token;return h;}
  function badgeBlocked(b){ if(b==='Y'||b===true||b===1){return '<span class="chip chip-cancel">차단</span>';} return '<span class="chip chip-done">정상</span>'; }
  function loadUsers(p){if(!p)p=1;U.page=p;$.get(U.api,{page:U.page,size:U.size,role:U.role,blocked:U.blocked,keyword:U.keyword},function(res){renderUsers(res.items||[]);renderUPager(res.page||1,res.totalPage||1);}).fail(function(x){alert('유저 목록을 불러오지 못했습니다.');console.error(x.responseText);});}
  function renderUsers(items){
    var $tb=$('#usersTable tbody').empty();
    if(!items.length){$tb.append('<tr><td colspan="6" class="text-center mini">데이터 없음</td></tr>');return;}
    for(var i=0;i<items.length;i++){
      var u=items[i];
      var roleSel='<select id="role-'+u.userId+'" class="form-control form-control-sm d-inline-block" style="width:120px">'+
                  '<option value="ADMIN"'+(u.role==='ADMIN'?' selected':'')+'>ADMIN</option>'+
                  '<option value="CUSTOMER"'+(u.role!=='ADMIN'?' selected':'')+'>CUSTOMER</option>'+
                  '</select>';
      var row='<tr id="user-'+u.userId+'">'+
        '<td>#'+u.userId+'</td>'+
        '<td>'+escapeHtml(u.loginId||'')+'</td>'+
        '<td>'+escapeHtml(u.name||'')+'</td>'+
        '<td class="text-center">'+roleSel+'</td>'+
        '<td class="text-center">'+badgeBlocked(u.blocked)+'</td>'+
        '<td class="text-right">'+
          '<button class="btn btn-primary btn-sm btn-mini" onclick="applyRole('+u.userId+')">권한적용</button> '+
          ((u.blocked==='Y'||u.blocked===true||u.blocked===1)
            ? '<button class="btn btn-outline-secondary btn-sm btn-mini" onclick="unblockUser('+u.userId+')">차단해제</button> '
            : '<button class="btn btn-outline-warning btn-sm btn-mini" onclick="blockUser('+u.userId+')">차단</button> ')+
          '<button class="btn btn-outline-danger btn-sm btn-mini" onclick="deleteUser('+u.userId+')">삭제</button>'+
        '</td>'+
      '</tr>';
      $tb.append(row);
    }
  }
  function renderUPager(p,t){var $pg=$('#usersPager').empty();if(t<=1)return;for(var i=1;i<=t;i++){var a=(i===p)?'active':'';$pg.append('<li class="page-item '+a+'"><a class="page-link" href="#" onclick="loadUsers('+i+');return false;">'+i+'</a></li>');}}
  function applyRole(id){var v=$('#role-'+id).val();$.ajax({url:U.api+'/'+id+'/role',method:'PUT',headers:uHeaders(),data:JSON.stringify({role:v}),success:function(){loadUsers(U.page);},error:function(x){alert('권한 변경 실패');console.error(x.responseText);}});}
  function blockUser(id){if(!confirm('해당 사용자를 차단할까요?'))return;$.ajax({url:U.api+'/'+id+'/block',method:'PUT',headers:uHeaders(),data:JSON.stringify({blocked:true}),success:function(){loadUsers(U.page);},error:function(x){alert('차단 실패');console.error(x.responseText);}});}
  function unblockUser(id){$.ajax({url:U.api+'/'+id+'/block',method:'PUT',headers:uHeaders(),data:JSON.stringify({blocked:false}),success:function(){loadUsers(U.page);},error:function(x){alert('차단 해제 실패');console.error(x.responseText);}});}
  function deleteUser(id){if(!confirm('정말 삭제할까요?'))return;$.ajax({url:U.api+'/'+id,method:'DELETE',headers:uHeaders(),success:function(){loadUsers(U.page);},error:function(x){alert('삭제 실패');console.error(x.responseText);}});}
  $('#userSearchForm').on('submit', function(e){e.preventDefault();U.role=$('#userRoleFilter').val();U.blocked=$('#userBlockedFilter').val();U.keyword=$('#userKeyword').val().trim();loadUsers(1);});
  var usersLoadedOnce=false;$('a[href="#tab-users"]').on('shown.bs.tab', function(){ if(!usersLoadedOnce){ usersLoadedOnce=true; loadUsers(1);} });

  /* Reviews (AJAX) */
  var RV={api:'${ctx}/admin/api/reviews',page:1,size:10,rating:'',status:'',keyword:'',bookId:'',csrf:(function(){var t=document.querySelector('meta[name="_csrf"]');var h=document.querySelector('meta[name="_csrf_header"]');return{token:t?t.content:null,header:h?h.content:null};})()};
  function rvHeaders(){var h={'Content-Type':'application/json'};if(RV.csrf.token&&RV.csrf.header)h[RV.csrf.header]=RV.csrf.token;return h;}
  function loadReviews(p){
    if(!p)p=1; RV.page=p;
    var q={page:RV.page,size:RV.size};
    if(RV.rating) q.rating=RV.rating;
    if(RV.status) q.status=RV.status;
    if(RV.keyword) q.keyword=RV.keyword;
    if(RV.bookId) q.bookId=RV.bookId;
    $.get(RV.api, q, function(res){
      renderReviews(res.items||[]); renderRPager(res.page||1, res.totalPage||1);
    }).fail(function(x){ alert('리뷰 목록을 불러오지 못했습니다.'); console.error(x.responseText); });
  }
  function reviewStatusChip(s){ return (s==='HIDDEN')?'<span class="chip chip-hid">숨김</span>':'<span class="chip chip-vis">노출</span>'; }
  function renderReviews(items){
    var $tb=$('#reviewsTable tbody').empty();
    if(!items.length){ $tb.append('<tr><td colspan="8" class="text-center mini">데이터 없음</td></tr>'); return; }
    items.forEach(function(r){
      var bookText = r.bookTitle ? (escapeHtml(r.bookTitle)+' <span class="text-muted">#'+r.bookId+'</span>') : ('#'+r.bookId);
      var contentShort = escapeHtml(String(r.content||'')).slice(0,120); if((r.content||'').length>120) contentShort += '…';
      var row = '<tr id="rv-'+r.reviewId+'">'+
        '<td>#'+r.reviewId+'</td>'+
        '<td>'+bookText+'</td>'+
        '<td>'+(escapeHtml(r.userLoginId||('#'+r.userId)))+'</td>'+
        '<td class="text-center">'+stars(r.rating)+'</td>'+
        '<td><div class="text-truncate-2">'+contentShort+'</div></td>'+
        '<td>'+fmtDate(r.createdAt)+'</td>'+
        '<td class="text-center">'+reviewStatusChip(r.status||'VISIBLE')+'</td>'+
        '<td class="text-right">'+
          '<div class="btn-group btn-group-sm" role="group">'+
            '<button class="btn btn-soft" onclick="openReviewModal('+r.reviewId+', \''+escapeHtml(r.bookTitle||('#'+r.bookId))+'\', \''+escapeHtml(r.userLoginId||('#'+r.userId))+'\', '+(Number(r.rating)||0)+', \''+fmtDate(r.createdAt)+'\')">보기</button>'+
            ((r.status==='HIDDEN')?'<button class="btn btn-outline-success" onclick="setReviewStatus('+r.reviewId+',\'VISIBLE\')">노출</button>':'<button class="btn btn-outline-warning" onclick="setReviewStatus('+r.reviewId+',\'HIDDEN\')">숨김</button>')+
            '<button class="btn btn-outline-danger" onclick="deleteReview('+r.reviewId+')">삭제</button>'+
          '</div>'+
        '</td>'+
      '</tr>';
      $tb.append(row);
    });
  }
  function renderRPager(p,t){var $pg=$('#reviewsPager').empty();if(t<=1)return;for(var i=1;i<=t;i++){var a=(i===p)?'active':'';$pg.append('<li class="page-item '+a+'"><a class="page-link" href="#" onclick="loadReviews('+i+');return false;">'+i+'</a></li>');}}
  function setReviewStatus(id, st){ $.ajax({url: RV.api + '/' + id + '/status', method:'PUT', headers: rvHeaders(), data: JSON.stringify({status: st}), success:function(){ loadReviews(RV.page); }, error:function(x){ alert('상태 변경 실패'); console.error(x.responseText); }}); }
  function deleteReview(id){ if(!confirm('해당 리뷰를 삭제할까요?')) return; $.ajax({url: RV.api + '/' + id, method:'DELETE', headers: rvHeaders(), success:function(){ loadReviews(RV.page); }, error:function(x){ alert('삭제 실패'); console.error(x.responseText); }}); }
  function openReviewModal(id, bookText, userText, rating, created){
    $.get(RV.api + '/' + id, function(r){
      var content = (r && r.content!=null) ? String(r.content) : '';
      $('#rvmBook').text(r.bookTitle ? (r.bookTitle + ' #' + r.bookId) : ('#'+(r.bookId||'')));
      $('#rvmUser').text(r.userLoginId ? r.userLoginId : ('#'+(r.userId||'')));
      $('#rvmRating').text(stars(r.rating));
      $('#rvmDate').text(fmtDate(r.createdAt));
      $('#rvmContent').text(content);
      $('#rvModal').modal('show');
    }).fail(function(){
      $('#rvmBook').text(bookText); $('#rvmUser').text(userText); $('#rvmRating').text(stars(rating));
      $('#rvmDate').text(created); $('#rvmContent').text('상세 API가 비활성화되어 목록 요약만 표시합니다.'); $('#rvModal').modal('show');
    });
  }
  $('#rvSearchForm').on('submit', function(e){
    e.preventDefault();
    RV.rating = $('#rvRating').val(); RV.status = $('#rvStatus').val();
    RV.bookId = $('#rvBookId').val().trim(); RV.keyword = $('#rvKeyword').val().trim();
    loadReviews(1);
  });
  var reviewsLoadedOnce=false; $('a[href="#tab-reviews"]').on('shown.bs.tab', function(){ if(!reviewsLoadedOnce){ reviewsLoadedOnce=true; loadReviews(1);} });
</script>
</body>
</html>