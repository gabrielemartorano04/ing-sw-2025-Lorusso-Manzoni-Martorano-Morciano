package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.EnumSet;

public class GUIApplication extends Application {


    private AvailableComponents availableComponents;

    @Override
    public void start(Stage primaryStage) {
        AvailableComponents components = new AvailableComponents();
        GUI gui = new GUI(primaryStage);
        gui.showAvailableComponent(components);
        gui.showTruckerWithdrawn("luca");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
