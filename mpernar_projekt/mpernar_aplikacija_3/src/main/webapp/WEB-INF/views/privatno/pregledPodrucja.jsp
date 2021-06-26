<%-- 
    Document   : radSAerodromima
    Created on : May 8, 2021, 5:38:15 AM
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
        <title>Pregled područja</title>
    </head>
    <body>
        <h1>Korisnici</h1>
        <div>
            <h2>Svi korisnici</h2>
            <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/podrucja/aktiviraj">
                <label for="kimeA">Korisnicko ime:  </label>
                <input type="text" name="kimeA" id="kimeA"/>
                <label for="podrucjeA">Podrucje: </label>
                <input type="text" name="podrucjeA" id="podrucjeA"/>
                <input type="submit" value="Aktiviraj podrucje"/>
            </form>
            <br><br>
            <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/podrucja/deaktiviraj">
                <label for="kimeD">Korisnicko ime:  </label>
                <input type="text" name="kimeD" id="kimeD"/>
                <label for="podrucjeD">Podrucje: </label>
                <input type="text" name="podrucjeD" id="podrucjeD"/>
                <input type="submit" value="Deaktiviraj podrucje"/>
            </form>
            <br><br>
            <table id="korisnici" class="diplay">
                <thead>
                    <tr>
                        <th>KORISNICKO IME</th>
                        <th>IME</th>
                        <th>PREZIME</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${sviKorisnici}" var="k">
                        <tr>
                            <td>${k.korisnik}</td> 
                            <td>${k.ime}</td> 
                            <td>${k.prezime}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div>
            <h2>Sva podrucja</h2>
            <ol>
                <li>administracija</li>
                <li>administracijaAerodroma</li>
                <li>pregledKorisnik</li>
                <li>pregledJMS</li>
                <li>pregledDnevnik</li>
                <li>pregledAktivnihKorisnika</li>
                <li>pregledAerodroma</li>
            </ol>        
        </div>
        <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/izbornik">Izbornik</a>
    </body>
</html>


<script>
    $(document).ready(function () {
        $('#korisnici').DataTable();
    });
</script>