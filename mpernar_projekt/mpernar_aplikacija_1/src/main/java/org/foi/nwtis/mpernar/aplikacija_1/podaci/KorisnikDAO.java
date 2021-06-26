/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_1.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

/**
 * Klasa za pristupanje entitetu korisnici iz baze podataka
 * @author Mario
 */
public class KorisnikDAO {
    
    /**
     * metoda za dohvacanje korisnika iz baze podataka
     * @param korisnik korisnicko ime korisnika
     * @param lozinka lozinka korisnika
     * @param pbp konfiguracijske postavke
     * @return objekt klase Korisnik
     */
    public Korisnik dohvatiKorisnika(String korisnik, String lozinka, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM korisnici WHERE korisnik = ? and lozinka = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);
                s.setString(2, lozinka);
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String kime = rs.getString("korisnickoIme");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String loz = rs.getString("lozinka");

                    Korisnik k = new Korisnik(ime, prezime, kime, loz);
                    return k;
                }

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * metoda za dohvacanje svih korisnika iz baze podataka
     * @param pbp konfiguracijske postavke
     * @return lista svih korisnika
     */
    public HashMap<String, Korisnik> dohvatiSveKorisnike(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM korisnici";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            HashMap<String, Korisnik> korisnici = new HashMap<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     Statement s = con.createStatement();
                     ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String kime = rs.getString("korisnickoIme");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String loz = rs.getString("lozinka");
                    
                    Korisnik k = new Korisnik(ime, prezime, kime, loz);

                    korisnici.put(k.korisnickoIme, k);
                }
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    

    /**
     * metoda za dodavanje novog korisnika u bazu podataka
     * @param k objekt Korisnik koji ce se dodati u bazu
     * @param pbp konfiguracijske postavke
     * @return true ako je uspjesno, inace false
     */
    public boolean dodajKorisnika(Korisnik k, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO korisnici (ime, prezime, korisnickoIme, lozinka) "
                + "VALUES (?, ?, ?, ?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, k.ime);
                s.setString(2, k.prezime);
                s.setString(3, k.korisnickoIme);
                s.setString(4, k.lozinka);

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
