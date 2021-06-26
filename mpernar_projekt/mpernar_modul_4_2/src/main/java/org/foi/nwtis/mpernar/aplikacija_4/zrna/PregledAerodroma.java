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
import lombok.Setter;
import org.foi.nwtis.mpernar.aplikacija_4.PristupServeru;
import org.foi.nwtis.mpernar.aplikacija_4.ejb.sb.Aerodromi;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mpernar.aplikacija_4.slusaci.SlusacAplikacije;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Meteo;

/**
 * Klasa koja predstavlja zrno za pregled aerodroma
 * @author Mario
 */
@Named(value = "pregledAerodroma")
@ViewScoped
public class PregledAerodroma implements Serializable{

    @EJB
    Aerodromi aerodromi;
    
    @Getter
    @Setter
    String odabraniAerodrom;
    
    @Getter
    @Setter
    String datum;
    
    @Getter
    @Setter
    String vrijeme;
    
    @Getter
    @Setter
    Float temperatura;
    
    @Getter
    @Setter
    int vlaga;
    
    @Getter
    @Setter
    Float tlak;
    
    @Getter
    @Setter
    Float brzinaVjetra;
    
    @Getter
    @Setter
    int smjerVjetra;

    int port;
    String adresa;
    
    @Inject
    HttpSession sjednica;
    
    @Getter
    private static ServletContext servletContext;
    
    private List<Aerodrom> mojiAerodromi;
    
    private List<AvionLeti> letoviAviona;
    
    private List<Meteo> meteoPodaci;
    
    private Meteo meteoPodatak;

    /**
     * konstruktor u kojem se dohvacaju postavke
     */
    public PregledAerodroma() {
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
        
        String zahtjev = "AUTHOR " + korisnik + " " + idSjednice + " pregledAerodroma";
        
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (odgovor.equals("OK")) {
            return "pregledAerodroma";
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
     * metoda koja dohvaca listu aerodroma koje prati korisnik iz beana aerodromi
     * @return lista aerodroma
     */
    public List<Aerodrom> getMojiAerodromi() {
        String korisnik = (String) sjednica.getAttribute("korisnik");
        String lozinka = (String) sjednica.getAttribute("lozinka");
        
        this.mojiAerodromi = aerodromi.dohvatiMojeAerodrome(korisnik, lozinka);
        return mojiAerodromi;
    }

    /**
     * getter za field letoviAviona
     * @return lista letova
     */
    public List<AvionLeti> getLetoviAviona() {
        return letoviAviona;
    }

    /**
     * getter za field meteoPodaci
     * @return lista meteo podataka
     */
    public List<Meteo> getMeteoPodaci() {
        return meteoPodaci;
    }

    /**
     * getter za field meteoPodatak
     * @return meteo podatak
     */
    public Meteo getMeteoPodatak() {
        return meteoPodatak;
    }
    
    /**
     * metoda koja dohvaca podatke ovisno o upisanim vrijednostima vremena i datuma
     * @return prazan string
     */
    public String dohvatiPodatke(){
        String korisnik = (String) sjednica.getAttribute("korisnik");
        String lozinka = (String) sjednica.getAttribute("lozinka");
        
        if (!datum.equals("")) {
            this.letoviAviona = aerodromi.dohvatiLetove(odabraniAerodrom, datum, korisnik, lozinka);
            this.meteoPodaci = aerodromi.dohvatiMeteoPodatkeDan(odabraniAerodrom, datum, korisnik, lozinka);
        }
        
        if (!vrijeme.equals("")) {
            this.meteoPodatak = aerodromi.dohvatiMeteoPodatkeVrijeme(odabraniAerodrom, vrijeme, korisnik, lozinka);
            this.temperatura = meteoPodatak.getMainTemp();
            this.tlak = meteoPodatak.getMainPressure();
            this.vlaga = meteoPodatak.getMainHumidity();
            this.brzinaVjetra = meteoPodatak.getWindSpeed();
            this.smjerVjetra = meteoPodatak.getWindDeg();
        }
        
        return "";
    }

}