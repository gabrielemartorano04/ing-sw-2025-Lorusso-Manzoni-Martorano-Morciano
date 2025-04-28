package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.OpenSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Deck deck;
    private SpaceShip ship;
    private Trucker trucker1,trucker2;

    @BeforeEach
    void setUp() {
        game = new Game();
        deck = new Deck();
        ship = new SpaceShip();
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
        game.noHumans(trucker1);
        assertFalse(game.getTruckers().contains(game.getTrucker(0)));
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
        assertEquals("manzo", game.getTrucker(1).getNickname());
    }

    @Test
    void getTruckers() {
        assertEquals(2, game.getTruckers().size());
        assertEquals("frenk", game.getTruckers().get(0).getNickname());
        assertEquals("manzo", game.getTruckers().get(1).getNickname());
    }

    @Test
    void getComponents() {
    }

    @Test
    void makeStacks() {
    }

    @Test
    void makeGameDeck() {
    }

    @Test
    void moveForward() {
    }

    @Test
    void moveBackward() {
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
    }

    @Test
    void isNicknameTaken() {
    }

    @Test
    void isColorTaken() {
    }

    @Test
    void addTrucker() {
        Trucker trucker = new Trucker("gabri", PlayerColors.YELLOW);
        game.addTrucker(trucker);
        assertTrue(game.getTruckers().contains(trucker));
    }

    @Test
    void setupGame() {
    }

    @Test
    void startFlightPhase() {
    }

    @Test
    void isHourglassExpired() {
    }

    @Test
    void allPlayersReady() {
    }

    @Test
    void getHourglass() {
    }
}