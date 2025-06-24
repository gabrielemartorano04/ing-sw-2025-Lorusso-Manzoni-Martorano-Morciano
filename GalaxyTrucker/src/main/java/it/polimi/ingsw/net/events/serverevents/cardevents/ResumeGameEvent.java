package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;
import java.util.Map;

public class ResumeGameEvent extends ServerEvent {

    private final ArrayList<Trucker> truckers;
    private final GameState gameState;
    private final int level;
    private final Map<String, Integer> track;
    private final String nickname;
    private final ArrayList<Component> components;
    private final ArrayList<String> playerNames;

    public ResumeGameEvent(ArrayList<Component> availableComponents, String nickname, ArrayList<Trucker> trucker, GameState gameState, int level, Map<String, Integer> track, ArrayList<String> playerNames) {
        super(true);
        this.truckers = trucker;
        this.gameState = gameState;
        this.level = level;
        this.track = track;
        this.nickname = nickname;
        this.components = availableComponents;
        this.playerNames = playerNames;
    }

    public int getLevel() {
        return level;
    }

    public Map<String, Integer> getTrack() {
        return track;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ArrayList<Trucker> getTruckers() {
        return truckers;
    }

    public String getNickname() {
        return nickname;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }


    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
