<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 등록</title>
    <style>body { font-family: sans-serif; }</style>
</head>
<body>
<h1>강의 등록</h1>
<form method="post" action="/v2/lectures">
    <label>제목: <input type="text" name="title" required></label>
    <button type="submit">등록</button>
</form>
<a href="/v2/lectures">목록으로</a>
</body>
</html>
