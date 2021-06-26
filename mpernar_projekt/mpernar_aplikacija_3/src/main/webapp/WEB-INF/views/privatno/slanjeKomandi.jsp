<%-- 
    Document   : slanjeKomandi
    Created on : Jun 11, 2021, 5:43:10 PM
    Author     : Mario
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>Slanje komandi</title>
    </head>
    <body>
        <h1>Slanje komandi</h1>
        <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/slanjeKomandi">
            <table>
                <tr>
                    <td>Zahtjev:</td>
                    <td><input type="text" name="zahtjev"/></td>
                </tr>
                <tr>
                    <td>Odgovor:</td>
                    <td>${odgovor}</td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Posalji zahtjev"/></td>
                </tr>
            </table>
        </form>
        <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/izbornik">Izbornik</a>
    </body>
</html>
