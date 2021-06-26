package org.foi.nwtis.mpernar.aplikacija_1;

import org.foi.nwtis.mpernar.aplikacija_1.podaci.Korisnik;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.foi.nwtis.mpernar.aplikacija_1.podaci.KorisnikDAO;
import org.foi.nwtis.mpernar.aplikacija_1.podaci.Ovlast;
import org.foi.nwtis.mpernar.aplikacija_1.podaci.OvlastDAO;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mpernar.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.mpernar.vjezba_03.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.mpernar.vjezba_03.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa koja služi kao poslužitelj mrežnoj utičnici i obrađuje zahtjeve korisnika.
 *
 * @author Mario Pernar
 */
public class ServerPodataka {
    
   
    PostavkeBazaPodataka pbp;
    String nazivDatotekeKonfiguracije;
    int brojDretve;
    int port;
    int maksCekaca;
    int maksDretvi;
    long trajanjeSjednice;
    static int idSjednice = 0;
    static HashMap<String, Korisnik> korisnici = new HashMap<>();
    static List<Ovlast> ovlasti = new ArrayList<>();
    static HashMap<String, Sjednica> sjednice = new HashMap<>();
    int maksBrojZahtjeva;

    public static void main(String[] args) {
        
        String sintaksa = "^([\\w-]+\\.(txt|json|bin|xml))$";
        
        if (Provjere.unosProsao(args, sintaksa)) {
            ServerPodataka serverPodataka = new ServerPodataka();
            serverPodataka.nazivDatotekeKonfiguracije = args[0];
            if (!Provjere.postojiDatoteka(serverPodataka.nazivDatotekeKonfiguracije))return;
            serverPodataka.ucitavanjePostavkiKonfiguracije();
            if (Provjere.portZauzet(serverPodataka.port)) return;
            serverPodataka.ucitajKorisnike();
            serverPodataka.ucitajOvlasti();
            serverPodataka.pokreniServer();
        }
        else{
            System.out.println("Datoteka koju ste zadali nije u ispravnom formatu!");
        }

    }

    /**
     * Učitavanje podataka iz datoteke konfiguracije.
     */
    public void ucitavanjePostavkiKonfiguracije() {
        try {
            Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatotekeKonfiguracije);
            this.port = Integer.parseInt(konf.dajPostavku("port"));
            this.maksCekaca = Integer.parseInt(konf.dajPostavku("maks.cekaca"));
            this.maksDretvi = Integer.parseInt(konf.dajPostavku("maks.dretvi"));
            this.trajanjeSjednice = Long.parseLong(konf.dajPostavku("sjednica.trajanje"));
            this.maksBrojZahtjeva = Integer.parseInt(konf.dajPostavku("maks.zahtjeva"));
            this.pbp = new PostavkeBazaPodataka(nazivDatotekeKonfiguracije);
            pbp.ucitajKonfiguraciju();

        } catch (NeispravnaKonfiguracija ex) {
            
            System.out.println("Problem prilikom ucitavanja postavki konfiguracije!");
        }
    }

    /**
     * Pokreće poslužitelj i čeka na mrežnoj utičnici zahtjeve za obradu.
     */
    public void pokreniServer() {

        try {
            ServerSocket ss = new ServerSocket(this.port, this.maksCekaca);
            while (true) {
                Socket uticnica = ss.accept();
                this.brojDretve = DretvaZahtjeva.brojDretve;
                if (brojDretve < this.maksDretvi) {
                    Thread dretva = new DretvaZahtjeva(uticnica, trajanjeSjednice, maksBrojZahtjeva, pbp);
                    dretva.start();
                }
                else{
                    System.out.println("ERROR 01 Nema slobodne dretve!");
                }
                
            }
        } catch (IOException|IllegalArgumentException ex) {
            System.out.println("Problem prilikom pokretanja servera podataka!");
        }
    }

    /**
     * metoda za učitavanje korisnika iz baze podataka
     */
    private void ucitajKorisnike() {
        KorisnikDAO kdao = new KorisnikDAO();
        
        this.korisnici = kdao.dohvatiSveKorisnike(pbp);
    }

    /**
     * metoda za učitavanje ovlasti iz baze podataka
     */
    private void ucitajOvlasti() {
        OvlastDAO odao = new OvlastDAO();
        
        this.ovlasti = odao.dohvatiSveOvlasti(pbp);
    }
}
