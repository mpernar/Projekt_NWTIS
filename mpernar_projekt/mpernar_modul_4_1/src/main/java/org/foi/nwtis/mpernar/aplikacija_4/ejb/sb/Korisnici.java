package org.foi.nwtis.mpernar.aplikacija_4.ejb.sb;

import jakarta.ejb.Stateful;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.foi.nwtis.mpernar.aplikacija_4.podaci.KorisniciKlijent_1;
import org.foi.nwtis.podaci.Korisnik;


/**
 * Klasa koja predstavlja stateful session bean za korisnike
 * @author Mario
 */
@Stateful
public class Korisnici {
    
    /**
     * metoda za dohvacanje svih korisnika putem rest servisa
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @return lista korisnika
     */
    public List<Korisnik> dohvatiSveKorisnike(String korisnik, String lozinka){
        
        KorisniciKlijent_1 kk1 = new KorisniciKlijent_1();
        
        Response r = kk1.dajKorisnike(Response.class, korisnik, lozinka);
        
        if (r.getStatusInfo() != Response.Status.OK) {
                return null;
            }
        
        List<Korisnik> korisnici = r.readEntity(List.class);
        System.out.println(korisnici);
        return korisnici;
    }
}
