<%-- 
    Document   : radSKorisnicima
    Created on : May 8, 2021, 5:38:32 AM
    Author     : Mario
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js" integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ==" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.21/js/jquery.dataTables.min.js" integrity="sha512-BkpSL20WETFylMrcirBahHfSnY++H2O1W+UnEEO4yNIl+jI2+zowyoGJpbtk6bx97fBXf++WJHSSK2MV4ghPcg==" crossorigin="anonymous"></script>
        <title>Moji aerodromi</title>
    </head>
    <body>
        <h1>Moji aerodromi</h1>
        <div>
            <h2>Moji aerodromi</h2>
            <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/dodajAerodrom">
                <label for="icao">ICAO: </label>
                <input type="text" name="icao" id="icao"/>
                <input type="submit" value="Zaprati aerodrom"/>
            </form>
            <br><br>
            <table id="mojiAerodromi" class="display">
                <thead>
                    <tr>
                        <th>IDENT</th>
                        <th>DRŽAVA</th>
                        <th>NAZIV</th>
                        <th>LONGITUDA</th>
                        <th>LATITUDA</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${mojiAerodromi}" var="a">
                        <tr>
                            <td>${a.icao}</td> 
                            <td>${a.drzava}</td> 
                            <td>${a.naziv}</td> 
                            <td>${a.lokacija.longitude}</td> 
                            <td>${a.lokacija.latitude}</td>
                            <td>
                                <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/pregledAerodromi/obrisiAerodrom/${a.icao}">
                                    <input type="submit" value="Otprati"/>
                                </form>
                            </td>
                            <td>
                                <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/pregledAerodromi/prikaziKorisnike/${a.icao}">
                                    <input type="submit" value="Korisnici"/>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div>
            <h2>Korisnici</h2>
            <table id="korisnici" class="diplay">
                <thead>
                    <tr>
                        <th>KORISNICKO IME</th>
                        <th>IME</th>
                        <th>PREZIME</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${korisniciZaAerodrom}" var="k">
                        <tr>
                            <td>${k.korisnik}</td> 
                            <td>${k.ime}</td> 
                            <td>${k.prezime}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/izbornik">Izbornik</a>
    </body>
</html>

<script>
    $(document).ready(function () {
        $('#mojiAerodromi').DataTable();
        $('#korisnici').DataTable();
    });
</script>
