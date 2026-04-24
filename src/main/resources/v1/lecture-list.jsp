<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 목록</title>
    <style>body {
        font-family: sans-serif;
    }</style>
</head>
<body>
<h1>강의 목록</h1>
<c:choose>
    <c:when test="${empty lectures}">
        <p>강의 목록이 없습니다.</p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="lecture" items="${lectures}">
                <li>${lecture.id} - ${lecture.title}
                    <a href="/v1/lectures/edit?id=${lecture.id}">수정</a>
                    <form method="post" action="/v1/lectures" style="display:inline">
                        <input type="hidden" name="_method" value="DELETE">
                        <input type="hidden" name="id" value="${lecture.id}">
                        <button type="submit">삭제</button>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>
<button onclick="location.href='/v1/lecture-registration.jsp'">등록</button>
</body>
</html>
