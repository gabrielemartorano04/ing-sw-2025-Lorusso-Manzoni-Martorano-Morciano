package it.polimi.ingsw;
import java.util.ArrayList;
import java.util.Timer;

//gestisce la partita
public class Game {
    //attributi
    private ArrayList<Trucker> truckers;
    private Card[] cardStack;
    private ArrayList<Card> gameDeck;
    private int numPlayer;
    private int[] dice;
    private AvailableComponents[] components;
    private Timer timer;
    private boolean mode;
    private GameState gameState;
    private Deck deck;

    public Game() {
        this.truckers = new ArrayList<>();
        this.gameDeck = new ArrayList<>();
        this.numPlayer = 1;
        this.dice = new int[2];
        this.components = new AvailableComponents[156];
        this.timer = new Timer();
        this.mode = false;
        this.gameState = GameState.WAITING;


    }

    public enum GameState {
        WAITING,  // Attesa giocatori
        SETUP,    // Setup in corso
        IN_PROGRESS, // Partita in corso
        ENDED     // Partita terminata
    }

    // model
    public void setupGame() {
        this.gameState = GameState.SETUP;
        makeDeck();
        for (Trucker t: truckers) {
            //passa gestione a trucker per creare la spaceShip
        }
        makeStack();
    }

    public GameState getGameState() {
        return gameState;
    } // da cambiare in GameState

    public void setGameState(GameState gameState) {}

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

    public void makeStack(Card[] cards) {

    }

    public void makeDeck() {}

    public ArrayList<Trucker> getTruckerList() {
        return null;
    }

    //controller
    public void startGame() {}

    public void endGame() {}

    public void setMode() {};

    public int[] computeScores(){
        return new int[0];
    };
}
