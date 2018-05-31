<%-- 
    Document   : index
    Created on : 2018-04-04, 21:40:05
    Author     : mirek
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Strona główna</title>
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>">
    </head>
    <body>
        <p class="welcome"> Witaj, ${user.fullName}</p>
        <h1 class="title">Strona główna</h1>
        <a href="<c:url value='/app/logout'/>">Wyloguj</a>

        <script type="text/javascript" src="<c:url value='/resources/js/script.js'/>"></script>
    </body>
</html>
