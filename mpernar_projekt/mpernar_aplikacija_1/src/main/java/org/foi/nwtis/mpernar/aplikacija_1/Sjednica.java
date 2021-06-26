package org.foi.nwtis.mpernar.aplikacija_1;

import java.util.Objects;

/**
 * Klasa objekta sjednice korisnika.
 * @author Mario
 */
public class Sjednica {
    
    static public enum StatusSjednice {
        Aktivna, Neaktivna
    }
    
    int id;
    String korisnik;
    long vrijemeKreiranja;
    long vrijemeDoKadaVrijedi;
    StatusSjednice status;
    int maksBrojZahtjeva;

    /**
     * Konstruktor klase Sjednica
     * @param id id sjednice
     * @param korisnik korisnicko ime korisnika
     * @param vrijemeKreiranja vrijeme kada je sjednica kreirana
     * @param vrijemeDoKadaVrijedi vrijeme kada sjednica istice
     * @param status status sjednice
     * @param maksBrojZahtjeva maksimalni broj zahtjeva u sjednici
     */
    public Sjednica(int id, String korisnik, long vrijemeKreiranja, long vrijemeDoKadaVrijedi, 
            StatusSjednice status, int maksBrojZahtjeva) {
        this.id = id;
        this.korisnik = korisnik;
        this.vrijemeKreiranja = vrijemeKreiranja;
        this.vrijemeDoKadaVrijedi = vrijemeDoKadaVrijedi;
        this.status = status;
        this.maksBrojZahtjeva = maksBrojZahtjeva;
    }

    @Override
    public String toString() {
        return "Sjednica{" + "id=" + id + ", korisnik=" + korisnik + ", vrijemeKreiranja=" + vrijemeKreiranja + ", vrijemeDoKadaVrijedi=" + vrijemeDoKadaVrijedi + ", status=" + status + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.id;
        hash = 47 * hash + Objects.hashCode(this.korisnik);
        hash = 47 * hash + (int) (this.vrijemeKreiranja ^ (this.vrijemeKreiranja >>> 32));
        hash = 47 * hash + (int) (this.vrijemeDoKadaVrijedi ^ (this.vrijemeDoKadaVrijedi >>> 32));
        hash = 47 * hash + Objects.hashCode(this.status);
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
        final Sjednica other = (Sjednica) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.vrijemeKreiranja != other.vrijemeKreiranja) {
            return false;
        }
        if (this.vrijemeDoKadaVrijedi != other.vrijemeDoKadaVrijedi) {
            return false;
        }
        if (!Objects.equals(this.korisnik, other.korisnik)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        return true;
    }
    
    
}
