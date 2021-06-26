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
import org.foi.nwtis.mpernar.aplikacija_3.wsep.OglasnikPrijave;

/**
 * Klasa koja sluzi kao kontroler za prijavu
 * @author Mario
 */
@Path("korisnikPrijava")
@Controller
public class KorisniciKontroler_3 {

    @FormParam("korisnik")
    String korisnik;
    
    @FormParam("lozinka")
    String lozinka;
    
    @Inject
    HttpSession sjednica;
    
    @Inject
    private Models model;
    
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
     * metoda za prijavu korisnika
     * @return pogled na koji ce se vratiti korisnik
     */
    @POST
    public String prijava() {
        
        ucitajPodatkePosluzitelja();
        
        if (!korisnik.equals("") && !lozinka.equals("")) {
            String zahtjev = "AUTHEN " + korisnik + " " + lozinka;
        
            String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);

            if (odgovor.contains("OK")) {
                String[] o = PristupServeru.kreirajPoljePodatakaSpace(odgovor);
                sjednica.setAttribute("korisnik", korisnik);
                sjednica.setAttribute("lozinka", lozinka);
                sjednica.setAttribute("idSjednice", o[1]);
                
                String poruka = "Korisnik: " + korisnik + ", IdSjednice: " + o[1] + ", Zahtjev: " + zahtjev +  ", Odgovor: " + odgovor + " Aplikacija: aplikacija_3, Akcija: prijava"; 
                oglasnikPrijave.posaljiPoruku(poruka);
                
                return "privatno/izbornik.jsp";
            }
            else{
                model.put("opisPogreske", "Pogresni podaci prilikom prijave!");
                return "greska.jsp";
            }
        }
        else{
            model.put("opisPogreske", "Polja za unos su prazna!");
                return "greska.jsp";
        }
    }
}
