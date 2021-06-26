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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

/**
 * Klasa za rad s ovlastima u bazi podataka
 * @author Mario
 */
public class OvlastDAO {

    /**
     * metoda za dohvacanje svih ovlasti iz baze podataka
     * @param pbp konfiguracijske postavke
     * @return lista ovlasti
     */
    public List<Ovlast> dohvatiSveOvlasti(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM ovlasti";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Ovlast> ovlasti = new ArrayList<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     Statement s = con.createStatement();
                     ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String kime = rs.getString("korisnickoIme");
                    String pr = rs.getString("podrucjeRada");
                    String status = rs.getString("status");

                    Ovlast o = new Ovlast(kime, pr, status);

                    ovlasti.add(o);
                }
                return ovlasti;

            } catch (SQLException ex) {
                Logger.getLogger(OvlastDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    

    /**
     * metoda za dodavanje nove ovlasti za korisnika
     * @param o ovlast koja se dodaje korisniku
     * @param pbp konfiguracijske postavke
     * @return true ako je uspjesno, inace false
     */
    public boolean dodajOvlast(Ovlast o, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO ovlasti (korisnickoIme, podrucjeRada, status) "
                + "VALUES (?, ?, ?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, o.korisnickoIme);
                s.setString(2, o.podrucjeRada);
                s.setString(3, o.status);

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(OvlastDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * metoda za azuriranje ovlasti za korisnika u bazi
     * @param o ovlast koja se azurira
     * @param pbp konfiguracijske postavke
     * @return true ako je uspjesno, inace false
     */
    public boolean azurirajOvlast(Ovlast o, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "UPDATE ovlasti SET status = ? WHERE korisnickoIme = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, o.status);
                s.setString(2, o.korisnickoIme);

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(OvlastDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
