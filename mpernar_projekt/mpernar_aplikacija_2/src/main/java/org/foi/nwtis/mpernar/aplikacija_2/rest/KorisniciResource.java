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
import org.foi.nwtis.mpernar.aplikacija_2.podaci.KorisnikDAO;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja sluzi za pruzanje RESTful servisa za korisnike
 * @author Mario
 */
@Path("korisnici")
public class KorisniciResource {

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
     * metoda za dohvacanje korisnika
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @return Response objekt
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnike(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {
        
        ucitajPodatkePosluzitelja();
        
        KorisnikDAO kdao = new KorisnikDAO();
        
        String zahtjev = "AUTHEN " + korisnik + " " + lozinka;
        
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.contains("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        String[] poljeOdgovora = PristupServeru.kreirajPoljePodatakaSpace(odgovor);
        
        zahtjev = "LISTALL " + korisnik + " " + poljeOdgovora[1];
        
        odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        poljeOdgovora = PristupServeru.kreirajPoljePodatakaSpace(odgovor);
        
        
        List<Korisnik> korisnici = kdao.dohvatiSveKorisnike(poljeOdgovora);
        
        return Response
                .status(Response.Status.OK)
                .entity(korisnici)
                .build();
    }

    /**
     * metoda za dodavanje novog korisnika
     * @param noviKorisnik novi korisnik koji ce se dodati
     * @return Response objekt
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajKorisnika(Korisnik noviKorisnik) {
        
        ucitajPodatkePosluzitelja();
        
        String zahtjev = "ADD " + noviKorisnik.getKorisnik() + " " + noviKorisnik.getLozinka() + " \""
                + noviKorisnik.getPrezime() + "\" \"" + noviKorisnik.getIme()+ "\"";
        
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Korisnik nije dodan!")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("Korisnik uspješno dodan!")
                .build();

    }

    /**
     * metoda za dohvacanje podataka o odredenom korisniku
     * @param korisnik korisnicko ime
     * @param lozinka lozinka
     * @param pKorisnik korisnicko ime korisnika za kojeg se traze podaci
     * @return Response objekt
     */
    @Path("{korisnik}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnika(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("korisnik") String pKorisnik) {
        
        ucitajPodatkePosluzitelja();
        
        String zahtjev = "AUTHEN " + korisnik + " " + lozinka;
        
        String odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.contains("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        String[] poljeOdgovora = PristupServeru.kreirajPoljePodatakaSpace(odgovor);
        
        zahtjev = "LIST " + korisnik + " " + poljeOdgovora[1] + " " + pKorisnik;
        
        odgovor = PristupServeru.posaljiZahtjev(zahtjev, adresa, port);
        
        if (!odgovor.contains("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Korisnik nije pronađen!")
                    .build();
        }
        
        poljeOdgovora = PristupServeru.kreirajPoljePodatakaSpace(odgovor);
        
        Korisnik kor = dohvatiKorisnika(poljeOdgovora);
        
        return Response
                .status(Response.Status.OK)
                .entity(kor)
                .build();
    }

    /**
     * metoda za dohvacanje korisnika iz odgovora servera podataka
     * @param poljeOdgovora polje odgovora
     * @return objekt korisnika
     */
    private Korisnik dohvatiKorisnika(String[] poljeOdgovora) {
        String[] splitano = PristupServeru.kreirajPoljePodatakaTab(poljeOdgovora[1]);
        
        return new Korisnik(splitano[0], splitano[1], splitano[2], 0);
    }

}
