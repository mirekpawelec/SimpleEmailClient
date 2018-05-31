<%-- 
    Document   : login
    Created on : 2018-04-03, 15:48:45
    Author     : mirek
--%>

<!--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>-->
<!--<%@page contentType="text/html" pageEncoding="UTF-8"%>-->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Logowanie</title>
        <link rel="icon" type="image/ico" href="<c:url value='/resources/favicon/envelope.ico'/>">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.10/css/all.css">
	<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>">
	<link rel="stylesheet" href="<c:url value='/resources/css/login.css'/>">
    </head>
    <body>
<!--MAIN CONTENT OF PAGE-->
        <header>
            <div class="container-full">
                <div class="btn-header">
                    <span id="showModalBtn" class="btn-register"> Zarejestruj się </span>
                </div>
            </div>
        </header>
        <section>
            <div class="container-full">
                <div class="row">
                        <div id="logo" class="col-12xs">
                            <div class="col-12xs col-10xm offset-2xm col-8m offset-4m col-7l offset-5l col-6lg offset-6lg first-line">
                                <span>W</span>
                                <span>i</span>
                                <span>t</span>
                                <span>a</span>
                                <span>j</span>
                                <span>&nbsp;</span>
                                <span>w</span>
                            </div>
                            <div class="col-12xs col-10xm offset-2xm col-8m offset-4m col-7l offset-5l col-6lg offset-6lg second-line wave-effect">
                                <span class="big-letter">S</span>
                                <span>i</span>
                                <span>m</span>
                                <span>p</span>
                                <span>l</span>
                                <span>e</span>
                                <span class="big-letter">E</span>
                                <span>m</span>
                                <span>a</span>
                                <span>i</span>
                                <span>l</span>
                                <span class="big-letter">C</span>
                                <span>l</span>
                                <span>i</span>
                                <span>e</span>
                                <span>n</span>
                                <span>t</span>
                                <span>!</span>
                            </div>
                        </div>
                </div>
                <div class="row">
                    <div class="col-12xs">
                        <div id="infoBox">
                            <div id="boxSuccess" class="${not empty sessionScope.success?'show':''}">
                                <div id="verticalLineLeft"></div>
                                <div id="message">
                                    <p class="welcome">Witaj <span>${sessionScope.success.value}</span>!</p>
                                    <p>Twoje konto zostało utworzone!</p>
                                    <p>Możesz się zalogować.</p>
                                </div>
                                <div id="verticalLineRight"> << </div>
                            </div>
                            <div id="boxError" class="${not empty requestScope.error?'show':''}">
                                <div class="btn-close">&times;</div>
                                <p>${requestScope.error}</p>
                                <div class="error-details">
                                    <ul>
                                    <c:forEach var="err" items="${errorMap}" varStatus="counter">
                                        <li>${err[0]} : ${err[1]}</li>
                                    </c:forEach>
                                    </ul>
                                </div> 
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-12xs">
                        <div class="panel-logowania">
                            <div class="panel-header">
                                <div class="panel-title">
                                    Logowanie do SCM
                                </div>
                            </div>
                            <div class="panel-body">
                                <form action="j_security_check" 
                                      method="post">
                                        <div class="input-group">
                                            <div class="merge">
                                                <i class="fas fa-user"></i>
                                                <input type="text" 
                                                       name="j_username" 
                                                       placeholder="login">
                                            </div>
                                        </div>
                                        <div class="input-group">
                                            <div class="merge">
                                                <i class="fas fa-key"></i>
                                                <input type="password" 
                                                       name="j_password" 
                                                       placeholder="hasło">
                                            </div>
                                        </div>
                                        <div class="input-group">
                                            <button type="submit">
                                                Zaloguj
                                            </button>
                                        </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>	
        </section>
        <footer></footer>

<!--MODAL BLOCK-->
        <div id="registerModalWindow" class="modal">
            <div class="modal-content">
                <form id="registerForm" 
                      action="<c:url value='/app/register'/>"
                      method="POST">
                    <div class="tab">
                        <div class="modal-header">
                            <div class="row">
                                <span class="close">&times;</span>
                                <div class="col-12xs col-10m offset-1m">
                                        <h1> Podstawowe dane twojego konta e-mail: </h1>
                                </div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-12xs form-group">
                                    <div class="col-12xs col-3s col-2m offset-1m form-label">
                                        <label for="firstName"> Imię </label>
                                    </div>
                                    <div class="col-12xs col-9s col-8m">
                                        <input  class="form-control" 
                                                type="text" 
                                                id="firstName" 
                                                name="firstName"
                                                data-error="empty=Podaj swoje imie.">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12xs form-group">
                                    <div class="col-12xs col-3s col-2m offset-1m form-label">
                                        <label for="lastName"> Nazwisko </label>
                                    </div>
                                    <div class="col-12xs col-9s col-8m">
                                        <input  class="form-control" 
                                                type="text" 
                                                id="lastName" 
                                                name="lastName"
                                                data-error="empty=Podaj swoje nazwisko.">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12xs form-group">
                                    <div class="col-12xs col-10m offset-1m form-label">
                                        <label for="emailAddress"> Adres i hasło </label>
                                    </div>
                                    <div class="col-12xs col-10m offset-1m additional-space">
                                        <input  class="form-control" 
                                                type="email" 
                                                id="emailAddress" 
                                                name="emailAddress"
                                                placeholder="adres e-mail"
                                                data-error="empty=Podaj adres email.; email=To nie jest adres email.">
                                    </div>
                                    <div class="col-12xs col-10m offset-1m additional-space">
                                        <input  class="form-control" 
                                                type="password" 
                                                id="emailPassword" 
                                                name="emailPassword" 
                                                placeholder="hasło"
                                                data-error="empty=Podaj hasło.">
                                    </div>
                                    <div class="col-12xs col-10m offset-1m additional-space">
                                        <input  class="form-control" 
                                                type="password" 
                                                id="emailRepeatPassword" 
                                                name="emailRepeatPassword" 
                                                placeholder="powtórz hasło"
                                                data-error="empty=Podaj ponownie hasło.; password=Hasła nie są identyczne.">
                                    </div>
                                </div>
                            </div>
                        </div>						
                    </div>
                    <div class="tab">
                        <div class="modal-header">
                            <div class="row">
                                <span class="close">&times;</span>
                                <div class="col-12xs col-10m offset-1m">
                                    <h1> Konfiguracja: </h1>
                                </div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-12xs form-group">
                                    <div class="col-12xs col-11m offset-1m form-label">
                                            <label>Serwer poczty przychodzącej POP3</label>
                                    </div>
                                    <div class="col-12xs col-8s col-7m offset-1m additional-space">
                                        <input  class="form-control" 
                                                type="text" 
                                                id="pop3Address"
                                                name="pop3Address" 
                                                placeholder="adres"
                                                data-error="empty=Nazwa serwera POP3 jest wymagana.">
                                    </div>
                                    <div class="col-12xs col-3s offset-1s col-2m">
                                        <input  class="form-control" 
                                                type="number" 
                                                id="pop3Port"
                                                name="pop3Port" 
                                                placeholder="nr portu"
                                                data-error="empty=Dodaj nr portu.">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12xs form-group">
                                    <div class="col-12xs col-11m offset-1m form-label">
                                            <label>Serwer poczty wychodzącej SMTP</label>
                                    </div>
                                    <div class="col-12xs col-8s col-7m offset-1m additional-space">
                                        <input  class="form-control" 
                                                type="text" 
                                                id="smtpAddress"
                                                name="smtpAddress" 
                                                placeholder="adres"
                                                data-error="empty=Nazwa serwera SMTP jest wymagana.">
                                    </div>
                                    <div class="col-12xs col-3s offset-1s col-2m">
                                        <input  class="form-control" 
                                                type="number"
                                                id="smtpPort" 
                                                name="smtpPort" 
                                                placeholder="nr portu"
                                                data-error="empty=Dodaj nr portu.">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="tab">
                        <div class="modal-header">
                            <div class="row">
                                <span class="close">&times;</span>
                                <div class="col-12xs col-10m offset-1m">
                                    <h1> Twoje konto w SCM: </h1>
                                </div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-12xs form-group">
                                    <div class="col-12xs col-4s col-3xm col-2m offset-1m form-label">
                                        <label for="login"> Login </label>
                                    </div>
                                    <div class="col-12xs col-8s col-9xm col-8m">
                                        <input  class="form-control" 
                                                type="text" 
                                                id="login" 
                                                name="login"
                                                data-error="empty=Podaj login dla twojego konta.; exists=Podany login jest używany.; pattern=Akceptowalne są tylko znaki alfanumeryczne (a-zA-Z0-9_).">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12xs form-group">
                                    <div class="col-12xs col-4s col-3xm col-2m offset-1m form-label">
                                        <label for="password"> Hasło </label>
                                    </div>
                                    <div class="col-12xs col-8s col-9xm col-8m">
                                        <input  class="form-control" 
                                                type="password" 
                                                id="password" 
                                                name="password"
                                                data-error="empty=Podaj hasło.; pattern=Twoje hasło nie spełnia wymagań.">
                                        <div id="infoPasswd" class="col-12xs info-password">
                                            Hasło powinno składać się z <i id="lengthPasswd">min 8 naków</i>, zawierać <i id="lowercaseLetterPasswd">małą literę</i>, <i id="digitPasswd">cyfrę</i> i <i id="uppercaseLetterPasswd"> dużą literę</i>!
                                        </div>
                                    </div>	
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12xs form-group">
                                    <div class="col-12xs col-4s col-3xm col-2m offset-1m form-label">
                                        <label for="repeatPassword"> Powtórz hasło </label>
                                    </div>
                                    <div class="col-12xs col-8s col-9xm col-8m">
                                        <input  class="form-control" 
                                                type="password" 
                                                id="repeatPassword" 
                                                name="repeatPassword"
                                                data-error="empty=Wpisz ponownie hasło.; password=Hasła nie są identyczne.">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="row">
                            <div class="col-12xs col-10m offset-1m">
                                <div class="col-12xs col-6s step-indicator">
                                    <span class="step"></span>
                                    <span class="step"></span>
                                    <span class="step"></span>
                                </div>
                                <div class="col-12xs col-6s">
                                    <div class="navigation-btn">
                                        <button type="button" id="prevBtn"> Wróć </button>
                                        <button type="button" id="nextBtn"> Dalej </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                   </div>
                </form>
            </div>
        </div>
    
        <script src="<c:url value='/resources/js/ajax.js'/>"></script>
        <script src="<c:url value='/resources/js/login.js'/>"></script>
        <% 
//            if(request.getSession(false).getAttribute("success")!=null) 
//                request.getSession(false).removeAttribute("success"); 
        %>
    </body>
</html>
