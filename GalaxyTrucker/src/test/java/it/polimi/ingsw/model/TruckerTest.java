package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.exceptions.CannotTurnHourglassException;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.model.components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckerTest {
    private Trucker trucker;

    @BeforeEach
    public void setup(){

        this.trucker=new Trucker("frenk", PlayerColors.BLUE);
    }

    @Test
    public void setAndGetBuildingState(){
        assertEquals(BuildingState.BUILDING, trucker.getBuildingState());
        trucker.setBuildingState(BuildingState.END);
        assertEquals(BuildingState.END, trucker.getBuildingState());
    }

    @Test
    public void setAndGetPosition(){
        trucker.setPosition(2);
        assertEquals(2, trucker.getPosition());
    }

    @Test
    public void getColor(){
        assertEquals(PlayerColors.BLUE, trucker.getColor());
    }

    @Test
    public void getNickname(){
        assertEquals("frenk", trucker.getNickname());
    }

    @Test
    public void addAndGetCosmicCredit(){
        assertEquals(0,trucker.getCosmicCredit());
        trucker.addCosmicCredit(2);
        assertEquals(2,trucker.getCosmicCredit());
        trucker.addCosmicCredit(3);
        assertEquals(5,trucker.getCosmicCredit());
    }

    @Test
    public void getSpaceship(){
        assertNotNull(trucker.getSpaceShip());
    }

    @Test
    public void chooseWhatToDo(){
        trucker.chooseWhatToDo("Turn Hourglass");
        assertEquals(BuildingState.TURN_HOURGLASS, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.BUILDING);
        trucker.chooseWhatToDo("Select Component");

        assertEquals(BuildingState.SELECT_COMPONENT, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.BUILDING);
        trucker.chooseWhatToDo("Show Stack");
        assertEquals(BuildingState.SHOW_STACK, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.BUILDING);
        trucker.chooseWhatToDo("End");
        assertEquals(BuildingState.WAIT_END, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.SELECT_COMPONENT);
        trucker.chooseWhatToDo("Link");
        assertEquals(BuildingState.LINK, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.SELECT_COMPONENT);
        trucker.chooseWhatToDo("Keep");
        assertEquals(BuildingState.KEEP, trucker.getBuildingState());

        trucker.setBuildingState(BuildingState.SELECT_COMPONENT);
        trucker.chooseWhatToDo("Discard");
        assertEquals(BuildingState.BUILDING, trucker.getBuildingState());
    }

    @Test
    public void showStack(){
        //assertNotNull(trucker.showStack(1,new Game()));
    }

    @Test
    public void keep() {
        trucker.setBuildingState(BuildingState.KEEP);
        Game game=new Game();
        Component comp=new Load(1,1,1,1,"Load","",1,false);
        try{
            trucker.keep(comp,game);
            assertEquals(BuildingState.BUILDING, trucker.getBuildingState());
        } catch (InvalidComponentsException e) {}

        trucker.setBuildingState(BuildingState.KEEP);
        trucker.getSpaceShip().getKept().add(comp);
        trucker.getSpaceShip().getKept().add(comp);
        try{
            trucker.keep(comp,game);

        } catch (InvalidComponentsException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(BuildingState.BUILDING, trucker.getBuildingState());


    }

    @Test
    public void link() {
        Component comp=new Load(1,1,1,1,"Load","",1,false);
        trucker.setBuildingState(BuildingState.LINK);
        trucker.link(comp,6,7);
        assertEquals(BuildingState.BUILDING, trucker.getBuildingState());

    }

    @Test
    public void selectComponent() {
        trucker.setBuildingState(BuildingState.SELECT_COMPONENT);
        try {
            trucker.selectComponent(2,new Game());
        } catch (InvalidComponentsException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(BuildingState.SELECT_COMPONENT, trucker.getBuildingState());

    }

    @Test
    public void turnHourglass() {
        trucker.setBuildingState(BuildingState.TURN_HOURGLASS);
        Game game=new Game();
        try {
            trucker.turnHourglass(game);
        } catch (CannotTurnHourglassException e) {
            System.out.println(e.getMessage());
        }
        if(game.getHourglass().getTimeTurns()==2)
            assertEquals(BuildingState.WAIT_END, trucker.getBuildingState());
        else assertEquals(BuildingState.BUILDING, trucker.getBuildingState());
    }

    @Test
    public void finish() {
    }

    @Test
    public void fillSpaceShip() {

    }
}