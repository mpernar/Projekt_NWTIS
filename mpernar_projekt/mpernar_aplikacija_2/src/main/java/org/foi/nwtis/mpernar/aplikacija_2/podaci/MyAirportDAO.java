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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.aplikacija_2.PristupServeru;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 * Klasa za pristup entitetu myairports iz baze podataka
 * @author Mario
 */
public class MyAirportDAO {

    /**
     * metoda koja dohvaca sve aerodrome iz baze podataka
     * @param pbp konfiguracijske postavke
     * @return lista aerodroma
     */
    public List<Aerodrom> dohvatiSveAerodrome(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT DISTINCT a.ident, a.type, a.name, a.elevation_ft, a.continent, a.iso_country, a.iso_region, a.municipality,"
                + "a.gps_code, a.iata_code, a.local_code, a.coordinates FROM airports a, myairports ma "
                + "WHERE a.ident = ma.ident";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Aerodrom> aerodromi = new ArrayList<>();
            
            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     Statement s = con.createStatement();
                     ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String icao = rs.getString("ident");
                    String naziv = rs.getString("name");
                    String drzava = rs.getString("iso_country");
                    String koordinate = rs.getString("coordinates");
                    String[] polje = koordinate.split("\\,");
                    Lokacija lokacija = new Lokacija(polje[1], polje[0]);
                    
                    Aerodrom aer = new Aerodrom(icao, naziv, drzava, lokacija);
                    
                    aerodromi.add(aer);
                }
                
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * metoda za dohvacanje svih korisnika koji prate odredeni aerodrom
     * @param polje polje s podacima o korisnicima
     * @param icao aerodrom za koji zelimo dohvatiti korisnike
     * @param pbp konfiguracijske postavke
     * @return lista korisnika koji prate odredeni aerodrom
     */
    public List<Korisnik> dohvatiKorisnike(String[] polje, String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT username FROM myairports WHERE ident = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Korisnik> korisnici = new ArrayList<>();
            List<String> korisnickaImena = new ArrayList<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                ResultSet rs = s.executeQuery();
                
                while (rs.next()) {
                    String korisnik = rs.getString("username");

                    korisnickaImena.add(korisnik);
                }
                
                for (int i = 1; i < polje.length; i++) {
                    String[] splitano = PristupServeru.kreirajPoljePodatakaTab(polje[i]);
                    String korisnik = splitano[0];
                    String ime = splitano[2];
                    String prezime = splitano[1];

                    if (korisnickaImena.contains(korisnik)) {
                        Korisnik k = new Korisnik(korisnik, prezime, ime, 0);
                        korisnici.add(k);
                    }
                }
                
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * metoda za dohvacanje aerodroma koje prati odredeni korisnik
     * @param korisnik korisnik za kojeg se dohvacaju aerodromi
     * @param pbp konfiguracijske postavke
     * @return lista aerodroma koje prati odredeni korisnik
     */
    public List<Aerodrom> dohvatiMojeAerodrome(String korisnik, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT a.ident, a.type, a.name, a.elevation_ft, a.continent, a.iso_country, a.iso_region, a.municipality,"
                + "a.gps_code, a.iata_code, a.local_code, a.coordinates FROM airports a, myairports ma "
                + "WHERE a.ident = ma.ident AND ma.username = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Aerodrom> aerodromi = new ArrayList<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);
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
                Logger.getLogger(MyAirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * metoda za dodavanje aerodroma u tablicu myairports
     * @param a aerodrom koji se dodaje
     * @param korisnik korisnicko ime korisnika koje se dodaje aerodrom
     * @param pbp konfiguracijske postavke
     * @return true ako je uspjesno, inace false
     */
    public boolean dodajAerodrom(Aerodrom a, String korisnik, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO myairports (ident, username, stored) "
                + "VALUES (?, ?, CURRENT_TIMESTAMP)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, a.getIcao());
                s.setString(2, korisnik);

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * metoda za brisanje aerodroma iz tablice myairports
     * @param icao oznaka aerodroma koji se brise
     * @param korisnik korisnicko ime korisnika kojem se brise aerodrom
     * @param pbp konfiguracijske postavke
     * @return true ako je uspjesno, inace false
     */
    public boolean obrisiAerodrom(String icao, String korisnik, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "DELETE FROM myairports WHERE ident = ? AND username = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                s.setString(2, korisnik);

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
