package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.components.Engine;
import it.polimi.ingsw.model.components.Load;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckerTest {

    private Trucker trucker;

    @BeforeEach
    void setUp() {
        this.trucker =new Trucker("frenk", PlayerColors.BLUE);
        this.trucker.getSpaceShip().initSpaceShip(2,PlayerColors.BLUE);
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
    public void addAndGetCosmicCredit(){
        assertEquals(0, trucker.getCosmicCredit());
        trucker.addCosmicCredit(2);
        assertEquals(2, trucker.getCosmicCredit());
        trucker.addCosmicCredit(3);
        assertEquals(5, trucker.getCosmicCredit());
    }

    @Test
    void getSelectedComponent() {
        AvailableComponents av=new AvailableComponents();
        try {
            trucker.selectComponent(1,av);
        } catch (GeneralError e) {
            System.out.println(e.getMessage());
        }
        assertNotNull(trucker.getSelectedComponent());
        assertEquals(av.getComponents().get(1),trucker.getSelectedComponent());

    }

    @Test
    public void getSpaceship(){
        assertNotNull(trucker.getSpaceShip());
    }

    @Test
    public void getNickname(){
        assertEquals("frenk", trucker.getNickname());
    }


    @Test
    void keepAndGetKeptComponentAndGetComponents() {
        Load load=new Load(1,1,1,1,"Load","",2,false);
        Engine engine=new Engine(1,1,1,"Engine","",true);
        try {
            trucker.keep(load);
            trucker.keep(engine);
        } catch (InvalidComponentsException e) {
            throw new RuntimeException(e);
        }
        assertThrows(InvalidComponentsException.class, () -> {
            trucker.keep(engine);
        });

        try {
            assertNotNull(trucker.getKeptComponent(0));
            assertEquals(trucker.getKeptComponent(0),load);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
        try {
            assertNotNull(trucker.getKeptComponent(1));
            assertEquals(trucker.getKeptComponent(1),engine);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
        assertEquals(2,trucker.getKeptComponents().size());
        assertThrows(GeneralError.class, () -> { trucker.getKeptComponent(3);});
    }

    @Test
    public void showStack(){
        try {
            assertNotNull(trucker.showStack(1,new Game(2,2)));
            assertNull(trucker.showStack(3,new Game(2,2)));
        } catch (GeneralError e) {
            System.out.println(e.getMessage());
        }
        Game game=new Game(2,2);
        game.viewStacks()[1].setSelected();

        assertThrows(GeneralError.class, () -> {
            trucker.showStack(1,game);
        });

    }

    @Test
    public void link() {
        try {
            trucker.link(new Load(1,1,1,1,"Load","",2,false),8,7);
        } catch (InvalidCoordinates e) {}

        assertNotNull(trucker.getSpaceShip().getComponents()[8][7]);

    }

    @Test
    public void selectComponent() {
        Game game=new Game(2,2);
        try {
            trucker.selectComponent(2, game.getComponents());
        }  catch (GeneralError e) {
            throw new RuntimeException(e);
        }
        game.getComponents().getComponents().get(4).setSelected(true);
        assertThrows(GeneralError.class, () -> { trucker.selectComponent(4, game.getComponents()); });

    }

    @Test
    void removeKeptComponent() {
        Load load=new Load(1,1,1,1,"Load","",2,false);
        Engine engine=new Engine(1,1,1,"Engine","",true);
        try {
            trucker.keep(load);

        }  catch (InvalidComponentsException e) {
            throw new RuntimeException(e);
        }
        try {
            trucker.keep(engine);

        } catch (InvalidComponentsException e) {
            throw new RuntimeException(e);
        }

        trucker.removeKeptComponent(0);
        assertEquals(1,trucker.getKeptComponents().size());
        try {
            assertEquals(engine,trucker.getKeptComponent(0));
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> {trucker.removeKeptComponent(3);});
    }

    @Test
    void setSpaceShip() {
        CreateShipTest support=new CreateShipTest();
        SpaceShip spaceShip = null;
        try {
            spaceShip=support.createShip3(spaceShip);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        trucker.setSpaceShip(spaceShip);
        assertEquals(spaceShip,trucker.getSpaceShip());
    }

    @Test
    void initSpaceShip() {
        trucker.initSpaceShip(2);
        assertNotNull(trucker.getSpaceShip());
    }

}