<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 구글 폰트 (Noto Sans KR) -->
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR:400,700&display=swap" rel="stylesheet">
<!-- 부트스트랩 CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

<style>
/* ... 기존 스타일 생략 (네가 올린 스타일 그대로 사용) ... */
</style>

<div class="container py-4">
  <h2>${pageTitle}</h2>

  <!-- 검색 폼 -->
  <form action="${ctx}/bookList" method="get" class="mb-3">
    <div class="input-group">
      <input type="text" name="keyword" value="${keyword}" class="form-control"
             placeholder="제목 검색" />
      <div class="input-group-append">
        <button type="submit" class="btn btn-primary">검색</button>
      </div>
    </div>
  </form>

  <div class="row">
    <c:forEach var="book" items="${books}">
      <div class="col-md-3 mb-4 d-flex">
        <div class="card w-100">
          <img src="${book.coverImage}" class="card-img-top" alt="Cover">
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">
              <a href="${ctx}/bookDetail/${book.bookId}">
                ${book.title}
              </a>
            </h5>
            <p class="card-text mb-1">저자: ${book.author}</p>
            <p class="card-text mb-3"><strong>${book.price}원</strong></p>

            <!-- 관리자 기능 -->
            <div class="mt-auto text-center">
              <a href="${ctx}/admin/update?bookId=${book.bookId}" class="btn btn-sm btn-warning">수정</a>
              <a href="${ctx}/admin/delete?bookId=${book.bookId}"
                 class="btn btn-sm btn-danger"
                 onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
            </div>
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

<!-- 페이지네이션 -->
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

<!-- 부트스트랩 JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.9.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>