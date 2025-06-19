package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This class manages the game controller. It's used by the server.
 * Attributes
 * - count: int counter of logged players
 * - timerStarted: boolean, true if the timer started, false otherwise
 * - gameStarted: boolean, true if the game started, false otherwise
 * - numOfPlayers: int, number of player decided by the first one
 * - scheduler: ScheduledExecutorService, threads scheduler
 * - currentCard: card, current card during flight phase
 */
public class GameController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * This class instance
     */
    private static GameController istance;

    /**
     * The linked game controlled by this class
     */
    private final Game game;

    /**
     * Logged players counter
     */
    private int loggedPlayers =0;

    /**
     * It signals the game started
     */
    private boolean gameStarted = false;

    /**
     * Players for the game
     */
    private final int numOfPlayers;

    /**
     * Threads scheduler
     */
    private transient ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    /**
     * Current drawn card in the game
     */
    private Card currentCard;

    /**
     * Class constructor
     * @param numberOfTruckers number of players
     * @param gameLevel game level (0 for trial flight,2 for level 2)
     */
    public GameController(int numberOfTruckers, int gameLevel) {
        this.game=new Game(numberOfTruckers, gameLevel);
        this.numOfPlayers=numberOfTruckers;
        this.currentCard=null;
    }

    /**
     * @return this game instance
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return available Player colors
     */
    public EnumSet<PlayerColors> getAvailablePlayerColors() {
        EnumSet<PlayerColors> availablePlayerColors = EnumSet.noneOf(PlayerColors.class);
        if(!game.isColorTaken(PlayerColors.BLUE))
            availablePlayerColors.add(PlayerColors.BLUE);
        if(!game.isColorTaken(PlayerColors.RED))
            availablePlayerColors.add(PlayerColors.RED);
        if(!game.isColorTaken(PlayerColors.GREEN))
            availablePlayerColors.add(PlayerColors.GREEN);
        if(!game.isColorTaken(PlayerColors.YELLOW))
            availablePlayerColors.add(PlayerColors.YELLOW);
        return availablePlayerColors;
    }

    /**
     * This method adds a new player in the game
     * @param nickname player's nickname
     * @param color player's color
     * @return the created instance of trucker controller
     * @throws GameAlreadyStartedException if the game already started
     * @throws NicknameUsedException if the choosen nickname is already in use
     * @throws ColorNotAvailable if the choosen color is already in use
     */
    public synchronized TruckerController tryAddPlayer(String nickname, PlayerColors color) throws GameAlreadyStartedException, NicknameUsedException, ColorNotAvailable {
        if (gameStarted) {
            throw new GameAlreadyStartedException("game already started");
        }

        if (game.isNicknameTaken(nickname)) {
            throw new NicknameUsedException("Nickname already taken.");
        }
        if ( game.isColorTaken(color)) {
            throw new ColorNotAvailable("Color already taken.");
        }

        TruckerController truckerController = new TruckerController(nickname, color);
        //todo togli dopo debug da qui
        if(nickname.equals("testuser0")){
            CreateShipTest test=new CreateShipTest();
            try {
                truckerController.getTrucker().setSpaceShip(test.createShip3(truckerController.getSpaceShip()));
            } catch (InvalidCoordinates e) {
                throw new RuntimeException(e);
            }
        } else
        if(nickname.equals("testuser2")){
            CreateShipTest test=new CreateShipTest();
            try {
                truckerController.getTrucker().setSpaceShip(test.createShip1(truckerController.getSpaceShip()));
            } catch (InvalidCoordinates e) {
                throw new RuntimeException(e);
            }
        }//todo a qui
        else truckerController.initSpaceShip(game.getGameLevel());
        game.addTrucker(truckerController.getTrucker());

        loggedPlayers++;

        if (loggedPlayers == 4 || loggedPlayers ==numOfPlayers) {
            startBuildingPhase();
        }

        return truckerController;
    }

    /**
     * This method readds an old player in the game
     * @param nickname old player nickname
     * @param color old player color
     * @return trucker controller linked to the trucker
     */
    public synchronized TruckerController addOldPlayer(String nickname, PlayerColors color) {
        if (gameStarted) {
            throw new GameAlreadyStartedException("game already started");
        }
        return new TruckerController(nickname, color);

    }

    /**
     * This method sets the current card just drawn from the deck
     * @return the current card
     */
    public Card pickCardFromDeck(){
        this.currentCard = game.drawCard();
        return currentCard;
    }

    /**
     * This method sorts players by their position
     */
    public void orderByPosition(){
        game.sortTruckers();
    }

    /**
     * @return enum set of available player colors
     */
    public EnumSet<PlayerColors> showAvailableColors() {
        return game.showAvailableColors();
    }

    /**
     * This method set the game for building phase
     */
    public void startBuildingPhase() {
        if (gameStarted) return;
        gameStarted = true;
        game.setupGame();
    }

    /**
     * This method show the cards stack requested by the player
     * @param stackIndex index of the stack to show
     * @return the requested stack
     * @throws GeneralError if another player is holding the stack
     */
    public Stack showStack(int stackIndex) throws GeneralError {
        try {
            return game.viewStack(stackIndex);
        }catch (GeneralError e) {
            throw new GeneralError(e.getMessage());
        }

    }

    /**
     * @return this game controller instance
     */
    public static GameController getIstance() {
        return istance;
    }

    /**
     * This method sets gameStarted attribute
     * @param gameStarted gameStarted's new value
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

}
