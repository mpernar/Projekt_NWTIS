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
 * Klasa koja sluzi kao kontroler za aktiviranje/deaktiviranje podrucja
 * @author Mario
 */
@Path("podrucja")
@Controller
public class KorisniciKontroler_9 {
    
    @Inject
    private Models model;
    
    @Inject
    HttpSession sjednica;
    
    @FormParam("kimeA")
    String korisnickoImeA;
    
    @FormParam("podrucjeA")
    String podrucjeA;
    
    @FormParam("kimeD")
    String korisnickoImeD;
    
    @FormParam("podrucjeD")
    String podrucjeD;
    
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
     * metoda za aktiviranje podrucja putem zahtjeva na app 1
     * @return pogled koji ce se vratiti korisniku
     */
    @Path("aktiviraj")
    @POST
    public String aktivirajPodrucje() {

        String korisnik = (String) sjednica.getAttribute("korisnik");
        String lozinka = (String) sjednica.getAttribute("lozinka");
        String idSjednice = (String)sjednica.getAttribute("idSjednice");
        
        ucitajPodatkePosluzitelja();

        if (null != korisnik && !korisnik.equals("") && null != lozinka && !lozinka.equals("")) {
            
            String zahtjev = "AUTHOR " + korisnik + " " + idSjednice + " administracijaAerodroma";
        
            String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
            
            if (!odgovor.equals("OK")) {
                if (odgovor.equals("ERROR 16 Broj preostalih zahtjeva je 0!")) {
                    model.put("opisObavijesti", "Korisnik je iskoristio maksimalan broj zahtjeva u sjednici!");
                    return "privatno/obavijest.jsp";
                }
                model.put("opisObavijesti", "Korisnik nije autoriziran za ovo podrucje!");
                return "privatno/obavijest.jsp";
            }
            
            zahtjev = "GRANT " + korisnik + " " + idSjednice + " " + podrucjeA + " " + korisnickoImeA;
            
            odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
            
            if (!odgovor.equals("OK")) {
                if (odgovor.equals("ERROR 16 Broj preostalih zahtjeva je 0!")) {
                    model.put("opisObavijesti", "Korisnik je iskoristio maksimalan broj zahtjeva u sjednici!");
                    return "privatno/obavijest.jsp";
                }
                else if (odgovor.equals("ERROR 13 Podrucje je vec aktivirano!")) {
                    model.put("opisObavijesti", "Korisnik vec ima aktivirano podrucje " + podrucjeA + "!");
                    return "privatno/obavijest.jsp";
                }
            }
            
            model.put("opisObavijesti", "Aktivirano podrucje " + podrucjeA + " za korisnika " + korisnickoImeA + "!");
                    return "privatno/obavijest.jsp";
        }

        model.put("opisPogreske", "Ne postoji vazeca sjednica za korisnika!");
        return "greska.jsp";
    }
    
    /**
     * metoda za deaktiviranje podrucja putem zahtjeva na app 1
     * @return pogled koji ce se vratiti korisniku
     */
    @Path("deaktiviraj")
    @POST
    public String deaktivirajPodrucje() {

        String korisnik = (String) sjednica.getAttribute("korisnik");
        String lozinka = (String) sjednica.getAttribute("lozinka");
        String idSjednice = (String)sjednica.getAttribute("idSjednice");
        
        ucitajPodatkePosluzitelja();

        if (null != korisnik && !korisnik.equals("") && null != lozinka && !lozinka.equals("")) {
            
            String zahtjev = "AUTHOR " + korisnik + " " + idSjednice + " administracijaAerodroma";
        
            String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
            
            if (!odgovor.equals("OK")) {
                if (odgovor.equals("ERROR 16 Broj preostalih zahtjeva je 0!")) {
                    model.put("opisObavijesti", "Korisnik je iskoristio maksimalan broj zahtjeva u sjednici!");
                    return "privatno/obavijest.jsp";
                }
                model.put("opisObavijesti", "Korisnik nije autoriziran za ovo podrucje!");
                return "privatno/obavijest.jsp";
            }
            
            zahtjev = "REVOKE " + korisnik + " " + idSjednice + " " + podrucjeD + " " + korisnickoImeD;
            
            odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
            
            if (!odgovor.equals("OK")) {
                if (odgovor.equals("ERROR 16 Broj preostalih zahtjeva je 0!")) {
                    model.put("opisObavijesti", "Korisnik je iskoristio maksimalan broj zahtjeva u sjednici!");
                    return "privatno/obavijest.jsp";
                }
                else if (odgovor.equals("ERROR 14 Podrucje nije aktivirano!")) {
                    model.put("opisObavijesti", "Korisnik nema aktivirano podrucje " + podrucjeD + "!");
                    return "privatno/obavijest.jsp";
                }
            }
            
            model.put("opisObavijesti", "Deaktivirano podrucje " + podrucjeD + " za korisnika " + korisnickoImeD + "!");
                    return "privatno/obavijest.jsp";
        }

        model.put("opisPogreske", "Ne postoji vazeca sjednica za korisnika!");
        return "greska.jsp";
    }
}
