package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Exceptions.TimeExpiredException;
import it.polimi.ingsw.Model.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {

    private GameState gameState;
    private int numPlayer;
    private AskController askController;
    private Game game;
    private CardController cardController;
    private SpaceShipController spaceShipController;

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
        this.game = new Game();
        this.cardController = new CardController(this.askController, this.game);
        this.spaceShipController = new SpaceShipController();
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public void addNumPlayer() {
         numPlayer++;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void manageGame(){
        do{
            numPlayer=0;
            try {
                manageSetUp();
            } catch (TimeExpiredException e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }while(gameState.equals(GameState.BUILDING));
        manageBuilding();
        manageFlight();
        manageEnded();
    }

    public void manageSetUp() throws TimeExpiredException {
        Timer timer = new Timer();
        boolean[] timeExpired = {false};
        long duration = 60000;
        long startTime = System.currentTimeMillis();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeExpired[0] = true; // Modifica il valore dell'array
                timer.cancel();
                System.out.println("Timer scaduto!"); // Aggiungi questa riga

            }
        };

        timer.schedule(task, duration);


        while(numPlayer<2 || numPlayer>4 && !timeExpired[0]){
            // mostro a scermo il tempo mancante attraverso la view
        }
        if (timeExpired[0]){
            throw new TimeExpiredException("Tempo scaduto! Non è stato raggiunto un numero valido di giocatori.");
        }
        game.setupGame(numPlayer);
        setGameState(GameState.BUILDING);
    }

    public void manageBuilding() {

    }

    public void manageFlight() {

    }

    public void manageEnded() {

    }

}
