package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.ColorNotAvailable;
import it.polimi.ingsw.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.exceptions.InvalidStateException;
import it.polimi.ingsw.exceptions.NicknameUsedException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class GameController {
    private final Game game;
    private int count=0;
    private final Timer startTimer = new Timer();
    private boolean timerStarted = false;
    private boolean gameStarted = false;
    private int numOfPlayers;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Card currentCard;


    public GameController(int numberOfTruckers, int gameLevel) {
        this.game=new Game(numberOfTruckers, gameLevel);
        this.numOfPlayers=numberOfTruckers;
        this.currentCard=null;
    }

    public Game getGame() {
        return game;
    }

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

    public synchronized TruckerController tryAddPlayer(String nickname, PlayerColors color) throws GameAlreadyStartedException, IllegalArgumentException, NicknameUsedException, ColorNotAvailable {
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
        game.addTrucker(truckerController.getTrucker());

        count++;

        if (count == 2 && !timerStarted) {
            startCountdown();
        }

        if (count == 4 || count==numOfPlayers) {
            startTimer.cancel();
            startBuildingPhase();
        }

        return truckerController;
    }

    public Card pickCardFromDeck(){
        this.currentCard = game.drawCard();
        return currentCard;
    }

    public void orderByPosition(ArrayList<ClientHandlerSocket> players){
        game.reorderPlayersByPosition(players);
    }

    public EnumSet<PlayerColors> showAvailableColors() {
        return game.showAvailableColors();
    }

    private void startCountdown() {
        timerStarted = true;
        startTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (GameController.this) {
                    if (game.getTruckerList().size() >= 2 && game.getTruckerList().size() < 4) {
                        startBuildingPhase();
                    }
                }
            }
        }, 2 * 60 * 1000); // 2 minuti
    }

    public void startBuildingPhase() {
        if (gameStarted) return;
        gameStarted = true;
        game.setupGame();
        startHourglassChecker();
    }

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

    public ArrayList<Card> showStack(int stackIndex) {
        return game.viewStack(stackIndex);
    }

    public synchronized void endBuildingFor(Trucker trucker) throws InvalidStateException {
        trucker.finish(); // FSM: controlla che sia valido e marca END

        if (game.allPlayersReady()) {
            game.startFlightPhase();
        }
    }

}
