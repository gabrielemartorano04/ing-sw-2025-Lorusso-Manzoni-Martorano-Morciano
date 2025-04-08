package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.CannotTurnHourglassException;
import it.polimi.ingsw.Model.Components.Component;

import javax.swing.plaf.basic.BasicButtonUI;

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

    public void chooseWhatToDo(String cmd, int index, Game game){
        switch(buildingState){
            case START:
                game.getHourglass().reset();
                buildingState=BuildingState.BUILDING;
                break;
            case BUILDING:
                switch (cmd){
                    case "Turn Hourglass":
                        buildingState=BuildingState.TURN_HOURGLASS;
                        break;
                    case "Turn Hourglass end":
                        buildingState=BuildingState.TURN_HOURGLASS_END;
                        break;
                    case "Select Component":
                        buildingState=BuildingState.SELECT_COMPONENT;
                        break;
                    case "Show Stack":


                }
            case SELECT_COMPONENT:
                chooseComponent(index, game);
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
            case SHOW_STACK:
            case TURN_HOURGLASS:
                try{
                    turnHourglass(game);
                }
                catch(CannotTurnHourglassException e){
                    buildingState=BuildingState.BUILDING;
                }
                buildingState=BuildingState.BUILDING;
            case TURN_HOURGLASS_END:
                try{
                    turnHourglassEnd(game);
                }
                catch(CannotTurnHourglassException e){
                    buildingState=BuildingState.END;
                }
            case LINK:
            case KEEP:
            case END:
        }

    }

    public Component chooseComponent(int index, Game game) throws IllegalStateException {
        if(buildingState!=BuildingState.SELECT_COMPONENT){
            throw new IllegalStateException("Card state is not SELECT_COMPONENT");
        }
        Component selectedComponent;
        selectedComponent=game.getComponents().getComponents().get(index);
        game.getComponents().getComponents().remove(index);
        buildingState=BuildingState.BUILDING;
        return selectedComponent;
    }

    public void useComponent(){

    }

    public void addComponent(){

    }

    public void turnHourglass(Game game) throws CannotTurnHourglassException{
        if(game.getHourglass().getTimeTurns()==0 && game.getHourglass().timeIsUp()){
            game.getHourglass().reset();
        }
        else throw new CannotTurnHourglassException("You can't turn the hourglass now!");
    }

    public void turnHourglassEnd(Game game) throws CannotTurnHourglassException{
        if(game.getHourglass().getTimeTurns()==1 && game.getHourglass().timeIsUp()){
            game.getHourglass().reset();
        }
        else throw new CannotTurnHourglassException("You can't turn the hourglass now!");
    }

    public void finish(){

    }

}

