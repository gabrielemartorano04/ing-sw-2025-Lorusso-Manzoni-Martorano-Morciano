package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Model.*;
import java.util.ArrayList;

public class GameController {

    private GameState gameState;
    private int numPlayer;
    private AskController askController;

    public enum GameState {
        SETUP,  // Attesa giocatori
        BUILDING,    // Setup in corso
        FLIGTH, // Partita in corso
        ENDED     // Partita terminata
    }

    public GameController() {
        this.gameState = GameState.SETUP;
        this.numPlayer = 0;
        this.askController = new AskController();

    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void manageGame(Game game){
        while
        game.setupGame(numPlayer);


    }

}
