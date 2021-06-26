<%-- 
    Document   : greska
    Created on : May 8, 2021, 5:37:33 AM
    Author     : Mario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Obavijest</title>
    </head>
    <body>
        <h1>Obavijest</h1>
        <p>Obavijest ${opisObavijesti}</p>
        <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/izbornik">Izbornik</a>
    </body>
</html>
