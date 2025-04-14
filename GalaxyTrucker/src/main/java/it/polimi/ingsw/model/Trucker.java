package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CannotTurnHourglassException;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;

import java.util.ArrayList;

/**
 *
 */

/**
 *
 */
public class Trucker {


    private PlayerColors color;
    private String nickname;
    private final SpaceShip spaceShip;
    private int position; //posizione assoluta
    private int cosmicCredit;
    private BuildingState buildingState;

    public Trucker(String nickName, PlayerColors color) {
        this.nickname = nickName;
        this.color = color;
        this.spaceShip = new SpaceShip();
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





    //FSM

    public void chooseWhatToDo(String cmd){
        switch(buildingState){
            case BUILDING:
                switch (cmd){
                    case "Turn Hourglass":
                        buildingState=BuildingState.TURN_HOURGLASS;
                        break;
                    case "Select Component":
                        buildingState=BuildingState.SELECT_COMPONENT;
                        break;
                    case "Show Stack":
                        buildingState=BuildingState.SHOW_STACK;
                        break;
                    case "End":
                        buildingState=BuildingState.WAIT_END;

                }
            case SELECT_COMPONENT:
                switch (cmd){
                    case "Link":
                        buildingState=BuildingState.LINK;
                        break;
                    case "Keep":
                        buildingState=BuildingState.KEEP;
                        break;
                    case "Discard":
                        buildingState=BuildingState.BUILDING;
                        break;
                }

        }


    }


    public ArrayList<Card> showStack(int index, Game game){
        if(buildingState!=BuildingState.SHOW_STACK){
            throw new IllegalStateException("Trucker is not in show stack state");
        }
        buildingState=BuildingState.BUILDING;
        return game.viewStack(index);

    }

    public void keep(Component component, Game game) throws InvalidComponentsException{
        if(buildingState!=BuildingState.KEEP){
            throw new IllegalStateException("Trucker is not in keep component state");
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

    public void link(Component component, int x, int y){
        if(buildingState!=BuildingState.LINK){
            throw new IllegalStateException("Trucker is not in link component state");
        }
        try{
            spaceShip.addComponent(component,x,y);
        } catch (InvalidCoordinates e) {
            buildingState=BuildingState.LINK;
        }
        buildingState=BuildingState.BUILDING;
    }

    public Component selectComponent(int index, Game game){
        if(buildingState!=BuildingState.SELECT_COMPONENT){
            throw new IllegalStateException("Trucker is not in select component state");
        }
        if (index > 0 && index < game.getComponents().getComponents().size()) {
            Component selectedComponent;
            selectedComponent = game.getComponents().getComponents().get(index);
            game.getComponents().getComponents().remove(index);
            buildingState=BuildingState.SELECT_COMPONENT;
            return selectedComponent;
        } else {
            buildingState=BuildingState.SELECT_COMPONENT;
            throw new InvalidComponentsException("You're not in the range of components");
        }

    }


    public void turnHourglass(Game game) throws CannotTurnHourglassException{
        if(buildingState!=BuildingState.TURN_HOURGLASS){
            throw new IllegalStateException("Trucker is not in turn hourglass state");
        }
        if(game.getHourglass().getTimeTurns()==0 && game.getHourglass().timeIsUp()){
            game.getHourglass().reset();
            buildingState=BuildingState.BUILDING;
        } else if (game.getHourglass().getTimeTurns()==1 && game.getHourglass().timeIsUp()){
            game.getHourglass().reset();
            buildingState=BuildingState.WAIT_END;
        } else {
            buildingState=BuildingState.BUILDING;
            throw new CannotTurnHourglassException("You can't turn the hourglass now!");
        }
    }

    public void finish(){
        if(buildingState!=BuildingState.END){
            throw new IllegalStateException("Trucker is not in finish state");
        }
        if(!spaceShip.wellDone()){
            spaceShip.increaseLost(spaceShip.getKept().size());
            spaceShip.getKept().removeAll(spaceShip.getKept());
        }
    }

}

