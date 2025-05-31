package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class manages the game controller. It's used by the server.
 * Attributes:
 * - game: Game instance
 * - count: int counter of logged players
 * - timerStarted: boolean, true if the timer started, false otherwise
 * - gameStarted: boolean, true if the game started, false otherwise
 * - numOfPlayers: int, number of player decided by the first one
 * - scheduler: ScheduledExecutorService, threads scheduler
 * - currentCard: card, current card during flight phase
 */
public class GameController {
    private final Game game;
    private int count=0;
    private final Timer startTimer = new Timer();//serve??
    private boolean timerStarted = false;
    private boolean gameStarted = false;
    private final int numOfPlayers;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
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
     * @return game instance
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
        truckerController.getSpaceShip().setSpaceship(game.getGameLevel(), color);
        game.addTrucker(truckerController.getTrucker());

        count++;

        if (count == 4 || count==numOfPlayers) {
            startTimer.cancel();
            startBuildingPhase();
        }

        return truckerController;
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
     * @param players arraylist of players' Client handler sockets
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
        startHourglassChecker();
    }

    /**
     * SERVE?????????????
     */
    private void startHourglassChecker() {
        scheduler.scheduleAtFixedRate(() -> {
            if (game.isHourglassExpired()) {
                forcePlayersToWaitEnd();
            }
        }, 0, 2, TimeUnit.SECONDS); // ogni 2 secondi controlla
    }

    private synchronized void forcePlayersToWaitEnd() {
        for (Trucker t : game.getTruckerList()) {
            if (t.getBuildingState() != BuildingState.WAIT_END) {
                t.setBuildingState(BuildingState.WAIT_END);
            }
        }
        scheduler.shutdown(); // ferma il controllo
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

}
