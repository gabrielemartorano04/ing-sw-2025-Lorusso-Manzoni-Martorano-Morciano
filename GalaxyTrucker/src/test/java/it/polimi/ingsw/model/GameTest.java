package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.OpenSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Deck deck;
    private SpaceShip ship;
    private Trucker trucker1, trucker2;
    private Hourglass hourglass;
    private Card card;

    @BeforeEach
    void setUp() {
        game = new Game();
        deck = new Deck();
        ship = new SpaceShip();
        hourglass = new Hourglass(game);
        trucker1 = new Trucker("frenk", PlayerColors.BLUE);
        game.addTrucker(trucker1);
        trucker2 = new Trucker("manzo", PlayerColors.RED);
        game.addTrucker(trucker2);
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

    }

    @Test
    void rollDice() {
        int sum;
        sum = game.rollDice();
        assertTrue(sum >= 1 && sum <= 12);
    }

    @Test
    void drawCard() {

    }

    @Test
    void noHumans() {

    }

    @Test
    void viewStack() {

    }

    @Test
    void checkOvertaken() {

    }

    @Test
    void withdraw() {

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
        trucker1.setPosition(1);
        trucker2.setPosition(2);
        game.moveForward(trucker2, 1);
        assertEquals(3, trucker2.getPosition());
        game.moveForward(trucker1, 3);
        assertEquals(5, trucker1.getPosition());
    }

    @Test
    void moveBackward() {
        trucker1.setPosition(5);
        trucker2.setPosition(4);
        game.moveBackward(trucker2, 1);
        assertEquals(3, trucker2.getPosition());
        game.moveBackward(trucker1, 3);
        assertEquals(1, trucker1.getPosition());
    }

    @Test
    void ready() {
        game.ready(trucker1);
        assertEquals(BuildingState.END,game.getTrucker(0).getBuildingState());
        game.ready(trucker2);
        assertEquals(BuildingState.END,game.getTrucker(1).getBuildingState());
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
        assertTrue(colors.contains(trucker1.getColor()));
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
        Trucker trucker = new Trucker("gabri", PlayerColors.YELLOW);
        game.addTrucker(trucker);
        assertTrue(game.getTruckers().contains(trucker));
    }

    @Test
    void setupGame() {
        game.setupGame();
        assertEquals(GameState.SETUP,game.getGameState());
        assertEquals(2,game.getTruckers().size());
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
        for (Trucker trucker : game.getTruckers()) {
            trucker.setBuildingState(BuildingState.END);
        }
        assertTrue(game.allPlayersReady());
    }

    @Test
    void getHourglass() {
        assertNotNull(game.getHourglass());
        assertEquals(hourglass,game.getHourglass());
    }
}