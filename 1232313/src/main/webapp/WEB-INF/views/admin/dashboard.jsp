<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>BookShop Admin</title>

  <c:if test="${not empty _csrf}">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
  </c:if>

  <link href="https://fonts.googleapis.com/css2?family=Pretendard:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

  <style>
    /* ===== Theme vars ===== */
    :root{
      /* light (fallback) */
      --bg:#f5f7fb; --card:#ffffff; --text:#0f172a; --muted:#64748b;
      --primary:#60a5fa; --ring:0 2px 10px rgba(0,0,0,.06);
      --chip:#eef2ff; --chip-text:#334155; --table-head:#f8fafc; --border:rgba(15,23,42,.08);
      --grid: rgba(100,116,139,.25);
    }
    body.dark{
      --bg:#0b1220;         /* 배경 */
      --card:#0f172a;       /* 카드 */
      --text:#e5e7eb;       /* 본문 텍스트 */
      --muted:#94a3b8;      /* 보조 텍스트 */
      --primary:#60a5fa;    /* 메인 컬러 */
      --ring:0 2px 16px rgba(0,0,0,.35);
      --chip:#17213a;       /* 소프트 버튼 배경 */
      --chip-text:#cbd5e1;
      --table-head:#111827;
      --border:rgba(148,163,184,.15);
      --grid: rgba(148,163,184,.25);
    }

    /* ===== Base ===== */
    html,body{height:100%}
    body{background:var(--bg); color:var(--text); font-family:'Pretendard','Noto Sans KR',sans-serif;}
    a{color:var(--text)}
    .navbar{background:var(--card)!important; box-shadow:var(--ring)}
    .brand{font-weight:700}
    .brand i{color:var(--primary)}

    /* tabs */
    .nav-pills .nav-link{
      color:var(--muted);
      background:transparent;
      border-radius:10px;
      margin-right:.5rem;
    }
    .nav-pills .nav-link.active{
      background:rgba(96,165,250,.15);
      color:var(--text);
      font-weight:700;
      border:1px solid var(--border);
    }

    /* cards & KPI */
    .card, .kpi{background:var(--card); border:none; border-radius:14px; box-shadow:var(--ring)}
    .kpi .label{font-size:.85rem; color:var(--muted)}
    .kpi .value{font-size:1.4rem; font-weight:700}

    /* table */
    .table{color:var(--text)}
    .table thead th{background:var(--table-head); border-top:none; color:var(--muted); border-bottom:1px solid var(--border)}
    .table td, .table th{border-color:var(--border)}
    .table tbody tr:hover{background:rgba(96,165,250,.06)}

    /* buttons */
    .btn-soft{background:var(--chip); color:var(--chip-text); border:none}
    .btn-soft:hover{background:rgba(96,165,250,.18)}
    .btn-outline-secondary{border-color:var(--border); color:var(--muted)}
    .btn-outline-secondary:hover{background:rgba(96,165,250,.15); color:var(--text)}

    /* misc */
    .container-xl{max-width:1320px}
    .mini{font-size:.9rem; color:var(--muted)}
    .cover{width:42px;height:56px;object-fit:cover;border-radius:6px;box-shadow:0 1px 4px rgba(0,0,0,.25)}
    footer{color:var(--muted)}
    .badge-danger{background:#ef4444}
  </style>
</head>

<body class="dark"><!-- 기본: 다크모드 -->

<!-- Topbar -->
<nav class="navbar navbar-expand navbar-dark">
  <div class="container-xl d-flex align-items-center justify-content-between">
    <div class="brand">
      <i class="bi bi-bar-chart-fill mr-2"></i>BookShop Admin
    </div>

    <div class="d-flex align-items-center">
      <a class="btn btn-soft btn-sm mr-2" href="${ctx}/admin/bookManage">
        <i class="bi bi-plus-lg mr-1"></i>도서 추가
      </a>

      <!-- theme toggle -->
      <button id="themeBtn" type="button" class="btn btn-soft btn-sm mr-2">
        <i class="bi bi-moon-stars mr-1"></i><span>다크</span>
      </button>

      <!-- POST /logout -->
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
  <!-- Tabs -->
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
      <!-- KPIs -->
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

        <!-- category pie -->
        <div class="col-lg-4 mb-3">
          <div class="card p-3">
            <h6 class="mb-3">카테고리 분포</h6>
            <canvas id="categoryChart" height="160"></canvas>
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

      <div class="card p-3 mt-2">
        <div class="d-flex flex-wrap">
          <a class="btn btn-soft mr-2 mb-2" href="${ctx}/admin/bookManage"><i class="bi bi-journal-plus mr-1"></i>도서 등록</a>
          <a class="btn btn-soft mr-2 mb-2" href="${ctx}/admin/orders"><i class="bi bi-receipt mr-1"></i>주문 목록</a>
          <a class="btn btn-soft mr-2 mb-2" href="${ctx}/admin/users"><i class="bi bi-people mr-1"></i>회원 관리</a>
        </div>
      </div>
    </div>

    <!-- other tabs (link only) -->
    <div class="tab-pane fade" id="tab-books"><div class="card p-4 text-center mini">도서 관리는 <a href="${ctx}/admin/bookManage">여기</a>에서 진행하세요.</div></div>
    <div class="tab-pane fade" id="tab-orders"><div class="card p-4 text-center mini">주문 관리는 <a href="${ctx}/admin/orders">여기</a>에서 진행하세요.</div></div>
    <div class="tab-pane fade" id="tab-users"><div class="card p-4 text-center mini">회원 관리는 <a href="${ctx}/admin/users">여기</a>에서 진행하세요.</div></div>
    <div class="tab-pane fade" id="tab-reviews"><div class="card p-4 text-center mini">리뷰 관리는 <a href="${ctx}/admin/reviews">여기</a>에서 진행하세요.</div></div>

  </div>

  <footer class="text-center mt-4">&copy; 2025 BookShop. All rights reserved.</footer>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.9.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>

<script>
  // ===== Theme toggle (persist) =====
  (function(){
    const saved = localStorage.getItem('theme') || 'dark';
    if(saved === 'dark') document.body.classList.add('dark'); else document.body.classList.remove('dark');
    document.querySelector('#themeBtn span').textContent = document.body.classList.contains('dark') ? '다크' : '라이트';
    document.getElementById('themeBtn').addEventListener('click', ()=>{
      document.body.classList.toggle('dark');
      localStorage.setItem('theme', document.body.classList.contains('dark') ? 'dark' : 'light');
      document.querySelector('#themeBtn span').textContent = document.body.classList.contains('dark') ? '다크' : '라이트';
      // 차트 색도 즉시 반영
      recolorCharts();
    });
  })();

  // ===== Data (server -> fallback) =====
  const labelSales  = ${revenueLabels != null ? revenueLabels : '["3월","4월","5월","6월","7월","8월"]'};
  const dataSales   = ${revenueData   != null ? revenueData   : '[120000,95000,110000,150000,170000,140000]'};
  const categoryLbl = ${categoryLabels != null ? categoryLabels : '["기술","소설","자기계발","에세이"]'};
  const categoryDat = ${categoryData   != null ? categoryData   : '[3,1,1,1]'};
  const statusLbl   = ${statusLabels   != null ? statusLabels   : '["PAID","SHIPPED","PENDING","CANCELLED"]'};
  const statusDat   = ${statusData     != null ? statusData     : '[2,1,1,1]'};

  // ===== Chart helpers to read CSS vars =====
  function cssVar(name){ return getComputedStyle(document.body).getPropertyValue(name).trim(); }
  function gridColor(){ return cssVar('--grid'); }
  function textColor(){ return cssVar('--muted'); }
  function primary(){ return cssVar('--primary'); }

  // charts
  let salesChart, catChart, statChart;
  function buildCharts(){
    Chart.defaults.color = textColor();
    Chart.defaults.borderColor = gridColor();

    // gradient fill for line chart
    const ctx = document.getElementById('salesChart').getContext('2d');
    const grad = ctx.createLinearGradient(0,0,0,220);
    grad.addColorStop(0, primary()+'66'); // 40% alpha
    grad.addColorStop(1, primary()+'00');

    salesChart = new Chart(ctx, {
      type:'line',
      data:{labels:labelSales, datasets:[{data:dataSales, fill:true, tension:.35, borderWidth:2, borderColor:primary(), backgroundColor:grad, pointRadius:2}]},
      options:{plugins:{legend:{display:false}}, scales:{x:{grid:{color:gridColor()}}, y:{grid:{color:gridColor()}, ticks:{callback:v=>v.toLocaleString()}}}}
    });

    catChart = new Chart(document.getElementById('categoryChart'), {
      type:'doughnut',
      data:{labels:categoryLbl, datasets:[{data:categoryDat}]},
      options:{plugins:{legend:{position:'bottom', labels:{color:textColor()}}}}
    });

    statChart = new Chart(document.getElementById('statusChart'), {
      type:'bar',
      data:{labels:statusLbl, datasets:[{data:statusDat}]},
      options:{plugins:{legend:{display:false}}, scales:{x:{grid:{color:gridColor()}}, y:{beginAtZero:true, grid:{color:gridColor()}, ticks:{stepSize:1}}}}
    });
  }
  function recolorCharts(){
    // 업데이트만으로는 legend/grid 컬러 반영이 아쉬워 재생성
    salesChart.destroy(); catChart.destroy(); statChart.destroy();
    buildCharts();
  }
  buildCharts();
</script>
</body>
</html>