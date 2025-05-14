package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.TurnManager;
import it.polimi.ingsw.utilities.exceptions.CannotTurnHourglassException;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckerTest {
    private Trucker trucker;

    @BeforeEach
    public void setup(){

        this.trucker =new Trucker("frenk", PlayerColors.BLUE);
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
        assertEquals(0, trucker.getCosmicCredit());
        trucker.addCosmicCredit(2);
        assertEquals(2, trucker.getCosmicCredit());
        trucker.addCosmicCredit(3);
        assertEquals(5, trucker.getCosmicCredit());
    }

    @Test
    public void getSpaceship(){
        assertNotNull(trucker.getSpaceShip());
    }

    @Test
    public void showStack(){
        //assertNotNull(trucker.showStack(1,new Game()));
    }

    @Test
    public void keep() {
        trucker.setBuildingState(BuildingState.KEEP);
        Game game=new Game(2,2);
        Component comp=new Load(1,1,1,1,"Load","",1,false);
        try{
            trucker.keep(comp,game);
            assertEquals(BuildingState.BUILDING, trucker.getBuildingState());
        } catch (InvalidComponentsException e) {} catch (InvalidStateException e) {

        }

        trucker.setBuildingState(BuildingState.KEEP);
        trucker.getSpaceShip().getKept().add(comp);
        trucker.getSpaceShip().getKept().add(comp);
        try{
            trucker.keep(comp,game);

        } catch (InvalidComponentsException e) {
            System.out.println(e.getMessage());
        } catch (InvalidStateException e) {

        }
        assertEquals(BuildingState.BUILDING, trucker.getBuildingState());


    }

    @Test
    public void link() {
        Component comp=new Load(1,1,1,1,"Load","",1,false);
        trucker.setBuildingState(BuildingState.LINK);
        try {
            trucker.link(comp,6,7);
        } catch (InvalidStateException e) {

        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        assertEquals(BuildingState.BUILDING, trucker.getBuildingState());

    }

    @Test
    public void selectComponent() {
        trucker.setBuildingState(BuildingState.SELECT_COMPONENT);
        try {
            trucker.selectComponent(2, new Game(2,2).getComponents());
        } catch (InvalidComponentsException e) {
            System.out.println(e.getMessage());
        } catch (InvalidStateException e) {

        }
        assertEquals(BuildingState.SELECT_COMPONENT, trucker.getBuildingState());

    }

    @Test
    public void finish() {
    }

    @Test
    public void fillSpaceShip() {

    }
}