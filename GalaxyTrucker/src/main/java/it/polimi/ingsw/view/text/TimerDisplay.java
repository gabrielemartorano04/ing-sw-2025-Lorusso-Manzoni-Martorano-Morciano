package it.polimi.ingsw.view.text;

import it.polimi.ingsw.view.UI;

public class TimerDisplay {
    private int durationSeconds;
    private long startTime;
    private int expirationCount = 0;
    private Thread timerThread;
    private final UI ui;

    public TimerDisplay(UI ui) {
        this.ui = ui;
    }

    public void start(int durationSeconds) {
        this.startTime = System.currentTimeMillis();
        this.durationSeconds=durationSeconds;
        ui.turnHourGlass(durationSeconds);

        timerThread = new Thread(() -> {
            try {
                Thread.sleep(durationSeconds * 1000L);
            } catch (InterruptedException e) {
                System.out.println("Timer interrupted.");
                return;
            }

            expirationCount++;
            ui.showTimeExpired(expirationCount);
        });

        timerThread.setDaemon(true);
        timerThread.start();
    }

    public long getTimeLeft() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        long remaining = durationSeconds - elapsed;
        return Math.max(0, remaining);
    }

    public int getExpirationCount() {
        return expirationCount;
    }


    public void stop() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
    }
}
