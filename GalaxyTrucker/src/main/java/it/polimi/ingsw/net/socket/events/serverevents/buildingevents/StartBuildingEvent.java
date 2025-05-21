package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Event from server to client. It's time for all the players to start building their spaceships
 */


public class StartBuildingEvent extends ServerEvent {
    private String message;
    private Stack[] stacks;
    private final SpaceShip spaceShip;
    private int seconds;
    private ArrayList<Component> components;
    private final String nickname;
    private ArrayList<String> players;

    public StartBuildingEvent(String username, SpaceShip spaceShip, Stack[] stacks, ArrayList<Component> availableComponents, int seconds, ArrayList<String> players) {
        super(ServerEventType.START_BUILDING, true);
        message = username + " logged\nIt's time to start building";
        this.nickname = username;
        this.spaceShip = spaceShip;
        this.components = availableComponents;
        this.stacks = stacks;
        this.seconds = seconds;
        this.players=players;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public Stack[] getStacks() {return stacks;}

    public ArrayList<Component> getComponents() {return components;}

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public int getSeconds() {
        return seconds;
    }
}
