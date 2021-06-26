package org.foi.nwtis.mpernar.aplikacija_1;

import org.foi.nwtis.mpernar.aplikacija_1.podaci.Korisnik;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.mpernar.aplikacija_1.podaci.KorisnikDAO;
import org.foi.nwtis.mpernar.aplikacija_1.podaci.Ovlast;
import org.foi.nwtis.mpernar.aplikacija_1.podaci.OvlastDAO;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

/**
 * Klasa koja sluzi za kreiranje dretve zahtjeva koja se koristi prilikom obrade zahtjeva na server podataka.
 * @author Mario
 */
public class DretvaZahtjeva extends Thread {
    
    PostavkeBazaPodataka pbp;
    static int brojDretve = 0;
    Socket uticnica;
    long trajanjeSjednice;
    int maksBrojZahtjeva;

    /**
     * Konstruktor klase DretvaZahtjeva.
     * @param uticnica otvorena utičnica sa zahtjevom.
     * @param trajanjeSjednice broj sekundi koliko traje sjednica
     * @param maksBrojZahtjeva maksimalni broj zahtjeva u sjednici
     * @param pbp konfiguracijske postavke
     */
    public DretvaZahtjeva(Socket uticnica, long trajanjeSjednice, int maksBrojZahtjeva, PostavkeBazaPodataka pbp) {
        this.uticnica = uticnica;
        this.maksBrojZahtjeva = maksBrojZahtjeva;
        this.trajanjeSjednice = trajanjeSjednice;
        this.pbp = pbp;
        this.setName("mpernar_" + brojDretve);
        this.brojDretve++;
    }

    @Override
    public void run() {
        this.obradiZahtjev(uticnica);
        this.brojDretve--;
    }

    /**
     * Obrađuje zahtjev koji dobije na mrežnoj utičnici.
     *
     * @param uticnica otvorena utičnica sa zahtjevom.
     * @return zapis datoteke dnevnika
     */
    public String obradiZahtjev(Socket uticnica) {
        String sintaksa = "^ADD [A-Za-z\\d_-]{3,10} [A-Za-z\\d_#!-]{3,10} \\\"[A-Za-z]+(\\s[A-Za-z]+)*\\\" \\\"[A-Za-z]+(\\s[A-Za-z]+)*\\\"|AUTHEN [A-Za-z\\d_-]{3,10} [A-Za-z\\d_#!-]{3,10}|LOGOUT [A-Za-z\\d_-]{3,10} [\\d]+|GRANT [A-Za-z\\d_-]{3,10} [\\d]+ [A-Za-z]+ [A-Za-z\\d_-]{3,10}|REVOKE [A-Za-z\\d_-]{3,10} [\\d]+ [A-Za-z]+ [A-Za-z\\d_-]{3,10}|RIGHTS [A-Za-z\\d_-]{3,10} [\\d]+ [A-Za-z\\d_-]{3,10}|AUTHOR [A-Za-z\\d_-]{3,10} [\\d]+ [A-Za-z]+|LIST [A-Za-z\\d_-]{3,10} [\\d]+ [A-Za-z\\d_-]{3,10}|LISTALL [A-Za-z\\d_-]{3,10} [\\d]+$";
        
        try ( InputStream is = uticnica.getInputStream();  
              InputStreamReader isr = new InputStreamReader(is,"UTF-8");
              OutputStream os = uticnica.getOutputStream();  
              OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");) {
            
            StringBuilder zahtjev = new StringBuilder();
            while (true) {
                int i = isr.read();
                if (i == -1) {
                    break;
                }
                zahtjev.append((char) i);
            }
            uticnica.shutdownInput();
            
            String[] poljePodataka = kreirajPoljePodataka(zahtjev); 
            String odgovor = izgenerirajOdgovor(poljePodataka, sintaksa, uticnica);
            osw.write(odgovor);
            osw.flush();
            uticnica.shutdownOutput();
            uticnica.close();
            
        } catch (IOException ex) {
            System.out.println("Problem prilikom obrade zahtjeva!");
        }

        return "";
    }

    /**
     * Metoda koja kreira polje iz teksta zahtjeva na nacin da uzima parametar koji je 
     * u navodnicima kao jedan parametar umjesto da ga splita po razmaku
     * @param zahtjev zahtjev koji je pristigao na server podataka
     * @return kreirano polje zahtjeva
     */
    public String[] kreirajPoljePodataka(StringBuilder zahtjev) {
        
        List<String> matchList = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(zahtjev);
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
        } 
        
        String[] polje = new String[matchList.size()];
        for (int i = 0; i < matchList.size(); i++) {
            polje[i] = matchList.get(i);
        }

        return polje;
    }

    /**
     * Metoda za generiranje odgovora servera na zahtjev
     * @param t zahtjev splitan u polje
     * @param sintaksa regex za provjeru naredbe
     * @param uticnica otvorena uticnica sa zahtjevom
     * @return izgeneriran odgovor na zahtjev
     */
    public String izgenerirajOdgovor(String[] t, String sintaksa, Socket uticnica) {
        String odgovor = "";
        
        if (!Provjere.unosProsao(t, sintaksa)){
            odgovor = "ERROR 10 Format komande nije ispravan!";
            return odgovor;
        }

        odgovor = generiranjeOdgovoraNaZahtjev(t);

        return odgovor;
    }

    /**
     * Metoda za generiranje odgovora na zahtjev s obzirom na proslijedeni parametar
     * @param t polje na temelju kojeg se generira odgovor
     * @return izgenerirani odgovor
     */
    public String generiranjeOdgovoraNaZahtjev(String[] t) {
        String odgovor = "";
        Sjednica s;
        switch(t[0]){
            case "ADD":
                if (korisnikPostoji(t)) {
                    return "ERROR 18 Korisnik vec postoji!";
                }
                
                return dodajKorisnika(t);
            case "AUTHEN":
                if (!korisnikAutenticiran(t)) {
                    return "ERROR 11 Nemoguce autenticirati korisnika!";
                }
                
                return kreirajSjednicu(t);
            case "LOGOUT":
                if (!korisnikPostoji(t)) {
                    return "ERROR 18 Korisnik ne postoji!";
                }
                
                 if (!sjednicaAktivna(t)) {
                    return "ERROR 15 Korisnik nema aktivnu sjednicu.";
                }
                 
                return odjaviKorisnika(t);
            case "GRANT":
                if (!korisnikPostoji(t)) {
                    return "ERROR 18 Korisnik ne postoji!";
                }
                
                if (!sjednicaAktivna(t)) {
                    return "ERROR 15 Korisnik nema aktivnu sjednicu.";
                }
                
                s = ServerPodataka.sjednice.get(t[1]);
                if (s.maksBrojZahtjeva == 0) {
                    return "ERROR 16 Broj preostalih zahtjeva je 0!";
                }
                
                ServerPodataka.sjednice.get(t[1]).maksBrojZahtjeva--;
                
                return aktivirajPodrucje(t);
            case "REVOKE":
                if (!korisnikPostoji(t)) {
                    return "ERROR 18 Korisnik ne postoji!";
                }
                
                if (!sjednicaAktivna(t)) {
                    return "ERROR 15 Korisnik nema aktivnu sjednicu.";
                }
                
                s = ServerPodataka.sjednice.get(t[1]);
                if (s.maksBrojZahtjeva == 0) {
                    return "ERROR 16 Broj preostalih zahtjeva je 0!";
                }
                
                ServerPodataka.sjednice.get(t[1]).maksBrojZahtjeva--;
                
                return deaktivirajPodrucje(t);
            case "RIGHTS":
                if (!korisnikPostoji(t)) {
                    return "ERROR 18 Korisnik ne postoji!";
                }
                
                if (!sjednicaAktivna(t)) {
                    return "ERROR 15 Korisnik nema aktivnu sjednicu.";
                }
                
                s = ServerPodataka.sjednice.get(t[1]);
                if (s.maksBrojZahtjeva == 0) {
                    return "ERROR 16 Broj preostalih zahtjeva je 0!";
                }
                
                ServerPodataka.sjednice.get(t[1]).maksBrojZahtjeva--;
                
                return dohvatiPodrucja(t);
            case "AUTHOR":
                if (!korisnikPostoji(t)) {
                    return "ERROR 18 Korisnik ne postoji!";
                }
                
                if (!sjednicaAktivna(t)) {
                    return "ERROR 15 Korisnik nema aktivnu sjednicu.";
                }
                
                s = ServerPodataka.sjednice.get(t[1]);
                if (s.maksBrojZahtjeva == 0) {
                    return "ERROR 16 Broj preostalih zahtjeva je 0!";
                }
                
                ServerPodataka.sjednice.get(t[1]).maksBrojZahtjeva--;
                
                return imaPodrucje(t);
            case "LIST":
                if (!korisnikPostoji(t)) {
                    return "ERROR 18 Korisnik ne postoji!";
                }
                
                if (!sjednicaAktivna(t)) {
                    return "ERROR 15 Korisnik nema aktivnu sjednicu.";
                }
                
                s = ServerPodataka.sjednice.get(t[1]);
                if (s.maksBrojZahtjeva == 0) {
                    return "ERROR 16 Broj preostalih zahtjeva je 0!";
                }
                
                ServerPodataka.sjednice.get(t[1]).maksBrojZahtjeva--;
                
                return dohvatiKorisnika(t);
            case "LISTALL":
                if (!korisnikPostoji(t)) {
                    return "ERROR 18 Korisnik ne postoji!";
                }
                
                if (!sjednicaAktivna(t)) {
                    return "ERROR 15 Korisnik nema aktivnu sjednicu.";
                }
                
                s = ServerPodataka.sjednice.get(t[1]);
                if (s.maksBrojZahtjeva == 0) {
                    return "ERROR 16 Broj preostalih zahtjeva je 0!";
                }
                
                ServerPodataka.sjednice.get(t[1]).maksBrojZahtjeva--;
                
                return dohvatiKorisnike();
        }
        return odgovor;
    }
    
    /**
     * Metoda za autenticiranje korisnika.
     * @param t zahtjev splitan u polje
     * @return true ako je autenticiran false ako nije
     */
    public boolean korisnikAutenticiran(String[] t) {
        return ServerPodataka.korisnici.containsKey(t[1]) && ServerPodataka.korisnici.get(t[1]).lozinka.equals(t[2]);
    }
    
    /**
     * Metoda za kreiranje ili produljivanje sjednice.
     * @param t zahtjev splitan u polje
     * @return odgovor na zahtjev
     */
    public String kreirajSjednicu(String[] t) {
        
        Sjednica s;
        Date datum = new Date();
        long vrijeme = datum.getTime();
        long vrijemeDo = vrijeme + trajanjeSjednice;
        if (ServerPodataka.sjednice.containsKey(t[1])) {
            s = ServerPodataka.sjednice.get(t[1]);
            s.vrijemeKreiranja = vrijeme;
            s.vrijemeDoKadaVrijedi = vrijemeDo;
            s.maksBrojZahtjeva = maksBrojZahtjeva;
        }
        else{
            s = new Sjednica(ServerPodataka.idSjednice++, t[1], vrijeme, vrijemeDo, Sjednica.StatusSjednice.Aktivna, maksBrojZahtjeva);
            ServerPodataka.sjednice.put(s.korisnik, s);
        }
        
        return "OK " + s.id + " " + s.vrijemeDoKadaVrijedi + " " + s.maksBrojZahtjeva;
    }

    

    /**
     * Metoda za provjeravanje ukoliko je sjednica aktivna.
     * @param t zahtjev splitan u polje
     * @return odgovor na zahtjev ovisno o statusu sjednice
     */
    public boolean sjednicaAktivna(String[] t) {
        Sjednica s = null;
        for (String k : ServerPodataka.sjednice.keySet()) {
            if (k.equals(t[1]) && ServerPodataka.sjednice.get(k).id == Integer.parseInt(t[2])) {
                s = ServerPodataka.sjednice.get(k);
            }
        }
        if (s != null) {
            if (s.status == Sjednica.StatusSjednice.Aktivna) {
                if(s.vrijemeDoKadaVrijedi < (new Date()).getTime()){
                    s.status = Sjednica.StatusSjednice.Neaktivna;
                    return false;
                }
                    return true;
                }
                else{
                    return false;
                }
        }
        
        return false;
    }

    /**
     * metoda za provjetu ukoliko korisnik postoji
     * @param t polje zahtjeva
     * @return true ako korisnik postoji, false ako ne
     */
    private boolean korisnikPostoji(String[] t) {
        if (ServerPodataka.korisnici.containsKey(t[1])) {
            return true;
        }
        
        return false;
    }

    /**
     * metoda za dodavanje korisnika u bazu
     * @param t polje zahtjeva
     * @return odgovor s obzirom na ishod dodavanja
     */
    private String dodajKorisnika(String[] t) {
        KorisnikDAO kdao = new KorisnikDAO();
        
        String ime = t[4].substring(1, t[4].length() - 1);
        String prezime = t[3].substring(1, t[3].length() - 1);
        String korime = t[1];
        String lozinka = t[2];
        
        Korisnik k = new Korisnik(ime, prezime, korime, lozinka);
        
        if (kdao.dodajKorisnika(k, pbp)) {
            ServerPodataka.korisnici.put(korime, k);
            return "OK";
        }
        
        return "ERROR 18 Korisnik nije dodan!";
    }

    /**
     * metoda za odjavu korisnika
     * @param t polje zahtjeva
     * @return vraca odgovor "OK"
     */
    private String odjaviKorisnika(String[] t) {
        Sjednica s = ServerPodataka.sjednice.get(t[1]);
        s.vrijemeDoKadaVrijedi = new Date().getTime();
        s.maksBrojZahtjeva = 0;
        ServerPodataka.sjednice.remove(t[1]);
        return "OK";
    }

    /**
     * metoda za aktivaciju područja za određenog korisnika
     * @param t polje zahtjeva
     * @return odgovor ovisno o uspjehu/neuspjehu aktivacije
     */
    private String aktivirajPodrucje(String[] t) {
        Ovlast o = null;
        OvlastDAO odao = new OvlastDAO();
        for (int i = 0; i < ServerPodataka.ovlasti.size(); i++) {
            if (ServerPodataka.ovlasti.get(i).korisnickoIme.equals(t[4])) {
                if (ServerPodataka.ovlasti.get(i).podrucjeRada.equals(t[3])) {
                    o = ServerPodataka.ovlasti.get(i);
                }
            }
        }
        
        if (o == null) {
            o = new Ovlast(t[4], t[3], "AKTIVNO");
            if (odao.dodajOvlast(o, pbp)) {
                ServerPodataka.ovlasti.add(o);
                return "OK";
            }
            
            return "ERROR 18 Ovlast nije dodana!";
        }
        else{
            if (o.status.equals("AKTIVNO")) {
                return "ERROR 13 Podrucje je vec aktivirano!";
            }
            else{
                o.status = "AKTIVNO";
                if (odao.azurirajOvlast(o, pbp)) {
                    
                    return "OK";
                }

                return "ERROR 18 Ovlast nije aktivirana!";
            }
        }
    }

    /**
     * metoda za deaktivaciju područja za određenog korisnika
     * @param t polje zahtjeva
     * @return odgovor ovisno o uspjehu/neuspjehu deaktivacije
     */
    private String deaktivirajPodrucje(String[] t) {
        Ovlast o = null;
        OvlastDAO odao = new OvlastDAO();
        for (int i = 0; i < ServerPodataka.ovlasti.size(); i++) {
            if (ServerPodataka.ovlasti.get(i).korisnickoIme.equals(t[4])) {
                if (ServerPodataka.ovlasti.get(i).podrucjeRada.equals(t[3])) {
                    o = ServerPodataka.ovlasti.get(i);
                }
            }
        }
        
        if (o == null) {
            return "ERROR 14 Podrucje nije aktivirano!";
        }
        else{
            if (o.status.equals("NEAKTIVNO")) {
                return "ERROR 14 Podrucje nije aktivirano!";
            }
            else{
                o.status = "NEAKTIVNO";
                if (odao.azurirajOvlast(o, pbp)) {
                    return "OK";
                }

                return "ERROR 18 Ovlast nije deaktivirana!";
            }
        }
    }

    /**
     * metoda za dohvacanje aktivnih podrucja odredenog korisnika
     * @param t polje zahtjeva
     * @return odgovor ovisno o tome ima li korisnik aktivna podrucja ili ne
     */
    private String dohvatiPodrucja(String[] t) {
        List<String> aktivne = new ArrayList<>();
        for (int i = 0; i < ServerPodataka.ovlasti.size(); i++) {
            if (ServerPodataka.ovlasti.get(i).korisnickoIme.equals(t[3])) {
                if (ServerPodataka.ovlasti.get(i).status.equals("AKTIVNO")) {
                    aktivne.add(ServerPodataka.ovlasti.get(i).podrucjeRada);
                }
            }
        }
        
        if (aktivne.isEmpty()) {
            return "ERROR 14 Korisnik nema aktivnih podrucja!";
        }
        
        String odg = "OK ";
        for (int i = 0; i < aktivne.size(); i++) {
            odg += aktivne.get(i) + " ";
        }
        return odg;
    }

    /**
     * metoda za provjeru ako korisnik ima aktivirano odredeno podrucje
     * @param t polje zahtjeva
     * @return odgovor ovisno ima li ili nema aktivirano podrucje
     */
    private String imaPodrucje(String[] t) {
        for (int i = 0; i < ServerPodataka.ovlasti.size(); i++) {
            if (ServerPodataka.ovlasti.get(i).korisnickoIme.equals(t[1])) {
                Ovlast o = ServerPodataka.ovlasti.get(i);
                if (o.podrucjeRada.equals(t[3]) && o.status.equals("AKTIVNO")) {
                    return "OK";
                }
            }
        }
        
        return "ERROR 14 Podrucje " + t[3] + " nije aktivno za korisnika " + t[1];
    }

    /**
     * metoda za dohvacanje podataka o odredenom korisniku
     * @param t polje zahtjeva
     * @return odgovor ovisno o tome postoji li korisnik ili ne
     */
    private String dohvatiKorisnika(String[] t) {
        if (!ServerPodataka.korisnici.containsKey(t[3])) {
            return "ERROR 17 Nema trazenog korisnika!";
        }
        
        Korisnik k = ServerPodataka.korisnici.get(t[3]);
        
        return "OK \"" + k.korisnickoIme + "\t" + k.prezime + "\t" + k.ime + "\"";
    }

    /**
     * metoda za dohvacanje podataka o svim korisnicima
     * @return odgovor koji sadrzi podatke o svim korisnicima
     */
    private String dohvatiKorisnike() {
        String odg = "OK ";
        for (String k : ServerPodataka.korisnici.keySet()) {
            Korisnik kor = ServerPodataka.korisnici.get(k);
            odg += "\"" + kor.korisnickoIme + "\t" + kor.prezime + "\t" + kor.ime + "\" ";
        }
        
        return odg;
    }
}
