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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.Meteo;

/**
 * Klasa koja sluzi za pristup entitetu airports iz baze podataka
 * @author Mario
 */
public class AirportDAO {

    /**
     * metoda za dohvacanje svih aerodroma iz baze podataka koji se mogu filtrirati po nazivu aerodroma i drzavi
     * @param nazivAerodroma naziv aerodroma za filtriranje
     * @param nazivDrzave drzava za filtriranje
     * @param pbp konfiguracijske postavke
     * @return lista aerodroma
     */
    public List<Aerodrom> dohvatiSveAerodrome(String nazivAerodroma, String nazivDrzave, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT ident, type, name, elevation_ft, continent, iso_country, iso_region, municipality,"
                + "gps_code, iata_code, local_code, coordinates FROM airports";

        if(nazivAerodroma != null && !nazivAerodroma.equals("") && nazivDrzave != null && !nazivDrzave.equals("")){
            upit += " WHERE name LIKE ? AND iso_country = ?";
        }
        else if (nazivAerodroma != null && !nazivAerodroma.equals("")) {
            upit += " WHERE name LIKE ?";
        }
        else if (nazivDrzave != null && !nazivDrzave.equals("")) {
            upit += " WHERE iso_country = ?";
        }
        
        
        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Aerodrom> aerodromi = new ArrayList<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit);
                     ) {

                if(nazivAerodroma != null && !nazivAerodroma.equals("") && nazivDrzave != null && !nazivDrzave.equals("")){
                    s.setString(1, nazivAerodroma);
                    s.setString(2, nazivDrzave);
                }
                else if (nazivAerodroma != null && !nazivAerodroma.equals("")) {
                    s.setString(1, nazivAerodroma);
                }
                else if (nazivDrzave != null && !nazivDrzave.equals("")) {
                    s.setString(1, nazivDrzave);
                }
                
                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    String icao = rs.getString("ident");
                    String naziv = rs.getString("name");
                    String drzava = rs.getString("iso_country");
                    String koordinate = rs.getString("coordinates");
                    String[] polje = koordinate.split("\\,");
                    Lokacija lokacija = new Lokacija(polje[1], polje[0]);
                    Aerodrom a = new Aerodrom(icao, naziv, drzava, lokacija);

                    aerodromi.add(a);
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * metoda za dohvacanje broja letova na odredenom aerodromu
     * @param icao oznaka aerodroma za koji se dohvaca
     * @param pbp konfiguracijske postavke
     * @return broj letova
     */
    public String dohvatiBrojLetova(String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT COUNT(*) AS brojLetova FROM airplanes WHERE estdepartureairport = ?";

        
        String brojLetova = "nije ispravno";
        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {
                
                s.setString(1, icao);
                
                ResultSet rs = s.executeQuery();
                while (rs.next()) {                    
                    int broj = rs.getInt("brojLetova");
                    brojLetova = broj + "";
                }
                

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return brojLetova;
    }

    /**
     * metoda za dohvacanje informacija o letovima s odredenog aerodroma na odredeni dan
     * @param icao oznaka aerodroma za koji se dohvacaju letovi
     * @param dan datum za koji se dohvacaju letovi
     * @param pbp konfiguracijske postavke
     * @return lista letova aviona
     */
    public List<AvionLeti> dohvatiLetoveNaDan(String icao, String dan, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM airplanes WHERE estdepartureairport = ? AND date(FROM_UNIXTIME(firstSeen)) = ?";

        List<AvionLeti> letovi = new ArrayList<>();
        
        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {
                
                s.setString(1, icao);
                s.setString(2, dan);
                
                
                ResultSet rs = s.executeQuery();
                while (rs.next()) {   
                    String ident = rs.getString("icao24");
                    int firstSeen = rs.getInt("firstSeen");
                    String estDepartureAirport = rs.getString("estDepartureAirport");
                    String estArrivalAirport = rs.getString("estArrivalAirport");
                    String callsign = rs.getString("callsign");
                    int lastSeen = rs.getInt("lastSeen");
                    int estDepartureAirportHorizDistance = rs.getInt("estDepartureAirportHorizDistance");
                    int estDepartureAirportVertDistance = rs.getInt("estDepartureAirportVertDistance");
                    int estArrivalAirportHorizDistance = rs.getInt("estArrivalAirportHorizDistance");
                    int estArrivalAirportVertDistance = rs.getInt("estArrivalAirportVertDistance");
                    int departureAirportCandidatesCount = rs.getInt("departureAirportCandidatesCount");
                    int arrivalAirportCandidatesCount = rs.getInt("arrivalAirportCandidatesCount");
                    
                    AvionLeti let = new AvionLeti(ident, firstSeen, estDepartureAirport, lastSeen, 
                            estArrivalAirport, callsign, estDepartureAirportHorizDistance, 
                            estDepartureAirportVertDistance, estArrivalAirportHorizDistance, 
                            estArrivalAirportVertDistance, departureAirportCandidatesCount, 
                            arrivalAirportCandidatesCount);
                    
                    letovi.add(let);
                }
                
                return letovi;
            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * metoda za dohvacanje meteo podataka s odredenog aerodroma na odredeni dan
     * @param icao oznaka aerodroma za koji se dohvacaju meteo podaci
     * @param dan datum za koji se dohvacaju meteo podaci
     * @param pbp konfiguracijske postavke
     * @return lista meteoroloskih podataka
     */
    public List<Meteo> dohvatiMeteoDan(String icao, String dan, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM meteo WHERE ident = ? AND date(stored) = ?";

        
        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Meteo> meteoPodaci = new ArrayList<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit);) {

                s.setString(1, icao);
                s.setString(2, dan);
                
                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    float temperatura = rs.getFloat("temperatura");
                    Integer vlaga = rs.getInt("vlaga");
                    float tlak = rs.getFloat("tlak");
                    Integer smjerVjetra = rs.getInt("smjerVjetra");
                    float brzinaVjetra = rs.getFloat("brzinaVjetra");
                    String ident = rs.getString("ident");
                    
                    Meteo m = new Meteo();
                    m.setMainTemp(temperatura);
                    m.setMainHumidity(vlaga);
                    m.setMainPressure(tlak);
                    m.setWindSpeed(brzinaVjetra);
                    m.setWindDeg(smjerVjetra);
                    m.setIdent(ident);

                    meteoPodaci.add(m);
                }
                return meteoPodaci;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * metoda za dohvacanje meteo podataka s odredenog aerodroma u odredeno vrijeme
     * @param icao oznaka aerodroma za koji se dohvacaju meteo podaci
     * @param vrijeme vrijeme za koje se dohvacaju meteo podaci
     * @param pbp konfiguracijske postavke
     * @return meteo podaci za to vrijeme
     */
    public Meteo dohvatiMeteoVrijeme(String icao, long vrijeme, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM meteo WHERE ident = ? AND stored >= ?";

        
        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Meteo> meteoPodaci = new ArrayList<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit);) {

                Timestamp t = new Timestamp(vrijeme);
                
                s.setString(1, icao);
                s.setTimestamp(2, t);
                
                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    float temperatura = rs.getFloat("temperatura");
                    Integer vlaga = rs.getInt("vlaga");
                    float tlak = rs.getFloat("tlak");
                    Integer smjerVjetra = rs.getInt("smjerVjetra");
                    float brzinaVjetra = rs.getFloat("brzinaVjetra");
                    String ident = rs.getString("ident");
                    
                    Meteo m = new Meteo();
                    m.setMainTemp(temperatura);
                    m.setMainHumidity(vlaga);
                    m.setMainPressure(tlak);
                    m.setWindSpeed(brzinaVjetra);
                    m.setWindDeg(smjerVjetra);
                    m.setIdent(ident);

                    meteoPodaci.add(m);
                }
                return meteoPodaci.get(0);

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
