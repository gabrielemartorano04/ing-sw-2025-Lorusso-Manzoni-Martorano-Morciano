package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game1;
    private Game game2;

    @BeforeEach
    void setUp() {
        game1=new Game(2,0);
        game2=new Game(2,2);
    }

    @Test
    void setupGame(){
        game1.setupGame();
        assertNotNull(game1.viewStacks());
        assertNotNull(game1.drawCard());
    }

    @Test
    void getGameLevel() {
        assertEquals(0,game1.getGameLevel());
        assertEquals(2,game2.getGameLevel());
    }

    @Test
    void setAndGetIstance() {
        game1.setIstance(game1);
        assertEquals(game1,game1.getIstance());
        game2.setIstance(game2);
        assertEquals(game2,game2.getIstance());
        game1.setIstance(game2);
        assertEquals(game2,game1.getIstance());
    }

    @Test
    void startAndGetHourglass() {
        game1.startHourglass();
        assertNull(game1.getHourglass());
        game2.startHourglass();
        assertNotNull(game2.getHourglass());
    }

    @Test
    void getNumPlayers() {
        assertEquals(2,game1.getNumPlayers());
        assertEquals(2,game2.getNumPlayers());
    }

    @Test
    void getBank() {
        assertNotNull(game1.getBank());
        assertNotNull(game2.getBank());
    }

    @Test
    void sortTruckers() {
        game1.addTrucker(new Trucker("frenk",PlayerColors.BLUE));
        game1.addTrucker(new Trucker("manzo",PlayerColors.RED));
        game1.setGameState(GameState.FLIGHT);
        assertThrows(IllegalStateException.class, () ->         game1.addTrucker(new Trucker("manzo",PlayerColors.RED)));
        assertEquals("frenk", game1.getTruckerList().get(0).getNickname());
        assertEquals("manzo", game1.getTruckerList().get(1).getNickname());
        game1.getTrucker(0).setPosition(3);
        game1.getTrucker(1).setPosition(5);
        game1.sortTruckers();
        assertEquals("manzo", game1.getTruckerList().get(0).getNickname());
        assertEquals("frenk", game1.getTruckerList().get(1).getNickname());
    }

    @Test
    void rollDice() {
        int sum;
        sum = game1.rollDice();
        assertTrue(sum >= 2 && sum <= 12);
    }

    @Test
    void drawCard() {
        game2.makeStacks();
        game2.makeGameDeck();
        assertNotNull(game2.drawCard());

        game1.makeGameDeck();
        assertNotNull(game1.drawCard());
    }

    @Test
    void withdraw() {
        Trucker trucker=new Trucker("frenk",PlayerColors.BLUE);
        game1.addTrucker(trucker);
        game1.withdraw(trucker);
        assertFalse(game1.getTruckerList().contains(trucker));
    }

    @Test
    void getTrucker() {
        Trucker trucker1=new Trucker("frenk",PlayerColors.BLUE);
        Trucker trucker2=new Trucker("manzo",PlayerColors.RED);
        game1.addTrucker(trucker1);
        game1.addTrucker(trucker2);
        assertEquals(trucker1,game1.getTruckerList().get(0));
        assertEquals(trucker2,game1.getTruckerList().get(1));
    }

    @Test
    void getComponents() {
        assertNotNull(game1.getComponents());
        assertNotNull(game2.getComponents());
    }

    @Test
    void makeAndViewStacks() {
        game2.makeStacks();
        try {
            assertNotNull(game2.viewStack(0));
            assertNotNull(game2.viewStack(1));
            assertNotNull(game2.viewStack(2));
            assertNotNull(game2.viewStacks());
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void makeGameDeck() {
        game2.makeStacks();
        game2.makeGameDeck();
        assertNotNull(game2.drawCard());

        game1.makeGameDeck();
        assertNotNull(game1.drawCard());
    }

    @Test
    void getTruckerList() {
        Trucker trucker1=new Trucker("frenk",PlayerColors.BLUE);
        Trucker trucker2=new Trucker("manzo",PlayerColors.RED);
        game1.addTrucker(trucker1);
        game1.addTrucker(trucker2);
        assertEquals(game1.getTruckerList().size(),2);
    }


    @Test
    void moveForward() {
        Trucker trucker1=new Trucker("frenk",PlayerColors.BLUE);
        Trucker trucker2=new Trucker("manzo",PlayerColors.RED);
        game1.addTrucker(trucker1);
        game1.addTrucker(trucker2);
        trucker1.setPosition(2);
        trucker2.setPosition(3);
        game1.moveForward(trucker1,1);
        assertEquals(4,trucker1.getPosition());
    }

    @Test
    void moveBackward() {
        Trucker trucker1=new Trucker("frenk",PlayerColors.BLUE);
        Trucker trucker2=new Trucker("manzo",PlayerColors.RED);
        game1.addTrucker(trucker1);
        game1.addTrucker(trucker2);
        trucker1.setPosition(2);
        trucker2.setPosition(3);
        game1.moveBackward(trucker2,1);
        assertEquals(1,trucker2.getPosition());
    }

    @Test
    void setLevel() {
        game1.setLevel(2);
        assertEquals(2,game1.getGameLevel());
        game2.setLevel(0);
        assertEquals(0,game2.getGameLevel());
    }

    @Test
    void isHourglassExpired() {
        Object lock = new Object();
        game2.startHourglass();
        assertFalse(game2.isHourglassExpired());
        synchronized (lock) {
            try {
                lock.wait(91000); // aspetta 90 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        game2.getHourglass().reset();
        synchronized (lock) {
            try {
                lock.wait(91000); // aspetta 90 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        game2.getHourglass().reset();
        synchronized (lock) {
            try {
                lock.wait(91000); // aspetta 90 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertTrue(game2.isHourglassExpired());
    }

    @Test
    void setAndGetGameState(){
        game1.setGameState(GameState.FLIGHT);
        assertEquals(GameState.FLIGHT,game1.getGameState());
    }

    @Test
    void endTurnAndGetTruckersOut(){
        Trucker trucker=new Trucker("frenk",PlayerColors.RED);
        game1.addTrucker(trucker);
        game1.endTurn();
        assertFalse(game1.getTruckerOut().isEmpty());
    }

    @Test
    void oldGame(){
        Trucker trucker=new Trucker("frenk",PlayerColors.RED);
        game1.addTrucker(trucker);

        game1.createOldNicknames();
        game1.createOldColors();

        assertTrue(game1.checkOldPlayers("frenk",PlayerColors.RED));
        assertFalse(game1.checkOldPlayers("manzo",PlayerColors.BLUE));

        assertFalse(game1.getOldColors().isEmpty());
        assertFalse(game1.getOldNicknames().isEmpty());
        game1.removeOldColors(PlayerColors.RED);
        game1.removeOldNicknames("frenk");

        assertTrue(game1.getOldColors().isEmpty());
        assertTrue(game1.getOldNicknames().isEmpty());
    }

    @Test
    void computeScores(){
        Trucker trucker1=new Trucker("frenk",PlayerColors.BLUE);
        Trucker trucker2=new Trucker("manzo",PlayerColors.RED);
        game1.addTrucker(trucker1);
        game1.addTrucker(trucker2);

        game1.setGameState(GameState.BUILDING);
        assertEquals(0,game1.computeScores()[0]);
        assertEquals(0,game1.computeScores()[1]);

        game1.setGameState(GameState.FLIGHT);
        SpaceShip spaceShip1=null;
        SpaceShip spaceShip2=null;
        CreateShipTest support=new CreateShipTest();
        try {
            trucker1.setSpaceShip(support.createShip1(spaceShip1));
            trucker2.setSpaceShip(support.createShip2(spaceShip2));
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        //todo calcola dei punteggi sulla base della nave ma devi aggiungere dei goods e cosmic credits e lost e truckers out

    }

    @Test
    void bestSpaceship(){
        Trucker trucker1=new Trucker("frenk",PlayerColors.BLUE);
        Trucker trucker2=new Trucker("manzo",PlayerColors.RED);
        game1.addTrucker(trucker1);
        game1.addTrucker(trucker2);
        SpaceShip spaceShip1=null;
        SpaceShip spaceShip2=null;
        CreateShipTest support=new CreateShipTest();
        try {
            trucker1.setSpaceShip(support.createShip1(spaceShip1));
            trucker2.setSpaceShip(support.createShip3(spaceShip2));
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        assertTrue(game1.bestSpaceShip(trucker1));
        assertTrue(game1.bestSpaceShip(trucker2));
    }

    @Test
    void isNicknameTaken(){
        Trucker trucker1=new Trucker("frenk",PlayerColors.BLUE);
        game1.addTrucker(trucker1);
        assertTrue(game1.isNicknameTaken("frenk"));
    }

    @Test
    void isColorTaken(){
        Trucker trucker1=new Trucker("frenk",PlayerColors.BLUE);
        game1.addTrucker(trucker1);
        assertTrue(game1.isColorTaken(PlayerColors.BLUE));
    }

}