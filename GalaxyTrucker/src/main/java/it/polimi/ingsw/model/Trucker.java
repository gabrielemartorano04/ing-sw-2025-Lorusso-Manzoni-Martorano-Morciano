package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.exceptions.ComponentNotAvailable;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.utilities.exceptions.InvalidStateException;

import java.util.ArrayList;

/**
 *
 */
public class Trucker {


    private final PlayerColors color;
    private final String nickname;
    private final SpaceShip spaceShip;
    private int position; //posizione assoluta
    private int cosmicCredit;
    private BuildingState buildingState;
    private ArrayList<Component> keptComponents;
    private Component selectedComponent;

    public Trucker(String nickName, PlayerColors color) {
        this.nickname = nickName;
        this.color = color;
        this.spaceShip = new SpaceShip(color);
        this.cosmicCredit = 0;
        buildingState=BuildingState.BUILDING;
        keptComponents = new ArrayList<>();
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

    public Component getSelectedComponent() {
        return selectedComponent;
    }

    public void addKeptComponent(Component keptComponent) {
        this.keptComponents.add(keptComponent);
    }


    public String getNickname() {return nickname;}

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }


    public ArrayList<Card> showStack(int index, Game game){
        return game.viewStack(index);
    }

    public void keep(Component component, Game game) throws InvalidComponentsException {
        if(keptComponents.size()<2){
            keptComponents.add(component);
        }
        else { //non c'è spazio per tenerlo quindi lancio eccezione
            throw new InvalidComponentsException("No available spots to keep this component cause both places are taken");
        }

    }

    public void link(Component component, int x, int y) throws InvalidCoordinates {
        spaceShip.addComponent(component,x,y);
    }

    public void selectComponent(int index, AvailableComponents availableComponents) throws ComponentNotAvailable {
        if(availableComponents.getComponents().get(index).getSelected()){
            throw new ComponentNotAvailable("Error");
        }
        this.selectedComponent =  availableComponents.getComponents().get(index);
    }


    public void finish() {
        if(!spaceShip.wellDone()){
            spaceShip.increaseLost(keptComponents.size());
            keptComponents.clear();
        }
    }

    public void fillSpaceship(ArrayList<int[]> coordAliens){
        spaceShip.setBattery();
        int goodsCapacity = spaceShip.getGoodsCapacity();
        spaceShip.setNumAlien(coordAliens);
        spaceShip.setNumHuman();
    }

    public void rotateComponent(){
        // TODO: method that rotate a componente
    }

}

