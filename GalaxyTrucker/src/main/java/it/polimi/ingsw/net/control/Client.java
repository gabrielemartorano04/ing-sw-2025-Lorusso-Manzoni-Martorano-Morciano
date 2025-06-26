package it.polimi.ingsw.net.control;

import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.net.events.serverevents.PingEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;
import it.polimi.ingsw.net.socket.server.ClientHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a client connected to the server, either via socket or RMI.
 * Manages communication and associates the specific id and truckerController to
 * the client, in addiction with its lastPongTime to see if it's still active.
 */
public class Client {

    /**
     * socket client handler if the client chose socket
     */
    private final ClientHandler clientHandler;

    /**
     * rmi client handler if the client chose rmi
     */
    private final RemoteClientHandler remoteClientHandler;

    /**
     * controller of the trucker which correspond to the player model implementation
     */
    private TruckerController truckerController;

    /**
     * id of this client
     */
    private int id;

    /**
     * last time that the client responded to the ping
     */
    private long lastPongTime;

    /**
     * constructor of the class
     *
     * @param clientHandler client handler socket if the client chose socket, null otherwise
     * @param remoteClientHandler client rmi handler if the client chose rmi, null otherwise
     */
    public Client(ClientHandler clientHandler, RemoteClientHandler remoteClientHandler) {
        this.clientHandler = clientHandler;
        this.remoteClientHandler = remoteClientHandler;
        this.id = -1;
        this.lastPongTime = System.currentTimeMillis();
    }

    /**
     * sets the id to this client and to the client handler
     *
     * @param id id assigned by the server Controller
     */
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
        }
    }

    /**
     * @return the id of this client
     */
    public int getId() {
        return id;
    }

    /**
     * @return the trucker controller associated to this client
     */
    public TruckerController getTruckerController() {
        return truckerController;
    }

    /**
     * sets the trucker controller associated to this client
     *
     * @param truckerController trucker controller to be associated to this client
     */
    public void setTruckerController(TruckerController truckerController) {
        this.truckerController = truckerController;
    }

    /**
     * notifies a ping from the server that needs a pong response to verify
     * whether the client is still active or not
     */
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

    /**
     * updates the client view
     *
     * @param event generic event that is needed by the view
     */
    public void updateClient(ServerEvent event){
        if(clientHandler != null){
            clientHandler.sendEvent(event);
        }else if (remoteClientHandler != null){
            try {
                remoteClientHandler.handleIncomingEvent(event);
            } catch (RemoteException e) {
                ArrayList<Client> toRemove = new ArrayList<>();
                toRemove.add(this);
                ServerController.getIstance().finishGame(toRemove);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * updates the last time this client has responded to a ping event
     */
    public void updateLastPong() {
        this.lastPongTime = System.currentTimeMillis();
    }

    /**
     * @param now current time in the server
     * @return true if the client hasn't responded in time to the ping sent by the server
     * and will be eliminated from the game
     */
    public boolean isTimedOut(long now) {
        return now - lastPongTime > 10000;
    }

    /**
     * closes the connection with the current client
     */
    public void closeConnection(){
        if(clientHandler != null){
            clientHandler.close();
        }else if(remoteClientHandler != null){
            try {
                this.remoteClientHandler.close();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
