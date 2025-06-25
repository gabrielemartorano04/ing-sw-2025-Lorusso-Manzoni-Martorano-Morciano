package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.net.control.ServerController;
import it.polimi.ingsw.net.events.serverevents.buildingevents.BuildCompleteEvent;

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
     * @param controller Server Controller
     * @param seconds timer duration is seconds
     */
    public void start(ServerController controller, int seconds) {
        if (running) return; // one timer at the time

        running = true;
        timerThread = new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000L);
                if (running) {
                    System.out.println("[TIMER] Tempo scaduto. Invia BuildCompleteEvent");
                    controller.broadcastEvent(new BuildCompleteEvent(Game.getIstance().getTruckerList()));
                }
            } catch (InterruptedException e) {
                System.out.println("[TIMER] Interrotto prima del termine");
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
}
