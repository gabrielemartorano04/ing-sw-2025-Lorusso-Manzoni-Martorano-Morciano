package it.polimi.ingsw.net.server;

import it.polimi.ingsw.net.events.Event;

import it.polimi.ingsw.net.events.Event;
import it.polimi.ingsw.net.events.EventType;
import it.polimi.ingsw.net.events.HelloEvent;
import it.polimi.ingsw.net.server.ClientHandlerServer;

public class ServerController {
    public void handleEvent(Event event, ClientHandlerServer client) {
        System.out.println("[SERVER] Ricevuto evento: " + event);

        if (event instanceof HelloEvent textEvent) {
            String msg = textEvent.getMessage();
            System.out.println("[SERVER] Testo ricevuto: " + msg);

            // Rispondiamo al client
            HelloEvent response = new HelloEvent("Server ha ricevuto il tuo messaggio: '" + msg + "'", EventType.HELLO_MESSAGE);
            try {
                client.sendEvent(response);
            } catch (Exception e) {
                System.err.println("Errore nell'invio di risposta al client: " + e.getMessage());
            }
        }
    }
}

