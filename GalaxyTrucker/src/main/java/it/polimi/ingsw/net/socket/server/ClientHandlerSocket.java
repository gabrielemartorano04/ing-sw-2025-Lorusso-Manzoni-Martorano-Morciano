package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.EndGameEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardEvent;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class ClientHandlerSocket implements ClientHandler {
    private final NetworkServer network;
    private final ServerController controller;
    private int id;

    public ClientHandlerSocket(NetworkServer network, ServerController controller) {
        this.network = network;
        this.controller = controller;
        this.id = -1;
        startListener();
    }

    private void startListener() {
        Thread listenerThread = new Thread(() -> {
            try {
                while (!network.isClosed()) {
                    ClientEvent event = network.receiveMessage();
                    controller.addEventToQueue(event, this.getId());
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error receiving the event: " + e.getMessage());
            } finally {
                System.out.println("Client disconnected.");
                controller.removeClient(this);
                Game.getIstance().setNumPlayers(Game.getIstance().getNumPlayers()-1);
                int[] scores;
                Map<String, Integer> playersScores = new HashMap<>();
                scores = Game.getIstance().computeScores();
                for(int i=0; i<Game.getIstance().getNumPlayers(); i++){
                    playersScores.put(Game.getIstance().getTruckerOut().get(i).getNickname(), scores[i]);
                }
                sendEvent(new EndGameEvent(playersScores));
                close();
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    @Override
    public void setId(int id) {
        if(this.id==-1){
            this.id = id;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void sendEvent(ServerEvent event) {
        try {

            network.sendMessage(event);
        } catch (IOException e) {
            System.err.println("Error while sending event to the client: " + e.getMessage());
        }
    }


    public void close() {
        try {
            network.close();
        } catch (IOException e) {
            System.err.println("Error while closing connection: " + e.getMessage());
        }
    }
}

