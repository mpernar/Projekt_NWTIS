/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_2.dretve;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.aplikacija_2.podaci.MeteoDAO;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mpernar.aplikacija_2.podaci.MyAirportDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Meteo;

/**
 * Klasa koja sluzi kao dretva za preuzimanje meteoroloskih podataka za aerodrom
 * @author Mario
 */
public class PreuzimanjeMeteoPodataka extends Thread {

    private PostavkeBazaPodataka pbp;
    private String apiKey;
    private int trajanjeCiklusa;
    private int trajanjePauze;
    private boolean kraj = false;

    /**
     * konstruktor klase koji preuzima konfiguracijske postavke
     * @param pbp konfiguracijske postavke
     */
    public PreuzimanjeMeteoPodataka(PostavkeBazaPodataka pbp) {
        this.pbp = pbp;
    }

    @Override
    public void interrupt() {
        kraj = true;
        super.interrupt();
    }

    @Override
    public void run() {
        System.out.println("Krenuli preuzimati podatke!");
        
        MyAirportDAO madao = new MyAirportDAO();
        MeteoDAO mdao = new MeteoDAO();

        while (!kraj) {
            System.out.println("Preuzimanje podataka: ");
            List<Aerodrom> aerodromi = madao.dohvatiSveAerodrome(pbp);
            
            try {
                long vrijemePocetka = new Date().getTime();
                for (int i = 0; i < aerodromi.size(); i++) {
                    Aerodrom a = aerodromi.get(i);
                    
                    Meteo mp = null;
        
                    OWMKlijent owmk = new OWMKlijent(apiKey);
                    mp = owmk.getRealTimeWeatherOriginal(a.getLokacija().getLatitude(),a.getLokacija().getLongitude());
                    
                    mdao.dodajMeteo(mp, a.getIcao(), pbp);
                    
                }
                
                Thread.sleep(trajanjePauze);
                long vrijemeKraja = new Date().getTime();
                long trajanje = vrijemeKraja - vrijemePocetka;
                if (trajanje > 300_000) {
                    trajanje = 0;
                }
                
                Thread.sleep(trajanjeCiklusa * 1000 - trajanje);
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzimanjeMeteoPodataka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Preuzimanje podataka zavrsilo!");
    }

    @Override
    public synchronized void start() {
        boolean status = Boolean.parseBoolean(pbp.dajPostavku("preuzimanje.meteo.status"));
        if (!status) {
            System.out.println("Ne preuzimam podatke!");
            return;
        }
        this.trajanjeCiklusa = Integer.parseInt(pbp.dajPostavku("preuzimanje.meteo.ciklus"));
        this.trajanjePauze = Integer.parseInt(pbp.dajPostavku("preuzimanje.meteo.pauza"));
        this.apiKey = pbp.dajPostavku("OpenWeatherMap.apikey");
        
        super.start();
    }

}
