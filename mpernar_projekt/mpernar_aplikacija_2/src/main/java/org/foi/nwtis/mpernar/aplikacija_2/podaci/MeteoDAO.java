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
import org.foi.nwtis.rest.podaci.Meteo;

/**
 * Klasa za pristup entitetu meteo iz baze podataka
 * @author Mario
 */
public class MeteoDAO {

    /**
     * metoda za dodavanje meteo podataka u bazu
     * @param m meteo podaci koji ce se dodati u bazu
     * @param ident oznaka aerodroma za koji se dodaju podaci
     * @param pbp konfiguracijske postavke
     * @return true ako je uspjesno, inace false
     */
    public boolean dodajMeteo(Meteo m, String ident, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO meteo (temperatura, vlaga, tlak, brzinaVjetra, smjerVjetra, ident, stored) "
                + "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setDouble(1, m.getMainTemp());
                s.setDouble(2, m.getMainHumidity());
                s.setDouble(3, m.getMainPressure());
                if (m.getWindSpeed() == null) {
                    s.setDouble(4, 0);
                }
                else{
                    s.setDouble(4, m.getWindSpeed());
                }
                if (m.getWindDeg() == null) {
                    s.setDouble(5, 0);
                }
                else{
                    s.setDouble(5, m.getWindDeg());
                }
                s.setString(6, ident);

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
