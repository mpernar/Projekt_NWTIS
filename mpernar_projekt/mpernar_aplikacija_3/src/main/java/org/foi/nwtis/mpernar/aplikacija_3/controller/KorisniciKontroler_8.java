package org.foi.nwtis.mpernar.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.foi.nwtis.mpernar.aplikacija_3.PristupServeru;
import org.foi.nwtis.mpernar.aplikacija_3.podaci.AerodromiKlijent_2;
import org.foi.nwtis.mpernar.aplikacija_3.podaci.MojiAerodromiKlijent_3;
import org.foi.nwtis.podaci.Aerodrom;

/**
 * Klasa koja sluzi kao kontroler za dodavanje aerodroma
 * @author Mario
 */
@Path("dodajAerodrom")
@Controller
public class KorisniciKontroler_8 {
    
    @Inject
    private Models model;
    
    @Inject
    HttpSession sjednica;
    
    @FormParam("icao")
    String icao;
    
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
     * metoda za dodavanje aerodroma putem rest servisa
     * @return pogled koji ce se vratiti korisniku
     */
    @POST
    public String dodajAerodrom() {

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
            
            MojiAerodromiKlijent_3 mak3 = new MojiAerodromiKlijent_3(korisnik);
            
            AerodromiKlijent_2 ak2 = new AerodromiKlijent_2(icao);
            
            Response aerodrom = ak2.dajAerodrom(Response.class, korisnik, lozinka);
            
            if (aerodrom.getStatusInfo() != Response.Status.OK) {
                model.put("opisObavijesti", "Pogreska prilikom dohvacanja aerodroma!");
                return "privatno/obavijest.jsp";
            }
            
            Aerodrom a = aerodrom.readEntity(Aerodrom.class);
            
            Response res = mak3.dodajAerodrom(a, Response.class, korisnik, lozinka);
            
            if (res.getStatusInfo() == Response.Status.OK) {
                model.put("opisObavijesti", "Korisnik prati aerodrom " + icao + "!");
                return "privatno/obavijest.jsp";
            } else {
                model.put("opisObavijesti", "Korisnik vec prati aerodrom " + icao + "!");
                return "privatno/obavijest.jsp";
            }
        }

        model.put("opisPogreske", "Ne postoji vazeca sjednica za korisnika!");
        return "greska.jsp";
    }
}
