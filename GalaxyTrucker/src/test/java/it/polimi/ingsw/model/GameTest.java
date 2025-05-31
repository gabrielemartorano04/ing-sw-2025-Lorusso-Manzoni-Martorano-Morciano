package it.polimi.ingsw.model;

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
    void getGameLevel() {
        assertEquals(0,game1.getGameLevel());
        assertEquals(2,game2.getGameLevel());
    }

    @Test
    void getIstance() {
        assertEquals(game1,game1.getIstance());
        assertEquals(game2,game2.getIstance());
    }

    @Test
    void setIstance() {
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
    void setAndGetGameState() {
        game1.setGameState(GameState.WAITING);
        assertEquals(GameState.WAITING,game1.getGameState());
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
        assertEquals("frenk", game1.getTruckerList().get(0).getNickname());
        assertEquals("manzo", game1.getTruckerList().get(1).getNickname());
        game1.getTrucker(0).setPosition(3);
        game1.getTrucker(1).setPosition(5);
        game1.sortTruckers();
        assertEquals("manzo", game1.getTruckerList().get(0).getNickname());
        assertEquals("frenk", game1.getTruckerList().get(1).getNickname());
    }


    @Test
    void reorderPlayersByPosition() {

    }

    @Test
    void rollDice() {
        int sum;
        sum = game1.rollDice();
        assertTrue(sum >= 2 && sum <= 12);
    }

    @Test
    void drawCard() {
    }

    @Test
    void viewStack() {
    }

    @Test
    void withdraw() {
    }

    @Test
    void getTrucker() {
    }

    @Test
    void getComponents() {
        assertNotNull(game1.getComponents());
        assertNotNull(game2.getComponents());
    }

    @Test
    void makeStacks() {
    }

    @Test
    void makeGameDeck() {
    }

    @Test
    void getTruckerList() {
    }

    @Test
    void moveForward() {
    }

    @Test
    void moveBackward() {
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
    void showAvailableColors() {
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
    }

    @Test
    void setupGame() {
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
    void allPlayersReady() {
        for (Trucker trucker : game1.getTruckerList()) {
            trucker.setBuildingState(BuildingState.END);
        }
        assertTrue(game1.allPlayersReady());
    }


    @Test
    void endTurn() {
    }

    @Test
    void turnHourglass() {
    }

    @Test
    void viewStacks() {
    }

    @Test
    void getTruckerOut() {
    }
}