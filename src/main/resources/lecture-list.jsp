<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 목록</title>
</head>
<body>
<a href="/lecture-registration.jsp">등록</a>
<ul>
    <c:forEach var="lecture" items="${lectures}">
        <li>${lecture.id} - ${lecture.title}</li>
    </c:forEach>
</ul>
</body>
</html>