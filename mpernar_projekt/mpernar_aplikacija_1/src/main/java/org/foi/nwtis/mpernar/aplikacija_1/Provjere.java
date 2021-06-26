package org.foi.nwtis.mpernar.aplikacija_1;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa koja sadrzi metode koje sluze za provjeravanje razicitih stvari
 * @author Mario
 */
public class Provjere {

    /**
     * Metoda a provjeravanje korisnikovog unosa uz pomoc regexa.
     * @param args korisnikov unos
     * @param sintaksa regex
     * @return true ukoliko unos prode provjeru, false ukoliko ne prode
     */
    protected static boolean unosProsao(String[] args, String sintaksa) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                sb.append(args[i]).append(" ");
            }
        }
        String p = sb.toString().trim();
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();

        return status;
    }

    /**
     * Metoda za prvjeravanje postojanje datoteke.
     * @param naziv naziv datoteke koja se provjerava
     * @return true ukoliko datoteka postoji, false ukoliko ne postoji
     */
    protected static boolean postojiDatoteka(String naziv) {

        File f = new File(naziv);
        if (f.exists() && f.isFile()) {
            return true;
        }
        System.out.println("Datoteka " + naziv + " ne postoji!");
        return false;
    }

    /**
     * Metoda za provjeru ukoliko je port zauzet
     * @param port broj porta koji se provjerava
     * @return true ukoliko je port zauzet, false ukoliko nije zauzet
     * @throws IOException
     */
    protected static boolean portZauzet(int port) {

        try {
            
            new ServerSocket(port).close();
            
            return false;
        } catch (IOException e) {
            System.out.println("Racunalna vrata " + port + " su zauzeta!");
            return true;
        }

    }
}
