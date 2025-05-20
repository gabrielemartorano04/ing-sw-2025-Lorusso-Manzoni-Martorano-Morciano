package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.components.*;
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
    public void keep() throws GeneralError {
        trucker.setBuildingState(BuildingState.KEEP);
        Component comp=new Load(1,1,1,1,"Load","",1,false);
        try{
            trucker.keep(comp);
            assertEquals(BuildingState.BUILDING, trucker.getBuildingState());
        } catch (InvalidComponentsException e) {}

        trucker.setBuildingState(BuildingState.KEEP);
        trucker.addKeptComponent(comp);
        try{
            trucker.keep(comp);

        } catch (InvalidComponentsException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(BuildingState.BUILDING, trucker.getBuildingState());


    }

    @Test
    public void link() {
        Component comp=new Load(1,1,1,1,"Load","",1,false);
        trucker.setBuildingState(BuildingState.LINK);
        try {
            trucker.link(comp,6,7);
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
        }  catch (GeneralError e) {
            throw new RuntimeException(e);
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