<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 목록</title>
    <style>body { font-family: sans-serif; }</style>
</head>
<body>
    <form method="post" action="/lectures">
        <input type="hidden" name="_method" value="PUT">
        <input type="hidden" name="id" value="${lecture.id}">
        <input type="text" name="title" value="${lecture.title}">
        <button type="submit">저장</button>
    </form>
</body>
</html>