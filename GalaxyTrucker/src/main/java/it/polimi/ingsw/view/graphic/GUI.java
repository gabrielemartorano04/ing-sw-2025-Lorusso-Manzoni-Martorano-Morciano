package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.AlienCabinCoord;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.FlightStartEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.AvailableColorsEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.ColorAlreadyInError;
import it.polimi.ingsw.net.events.serverevents.setupevents.NicknameAlreadyInError;
import it.polimi.ingsw.view.ParserState;
import it.polimi.ingsw.view.UI;

import it.polimi.ingsw.view.ViewFlow;
import it.polimi.ingsw.view.graphic.scenes.BuildingScene;
import it.polimi.ingsw.view.graphic.scenes.FlightScene;
import it.polimi.ingsw.view.graphic.scenes.LoginScene;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.*;

public class GUI implements UI {

    private Stage stage;
    private LoginScene loginScene;
    private BuildingScene buildingScene;
    private FlightScene flightScene;
    private ClientEvent currentEvent;
    private Component selectedComponent;
    public final ViewFlow viewFlow;
    private boolean isLinkingEnabled;
    private final Map<String, Integer> previousPositions;

    public GUI(ViewFlow viewFlow) {

        //this.stage =new Stage();
        this.viewFlow = viewFlow;
        this.isLinkingEnabled = false;
        this.previousPositions = new HashMap<>();

        //new Thread(() -> Application.launch(GUIApplication.class)).start();

    }

    public void initGui(Stage primaryStage){
        this.stage=primaryStage;
        Scene loadingScene = new Scene(new StackPane(), 1600, 900);
        stage.setScene(loadingScene);
        stage.setTitle("Galaxy Trucker");
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    public void setCurrentEvent(ClientEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    public ClientEvent getCurrentEvent() {
        return currentEvent;
    }


    // insertnumPlayerscene

    @Override
    public void showInsertNumPlayers() {
        Platform.runLater(() -> {
            double width = 1600;
            double height = 900;

            Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(width);
            backgroundView.setFitHeight(height);
            backgroundView.setPreserveRatio(true);
            backgroundView.setMouseTransparent(true);

            VBox content = new VBox(12);
            content.setAlignment(Pos.CENTER);

            Label truckerLabel = new Label("Insert the number of truckers...");
            truckerLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
            truckerLabel.setTextFill(Color.web("#FFDE69"));

            ToggleGroup truckerGroup = new ToggleGroup();
            HBox truckerButtons = new HBox(10);
            truckerButtons.setAlignment(Pos.CENTER);

            for (int i = 2; i <= 4; i++) {
                RadioButton btn = new RadioButton(String.valueOf(i));
                btn.setToggleGroup(truckerGroup);
                btn.setUserData(i);
                btn.setFont(Font.font("Bangers", 16));
                btn.setTextFill(Color.WHITE);
                btn.setMinWidth(60);
                btn.setPrefHeight(35);
                truckerButtons.getChildren().add(btn);
            }

            Label levelLabel = new Label("...and the flight level");
            levelLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
            levelLabel.setTextFill(Color.web("#FFDE69"));

            ToggleGroup levelGroup = new ToggleGroup();
            HBox levelButtons = new HBox(10);
            levelButtons.setAlignment(Pos.CENTER);

            RadioButton trialBtn = new RadioButton("Trial Flight");
            trialBtn.setToggleGroup(levelGroup);
            trialBtn.setUserData(0);
            trialBtn.setFont(Font.font("Bangers", 16));
            trialBtn.setTextFill(Color.WHITE);
            trialBtn.setPrefWidth(120);
            trialBtn.setPrefHeight(35);

            RadioButton fullBtn = new RadioButton("Full Game");
            fullBtn.setToggleGroup(levelGroup);
            fullBtn.setUserData(2);
            fullBtn.setFont(Font.font("Bangers", 16));
            fullBtn.setTextFill(Color.WHITE);
            fullBtn.setPrefWidth(120);
            fullBtn.setPrefHeight(35);

            levelButtons.getChildren().addAll(trialBtn, fullBtn);

            Button submitButton = new Button("START");
            submitButton.setFont(Font.font("Bangers", 18));
            submitButton.setDisable(true);
            submitButton.setPrefWidth(150);
            submitButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

            ChangeListener<Toggle> updateStart = (obs, oldVal, newVal) -> {
                boolean enabled = truckerGroup.getSelectedToggle() != null && levelGroup.getSelectedToggle() != null;
                submitButton.setDisable(!enabled);
                submitButton.setStyle(enabled
                        ? "-fx-background-color: #FFDE69; -fx-text-fill: black;"
                        : "-fx-background-color: gray; -fx-text-fill: white;");
            };

            truckerGroup.selectedToggleProperty().addListener(updateStart);
            levelGroup.selectedToggleProperty().addListener(updateStart);

            submitButton.setOnAction(e -> {
                int players = (int) truckerGroup.getSelectedToggle().getUserData();
                int level = (int) levelGroup.getSelectedToggle().getUserData();
                viewFlow.handleInput(players + " " + level);
            });

            VBox.setMargin(truckerLabel, new Insets(10, 0, 0, 0));
            VBox.setMargin(truckerButtons, new Insets(5, 0, 0, 0));
            VBox.setMargin(levelLabel, new Insets(15, 0, 0, 0));
            VBox.setMargin(levelButtons, new Insets(5, 0, 0, 0));
            VBox.setMargin(submitButton, new Insets(20, 0, 0, 0));

            content.getChildren().setAll(
                    truckerLabel,
                    truckerButtons,
                    levelLabel,
                    levelButtons,
                    submitButton
            );

            StackPane root = new StackPane(backgroundView, content);
            StackPane.setAlignment(content, Pos.CENTER);

            if (stage.getScene() == null) {
                stage.setScene(new Scene(root, width, height));
            } else {
                stage.getScene().setRoot(root);
            }

            stage.centerOnScreen();
            stage.setFullScreen(true);
        });
    }

    @Override
    public void welcomeOldScene(ArrayList<PlayerColors> colors, ArrayList<String> nicknames) {
//        double width = 1600;
//        double height = 900;
//
//        ImageView backgroundView = new ImageView(new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm()));
//        backgroundView.setFitWidth(width);
//        backgroundView.setFitHeight(height);
//        backgroundView.setPreserveRatio(true);
//        backgroundView.setMouseTransparent(true);
//
//        Label titleLabel = new Label("Welcome back! Select your profile:");
//        titleLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 30));
//        titleLabel.setTextFill(Color.web("#FFDE69"));
//
//        // Contenitore per i riquadri
//        TilePane playerPane = new TilePane(30, 30);
//        playerPane.setAlignment(Pos.CENTER);
//
//        // Per gestire selezione
//        final StackPane[] selectedBox = {null};
//        final String[] selectedNickname = {null};
//        final PlayerColors[] selectedColor = {null};
//
//        for (int i = 0; i < colors.size(); i++) {
//            PlayerColors color = colors.get(i);
//            String nickname = nicknames.get(i);
//
//            Rectangle rect = new Rectangle(200, 80);
//            rect.setArcWidth(20);
//            rect.setArcHeight(20);
//            rect.setFill(getColorForEnum(color));
//
//            Label nameLabel = new Label(nickname);
//            nameLabel.setTextFill(Color.BLACK);
//            nameLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
//
//            StackPane box = new StackPane(rect, nameLabel);
//            box.setCursor(Cursor.HAND);
//
//            box.setOnMouseClicked(e -> {
//                // Rimuovi bordo precedente
//                if (selectedBox[0] != null) {
//                    selectedBox[0].setStyle("");
//                }
//                // Applica bordo al selezionato
//                box.setStyle("-fx-border-color: white; -fx-border-width: 3px;");
//                selectedBox[0] = box;
//                selectedNickname[0] = nickname;
//                selectedColor[0] = color;
//            });
//
//            playerPane.getChildren().add(box);
//        }
//
//        // Pulsante Confirm
//        Button confirmBtn = new Button("Confirm");
//        confirmBtn.setFont(Font.font("Bangers", 18));
//        confirmBtn.setTextFill(Color.BLACK);
//        confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
//        confirmBtn.setCursor(Cursor.HAND);
//        confirmBtn.setDisable(false);
//
//        confirmBtn.setOnAction(e -> {
//            if (selectedNickname[0] != null && selectedColor[0] != null) {
//                String input = selectedNickname[0] + " " + selectedColor[0];
//                viewFlow.handleInput(input);
//            }
//        });
//
//        VBox content = new VBox(30, titleLabel, playerPane, confirmBtn);
//        content.setAlignment(Pos.CENTER);
//        content.setPadding(new Insets(40));
//
//        this.root = new StackPane(backgroundView, content);
//        Scene scene = new Scene(root, width, height);
//        Stage stage = (Stage) root.getScene().getWindow(); // se già visibile
//        if (stage != null) stage.setScene(scene);
    }

    @Override
    public void showChooseResume() {

    }

    @Override
    public void resumeBuildingScene(ArrayList<String> playerNames){
        Platform.runLater(() -> {
            buildingScene=new BuildingScene(viewFlow.getNickname(),viewFlow.getShip().getComponents(),viewFlow, new StackPane());
            stage.setScene(this.buildingScene.createScene(playerNames, viewFlow.getLevel()));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
            if(viewFlow.getSelectedComponent()!=null){
                this.buildingScene.setTurnToDo((viewFlow.getSelectedComponent().getRotationAngle()/90));
                ImageView component = new ImageView(viewFlow.getSelectedComponent().getFrontImage());
                this.buildingScene.insertSelectedComponent(component, true);
            }
            for(int i = 0; i < viewFlow.getKeptComponent().size(); i++){
                Component c = viewFlow.getKeptComponent().get(i);
                if(c!=null){
                    this.buildingScene.insertComponentToKepts(c);
                }
            }
        });
    }

    @Override
    public void resumeFlightScene(ArrayList<String> playerNames){
        this.flightScene = new FlightScene(viewFlow.getNickname(), viewFlow.getShip().getComponents(), viewFlow, new StackPane());
        stage.setScene(this.flightScene.createScene(playerNames, viewFlow.getLevel()));
        stage.setTitle("Galaxy Trucker");
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();

    }


    //fine insertNumPlayerscene



    // welcome scene

    @Override
    public void welcomeScene(AvailableColorsEvent event){
        Platform.runLater(() -> {
            this.loginScene = new LoginScene(event.getColors(), viewFlow);
            stage.setScene(this.loginScene.createScene(false));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
        });
    }

    @Override
    public void nickNameAlreadyTaken(NicknameAlreadyInError event){
        Platform.runLater(() -> {
            this.loginScene.showNicknameTakenPopup();
        });
    }

    @Override
    public void colorAlreadyTaken(ColorAlreadyInError event){
        Platform.runLater(() -> {
            loginScene.showColorTakenPopup();
            loginScene.markColorAsUnavailable(event.getColor());
        });
    }

    @Override
    public void showPlayerJoined(String nickname) {
        this.loginScene.showPlayerJoined(nickname);
    }

    // fine welcome scene


    // building scene

    @Override
    public void startBuildingScene(StartBuildingEvent event){
        this.loginScene.hideWaitForOthers();
        Platform.runLater(() -> {
            buildingScene=new BuildingScene(viewFlow.getNickname(),viewFlow.getShip().getComponents(),viewFlow, new StackPane());
            stage.setScene(this.buildingScene.createScene(event.getPlayers(), viewFlow.getLevel()));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
        });
    }

    @Override
    public void showDiscardComponent(Component component) {
        Platform.runLater(() -> {
            this.buildingScene.resetComponentSelectedBox();
        });
    }

    @Override
    public void componentLinked(ComponentLinkedEvent event){
        Platform.runLater(() -> {
            this.buildingScene.insertComponentInGrid(event.getComponent(),event.getX(),event.getY());
        });
    }

    @Override
    public void showStack(Stack stack) {
        Platform.runLater(() -> {
            this.buildingScene.showStack(stack);
        });
    }

    @Override
    public void componentNotAvailable(ComponentNotAvailableError event){
        //TODO: notifica che il componente che hai provato a selezionare non è disponibile
        // vogliamo farti restare nella parte dove vedi le componenti disponibili e farti selezionare un altra componente
        // vogliamo che venga aggiornata la lista dei componenti cioè non devono essere plottati tutti quelli che hanno il selected a true

        //this.buildingScene.showComponentNotAvailablePopup();
    }

    @Override
    public void componentSelected(ComponentSelectedEvent event){
        // ti riporta nella schermata dove vedi la nave con visualizzato il componente che hai selezionato e le posibili azioni
        Platform.runLater(() -> {
            ImageView component = new ImageView(this.viewFlow.getSelectedComponent().getFrontImage());
            this.buildingScene.insertSelectedComponent(component, false);
        });
    }

    @Override
    public void invalidCoordinates(InvalidCoordinatesError event) {
        Platform.runLater(() -> {
            this.buildingScene.showPopUpError(event.getMessage());
        });
    }


    @Override
    public void turnHourGlassError(TurnHourglassError event) {
        Platform.runLater(() -> {
            this.buildingScene.showPopUpError(event.getMessage());

        });
    }

    @Override
    public void turnHourGlass(int seconds){
        Platform.runLater(() -> {
            this.buildingScene.startHourglassTimer();
        });
    }

    @Override
    public void showTimeExpired(int expirationCount) {
        Platform.runLater(() -> {
            this.buildingScene.showTimeExpired(expirationCount);
        });
    }


    @Override
    public void keptComponent(KeptComponentEvent event){
        Platform.runLater(() -> {
            this.buildingScene.insertComponentToKepts(event.getKept());
        });
    }

    @Override
    public void componentTurned(TurnedComponentSuccessEvent event){
        Platform.runLater(() -> {
            ImageView component = new ImageView(this.viewFlow.getSelectedComponent().getFrontImage());
            this.buildingScene.insertSelectedComponent(component, true);
        });
    }

    @Override
    public void keptSelected(SuccessSelectKept event){
        Platform.runLater(() -> {
            this.buildingScene.selectKeptComponent(event.getIndex());
        });
    }

    @Override
    public void refreshAvailableComponents(){
        Platform.runLater(() -> {
            this.buildingScene.refreshComponentsViewIfVisible();
        });
    }

    @Override
    public void showAvailableComponent(ArrayList<Component> components) {
        Platform.runLater(() -> {
            this.buildingScene.refreshComponentsViewIfVisible();
        });
    }

    @Override
    public void showKeptComponents(ArrayList<Component> kept) {
        // nella gui non deve fare nulla forse c'è un modo migliore di unificare ma per ora tengo cosi
    }

    @Override
    public void dropStack(StackDropOk event){
        //torna alla schermata base del buildind
        Platform.runLater(() -> {
            this.buildingScene.hideStackOverlay();
        });
    }


    @Override
    public void  buildingComplete(BuildCompleteEvent event){
        Platform.runLater(() -> {
            this.buildingScene.hideWaitForOthers();
            this.buildingScene.buildShipGrid(false, viewFlow.getShip().getComponents());
            viewFlow.handleInput("alien");
        });
    }


    @Override
    public void askAlienCabin(AlienCabinCoord event){
        Platform.runLater(() -> {
            this.buildingScene.updateAlienCabinCoord(event.getAlienCabinCoord());
            this.buildingScene.showAlienCabin();
        });
    }

    // fine building scene



    // flight scene


    @Override
    public void flightScene(FlightStartEvent event) {
        Platform.runLater(() -> {
            this.buildingScene.hideWaitForOthers();

            this.flightScene = new FlightScene(viewFlow.getNickname(), viewFlow.getShip().getComponents(), viewFlow, new StackPane());
            stage.setScene(this.flightScene.createScene(event.getPlayers(), viewFlow.getLevel()));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();

            this.flightScene.showStateInput();

        });
    }

    @Override
    public void showCurrCondition(CurrentCondition event, boolean isLeader) {
        //TODO:
        Platform.runLater(() -> {
            this.flightScene.clearRollDiceResult();
            this.flightScene.showCurrCondition(event.getCondition(), isLeader);
        });

    }



    @Override
    public void cardFinished(CardFinished event){
        Platform.runLater(() -> {
            this.flightScene.clearRollDiceResult();
            this.flightScene.setShip(viewFlow.getShip().getComponents());
            this.flightScene.updateShipLayout();
            this.flightScene.updateStatusBar();
            this.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        });
    }

    @Override
    public void showTrack(Map<String, Integer> truckers, int level){
        Platform.runLater(() -> {
            this.flightScene.updateTrack(truckers);
        });
    }

    @Override
    public void showCardDrawn(StartCardEvent event) {
        Platform.runLater(() -> {
            this.flightScene.hideWaitForOthers();
            this.flightScene.updateCardInScene(event.getCard());
        });
    }

    @Override
    public  void epidemic(EpidemicEvent event){

        Platform.runLater(() -> {
            this.flightScene.setShip(viewFlow.getShip().getComponents());
            this.flightScene.updateShipLayout();
            this.flightScene.updateStatusBar();
            this.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        });
    }

    @Override
    public void showStardustCard(StarDustEvent event) {
        Platform.runLater(() -> {
            this.flightScene.updateCardInScene(event.getCard());
        });
    }

    @Override
    public void updateCurrGood(Goods good) {
        Platform.runLater(() -> {
            this.flightScene.updateCurrGood(good);
        });
    }

    @Override
    public void updateGoodsListToRemove(ArrayList<Goods> goodsList) {
        Platform.runLater(() -> {
            // this.flightScene.updateGoodsListToRemove(goodsList);
        });
    }

    @Override
    public void showEpidemicCard(EpidemicEvent event) {
        Platform.runLater(() -> {
            this.flightScene.updateCardInScene(event.getCard());
        });
    }

    @Override
    public void stardust(StarDustEvent event){

        Platform.runLater(() -> {
            this.flightScene.setShip(viewFlow.getShip().getComponents());
            this.flightScene.updateShipLayout();
            this.flightScene.updateStatusBar();
            this.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        });
    }

    @Override
    public void showInputFormat(ParserState parserState) {
        Platform.runLater(() -> {
            switch (parserState){
                case ACTIVATE_ENGINES ->  this.flightScene.showActivateEngineInput();
                case ACTIVATE_CANNONS -> this.flightScene.showActivateCannonsInput();
                case ACTIVATE_SHIELD -> this.flightScene.showActivateShieldInput();
                case LAND -> this.flightScene.showLandInput();
                case HUMAN_COORD -> this.flightScene.showHumanCoordInput();
                case PLOT_GOOD -> this.flightScene.showPlotGoodInput();
                case ROLL_DICE -> this.flightScene.showRollDiceInput();
                case PLANET_DECISION-> this.flightScene.showPlanetDecisionInput();
                case DRAW_CARD -> this.flightScene.showDrawCardInput();
                case REMOTION_CONFIRMED -> this.flightScene.showRemotionConfirmedInput();
                case REMOVE_BATTERIES -> this.flightScene.showRemoveBatteriesInput();
                case REMOVE_GOODS -> this.flightScene.showRemoveGoodsInput();
                case REARRANGE -> this.flightScene.showRearrangeInput();
                case COSMIC_CREDITS -> this.flightScene.showCosmicCreditsInput();
                case WARZONE -> this.flightScene.showWarzoneInput();

            }
        });
    }

    @Override
    public void showResult(RollDiceResult event){
        this.flightScene.clearRollDiceResult();
        this.flightScene.showRollDiceResult(event.getDiceResult());
    }

    @Override
    public void showDead(){
//        this.flightScene.showDeadTrucker();
    }

    // fine flight scene


    // metodi validi in piu scene

    @Override
    public void showWaitForOthers() {
        if(viewFlow.getViewState() == ViewFlow.ViewState.START){
            this.loginScene.showWaitForOthers();
        }else if (viewFlow.getViewState() == ViewFlow.ViewState.BULDING){
            this.buildingScene.showWaitForOthers();
        }
        else if (viewFlow.getViewState() == ViewFlow.ViewState.FLIGTH){
            //TODO: ricordati di inserire l'hide del showWaitForOthers per flight quando finisce lo showWaitForOthers
            this.flightScene.showWaitForOthers();
        }
    }

    @Override
    public void showMessage(String message) {
        if(viewFlow.getViewState() == ViewFlow.ViewState.BULDING){
            this.buildingScene.showMessage(message);
        }else if(viewFlow.getViewState() == ViewFlow.ViewState.FLIGTH){
            this.flightScene.showMessage(message);
        }
    }

    @Override
    public void showOtherShipLayout(String nickname, Component[][] layout) {
        if (viewFlow.getViewState() == ViewFlow.ViewState.BULDING) {
            Platform.runLater(() -> {
                this.buildingScene.showOtherShip(layout, nickname);
            });
        } else if (viewFlow.getViewState() == ViewFlow.ViewState.FLIGTH) {
            Platform.runLater(() -> {
                //TODO: mettere showOthership dentro la fligth scene
                // this.flightScene.showOtherShip(layout, nickname);
            });
        }
    }

    // fine eventi validi in piu scene


    // end scene

    // fine gioco




    @Override
    public void showBuildOptions() {
        // Nella gui non deve fare nulla serve per la tui sarebbe da unificare meglio
    }





    @Override
    public void showShipLayout(String nickname, Component[][] layout){
        //serve per la TUI forse c'è modo di unificare meglio intanto non rimuovere
        return;
    }




    @Override
    public void showTruckerWithdrawn(String nickname) {
        Label message = new Label(nickname + " has withdrawn");
        message.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.8); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-background-radius: 10px;"
        );
        message.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        StackPane overlay = new StackPane(message);
        overlay.setMouseTransparent(true);
        StackPane.setAlignment(message, Pos.TOP_RIGHT);
        StackPane.setMargin(message, new Insets(15, 15, 0, 0));

        Platform.runLater(() -> {
            Scene scene = stage.getScene();
            Node currentRoot = scene.getRoot();

            StackPane newRoot;
            if (!currentRoot.getClass().getName().equals("javafx.scene.layout.StackPane")) {
                newRoot = new StackPane(currentRoot);
                scene.setRoot(newRoot);
            } else {
                newRoot = (StackPane) currentRoot;
            }

            newRoot.getChildren().add(overlay);

            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(e -> newRoot.getChildren().remove(overlay));
            delay.play();
        });
    }


    @Override
    public void updatePlanetsOccupied(PlanetDecision event) {
        Platform.runLater(() -> {
            this.flightScene.updatePlanetsOccupied(event.getPlanetsOccupied());
        });
    }

    @Override
    public void showGameEnded() {

    }

    @Override
    public void showScores(Map<String, Integer> finalScores) {

    }

    @Override
    public void showError(String message) {
        if(viewFlow.getViewState() == ViewFlow.ViewState.BULDING){
            this.buildingScene.showError(message);
        }else if(viewFlow.getViewState() == ViewFlow.ViewState.FLIGTH){
            this.flightScene.showError(message);
        }
    }


    @Override
    public void showPlayerShipStatus() {
        Platform.runLater(() -> {
            this.flightScene.updateStatusBar();
        });
    }






    @Deprecated
    public void showTrackOld(Map<String, Integer> truckers, int level) {
        double width = 1600;
        double height = 900;

        // 1. Board di sfondo
        Image boardImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-5.png").toExternalForm());
        ImageView boardView = new ImageView(boardImage);
        boardView.setFitHeight(500);
        boardView.setPreserveRatio(true);
        boardView.setMouseTransparent(true);

        // 2. Overlay contenente triangoli
        Pane overlay = new Pane();

        // 3. Parametri del tracciato (ellisse)
        double centerX = width / 2 - 5;
        double centerY = height / 2 + 2;
        double radiusX = 315;
        double radiusY = 175;

        int totalSteps = 24;
        int startOffset = 2; // posizione iniziale ruotata (0 = alto, 6 = basso)

        // 4. Colori associati ai giocatori
        Map<String, Color> playerColors = Map.of(
                "RED", Color.RED,
                "GREEN", Color.LIMEGREEN,
                "BLUE", Color.DODGERBLUE,
                "YELLOW", Color.GOLD
        );

        // 5. Disegna i triangoli (track positions)
        for (int i = 0; i < totalSteps; i++) {
            // Calcolo angolo e posizione
            double angle = 2 * Math.PI * ((i + startOffset) % totalSteps) / totalSteps - Math.PI / 2;
            double x = centerX + radiusX * Math.cos(angle);
            double y = centerY + radiusY * Math.sin(angle);

            // Triangolo
            Polygon triangle = new Polygon();
            triangle.getPoints().addAll(
                    x, y - 15,      // punta
                    x - 15, y + 5,   // base sinistra
                    x + 15, y + 5    // base destra
            );

            triangle.setStroke(Color.WHITE);
            triangle.setStrokeWidth(2);
            triangle.setRotate(Math.toDegrees(angle) + 180); // punta verso direzione inversa
            triangle.setFill(Color.TRANSPARENT); // default: trasparente

            // Colora il triangolo se un giocatore si trova qui
            for (Map.Entry<String, Integer> entry : truckers.entrySet()) {
                if (entry.getValue() == i) {
                    String name = entry.getKey().toUpperCase();
                    triangle.setFill(playerColors.getOrDefault(name, Color.GRAY));
                    break;
                }
            }

            overlay.getChildren().add(triangle);
        }

        // 6. Stack finale
        StackPane root = new StackPane(boardView, overlay);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setFullScreen(true);
    }



}
