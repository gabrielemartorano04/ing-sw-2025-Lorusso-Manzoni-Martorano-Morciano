package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.controller.SpaceShipController;
import it.polimi.ingsw.model.components.Component;

public class ShipUpdateEvent extends ServerEvent {

    private String message;
    private Component[][] ship;
    private int truckerPosition,numBatteries,numCrew,cannonPower,enginePower;

    public ShipUpdateEvent(Component[][] ship, int truckerPosition, int numBatteries, int numCrew, int cannonPower, int enginePower) {
        super(ServerEventType.UPDATE_STATE, false);
        message = "Ship updated";
        this.ship = ship;
        this.truckerPosition = truckerPosition;
        this.numBatteries = numBatteries;
        this.numCrew = numCrew;
        this.cannonPower = cannonPower;
        this.enginePower = enginePower;
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
}
