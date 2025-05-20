package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */

public class Hourglass implements Serializable {

    private Timer timer;
    private boolean timeUp;
    private int secondiRimanenti;
    private Game game;
    private int timeTurns;

    public Hourglass(Game game) {
        timer = new Timer();
        timeUp = false;
        timeTurns = 0;
        secondiRimanenti = 90;
        startTimer();
        this.game = game;
    }

    private void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (secondiRimanenti > 0) {
                    secondiRimanenti--;
                } else {
                    timeUp = true;
                    timer.cancel(); // Ferma il timer
                }
            }
        }, 0, 1000); // Esegui ogni secondo
    }

    public int getTimeTurns(){
        return timeTurns;
    }

    public boolean timeIsUp() {
        return timeUp;
    }

    public void increaseTimeTurns(){
        timeTurns++;
    }

    //da chiamare qunado un trucker gira la clessidra
    public void reset() {
        if (secondiRimanenti > 0) {
            System.out.println(secondiRimanenti);
            return;
        }
        increaseTimeTurns();
        timer.cancel(); // Ferma il timer corrente
        timer = new Timer(); // Crea un nuovo timer
        timeUp = false;
        secondiRimanenti = 90;
        startTimer(); // Avvia il nuovo timer
    }

    public int getSecondiRimanenti() {
        return secondiRimanenti;
    }


}
