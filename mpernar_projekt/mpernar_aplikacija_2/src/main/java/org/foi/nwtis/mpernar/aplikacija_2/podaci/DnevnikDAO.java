/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

/**
 * Klasa koja sluzi za pristupanje entitetu dnevnik iz baze podataka
 * @author Mario
 */
public class DnevnikDAO {

    /**
     * metoda za dodavanje zapisa u bazu
     * @param d zapis koji se dodaje
     * @param pbp konfiguracijske postavke
     * @return true ako je uspjesno, inace false
     */
    public boolean dodajZapis(Dnevnik d, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO dnevnik (korisnik, vrijemePrimitka, sadrzajKomande, sadrzajOdgovora) "
                + "VALUES (?, CURRENT_TIMESTAMP, ?, ?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, d.getKorisnik());
                s.setString(2, d.getZahtjev());
                s.setString(3, d.getOdgovor());

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

    /**
     * metoda za dohvacanje zapisa odredenog korisnika
     * @param pKorisnik korisnik za kojeg se dohvacaju zapisi
     * @param pbp konfiguracijske postavke
     * @return lista zapisa
     */
    public List<Dnevnik> dohvatiZapiseKorisnika(String pKorisnik, PostavkeBazaPodataka pbp) {
        //TODO implementirati ograničenja vremenska i pomak i ostalo
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM dnevnik WHERE korisnik = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Dnevnik> zapisi = new ArrayList<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {
                
                s.setString(1, pKorisnik);

                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    String korisnik = rs.getString("korisnik");
                    Timestamp vrijeme = rs.getTimestamp("vrijemePrimitka");
                    String zahtjev = rs.getString("sadrzajKomande");
                    String odgovor = rs.getString("sadrzajOdgovora");
                    
                    Dnevnik d = new Dnevnik(korisnik, vrijeme, zahtjev, odgovor);

                    zapisi.add(d);
                }
                return zapisi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * metoda za dohvacanje broja zapisa odredenog korisnika
     * @param pKorisnik korisnik za kojeg se dohvaca broj zapisa
     * @param pbp konfiguracijske postavke
     * @return broj zapisa
     */
    public String dohvatiBrojZapisaKorisnika(String pKorisnik, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT COUNT(*) AS brojZapisa FROM dnevnik WHERE korisnik = ?";
        
        String brojZapisa = "nije ispravno";
        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {
                
                s.setString(1, pKorisnik);
                
                ResultSet rs = s.executeQuery();
                while (rs.next()) {                    
                    int broj = rs.getInt("brojZapisa");
                    brojZapisa = broj + "";
                }
                

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return brojZapisa;
    }
}
