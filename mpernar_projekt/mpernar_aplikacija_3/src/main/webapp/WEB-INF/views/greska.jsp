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
        <title>Greška!</title>
    </head>
    <body>
        <h1>Greška!</h1>
        <p>Pogreška: ${opisPogreske}</p>
        <a href="${pageContext.servletContext.contextPath}">Početna</a>
    </body>
</html>
