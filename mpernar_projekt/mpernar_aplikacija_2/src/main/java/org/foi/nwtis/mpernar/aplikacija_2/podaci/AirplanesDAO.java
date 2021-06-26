/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 * Klasa koja sluzi za pristup entitetu airplanes iz baze podataka
 * @author Mario
 */
public class AirplanesDAO {

    /**
     * metoda za dodavanje leta aviona u bazu
     * @param al let koji se mora dodati u bazu
     * @param pbp konfiguracijska datoteka
     * @return true ako je uspješno, inace false
     */
    public boolean dodajAvion(AvionLeti al, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO airplanes (icao24, firstSeen, estDepartureAirport, lastSeen, "
                + "estArrivalAirport, callsign, estDepartureAirportHorizDistance, "
                + "estDepartureAirportVertDistance, estArrivalAirportHorizDistance, "
                + "estArrivalAirportVertDistance, departureAirportCandidatesCount, "
                + "arrivalAirportCandidatesCount, stored) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, al.getIcao24());
                s.setInt(2, al.getFirstSeen());
                s.setString(3, al.getEstDepartureAirport());
                s.setInt(4, al.getLastSeen());
                s.setString(5, al.getEstArrivalAirport());
                s.setString(6, al.getCallsign());
                s.setInt(7, al.getEstDepartureAirportHorizDistance());
                s.setInt(8, al.getEstDepartureAirportVertDistance());
                s.setInt(9, al.getEstArrivalAirportHorizDistance());
                s.setInt(10, al.getEstArrivalAirportVertDistance());
                s.setInt(11, al.getDepartureAirportCandidatesCount());
                s.setInt(12, al.getArrivalAirportCandidatesCount());

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

}
