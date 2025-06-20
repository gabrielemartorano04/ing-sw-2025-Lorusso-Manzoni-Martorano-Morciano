package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.net.socket.client.ClientEventSender;
import it.polimi.ingsw.net.socket.client.NetworkClient;
import it.polimi.ingsw.view.ViewFlow;
import it.polimi.ingsw.view.graphic.scenes.BuildingScene;
import it.polimi.ingsw.view.graphic.scenes.FlightScene;
import it.polimi.ingsw.view.graphic.scenes.ResultScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Color.red;

public class AppPerTest extends Application {


    private AvailableComponents availableComponents;
    private SpaceShip spaceship;
    private Stack stack;
    private Map<String, Integer> mockTrack, color;
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
        Card card1 = new OpenSpace(false, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_018.jpg", "",0);
        Card card2 = new Epidemic(false, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_015.jpg", "",0);
        Card card3 = new AbandonedShip(false, 2, 4, 6, 1, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0117.jpg", "",0);
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
        mockTrack.put("luca", 0);
        mockTrack.put("frenk", 5);
        mockTrack.put("gabri", 12);
        mockTrack.put("manzo", 20);


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
//        BuildingScene buildingScene = new BuildingScene(trucker.getNickname(), createShip1(trucker.getSpaceShip()).getComponents(), viewFlow, stackPane);
//        primaryStage.setScene(buildingScene.createScene(playersNames, 2));
//        buildingScene.showTimeExpired(2);
//        primaryStage.setResizable(true);        // ← abilita ridimensionamento
//        primaryStage.setFullScreenExitHint(""); // ← facoltativo, se in fullscreen
//        primaryStage.show();

//        buildingScene.insertSelectedComponent(cannon, false);
//        buildingScene.insertComponentInGrid(cannon,6,7);
//        buildingScene.insertComponentToKepts(cannon);
//        buildingScene.selectKeptComponent(0);
//        buildingScene.showPopUpError("Errore");
//        buildingScene.selectKeptComponent(0);
//        buildingScene.showOtherShip(trucker.getSpaceShip().getComponents(), trucker.getNickname());
//        buildingScene.updateAlienCabinCoord(new ArrayList<>(){
//            {
//                add(new int[]{5,6,0});
//                add(new int[]{7,8,0});
//                add(new int[]{6,9,1});
//            }
//        });
//        buildingScene.showAlienCabin();
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
        FlightScene flightScene = new FlightScene(trucker.getNickname(), createShip1(trucker.getSpaceShip()).getComponents(), viewFlow, stackPane);
        primaryStage.setScene(flightScene.createScene(playersNames, 2));

        primaryStage.setResizable(true);        // ← abilita ridimensionamento
        primaryStage.setFullScreenExitHint(""); // ← facoltativo, se in fullscreen
        primaryStage.show();
        //flightScene.showCurrCondition(Condition.ENGINES, true);
        //flightScene.showRollDiceInput();
//        flightScene.updatePlanetsOccupied(new ArrayList<>(){
//            {
//                add(trucker);
//                add(null);
//                add(null);
//
//            }
//        });
//        flightScene.showPlanetDecisionInput();
//        flightScene.updateTrack(mockTrack);
//        color = new HashMap<>();
//        color.put("red", 3);
//        color.put("yellow", 2);
//        color.put("green", 4);
//        color.put("blue", 1);

//        Map<String, Integer> scores = new HashMap<>();
//        scores.put("luca", 0);
//        scores.put("frenk", 5);
//        scores.put("gabri", 12);
//        scores.put("manzo", 20);
//
//        StackPane stackPane = new StackPane();
//        ResultScene resultScene = new ResultScene(viewFlow, stackPane);
//        primaryStage.setScene(resultScene.createScene(scores));
//        primaryStage.setResizable(true);        // ← abilita ridimensionamento
//        primaryStage.setFullScreenExitHint(""); // ← facoltativo, se in fullscreen
//        primaryStage.show();

    }

    public SpaceShip createShip1(SpaceShip ship) throws InvalidCoordinates {
        ship = new SpaceShip();
        ship.initSpaceShip(2,PlayerColors.RED);
        Component c1 = new BatteryStorage(3, 2, 0, 0, "Battery Storage", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web2.jpg", 2);
        Component c2 = new Cannon(0, 3, 2, "Cannon", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web133.jpg", true);
        Component c3 = new Cabin(0, 2, 0, 3, "Cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web47.jpg", null, false);
        Component c4 = new LifeSupport(1, 1, 0, 1, "Life Support", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web137.jpg", AlienColor.BROWN);
        Component c5 = new Cannon(1, 1, 1, "Cannon", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web115.jpg", false);
        Component c6 = new Pipes(1, 3, 2, 3, "Pipes", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web56.jpg");
        Component c7 = new BatteryStorage(0, 0, 0, 1, "Battery Storage", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web17.jpg", 3);
        Component c8 = new Pipes(3, 2, 0, 3, "Pipes", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web59.jpg");
        Component c9 = new Cabin(1, 0, 2, 3, "Cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web49.jpg", null, false);
        Component c10 = new Load(2, 2, 2, 3, "Load", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web66.jpg", 1, true);
        Component c11 = new Cabin(0, 1, 3, 2, "Cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web41.jpg", null, false);
        Component c12 = new Cannon(2, 1, 0, "Cannon", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web109.jpg", false);
        Component c13 = new Pipes(3, 2, 2, 3, "Pipes", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web60.jpg");
        Component c14 = new Engine(3, 1, 0, "Engine", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web95.jpg", true);
        Component c15 = new Shield(1, 2, 1, 2, "Shield", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web154.jpg");
        Component c16 = new Load(2, 0, 1, 2, "Load", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web32.jpg", 3, false);
        Component c17 = new Engine(2, 0, 3, "Engine", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web100.jpg", true);
        Component c18 = new Cannon(1, 0, 0, "Cannon", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web105.jpg", false);
        Component c19 = new Engine(2, 1, 1, "Engine", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web81.jpg", false);
        Component c20 = new Engine(0, 0, 3, "Engine", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web89.jpg", false);
        Component c21 = new Load(0, 0, 0, 2, "Load", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web70.jpg", 2, true);
        ship.addComponent(c10, 7, 8);
        c9.turn();
        ship.addComponent(c9, 7, 6);
        c8.turn();
        ship.addComponent(c8, 7, 5);
        ship.addComponent(c11, 7, 9);
        ship.addComponent(c5, 6, 7);
        c4.turn();
        ship.addComponent(c4, 6, 6);
        c3.turn();
        ship.addComponent(c3, 6, 5);
        ship.addComponent(c6, 6, 8);
        ship.addComponent(c7, 6, 9);
        c1.turn();
        c1.turn();
        ship.addComponent(c1, 5, 6);
        ship.addComponent(c2, 5, 8);
        c12.turn();
        ship.addComponent(c12, 7, 10);
        ship.addComponent(c14, 8, 7);
        ship.addComponent(c13, 8, 5);
        c15.turn();
        ship.addComponent(c15, 8, 8);
        c16.turn();
        ship.addComponent(c16, 8, 9);
        ship.addComponent(c17, 8, 10);
        ship.addComponent(c19, 9, 5);
        ship.addComponent(c18, 9, 4);
        ship.addComponent(c20, 9, 6);
        c21.turn();
        ship.addComponent(c21, 9, 8);
        return ship;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
