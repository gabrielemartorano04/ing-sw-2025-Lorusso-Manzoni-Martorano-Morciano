package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class PlanetDecision extends ServerEvent {
    private ArrayList<Trucker> planetsOccupied;
    private String nickname;
    public PlanetDecision(boolean forAll, ArrayList<Trucker> planetsOccupied, String nickname) {
        super(ServerEventType.PLOT_PLANETS, forAll);
        this.planetsOccupied = planetsOccupied;
        this.nickname = nickname;
    }

    public ArrayList<Trucker> getPlanetsOccupied() {
        return planetsOccupied;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {

    }
    public String getInfoForOthers(){
        return nickname+" is deciding if landing on a planet or not";
    }
}
