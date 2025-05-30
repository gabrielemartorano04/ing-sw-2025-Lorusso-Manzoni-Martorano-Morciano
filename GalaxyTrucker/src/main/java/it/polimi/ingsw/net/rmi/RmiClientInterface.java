package it.polimi.ingsw.net.rmi;

import java.rmi.Remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.*;

public interface RmiClientInterface extends Remote {
    void handleEvent(ServerEvent event) throws RemoteException;

}



