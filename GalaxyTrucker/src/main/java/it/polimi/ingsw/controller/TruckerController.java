package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidStateException;

import java.util.ArrayList;

public class TruckerController  {
    private Trucker trucker;
    private Component selectedComponent;

    public TruckerController(String nickname, PlayerColors color){
        trucker=new Trucker(nickname,color);
    }

    public Component getSelectedComponent() {
        return selectedComponent;
    }

    public BuildingState getBuildingState() {
        return trucker.getBuildingState();
    }

    public void setBuildingState(BuildingState buildingState) {
        trucker.setBuildingState(buildingState);
    }

    public void setPosition(int position) {
        trucker.setPosition(position);
    }

    public int getPosition() {
        return trucker.getPosition();
    }

    public PlayerColors getColor() {
        return trucker.getColor();
    }

    public void addCosmicCredit(int cosmicCredit) {
        trucker.addCosmicCredit(cosmicCredit);
    }

    public int getCosmicCredit() {
        return trucker.getCosmicCredit();
    }

    public String getNickname() {
        return trucker.getNickname();
    }

    public SpaceShip getSpaceShip() {
        return trucker.getSpaceShip();
    }

    public ArrayList<Card> showStack(int index, Game game) {
        return trucker.showStack(index, game);
    }

    public void keep(Component component, Game game) throws InvalidComponentsException, InvalidStateException {
        trucker.keep(component, game);
    }

    public void link(Component component, int x, int y) throws InvalidStateException, InvalidCoordinates {
        trucker.link(component, x, y);
    }

    public void selectComponent(int index, AvailableComponents availableComponents) throws InvalidStateException {
        selectedComponent = trucker.selectComponent(index, availableComponents);
    }

    public void finish() throws InvalidStateException {
        trucker.finish();
    }

    public void fillSpaceship(ArrayList<int[]> coordAliens) {
        trucker.fillSpaceship(coordAliens);
    }

    public Trucker getTrucker() {
        return trucker;
    }

}
