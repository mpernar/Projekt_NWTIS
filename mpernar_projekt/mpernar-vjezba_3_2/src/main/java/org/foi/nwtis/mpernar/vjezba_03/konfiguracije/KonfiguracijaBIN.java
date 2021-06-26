package org.foi.nwtis.mpernar.vjezba_03.konfiguracije;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.foi.nwtis.mpernar.vjezba_03.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.mpernar.vjezba_03.konfiguracije.NeispravnaKonfiguracija;

public class KonfiguracijaBIN extends KonfiguracijaApstraktna {

    public KonfiguracijaBIN(String nazivDatoteke) {
        super(nazivDatoteke);
    }

    @Override
    public void ucitajKonfiguraciju(String nazivDatoteke) throws NeispravnaKonfiguracija {
        this.obrisiSvePostavke();

        if (nazivDatoteke == null || nazivDatoteke.length() == 0) {
            throw new NeispravnaKonfiguracija("Nesipravni naziv datoteke: '" + nazivDatoteke + "'");
        }

        File f = new File(nazivDatoteke);
        if (f.exists() && f.isFile()) {

            Object o = null;
            try ( ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                o = ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                throw new NeispravnaKonfiguracija("Problem kod učitavanja daoteke konfiguracije: '" + nazivDatoteke + "'!");
            }

            if (o instanceof Properties) {
                this.postavke = (Properties) o;
            } else {
                throw new NeispravnaKonfiguracija("Problem datoteka: '" + nazivDatoteke + "' ne sadrži serijalizirani objekt tipa Properties!");
            }

        } else {
            throw new NeispravnaKonfiguracija("Datoteka pod nazivom: '" + nazivDatoteke + "' ne postoji ili nije datoteka!");
        }
    }

    @Override
    public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {

        if (datoteka == null || datoteka.length() == 0) {
            throw new NeispravnaKonfiguracija("Nesipravni naziv datoteke: '" + datoteka + "'");
        }

        File f = new File(datoteka);
        if (!f.exists() || (f.exists() && f.isFile())) {
            try ( ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
                oos.writeObject(this.postavke);
            } catch (IOException ex) {
                throw new NeispravnaKonfiguracija("Problem kod spremanja daoteke konfiguracije: '" + datoteka + "'!");
            }
        } else {
            throw new NeispravnaKonfiguracija("Datoteka pod nazivom: '" + datoteka + "' ne postoji ili nije datoteka!");
        }
    }

}
