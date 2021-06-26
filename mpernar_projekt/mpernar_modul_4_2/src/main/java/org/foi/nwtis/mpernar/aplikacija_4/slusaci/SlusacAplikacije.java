package org.foi.nwtis.mpernar.aplikacija_4.slusaci;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.KonfiguracijaBP;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mpernar.vjezba_03.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa koja reprezentira slusaca aplikacije
 * @author Mario
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    @Getter
    private static ServletContext servletContext;
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        servletContext.removeAttribute("Postavke");
    }
        

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        
        String nazivDatKonf = servletContext.getRealPath("WEB-INF") + File.separator
                + servletContext.getInitParameter("konfiguracija");
        
        KonfiguracijaBP konfBP = new PostavkeBazaPodataka(nazivDatKonf);
        
        try {    
            konfBP.ucitajKonfiguraciju();
            servletContext.setAttribute("Postavke", konfBP);
        } catch (NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}