package org.foi.nwtis.mpernar.aplikacija_2.slusaci;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mpernar.vjezba_03.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mpernar.aplikacija_2.dretve.PreuzimanjeLetovaAviona;
import org.foi.nwtis.mpernar.aplikacija_2.dretve.PreuzimanjeMeteoPodataka;

/**
 * Klasa koja reprezentira slusaca aplikacije
 * @author Mario
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {
    
    PreuzimanjeLetovaAviona pla;
    PreuzimanjeMeteoPodataka pmp;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
        if (pla != null && pla.isAlive()) {
            pla.interrupt();
        }
        
        if (pmp != null && pmp.isAlive()) {
            pmp.interrupt();
        }
        
        ServletContext servletContext = sce.getServletContext();
        
        servletContext.removeAttribute("Postavke");
        servletContext.removeAttribute("portServera");
        servletContext.removeAttribute("adresaServera");
        System.out.println("Konfiguracija obrisana!");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        
        String putanjaKonfDatoteke = servletContext.getRealPath("WEB-INF")+ File.separator +
                servletContext.getInitParameter("konfiguracija");
        
        PostavkeBazaPodataka pbp = new PostavkeBazaPodataka(putanjaKonfDatoteke);
        
        try {
            pbp.ucitajKonfiguraciju();
            
            int port = Integer.parseInt(pbp.dajPostavku("server.podataka.port"));
            String adresa = pbp.dajPostavku("server.podataka.adresa");
            
            servletContext.setAttribute("portServera", port);
            servletContext.setAttribute("adresaServera", adresa);
            servletContext.setAttribute("Postavke", pbp);
            
            pmp = new PreuzimanjeMeteoPodataka(pbp);
            pmp.start();
            pla = new PreuzimanjeLetovaAviona(pbp);
            pla.start();
            
            System.out.println("Konfiguracija ucitana!");
        } catch (NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
