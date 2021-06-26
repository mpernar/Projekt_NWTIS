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
import java.time.LocalDate;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

/**
 * Klasa za pristup entitetu myairportslog iz baze podataka
 * @author Mario
 */
public class MyAirportsLogDAO {

    /**
     * metoda za dodavanje zapisa o dohvacanju aerodroma
     * @param icao oznaka aerodroma za koji se dodaje zapis
     * @param flightDate datum leta koji se dohvatio
     * @param pbp konfiguracijske postavke
     * @return true ako je uspjesno, inace false
     */
    public boolean dodajZapisODohvacanju(String icao, LocalDate flightDate, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO myairportslog (ident, flightdate, stored) "
                + "VALUES (?, ?, CURRENT_TIMESTAMP)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                s.setDate(2, Date.valueOf(flightDate));

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(AirplanesDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirplanesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * metoda za provjeravanje ako je dohvacen let za odredeni aerodrom u odredenom intervalu
     * @param icao oznaka aerodroma za koji se provjeravaju podaci
     * @param odVremena pocetna vrijednost intervala
     * @param doVremena zavrsna vrijednost intervala
     * @param pbp konfiguracijke postavke
     * @return true ako su podaci vec dohvaceni, inace false
     */
    public boolean avionDohvacen(String icao, long odVremena, long doVremena, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM myairportslog WHERE ident = ? AND flightdate BETWEEN ? AND ?";
        
        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                LocalDate dateFrom = LocalDate.ofInstant(Instant.ofEpochSecond(odVremena), ZoneId.systemDefault());
                LocalDate dateTo = LocalDate.ofInstant(Instant.ofEpochSecond(doVremena), ZoneId.systemDefault());
                
                s.setString(1, icao);
                s.setDate(2,  Date.valueOf(dateFrom));
                s.setDate(3,  Date.valueOf(dateTo));

                ResultSet rs = s.executeQuery();
                Date stored = null;
                
                while (rs.next()) {
                    stored = rs.getDate("stored");
                }
                
                if (stored != null) {
                    return true;
                }

            } catch (SQLException ex) {
                Logger.getLogger(AirplanesDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirplanesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
