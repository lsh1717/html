<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>결제 완료</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<div class="container my-5 d-flex justify-content-center">
    <div class="card shadow-lg p-4 text-center" style="max-width: 500px;">
        <div class="card-body">
            <h2 class="mb-3 text-success">
                <i class="bi bi-check-circle-fill"></i> 결제가 완료되었습니다!
            </h2>
            <p class="mb-4">주문이 정상적으로 처리되었습니다.</p>
            <a href="/user/bookList" class="btn btn-primary btn-lg">홈으로 돌아가기</a>
        </div>
    </div>
</div>

<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>