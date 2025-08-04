<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<!-- ✅ Bootstrap CSS 추가 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
	<div class="container mt-5">
		<div class="row justify-content-center">
			<div class="col-md-6">
				<div class="card shadow">
					<div class="card-header text-center bg-primary text-white">
						<h3 class="mb-0">회원가입</h3>
					</div>
					<div class="card-body">
						<form action="/login/register" method="post">
							<div class="mb-3">
								<label for="username" class="form-label">아이디</label>
								<input type="text" class="form-control" id="login_id" name="loginId" placeholder="아이디를 입력하세요">
							</div>
							<div class="mb-3">
								<label for="password" class="form-label">비밀번호</label>
								<input type="password" class="form-control" id="password" name="password" placeholder="비밀번호를 입력하세요">
							</div>
							<div class="mb-3">
								<label for="name" class="form-label">이름</label>
								<input type="text" class="form-control" id="name" name="name" placeholder="이름을 입력하세요">
							</div>
							<div class="mb-3">
								<label for="email" class="form-label">이메일</label>
								<input type="email" class="form-control" id="email" name="email" placeholder="이메일을 입력하세요">
							</div>
							<div class="mb-3">
								<label for="hp" class="form-label">전화번호</label>
								<input type="text" class="form-control" id="hp" name="hp" placeholder="전화번호를 입력하세요">
							</div>
							
							<div class="d-grid">
								<button type="submit" class="btn btn-primary">회원가입</button>
							</div>
							
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token }" />
							<input type="hidden" name="role" value="CUSTOMER" />
						</form>
					</div>
					<div class="card-footer text-center">
						<a href="/login" class="btn btn-link">이미 계정이 있으신가요? 로그인</a>
					</div>
				</div>
			</div>
		</div>
	</div>

<!-- ✅ Bootstrap JS 추가 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
