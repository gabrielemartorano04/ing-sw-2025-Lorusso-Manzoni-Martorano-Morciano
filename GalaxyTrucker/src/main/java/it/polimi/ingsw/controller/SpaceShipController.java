package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.SpaceShip;

public class SpaceShipController {

    public SpaceShipController() {
    }

    public void addComponent(SpaceShip spaceShip, Component component, int x, int y) throws InvalidLinkException, InvalidPositionException {
        if (spaceShip.getComponents()[x][y] != null) {
            throw new InvalidPositionException("La posizione [" + x + "] [" + y + "] non è valida");
        }
        spaceShip.addComponent(component, x, y);
        if (!spaceShip.isValid(x, y)){
            spaceShip.removeComponent(x, y);
            throw new InvalidLinkException("Il componente " + component.getType() + " non può essere collegato in questo modo");
        }
    }
}
