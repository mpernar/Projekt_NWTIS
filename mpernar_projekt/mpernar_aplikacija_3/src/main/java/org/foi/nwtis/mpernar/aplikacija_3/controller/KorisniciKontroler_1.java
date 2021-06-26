package org.foi.nwtis.mpernar.aplikacija_3.controller;

import jakarta.mvc.Controller;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Klasa koja sluzi za preusmjeravanje korisnika na razlicite poglede
 * @author Mario
 */
@Path("korisnik")
@Controller
public class KorisniciKontroler_1 {
    
    /**
     * metoda koja vraca korisnika na pogled za registraciju
     */
    @Path("registracijaKorisnika")
    @GET
    @View("korisnikPodaci.jsp")
    public void registracija() {
        return;
    }
    
    /**
     * metoda koja vraca korisnika na pogled za prijavu
     */
    @Path("prijavaKorisnika")
    @GET
    @View("korisnikPrijava.jsp")
    public void prijava() {
        return;
    }
    
    /**
     * metoda koja vraca korisnika na pogled za slanje komande
     */
    @Path("slanjeKomandi")
    @GET
    @View("privatno/slanjeKomandi.jsp")
    public void komande() {
        return;
    }
    
    /**
     * metoda koja vraca korisnika na pogled s izbornikom
     */
    @Path("izbornik")
    @GET
    @View("privatno/izbornik.jsp")
    public void izbornik() {
        return;
    }
}
