package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Trucker;
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
    private Trucker trucker1, trucker2;
    private Hourglass hourglass;

    @BeforeEach
    void setUp() {
        game = new Game(2,2);
        deck = new ArrayList<Card>();
        ship = new SpaceShip(PlayerColors.BLUE);
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
        assertEquals("frenk", game.getTruckerList().get(0).getNickname());
        assertEquals("manzo", game.getTruckerList().get(1).getNickname());
        game.getTrucker(0).setPosition(3);
        game.getTrucker(1).setPosition(5);
        game.sortTruckers();
        assertEquals("manzo", game.getTruckerList().get(0).getNickname());
        assertEquals("frenk", game.getTruckerList().get(1).getNickname());
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
        Trucker t1 = trucker1;
        trucker1.setPosition(1);
        trucker2.setPosition(27);
        game.checkOvertaken(trucker1);
        assertFalse(game.getTruckerList().contains(t1));
    }

    @Test
    void withdraw() {
        Trucker t1 = trucker1;
        game.withdraw(trucker1);
        assertFalse(game.getTruckerList().contains(t1));
        assertNotNull(game.getTrucker(0).getNickname());
        //invertiti perche ho fatto sort sopra
    }

    @Test
    void getTrucker() {
        assertEquals("frenk", game.getTrucker(0).getNickname());
        assertEquals("manzo", game.getTrucker(1).getNickname());
        assertEquals(2,game.getTruckerList().size());
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
        game.moveForward(trucker1, 3);
        assertEquals(3, trucker2.getPosition());
        assertEquals(5, trucker1.getPosition());
    }

    @Test
    void moveBackward() {
        trucker1.setPosition(5);
        trucker2.setPosition(4);
        game.moveBackward(trucker2, 1);
        game.moveBackward(trucker1, 3);
        assertEquals(3, trucker2.getPosition());
        assertEquals(1, trucker1.getPosition());
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
        assertTrue(game.getTruckerList().contains(trucker));
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
        for (Trucker trucker : game.getTruckerList()) {
            trucker.setBuildingState(BuildingState.END);
        }
        assertTrue(game.allPlayersReady());
    }

    @Test
    void getHourglass() {
        assertNotNull(game.getHourglass());
    }
}