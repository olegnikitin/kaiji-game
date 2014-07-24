<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>DAO testing</title>
</head>
<body>
    DAO API:<br>
    <hr>
    /save/user/{nick}/{email}<br>
    /save/game-info/{game-runtime-id}<br>
    /save/game-history/{game-runtime-id}<br>
    <hr>
    /get/user/all <br>
    /get/user/{id} <br>
    /get/game-info/all<br>
    /get/game-info/{id}<br>
    /get/game-history/all<br>
    /get/game-history/{id}<br>
    <hr>
    /update/game-info/{game-runtime-id}/{saved-game-info-id}<br>
    /update/game-info/{game-runtime-id}/{saved-game-history-id}<br>
    <hr>
    /remove/user/{id}<br>
    /remove/game-info/{id}<br>
    /remove/game-history/{id}<br>
    <hr>
    <a href="/">Kaiji</a>

    <br><br>
    Data:<br>
    <c:out value="${data}"></c:out>

</body>
</html>