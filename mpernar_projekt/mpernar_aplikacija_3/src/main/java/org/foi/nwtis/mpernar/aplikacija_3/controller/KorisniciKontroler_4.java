package org.foi.nwtis.mpernar.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import org.foi.nwtis.mpernar.aplikacija_3.PristupServeru;
import org.foi.nwtis.mpernar.aplikacija_3.podaci.MojiAerodromiKlijent_1;
import org.foi.nwtis.mpernar.aplikacija_3.podaci.MojiAerodromiKlijent_2;
import org.foi.nwtis.mpernar.aplikacija_3.podaci.MojiAerodromiKlijent_4;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja sluzi kao kontroler za pogled aerodroma
 * @author Mario
 */
@Path("pregledAerodromi")
@Controller
public class KorisniciKontroler_4 {

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
     * metoda za dohvacanje aerodroma preko rest servisa
     * @return pogled na koji ce se vratiti korisnik
     */
    @GET
    public String dohvatiAerodrome() {

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
            
            MojiAerodromiKlijent_1 mak1 = new MojiAerodromiKlijent_1();

            Response mojiAerodromiOdgovor = mak1.dajSveAerodrome(Response.class, korisnik, lozinka);

            if (mojiAerodromiOdgovor.getStatusInfo() != Status.OK) {
                model.put("opisObavijesti", "Pogreska prilikom dohvacanja aerodroma!");
                return "privatno/obavijest.jsp";
            }

            List<Aerodrom> mojiAerodromi = mojiAerodromiOdgovor.readEntity(List.class);
            model.put("mojiAerodromi", mojiAerodromi);

            return "privatno/pregledAerodroma.jsp";
        }

        model.put("opisPogreske", "Ne postoji vazeca sjednica za korisnika!");
        return "greska.jsp";
    }
    
    /**
     * metoda za brisanje aerodroma putem rest servisa
     * @param icao aerodrom koji ce se obrisati
     * @return pogled na koji ce se vratiti korisnik
     */
    @Path("obrisiAerodrom/{icao}")
    @POST
    public String obrisiAerodrom(@PathParam("icao") String icao) {

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
            
            MojiAerodromiKlijent_4 mak4 = new MojiAerodromiKlijent_4(korisnik, icao);

            Response res = mak4.obrisiAerodrom(Response.class, korisnik, lozinka);
            

            if (res.getStatusInfo() == Status.OK) {
                dohvatiAerodrome();
                return "privatno/pregledAerodroma.jsp";
            } else {
                model.put("opisObavijesti", "Korisnik ne prati ovaj aerodrom!");
                return "privatno/obavijest.jsp";
            }
        }

        model.put("opisPogreske", "Ne postoji vazeca sjednica za korisnika!");
        return "privatno/obavijest.jsp";
    }
    
    /**
     * metoda za prikaz korisnika koji prate odredeni aerodrom
     * @param icao aerodrom za koji ce se prikazati korisnici
     * @return pogled na koji ce se vratiti korisnik
     */
    @Path("prikaziKorisnike/{icao}")
    @POST
    public String prikaziKorisnike(@PathParam("icao") String icao) {

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
            
            MojiAerodromiKlijent_2 mak2 = new MojiAerodromiKlijent_2(icao);

            Response korisnici = mak2.dajKorisnike(Response.class, korisnik, lozinka);
            

            if (korisnici.getStatusInfo() != Status.OK) {
                model.put("opisObavijesti", "Problem prilikom dohvacanja korisnika!");
                return "privatno/obavijest.jsp";
            }
            
            List<Korisnik> korisniciZaAerodrom = korisnici.readEntity(List.class);
            model.put("korisniciZaAerodrom", korisniciZaAerodrom);
            
            dohvatiAerodrome();
            return "privatno/pregledAerodroma.jsp";
        }

        model.put("opisPogreske", "Ne postoji vazeca sjednica za korisnika!");
        return "privatno/obavijest.jsp";
    }
}
