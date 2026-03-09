<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 목록</title>
</head>
<body>
<a href="/lecture-registration">등록</a>
<c:forEach var="lecture" items="${lectures}">
    <li>No: ${lecture.id}</li>
    <li>강의 이름: ${lecture.name}</li>
    <li>강의 가격: ${lecture.price}</li>
    <br>
</c:forEach>
</body>
</html>
