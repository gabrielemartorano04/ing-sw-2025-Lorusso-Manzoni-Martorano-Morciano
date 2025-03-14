package it.polimi.ingsw;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Collections;
import java.util.Random;
import java.util.Comparator;

//gestisce la partita
public class Game {
    //attributi
    private ArrayList<Trucker> truckers;
    private ArrayList<Card>[] cardStacks;
    private ArrayList<Card> gameDeck;
    private int numPlayer;
    private int[] dice;
    private AvailableComponents[] components;
    private Timer timer;
    private int gameLevel;
    private GameState gameState;
    private Deck deck;

    public Game() {
        this.truckers = new ArrayList<>();
        this.gameDeck = new ArrayList<>();
        this.dice = new int[2];
        this.components = new AvailableComponents[156];
        this.timer = new Timer();
        this.gameState = GameState.SETUP;
        this.deck = new Deck();
        this.cardStacks = new ArrayList[4];
    }

    public enum GameState {
        SETUP,  // Attesa giocatori
        BUILDING,    // Setup in corso
        IN_PROGRESS, // Partita in corso
        ENDED     // Partita terminata
    }

    // model
    public void setupGame() {
        this.gameState = GameState.SETUP;
        makeDeck();
        makeStacks();
        for(int i=0; i<numPlayer; i++) {
            makeTruckers();
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void sortTruckers() {
        truckers.sort(Comparator.comparingInt(Trucker::getPosition).reversed());
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public void setNumPlayer(int num) {
        this.numPlayer = num;
    }

    public int rollDice(int[] dice, Trucker trucker) {
        Random rand = new Random();
        dice[0] = rand.nextInt(5) + 1 ;
        dice[1] = rand.nextInt(5) + 1;
        return dice[0]+dice[1];
    }

    // prendo la prima carta nel mazzo da gioco attivo il suo effetto e la rimuovo
    public void drawCard(Trucker trucker) {
        if (gameDeck.isEmpty()) return;
        gameDeck.removeFirst().cardEffect(this, null ); //da modificare null una volta volta implementato game controller
    }

    public ArrayList<Card> viewStack(int i) {
        if (i != 3)
            return cardStacks[i];
        else return null;
    }

    public void checkOvertaken(Trucker trucker) {
        for(Trucker t: truckers) {
            if(t.getPosition() - trucker.getPosition() >= 24){
                withdraw(trucker);
            }
        }
    }

    public void withdraw(Trucker trucker) {
        truckers.remove(trucker);
    }

    //i=posizione assoluta
    public Trucker getTrucker(int i) {
        return truckers.get(i);
    }

    public Component[] getComponents() {
        return null;
    }

    public void setComponents(Component[] components) {
    }

    public void makeStacks() {
        Random rand = new Random();
        for(ArrayList<Card> cardStack: cardStacks ) {
            cardStack.addFirst(deck.getDeck().remove(rand.nextInt(19)));
            for (int i=0; i<2; i++){
                cardStack.addFirst(deck.getDeck().remove(rand.nextInt(19) + 20));
            }
        }
    }

    public void makeDeck() {
        for (ArrayList<Card> cardStack: cardStacks ) {
            gameDeck.addAll(cardStack);
        }
        do{
            Collections.shuffle(gameDeck);
        }while(gameDeck.getFirst().getLevel() != gameLevel);
    }

    public ArrayList<Trucker> getTruckerList() {
        return truckers;
    }

    public void moveTrucker(Trucker trucker, int days){
        int result = trucker.getPosition() + days;
        trucker.setPosition(result);
    }

    public void makeTruckers(){
        for (int i=0; i<numPlayer; i++) {
            truckers.add(new Trucker());
        }
    }

    //controller
    public boolean ready(Trucker trucker){
        return false;
    } //trucker's ship is ready

    public void startGame() {}

    public void setStateGame(){}

    public void endGame() {}

    public void setLevel(int level) {};

    public void manageTime(Timer timer) {
        this.timer = timer;
    }

    public int[] computeScores(){
        return new int[0];
    };
}
