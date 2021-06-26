package org.foi.nwtis.mpernar.aplikacija_2.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mpernar.aplikacija_2.podaci.MyAirportDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja sluzi za pruzanje RESTful servisa za moje aerodrome
 * @author Mario
 */
@Path("mojiAerodromi")
public class MyAirportsResource {
    
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
     * metoda za dohvacanje svih aerodroma koje prati bar jedan korisnik
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @return Response objekt
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajSveAerodrome(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka) {
        
        ucitajPodatkePosluzitelja();
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        String zahtjev = "AUTHEN " + korisnik + " " + lozinka;
        
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.contains("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        MyAirportDAO madao = new MyAirportDAO();
        List<Aerodrom> aerodromi = madao.dohvatiSveAerodrome(pbp);
        return Response
                .status(Response.Status.OK)
                .entity(aerodromi)
                .build();
    }
    
    /**
     * metoda za dohvacanje korisnika koji prate odredeni aerodrom
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param icao oznaka aerodroma za koji se dohvacaju korisnici
     * @return Response objekt
     */
    @Path("{icao}/prate")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnike(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka,
        @PathParam("icao") String icao) {
        
        ucitajPodatkePosluzitelja();
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        String zahtjev = "AUTHEN " + korisnik + " " + lozinka;
        
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.contains("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        MyAirportDAO madao = new MyAirportDAO();
        String[] poljeOdgovora = PristupServeru.kreirajPoljePodatakaSpace(odgovor);
        
        zahtjev = "LISTALL " + korisnik + " " + poljeOdgovora[1];
        
        odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        poljeOdgovora = PristupServeru.kreirajPoljePodatakaSpace(odgovor);
        
        List<Korisnik> korisnici = madao.dohvatiKorisnike(poljeOdgovora, icao, pbp);
        
        return Response
                .status(Response.Status.OK)
                .entity(korisnici)
                .build();
    }
    
    /**
     * metoda za dohvacanje aerodroma koje prati odredeni korisnik
     * @param korisnik korisnicko ime 
     * @param lozinka lozinka
     * @param pKorisnik korisnicko ime korisnika za kojeg se dohvacaju aerodromi
     * @return Response objekt
     */
    @Path("{korisnik}/prati")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajMojeAerodrome(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka,
        @PathParam("korisnik") String pKorisnik) {
        
        ucitajPodatkePosluzitelja();
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        String zahtjev = "AUTHEN " + korisnik + " " + lozinka;
        
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.contains("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        MyAirportDAO madao = new MyAirportDAO();
        
        List<Aerodrom> mojiAerodromi = madao.dohvatiMojeAerodrome(pKorisnik, pbp);
        return Response
                .status(Response.Status.OK)
                .entity(mojiAerodromi)
                .build();
    }
    
    /**
     * metoda za dodavanje aerodroma korisniku da ga prati
     * @param aerodrom aerodrom koji se dodaje
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param pKorisnik korisnicko ime korisnika kojem se dodaje aerodrom
     * @return Response objekt
     */
    @Path("{korisnik}/prati")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajAerodrom(Aerodrom aerodrom, @HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka, @PathParam("korisnik") String pKorisnik) {
        
        ucitajPodatkePosluzitelja();
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        String zahtjev = "AUTHEN " + korisnik + " " + lozinka;
        
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.contains("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        MyAirportDAO madao = new MyAirportDAO();
        boolean dodan = madao.dodajAerodrom(aerodrom, pKorisnik, pbp);
        if (!dodan) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Aerodrom nije dodan!")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("Aerodrom uspješno dodan!")
                .build();

    }
    
    /**
     * metoda za brisanje aerodroma odredenom korisniku
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param icao oznaka aerodroma koji se brise
     * @param pKorisnik korisnicko ime korisnika kojem se brise aerodrom
     * @return Response objekt
     */
    @Path("{korisnik}/prati/{icao}")
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response obrisiAerodrom(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka, @PathParam("korisnik") String pKorisnik, 
            @PathParam("icao") String icao) {
        
        ucitajPodatkePosluzitelja();
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        String zahtjev = "AUTHEN " + korisnik + " " + lozinka;
        
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.contains("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        MyAirportDAO madao = new MyAirportDAO();
        boolean obrisan = madao.obrisiAerodrom(icao, pKorisnik, pbp);
        if (!obrisan) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Aerodrom nije obrisan!")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("Aerodrom uspješno obrisan s liste!")
                .build();

    }
    
    
}
