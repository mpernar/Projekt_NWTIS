package org.foi.nwtis.mpernar.aplikacija_4.wsep;

import jakarta.websocket.OnMessage;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Klasa koja predstavlja krajnju tocku /login
 * @author Mario
 */
@ServerEndpoint("/login")
public class WebSocketLogin {

    /**
     * metoda za primanje poruke na websocket
     * @param message 
     */
    @OnMessage
    public void primiPoruku(String message) {
        System.out.println(message);
    }
}
