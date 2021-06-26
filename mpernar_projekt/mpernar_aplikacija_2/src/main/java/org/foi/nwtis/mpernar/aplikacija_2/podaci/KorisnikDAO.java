/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_2.podaci;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.mpernar.aplikacija_2.PristupServeru;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa za pristup entitetu korisnici iz baze podataka
 * @author Mario
 */
public class KorisnikDAO {

    /**
     * metoda za dohvacanje podataka o korisnicima iz polja
     * @param polje polje koje sadrzi informacije o korisnicima
     * @return lista korisnika
     */
    public List<Korisnik> dohvatiSveKorisnike(String[] polje) {

        List<Korisnik> korisnici = new ArrayList<>();

        for (int i = 1; i < polje.length; i++) {
            String[] splitano = PristupServeru.kreirajPoljePodatakaTab(polje[i]);
            String korisnik = splitano[0];
            String ime = splitano[2];
            String prezime = splitano[1];
            
            Korisnik k = new Korisnik(korisnik, prezime, ime, 0);
            
            korisnici.add(k);
        }
        
        return korisnici;

            
    }
}
