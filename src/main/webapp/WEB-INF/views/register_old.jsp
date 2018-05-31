<!--
<%-- 
    Document   : register
    Created on : 2018-04-03, 17:51:33
    Author     : mirek
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
-->

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Rejestracja</title>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
        <link rel="stylesheet" href="../../resources/css/layout.css">
        <link rel="stylesheet" href="../../resources/css/register.css">
    </head>
    <body>
       <button type="button" id="showModalBtn">Pokaż</button>
        <div id="registerModalWindow" class="modal">
        	<div class="modal-content">
				<form id="registerForm" method="POST">
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
									<div class="col-3xs col-2m offset-1m form-label">
										<label for="firstName"> Imię </label>
									</div>
									<div class="col-9xs col-8m">
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
									<div class="col-3xs col-2m offset-1m form-label">
										<label for="lastName"> Nazwisko </label>
									</div>
									<div class="col-9xs col-8m">
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
									<div class="col-8xs col-7m offset-1m additional-space">
										<input  class="form-control" 
												type="text" 
												id="pop3Address"
												name="pop3Address" 
												placeholder="adres"
												data-error="empty=Nazwa serwera POP3 jest wymagana.">
									</div>
									<div class="col-3xs offset-1xs col-2m">
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
									<div class="col-8xs col-7m offset-1m additional-space">
										<input  class="form-control" 
												type="text" 
												id="smtpAddress"
												name="smtpAddress" 
												placeholder="adres"
												data-error="empty=Nazwa serwera SMTP jest wymagana.">
									</div>
									<div class="col-3xs offset-1xs col-2m">
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
									<div class="col-4xs col-3s col-2m offset-1m form-label">
										<label for="login"> Login </label>
									</div>
									<div class="col-8xs col-9s col-8m">
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
									<div class="col-4xs col-3s col-2m offset-1m form-label">
										<label for="password"> Hasło </label>
									</div>
									<div class="col-8xs col-9s col-8m">
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
									<div class="col-4xs col-3s col-2m offset-1m form-label">
										<label for="repeatPassword"> Powtórz hasło </label>
									</div>
									<div class="col-8xs col-9s col-8m">
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
								<div class="col-6xs step-indicator">
									<span class="step"></span>
									<span class="step"></span>
									<span class="step"></span>
								</div>
								<div class="col-6xs">
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
    	<script src="../../resources/js/ajax.js"></script>
    	<script src="../../resources/js/login.js"></script>
    </body>
</html>
