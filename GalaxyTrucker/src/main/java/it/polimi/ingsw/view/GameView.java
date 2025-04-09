package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;

public class GameView {

    private final GameController gameController;

    GameView(GameController gameController) {
        this.gameController = gameController;
    }

    public void playButton(){
        gameController.addNumPlayer();
    }

}
