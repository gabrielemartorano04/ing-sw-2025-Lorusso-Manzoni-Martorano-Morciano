package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.AskController;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Comparator;

//gestisce la partita

/**
 * The Game class manages the entire game process, including player actions,
 * deck management, and game phases. It handles trucker movements, card effects,
 * and the overall game state.
 */

public class Game {
    //attributi
    private ArrayList<Trucker> truckers;
    private ArrayList<Trucker> truckersOut;
    private ArrayList<Card>[] cardStacks;
    private ArrayList<Card> gameDeck;
    private AvailableComponents components;
    private Hourglass hourglass;
    private int gameLevel;
    private Deck deck;
    private Bank bank;

    private AskController askController;
    private GameState gameState;
    private ArrayList<PlayerColors> availableColors;

    /**
     * Constructor that initializes the game attributes.
     */
    public Game() {
        this.truckers = new ArrayList<>();
        this.truckersOut = new ArrayList<>();
        this.gameDeck = new ArrayList<>();
        this.components = new AvailableComponents();
        this.hourglass = new Hourglass(this);
        this.deck = new Deck();
        this.cardStacks = new ArrayList[4];
        this.bank = new Bank();

        this.availableColors = new ArrayList<>();
        this.availableColors.addFirst(PlayerColors.RED);
        this.availableColors.addFirst(PlayerColors.BLUE);
        this.availableColors.addFirst(PlayerColors.GREEN);
        this.availableColors.addFirst(PlayerColors.YELLOW);
    }



    // model

    public Bank getBank() {
        return bank;
    }

    /**
     * Sorts truckers in descending order based on their position.
     */
    public void sortTruckers() {
        truckers.sort(Comparator.comparingInt(Trucker::getPosition).reversed());
    }

    /**
     * Rolls two dice and returns their sum.
     * @return the sum of two random dice values (1-6)
     */
    public int rollDice() {
        Random rand = new Random();
        int x = rand.nextInt(5) + 1 ;
        int y = rand.nextInt(5) + 1;
        return x+y;
    }

    /**
     * Draws the first card from the deck, returns it, and removes it.
     */
    public Card drawCard() {
        if (gameDeck.isEmpty()) return null;
        Game game= new Game();
        return gameDeck.removeFirst();
    }

    /**
     * Removes truckers with no astronauts left on their spaceship.
     * @param trucker the trucker to check
     */
    public void noHumans(Trucker trucker) {
        for(Trucker t: truckers){
            if (t.getSpaceShip().getNumHuman() == 0)
                withdraw(t);
        }
    }

    /**
     * Returns a specific stack of cards.
     * @param i index of the stack
     * @return the requested card stack or null if index is invalid
     */
    public ArrayList<Card> viewStack(int i) {
        if (i != 3)
            return cardStacks[i];
        else return null;
    }

    /**
     * Checks if a trucker has been overtaken by 24+ positions and removes them.
     * @param trucker the trucker to check
     */
    public void checkOvertaken(Trucker trucker) {
        for(Trucker t: truckers) {
            if(t.getPosition() - trucker.getPosition() >= 24){
                withdraw(trucker);
            }
        }
    }

    /**
     * Moves a trucker to the 'out' list and removes them from the active game.
     * @param trucker the trucker to withdraw
     */
    public void withdraw(Trucker trucker) {
        truckersOut.add(trucker);
        truckers.remove(trucker);
    }

    //i=posizione assoluta
    /**
     * Retrieves a trucker at a given index.
     * @param i index of the trucker
     * @return the trucker at the specified position
     */
    public Trucker getTrucker(int i) {
        return truckers.get(i);
    }

    /**
     * Returns the list of available components.
     * @return an array of available components
     */
    public AvailableComponents getComponents() {
        return components;
    }

    /**
     * Creates stacks of cards from the deck.
     */
    public void makeStacks() {
        Random rand = new Random();
        for(ArrayList<Card> cardStack: cardStacks ) {
            cardStack.addFirst(deck.getDeck().remove(rand.nextInt(19)));
            for (int i=0; i<2; i++){
                cardStack.addFirst(deck.getDeck().remove(rand.nextInt(19) + 20));
            }
        }
    }

    /**
     * Creates the game deck by shuffling the card stacks.
     */
    public void makeGameDeck() {
        for (ArrayList<Card> cardStack: cardStacks ) {
            gameDeck.addAll(cardStack);
        }
        do{
            Collections.shuffle(gameDeck);
        }while(gameDeck.getFirst().getLevel() != gameLevel);
    }

    /**
     * Moves a trucker forward by a specified number of days.
     */
    public ArrayList<Trucker> getTruckerList() {
        return truckers;
    }

    //muove trucker in avanti, riceve days positivo
    /**
     * Moves a trucker forward by a specified number of days.
     */
    public void moveForward(Trucker trucker, int days){
        int finalPos = trucker.getPosition();
        for (int i=0; i<days; i++){
            finalPos = trucker.getPosition() + 1;
            for(Trucker t: truckers){
                if (t.getPosition() == finalPos && trucker!=t){
                    finalPos++;
                }
            }
        }
        trucker.setPosition(finalPos);
    }

    //muove trucker in avanti, riceve days positivo
    /**
     * Moves a trucker backward by a specified number of days.
     */
    public void moveBackward(Trucker trucker, int days){
        int finalPos = trucker.getPosition();
        for (int i=0; i<days; i++){
            finalPos = trucker.getPosition() - 1;
            for(Trucker t: truckers){
                if (t.getPosition() == finalPos && trucker!=t){
                    finalPos--;
                }
            }
        }
        trucker.setPosition(finalPos);
    }

    /**
     * Adds a new trucker to the game.
     * @param numPlayer the number of players
     * @param nickname the player's nickname
     * @param color the player's chosen color
     */
    public void makeTrucker(int numPlayer, String nickname, PlayerColors color){
        truckers.add(new Trucker(nickname, color));
    }

    /**
     * Checks if a trucker's spaceship is ready.
     * @param trucker the trucker to check
     * @return false (to be implemented)
     */
    public boolean ready(Trucker trucker){
        return false;
    } //trucker's ship is ready



    /**
     * Sets the game level.
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.gameLevel = level;
    };



    /**
     * Computes the scores for each player at the end of the game.
     * @param numPlayer the number of players
     * @return an array of scores
     */
    public int[] computeScores(int numPlayer){
        int i=0;
        int[] scores = new int[numPlayer];
        ArrayList<Goods> goodsList = new ArrayList<>();
        for(Trucker t: truckers) {
            if(t.getCosmicCredit()<t.getSpaceShip().getLost()) {
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

    /**
     * Determines if a trucker has the best spaceship.
     * @param trucker the trucker to check
     * @return true if the trucker's spaceship has the fewest exposed connectors
     */
    public boolean bestSpaceShip(Trucker trucker) {
        for(Trucker t: truckers) {
            if (trucker.getSpaceShip().countExposedConnectors() > t.getSpaceShip().countExposedConnectors()){
                return false;
            }
        }
        return true;
    }

    public ArrayList<PlayerColors> showAvailableColors() {
        return availableColors;
    }

    //FSM

    public void addTrucker(PlayerColors color, String nickname) throws IllegalStateException{
        if (this.gameState != GameState.SETUP){
            throw new IllegalStateException("State is not setup");
        }
        if(truckers.size()=4 || ){

        }
        if(truckers.stream().anyMatch(trucker -> trucker.getNickname().equals(nickname))){
            throw new IllegalArgumentException("Nickname already exists");
        }
        if (truckers.stream().anyMatch(trucker -> trucker.getColor().equals(color))){
            throw new IllegalArgumentException("Color already taken");
        }
        availableColors.remove(color);
        truckers.addLast(new Trucker(nickname, color));
        gameState = GameState.WAITING;
    }

    public void waiting(){
        if (this.gameState != GameState.WAITING){
            throw new IllegalStateException("State is not waiting");
        }
        if (){
            gameState = GameState.BUILDING;
        }
    }

    public void building(Trucker trucker, boolean finish){
        if (gameState != GameState.BUILDING){
            throw new IllegalStateException("State is not building");
        }
        if (finish) {
            trucker.setBuildingState(BuildingState.BUILDING);
        }
    }

    /**
     * checks game conditions for every turn of the game.
     */
    public void turn(){
        for(Trucker t: truckers){
            checkOvertaken(t);
            noHumans(t);
        }
    }

    public void finish(){

    }
    public Hourglass getHourglass(){
        return hourglass;
    }

}

