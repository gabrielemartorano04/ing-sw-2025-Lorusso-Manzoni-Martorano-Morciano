package it.polimi.ingsw;
import java.util.ArrayList;
import java.util.Timer;

//gestisce la partita
public class Game {
    //attributi
    private ArrayList<Trucker> truckers;
    private Card[] cardStack;
    private Card[] gameDeck;
    private int numPlayer;
    private int[] dice;
    private Component[] components;
    private Timer timer;
    private boolean mode;
    private String gameState; // modificare in enum di GameState

    public Game() {}

    // model
    public void setupGame() {}

    public String getGameState() {
        return "";
    } // da cambiare in GameState

    public void setGameState(String gameState) {}

    public void manageTime(Timer timer) {}

    public void sortTruckers() {}

    public int getNumPlayer() {
        return 0;
    }

    public void setNumPlayer() {}

    public int rollDice(int[] dice, Trucker trucker) {
        return 0;
    }

    public Card drawCard(Trucker trucker) {
        return null;
    }

    public Card[] viewStack() {
        return new Card[0];
    }

    public void checkOvertaken(Trucker trucker) {}

    public void withdraw(Trucker trucker) {}

    public Trucker getTrucker() {
        return null;
    }

    public Component[] getComponents() {
        return new Component[0];
    }

    public void setComponents(Component[] components) {}

    public void makeStack(Card[] cards) {}

    public void makeDeck() {}

    //controller
    public void startGame() {}

    public void endGame() {}


    public void setMode() {};
}
