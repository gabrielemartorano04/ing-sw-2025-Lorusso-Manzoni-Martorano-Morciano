package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.ColorNotAvailable;
import it.polimi.ingsw.exceptions.NicknameUsedException;
import it.polimi.ingsw.exceptions.TimeExpiredException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;

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
    private ArrayList<PlayerColors> availableColor;
    private ArrayList<String> usedNickname;
    private int[] scores;

    public GameController() {
        this.gameState = GameState.SETUP;
        this.numPlayer = 0;
        this.askController = new AskController();
        this.game = new Game();
        this.cardController = new CardController(this.askController, this.game);
        this.spaceShipController = new SpaceShipController();
        this.availableColor = new ArrayList<PlayerColors>();
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

    public void manageSetUp() throws TimeExpiredException, NicknameUsedException {
        availableColor.add(PlayerColors.YELLOW);
        availableColor.add(PlayerColors.RED);
        availableColor.add(PlayerColors.GREEN);
        availableColor.add(PlayerColors.BLUE);
        Timer timer = new Timer();
        PlayerColors color = null;
        String nickname = null;
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

        while((numPlayer<2 || numPlayer>4) && !timeExpired[0]){
            // mostro a scermo il tempo mancante attraverso la view e prendo il nickname e il color dell'utente
            do {
                color = askController.askColor();
                if (!availableColor.contains(color)) {
                    //catch in askColor
                    throw new ColorNotAvailable("Color already selected!");
                }
            }while(!availableColor.contains(color));
            availableColor.remove(color);
            do {
                nickname = askController.askNickname();
                if(usedNickname.contains(nickname)){
                    //catch in askNickname
                    throw new NicknameUsedException("Nickname already used!");
                }
            } while (usedNickname.contains(nickname));
            usedNickname.add(nickname);
        }
        if (timeExpired[0]){
            throw new TimeExpiredException("Tempo scaduto! Non è stato raggiunto un numero valido di giocatori.");
        }
        game.makeStacks();
        game.makeTrucker(numPlayer, nickname, color);
        setGameState(GameState.BUILDING);
    }

    public void manageBuilding() {
        ArrayList<Thread> threads = new ArrayList<>();
        //creo un thread per giocatore
        for (Trucker t: game.getTruckerList()){
            Thread playerThread = new Thread(() -> {
                
            });
        }
    }

    public void manageFlight() {
        game.makeGameDeck();
        Card card;
        do
        {
            card = game.drawCard();
            cardController.applyCardEffect(card);
            game.sortTruckers();
            game.turn();
        }while(card != null);
    }

    public void manageEnded() {
        scores = game.computeScores(numPlayer);
    }

}
