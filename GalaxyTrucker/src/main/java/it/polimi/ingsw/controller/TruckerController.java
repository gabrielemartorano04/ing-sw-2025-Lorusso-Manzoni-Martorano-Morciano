package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.utilities.exceptions.InvalidStateException;

import java.util.ArrayList;

/**
 *
 */
public class TruckerController {


    private PlayerColors color;
    private String nickname;
    private final SpaceShip spaceShip;
    private int position; //posizione assoluta
    private int cosmicCredit;
    private BuildingState buildingState;

    public TruckerController(String nickName, PlayerColors color) {
        this.nickname = nickName;
        this.color = color;
        this.spaceShip = new SpaceShip(color);
        this.cosmicCredit = 0;
        buildingState=BuildingState.BUILDING;
    }

    public BuildingState getBuildingState() {
        return buildingState;
    }

    public void setBuildingState(BuildingState buildingState) {
        this.buildingState = buildingState;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public PlayerColors getColor() {
        return color;
    }

    public void addCosmicCredit(int cosmicCredit) {
        this.cosmicCredit += cosmicCredit;
    }

    public int getCosmicCredit() {
        return cosmicCredit;
    }



    public String getNickname() {return nickname;}

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }


    public ArrayList<Card> showStack(int index, Game game){
        if(buildingState!=BuildingState.SHOW_STACK){
            throw new IllegalStateException("Trucker is not in show stack state");
        }
        buildingState=BuildingState.BUILDING;
        return game.viewStack(index);

    }

    public void keep(Component component, Game game) throws InvalidComponentsException, InvalidStateException {
        if(buildingState!=BuildingState.KEEP){
            throw new InvalidStateException("Trucker is not in keep component state");
        }
        if(spaceShip.getKept().size()<2){
            spaceShip.getKept().add(component);
            buildingState=BuildingState.BUILDING;
        }
        else {
            component.setCovered(false);
            game.getComponents().getComponents().addFirst(component);
            buildingState=BuildingState.BUILDING;
            throw new InvalidComponentsException("No available space in kept component");
        }

    }

    public void link(Component component, int x, int y) throws InvalidStateException, InvalidCoordinates {
        if(buildingState!=BuildingState.LINK){
            throw new InvalidStateException("Trucker is not in link component state");
        }

        spaceShip.addComponent(component,x,y);
        buildingState=BuildingState.BUILDING;
    }

    public Component selectComponent(int index, AvailableComponents availableComponents) throws InvalidComponentsException, InvalidStateException {
        if(buildingState!=BuildingState.SELECT_COMPONENT){
            throw new InvalidStateException("Trucker is not in select component state");
        }
        if (index >= 0 && index < availableComponents.getComponents().size()) {
            Component selectedComponent;
            selectedComponent = availableComponents.getComponents().get(index);
            availableComponents.removeComponent(index);
            buildingState=BuildingState.SELECT_COMPONENT;
            return selectedComponent;
        } else {
            buildingState=BuildingState.SELECT_COMPONENT;
            throw new InvalidComponentsException("You're not in the range of components");
        }

    }




    public void finish() throws InvalidStateException {
        if(buildingState!=BuildingState.WAIT_END){
            throw new InvalidStateException("Trucker is not in finish state");
        }
        if(!spaceShip.wellDone()){
            spaceShip.increaseLost(spaceShip.getKept().size());
            spaceShip.getKept().removeAll(spaceShip.getKept());
        }
        buildingState=BuildingState.END;
    }

    public void fillSpaceship(ArrayList<int[]> coordAliens){
        spaceShip.setBattery();
        int goodsCapacity = spaceShip.getGoodsCapacity();
        spaceShip.setNumAlien(coordAliens);
        spaceShip.setNumHuman();
    }

    //FSM ingresso

}

