package it.polimi.ingsw.net;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.net.events.serverevents.PingEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Client {
    private final ClientHandler clientHandler;
    private final RemoteClientHandler remoteClientHandler;
    private TruckerController truckerController;
    private int id;
    private long lastPongTime;

    public long getLastPongTime() {
        return lastPongTime;
    }

    public Client(ClientHandler clientHandler, RemoteClientHandler remoteClientHandler) {
        this.clientHandler = clientHandler;
        this.remoteClientHandler = remoteClientHandler;
        this.id = -1;
        this.lastPongTime = System.currentTimeMillis();
    }

    public void setId(int id) {
        if(this.id == -1){
            this.id = id;
            if(clientHandler != null){
                clientHandler.setId(id);
            }else{
                try {
                    remoteClientHandler.setId(id);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            throw new RuntimeException("Errore");
        }

    }

    public int getId() {
        return id;
    }


    public ClientHandler getSocketClient() {
        return clientHandler;
    }

    public RemoteClientHandler getRmiClient() {
        return remoteClientHandler;
    }

    public TruckerController getTruckerController() {
        return truckerController;
    }

    public void setTruckerController(TruckerController truckerController) {
        this.truckerController = truckerController;
    }

    public void notifyPing(){
        if(clientHandler != null){
            clientHandler.sendEvent(new PingEvent());
        }else {
            try {
                remoteClientHandler.respondToPing(new PingEvent());
            } catch (RemoteException e) {
                ArrayList<Client> toRemove = new ArrayList<>();
                toRemove.add(this);
                ServerController.getIstance().finishGame(toRemove);
                throw new RuntimeException(e);
            }
        }
    }

    public void updateClient(ServerEvent event){
        if(clientHandler != null){
            clientHandler.sendEvent(event);
        }else if (remoteClientHandler != null){
            try {
                remoteClientHandler.handleIncomingEvent(event);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            System.out.println("Both remoteClientHandler and clientHandler are null!");
        }
    }

    public void updateLastPong() {
        this.lastPongTime = System.currentTimeMillis();
    }

    public boolean isTimedOut(long now) {
        return now - lastPongTime > 10000; // 10 secondi
    }

    public void closeConnection(){
        if(clientHandler != null){
            clientHandler.close();
        }else if(remoteClientHandler != null){

        }
    }

}
