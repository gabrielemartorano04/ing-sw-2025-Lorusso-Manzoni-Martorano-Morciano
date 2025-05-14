package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.Cabin;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
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
    private Component[] kept;


    @Override
    public void start(Stage primaryStage) throws InvalidCoordinates {
        availableComponents = new AvailableComponents();
        Trucker trucker= new Trucker("luca", PlayerColors.BLUE);
        kept = new Component[2];
        spaceship = new SpaceShip(PlayerColors.RED);
//        Cabin cabin = new Cabin(3, 3, 3, 3, "Main Cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web52.jpg", PlayerColors.RED, true);
//        trucker.getSpaceShip().addComponent(cabin, 7,7);
        kept[0] = availableComponents.getComponents().get(15);
        kept[1] = availableComponents.getComponents().get(130);

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

        gui.showKeptComponents(kept);
        gui.showShipLayout(trucker.getNickname(), trucker.getSpaceShip().getComponents());
//        gui.showWaitForOthers();
//        gui.showBuildComplete();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
