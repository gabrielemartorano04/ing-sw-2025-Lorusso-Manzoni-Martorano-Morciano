package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.SpaceShip;
import it.polimi.ingsw.Model.Trucker;

import java.util.HashMap;
import java.util.Map;

public class TruckerController {
    private final Map<Trucker, SpaceShip> truckerSpaceShipMap;

    public TruckerController() {
        this.truckerSpaceShipMap = new HashMap<>();
    }

    public void assignSpaceShip(Trucker trucker, SpaceShip spaceShip) {
        this.truckerSpaceShipMap.put(trucker, spaceShip);
    }

    public void removeTrucker(Trucker trucker) {
        this.truckerSpaceShipMap.remove(trucker);
    }

    public SpaceShip getSpaceShip(Trucker trucker) {
        return this.truckerSpaceShipMap.get(trucker);
    }
}
