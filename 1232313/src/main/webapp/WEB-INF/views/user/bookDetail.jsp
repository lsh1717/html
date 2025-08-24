<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"/>

<style>
  .star { color:#fbbf24; } /* amber-400 */
  .star.off { color:#334155; } /* slate-700 (다크에서도 어울림) */
  .hist .bar { height: 8px; border-radius: 999px; background:#374151; }
  .hist .bar > span { display:block; height:100%; border-radius:999px; background:#60a5fa; }
  .review-item{ border-bottom:1px solid rgba(148,163,184,.3); padding:14px 0; }
  .review-header{ display:flex; align-items:center; justify-content:space-between; }
  .muted{ color:#6b7280; font-size:.9rem; }
  .cover { max-height:320px; object-fit:cover; border-radius:12px; }
  .badge-stock { background:#ef4444; }
</style>

<div class="container my-5">

  <!-- flash message -->
  <c:if test="${not empty msg}">
    <div class="alert alert-info">${msg}</div>
  </c:if>

  <!-- 책 정보 -->
  <div class="row">
    <div class="col-md-4 text-center mb-3">
      <img src="${book.cover_image}" alt="표지" class="img-fluid cover border">
    </div>
    <div class="col-md-8">
      <h3 class="mb-2">${book.title}</h3>
      <p class="mb-1"><strong>저자</strong> : ${book.author}</p>
      <p class="mb-1"><strong>가격</strong> :
        <fmt:formatNumber value="${book.price}" pattern="#,###"/>원
      </p>
      <p class="mb-3"><strong>재고</strong> :
        <span class="badge badge-stock">${book.stock}</span> 권
      </p>

      <c:if test="${not empty book.description}">
        <div class="p-3 border rounded bg-light mb-4" style="white-space:pre-line;">
          ${book.description}
        </div>
      </c:if>

      <form action="${ctx}/cart/add" method="post" class="form-inline">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="bookId" value="${book.bookId}"/>
        <label for="quantity" class="mr-2">수량</label>
        <input type="number" id="quantity" name="quantity" class="form-control mr-2"
               value="1" min="1" max="${book.stock}" required style="width:120px;">
        <button type="submit" class="btn btn-primary">장바구니 담기</button>
        <a href="${ctx}/user/bookList" class="btn btn-secondary ml-2">목록으로</a>
      </form>
    </div>
  </div>

  <!-- 리뷰 요약 -->
  <div class="row mt-5">
    <div class="col-md-4">
      <div class="card">
        <div class="card-header">리뷰 요약</div>
        <div class="card-body">
          <div class="d-flex align-items-end mb-2">
            <div style="font-size:2.2rem;font-weight:700;">
              <fmt:formatNumber value="${avgRating}" minFractionDigits="1" maxFractionDigits="1"/>
            </div>
            <div class="ml-2">
              <c:forEach var="i" begin="1" end="5">
                <span class="star ${i <= avgRating ? '' : 'off'}">★</span>
              </c:forEach>
            </div>
          </div>
          <div class="muted">총 <strong>${totalReviews}</strong>개 리뷰</div>

          <!-- 평점 히스토그램 -->
          <!-- ★별점 히스토그램 (5→1을 1→5로 돌려서 계산) -->
<div class="hist mt-3">
  <c:forEach var="s" begin="1" end="5" step="1">
    <c:set var="star" value="${6 - s}"/>            <!-- 5,4,3,2,1 -->
    <c:set var="idx"  value="${star - 1}"/>         <!-- 4..0 -->
    <c:set var="cnt"  value="${ratingCounts[idx]}"/>
    <c:set var="pct"  value="${totalReviews > 0 ? (cnt * 100 / totalReviews) : 0}"/>

    <div class="d-flex align-items-center mb-1">
      <div style="width:40px" class="text-right">${star}★</div>
      <div class="progress flex-grow-1 mx-2" style="height:8px;background:#1f2937">
        <div class="progress-bar" role="progressbar"
             style="width:${pct}%; background:#60a5fa"></div>
      </div>
      <div style="width:40px" class="text-right">${cnt}</div>
    </div>
  </c:forEach>
</div>
        </div>
      </div>
    </div>

    <!-- 리뷰 목록 -->
    <div class="col-md-8">
      <div class="card">
        <div class="card-header">리뷰</div>
        <div class="card-body">

          <!-- 작성 폼 (구매자 & 미작성자만 표시) -->
          <c:if test="${canReview}">
            <div class="border rounded p-3 mb-4">
              <form action="${ctx}/reviews" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <input type="hidden" name="bookId" value="${book.bookId}">
                <div class="form-row">
                  <div class="form-group col-md-3">
                    <label>평점</label>
                    <select name="rating" class="form-control" required>
                      <option value="5">★★★★★ (5)</option>
                      <option value="4">★★★★☆ (4)</option>
                      <option value="3">★★★☆☆ (3)</option>
                      <option value="2">★★☆☆☆ (2)</option>
                      <option value="1">★☆☆☆☆ (1)</option>
                    </select>
                  </div>
                  <div class="form-group col-md-9">
                    <label>내용</label>
                    <textarea name="content" class="form-control" rows="3" maxlength="1000" required></textarea>
                  </div>
                </div>
                <button class="btn btn-primary">리뷰 등록</button>
              </form>
            </div>
          </c:if>

          <!-- 비-작성 가능 안내(옵션) -->
          <c:if test="${!canReview}">
            <div class="alert alert-secondary small">
              리뷰 작성은 <strong>해당 도서를 구매한 이용자</strong>만 가능합니다.
            </div>
          </c:if>

          <!-- 리스트 -->
          <c:if test="${empty reviews}">
            <div class="text-center text-muted py-4">아직 등록된 리뷰가 없습니다.</div>
          </c:if>

          <c:forEach var="r" items="${reviews}">
            <div class="review-item">
              <div class="review-header">
                <div>
                  <c:forEach var="i" begin="1" end="5">
                    <span class="star ${i <= r.rating ? '' : 'off'}">★</span>
                  </c:forEach>
                  <span class="ml-2 muted">by #${r.userId}</span>
                </div>
                <div class="muted">
                  <c:out value="${r.createdAt}"/>
                </div>
              </div>
              <div class="mt-2" style="white-space:pre-line;"><c:out value="${r.content}"/></div>

              <!-- 본인 리뷰 삭제(옵션) -->
              <c:if test="${not empty r.reviewId && loginUserId == r.userId}">
                <form action="${ctx}/reviews/${r.reviewId}/delete" method="post" class="mt-2">
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                  <input type="hidden" name="bookId" value="${book.bookId}">
                  <button class="btn btn-sm btn-outline-danger">삭제</button>
                </form>
              </c:if>
            </div>
          </c:forEach>

          <!-- 페이지네이션 -->
          <c:if test="${rtotalPage > 1}">
            <nav class="mt-3">
              <ul class="pagination pagination-sm justify-content-center mb-0">
                <c:forEach var="p" begin="1" end="${rtotalPage}">
                  <li class="page-item ${p==rpage?'active':''}">
                    <a class="page-link"
                       href="${ctx}/user/bookDetail/${book.bookId}?rpage=${p}#reviews">${p}</a>
                  </li>
                </c:forEach>
              </ul>
            </nav>
          </c:if>

        </div>
      </div>
    </div>
  </div>

  <a id="reviews"></a>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>