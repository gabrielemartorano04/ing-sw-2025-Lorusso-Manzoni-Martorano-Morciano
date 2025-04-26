package it.polimi.ingsw.net.server;

import it.polimi.ingsw.net.events.Event;

public class ServerController {

    public void handleEvent(Event event, ClientHandlerServer sender) {
        System.out.println("Ricevuto evento: " + event);
        // Esegui logica di gioco qui
        // Potresti anche inviare risposte
        // sender.sendEvent(new SomeOtherEvent());
    }
}
