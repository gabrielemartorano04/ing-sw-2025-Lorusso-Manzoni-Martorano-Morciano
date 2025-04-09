package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.CannotTurnHourglassException;
import it.polimi.ingsw.Exceptions.InvalidComponentsException;
import it.polimi.ingsw.Exceptions.InvalidCoordinates;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;

import javax.swing.plaf.basic.BasicButtonUI;
import java.util.ArrayList;

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

    public void addCosmicCredit(int cosmicCredit) {
        this.cosmicCredit += cosmicCredit;
    }

    public PlayerColors getColor() {
        return color;
    }

    public String getNickname() {return nickname;}

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public int getPosition() {
        return position;
    }

    public int getCosmicCredit() {
        return cosmicCredit;
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
        Component selectedComponent;
        selectedComponent=game.getComponents().getComponents().get(index);
        //controlla index
        game.getComponents().getComponents().remove(index);
        buildingState=BuildingState.SELECT_COMPONENT;
        return selectedComponent;
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
            buildingState=BuildingState.END;
        } else {
            buildingState=BuildingState.BUILDING;
            throw new CannotTurnHourglassException("You can't turn the hourglass now!");
        }
    }

    public void finish(){
        //dobbiamo controllare la nave???????????????????????????????????????????????????'
    }

}

