<%-- 
    Document   : contacts
    Created on : 2018-05-25, 05:53:02
    Author     : mirek
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Moje konto</title>
        <link rel="icon" type="image/ico" href="<c:url value='/resources/favicon/envelope.ico'/>">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/layout.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/header.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/footer.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/myAccount.css'/>">
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <div class="container">
            <div class="row">
                <div class="menu">
                    <div class="btn-groups">
                        <button id="myDataBtn">Twoje dane</button>
                        <button id="filtersBtn">Filtry</button>
                    </div>
                </div>
            </div>
            <div id="myDataPanel">
                <p> Twój login: ${sessionScope.user.login} </p>
                <form id="myDataForm" method="post">
                    <label for="fullName">Imię i nazwisko</label>
                    <input id="fullName" type="text" name="fullName">
                    <label for="password">Zmień hasło</label>
                    <input id="password" type="password" name="password">
                    <input id="repeatPassword" type="password" name="repeatPassword">
                    <button type="submit"> Zapisz </button>
                </form> 
           </div>
            <div id="filtersPanel">
                
            </div>
        </div>
        <jsp:include page="footer.jsp"/>
        <script type="text/javascript" src="<c:url value='/resources/js/myAccount.js'/>"></script>
    </body>
</html>
