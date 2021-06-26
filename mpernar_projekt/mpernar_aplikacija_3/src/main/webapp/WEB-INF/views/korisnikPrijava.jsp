<%-- 
    Document   : korisnikUnos
    Created on : Apr 27, 2021, 8:47:18 PM
    Author     : Mario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Prijava korisnika</title>
    </head>
    <body>
        <h1>Prijava korisnika</h1>
        <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/korisnikPrijava">
            <table>
                <tr>
                    <td>Korisničko ime: </td>
                    <td><input type="text" name="korisnik"/></td>
                </tr>
                <tr>
                    <td>Lozinka: </td>
                    <td><input type="password" name="lozinka"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Prijavi me"/></td>
                </tr>
            </table>
        </form>
        <a href="${pageContext.servletContext.contextPath}">Pocetna</a>
    </body>
</html>
