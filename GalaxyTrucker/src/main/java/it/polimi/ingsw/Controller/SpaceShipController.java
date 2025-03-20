package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.SpaceShip;

public class SpaceShipController {

    public SpaceShipController() {
    }

    public void addComponent(SpaceShip spaceShip, Component component, int[] coord) throws InvalidLinkException, InvalidPositionException {
        if (spaceShip.components[coord[0]][coord[1]] != null) {
            throw new InvalidPositionException("La posizione [" + coord[0] + "] [" + coord[1] + "] è gia occupata");
        }
        if ("Empty Space".equals((spaceShip.getComponents()[coord[0]][coord[1]]).getType())){
            throw new InvalidPositionException("La posizione [" + coord[0] + "] [" + coord[1] + "] è al di fuori dello spazio disponibile");
        }
        if (!spaceShip.isValid(coord[0], coord[1])){
            throw new InvalidLinkException("Il componente " + component.getType() + " non può essere collegato in questo modo");
        }
        spaceShip.components[coord[0]][coord[1]] = component;
    }
}
