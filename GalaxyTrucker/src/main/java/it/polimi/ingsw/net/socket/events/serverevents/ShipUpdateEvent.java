package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.controller.SpaceShipController;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;

import java.util.ArrayList;
import java.util.Map;

public class ShipUpdateEvent extends ServerEvent {

    private String message;
    private Component[][] ship;
    private int truckerPosition,numBatteries,numCrew,cannonPower,enginePower;
    private Map<String, Integer> goods;

    public ShipUpdateEvent(SpaceShip ship, int truckerPosition, int numBatteries, int numCrew, int cannonPower, int enginePower, ArrayList<Goods> goods) {
        super(ServerEventType.UPDATE_STATE, false);
        message = "Ship updated";
        this.ship = ship.getComponents();
        this.truckerPosition = truckerPosition;
        this.numBatteries = numBatteries;
        this.numCrew = numCrew;
        this.cannonPower = cannonPower;
        this.enginePower = enginePower;
        this.goods.put("RED", ship.getRedGoods());
        this.goods.put("YELLOW", ship.getYellowGoods());
        this.goods.put("BLUE", ship.getBlueGoods());
        this.goods.put("GREEN", ship.getGreenGoods());
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
