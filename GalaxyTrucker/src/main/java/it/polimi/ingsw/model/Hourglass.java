package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class that manage the building phase timer
 */

public class Hourglass implements Serializable {
    /**
     * 90 seconds timer
     */
    private transient Timer timer;

    /**
     * this variable is true when time is up
     */
    private boolean timeUp;

    /**
     * seconds left in the timer
     */
    private int secondsLeft;

    /**
     * times the hourglass was turned
     */
    private int timeTurns;

    /**
     * Class constructor that initializes variables
     */
    public Hourglass() {
        timer = new Timer();
        timeUp = false;
        timeTurns = 0;
        secondsLeft = 90;
        startTimer();
    }

    /**
     * Method to start the timer
     */
    private void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer: " + secondsLeft);
                if (secondsLeft > 0) {
                    secondsLeft--;
                } else {
                    timeUp = true;
                    timer.cancel(); // Ferma il timer
                }
            }
        }, 0, 1000); // Esegui ogni secondo
    }

    /**
     * timeTurns getter
     * @return how many time the hourglass was turned
     */
    public int getTimeTurns(){
        return timeTurns;
    }

    /**
     * timeUp getter
     * @return true if time is up, false otherwise
     */
    public boolean timeIsUp() {
        return timeUp;
    }

    /**
     * This methos increase timeTurns variable. It's called when the hourglass is turned
     */
    public void increaseTimeTurns(){
        timeTurns++;
    }

    /**
     * This method resets and starts the timer when the hourglass is turned
     */
    public void reset() {
        if (secondsLeft > 0) {
            return;
        }
        increaseTimeTurns();
        timer.cancel(); // Ferma il timer corrente
        timer = new Timer(); // Crea un nuovo timer
        timeUp = false;
        secondsLeft = 90;
        startTimer(); // Avvia il nuovo timer
    }

    /**
     * secondLeft getter
     * @return seconds left for the timer
     */
    public int getSecondsLeft() {
        return secondsLeft;
    }

    /**
     * Sets the number of times the hourglass has been turned.
     *
     * @param timeTurns the number of time turns to set
     */
    public void setTimeTurns(int timeTurns) {
        this.timeTurns = timeTurns;
    }
}
