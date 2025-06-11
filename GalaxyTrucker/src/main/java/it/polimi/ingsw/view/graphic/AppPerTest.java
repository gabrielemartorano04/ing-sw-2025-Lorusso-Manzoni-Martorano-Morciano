package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.Cabin;
import it.polimi.ingsw.model.components.Cannon;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.net.events.serverevents.setupevents.ColorAlreadyInError;
import it.polimi.ingsw.net.socket.client.ClientEventSender;
import it.polimi.ingsw.net.socket.client.NetworkClient;
import it.polimi.ingsw.view.ViewFlow;
import it.polimi.ingsw.view.graphic.scenes.BuildingScene;
import it.polimi.ingsw.view.graphic.scenes.FlightScene;
import it.polimi.ingsw.view.graphic.scenes.LoginScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class AppPerTest extends Application {


    private AvailableComponents availableComponents;
    private SpaceShip spaceship;
    private Stack stack;
    private Map<String, Integer> mockTrack;
    private ArrayList<Component> kept, kept2;
    private Component c;


    @Override
    public void start(Stage primaryStage) throws InvalidCoordinates {
        ViewFlow viewFlow = new ViewFlow(new ClientEventSender(new NetworkClient("localhost", 12346)), true);
        availableComponents = new AvailableComponents();
        ArrayList<String> playersNames = new ArrayList<>();
        playersNames.add("Luca");
        playersNames.add("Manzo");
        playersNames.add("Gabri");

        c = new Cannon(3, 0, 0, "Cannon", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web129.jpg", true);
        Trucker trucker = new Trucker("luca", PlayerColors.BLUE);
        kept = new ArrayList<>();
        kept2 = new ArrayList<>();
        //stack = new Stack();
        Card card1 = new OpenSpace(false, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_018.jpg", "");
        Card card2 = new Epidemic(false, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_015.jpg", "");
        Card card3 = new AbandonedShip(false, 2, 4, 6, 1, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0117.jpg", "");
        stack = new Stack(0);
        stack.addCardInStack(card1);
        stack.addCardInStack(card2);
        stack.addCardInStack(card3);
        spaceship = new SpaceShip();
        //spaceship.setSpaceship(level, color); AGGIUNGIIIII
        Cannon cannon = new Cannon(3, 0, 0, "Cannon", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web129.jpg", true);
//        trucker.getSpaceShip().addComponent(cabin, 7,7);
        kept.add(availableComponents.getComponents().get(15));
        kept.add(availableComponents.getComponents().get(130));

        mockTrack = new HashMap<>();
        mockTrack.put("RED", 0);
        mockTrack.put("GREEN", 5);
        mockTrack.put("BLUE", 12);
        mockTrack.put("YELLOW", 20);


        // DA SISTEMARE la creazione della gui e il collegamento a viewFlow
        GUI gui = new GUI(viewFlow);

        //gui.showTrack(mockTrack, 2);
        //gui.showShipLayout("Luca", spaceship.getComponents());
        //gui.showChooseColor(EnumSet.allOf(PlayerColors.class));
        //gui.showWelcome(EnumSet.allOf(PlayerColors.class));
        //gui.showInsertNumPlayers();
        //gui.showNicknameTaken();
        //gui.showStartCountdown(3);
        //gui.showBuildPhaseStart(components);
        //gui.showKeptComponents(kept);

        //gui.showWelcome(EnumSet.allOf(PlayerColors.class));
//        gui.showKeptComponents(kept);
        //gui.showShipLayout(trucker.getNickname(), trucker.getSpaceShip().getComponents());
        //gui.showComponentSelected(c);
        // gui.showWaitForOthers();
        //gui.showBuildComplete();
        //gui.showSelectComponent();
        //gui.showAvailableComponent(availableComponents.getComponents());
        //gui.showStack(stack);
        //gui.showBuildOptions();
        //gui.showCardDrawn(card2);
        //gui.showTruckerLostAllCrew(trucker.getNickname());
        //gui.showConnectionLost();
        //gui.showMessage("luca ha perso tutte le merci");
        //gui.showPlayerShipStatus(12, 4, 3, 6, 5, mockTrack);
        //gui.colorAlreadyTaken(new ColorAlreadyInError(PlayerColors.RED, EnumSet.of(PlayerColors.GREEN, PlayerColors.BLUE, PlayerColors.YELLOW)));ù

        trucker.initSpaceShip(2);

//        LoginScene loginScene = new LoginScene(EnumSet.allOf(PlayerColors.class), viewFlow);
//        primaryStage.setScene(loginScene.createScene());
//        loginScene.showWaitForOthers();
//        primaryStage.setResizable(true);
//        primaryStage.setFullScreenExitHint("");
//        primaryStage.setTitle("Test Building Scene");
//        primaryStage.show();



//        StackPane stackPane = new StackPane();
//        BuildingScene buildingScene = new BuildingScene(trucker.getNickname(), trucker.getSpaceShip().getComponents(), viewFlow, stackPane);
//        Scene scene = buildingScene.createScene(playersNames, 2);
//        buildingScene.showStack(stack);
//        buildingScene.hideStackOverlay();
//
////        buildingScene.insertSelectedComponent(cannon, false);
////        buildingScene.insertComponentInGrid(cannon,6,7);
////        buildingScene.insertComponentToKepts(cannon);
////        buildingScene.selectKeptComponent(0);
////        buildingScene.showPopUpError("Errore");
////        buildingScene.selectKeptComponent(0);
////        buildingScene.showOtherShip(trucker.getSpaceShip().getComponents(), trucker.getNickname());
//
//
//        primaryStage.setScene(scene);
//
//
//        primaryStage.setResizable(true);        // ← abilita ridimensionamento
//        primaryStage.setFullScreenExitHint(""); // ← facoltativo, se in fullscreen
//        primaryStage.show();
//
////        ArrayList<Trucker> planets = new ArrayList<>(){
////            {
////                add(trucker);
////                add(null);
////            }
////        };
//
//
        StackPane stackPane = new StackPane();
        FlightScene flightScene = new FlightScene(trucker.getNickname(), trucker.getSpaceShip().getComponents(), viewFlow, stackPane);
        primaryStage.setScene(flightScene.createScene(playersNames, 2));

        primaryStage.setResizable(true);        // ← abilita ridimensionamento
        primaryStage.setFullScreenExitHint(""); // ← facoltativo, se in fullscreen
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
