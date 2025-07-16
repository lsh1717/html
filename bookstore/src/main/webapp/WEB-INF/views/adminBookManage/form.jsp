<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 부트스트랩 CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

<style>
  body {
    background-color: #f8f9fa;
    font-family: 'Noto Sans KR', Arial, sans-serif;
  }
  .container {
    max-width: 600px;
    margin-top: 40px;
    margin-bottom: 40px;
    background: white;
    padding: 30px;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.07);
  }
  h2 {
    font-weight: 700;
    margin-bottom: 30px;
    color: #222;
  }
  label {
    font-weight: 600;
    margin-top: 15px;
  }
  input.form-control, textarea.form-control {
    border-radius: 8px;
    font-size: 1rem;
  }
  button.btn-primary {
    margin-top: 30px;
    padding: 10px 24px;
    border-radius: 30px;
    font-weight: 700;
  }
  a.btn-secondary {
    margin-top: 30px;
    margin-left: 15px;
    padding: 10px 24px;
    border-radius: 30px;
    font-weight: 700;
  }
</style>

<div class="container">
  <h2>${book.bookId != null ? "도서 수정" : "도서 등록"}</h2>

  <form action="${ctx}/admin/bookManage/${book.bookId != null ? 'update' : 'insert'}" method="post">
    <input type="hidden" name="bookId" value="${book.bookId}" />

    <label for="title">제목</label>
    <input type="text" id="title" name="title" class="form-control" value="${book.title}" required />

    <label for="author">저자</label>
    <input type="text" id="author" name="author" class="form-control" value="${book.author}" required />

    <label for="price">가격</label>
    <input type="number" id="price" name="price" class="form-control" value="${book.price}" required />

    <label for="stock">재고</label>
    <input type="number" id="stock" name="stock" class="form-control" value="${book.stock}" required />

    <label for="description">설명</label>
    <textarea id="description" name="description" class="form-control" rows="4">${book.description}</textarea>

    <label for="coverImage">커버 이미지 URL</label>
    <input type="text" id="coverImage" name="coverImage" class="form-control" value="${book.coverImage}" />

    <button type="submit" class="btn btn-primary">
      ${book.bookId != null ? "수정" : "등록"}
    </button>

    <a href="${ctx}/admin/bookManage/list" class="btn btn-secondary">목록으로 돌아가기</a>
  </form>
</div>

<!-- 부트스트랩 JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.9.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>