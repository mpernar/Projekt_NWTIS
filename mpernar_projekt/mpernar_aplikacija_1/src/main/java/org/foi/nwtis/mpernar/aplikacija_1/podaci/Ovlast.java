/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_1.podaci;

import java.util.Objects;

/**
 * Klasa za entitet ovlasti iz baze podataka
 * @author Mario
 */
public class Ovlast {
    public String korisnickoIme;
    public String podrucjeRada;
    public String status;

    /**
     * konstruktor klase ovlast
     * @param korisnickoIme korisnicko ime korisnika
     * @param podrucjeRada podrucje za koje se ovlast dodaje
     * @param status status ovlasti 
     */
    public Ovlast(String korisnickoIme, String podrucjeRada, String status) {
        this.korisnickoIme = korisnickoIme;
        this.podrucjeRada = podrucjeRada;
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.korisnickoIme);
        hash = 89 * hash + Objects.hashCode(this.podrucjeRada);
        hash = 89 * hash + Objects.hashCode(this.status);
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
        final Ovlast other = (Ovlast) obj;
        if (!Objects.equals(this.korisnickoIme, other.korisnickoIme)) {
            return false;
        }
        if (!Objects.equals(this.podrucjeRada, other.podrucjeRada)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }
    
    
}
