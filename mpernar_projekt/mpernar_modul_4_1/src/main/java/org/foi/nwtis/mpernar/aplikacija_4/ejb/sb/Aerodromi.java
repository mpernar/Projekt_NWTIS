package org.foi.nwtis.mpernar.aplikacija_4.ejb.sb;

import jakarta.ejb.Stateful;
import jakarta.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.aplikacija_4.podaci.AerodromiKlijent_4;
import org.foi.nwtis.mpernar.aplikacija_4.podaci.AerodromiKlijent_5;
import org.foi.nwtis.mpernar.aplikacija_4.podaci.AerodromiKlijent_6;
import org.foi.nwtis.mpernar.aplikacija_4.podaci.MojiAerodromiKlijent_3;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Meteo;

/**
 * Klasa koja sluzi kao stateful session bean za aerodrome
 * @author Mario
 */
@Stateful
public class Aerodromi {
    
    /**
     * metoda za dohvacanje aerodroma koje prati odredeni korisnik putem rest servisa
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @return lista aerodroma
     */
    public List<Aerodrom> dohvatiMojeAerodrome(String korisnik, String lozinka){
        
        MojiAerodromiKlijent_3 mak3 = new MojiAerodromiKlijent_3(korisnik);
        
        Response r = mak3.dajMojeAerodrome(Response.class, korisnik, lozinka);
        
        if (r.getStatusInfo() != Response.Status.OK) {
                return null;
            }
        
        List<Aerodrom> mojiAerodromi = r.readEntity(List.class);
        
        return mojiAerodromi;
    }
    
    /**
     * metoda za dohvacanje letova odredenog aerodroma na odredeni dan
     * @param icao oznaka aerodroma
     * @param unesenDatum datum za koji se dohvacaju letovi
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @return lista letova 
     */
    public List<AvionLeti> dohvatiLetove(String icao, String unesenDatum, String korisnik, String lozinka){
        
        String datum = pretvoriFormat(unesenDatum);
        
        AerodromiKlijent_4 ak4 = new AerodromiKlijent_4(icao, datum);
        
        Response r = ak4.dajLetoveNaDan(Response.class, korisnik, lozinka);
        
        if (r.getStatusInfo() != Response.Status.OK) {
                return null;
            }
        
        List<AvionLeti> letovi = r.readEntity(List.class);
        
        return letovi;
    }
    
    /**
     * metoda za dohvacanje meteo podataka za odredeni aerodrom na odredeni datum
     * @param icao oznaka aerodroma
     * @param unesenDatum datum za koji se dohvacaju podaci
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @return lista meteo podataka
     */
    public List<Meteo> dohvatiMeteoPodatkeDan(String icao, String unesenDatum, String korisnik, String lozinka){
        
        String datum = pretvoriFormat(unesenDatum);
        
        AerodromiKlijent_5 ak5 = new AerodromiKlijent_5(icao, datum);
        
        Response r = ak5.dajMeteoZaAerodromDan(Response.class, korisnik, lozinka);
        
        if (r.getStatusInfo() != Response.Status.OK) {
                return null;
            }
        
        List<Meteo> meteoPodaci = r.readEntity(List.class);
        
        return meteoPodaci;
    }
    
    /**
     * metoda za dohvacanje meteo podataka za odredeni aerodrom u odredeno vrijeme
     * @param icao oznaka aerodroma
     * @param unesenoVrijeme vrijeme
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @return meteo podaci
     */
    public Meteo dohvatiMeteoPodatkeVrijeme(String icao, String unesenoVrijeme, String korisnik, String lozinka){
        
        String vrijeme = pretvoriFormatVrijeme(unesenoVrijeme);
        
        AerodromiKlijent_6 ak6 = new AerodromiKlijent_6(icao, vrijeme);
        
        Response r = ak6.dajMeteoZaAerodromVrijeme(Response.class, korisnik, lozinka);
        
        if (r.getStatusInfo() != Response.Status.OK) {
                return null;
            }
        
        Meteo meteoPodaci = r.readEntity(Meteo.class);
        
        return meteoPodaci;
    }

    /**
     * metoda za pretvaranje formata datuma iz dd.mm.gggg u gggg-mm-dd
     * @param unesenDatum datum kojem se mijenja format
     * @return datum pretvorenog formata
     */
    private String pretvoriFormat(String unesenDatum) {
        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
        
        Date d = null;
        
        try {
            d = f.parse(unesenDatum);
        } catch (ParseException ex) {
            Logger.getLogger(Aerodromi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        f = new SimpleDateFormat("YYYY-MM-dd");
        
        return f.format(d);
    }

    /**
     * metoda za pretvaranje unesenog vremena u milisekunde
     * @param unesenoVrijeme vrijeme u formatu dd.mm.gggg hh:mm:ss
     * @return broj milisekundi koji predstavlja vrijeme
     */
    private String pretvoriFormatVrijeme(String unesenoVrijeme) {
        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        
        Date d = null;
        
        try {
            d = f.parse(unesenoVrijeme);
        } catch (ParseException ex) {
            Logger.getLogger(Aerodromi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return d.getTime() + "";
    }
}
