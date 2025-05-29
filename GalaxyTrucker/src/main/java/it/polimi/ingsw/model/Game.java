package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.WaitOthersBuildingEvent;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;
import it.polimi.ingsw.exceptions.CannotTurnHourglassException;

import java.util.*;

//gestisce la partita

/**
 * The Game class manages the entire game process, including player actions,
 * deck management, and game phases. It handles trucker movements, card effects,
 * and the overall game state.
 */

public class Game {
    //attributi
    private final ArrayList<Trucker> truckers;
    private final ArrayList<Trucker> truckersOut;
    private final Stack[] cardStacks;
    private final ArrayList<Card> gameDeck;
    private final AvailableComponents components;
    private Hourglass hourglass;
    private int gameLevel;
    private final Deck deck;
    private final Bank bank;
    private static Game istance;

    //private AskController askController;
    private GameState gameState;
    private final Set<String> nicknames = new HashSet<>();
    private final Set<PlayerColors> usedColors = new HashSet<>();
    private final EnumSet<PlayerColors> availableColors;
    private final int numPlayers;
    /**
     * Constructor that initializes the game attributes.
     */
    public Game(int numPlayers, int gameLevel) {
        this.truckers = new ArrayList<>(numPlayers);
        this.numPlayers = numPlayers;
        this.truckersOut = new ArrayList<>();
        this.gameDeck = new ArrayList<>();
        this.components = new AvailableComponents();
        this.gameLevel = gameLevel;
        this.deck = new Deck(gameLevel);
        this.cardStacks = new Stack[4];
        for(int i=0; i<4; i++){
            cardStacks[i] = new Stack(i);
        }
        this.bank = new Bank();
        this.gameState = GameState.SETUP;
        this.availableColors = EnumSet.allOf(PlayerColors.class);

        setIstance(this);
    }

    public int getGameLevel(){
        return gameLevel;
    }

    public static Game getIstance() {
        return istance;
    }

    public static void setIstance(Game istance) {
        Game.istance = istance;
    }

    public void startHourglass(){
        hourglass=new Hourglass();
    }

    public int getNumPlayers(){
        return numPlayers;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Bank getBank() {
        return bank;
    }

    /**
     * Sorts truckers in descending order based on their position.
     */
    public void sortTruckers() {
        truckers.sort(Comparator.comparingInt(Trucker::getPosition).reversed());
    }

    public void reorderPlayersByPosition(ArrayList<ClientHandlerSocket> players) {
        players.sort((a, b) -> Integer.compare(a.getTruckerController().getPosition(), b.getTruckerController().getPosition()));
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
        return gameDeck.removeFirst();
    }

    /**
     * Removes truckers with no astronauts left on their spaceship.
     * @param trucker the trucker to check
     */
    private boolean noHumans(Trucker trucker) {
        return trucker.getSpaceShip().getNumHuman() == 0;
    }

    /**
     * Returns a specific stack of cards.
     * @param i index of the stack
     * @return the requested card stack or null if index is invalid
     */
    public Stack viewStack(int i) throws GeneralError{
        if (i < 3)
            if (!cardStacks[i].isSelected()) {
                Stack selectedStack=cardStacks[i];
                synchronized (selectedStack) {
                    return selectedStack;
                }
            }else{
                throw new GeneralError("un altro giocatore ha questo stack");
            }
        else return null;
    }

    /**
     * Checks if a trucker has been overtaken by 24+ positions and removes them.
     * @param trucker the trucker to check
     */
    private boolean checkOvertaken(Trucker trucker) {
        return truckers.stream()
                .anyMatch(t -> t != trucker && t.getPosition() - trucker.getPosition() >= 24);
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


    //TODO CREARE DECK IN BASE A LIVELLO
    /**
     * Creates stacks of cards from the deck.
     */
    public void makeStacks() {
        List<Card> mainDeck = deck.getDeck();

        //TODO: da togliere, messo per debug
        int totalCards = mainDeck.size();
        int perStack = totalCards / 4;
        int remaining = totalCards % 4;
        int index = 0;

        for (int i = 0; i < 4; i++) {
            int cardsInThisStack = perStack + (i < remaining ? 1 : 0);
            for (int j = 0; j < cardsInThisStack; j++) {
                cardStacks[i].addCardInStack(mainDeck.get(index));
                index++;
            }
        }


            //TODO: da rimettere, tolto per debug
//        if (mainDeck == null || mainDeck.size() < 39) {
//            throw new IllegalStateException("Il mazzo non contiene abbastanza carte (almeno 39 richieste).");
//        }
//
//
//        List<Card> zone1 = new ArrayList<>(mainDeck.subList(0, 19));   // [0–18]
//        List<Card> zone2 = new ArrayList<>(mainDeck.subList(20, 39));  // [20–38]
//
//        Random rand = new Random();
//
//        for (int j = 0; j < 4; j++) {
//            ArrayList<Card> cardStack = cardStacks[j];
//
//
//            int i1 = rand.nextInt(zone1.size());
//            cardStack.add(0, zone1.remove(i1));
//
//
//            for (int i = 0; i < 2; i++) {
//                int i2 = rand.nextInt(zone2.size());
//                cardStack.add(0, zone2.remove(i2));
//           }
//        }
//
//        // Ricostruisci il mazzo togliendo le carte usate da zone1 e zone2
//
//        mainDeck.subList(0, 39).clear();
//        // Reinserisci le carte rimaste
//        mainDeck.addAll(zone1);
//        mainDeck.addAll(zone2);
    }

    /**
     * Creates the game deck by shuffling the card stacks.
     */
    public void makeGameDeck() {
        for (Stack cardStack: cardStacks ) {
            gameDeck.addAll(cardStack.getStack());
        }
        //TODO: non mischio per debug, poi bisgona mischiare
//        do{
//
//            //Collections.shuffle(gameDeck);
//            //System.out.println("sto mescolando");
//        }while(gameDeck.getFirst().getLevel() != gameLevel);
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
            finalPos ++;
            for(Trucker t: truckers){
                if (t.getPosition() == finalPos && trucker !=t){
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
            finalPos --;
            for(Trucker t: truckers){
                if (t.getPosition() == finalPos && trucker !=t){
                    finalPos--;
                }
            }
        }
        trucker.setPosition(finalPos);
    }

//    /**
//     * Checks if a trucker's spaceship is ready.
//     * @param trucker the trucker to check
//     * @return false (to be implemented)
//     */
//    public boolean ready(Trucker trucker){
//        return false;
//    } //trucker's ship is ready



    /**
     * Sets the game level.
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.gameLevel = level;
    }



    /**
     * Computes the scores for each player at the end of the game.
     * @param numPlayer the number of players
     * @return an array of scores
     */
    public int[] computeScores(int numPlayer){
        int i=0;
        int[] scores = new int[numPlayer];
        ArrayList<Goods> goodsList;
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
    }

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

    public EnumSet<PlayerColors> showAvailableColors() {
        return availableColors;
    }

    public Set<PlayerColors> showUsedColors() {
        return usedColors;
    }

    public synchronized boolean isNicknameTaken(String nickname) {
        return nicknames.contains(nickname);
    }

    public synchronized boolean isColorTaken(PlayerColors color) {
        return usedColors.contains(color);
    }

    public synchronized void addTrucker(Trucker trucker) throws IllegalStateException {
        if (gameState!= GameState.SETUP){
            throw new IllegalStateException("Game has already been started");
        }
        truckers.add(trucker);
        nicknames.add(trucker.getNickname());
        usedColors.add(trucker.getColor());
        availableColors.remove(trucker.getColor());
    }

    public void setupGame() throws IllegalStateException {
        if (gameState!= GameState.SETUP){
            throw new IllegalStateException("Game has already been started");
        }
        makeStacks();
        makeGameDeck();
        System.out.println("game created");
        gameState = GameState.BUILDING;
    }

//    public void startFlightPhase() {
//        if (gameState != GameState.BUILDING) {
//            throw new IllegalStateException("Cannot start flight phase from state: " + gameState);
//        }
//        gameState = GameState.FLIGHT;
//    }

    public boolean isHourglassExpired() {
        return hourglass.timeIsUp() && hourglass.getTimeTurns() == 2;
    }

    public boolean allPlayersReady() {
        return truckers.stream()
                .allMatch(t -> t.getBuildingState() == BuildingState.END);
    }

    public Hourglass getHourglass(){
        return hourglass;
    }

    public void endTurn() {
        List<Trucker> toRemove = new ArrayList<>();

        for (Trucker t : truckers) {
            if (noHumans(t) || checkOvertaken(t)) {
                toRemove.add(t);
            }
        }

        toRemove.forEach(this::withdraw);
        sortTruckers();
    }

    public ServerEvent turnHourglass() throws CannotTurnHourglassException {
        ServerEvent event=null;
        if(hourglass.getTimeTurns()==0 && hourglass.timeIsUp()){
            hourglass.reset();
        } else if (hourglass.getTimeTurns()==1 && hourglass.timeIsUp()){
            hourglass.reset();
            event= new WaitOthersBuildingEvent();
        } else {
            throw new CannotTurnHourglassException("You can't turn the hourglass now!");
        }
        return event;
    }

    public Stack[] viewStacks() {
        return cardStacks;
    }
}

