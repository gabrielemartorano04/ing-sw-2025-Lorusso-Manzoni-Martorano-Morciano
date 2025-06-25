package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Engine;
import it.polimi.ingsw.model.components.Pipes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckerControllerTest {
    TruckerController truckerController;
    TruckerController truckerController2;
    @BeforeEach
    void setUp() {
        Trucker trucker = new Trucker("frenk", PlayerColors.RED);
        truckerController=new TruckerController(trucker);
        truckerController.initSpaceShip(2);
        truckerController2=new TruckerController("manzo", PlayerColors.BLUE);
    }

    @Test
    void setAndGetPosition() {
        truckerController.setPosition(2);
        assertEquals(2,truckerController.getPosition());
    }


    @Test
    void getColor() {
        assertEquals(PlayerColors.RED,truckerController.getColor());
    }

    @Test
    void getNickname() {
        assertEquals("frenk", truckerController.getNickname());
    }

    @Test
    void getSpaceShip() {
        assertNotNull(truckerController.getSpaceShip());
    }

    @Test
    void showStack() {
        Game game=new Game(2,2);
        try {
            assertNotNull(truckerController.showStack(1,game));
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void link() {
        Component pipes=new Pipes(3,3,3,3,"","");
        try {
            truckerController.link(pipes,6,7);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void selectComponent() {
        try {
            truckerController.selectComponent(2, new AvailableComponents());
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void keepAndGetKeptComponent() {
        Component engine=new Engine(3,3,3,"","",true);
        try {
            truckerController.keep(engine);
            assertThrows(GeneralError.class,()->{truckerController.getKeptComponent(1);});
            truckerController.keep(engine);
        } catch (InvalidComponentsException e) {
            throw new RuntimeException(e);
        }
        try {
            assertEquals(engine,truckerController.getKeptComponent(0));
            assertEquals(engine,truckerController.getKeptComponent(1));

        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
        assertThrows(InvalidComponentsException.class, () -> truckerController.keep(engine));


    }


    @Test
    void getTrucker() {
        assertNotNull(truckerController.getTrucker());
    }
}