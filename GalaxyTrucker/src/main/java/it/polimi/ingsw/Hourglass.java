package it.polimi.ingsw;
import java.util.Timer;
import java.util.TimerTask;

public class Hourglass {

    private Timer timer;
    private boolean timeUp;
    private int secondiRimanenti;
    private Game game;

    public Hourglass(Game game) {
        timer = new Timer();
        timeUp = false;
        secondiRimanenti = 60;
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

    public boolean timeIsUp() {
        return timeUp;
    }

    //da chiamare qunado un trucker gira la clessidra
    public void reset() {
        if (secondiRimanenti > 0) {
            System.out.println(secondiRimanenti);
            return;
        }
        game.increaseTimeTurns();
        timer.cancel(); // Ferma il timer corrente
        timer = new Timer(); // Crea un nuovo timer
        timeUp = false;
        secondiRimanenti = 60;
        startTimer(); // Avvia il nuovo timer
    }
}
