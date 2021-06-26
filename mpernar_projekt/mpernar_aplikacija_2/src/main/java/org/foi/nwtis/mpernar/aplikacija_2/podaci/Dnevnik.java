/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_2.podaci;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Klasa koja predstavlja entitet dnevnik iz baze podataka
 * @author Mario
 */
public class Dnevnik {

    private String korisnik;
    private Timestamp vrijeme;
    private String zahtjev;
    private String odgovor;
    private String datum;

    public Dnevnik() {
    }

    /**
     * konstruktor klase dnevnik
     * @param korisnik korisnicko ime
     * @param zahtjev zahtjev na server
     * @param odgovor odgovor servera
     */
    public Dnevnik(String korisnik, String zahtjev, String odgovor) {
        this.korisnik = korisnik;
        this.zahtjev = zahtjev;
        this.odgovor = odgovor;
    }

    /**
     * konstruktor klase dnevnik
     * @param korisnik korisnicko ime
     * @param vrijeme vrijeme zahtjeva
     * @param zahtjev zahtjev na server
     * @param odgovor odgovor servera
     */
    public Dnevnik(String korisnik, Timestamp vrijeme, String zahtjev, String odgovor) {
        this.korisnik = korisnik;
        this.vrijeme = vrijeme;
        this.zahtjev = zahtjev;
        this.odgovor = odgovor;
        this.datum = pretvoriUDatum(vrijeme);
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public Timestamp getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Timestamp vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getZahtjev() {
        return zahtjev;
    }

    public void setZahtjev(String zahtjev) {
        this.zahtjev = zahtjev;
    }

    public String getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(String odgovor) {
        this.odgovor = odgovor;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    
    /**
     * metoda koja pretvara timestamp u datum
     * @param vrijeme vrijeme u timestamp obliku
     * @return datum koji reprezentira timestamp vrijednost
     */
    
    private String pretvoriUDatum(Timestamp vrijeme) {
        
        Date date = vrijeme;
        
        SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-dd");
        
        return f.format(date);
    }
}
