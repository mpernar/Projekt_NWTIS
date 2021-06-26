package org.foi.nwtis.mpernar.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.foi.nwtis.mpernar.aplikacija_3.podaci.KorisniciKlijent_1;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja sluzi kao kontroler za registraciju korisnika
 * @author Mario
 */
@Path("korisnikRegistracija")
@Controller
public class KorisniciKontroler_2 {

    @FormParam("korisnik")
    String korisnik;
    
    @FormParam("lozinka")
    String lozinka;
    
    @FormParam("lozinkaPotvrda")
    String lozinkaPotvrda;
    
    @FormParam("prezime")
    String prezime;
    
    @FormParam("ime")
    String ime;
    
    @FormParam("email")
    String email;
    
    @Inject
    private Models model;
    
    /**
     * metoda koja sluzi za registraciju korisnika
     * @return vraca pogled na koji ce se preusmjeriti korisnik
     */
    @POST
    public String registracija() {
        KorisniciKlijent_1 kk = new KorisniciKlijent_1();
        
        Korisnik kor = new Korisnik(korisnik, lozinka, prezime, ime, email, 0);
        
        Response r = kk.dodajKorisnika(kor, Response.class);
        
        if (r.getStatusInfo() == Status.OK) {
            return "korisnikPrijava.jsp";
        }
        else{
            model.put("opisPogreske", "Pogreska prilikom registracije!");
            return "greska.jsp";
        }
    }
}
