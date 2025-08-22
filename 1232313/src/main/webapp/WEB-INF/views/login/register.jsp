<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <title>회원가입 · BookShop</title>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>

  <c:if test="${not empty _csrf}">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
  </c:if>

  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700;800&display=swap" rel="stylesheet">

  <style>
    :root{
      --bg:#f6f8fc;
      --page:#ffffff;
      --ink:#152238;
      --muted:#6b7280;
      --line:#e7edf5;
      --edge1:#f6f8fc;
      --edge2:#e9eef6;
      --primary:#344055;
      --primary-600:#2e3b54;
      --radius:18px;
      --ring:0 24px 48px rgba(23,36,58,.08);
    }
    *{box-sizing:border-box}
    html,body{height:100%}
    body{
      margin:0; background:radial-gradient(900px 500px at 100% -150px,#fff 0,#eef3ff 35%,rgba(238,243,255,0) 60%),var(--bg);
      color:var(--ink);
      font-family:'Inter',system-ui,-apple-system,Segoe UI,Roboto,'Apple SD Gothic Neo','Noto Sans KR',sans-serif;
    }
    .wrap{min-height:100%; display:grid; place-items:center; padding:56px 16px}

    /* ===== Book card (same as 로그인) ===== */
    .book{
      width:100%; max-width:560px;
      background:var(--page);
      border:1px solid var(--line);
      border-radius:var(--radius);
      box-shadow:var(--ring);
      padding:28px 26px 22px;
      position:relative; isolation:isolate;
    }
    .book::before{ /* spine */
      content:""; position:absolute; inset:0 auto 0 0; width:14px;
      background:linear-gradient(180deg,#f3f7ff,#eaf1ff 60%,#f6f9ff);
      border-top-left-radius:var(--radius); border-bottom-left-radius:var(--radius);
      box-shadow:inset -1px 0 0 rgba(0,0,0,.06), inset 1px 0 0 rgba(255,255,255,.7);
      z-index:-1;
    }
    .book::after{ /* page edge */
      content:""; position:absolute; top:6px; bottom:6px; right:-10px; width:10px;
      background:repeating-linear-gradient(0deg,var(--edge1) 0 2px,var(--edge2) 2px 4px);
      border-top-right-radius:14px; border-bottom-right-radius:14px;
      box-shadow:0 6px 14px rgba(20,35,60,.06);
    }
    .bookmark{
      position:absolute; top:-8px; right:44px; width:14px; height:22px;
      background:linear-gradient(180deg,#f6f9ff,#dfe8ff);
      border:1px solid var(--line); border-bottom:none;
      clip-path:polygon(0 0,100% 0,100% 70%,50% 100%,0 70%);
      border-top-left-radius:4px; border-top-right-radius:4px;
      box-shadow:0 3px 0 #d0dbf3;
    }

    .brand{display:flex; align-items:center; gap:10px; margin-bottom:6px; font-weight:800; font-size:22px}
    .brand-badge{
      width:32px; height:32px; border-radius:8px; display:grid; place-items:center;
      background:linear-gradient(180deg,#f5f8ff,#eef3ff); border:1px solid var(--line);
    }
    .desc{color:var(--muted); font-size:14px; margin-bottom:14px}

    .alert{border:1px solid var(--line); border-radius:12px; padding:10px 12px; font-size:14px; display:flex; gap:8px; align-items:center; margin-bottom:14px}
    .alert.error{background:#fdecec}
    .icon{width:18px; height:18px; color:#7d8aa6}

    .grid{display:grid; grid-template-columns:1fr; gap:10px}
    @media (min-width:560px){
      .grid-2{display:grid; grid-template-columns:1fr 1fr; gap:10px}
    }

    .field{position:relative}
    .input{
      width:100%; height:48px; border-radius:12px; border:1px solid var(--line);
      background:#fbfcff; padding:0 14px 0 42px; color:var(--ink); outline:none;
      transition:border-color .15s ease, background .15s ease;
      box-shadow:0 1px 0 rgba(0,0,0,.02) inset; font-size:15px;
    }
    .input:focus{border-color:#cfd8ea; background:#fff}
    .icon-left{position:absolute; left:14px; top:50%; transform:translateY(-50%); width:18px; height:18px; color:#97a3bb}
    .eye{
      position:absolute; right:12px; top:50%; transform:translateY(-50%);
      width:22px; height:22px; background:#eef2f7; border:1px solid var(--line); border-radius:999px;
      display:grid; place-items:center; color:#7d8aa6; cursor:pointer;
    }

    .btn{
      width:100%; height:48px; margin-top:14px; border:none; border-radius:12px;
      background:linear-gradient(180deg,var(--primary),var(--primary-600));
      color:#fff; font-weight:800; letter-spacing:.2px; font-size:15px;
      box-shadow:0 10px 28px rgba(46,59,84,.18); cursor:pointer; transition:filter .15s ease, transform .06s ease;
    }
    .btn:hover{filter:brightness(1.03)}
    .btn:active{transform:translateY(1px)}

    .foot{margin-top:16px; display:flex; gap:16px; align-items:center; justify-content:space-between}
    .link{color:#516188; text-decoration:none; font-weight:600}
    .link:hover{text-decoration:underline}
    .copy{color:var(--muted); font-size:12px}
  </style>
</head>
<body>
  <div class="wrap">
    <div class="book" aria-label="회원가입 패널(책 모양)">
      <span class="bookmark" aria-hidden="true"></span>

      <div class="brand">
        <div class="brand-badge" aria-hidden="true">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <path d="M6 3h9a2 2 0 0 1 2 2v14a1 1 0 0 1-1 1H6.8A2.8 2.8 0 0 1 4 17.2V6a3 3 0 0 1 2-3Z" stroke="#94a3b8" stroke-width="1.2"/>
            <path d="M7 6h8M7 9h8" stroke="#94a3b8" stroke-width="1.2" stroke-linecap="round"/>
          </svg>
        </div>
        BookShop
      </div>
      <p class="desc">계정을 생성하고 더 빠르게 주문·서재를 관리하세요.</p>

      <c:if test="${not empty param.error}">
        <div class="alert error">
          <svg class="icon" viewBox="0 0 24 24" fill="none"><path d="M12 22a10 10 0 1 1 0-20 10 10 0 0 1 0 20Z" stroke="#ef4444" stroke-width="1.4"/><path d="M12 7.5v6.2M12 17.5v.2" stroke="#ef4444" stroke-width="1.6" stroke-linecap="round"/></svg>
          <span>입력값을 확인해주세요.</span>
        </div>
      </c:if>

      <!-- ===== Register Form ===== -->
      <form action="<c:url value='/login/register'/>" method="post" autocomplete="on" novalidate>
        <div class="grid">
          <!-- 아이디 -->
          <div class="field">
            <input class="input" id="login_id" name="loginId" type="text" placeholder="아이디" required autocomplete="username">
            <svg class="icon-left" viewBox="0 0 24 24" fill="none"><path d="M4 6h16v12H4z" stroke="#97a3bb" stroke-width="1.4"/><path d="M4 7l8 6 8-6" stroke="#97a3bb" stroke-width="1.4" stroke-linecap="round"/></svg>
          </div>

          <!-- 비밀번호 -->
          <div class="field">
            <input class="input" id="password" name="password" type="password" placeholder="비밀번호" required autocomplete="new-password">
            <svg class="icon-left" viewBox="0 0 24 24" fill="none"><path d="M2 12s3.5-6 10-6 10 6 10 6-3.5 6-10 6S2 12 2 12Z" stroke="#97a3bb" stroke-width="1.4"/><circle cx="12" cy="12" r="3" stroke="#97a3bb" stroke-width="1.4"/></svg>
            <button class="eye" type="button" aria-label="비밀번호 보기" id="eye">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none"><path d="M3 3l18 18" stroke="#7d8aa6" stroke-width="1.6" stroke-linecap="round"/><path d="M21 12s-3.2-6-9-6c-1.1 0-2.2.2-3.2.6M3 12s1.4 2.6 4 4.2c1.2.7 2.7 1.2 4.5 1.2 5.8 0 9-6 9-6" stroke="#7d8aa6" stroke-width="1.4" stroke-linecap="round"/></svg>
            </button>
          </div>

          <!-- 이름 / 이메일 (2열) -->
          <div class="grid-2">
            <div class="field">
              <input class="input" id="name" name="name" type="text" placeholder="이름" required autocomplete="name">
              <svg class="icon-left" viewBox="0 0 24 24" fill="none"><circle cx="12" cy="8" r="3.2" stroke="#97a3bb" stroke-width="1.4"/><path d="M5 19a7 7 0 0 1 14 0" stroke="#97a3bb" stroke-width="1.4" stroke-linecap="round"/></svg>
            </div>
            <div class="field">
              <input class="input" id="email" name="email" type="email" placeholder="이메일" required autocomplete="email">
              <svg class="icon-left" viewBox="0 0 24 24" fill="none"><path d="M4 6h16v12H4z" stroke="#97a3bb" stroke-width="1.4"/><path d="M4 7l8 6 8-6" stroke="#97a3bb" stroke-width="1.4" stroke-linecap="round"/></svg>
            </div>
          </div>

          <!-- 휴대전화 -->
          <div class="field">
            <input class="input" id="hp" name="hp" type="text" placeholder="전화번호" autocomplete="tel">
            <svg class="icon-left" viewBox="0 0 24 24" fill="none"><rect x="6" y="3" width="12" height="18" rx="2" stroke="#97a3bb" stroke-width="1.4"/><circle cx="12" cy="17" r="1" fill="#97a3bb"/></svg>
          </div>
        </div>

        <button class="btn" type="submit">회원가입</button>

        <div class="foot">
          <a class="link" href="<c:url value='/login/login'/>">이미 계정이 있으신가요? 로그인</a>
          <span class="copy">© 2025 BookShop</span>
        </div>

        <input type="hidden" name="role" value="CUSTOMER"/>
        <c:if test="${not empty _csrf}">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </c:if>
      </form>
    </div>
  </div>

  <script>
    // 비밀번호 보기/숨기기
    (function(){
      var eye=document.getElementById('eye');
      var pw=document.getElementById('password');
      eye.addEventListener('click',function(){ pw.type = (pw.type==='password'?'text':'password'); });
    })();
  </script>
</body>
</html>