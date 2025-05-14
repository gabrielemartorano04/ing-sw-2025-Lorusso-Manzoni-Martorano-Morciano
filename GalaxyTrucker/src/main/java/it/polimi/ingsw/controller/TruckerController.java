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
    public TruckerController(String nickname, PlayerColors color){
        trucker=new Trucker(nickname,color);
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
        return 0;
    }

    public PlayerColors getColor() {
        return null;
    }

    public void addCosmicCredit(int cosmicCredit) {

    }

    public int getCosmicCredit() {
        return 0;
    }

    public String getNickname() {
        return "";
    }

    public SpaceShip getSpaceShip() {
        return null;
    }

    public ArrayList<Card> showStack(int index, Game game) {
        return null;
    }

    public void keep(Component component, Game game) throws InvalidComponentsException, InvalidStateException {

    }

    public void link(Component component, int x, int y) throws InvalidStateException, InvalidCoordinates {

    }

    public Component selectComponent(int index, AvailableComponents availableComponents) throws InvalidComponentsException, InvalidStateException {
        return null;
    }

    public void finish() throws InvalidStateException {

    }

    public void fillSpaceship(ArrayList<int[]> coordAliens) {

    }
}
