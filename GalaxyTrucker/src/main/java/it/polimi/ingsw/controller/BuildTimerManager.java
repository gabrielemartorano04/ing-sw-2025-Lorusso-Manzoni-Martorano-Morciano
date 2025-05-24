package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.BuildCompleteEvent;

public class BuildTimerManager {
    private static BuildTimerManager instance;
    private Thread timerThread;
    private boolean running = false;

    private BuildTimerManager() {}

    public static synchronized BuildTimerManager getInstance() {
        if (instance == null) {
            instance = new BuildTimerManager();
        }
        return instance;
    }

    public void start(ServerController controller, int seconds) {
        if (running) return; // evita di avviare più timer

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

    public void stop() {
        running = false;
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
    }

    public boolean isRunning() {
        return running;
    }
}
