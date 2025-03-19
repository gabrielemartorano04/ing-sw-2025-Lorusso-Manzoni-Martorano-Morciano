package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.GameController;

public class GameView {

    private final GameController gameController;

    GameView(GameController gameController) {
        this.gameController = gameController;
    }

    public void playButton(){
        gameController.addNumPlayer();
    }

}
