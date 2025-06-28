package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.ServerController;
import it.polimi.ingsw.net.events.serverevents.buildingevents.BuildCompleteEvent;

import java.util.ArrayList;

/**
 * This class is used to manage the timer that controls the building time.
 */
public class BuildTimerManager {

    /**
     * This timer istance
     */
    private static BuildTimerManager instance;

    /**
     * The timer thread
     */
    private Thread timerThread;

    /**
     * It says if the timer is running
     */
    private boolean running = false;

    /**
     * The class constructor
     */
    private BuildTimerManager() {
        this.instance=null;
        this.timerThread = null;
    }

    /**
     * @return the instance of the classe BuildTimerManager
     */
    public static synchronized BuildTimerManager getInstance() {
        if (instance == null) {
            instance = new BuildTimerManager();
        }
        return instance;
    }

    /**
     * This method manages the timer start
     * @param seconds timer duration is seconds
     */
    public void start(int seconds) {
        if (running) return;

        running = true;
        timerThread = new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000L);
                System.out.println("[TIMER] Time expired for the third time");
                stop();
                handleFinalTimeout();

            } catch (InterruptedException e) {
                System.out.println("[TIMER] Interrupted before the end");
                Thread.currentThread().interrupt();
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    /**
     * This method manages the timer stop
     */
    public void stop() {
        running = false;
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
    }

    /**
     * This method indicates if the timer is running at the moment or not
     * @return true if the timer is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Handles the case when the timer is ended for the third time
     */
    private void handleFinalTimeout() {
        ArrayList<Trucker> truckers = Game.getIstance().getTruckerList();
        boolean[] pos = new boolean[]{false, false, false, false};
        ArrayList<String> nickNamesIllegalShip = new ArrayList<>();

        for (Trucker t : truckers) {
            if (t.getPosition() != -1) {
                switch (t.getPosition()) {
                    case 6 -> pos[0] = true;
                    case 3 -> pos[1] = true;
                    case 1 -> pos[2] = true;
                    case 0 -> pos[3] = true;
                }
            }
        }

        for (Trucker t : truckers) {
            if (t.getPosition() == -1) {
                for (int i = 0; i < pos.length; i++) {
                    if (!pos[i]) {
                        switch (i) {
                            case 0 -> t.setPosition(6);
                            case 1 -> t.setPosition(3);
                            case 2 -> t.setPosition(1);
                            case 3 -> t.setPosition(0);
                        }
                        pos[i] = true;
                        break;
                    }
                }
            }

        }

        //TODO TOGLI

        for (Trucker t : truckers) {
            System.out.println("Trucker " + t.getNickname() + " pos " + t.getPosition());
        }

        ServerController.getIstance().broadcastEvent(new BuildCompleteEvent(truckers, GameController.getIstance().getIllegalShipNames()));
    }

}
