package org.foi.nwtis.mpernar.aplikacija_2.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.foi.nwtis.mpernar.aplikacija_2.PristupServeru;
import org.foi.nwtis.mpernar.aplikacija_2.podaci.Dnevnik;
import org.foi.nwtis.mpernar.aplikacija_2.podaci.DnevnikDAO;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

/**
 * Klasa koja sluzi za pruzanje RESTful servisa za dnevnik
 * @author Mario
 */
@Path("dnevnik")
public class DnevnikResource {
    
    @Inject
    ServletContext context;

    int port;
    String adresa;
    
    /**
     * metoda za ucitavanje podataka posluzitelja
     */
    private void ucitajPodatkePosluzitelja(){
        this.port = (int) context.getAttribute("portServera");
        this.adresa = (String) context.getAttribute("adresaServera");
    }

    /**
     * metoda za dodavanje zapisa dnevnika u bazu
     * @param dnevnik zapis koji se dodaje u bazu
     * @return Response objekt
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajZapis(Dnevnik dnevnik) {
        
        ucitajPodatkePosluzitelja();
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        DnevnikDAO ddao = new DnevnikDAO();
        boolean dodan = ddao.dodajZapis(dnevnik, pbp);
        if (!dodan) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Zapis nije dodan!")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("Zapis uspješno dodan!")
                .build();

    }
    
    /**
     * metoda za dohvacanje zapisa odredenog korisnika
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param pKorisnik korisnicko ime korisnika za kojeg se dohvacaju zapisi
     * @return Response objekt
     */
    @Path("{korisnik}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajZapiseKorisnika(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka, @PathParam("korisnik") String pKorisnik) {
        String zahtjev = "AUTHEN " + korisnik + " " + lozinka;
        
        ucitajPodatkePosluzitelja();
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.contains("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        DnevnikDAO ddao = new DnevnikDAO();
        
        List<Dnevnik> zapisi = ddao.dohvatiZapiseKorisnika(pKorisnik, pbp);
        
        return Response
                .status(Response.Status.OK)
                .entity(zapisi)
                .build();
    }
    
    /**
     * metoda za dohvacanje broja zapisa odredenog korisnika
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param pKorisnik korisnicko ime korisnika za kojeg se dohvaca broj zapisa
     * @return Response objekt
     */
    @Path("{korisnik}/broj")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajBrojZapisaKorisnika(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka, @PathParam("korisnik") String pKorisnik) {
        String zahtjev = "AUTHEN " + korisnik + " " + lozinka;
        
        ucitajPodatkePosluzitelja();
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.contains("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        DnevnikDAO ddao = new DnevnikDAO();
        
        String brojZapisa = ddao.dohvatiBrojZapisaKorisnika(pKorisnik, pbp);
        
        return Response
                .status(Response.Status.OK)
                .entity(brojZapisa)
                .build();
    }
}
