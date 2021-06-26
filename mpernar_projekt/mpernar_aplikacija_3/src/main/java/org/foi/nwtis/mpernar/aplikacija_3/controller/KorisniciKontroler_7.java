package org.foi.nwtis.mpernar.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.foi.nwtis.mpernar.aplikacija_3.PristupServeru;
import org.foi.nwtis.mpernar.aplikacija_3.wsep.OglasnikPrijave;

/**
 * Klasa koja sluzi kao kontroler za odjavu korisnika
 * @author Mario
 */
@Path("odjavaKorisnika")
@Controller
public class KorisniciKontroler_7 {
    
    @Inject
    private Models model;
    
    @Inject
    HttpSession sjednica;
    
    @Inject
    ServletContext context;
    
    @Inject
    OglasnikPrijave oglasnikPrijave;
    
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
     * metoda koja sluzi za odjavu korisnika
     * @return pogled koji ce se vratiti korisniku
     */
    @GET
    public String odjaviKorisnika() {
        
        String korisnik = (String)sjednica.getAttribute("korisnik");
        String lozinka = (String)sjednica.getAttribute("lozinka");
        String idSjednice = (String)sjednica.getAttribute("idSjednice");
        
        ucitajPodatkePosluzitelja();
        
        if (null != korisnik && !korisnik.equals("") && null != lozinka && !lozinka.equals("")) {
            
            String zahtjev = "LOGOUT " + korisnik + " " + idSjednice;
        
            String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
            
            if (odgovor.equals("OK")) {
                sjednica.removeAttribute("korisnik");
                sjednica.removeAttribute("lozinka");
                sjednica.removeAttribute("idSjednice");
                
                String poruka = "Korisnik: " + korisnik + ", IdSjednice: " + idSjednice + ", Zahtjev: " + zahtjev +  ", Odgovor: " + odgovor + " Aplikacija: aplikacija_3, Akcija: odjava"; 
                oglasnikPrijave.posaljiPoruku(poruka);
                
                return "../../index.jsp";
            }
           
            model.put("opisPogreske", "Odjava neuspješna!");
        
            return "privatno/izbornik.jsp";
        }
        
        model.put("opisPogreske", "Ne postoji vazeca sjednica za korisnika!");
        
        return "greska.jsp";
    }
}
