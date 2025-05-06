package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.utilities.exceptions.CannotTurnHourglassException;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class GameController {
    private final Game game;
    private final List<TruckerController> truckerControllers = new ArrayList<>();
    private final Timer startTimer = new Timer();
    private boolean timerStarted = false;
    private boolean gameStarted = false;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public GameController() {
        this.game = new Game();
    }

    public synchronized boolean tryAddPlayer(String nickname, PlayerColors color) throws IllegalStateException, IllegalArgumentException {
        if (gameStarted) {
            throw new IllegalStateException("game already started");
        }

        if (game.isNicknameTaken(nickname) || game.isColorTaken(color)) {
            throw new IllegalArgumentException("Nickname or color already taken.");
        }

        Trucker trucker = new Trucker(nickname, color);
        TruckerController tc = new TruckerController(trucker, this);
        truckerControllers.add(tc);
        game.addTrucker(trucker);

        int count = game.getTruckers().size();

        if (count == 2 && !timerStarted) {
            startCountdown();
        }

        if (count == 4) {
            startTimer.cancel();
            startBuildingPhase();
        }

        return true;
    }

    private void startCountdown() {
        timerStarted = true;
        startTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (GameController.this) {
                    if (game.getTruckers().size() >= 2 && game.getTruckers().size() < 4) {
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
        for (Trucker t : game.getTruckers()) {
            if (t.getBuildingState() != BuildingState.WAIT_END) {
                t.setBuildingState(BuildingState.WAIT_END);
            }
        }
        scheduler.shutdown(); // ferma il controllo
    }

    public Component selectComponent(Trucker t, int index) throws InvalidComponentsException {
        return t.selectComponent(index, game);
    }

    public void keepComponent(Trucker t, Component c) throws InvalidComponentsException {
        t.keep(c, game);
    }

    public void linkComponent(Trucker trucker, Component component, int x, int y) {
        trucker.link(component, x, y);
    }

    public void turnHourglass(Trucker trucker) throws CannotTurnHourglassException {
        trucker.turnHourglass(game);
    }

    public ArrayList<Card> showStack(Trucker trucker, int stackIndex) {
        return trucker.showStack(stackIndex, game);
    }

    public synchronized void endBuildingFor(Trucker trucker) {
        trucker.finish(); // FSM: controlla che sia valido e marca END

        if (game.allPlayersReady()) {
            game.startFlightPhase();
        }
    }
}
