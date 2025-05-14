package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.controller.TurnManager;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

import java.util.Map;

public class ShipUpdateEvent extends ServerEvent {

    private String message;
    private Component[][] ship;
    private int truckerPosition,numBatteries,numCrew,cannonPower,enginePower;
    private Map<String, Integer> goods;

    public ShipUpdateEvent(TurnManager.TruckerController truckerController) {
        super(ServerEventType.UPDATE_STATE, false);
        message = "Ship updated";
        this.ship = truckerController.getSpaceShip().getComponents();
        this.truckerPosition = truckerController.getPosition();
        this.numBatteries = truckerController.getSpaceShip().getNumBatteries();
        this.numCrew = truckerController.getSpaceShip().getNumHuman();
        this.cannonPower = truckerController.getSpaceShip().getSingleCannonPower();
        this.enginePower = truckerController.getSpaceShip().getSingleEnginePower();
        this.goods.put("RED", truckerController.getSpaceShip().getRedGoods());
        this.goods.put("YELLOW", truckerController.getSpaceShip().getYellowGoods());
        this.goods.put("BLUE", truckerController.getSpaceShip().getBlueGoods());
        this.goods.put("GREEN", truckerController.getSpaceShip().getGreenGoods());
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

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
