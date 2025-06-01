package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUIApplication extends Application {


    private AvailableComponents availableComponents;
    private SpaceShip spaceship;
    private ArrayList<Card> stack;
    private Map<String, Integer> mockTrack;
    private ArrayList<Component> kept;


    @Override
    public void start(Stage primaryStage) throws InvalidCoordinates {
        availableComponents = new AvailableComponents();
        Trucker trucker = new Trucker("luca", PlayerColors.BLUE);
        kept = new ArrayList<>();
        stack = new ArrayList<>();
        Card card1 = new OpenSpace(false, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_018.jpg", "");
        Card card2 = new Epidemic(false, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_015.jpg", "");
        Card card3 = new AbandonedShip(false, 2, 4, 6, 1, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0117.jpg", "");
        stack.add(card1);
        stack.add(card2);
        stack.add(card3);
        spaceship = new SpaceShip();
        //spaceship.setSpaceship(level, color); AGGIUNGIIIII
//        Cabin cabin = new Cabin(3, 3, 3, 3, "Main Cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web52.jpg", PlayerColors.RED, true);
//        trucker.getSpaceShip().addComponent(cabin, 7,7);
        kept.add(availableComponents.getComponents().get(15));
        kept.add(availableComponents.getComponents().get(130));

        mockTrack = new HashMap<>();
        mockTrack.put("RED", 0);
        mockTrack.put("GREEN", 5);
        mockTrack.put("BLUE", 12);
        mockTrack.put("YELLOW", 20);
        GUI gui = new GUI(primaryStage);

        //gui.showTrack(mockTrack);
        //gui.showShipLayout("Luca", spaceship.getComponents());
        //gui.showChooseColor(EnumSet.allOf(PlayerColors.class));
        //gui.showWelcome(EnumSet.allOf(PlayerColors.class));
        //gui.showInsertNumPlayers();
        //gui.showNicknameTaken();
        //gui.showStartCountdown(3);
        //gui.showBuildPhaseStart(components);
        //gui.showKeptComponents(kept);

        //gui.showWelcome(EnumSet.allOf(PlayerColors.class));
        //gui.showKeptComponents(kept);
        gui.showShipLayout(trucker.getNickname(), trucker.getSpaceShip().getComponents());
//        gui.showWaitForOthers();
//        gui.showBuildComplete();
        //gui.showSelectComponent();
        //gui.showAvailableComponent(availableComponents.getComponents());
        //gui.showStack(stack);
        //gui.showBuildOptions();
        //gui.showCardDrawn(card2);
        //gui.showTruckerLostAllCrew(trucker.getNickname());
        //gui.showConnectionLost();
        //gui.showMessage("luca ha perso tutte le merci");
        //gui.showPlayerShipStatus(12, 4, 3, 6, 5, mockTrack);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
