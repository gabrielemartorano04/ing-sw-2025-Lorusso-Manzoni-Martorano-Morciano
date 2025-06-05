package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.EndGameEvent;
import it.polimi.ingsw.net.events.serverevents.PingEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientHandlerSocket implements ClientHandler {
    private final NetworkServer network;
    private final ServerController controller;
    private int id;
    private long lastPongTime = System.currentTimeMillis();

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
                if(Game.getIstance()!=null) {
                    System.out.println("Client disconnected.");
                    controller.removeClient(this);
                    int[] scores;
                    Map<String, Integer> playersScores = new HashMap<>();
                    scores = Game.getIstance().computeScores();
                    for (int i = 0; i < Game.getIstance().getTruckerList().size() - 1; i++) {
                        if (Game.getIstance().getTrucker(i).getNickname() != null) {
                            playersScores.put(Game.getIstance().getTrucker(i).getNickname(), scores[i]);
                        } else {
                            playersScores.put("giocatore " + i, scores[i]);
                        }
                    }
                    for (int i = 0; i < Game.getIstance().getTruckerOut().size() - 1; i++) {
                        playersScores.put(Game.getIstance().getTruckerOut().get(i).getNickname(), scores[i]);
                    }
                    playersScores.forEach((name, score) -> System.out.println("- " + name + ": " + score));
                    controller.broadcastEventExceptSome(new EndGameEvent(playersScores), this.getId());
                    close();
                }
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


    @Override
    public void close() {
        try {
            network.close();
        } catch (IOException e) {
            System.err.println("Error while closing connection: " + e.getMessage());
        }
    }


}

