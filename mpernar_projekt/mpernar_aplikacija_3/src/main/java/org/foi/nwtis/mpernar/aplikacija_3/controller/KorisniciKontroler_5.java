package org.foi.nwtis.mpernar.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import org.foi.nwtis.mpernar.aplikacija_3.PristupServeru;
import org.foi.nwtis.mpernar.aplikacija_3.podaci.KorisniciKlijent_1;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja sluzi kao kontroler za pogled podrucja
 * @author Mario
 */
@Path("pregledPodrucja")
@Controller
public class KorisniciKontroler_5 {
    
    @Inject
    private Models model;
    
    @Inject
    HttpSession sjednica;
    
    @Inject
    ServletContext context;
    
    int port;
    String adresa;

    /**
     * metoda za ucitavanje podataka posluzitelja
     */
    private void ucitajPodatkePosluzitelja(){
        this.port = (int) context.getAttribute("portServera");
        this.adresa = (String) context.getAttribute("adresaServera");
    }
    
    /**
     * metoda za dohvacanje korisnika putem rest servisa
     * @return pogled na koji ce se vratiti korisnik
     */
    @GET
    public String dohvatiKorisnike() {
        
        String korisnik = (String)sjednica.getAttribute("korisnik");
        String lozinka = (String)sjednica.getAttribute("lozinka");
        String idSjednice = (String)sjednica.getAttribute("idSjednice");
        
        ucitajPodatkePosluzitelja();
        
        if (null != korisnik && !korisnik.equals("") && null != lozinka && !lozinka.equals("")) {
            
            String zahtjev = "AUTHOR " + korisnik + " " + idSjednice + " administracija";
        
            String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
            
            if (!odgovor.equals("OK")) {
                if (odgovor.equals("ERROR 16 Broj preostalih zahtjeva je 0!")) {
                    model.put("opisObavijesti", "Korisnik je iskoristio maksimalan broj zahtjeva u sjednici!");
                    return "privatno/obavijest.jsp";
                }
                model.put("opisObavijesti", "Korisnik nije autoriziran za ovo podrucje!");
                return "privatno/obavijest.jsp";
            }
            
            KorisniciKlijent_1 kk1 = new KorisniciKlijent_1();
        
            Response korisnici = kk1.dajKorisnike(Response.class, korisnik, lozinka);

            if (korisnici.getStatusInfo() != Status.OK) {
                model.put("opisObavijesti", "Problem prilikom dohvacanja korisnika!");
                return "privatno/obavijest.jsp";
            }
            
            List<Korisnik> sviKorisnici = korisnici.readEntity(List.class);
            model.put("sviKorisnici", sviKorisnici);
            
            return "privatno/pregledPodrucja.jsp";
        }
        
        model.put("opisPogreske", "Ne postoji vazeca sjednica za korisnika!");
        
        return "greska.jsp";
    }
}
