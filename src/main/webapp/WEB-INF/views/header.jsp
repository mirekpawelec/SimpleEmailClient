<%-- 
    Document   : header
    Created on : 2018-05-25, 06:03:48
    Author     : mirek
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<header class="container-full">
    <div class="row header-elements">
        <div id="logo" class="col-4xs col-3s col-2m">
            <ul>
                <li><a class="logo animation-hover" href="<c:url value="/"/>">SEC</a></li>
            </ul>
        </div>
        <div id="menuUser" class="col-8xs col-6s col-5m col-4l">
            <ul>
                <li><a class="contacts animation-hover" href="<c:url value='/app/contacts'/>">Kontakty</a></li>
                <li><a class="welcome animation-hover" href="<c:url value='/app/myAccount'/>">Witaj <span>${user.fullName}</span></a></li>
                <li><a class="sign-out animation-hover" href="<c:url value='/app/logout'/>"><i class="fas fa-sign-out-alt"></i> Wyloguj </a></li>
            </ul>                
        </div>
    </div>
</header>
