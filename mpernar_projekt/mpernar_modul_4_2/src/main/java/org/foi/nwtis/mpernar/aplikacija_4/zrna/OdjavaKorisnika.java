package org.foi.nwtis.mpernar.aplikacija_4.zrna;

import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import lombok.Getter;
import org.foi.nwtis.mpernar.aplikacija_4.PristupServeru;
import org.foi.nwtis.mpernar.aplikacija_4.ejb.sb.Korisnici;
import org.foi.nwtis.mpernar.aplikacija_4.slusaci.SlusacAplikacije;
import org.foi.nwtis.mpernar.aplikacija_4.wsep.OglasnikPrijave;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

/**
 * Klasa koja predstavlja zrno za odjavu korisnika
 * @author Mario
 */
@Named(value = "odjava")
@ViewScoped
public class OdjavaKorisnika implements Serializable {

    @EJB
    Korisnici korisnici;
    
    @Inject
    HttpSession sjednica;
    
    @Getter
    private static ServletContext servletContext;
    
    @Inject
    OglasnikPrijave oglasnikPrijave;
     
    int port;
    String adresa;

    /**
     * konstruktor u kojem se dohvacaju postavke
     */
    public OdjavaKorisnika() {
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) SlusacAplikacije.getServletContext().getAttribute("Postavke");
        port = Integer.parseInt(pbp.dajPostavku("server.podataka.port"));
        adresa = pbp.dajPostavku("server.podataka.adresa");
    }

    /**
     * metoda za odjavu korisnika
     * @return pogled na koji ce se vratiti korisnika
     */
    public String odjaviKorisnika() {
        
        String korisnik = (String) sjednica.getAttribute("korisnik");
        String lozinka = (String) sjednica.getAttribute("lozinka");
        String idSjednice = (String) sjednica.getAttribute("idSjednice");
        
        if (!korisnik.equals("") && !lozinka.equals("")) {
            
            String zahtjev = "LOGOUT " + korisnik + " " + idSjednice;
            
            String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);

            if (odgovor.equals("OK")) {
                
                sjednica.removeAttribute("korisnik");
                sjednica.removeAttribute("lozinka");
                sjednica.removeAttribute("idSjednice");
                
                String poruka = "Korisnik: " + korisnik + ", IdSjednice: " + idSjednice + ", Zahtjev: " + zahtjev +  ", Odgovor: " + odgovor + " Aplikacija: aplikacija_4, Akcija: odjava"; 
                oglasnikPrijave.posaljiPoruku(poruka);
                
                return "prijavaKorisnika";
            } else {
                return "error";
            }
        }
        
        return "obavijest";
    }

}
