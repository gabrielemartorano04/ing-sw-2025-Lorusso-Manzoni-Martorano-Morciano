package it.polimi.ingsw.controller;

public class CheckHourglassController extends Thread {
    private GameController gameController;

    public CheckHourglassController(GameController gameController) {
    this.gameController = gameController;
    }

    public void run(){
        if(gameController.getGame().isHourglassExpired()) {
            //mandare a tutti
        }
    }
}
