package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class TruckerTest {

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
        //moka il game
//        Game game=new Game();
//        assertNotNull(trucker.showStack(1,game));
//        assertEquals(BuildingState.BUILDING, trucker.getBuildingState());
    }

    @Test
    public void keep() {
        Component component=new Cabin(1,1,1,1,"Cabin","",PlayerColors.BLUE,false);
        Game game=new Game();
        int numOfKept=trucker.getSpaceShip().getKept().size();
        trucker.keep(component,game);
        assertTrue(trucker.getSpaceShip().getKept().contains(component));
        assertEquals(BuildingState.BUILDING, trucker.getBuildingState());

        Component com1=new Load(2,3,2,1,"Load","",2,true);
        Component com2=new Engine(1,0,0,"Engine","",true);
        trucker.getSpaceShip().getKept().add(com1);
        trucker.getSpaceShip().getKept().add(com2);
        trucker.keep(component,game);
        assertFalse(component.getCovered());
        assertTrue(game.getComponents().getComponents().contains(component));
    }

    @Test
    public void link() {
        Component component1=new BatteryStorage(2,1,2,1,"Battery Storage","",2);

        assertThrows(InvalidCoordinates.class, () -> { trucker.link(component1,5,7); });

        Component component2=new Pipes(3,3,3,3,"Pipes","");
        trucker.link(component2,7,8 );
        assertEquals(component2, trucker.getSpaceShip().getComponents()[7][8]);

    }

    @Test
    public void selectComponent() {
        Game game=new Game();
        ArrayList<Component> av=game.getComponents().getComponents();
        int index1=4;
        assertEquals(av.get(index1),trucker.selectComponent(index1,game));
        int index2=159;
        assertThrows(InvalidComponentsException.class, () -> { trucker.selectComponent(index2,game); });
        assertEquals(trucker.getBuildingState(), BuildingState.SELECT_COMPONENT);
    }

    @Test
    public void turnHourglass() {
        Game game=new Game();
        trucker.turnHourglass(game);
        assertEquals(game.getHourglass().getTimeTurns(),1);
        assertEquals(trucker.getBuildingState(), BuildingState.BUILDING);

        trucker.turnHourglass(game);
        assertEquals(game.getHourglass().getTimeTurns(),2);
        assertEquals(trucker.getBuildingState(), BuildingState.WAIT_END);

        assertThrows(CannotTurnHourglassException.class, () -> { trucker.turnHourglass(game); });
    }

    @Test
    public void finish() {
    }

    @Test
    public void fillSpaceShip() {

    }
}