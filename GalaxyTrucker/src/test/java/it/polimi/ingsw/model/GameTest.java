package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.TurnManager;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.OpenSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.easymock.EasyMock.mock;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private ArrayList<Card> deck;
    private SpaceShip ship;
    private TurnManager.TruckerController truckerController1, truckerController2;
    private Hourglass hourglass;

    @BeforeEach
    void setUp() {
        game = new Game(2,2);
        deck = new ArrayList<Card>();
        ship = new SpaceShip(PlayerColors.BLUE);
        hourglass = new Hourglass(game);
        truckerController1 = new TurnManager.TruckerController("frenk", PlayerColors.BLUE);
        game.addTrucker(truckerController1);
        truckerController2 = new TurnManager.TruckerController("manzo", PlayerColors.RED);
        game.addTrucker(truckerController2);
    }

    @Test
    void setGameState() {
        game.setGameState(GameState.BUILDING);
        assertEquals(GameState.BUILDING,game.getGameState());
    }

    @Test
    void getGameState() {
        assertEquals(GameState.SETUP,game.getGameState());
    }

    @Test
    void getBank() {
        assertNotNull(game.getBank());
    }

    @Test
    void sortTruckers() {
        assertEquals("frenk", game.getTruckers().get(0).getNickname());
        assertEquals("manzo", game.getTruckers().get(1).getNickname());
        game.getTrucker(0).setPosition(3);
        game.getTrucker(1).setPosition(5);
        game.sortTruckers();
        assertEquals("manzo", game.getTruckers().get(0).getNickname());
        assertEquals("frenk", game.getTruckers().get(1).getNickname());
    }

    @Test
    void rollDice() {
        int sum;
        sum = game.rollDice();
        assertTrue(sum >= 2 && sum <= 12);
    }

    @Test
    void drawCard() {
        Card card = new OpenSpace(false, 2, "", "");

        assertNotNull(game.drawCard());
    }

    @Test
    void noHumans() {

    }

    @Test
    void viewStack() {

    }

    @Test
    void checkOvertaken() {
        TurnManager.TruckerController t1 = truckerController1;
        truckerController1.setPosition(1);
        truckerController2.setPosition(27);
        game.checkOvertaken(truckerController1);
        assertFalse(game.getTruckers().contains(t1));
    }

    @Test
    void withdraw() {
        TurnManager.TruckerController t1 = truckerController1;
        game.withdraw(truckerController1);
        assertFalse(game.getTruckers().contains(t1));
        assertNotNull(game.getTrucker(0).getNickname());
        //invertiti perche ho fatto sort sopra
    }

    @Test
    void getTrucker() {
        assertEquals("frenk", game.getTrucker(0).getNickname());
        assertEquals("manzo", game.getTrucker(1).getNickname());
        assertEquals(2,game.getTruckers().size());
    }

    @Test
    void getTruckers() {
        assertEquals(2, game.getTruckers().size());
        assertEquals("frenk", game.getTruckers().get(0).getNickname());
        assertEquals("manzo", game.getTruckers().get(1).getNickname());
    }

    @Test
    void getComponents() {
        assertNotNull(game.getComponents());
    }

    @Test
    void makeStacks() {

    }

    @Test
    void makeGameDeck() {

    }

    @Test
    void getTruckerList() {
        assertEquals("frenk", game.getTrucker(0).getNickname());
        assertEquals("manzo", game.getTrucker(1).getNickname());
        assertEquals(2,game.getTruckerList().size());
    }

    @Test
    void moveForward() {
        truckerController1.setPosition(1);
        truckerController2.setPosition(2);
        game.moveForward(truckerController2, 1);
        game.moveForward(truckerController1, 3);
        assertEquals(3, truckerController2.getPosition());
        assertEquals(5, truckerController1.getPosition());
    }

    @Test
    void moveBackward() {
        truckerController1.setPosition(5);
        truckerController2.setPosition(4);
        game.moveBackward(truckerController2, 1);
        game.moveBackward(truckerController1, 3);
        assertEquals(3, truckerController2.getPosition());
        assertEquals(1, truckerController1.getPosition());
    }

    @Test
    void ready() {

    }

    @Test
    void setLevel() {

    }

    @Test
    void computeScores() {

    }

    @Test
    void bestSpaceShip() {

    }

    @Test
    void showUsedColors() {
        Set<PlayerColors> colors = game.showUsedColors();
        assertTrue(colors.contains(truckerController1.getColor()));
        assertFalse(colors.contains(PlayerColors.YELLOW));
    }

    @Test
    void isNicknameTaken() {
        assertTrue(game.isNicknameTaken("frenk"));
        assertFalse(game.isNicknameTaken("gabri"));
    }

    @Test
    void isColorTaken() {
        assertTrue(game.isColorTaken(PlayerColors.BLUE));
        assertFalse(game.isColorTaken(PlayerColors.YELLOW));
    }

    @Test
    void addTrucker() {
        TurnManager.TruckerController truckerController = new TurnManager.TruckerController("gabri", PlayerColors.YELLOW);
        game.addTrucker(truckerController);
        assertTrue(game.getTruckers().contains(truckerController));
    }

    @Test
    void setupGame() {

    }

    @Test
    void startFlightPhase() {

    }

    @Test
    void isHourglassExpired() {
        Object lock = new Object();
        game.getHourglass().increaseTimeTurns();
        assertFalse(game.isHourglassExpired());
        synchronized (lock) {
            try {
                lock.wait(91000); // aspetta 90 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        game.getHourglass().reset();
        synchronized (lock) {
            try {
                lock.wait(91000); // aspetta 90 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertTrue(game.isHourglassExpired());
    }

    @Test
    void allPlayersReady() {
        for (TurnManager.TruckerController truckerController : game.getTruckers()) {
            truckerController.setBuildingState(BuildingState.END);
        }
        assertTrue(game.allPlayersReady());
    }

    @Test
    void getHourglass() {
        assertNotNull(game.getHourglass());
    }
}