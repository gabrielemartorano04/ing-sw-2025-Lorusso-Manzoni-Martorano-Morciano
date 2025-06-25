package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.ColorNotAvailable;
import it.polimi.ingsw.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.NicknameUsedException;
import it.polimi.ingsw.model.PlayerColors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class GameControllerTest {

    GameController gameController;

    @BeforeEach
    void setUp() {
        gameController=new GameController(2,2);
    }

    @Test
    void setAndGetIstance() {
        assertNotNull(gameController.getIstance());
    }

    @Test
    void getGame() {
        assertNotNull(gameController.getGame());
    }

    @Test
    void getAvailablePlayerColors() {
        assertNotNull(gameController.getAvailablePlayerColors());
        assertFalse(gameController.getAvailablePlayerColors().isEmpty());
    }

    @Test
    void tryAddPlayer() {
        try {
            gameController.tryAddPlayer("frenk", PlayerColors.RED);
        } catch (NicknameUsedException e) {
            throw new RuntimeException(e);
        } catch (ColorNotAvailable e) {
            throw new RuntimeException(e);
        }
        assertThrows(NicknameUsedException.class, () -> gameController.tryAddPlayer("frenk", PlayerColors.BLUE));
        assertThrows(ColorNotAvailable.class, () -> gameController.tryAddPlayer("manzo", PlayerColors.RED));

    }

    @Test
    void addOldPlayer() {
        try {
            gameController.tryAddPlayer("frenk", PlayerColors.RED);
        } catch (NicknameUsedException e) {
            throw new RuntimeException(e);
        } catch (ColorNotAvailable e) {
            throw new RuntimeException(e);
        }
        gameController.addOldPlayer("frenk", PlayerColors.RED);
    }

    @Test
    void pickCardFromDeck() {
        gameController.getGame().setupGame();
        assertNotNull(gameController.pickCardFromDeck());
    }

    @Test
    void startBuildingPhase() {
        gameController.startBuildingPhase();
    }

    @Test
    void showStack() {
        try {
            assertNotNull(gameController.showStack(0));
            assertNotNull(gameController.showStack(1));
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void setGameStarted() {
        gameController.setGameStarted(true);
        assertThrows(GameAlreadyStartedException.class,()->{
            gameController.tryAddPlayer("manzo",PlayerColors.BLUE);
        });
    }

    @Test
    void getCardController() {
        assertNotNull(gameController.getCardController());
    }
}