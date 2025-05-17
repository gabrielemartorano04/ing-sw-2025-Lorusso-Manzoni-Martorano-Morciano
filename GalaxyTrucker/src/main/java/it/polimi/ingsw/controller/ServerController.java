package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.BuildCompleteEvent;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.AvailableColorsEvent;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.FirstConnectionEvent;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.NotYourTurnError;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientHandlerVisitor;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerController {
    private final ArrayList<ClientHandler> clients;
    private final ClientHandlerVisitor visitor;
    private TurnManager turnManager;
    private boolean turnStarted;
    private boolean gameCreated;
    private GameController gameController;

    private final BlockingQueue<EventWithSource> eventQueue = new LinkedBlockingQueue<>();

    public ServerController() {
        startDispatcher();
        this.visitor = new ClientHandlerVisitor();
        this.turnStarted = false;
        this.clients = new ArrayList<>();
        this.gameCreated = false;
    }

    public void handleEvent(ClientEvent event, ClientHandlerSocket client) {
        eventQueue.offer(new EventWithSource(event, client));
    }

    public synchronized void addClient(ClientHandler client) {
        if (clients.isEmpty()) {
            FirstConnectionEvent event = new FirstConnectionEvent(
                    "First player in the game, choose the total number of players of the Game"
            );
            client.sendEvent(event);
        }else{
            AvailableColorsEvent event = new AvailableColorsEvent(gameController.getAvailablePlayerColors(),
                    "Login");
            client.sendEvent(event);
        }
        clients.add(client);

    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client disconnected and removed from the game.");
    }

    public void startDispatcher() {
        Thread dispatcher = new Thread(() -> {
            while (true){
                try {
                    EventWithSource entry = eventQueue.take();
                    processEvent(entry, visitor);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        dispatcher.setDaemon(true);
        dispatcher.start();
    }

    /**
     * method used to create the turnManager once all the players have joined the game
     * @param players
     */
    private void CreateTurnManager(ArrayList<ClientHandlerSocket> players) {
        this.turnManager = new TurnManager(players);
        this.turnStarted = true;
    }

    public void processEvent(EventWithSource eventWithSource, ClientHandlerVisitor visitor) {
        ClientEvent event = eventWithSource.getEvent();
        ClientHandlerSocket client = eventWithSource.getClient();

        if (turnStarted && event.requiresTurn() && !turnManager.isPlayerTurn(client)) {
            client.sendEvent(new NotYourTurnError("Non è il tuo turno."));
            return;
        }

        visitor.setClientHandlerServer(client);
        ServerEvent response = event.call(visitor);

        if (!gameCreated && visitor.getNumOfPlayers() > 0) {
            try {
                this.gameController = new GameController(visitor.getNumOfPlayers(), visitor.getGameLevel());
                visitor.setGameController(this.gameController);
                this.gameCreated = true;
            } catch (Exception e) {
                e.printStackTrace();
                client.sendEvent(response);
                return;
            }
        }

//        if(gameCreated){
//            if(gameController.getGame().isHourglassExpired()) {
//                response = new BuildCompleteEvent();
//            }
//        }

        if(response.isForAll()){
            for(ClientHandler c: clients){
                c.sendEvent(response);
            }
        }else{
            client.sendEvent(response);
        }
    }
}

