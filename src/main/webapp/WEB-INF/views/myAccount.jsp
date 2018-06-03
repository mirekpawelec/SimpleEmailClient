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
                        <button id="myDataBtn" 
                                class="default">
                            Twoje dane
                        </button>
                        <button id="filtersBtn"
                                class="default">
                            Filtry
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="alert success ${not empty success?'success':''} ${not empty error?'error':''}" 
                     style="display: ${not empty success||not empty error?'block':'none'}">
                    <span class="alert-close">&times;</span>  
                    <c:choose>
                        <c:when test="${not empty success}">
                            <strong>Udało się!</strong> ${success}
                        </c:when>
                        <c:when test="${not empty error}">
                            <strong>Niepowodzenie!</strong> ${error}
                        </c:when>
                    </c:choose>
                    
                </div>
            </div>
            <div class="row">
                <div id="myDataPanel" class="col-12xs">
                    <p> Twój login: 
                        <span class="login">${sessionScope.user.login}</span> 
                    </p>
                    <br/><br/>
                    <form id="myDataForm" method="post">
                        <div class="row">
                            <div class="form-group">
                                <label for="fullName" 
                                       class="col-12xs col-3s col-2m">
                                    Imię i nazwisko
                                </label>
                                <div class="col-12xs col-9s col-10m">
                                    <input id="fullName"  
                                           type="text" 
                                           name="fullName"
                                           data-error="length=Nazwa jest zbyt długa (max. 150 znaków)."
                                           value="${sessionScope.user.fullName}">
                                    <div id="form-error-fullName"></div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label for="password"
                                       class="col-12xs">
                                    Zmień hasło
                                </label>
                                <div class="col-12xs">
                                    <input id="password"
                                           type="password" 
                                           name="password"
                                           data-error="empty=Podaj hasło.; pattern=Twoje hasło nie spełnia wymagań.">
                                    <div id="infoPasswd" class="col-12xs info-password">
                                        Hasło powinno składać się z <i id="lengthPasswd">min 8 naków</i>, zawierać <i id="lowercaseLetterPasswd">małą literę</i>, <i id="digitPasswd">cyfrę</i> i <i id="uppercaseLetterPasswd"> dużą literę</i>!
                                    </div>
                                    <div id="form-error-password"></div>
                                    <br/>
                                </div>
                                <div class="col-12xs">
                                    <input id="repeatPassword" 
                                           type="password" 
                                           name="repeatPassword"
                                           data-error="empty=Wpisz ponownie hasło.; password=Hasła nie są identyczne.">
                                    <div id="form-error-repeatPassword"></div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-12xs form-group">
                                <input type="submit" 
                                       class="form-btn" 
                                       value="Zapisz">
                            </div>
                        </div>
                    </form> 
                </div>
            </div>
            <div class="row">
                <div id="filtersPanel">
                    <p> W trakcie budowy :-) </p>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp"/>
        <script type="text/javascript" src="<c:url value='/resources/js/myAccount.js'/>"></script>
    </body>
</html>
