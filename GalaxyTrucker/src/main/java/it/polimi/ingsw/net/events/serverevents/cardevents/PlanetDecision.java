package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

public class PlanetDecision extends ServerEvent {
    private ArrayList<Trucker> planetsOccupied;
    private String nickname;
    private SpaceShip spaceShip;
    public PlanetDecision(boolean forAll, ArrayList<Trucker> planetsOccupied, String nickname,SpaceShip spaceShip) {
        super(forAll);
        this.planetsOccupied = planetsOccupied;
        this.nickname = nickname;
        this.spaceShip=spaceShip;
    }

    public SpaceShip getShip(){
        return spaceShip;
    }

    public ArrayList<Trucker> getPlanetsOccupied() {
        return planetsOccupied;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
    public String getInfoForOthers(){
        return nickname+" is deciding if landing on a planet or not";
    }
}
