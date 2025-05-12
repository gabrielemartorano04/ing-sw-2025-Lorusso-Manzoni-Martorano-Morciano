package it.polimi.ingsw.view.graphic;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUIApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        GUI gui = new GUI(primaryStage);
        gui.showWelcome();
        gui.showInsertNumPlayers();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
