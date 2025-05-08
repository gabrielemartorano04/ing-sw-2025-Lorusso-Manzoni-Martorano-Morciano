package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.utilities.exceptions.ColorNotAvailable;
import it.polimi.ingsw.utilities.exceptions.NicknameUsedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController=new GameController(3);
    }

    @Test
    void tryAddPlayer() throws ColorNotAvailable, NicknameUsedException {
        gameController.tryAddPlayer("frenk", PlayerColors.BLUE);
        try{
            gameController.tryAddPlayer("ciao", PlayerColors.BLUE);
        }
        catch(Exception e){}
    }

    @Test
    void startBuildingPhase() {
    }

    @Test
    void selectComponent() {
    }

    @Test
    void keepComponent() {
    }

    @Test
    void linkComponent() {
    }

    @Test
    void turnHourglass() {
    }

    @Test
    void showStack() {
    }

    @Test
    void endBuildingFor() {
    }
}