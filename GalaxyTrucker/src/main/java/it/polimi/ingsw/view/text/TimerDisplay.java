package it.polimi.ingsw.view.text;

import it.polimi.ingsw.view.UI;

/**
 * The TimerDisplay class is responsible for managing a countdown timer with
 * a specified duration and notifying a UI component when the timer expires
 */
public class TimerDisplay {

    /**
     * Tracks the number of times the countdown timer has completed and expired
     */
    private int expirationCount;

    /**
     * The thread responsible for executing the countdown timer functionality
     * in the TimerDisplay class.
     */
    private Thread timerThread;

    /**
     * The UI component associated with the TimerDisplay class, responsible for
     * providing visual feedback to the user, such as displaying the countdown,
     * indicating when the timer starts, or showing when the timer has expired.
     */
    private final UI ui;


    /**
     * Constructs a TimerDisplay object and initializes the associated UI component.
     * @param ui the UI component that provides visual feedback for the timer, such
     *           as indicating the countdown progress or showing when the timer expires
     */
    public TimerDisplay(UI ui) {
        this.ui = ui;
        expirationCount = 0;
    }

    /**
     * Starts the countdown timer with the specified duration in seconds and
     * updates the associated UI component. Once the timer expires, it notifies
     * the UI and increments the expiration count.
     * @param durationSeconds the duration of the countdown timer in seconds
     */
    public void start(int durationSeconds) {
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

    /**
     * Stops the currently running countdown timer if it exists and is active.
     * This method interrupts the timer thread to cease its execution prematurely.
     */
    public void stop() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
    }
}
