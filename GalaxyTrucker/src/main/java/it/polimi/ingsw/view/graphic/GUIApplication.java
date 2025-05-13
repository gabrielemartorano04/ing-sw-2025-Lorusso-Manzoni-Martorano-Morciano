package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.Component;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class GUIApplication extends Application {


    private AvailableComponents availableComponents;
    private SpaceShip spaceship;
    private ArrayList<Card> stack;
    private Map<String, Integer> mockTrack;
    private ArrayList<Component> kept;


    @Override
    public void start(Stage primaryStage) {
        availableComponents = new AvailableComponents();
        spaceship = new SpaceShip();
        kept = new ArrayList<>();
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

        gui.showKeptComponents(kept);
        gui.showShipLayout("RED", spaceship.getComponents());

    }

    public static void main(String[] args) {
        launch(args);
    }
}
