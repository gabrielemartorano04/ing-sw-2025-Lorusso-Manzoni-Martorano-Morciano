package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.TurnManager;
import it.polimi.ingsw.utilities.exceptions.CannotTurnHourglassException;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.model.components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckerTest {
    private TurnManager.TruckerController truckerController;

    @BeforeEach
    public void setup(){

        this.truckerController =new TurnManager.TruckerController("frenk", PlayerColors.BLUE);
    }

    @Test
    public void setAndGetBuildingState(){
        assertEquals(BuildingState.BUILDING, truckerController.getBuildingState());
        truckerController.setBuildingState(BuildingState.END);
        assertEquals(BuildingState.END, truckerController.getBuildingState());
    }

    @Test
    public void setAndGetPosition(){
        truckerController.setPosition(2);
        assertEquals(2, truckerController.getPosition());
    }

    @Test
    public void getColor(){
        assertEquals(PlayerColors.BLUE, truckerController.getColor());
    }

    @Test
    public void getNickname(){
        assertEquals("frenk", truckerController.getNickname());
    }

    @Test
    public void addAndGetCosmicCredit(){
        assertEquals(0, truckerController.getCosmicCredit());
        truckerController.addCosmicCredit(2);
        assertEquals(2, truckerController.getCosmicCredit());
        truckerController.addCosmicCredit(3);
        assertEquals(5, truckerController.getCosmicCredit());
    }

    @Test
    public void getSpaceship(){
        assertNotNull(truckerController.getSpaceShip());
    }

    @Test
    public void chooseWhatToDo(){
        truckerController.chooseWhatToDo("Turn Hourglass");
        assertEquals(BuildingState.TURN_HOURGLASS, truckerController.getBuildingState());

        truckerController.setBuildingState(BuildingState.BUILDING);
        truckerController.chooseWhatToDo("Select Component");

        assertEquals(BuildingState.SELECT_COMPONENT, truckerController.getBuildingState());

        truckerController.setBuildingState(BuildingState.BUILDING);
        truckerController.chooseWhatToDo("Show Stack");
        assertEquals(BuildingState.SHOW_STACK, truckerController.getBuildingState());

        truckerController.setBuildingState(BuildingState.BUILDING);
        truckerController.chooseWhatToDo("End");
        assertEquals(BuildingState.WAIT_END, truckerController.getBuildingState());

        truckerController.setBuildingState(BuildingState.SELECT_COMPONENT);
        truckerController.chooseWhatToDo("Link");
        assertEquals(BuildingState.LINK, truckerController.getBuildingState());

        truckerController.setBuildingState(BuildingState.SELECT_COMPONENT);
        truckerController.chooseWhatToDo("Keep");
        assertEquals(BuildingState.KEEP, truckerController.getBuildingState());

        truckerController.setBuildingState(BuildingState.SELECT_COMPONENT);
        truckerController.chooseWhatToDo("Discard");
        assertEquals(BuildingState.BUILDING, truckerController.getBuildingState());
    }

    @Test
    public void showStack(){
        //assertNotNull(trucker.showStack(1,new Game()));
    }

    @Test
    public void keep() {
        truckerController.setBuildingState(BuildingState.KEEP);
        Game game=new Game();
        Component comp=new Load(1,1,1,1,"Load","",1,false);
        try{
            truckerController.keep(comp,game);
            assertEquals(BuildingState.BUILDING, truckerController.getBuildingState());
        } catch (InvalidComponentsException e) {}

        truckerController.setBuildingState(BuildingState.KEEP);
        truckerController.getSpaceShip().getKept().add(comp);
        truckerController.getSpaceShip().getKept().add(comp);
        try{
            truckerController.keep(comp,game);

        } catch (InvalidComponentsException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(BuildingState.BUILDING, truckerController.getBuildingState());


    }

    @Test
    public void link() {
        Component comp=new Load(1,1,1,1,"Load","",1,false);
        truckerController.setBuildingState(BuildingState.LINK);
        truckerController.link(comp,6,7);
        assertEquals(BuildingState.BUILDING, truckerController.getBuildingState());

    }

    @Test
    public void selectComponent() {
        truckerController.setBuildingState(BuildingState.SELECT_COMPONENT);
        try {
            truckerController.selectComponent(2,new Game());
        } catch (InvalidComponentsException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(BuildingState.SELECT_COMPONENT, truckerController.getBuildingState());

    }

    @Test
    public void turnHourglass() {
        truckerController.setBuildingState(BuildingState.TURN_HOURGLASS);
        Game game=new Game();
        try {
            truckerController.turnHourglass(game);
        } catch (CannotTurnHourglassException e) {
            System.out.println(e.getMessage());
        }
        if(game.getHourglass().getTimeTurns()==2)
            assertEquals(BuildingState.WAIT_END, truckerController.getBuildingState());
        else assertEquals(BuildingState.BUILDING, truckerController.getBuildingState());
    }

    @Test
    public void finish() {
    }

    @Test
    public void fillSpaceShip() {

    }
}