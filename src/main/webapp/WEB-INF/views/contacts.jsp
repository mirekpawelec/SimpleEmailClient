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
        <title>Kontakty</title>
        <link rel="icon" type="image/ico" href="<c:url value='/resources/favicon/envelope.ico'/>">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/layout.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/header.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/footer.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/contacts.css'/>">
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        
        <div id="mainBox" class="container-variable-width">
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
                <div class="actionBar">
                    <a id="newContact">nowy kontakt</a>
                </div>
            </div>
            <div class="row">
                <div id="contacts" class="table">
                    <table>
                        <thead>
                            <tr>
                                <th>lp.</th>
                                <th>nazwa</th>
                                <th>adres e-mail</th>
                                <th>data dodania</th>
                                <th>status</th>
                                <th>akcja</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="contact" items="${contacts}" varStatus="couner">
                            <tr id="contact_${contact.id}">
                                <td id="lp_${contact.id}">${couner.count}</td>
                                <td id="name_${contact.id}">${contact.name}</td>
                                <td id="emailAddress_${contact.id}">${contact.emailAddress}</td>
                                <td id="createDate_${contact.id}">${fn:replace(contact.createDate,'T',' ')}</td>
                                <td id="status_${contact.id}">${contact.status}</td>
                                <td>
                                    <div class="col-12xs">
                                        <div class="col-12xs col-6l ">
                                            <button id="updateBtn_${contact.id}" class="table-button default" type="button">Zmień</button>
                                        </div>
                                        <div class="col-12xs col-6l">
                                            <form method="post">
                                                <input type="hidden" name="contactId" value="${contact.id}">
                                                <input type="hidden" name="delete" value="true">
                                                <button id="deleteBtn_${contact.id}" class="table-button danger" type="submit">Usuń</button>
                                            </form>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach> 
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp"/>
        <div class="modal">
            <div class="col-12xs offset-1xm col-10xm offset-2m col-8m offset-3lg col-6lg modalBoxContact">
                <span class="close">&times;</span>
                <div class="col-12xs modalBoxContactHeader">    
                    <p>Dodaj nowy kontakt</p>
                </div>
                <div class="col-12xs modalBoxContactBody">
                    <form id="contactForm" method="post">
                        <input id="contactId" type="hidden" name="contactId">
                        <div class="row">
                            <div class="form-group">
                                <label for="name" 
                                       class="col-12xs col-2s">
                                    Nazwa: 
                                </label>
                                <div class="col-12xs col-10s">
                                    <input id="name" 
                                           type="text" 
                                           name="name"
                                           data-error="length=Nazwa jest zbyt długa (max 100 znaków).;pattern=Niepoprawny format (niedozwolone znaki to: <>[]@).">
                                    <div id="form-error-name"></div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label for="emailAddress" 
                                       class="col-12xs col-2s">
                                    E-mail: 
                                </label>
                                <div class="col-12xs col-10s">
                                    <input id="emailAddress" 
                                           type="text" 
                                           name="emailAddress"
                                           data-error="empty=Podaj adres email.;length=Adres e-mail jest zbyt długi (max 100 znaków).;email=Niepoprawny format adresu e-mail.">
                                    <div id="form-error-emailAddress"></div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label for="status" 
                                       class="col-12xs col-2s">
                                    Status: 
                                </label>
                                <div class="col-12xs col-10s">
                                    <select id="status"                
                                            name="status"
                                            data-error="empty=Wybierz z listy jedną z opcji.">
                                        <option value="" selected disabled> --- wybierz --- </option>
                                        <c:forEach var="item" items="${contactStatuses}">
                                            <option value="${item.name}">${item.name}</option>
                                        </c:forEach>
                                    </select>
                                    <div id="form-error-status"></div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <input type="submit" 
                                       class="form-btn" 
                                       value="Zapisz">
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script type="text/javascript" src="<c:url value='/resources/js/contacts.js'/>"></script>
    </body>
</html>
