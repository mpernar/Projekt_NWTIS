package org.foi.nwtis.mpernar.vjezba_03.konfiguracije;

import java.io.File;
import java.io.IOException;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class KonfiguracijaJSON extends KonfiguracijaApstraktna {

    public KonfiguracijaJSON(String nazivDatoteke) {
        super(nazivDatoteke);
    }

    @Override
    public void ucitajKonfiguraciju(String nazivDatoteke) throws NeispravnaKonfiguracija {
        //TODO sami
        this.obrisiSvePostavke();

        if (nazivDatoteke == null || nazivDatoteke.length() == 0) {
            throw new NeispravnaKonfiguracija("Neispravan naziv datoteke: " + nazivDatoteke);
        }

        File f = new File(nazivDatoteke);
        Gson gson = new Gson();
        if (f.exists() && f.isFile()) {
            try(BufferedReader reader = new BufferedReader(new FileReader(f));) {
                
                this.postavke = gson.fromJson(reader, Properties.class);
                
            } catch (IOException ex) {
                throw new NeispravnaKonfiguracija("Problem kod učitavanja datoteke konfiguracije: " + nazivDatoteke);
            }
        }
    }

    @Override
    public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {
        //TODO sami
        if (datoteka == null || datoteka.length() == 0) {
            throw new NeispravnaKonfiguracija("Neispravan naziv datoteke: " + datoteka);
        }

        File f = new File(datoteka);
        Gson gson = new Gson();

        if (!f.exists() || f.exists() && f.isFile()) {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(f));) {

                String json = gson.toJson(this.postavke);
                writer.write(json);

            } catch (IOException ex) {
                throw new NeispravnaKonfiguracija("Problem kod spremanja datoteke konfiguracije: " + datoteka);
            }
        }
    }

}
