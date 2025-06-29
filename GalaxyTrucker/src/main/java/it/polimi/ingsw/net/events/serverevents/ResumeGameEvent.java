package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents an event sent from the server to resume the state of the game for a client
 */
public class ResumeGameEvent extends ServerEvent {

    /**
     * List of the unfinished game's players
     */
    private final ArrayList<Trucker> truckers;

    /**
     * Game state of the unfinished game
     */
    private final GameState gameState;

    /**
     * Level of the unfinished game
     */
    private final int level;

    /**
     * Positions of the unfinished game's players
     */
    private final Map<String, Integer> track;

    /**
     * Nickname of the player
     */
    private final String nickname;

    /**
     * List of unfinished game's available components
     */
    private final ArrayList<Component> components;

    /**
     * List of the unfinished game's players' nickname
     */
    private final ArrayList<String> playerNames;

    /**
     * number of cards remaining
     */
    private final int cardToGo;

    /**
     * Event constructor
     * @param availableComponents list of available components
     * @param nickname of the player
     * @param trucker list of players
     * @param gameState state of the game
     * @param level of the gam
     * @param track positions of the players
     * @param playerNames list of the players' nickname
     */
    public ResumeGameEvent(ArrayList<Component> availableComponents, String nickname, ArrayList<Trucker> trucker, GameState gameState, int level, Map<String, Integer> track, ArrayList<String> playerNames, int  cardToGo) {
        super(true);
        this.truckers = trucker;
        this.gameState = gameState;
        this.level = level;
        this.track = track;
        this.nickname = nickname;
        this.components = availableComponents;
        this.playerNames = playerNames;
        this.cardToGo = cardToGo;
    }

    /**
     * @return the level of the unfinished game
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return the truckers' positions of the unfinished game
     */
    public Map<String, Integer> getTrack() {
        return track;
    }

    /**
     * @return state of the unfinished game
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @return the list of the unfinished game's players
     */
    public ArrayList<Trucker> getTruckers() {
        return truckers;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the list of the unfinished game's available components
     */
    public ArrayList<Component> getComponents() {
        return components;
    }

    /**
     * @return the list of the unfinished game's players' nickname
     */
    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    /**
     * @return the number of cards remaining
     */
    public int getCardToGo(){
        return cardToGo;
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
