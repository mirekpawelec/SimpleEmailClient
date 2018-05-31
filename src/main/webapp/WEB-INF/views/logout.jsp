<%-- 
    Document   : logout
    Created on : 2018-04-09, 00:52:16
    Author     : mirek
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Wylogowanie</title>
    </head>
    <body>
        <h1>Zostałeś poprawnie wylogowany z serwisu!</h1>
        <a href="<c:url value='/app/index'/>"> Zaloguj ponownie </a>
    </body>
</html>
