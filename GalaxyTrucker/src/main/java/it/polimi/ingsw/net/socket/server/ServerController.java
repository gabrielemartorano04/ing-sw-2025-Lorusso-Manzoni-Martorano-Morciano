package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.net.socket.events.Event;

import it.polimi.ingsw.net.socket.events.EventType;
import it.polimi.ingsw.net.socket.events.HelloEvent;
import it.polimi.ingsw.net.socket.events.clientevents.CreateGameEvent;

import java.util.HashMap;

public class ServerController {
    private final HashMap<Integer, ClientHandlerServer> clients = new HashMap<>();
    private int nextId = 0;
    private GameController gameController;

    public synchronized int registerClient(ClientHandlerServer client) {
        int id = nextId++;
        clients.put(id, client);
        return id;
    }

    public void handleEvent(Event event, ClientHandlerServer client) {
        System.out.println("[SERVER] Ricevuto evento: " + event);
        switch(event){
            case CreateGameEvent e:
                gameController=new GameController();
                System.out.println("[SERVER] Gioco creato");
                break;
            case
            default:
                throw new IllegalStateException("Unexpected value: " + event);
        }
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

