package it.polimi.ingsw.net.events.serverevents.buildingevents;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;


/**
 * Represents an event triggered on the server when a player begins constructing a spaceship
 */
public class StartBuildingEvent extends ServerEvent {

    /**
     * Spaceship of the player
     */
    private final SpaceShip spaceShip;

    /**
     * Seconds remaining to the hourglass
     */
    private final int seconds;

    /**
     * List of the available components
     */
    private final ArrayList<Component> components;

    /**
     * Nickname of the player
     */
    private final String nickname;

    /**
     * List of all the players
     */
    private final ArrayList<String> players;

    /**
     * Level of the game
     */
    private final int level;

    /**
     * Event constructor
     * @param username of the player
     * @param spaceShip of the player
     * @param availableComponents list of all the available components
     * @param seconds remaining to the hourglass
     * @param players list of players
     * @param level of the game
     */
    public StartBuildingEvent(String username, SpaceShip spaceShip, ArrayList<Component> availableComponents, int seconds, ArrayList<String> players, int level) {
        super(true);
        this.nickname = username;
        this.spaceShip = spaceShip;
        this.components = availableComponents;
        this.seconds = seconds;
        this.players=players;
        this.level=level;
    }

    /**
     * @return the list of players
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the spaceship of the player
     */
    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    /**
     * @return the list of available components
     */
    public ArrayList<Component> getComponents() {return components;}

    /**
     * @return the seconds remaining to the hourglass
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * @return the level of the game
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return the message associated with the start of the building phase
     */
    @Override
    public String getMessage() {
        return nickname + " logged\nIt's time to start building";
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
