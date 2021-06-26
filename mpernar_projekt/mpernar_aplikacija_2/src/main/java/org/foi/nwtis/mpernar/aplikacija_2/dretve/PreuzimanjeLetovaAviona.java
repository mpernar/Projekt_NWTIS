/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_2.dretve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.aplikacija_2.podaci.AirplanesDAO;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mpernar.aplikacija_2.podaci.MyAirportDAO;
import org.foi.nwtis.mpernar.aplikacija_2.podaci.MyAirportsLogDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 * Klasa dretve za preuzimanje letova aviona
 * @author Mario
 */
public class PreuzimanjeLetovaAviona extends Thread {

    private PostavkeBazaPodataka pbp;
    private OSKlijent osk;
    private String pocetakPreuzimanja;
    private int trajanjeCiklusa;
    private int trajanjePauze;
    private boolean kraj = false;
    private MyAirportsLogDAO maldao = new MyAirportsLogDAO();
    AirplanesDAO adao = new AirplanesDAO();

    public PreuzimanjeLetovaAviona(PostavkeBazaPodataka pbp) {
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

        Date pocetni = dohvatiPocetniDatum();
        System.out.println(pocetni);
        long odVremena = pocetni.getTime() / 1000;
        long doVremena = odVremena + 86_400;
        
        MyAirportDAO madao = new MyAirportDAO();
        
        while (!kraj) {
            System.out.println("Preuzimanje podataka: " + new SimpleDateFormat("dd.MM.yyyy").format(new Date(odVremena * 1000)));
            List<Aerodrom> aerodromi = madao.dohvatiSveAerodrome(pbp);
            try {
                long vrijemePocetka = new Date().getTime();
                for (int i = 0; i < aerodromi.size(); i++) {
                    String icao = aerodromi.get(i).getIcao();
                    if (!maldao.avionDohvacen(icao, odVremena, doVremena, pbp)) {
                        List<AvionLeti> avioni = this.osk.getDepartures(icao, odVremena, doVremena);
                        if (avioni != null) {
                            dodajAvione(icao, odVremena, avioni);
                        }
                    }
                }
                
                Thread.sleep(trajanjePauze);
                long vrijemeKraja = new Date().getTime();
                long trajanje = vrijemeKraja - vrijemePocetka;
                if (trajanje > 300_000) {
                    trajanje = 0;
                }
                
                odVremena += 86_400;
                doVremena += 86_400;
                Thread.sleep(trajanjeCiklusa * 1000 - trajanje);
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzimanjeLetovaAviona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Preuzimanje podataka zavrsilo!");
    }

    /**
     * metoda za dohvacanje pocetnog datuma od kojeg se preuzimaju letovi
     * @return datum pocetka preuzimanja
     */
    private Date dohvatiPocetniDatum() {
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date pocetniDatum = null;
        try {
            pocetniDatum = sdf.parse(this.pocetakPreuzimanja);
        } catch (ParseException ex) {
            Logger.getLogger(PreuzimanjeLetovaAviona.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pocetniDatum;
    }

    @Override
    public synchronized void start() {
        boolean status = Boolean.parseBoolean(pbp.dajPostavku("preuzimanje.aviona.status"));
        if (!status) {
            System.out.println("Ne preuzimam podatke!");
            return;
        }
        this.pocetakPreuzimanja = pbp.dajPostavku("preuzimanje.aviona.pocetak");
        this.trajanjeCiklusa = Integer.parseInt(pbp.dajPostavku("preuzimanje.aviona.ciklus"));
        this.trajanjePauze = Integer.parseInt(pbp.dajPostavku("preuzimanje.aviona.pauza"));
        String korisnik = pbp.dajPostavku("OpenSkyNetwork.korisnik");
        String lozinka = pbp.dajPostavku("OpenSkyNetwork.lozinka");
        this.osk = new OSKlijent(korisnik, lozinka);
        super.start();
    }

    /**
     * metoda za dodavanje letova aviona odredenog aerodroma
     * @param icao oznaka aerodroma
     * @param odVremena vrijeme kada su dodani letovi
     * @param avioni lista letova koja se mora dodati
     */
    private void dodajAvione(String icao, long odVremena, List<AvionLeti> avioni) {

        LocalDate date = LocalDate.ofInstant(Instant.ofEpochSecond(odVremena), ZoneId.systemDefault());
        boolean dodan = false;
        for (int i = 0; i < avioni.size(); i++) {
            AvionLeti avion = avioni.get(i);
            if (avion.getEstArrivalAirport() != null && avion.getEstDepartureAirport() != null 
                    && avion.getCallsign() != null) {
                dodan = adao.dodajAvion(avion, pbp);
            }
        }
        
        if (dodan) {
            maldao.dodajZapisODohvacanju(icao, date, pbp);
        }
        
    }

}
