package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

import java.util.ArrayList;
import java.util.Map;

public class ResumeGameEvent extends ServerEvent {

    private ArrayList<Trucker> trucker;
    private GameState gameState;
    private int level;
    private Map<String, Integer> track;

    public ResumeGameEvent(ArrayList<Trucker> trucker, GameState gameState, int level, Map<String, Integer> track) {
        super(ServerEventType.RESUME_GAME, true);
        this.trucker = trucker;
        this.gameState = gameState;
        this.level = level;
        this.track = track;

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

    public ArrayList<Trucker> getTrucker() {
        return trucker;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
