package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.components.Component;

import java.util.ArrayList;

/**
 *
 */
public class SpaceShipController {
    private final SpaceShip spaceShip;

    public SpaceShipController(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }

    public ArrayList<Component> getKept() {
        return spaceShip.getKept();
    }

    public void increaseLost(int lost) {
        spaceShip.increaseLost(lost);
    }

    public void setBattery() {
        spaceShip.setBattery();
    }
}
