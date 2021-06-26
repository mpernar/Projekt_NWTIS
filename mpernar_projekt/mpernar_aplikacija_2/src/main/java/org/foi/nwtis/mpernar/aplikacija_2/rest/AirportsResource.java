package org.foi.nwtis.mpernar.aplikacija_2.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.foi.nwtis.mpernar.aplikacija_2.PristupServeru;
import org.foi.nwtis.mpernar.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mpernar.aplikacija_2.podaci.AirportDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Meteo;

/**
 * Klasa koja sluzi za pruzanje RESTful servisa za aerodrome
 * @author Mario
 */
@Path("aerodromi")
public class AirportsResource {
    
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
      * metoda za dohvacanje podaci o aerodromima
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param nazivAerodroma naziv po kojem se filtriraju aerodromi
     * @param nazivDrzave drzava po kojoj se filtriraju aerodromi
     * @return Response objekt
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrome(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka, @QueryParam("nazivAerodroma") String nazivAerodroma, 
        @QueryParam("nazivDrzave") String nazivDrzave) {
        
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
        
        AirportDAO adao = new AirportDAO();
        List<Aerodrom> aerodromi = adao.dohvatiSveAerodrome(nazivAerodroma, nazivDrzave, pbp);
        return Response
                .status(Response.Status.OK)
                .entity(aerodromi)
                .build();
    }
    
    /**
      * metoda za dohvacanje podataka o odredenom aerodromu
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param icao oznaka aerodroma za koji se dohvacaju podaci
     * @return Response objekt
     */
    @Path("{icao}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrom(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka, @PathParam("icao") String icao) {
        
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
        
        AirportDAO adao = new AirportDAO();
        List<Aerodrom> aerodromi = adao.dohvatiSveAerodrome("", "", pbp);
        Aerodrom aer = null;
        for (int i = 0; i < aerodromi.size(); i++) {
            if (icao.equals(aerodromi.get(i).getIcao())) {
                aer = aerodromi.get(i);
                break;
            }
        }
        
        if (aer == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("Aerodrom nije pronađen!")
                .build();
        }
        
        return Response
                .status(Response.Status.OK)
                .entity(aer)
                .build();
    }
    
    /**
      * metoda za dohvacanje broja letova za odredeni aerodrom na odredeni dan
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param icao oznaka aerodroma za koji se dohvacaju podaci
     * @return Response objekt
     */
    @Path("{icao}/letovi")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajBrojLetova(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka, @PathParam("icao") String icao) {
        
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
        
        AirportDAO adao = new AirportDAO();
        
        String brojLetova = adao.dohvatiBrojLetova(icao, pbp);
        
        if (brojLetova.equals("nije ispravno")) {
            return Response
                .status(Response.Status.OK)
                .entity("Pogreška prilikom dohvaćanja broja letova!")
                .build();
        }
        
        return Response
                .status(Response.Status.OK)
                .entity(brojLetova)
                .build();
    }
    
    /**
     * metoda za dohvacanje letova za odredeni aerodrom na odredeni dan
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param icao oznaka aerodroma za koji se dohvacaju podaci
     * @param dan datum za koji se dohvacaju podaci
     * @return Response objekt
     */
    @Path("{icao}/letovi/{dan}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajLetoveNaDan(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka, @PathParam("icao") String icao, 
        @PathParam("dan") String dan) {
        
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
        
        AirportDAO adao = new AirportDAO();
        
        List<AvionLeti> letovi = adao.dohvatiLetoveNaDan(icao, dan, pbp);
        
        if (letovi == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("Pogreška prilikom dohvaćanja letova!")
                .build();
        }
        
        return Response
                .status(Response.Status.OK)
                .entity(letovi)
                .build();
    }
    
    /**
     * metoda za dohvacanje meteo podataka za odredeni aerodrom na odredeni dan
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param icao oznaka aerodroma za koji se dohvacaju podaci
     * @param dan datum za koji se dohvacaju podaci
     * @return Response objekt
     */
    @Path("{icao}/meteoDan/{dan}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajMeteoZaAerodromDan(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka, @PathParam("icao") String icao, 
        @PathParam("dan") String dan) {
        
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
        
        AirportDAO adao = new AirportDAO();
        
        List<Meteo> podaci = adao.dohvatiMeteoDan(icao, dan, pbp);
        
        return Response
                .status(Response.Status.OK)
                .entity(podaci)
                .build();
    }
    
    /**
     * metoda za dohvacanje meteo podataka za odredeni aerodrom u odredeno vrijeme
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param icao oznaka aerodroma za koji se dohvacaju podaci
     * @param vrijeme vrijeme za koje se dohvacaju podaci
     * @return Response objekt
     */
    @Path("{icao}/meteoVrijeme/{vrijeme}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajMeteoZaAerodromVrijeme(@HeaderParam("korisnik") String korisnik,
        @HeaderParam("lozinka") String lozinka, @PathParam("icao") String icao, 
        @PathParam("vrijeme") long vrijeme) {
        
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
        
        AirportDAO adao = new AirportDAO();
        
        Meteo podaci = adao.dohvatiMeteoVrijeme(icao, vrijeme, pbp);
        
        return Response
                .status(Response.Status.OK)
                .entity(podaci)
                .build();
    }
}
