package org.foi.nwtis.mpernar.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.foi.nwtis.mpernar.aplikacija_3.PristupServeru;

/**
 * Klasa koja sluzi kao kontroler za pogled za slanje komandi
 * @author Mario
 */
@Path("slanjeKomandi")
@Controller
public class KorisniciKontroler_6 {
    
    @Inject
    private Models model;
    
    @Inject
    HttpSession sjednica;
    
    @FormParam("zahtjev")
    String zahtjev;
    
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
     * metoda za slanje zahtjeva na posluzitelj
     * @return pogled koji ce se vratiti korisniku
     */
    @POST
    public String posaljiZahtjev() {
        ucitajPodatkePosluzitelja();
        
        String korisnik = (String)sjednica.getAttribute("korisnik");
        String lozinka = (String)sjednica.getAttribute("lozinka");
        
        if (null != korisnik) {
            
            String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
            
            model.put("odgovor", odgovor);
            
            return "privatno/slanjeKomandi.jsp";
        }
        
        model.put("opisPogreske", "Ne postoji vazeca sjednica za korisnika!");
        
        return "greska.jsp";
    }
}
