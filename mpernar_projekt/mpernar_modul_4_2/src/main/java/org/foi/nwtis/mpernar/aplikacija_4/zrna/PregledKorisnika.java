package org.foi.nwtis.mpernar.aplikacija_4.zrna;

import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import org.foi.nwtis.mpernar.aplikacija_4.PristupServeru;
import org.foi.nwtis.mpernar.aplikacija_4.ejb.sb.Korisnici;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mpernar.aplikacija_4.slusaci.SlusacAplikacije;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja predstavlja zrno za pregled korisnika
 * @author Mario
 */
@Named(value = "pregledKorisnika")
@ViewScoped
public class PregledKorisnika implements Serializable{

    @EJB
    Korisnici korisnici;
    
    private List<Korisnik> sviKorisnici;
    
    int port;
    String adresa;
    
    @Getter
    private static ServletContext servletContext;
    
    @Inject
    HttpSession sjednica;

    /**
     * konstruktor u kojem se dohvacaju postavke
     */
    public PregledKorisnika() {
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) SlusacAplikacije.getServletContext().getAttribute("Postavke");
        port = Integer.parseInt(pbp.dajPostavku("server.podataka.port"));
        adresa = pbp.dajPostavku("server.podataka.adresa");
    }
    
    /**
     * metoda za autorizaciju korisnika slanjem zahtjeva na app 1
     * @return pogled na koji ce se vratiti korisnik
     */
    public String autoriziraj(){
        
        String korisnik = (String) sjednica.getAttribute("korisnik");
        String lozinka = (String) sjednica.getAttribute("lozinka");
        String idSjednice = (String) sjednica.getAttribute("idSjednice");
        
        String zahtjev = "AUTHOR " + korisnik + " " + idSjednice + " pregledKorisnik";
        System.out.println(zahtjev);
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        System.out.println(odgovor);
        if (odgovor.equals("OK")) {
            return "pregledKorisnika";
        }
        else if (odgovor.equals("ERROR 16 Broj preostalih zahtjeva je 0!")){
            return "error";
        }
        else if (odgovor.equals("ERROR 15 Korisnik nema aktivnu sjednicu.")){
            return "error";
        }
        
        return "obavijest";
    }

    /**
     * metoda koja dohvaca listu korisnika iz beana korisnici
     * @return lista korisnika
     */
    public List<Korisnik> getSviKorisnici() {
        String korisnik = (String) sjednica.getAttribute("korisnik");
        String lozinka = (String) sjednica.getAttribute("lozinka");
        
        this.sviKorisnici = korisnici.dohvatiSveKorisnike(korisnik, lozinka);
        return sviKorisnici;
    }
}