<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="container-fluid">
    <h2 class="mb-4">관리자 대시보드</h2>

    <!-- 요약 카드 -->
    <div class="row">
        <div class="col-md-3 mb-3">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h6 class="text-muted">총 회원 수</h6>
                    <h4>${totalMembers}</h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h6 class="text-muted">총 도서 수</h6>
                    <h4>${totalBooks}</h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h6 class="text-muted">오늘 주문 건수</h6>
                    <h4>${todayOrders}</h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h6 class="text-muted">오늘 매출</h6>
                    <h4>₩${todaySales}</h4>
                </div>
            </div>
        </div>
    </div>

    <!-- 매출 차트 -->
    <div class="card shadow-sm mt-4">
        <div class="card-body">
            <h6 class="mb-3">최근 매출 추이</h6>
            <canvas id="salesChart" height="100"></canvas>
        </div>
    </div>
</div>

<script>
  const ctx = document.getElementById('salesChart').getContext('2d');
  new Chart(ctx, {
    type: 'line',
    data: {
      labels: ['월','화','수','목','금','토','일'],
      datasets: [{
        label: '매출',
        data: [120000, 140000, 160000, 180000, 150000, 170000, 125000],
        borderColor: '#4A6CF7',
        backgroundColor: 'rgba(74,108,247,0.1)',
        fill: true,
        tension: 0.3
      }]
    },
    options: {
      responsive: true,
      plugins: { legend: { display: false } }
    }
  });
</script>