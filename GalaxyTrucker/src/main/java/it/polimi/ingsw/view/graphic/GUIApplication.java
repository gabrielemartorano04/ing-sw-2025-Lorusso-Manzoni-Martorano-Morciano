package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.SpaceShip;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.EnumSet;

public class GUIApplication extends Application {


    private AvailableComponents availableComponents;
    private SpaceShip spaceship;

    @Override
    public void start(Stage primaryStage) {
        AvailableComponents components = new AvailableComponents();
        SpaceShip spaceship = new SpaceShip();
        GUI gui = new GUI(primaryStage);
        gui.showShipLayout("luca", spaceship.getComponents());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
