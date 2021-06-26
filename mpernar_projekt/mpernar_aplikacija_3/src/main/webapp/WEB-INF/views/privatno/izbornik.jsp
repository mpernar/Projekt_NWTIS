<%-- 
    Document   : izbornik
    Created on : Jun 11, 2021, 5:58:22 PM
    Author     : Mario
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Izbornik</title>
    </head>
    <body>
        <h1>Izbornik</h1>
        <ul>
            <li><a href="${pageContext.servletContext.contextPath}/mvc/pregledPodrucja">Rad s podrucjima</a></li>
            <li><a href="${pageContext.servletContext.contextPath}/mvc/pregledAerodromi">Rad s aerodromima</a></li>
            <li><a href="${pageContext.servletContext.contextPath}/mvc/korisnik/slanjeKomandi">Rad s komandama</a></li>
            <li><a href="${pageContext.servletContext.contextPath}/mvc/odjavaKorisnika">Odjava korisnika</a></li>
        </ul>
        <p>${opisPogreske}</p>
    </body>
</html>
