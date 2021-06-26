package org.foi.nwtis.mpernar.aplikacija_4.zrna;

import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.mpernar.aplikacija_4.PristupServeru;
import org.foi.nwtis.mpernar.aplikacija_4.ejb.sb.Korisnici;
import org.foi.nwtis.mpernar.aplikacija_4.slusaci.SlusacAplikacije;
import org.foi.nwtis.mpernar.aplikacija_4.wsep.OglasnikPrijave;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

/**
 * Klasa koja predstavlja zrno za prijavu korisnika
 * @author Mario
 */
@Named(value = "prijava")
@ViewScoped
public class PrijavaKorisnika implements Serializable {

    @EJB
    Korisnici korisnici;

    @Inject
    HttpSession sjednica;

    @Getter
    @Setter
    private String korisnickoIme;

    @Getter
    @Setter
    private String lozinka;
    
    @Getter
    private static ServletContext servletContext;
    
    @Inject
    OglasnikPrijave oglasnikPrijave;
     
    int port;
    String adresa;

    /**
     * konstruktor u kojem se dohvacaju postavke
     */
    public PrijavaKorisnika() {
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) SlusacAplikacije.getServletContext().getAttribute("Postavke");
        port = Integer.parseInt(pbp.dajPostavku("server.podataka.port"));
        adresa = pbp.dajPostavku("server.podataka.adresa");
    }
    
    /**
     * metoda za prijavu korisnika
     * @return pogled na koji ce se vratiti korisnika
     */
    public String prijavaKorisnika() {

        if (!korisnickoIme.equals("") && !lozinka.equals("")) {
            String zahtjev = "AUTHEN " + korisnickoIme + " " + lozinka;
            String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);

            if (odgovor.contains("OK")) {
                String[] o = PristupServeru.kreirajPoljePodatakaSpace(odgovor);
                
                sjednica.setAttribute("korisnik", korisnickoIme);
                sjednica.setAttribute("lozinka", lozinka);
                sjednica.setAttribute("idSjednice", o[1]);
                
                
                String poruka = "Korisnik: " + korisnickoIme + ", IdSjednice: " + o[1] + ", Zahtjev: " + zahtjev +  ", Odgovor: " + odgovor + " Aplikacija: aplikacija_4, Akcija: prijava"; 
                oglasnikPrijave.posaljiPoruku(poruka);
                
                return "izbornik";
            } else {
                return "error";
            }
        }
        
        return "error";
    }
}
