package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class ResumeGameEvent extends ServerEvent {

    private ArrayList<Trucker> trucker;
    private GameState gameState;

    public ResumeGameEvent(ArrayList<Trucker> trucker, GameState gameState) {
        super(ServerEventType.RESUME_GAME, true);
        this.trucker = trucker;
        this.gameState = gameState;
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
