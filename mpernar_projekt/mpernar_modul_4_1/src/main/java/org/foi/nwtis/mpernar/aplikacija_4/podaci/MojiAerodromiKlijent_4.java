/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_4.podaci;

/**
 * Jersey REST client generated for REST resource:mojiAerodromi
 * [mojiAerodromi/{korisnik}/prati/{icao}]<br>
 * USAGE:
 * <pre>
 *        MojiAerodromiKlijent_4 client = new MojiAerodromiKlijent_4();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Mario
 */
public class MojiAerodromiKlijent_4 {

    private jakarta.ws.rs.client.WebTarget webTarget;
    private jakarta.ws.rs.client.Client client;
    private static final String BASE_URI = "http://localhost:8084/mpernar-aplikacija_2/rest/";

    public MojiAerodromiKlijent_4(String korisnik, String icao) {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        String resourcePath = java.text.MessageFormat.format("mojiAerodromi/{0}/prati/{1}", new Object[]{korisnik, icao});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String korisnik, String icao) {
        String resourcePath = java.text.MessageFormat.format("mojiAerodromi/{0}/prati/{1}", new Object[]{korisnik, icao});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    /**
     * @param responseType Class representing the response
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T obrisiAerodrom(Class<T> responseType, String korisnik, String lozinka) throws jakarta.ws.rs.ClientErrorException {
        return webTarget.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnik", korisnik).header("lozinka", lozinka).delete(responseType);
    }

    public void close() {
        client.close();
    }
    
}
