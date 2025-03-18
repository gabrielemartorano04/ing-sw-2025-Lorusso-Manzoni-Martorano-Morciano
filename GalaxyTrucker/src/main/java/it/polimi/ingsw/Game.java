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
    private ArrayList<Trucker> truckersOut;
    private ArrayList<Card>[] cardStacks;
    private ArrayList<Card> gameDeck;
    private int numPlayer;
    private AvailableComponents[] components;
    private Hourglass hourglass;
    private int gameLevel;
    private GameState gameState;
    private Deck deck;
    private int timeTurns;


    public Game() {
        this.truckers = new ArrayList<>();
        this.truckersOut = new ArrayList<>();
        this.gameDeck = new ArrayList<>();
        this.components = new AvailableComponents[156];
        this.hourglass = new Hourglass(this);
        this.gameState = GameState.SETUP;
        this.deck = new Deck();
        this.cardStacks = new ArrayList[4];
        this.timeTurns = 0;
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

    public int rollDice() {
        Random rand = new Random();
        int x = rand.nextInt(5) + 1 ;
        int y = rand.nextInt(5) + 1;
        return x+y;
    }

    // prendo la prima carta nel mazzo da gioco attivo il suo effetto e la rimuovo
    public void drawCard() {
        if (gameDeck.isEmpty()) return;
        gameDeck.removeFirst().cardEffect(this, null ); //da modificare null una volta volta implementato game controller
    }

    public void turn(){
        drawCard();
        for(Trucker t: truckers){
            checkOvertaken(t);
            noHumans(t);
        }
    }

    public void noHumans(Trucker trucker) {
        for(Trucker t: truckers){
            if (t.getSpaceShip().getNumHuman() == 0)
                withdraw(t);
        }
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
        truckersOut.add(trucker);
        truckers.remove(trucker);
    }

    //i=posizione assoluta
    public Trucker getTrucker(int i) {
        return truckers.get(i);
    }

    public AvailableComponents[] getComponents() {
        return components;
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

    //muove trucker in avanti, riceve days positivo
    public void moveForward(Trucker trucker, int days){
        int finalPos = trucker.getPosition();
        for (int i=0; i<days; i++){
            finalPos = trucker.getPosition() + 1;
            for(Trucker t: truckers){
                if (t.getPosition() == finalPos){
                    finalPos++;
                }
            }
        }
        trucker.setPosition(finalPos);
    }

    //muove trucker in avanti, riceve days positivo
    public void moveBackward(Trucker trucker, int days){
        int finalPos = trucker.getPosition();
        for (int i=0; i<days; i++){
            finalPos = trucker.getPosition() - 1;
            for(Trucker t: truckers){
                if (t.getPosition() == finalPos){
                    finalPos--;
                }
            }
        }
        trucker.setPosition(finalPos);
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

    public int getTimeTurns(){
        return timeTurns;
    }

    public void increaseTimeTurns() {
        timeTurns = timeTurns+1;
    }

    // da finire, per livello 2
    public int[] computeScores(){
        int i=0;
        int[] scores = new int[numPlayer];
        ArrayList<Goods> goodsList = new ArrayList<>();
        for(Trucker t: truckers) {
            if(t.getCosmicCredit()<t.getSpaceShip().getlost()) {
                scores[i] -= (t.getSpaceShip().getLost()-t.getCosmicCredit());
            }
            goodsList = t.getSpaceShip().getGoodsList();
            for(Goods g: goodsList) {
                scores[i] += g.getValue();
            }
            if (bestSpaceShip(t)){
                scores[i]+=4;
            }
            scores[i] += 8-(2*i);
            i++;
        }
        //non sicuro
        for (Trucker t: truckersOut) {
            scores[i] -= (t.getSpaceShip().getLost()-t.getCosmicCredit());
            goodsList = t.getSpaceShip().getGoodsList();
            for(Goods g: goodsList) {
                scores[i] += g.getValue()/2;
            }
            i++;
        }
        return scores;
    };

    public boolean bestSpaceShip(Trucker trucker) {
        for(Trucker t: truckers) {
            if (trucker.getSpaceShip().countExposedConnectors() > t.getSpaceShip().countExposedConnectors()){
                return false;
            }
        }
        return true;
    }

}
