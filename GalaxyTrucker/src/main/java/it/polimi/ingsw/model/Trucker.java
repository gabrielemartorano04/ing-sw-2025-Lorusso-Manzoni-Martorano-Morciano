package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.components.Component;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class Trucker implements Serializable {


    private final PlayerColors color;
    private final String nickname;
    private final SpaceShip spaceShip;
    private int position; //posizione assoluta
    private int cosmicCredit;
    private BuildingState buildingState;
    private final ArrayList<Component> keptComponents;
    private Component selectedComponent;

    public Trucker(String nickName, PlayerColors color) {
        this.nickname = nickName;
        this.color = color;
        this.spaceShip = new SpaceShip(color);
        this.cosmicCredit = 0;
        buildingState=BuildingState.BUILDING;
        keptComponents = new ArrayList<>();
        this.position=-1;
    }

    public ArrayList<Component> getKeptComponents() {
        return keptComponents;
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



    public String getNickname() {return nickname;}

    public SpaceShip getSpaceShip() {
        return this.spaceShip;
    }


    public Stack showStack(int index, Game game) throws GeneralError{
        try {
            return game.viewStack(index);
        } catch (GeneralError e) {
            throw new GeneralError(e.getMessage());
        }
    }

    public void keep(Component component) throws InvalidComponentsException {
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

    public void selectComponent(int index, AvailableComponents availableComponents) throws GeneralError {
        if(availableComponents.getComponents().get(index).getSelected()){
            throw new GeneralError("Component already selected");
        }
        Component component = availableComponents.getComponents().get(index);
        synchronized (component) {
            this.selectedComponent = component;
        }

    }


//    public void finish() {
//        if(!spaceShip.wellDone()){
//            spaceShip.increaseLost(keptComponents.size());
//            keptComponents.clear();
//        }
//    }

//    public void fillSpaceship(ArrayList<int[]> coordAliens){
//        spaceShip.setBattery();
//        int goodsCapacity = spaceShip.getGoodsCapacity();
//        spaceShip.setNumAlien(coordAliens);
//        spaceShip.setNumHuman();
//    }

    public Component getKeptComponent(int index) throws GeneralError{
        if(!(0 <= index && index < keptComponents.size())){
            throw new GeneralError("There isn't a kept component at this index " + index);
        }
        return keptComponents.get(index);
    }

    public void addKeptComponent(Component keptComponent) throws GeneralError {
        if(keptComponents.size()<2){
            this.keptComponents.add(keptComponent);
        }else{
            throw new GeneralError("Can't add kept component to the list cause it's already full");
        }
    }

    public void removeKeptComponent(int index) {
        keptComponents.remove(index);
    }
}

