/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_4.podaci;

/**
 * Jersey REST client generated for REST resource:aerodromi [aerodromi/{icao}/meteoDan/{dan}]<br>
 * USAGE:
 * <pre>
 *        AerodromiKlijent_5 client = new AerodromiKlijent_5();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Mario
 */
public class AerodromiKlijent_5 {

    private jakarta.ws.rs.client.WebTarget webTarget;
    private jakarta.ws.rs.client.Client client;
    private static final String BASE_URI = "http://localhost:8084/mpernar-aplikacija_2/rest/";

    public AerodromiKlijent_5(String icao, String dan) {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        String resourcePath = java.text.MessageFormat.format("aerodromi/{0}/meteoDan/{1}", new Object[]{icao, dan});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String icao, String dan) {
        String resourcePath = java.text.MessageFormat.format("aerodromi/{0}/meteoDan/{1}", new Object[]{icao, dan});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)@param korisnik header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T dajMeteoZaAerodromDan(Class<T> responseType, String korisnik, String lozinka) throws jakarta.ws.rs.ClientErrorException {
        return webTarget.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnik", korisnik).header("lozinka", lozinka).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
