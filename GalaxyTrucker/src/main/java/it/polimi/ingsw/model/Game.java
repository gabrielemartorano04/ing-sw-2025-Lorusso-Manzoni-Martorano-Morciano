package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.buildingevents.WaitOthersBuildingEvent;
import it.polimi.ingsw.exceptions.CannotTurnHourglassException;

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
     * List of players.
     */
    private final ArrayList<Trucker> truckers;

    /**
     * List of players out of the game
     */
    private final ArrayList<Trucker> truckersOut;

    /**
     * Card stacks for level 2
     */
    private final Stack[] cardStacks;

    /**
     * Building phase available components
     */
    private final AvailableComponents components;

    /**
     * Horglass for building phase timer
     */
    private Hourglass hourglass;

    /**
     * Game level
     */
    private int gameLevel;

    /**
     * Game deck with all the cards, based on the level.
     */
    private final ArrayList<Card> gameDeck;

    /**
     * Deck for game deck creation
     */
    private final Deck deck;

    /**
     * Available goods bank
     */
    private final Bank bank;

    /**
     * This game instance
     */
    private static Game istance;

    /**
     * Old players nicknames from old matches
     */
    private ArrayList<String> oldNicknames;

    /**
     * Old players colors from old matches
     */
    private ArrayList<PlayerColors> oldColors;

    /**
     * Track dimension based on level. It's used to check if a player was overtaken from another
     */
    private int checkOverTaken;

    /**
     * Game state
     */
    private GameState gameState;

    /**
     * Nicknames of players in the game.
     */
    private final Set<String> nicknames = new HashSet<>();

    /**
     * Colors of players in the game.
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
        this.oldNicknames = new ArrayList<>();
        this.oldColors = new ArrayList<>();
        setIstance(this);
        if(gameLevel == 0){
            checkOverTaken=20;
        } else if(gameLevel == 2){
            checkOverTaken=24;
        }

    }

    // todo togli dopo test
    public static void initializeForTest(SpaceShip ship1, SpaceShip ship2) {
        Game testGame = new Game(2, 2);

        Trucker t1 = new Trucker("Alice", PlayerColors.RED);
        Trucker t2 = new Trucker("Bob", PlayerColors.BLUE);

            t1.setSpaceShip(ship1);
            t2.setSpaceShip(ship2);
        try {
            testGame.addTrucker(t1);
            testGame.addTrucker(t2);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante addTrucker nel test", e);
        }

        // Imposta il game come stato di volo se serve bypassare la build
        testGame.setGameState(GameState.FLIGHT);

        // Imposta l'istanza statica
        Game.setIstance(testGame);
    }

    /**
     * Game level getter
     * @return level of current game
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
     * Game instance setter
     * @param istance new Game instance
     */
    public static void setIstance(Game istance) {
        Game.istance = istance;
    }

    /**
     * This method initializes the building phase timer if the level is 2
     */
    public void startHourglass(){
        if(gameLevel==2) hourglass=new Hourglass();
    }

    /**
     * Number of players getter
     * @return number of players
     */
    public int getNumPlayers(){
        return numPlayers;
    }

    /**
     * This method sets the game state
     * @param gameState new game state
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Game state getter
     * @return the current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Goods bank getter
     * @return available goods bank
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * This method sorts truckers in descending order based on their position.
     */
    public void sortTruckers() {
        truckers.sort(Comparator.comparingInt(Trucker::getPosition).reversed());
    }


    /**
     * This method rolls two dices and returns their sum.
     * @return the sum of two random dice each with values (1-6)
     */
    public int rollDice() {
        Random rand = new Random();
        int x = rand.nextInt(5) + 1 ;
        int y = rand.nextInt(5) + 1;
        return x+y;
    }

    /**
     * This method raws the first card from the deck, returns it and removes it.
     */
    public Card drawCard() {
        if (gameDeck.isEmpty()) return null;
        return gameDeck.removeFirst();
    }

    /**
     * This method removes truckers with no astronauts left on their spaceship.
     * @param trucker the trucker to check
     */
    private boolean noHumans(Trucker trucker) {
        return trucker.getSpaceShip().getNumCrew() == 0;
    }

    /**
     * This method returns a specific stack of cards.
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
     * This method checks if a trucker has been overtaken by 24+ positions and removes them.
     * @param trucker the trucker to check
     */
    private boolean checkOvertaken(Trucker trucker) {
        return truckers.stream()
                .anyMatch(t -> t != trucker && t.getPosition() - trucker.getPosition() >= checkOverTaken);
    }

    /**
     * This method moves a trucker to the 'out' list and removes them from the active game.
     * @param trucker the trucker to withdraw
     */
    public void withdraw(Trucker trucker) {
        for (Trucker t: truckers) {
            if (t.getNickname().equalsIgnoreCase(trucker.getNickname())){
                truckersOut.add(trucker);
                truckers.remove(trucker);
                System.out.println("trucker out: " + trucker.getNickname());
                break;
            }
        }
    }

    /**
     * This method retrieves a trucker at a given index.
     * @param i index of the trucker
     * @return the trucker at the specified position
     */
    public Trucker getTrucker(int i) {
        return truckers.get(i);
    }

    /**
     * This method returns the list of available components.
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
     * This method creates the game deck by shuffling the card stacks.
     */
    public void makeGameDeck() {
        if(gameLevel==2){
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
        else{
            gameDeck.addAll(deck.getDeck());
        }

    }

    /**
     * This method moves a trucker forward by a specified number of days.
     */
    public ArrayList<Trucker> getTruckerList() {
        return truckers;
    }


    /**
     * This method returns the current track updated with truckers' positions
     * @return
     */
    public Map<String, Integer> getTrack() {
        Map<String, Integer> track = new HashMap<>();
        for (Trucker p : truckers) {
            track.put(p.getNickname(), p.getPosition());
        }
        return track;
    }

    /**
     * This method moves a trucker forward a specified number of days in the track.
     */
    public void moveForward(Trucker trucker, int days){
        int finalPos = trucker.getPosition();
        System.out.println("startPos of " + trucker.getNickname() + ": " + finalPos);
        for (int i=0; i<days; i++){
            finalPos ++;
            for(Trucker t: truckers){
                if (t.getPosition() == finalPos && trucker != t){
                    finalPos++;
                }
            }
        }
        System.out.println("finalPos of " + trucker.getNickname() + ": " + finalPos);
        trucker.setPosition(finalPos);
        this.sortTruckers();
    }

    /**
     * Moves a trucker backward by a specified number of days in the track.
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
     * @param level the game level
     */
    public void setLevel(int level) {
        this.gameLevel = level;
    }



    /**
     * This method computes the scores for each player at the end of the game.
     * @return an array of scores
     */
    public int[] computeScores(){
        int[] scores = new int[numPlayers];
        if (gameState!= GameState.FLIGHT && gameState!= GameState.ENDED) {
            for(int j=0; j<numPlayers; j++){
                scores[j]=0;
            }
        }
        int i=0;
        for(i=0; i<numPlayers; i++){
            scores[i]=0;
        }
        i=0;
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
     * This method determines if a trucker has the best spaceship.
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

    /**
     *
     * @return
     */
    public EnumSet<PlayerColors> showAvailableColors() {
        return availableColors;
    }

    /**
     * This method tells if a nickname is already used in the game
     * @param nickname nickname to check
     * @return if the nickname is already used in the game
     */
    public synchronized boolean isNicknameTaken(String nickname) {
        return nicknames.contains(nickname);
    }

    /**
     * This method tells if a color is already used in the game
     * @param color color to check
     * @return if the color is already used in the game
     */
    public synchronized boolean isColorTaken(PlayerColors color) {
        return usedColors.contains(color);
    }

    /**
     * This method adds a player in the game
     * @param trucker player to add
     * @throws IllegalStateException if the game already started
     */
    public synchronized void addTrucker(Trucker trucker) throws IllegalStateException {
        if (gameState!= GameState.SETUP){
            throw new IllegalStateException("Game has already started");
        }
        truckers.add(trucker);
        nicknames.add(trucker.getNickname());
        usedColors.add(trucker.getColor());
        availableColors.remove(trucker.getColor());
    }

    /**
     * This method sets cards deck and stacks for the game.
     * @throws IllegalStateException if the game already started
     */
    public void setupGame() throws IllegalStateException {
        if (gameState!= GameState.SETUP){
            throw new IllegalStateException("Game has already been started");
        }
        makeStacks();
        makeGameDeck();
        System.out.println("game created");
        gameState = GameState.BUILDING;
    }

    /**
     * This method checks if the building time is finished
     * @return if the building time is finished
     */
    public boolean isHourglassExpired() {
        return hourglass.timeIsUp() && hourglass.getTimeTurns() == 2;
    }

    /**
     * This method returns the game hourglass
     * @return game hourglass
     */
    public Hourglass getHourglass(){
        return hourglass;
    }

    /**
     * This method removes players who have lost and sorts players.
     * @return list of players who have lost
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
     * This method is used when a player wants to turn the hourglass
     * @return serverEvent WaitOthersBuildingEvent for the player who turned the hourglass for the last time,
     * null otherwise.
     * @throws CannotTurnHourglassException if it's not possible to turn the hourglass at the moment
     */
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

    /**
     * This method is used to see cards stacks
     * @return list of cards stacks
     */
    public Stack[] viewStacks() {
        return cardStacks;
    }

    /**
     * This method returns truckers out of the game
     * @return list of truckers out of the game
     */
    public ArrayList<Trucker> getTruckerOut() {
        return truckersOut;
    }

    /**
     * This method is used for saved game. It checks if a nickname and a color identify an old player
     * @param nickname old player nickname to check
     * @param color old player color to check
     * @return if there was a player with that nickname and that color in the old match
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
     * This method creates old nicknames list to save the match
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
     * Old nicknames getter
     * @return list of old nicknames used
     */
    public ArrayList<String> getOldNicknames() {
        return oldNicknames;
    }

    /**
     * Old colora getter
     * @return list of old colors used
     */
    public ArrayList<PlayerColors> getOldColors() {
        return oldColors;
    }

    /**
     * This method creates old colors list to save the match
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
     * This method remove a color from old colors
     * @param color color to remove
     */
    public void removeOldColors(PlayerColors color) {
        oldColors.remove(color);
    }

    /**
     * This method remove a nickname from old nicknames
     * @param nickname nickname to remove
     */
    public void removeOldNicknames(String nickname) {
        oldNicknames.remove(nickname);
    }

}

