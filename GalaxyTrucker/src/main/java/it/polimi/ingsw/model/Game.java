package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.BuildTimerManager;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.buildingevents.BuildCompleteEvent;
import it.polimi.ingsw.net.events.serverevents.buildingevents.WaitOthersBuildingEvent;
import it.polimi.ingsw.exceptions.CannotTurnHourglassException;
import it.polimi.ingsw.net.events.serverevents.buildingevents.WaitOthersHourglassEvent;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;


/**
 * The Game class manages the entire game process, including player actions,
 * deck management, and game phases. It handles trucker movements, card effects,
 * and the overall game state.
 */

public class Game implements Serializable {
    /**
     * Serial version
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * List of players
     */
    private final ArrayList<Trucker> truckers;

    /**
     * List of players out
     */
    private final ArrayList<Trucker> truckersOut;

    /**
     * Cards stacks
     */
    private final Stack[] cardStacks;

    /**
     * Cards that forms the deck
     */
    private final ArrayList<Card> gameDeck;

    /**
     * Available components list
     */
    private final AvailableComponents components;

    /**
     * Hourglass for building timer
     */
    private Hourglass hourglass;

    /**
     * Game level
     */
    private int gameLevel;

    /**
     * Cards deck
     */
    private final Deck deck;

    /**
     * Available goods bank
     */
    private final Bank bank;

    /**
     * Game instance
     */
    private static Game istance;

    /**
     * List of nicknames used in an old match
     */
    private ArrayList<String> oldNicknames;

    /**
     * List of colors used in an old match
     */
    private ArrayList<PlayerColors> oldColors;

    /**
     * Track dimension based on level
     */
    private int checkOverTaken;

    /**
     * Game state
     */
    private GameState gameState;

    /**
     * Set of nicknames
     */
    private final Set<String> nicknames = new HashSet<>();

    /**
     * Set of colors
     */
    private final Set<PlayerColors> usedColors = new HashSet<>();

    /**
     * Available player colors
     */
    private final EnumSet<PlayerColors> availableColors;

    /**
     * Number of players
     */
    private int numPlayers;

    /**
     * Game track. Every player is associated to a position in the track
     */
    Map<String, Integer> track = new HashMap<>();

    /**
     * Constructor that initializes the game attributes.
     * @param numPlayers number of players
     * @param gameLevel game level
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
        this.oldNicknames = new ArrayList<>();
        this.oldColors = new ArrayList<>();
        setIstance(this);
        if(gameLevel == 0){
            checkOverTaken=20;
        } else if(gameLevel == 2){
            checkOverTaken=24;
        }

    }

    //todo da togliere dopo test o mettere in test
    public static void initializeForTest(SpaceShip ship1, SpaceShip ship2) {
        Game testGame = new Game(2, 2);

        Trucker t1 = new Trucker("Alice", PlayerColors.RED);
        t1.setSpaceShip(ship1);

        // Aggiungi i trucker al gioco
        try {
            testGame.addTrucker(t1);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante addTrucker nel test", e);
        }

        if(ship2 != null) {
            Trucker t2 = new Trucker("Bob", PlayerColors.BLUE);
            t2.setSpaceShip(ship2);
            testGame.addTrucker(t2);
        }

        // Imposta il game come stato di volo se serve bypassare la build
        testGame.setGameState(GameState.FLIGHT);

        // Imposta l'istanza statica
        Game.setIstance(testGame);
    }


    /**
     * @return game level: 0 for trial flight and 2 for level 2
     */
    public int getGameLevel(){
        return gameLevel;
    }

    /**
     * @return this game instance
     */
    public static Game getIstance() {
        return istance;
    }

    /**
     * @param istance game instance to set instance
     */
    public static void setIstance(Game istance) {
        Game.istance = istance;
    }

    /**
     * If game level is two this method initializes the hourglass and starts the timer
     */
    public void startHourglass(){
        if(gameLevel==2) hourglass=new Hourglass();
    }

    /**
     * @return number of players
     */
    public int getNumPlayers(){
        return numPlayers;
    }

    /**
     * Game state setter
     * @param gameState new game state
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * @return current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @return goods bank
     */
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
        return gameDeck.removeFirst();
    }

    /**
     * Removes truckers with no astronauts left on their spaceship.
     * @param trucker the trucker to check
     */
    private boolean noHumans(Trucker trucker) {
        return trucker.getSpaceShip().getNumCrew() == 0;
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
                .anyMatch(t -> t != trucker && t.getPosition() - trucker.getPosition() >= checkOverTaken);
    }

    /**
     * Moves a trucker to the 'out' list and removes them from the active game.
     * @param trucker the trucker to withdraw
     */
    public void withdraw(Trucker trucker) {
        for (Trucker t: truckers) {
            if (t.getNickname().equalsIgnoreCase(trucker.getNickname())){
                truckersOut.add(trucker);
                truckers.remove(trucker);
                break;
            }
        }
    }

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
        List<Card> mainDeck = deck.getDeck();

//        //TODO: da togliere, messo per debug
//        int totalCards = mainDeck.size();
//        int perStack = totalCards / 4;
//        int remaining = totalCards % 4;
//        int index = 0;
//
//        for (int i = 0; i < 4; i++) {
//            int cardsInThisStack = perStack + (i < remaining ? 1 : 0);
//            for (int j = 0; j < cardsInThisStack; j++) {
//                cardStacks[i].addCardInStack(mainDeck.get(index));
//                index++;
//            }
//        }


            //TODO: da rimettere, tolto per debug
        if (gameLevel==2 && mainDeck.size() < 39) {
            throw new IllegalStateException("Il mazzo non contiene abbastanza carte (almeno 39 richieste).");
        }


        List<Card> zone1 = new ArrayList<>(mainDeck.subList(0, 19));   // [0–18]
        List<Card> zone2 = new ArrayList<>(mainDeck.subList(20, 39));  // [20–38]

        Random rand = new Random();

        for (int j = 0; j < 4; j++) {
            ArrayList<Card> cardStack = cardStacks[j].getStack();


            int i1 = rand.nextInt(zone1.size());
            cardStack.add(0, zone1.remove(i1));


            for (int i = 0; i < 2; i++) {
                int i2 = rand.nextInt(zone2.size());
                cardStack.add(0, zone2.remove(i2));
           }
        }

        // Ricostruisci il mazzo togliendo le carte usate da zone1 e zone2

        mainDeck.subList(0, 39).clear();
        // Reinserisci le carte rimaste
        mainDeck.addAll(zone1);
        mainDeck.addAll(zone2);
    }

    /**
     * Creates the game deck by shuffling the card stacks.
     */
    public void makeGameDeck() {
        if(gameLevel==2){
            for (Stack cardStack: cardStacks ) {
                gameDeck.addAll(cardStack.getStack());
            }
            do{

                Collections.shuffle(gameDeck);
                System.out.println("sto mescolando");
            }while(gameDeck.getFirst().getLevel() != gameLevel);
        }else{
            gameDeck.addAll(deck.getDeck());
        }

    }

    /**
     * Moves a trucker forward by a specified number of days.
     */
    public ArrayList<Trucker> getTruckerList() {
        return truckers;
    }


    /**
     * returns the current track updated with truckers' positions
     * @return
     */
    public Map<String, Integer> getTrack() {
        for (Trucker p : truckers) {
            track.put(p.getNickname(), p.getPosition());
        }
        return track;
    }

    /**
     * Moves a trucker forward by a specified number of days.
     */
    public void moveForward(Trucker trucker, int days){
        int finalPos = trucker.getPosition();
        for (int i=0; i<days; i++){
            finalPos ++;
            for(Trucker t: truckers){
                if (t.getPosition() == finalPos && trucker != t){
                    finalPos++;
                }
            }
        }
        trucker.setPosition(finalPos);
        this.sortTruckers();
    }

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
        this.sortTruckers();
    }


    /**
     * Sets the game level.
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.gameLevel = level;
    }


    /**
     * Computes the scores for each player at the end of the game.
     * @return an array of scores
     */
    public int[] computeScores(){
        int[] scores = new int[numPlayers];

        for(int i=0; i<numPlayers; i++){
            scores[i]=0;
        }

        if (gameState!= GameState.FLIGHT && gameState!= GameState.ENDED) {
            return scores;
        }

        int i=0;
        for(Trucker t: truckers) {
            scores[i] = t.getCosmicCredit();
            scores[i] = scores[i] - t.getSpaceShip().getLost();


            if(gameLevel == 2){
                switch (i){
                    case 0 -> scores[i] = scores[i] + 8;
                    case 1 ->  scores[i] = scores[i] + 6;
                    case 2 ->  scores[i] = scores[i] + 4;
                    case 3 ->  scores[i] = scores[i] + 2;
                }
            }else {
                switch (i){
                    case 0 -> scores[i] = scores[i] + 4;
                    case 1 ->  scores[i] = scores[i] + 3;
                    case 2 ->  scores[i] = scores[i] + 2;
                    case 3 ->  scores[i] = scores[i] + 1;
                }
            }

            ArrayList<Goods> goodsList = t.getSpaceShip().getGoodsList();

            for(Goods g: goodsList) {
                scores[i] += g.getValue();
            }

            if (bestSpaceShip(t)){
                if(gameLevel == 2){
                    scores[i]+=4;
                }else{
                    scores[i]+=2;
                }
            }

            i++;
        }

        i = 0;
        for (Trucker t: truckersOut) {
            scores[i] = t.getCosmicCredit();
            scores[i] = scores[i] - t.getSpaceShip().getLost();

            ArrayList<Goods> goodsList = t.getSpaceShip().getGoodsList();
            for(Goods g: goodsList) {
                scores[i] += g.getValue()/2;
            }
            i++;
        }
        i = 0;
        for(Trucker t: truckers){
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
            if (trucker!=t && trucker.getSpaceShip().countExposedConnectors() >= t.getSpaceShip().countExposedConnectors()){
                return true;
            }
        }
        return false;
    }

    /**
     * This method tells if the nickname is already in the game
     * @param nickname nickname to check
     * @return if the nickname is already in the game
     */
    public synchronized boolean isNicknameTaken(String nickname) {
        return nicknames.contains(nickname);
    }

    /**
     * This method tells if the color is already in the game
     * @param color color to check
     * @return if the color is already in the game
     */
    public synchronized boolean isColorTaken(PlayerColors color) {
        return usedColors.contains(color);
    }

    /**
     * This method adds a trucker in the game
     * @param trucker player
     * @throws IllegalStateException if the game already started
     */
    public synchronized void addTrucker(Trucker trucker) throws IllegalStateException {
        if (gameState!= GameState.SETUP){
            throw new IllegalStateException("Game has already been started");
        }
        truckers.add(trucker);
        nicknames.add(trucker.getNickname());
        usedColors.add(trucker.getColor());
        availableColors.remove(trucker.getColor());
    }

    /**
     * This method sets cards for the game
     * @throws IllegalStateException
     */
    public void setupGame() throws IllegalStateException {
        if (gameState!= GameState.SETUP){
            throw new IllegalStateException("Game has already been started");
        }
        if(gameLevel==2) {
            makeStacks();
        }
        makeGameDeck();
        System.out.println("game created");
        gameState = GameState.BUILDING;
    }

    /**
     * @return if the building time ended
     */
    public boolean isHourglassExpired() {
        return hourglass.timeIsUp() && hourglass.getTimeTurns() == 2;
    }

    /**
     * @return game hourglass
     */
    public Hourglass getHourglass(){
        return hourglass;
    }

    /**
     * Actions to do when a card effect finishes
     * @return updated list of players
     */
    public ArrayList<Trucker> endTurn() {
        ArrayList<Trucker> toRemove = new ArrayList<>();
        for (Trucker t : truckers) {
            if (noHumans(t) || checkOvertaken(t)) {
                toRemove.add(t);
            }
        }
        for (Trucker t : toRemove) {
            withdraw(t);
        }
        sortTruckers();
        return toRemove;
    }

    /**
     * This method turns the hourglass
     * @return a server event for the player to wait others to finish building if it's the last hourglass turn
     * @throws CannotTurnHourglassException if the hourglass can't be turned in the current phase
     */
    public ServerEvent turnHourglass(String nickname) throws CannotTurnHourglassException {
        ServerEvent event=null;
        if(hourglass.getTimeTurns()==0 && hourglass.timeIsUp()){
            hourglass.reset();
        } else if (hourglass.getTimeTurns()==1 && hourglass.timeIsUp()){
            hourglass.reset();
            event= new WaitOthersHourglassEvent(nickname);

        } else {
            throw new CannotTurnHourglassException("You can't turn the hourglass now!");
        }
        return event;
    }

    /**
     * @return game cards stacks
     */
    public Stack[] viewStacks() {
        return cardStacks;
    }

    /**
     * @return Truckers out of the game
     */
    public ArrayList<Trucker> getTruckerOut() {
        return truckersOut;
    }

    /**
     * This method checks if an old player had a given nickname and color
     * @param nickname nickname to check
     * @param color color to check
     * @return if an old player had a given nickname and color
     */
    public boolean checkOldPlayers(String nickname, PlayerColors color) {
        for(int i=0; i<numPlayers; i++){
            if(oldColors.get(i).equals(color) && oldNicknames.get(i).equals(nickname)){
                return true;
            }
        }
        return false;
    }

    /**
     * This method fills the oldNicknames list
     */
    public void createOldNicknames() {
        ArrayList<Trucker> truckers;
        truckers= this.truckers;
        truckers.addAll(this.truckersOut);
        for(Trucker trucker : truckers){
            oldNicknames.add(trucker.getNickname());
        }
    }

    /**
     * @return old nicknames list
     */
    public ArrayList<String> getOldNicknames() {
        return oldNicknames;
    }

    /**
     * @return old colors list
     */
    public ArrayList<PlayerColors> getOldColors() {
        return oldColors;
    }

    /**
     * This method fills the oldColors list
     */
    public void createOldColors() {
        ArrayList<Trucker> truckers;
        truckers= this.truckers;
        truckers.addAll(this.truckersOut);
        for(Trucker trucker : truckers){
            oldColors.add(trucker.getColor());
        }
    }

    /**
     * This method removes a color from old colors list
     * @param color color to remove
     */
    public void removeOldColors(PlayerColors color) {
        oldColors.remove(color);
    }

    /**
     * This method removes a nickname from old nicknames list
     * @param nickname nickname to remove
     */
    public void removeOldNicknames(String nickname) {oldNicknames.remove(nickname);}

    /**
     * @return names of trucker in the game with an illegal ship
     */
    public ArrayList<String> getIllegalShipNames(){
        ArrayList<String> names = new ArrayList<>();
        for(Trucker t: truckers){
            if(!t.getSpaceShip().isShipLegal()){
                names.add(t.getNickname());
                //TODO TOGLI
                System.out.println(t.getNickname() + " has an illegal ship");
            }
        }
        return names;
    }

}

