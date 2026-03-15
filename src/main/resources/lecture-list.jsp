<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<body>
<ul>
    <c:forEach var="lecture" items="${lectures}">
        <li>${lecture.id} - ${lecture.title}</li>
    </c:forEach>
</ul>
</body>
</html>