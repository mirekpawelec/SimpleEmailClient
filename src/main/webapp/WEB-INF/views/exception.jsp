<%-- 
    Document   : exception
    Created on : 2018-04-08, 18:58:51
    Author     : mirek
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
    </head>
    <body>
        <h1>Błąd programu!</h1>
        <a href="<c:url value='/app/index'/>">Strona główna</a>
        <br>
        <a href="<c:url value='/app/logout'/>">Wyloguj</a>
    </body>
</html>
