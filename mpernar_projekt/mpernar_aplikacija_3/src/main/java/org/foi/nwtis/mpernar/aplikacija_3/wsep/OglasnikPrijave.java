/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpernar.aplikacija_3.wsep;

import jakarta.annotation.PostConstruct;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa koja sluzi kao klijentov endpoint za pristup web socketu login
 * @author Mario
 */
@ClientEndpoint()
public class OglasnikPrijave {

    private Session session;

    /**
     * metoda koja sluzi za spajanje na web socket login
     */
    @PostConstruct
    public void spajanje() {
        String uri = "ws://localhost:8380/mpernar_modul_4_2/login";
        
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI(uri));
        } catch (URISyntaxException | DeploymentException | IOException ex) {
            Logger.getLogger(OglasnikPrijave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * metoda koja sluzi za slanje poruke na web socket login
     * @param poruka poruka koja se salje
     */
    public void posaljiPoruku(String poruka){
        try {
            session.getBasicRemote().sendText(poruka);
        } catch (IOException ex) {
            Logger.getLogger(OglasnikPrijave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
