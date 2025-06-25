package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class PlanetDecision extends CardEvent {
    private ArrayList<Trucker> planetsOccupied;
    private SpaceShip spaceShip;
    public PlanetDecision(boolean forAll, ArrayList<Trucker> planetsOccupied, String nickname,SpaceShip spaceShip, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.PLANET_DECISION,nickname,truckers);
        this.planetsOccupied = planetsOccupied;
        this.spaceShip=spaceShip;
    }

    public SpaceShip getShip(){
        return spaceShip;
    }

    public ArrayList<Trucker> getPlanetsOccupied() {
        return planetsOccupied;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
    public String getInfoForOthers(){
        return getNickname()+" is deciding if landing on a planet or not";
    }
}
