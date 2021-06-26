/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_4;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa koja sluzi kao pristup na server podataka
 * @author Mario
 */
public class PristupServeru {
    
    
    /**
     * Metoda za slanje zahtjeva na server.
     * @param zahtjev zahtjev na server
     * @param adresa adresa servera
     * @param port port servera
     * @return odgovor servera
     */
    public static String posaljiZahtjev(String zahtjev, String adresa, int port) {

        try ( Socket uticnica = new Socket(adresa, port);  
              InputStream is = uticnica.getInputStream();  
              InputStreamReader isr = new InputStreamReader(is,"UTF-8");
              OutputStream os = uticnica.getOutputStream();  
              OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");) {
            
            StringBuilder text = new StringBuilder();

            osw.write(zahtjev);
            osw.flush();
            uticnica.shutdownOutput();

            while (true) {
                int i = isr.read();
                if (i == -1) {
                    break;
                }
                text.append((char) i);
            }
            uticnica.shutdownInput();
            uticnica.close();
            return text.toString();
        } catch (IOException ex) {
            return "ERROR 18 ServerPodataka ne radi!";
        }
    }
    
    /**
     * metoda koja iz zahtjeva kreira polje
     * @param zahtjev sadrzaj zahtjeva
     * @return polje vrijednosti u zahtjevu razdvojeno po spaceu
     */
    public static String[] kreirajPoljePodatakaSpace(String zahtjev) {
        
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
     * metoda koja iz zahtjeva kreira polje
     * @param zahtjev sadrzaj zahtjeva
     * @return polje vrijednosti u zahtjevu razdvojeno po tabu
     */
    public static String[] kreirajPoljePodatakaTab(String zahtjev) {
        
        String[] polje = zahtjev.split("\\\"");
        
        return polje[1].split("\\t");
    }
}
