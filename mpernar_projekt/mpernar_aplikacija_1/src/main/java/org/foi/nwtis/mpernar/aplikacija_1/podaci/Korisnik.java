package org.foi.nwtis.mpernar.aplikacija_1.podaci;

import java.util.Objects;

/**
 * Klasa objekta korisnika
 * @author Mario
 */
public class Korisnik {
    public String ime;
    public String prezime;
    public String korisnickoIme;
    public String lozinka;

    /**
     * Konstruktor klase Korisnik
     * @param ime ime korisnika
     * @param prezime prezime korisnika
     * @param korisnickoIme korisnicko ime korisnika
     * @param lozinka lozinka korisnika
     */
    public Korisnik(String ime, String prezime, String korisnickoIme, String lozinka) {
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }

    @Override
    public String toString() {
        return "Korisnik{" + "ime=" + ime + ", prezime=" + prezime + ", korisnickoIme=" + korisnickoIme + ", lozinka=" + lozinka + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.ime);
        hash = 23 * hash + Objects.hashCode(this.prezime);
        hash = 23 * hash + Objects.hashCode(this.korisnickoIme);
        hash = 23 * hash + Objects.hashCode(this.lozinka);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Korisnik other = (Korisnik) obj;
        if (!Objects.equals(this.ime, other.ime)) {
            return false;
        }
        if (!Objects.equals(this.prezime, other.prezime)) {
            return false;
        }
        if (!Objects.equals(this.korisnickoIme, other.korisnickoIme)) {
            return false;
        }
        if (!Objects.equals(this.lozinka, other.lozinka)) {
            return false;
        }
        return true;
    }
    
    
}
