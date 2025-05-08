package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.controller.SpaceShipController;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;

import java.util.ArrayList;
import java.util.Map;

public class ShipUpdateEvent extends ServerEvent {

    private String message;
    private Component[][] ship;
    private int truckerPosition,numBatteries,numCrew,cannonPower,enginePower;
    private Map<String, Integer> goods;

    public ShipUpdateEvent(Trucker trucker) {
        super(ServerEventType.UPDATE_STATE, false);
        message = "Ship updated";
        this.ship = trucker.getSpaceShip().getComponents();
        this.truckerPosition = trucker.getPosition();
        this.numBatteries = trucker.getSpaceShip().getNumBatteries();
        this.numCrew = trucker.getSpaceShip().getNumHuman();
        this.cannonPower = trucker.getSpaceShip().getSingleCannonPower();
        this.enginePower = trucker.getSpaceShip().getSingleEnginePower();
        this.goods.put("RED", trucker.getSpaceShip().getRedGoods());
        this.goods.put("YELLOW", trucker.getSpaceShip().getYellowGoods());
        this.goods.put("BLUE", trucker.getSpaceShip().getBlueGoods());
        this.goods.put("GREEN", trucker.getSpaceShip().getGreenGoods());
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Component[][] getShip() {
        return ship;
    }

    public int truckerPosition() {
        return truckerPosition;
    }

    public int numBatteries() {
        return numBatteries;
    }

    public int numCrew() {
        return numCrew();
    }

    public int cannonPower() {
        return cannonPower();
    }

    public int enginePower() {
        return enginePower();
    }

    public Map<String, Integer> getGoods() {
        return goods;
    }
}
