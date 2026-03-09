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
    <div id="view-${lecture.id}">
        <li>No: ${lecture.id}</li>
        <li>강의 이름: ${lecture.name}</li>
        <li>강의 가격: ${lecture.price}</li>
        <button onclick="showEditForm(${lecture.id}, '${lecture.name}', ${lecture.price})">수정</button>
        <button onclick="deleteLecture(${lecture.id})">삭제</button>
    </div>
    <div id="edit-${lecture.id}" style="display:none;">
        <li>No: ${lecture.id}</li>
        <li>강의 이름: <input type="text" id="edit-name-${lecture.id}" value="${lecture.name}"></li>
        <li>강의 가격: <input type="number" id="edit-price-${lecture.id}" value="${lecture.price}"></li>
        <button onclick="updateLecture(${lecture.id})">수정</button>
        <button onclick="cancelEdit(${lecture.id})">취소</button>
    </div>
    <br>
</c:forEach>

<script>
    function showEditForm(id, name, price) {
        document.getElementById("view-" + id).style.display = "none";
        document.getElementById("edit-" + id).style.display = "block";
    }

    function cancelEdit(id) {
        document.getElementById("edit-" + id).style.display = "none";
        document.getElementById("view-" + id).style.display = "block";
    }

    function updateLecture(id) {
        const name = document.getElementById("edit-name-" + id).value;
        const price = document.getElementById("edit-price-" + id).value;

        fetch("/lectures/" + id, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({id: Number(id), name: name, price: Number(price)})
        }).then(response => {
            if (response.ok) {
                alert("수정되었습니다.");
                window.location.reload();
            } else {
                alert("수정을 실패했습니다.");
            }
        });
    }

    function deleteLecture(id) {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        fetch("/lectures/" + id, {
            method: "DELETE"
        }).then(response => {
            if (response.ok) {
                alert("삭제되었습니다.");
                window.location.reload();
            } else {
                alert("삭제를 실패했습니다.");
            }
        });
    }
</script>
</body>
</html>
