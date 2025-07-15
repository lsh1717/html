<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>도서 등록</title>
</head>
<body>
    <h2>도서 등록 / 수정</h2>
    <form method="post" action="${pageContext.request.contextPath}/admin/${empty book.bookId ? 'add' : 'update'}">
        <input type="hidden" name="bookId" value="${book.bookId}"/>

        <label>제목: </label>
        <input type="text" name="title" value="${book.title}" required /><br/>

        <label>저자: </label>
        <input type="text" name="author" value="${book.author}" required /><br/>

        <label>가격: </label>
        <input type="number" name="price" value="${book.price}" required /><br/>

        <button type="submit">저장</button>
    </form>
</body>
</html>