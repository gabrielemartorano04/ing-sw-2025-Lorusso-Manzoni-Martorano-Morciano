package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.cards.*;
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


    @Override
    public void start(Stage primaryStage) {
        AvailableComponents components = new AvailableComponents();
        SpaceShip spaceship = new SpaceShip();
        Map<String, Integer> mockTrack = new HashMap<>();
        mockTrack.put("RED", 0);
        mockTrack.put("GREEN", 5);
        mockTrack.put("BLUE", 12);
        mockTrack.put("YELLOW", 20);
        GUI gui = new GUI(primaryStage);
        //gui.showTrack(mockTrack);
        gui.showShipLayout("Luca", spaceship.getComponents());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
