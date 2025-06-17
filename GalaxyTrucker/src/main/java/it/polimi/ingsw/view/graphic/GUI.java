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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;
import java.util.List;

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


    //fine insertNumPlayerscene



    // welcome scene

    @Override
    public void welcomeScene(AvailableColorsEvent event){
        Platform.runLater(() -> {
            this.loginScene = new LoginScene(event.getColors(), viewFlow);
            stage.setScene(this.loginScene.createScene());
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
            buildingScene=new BuildingScene(viewFlow.getNickname(),viewFlow.getShip(),viewFlow, new StackPane());
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
    public void turnHourGlass(HourglassTurnEvent event){
        Platform.runLater(() -> {
            this.buildingScene.startHourglassTimer();
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
            System.out.println("Turning component view");
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
            this.buildingScene.buildShipGrid(false, viewFlow.getShip());
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
            this.flightScene = new FlightScene(viewFlow.getNickname(), viewFlow.getShip(), viewFlow, new StackPane());
            stage.setScene(this.flightScene.createScene(event.getPlayers(), viewFlow.getLevel()));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
            this.flightScene.showStateInput();
        });
    }

    @Override
    public void showCurrCondition(CurrentCondition event) {
        //TODO:
        Platform.runLater(() -> {
            this.flightScene.showCurrContidion(event.getCondition());
        });

    }

    @Override
    public void cardFinished(CardFinished event){
        Platform.runLater(() -> {
            this.flightScene.createAndAddShipLayout();
            this.flightScene.updateStatusBar();
            this.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        });
    }

    @Override
    public  void epidemic(EpidemicEvent event){

        Platform.runLater(() -> {
            this.flightScene.createAndAddShipLayout();
            this.flightScene.updateStatusBar();
            this.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        });

    }

    @Override
    public void stardust(StarDustEvent event){

        Platform.runLater(() -> {
//          this.flightScene.showUpdateSpaceship();
//          this.flightScene.showUpdateTrack();
        });
    }

    @Override
    public void showInputFormat(ParserState parserState) {
        Platform.runLater(() -> {
            switch (parserState){
                case STATE-> this.flightScene.showStateInput();
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
        }else{
            this.flightScene.showMessage(message);
        }
    }

    @Override
    public void showotherShipLayout(String nickname, Component[][] layout) {
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
    public void showInvalidPlacement(String reason) {

    }


    @Override
    public void showTimeExpired() {
        // Crea la scritta TIME EXPIRED
        Label message = new Label("TIME EXPIRED");
        message.setTextFill(Color.RED);
        message.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 100));

        // Effetto lampeggiante
        FadeTransition blink = new FadeTransition(Duration.seconds(0.4), message);
        blink.setFromValue(1.0);
        blink.setToValue(0.0);
        blink.setCycleCount(Animation.INDEFINITE);
        blink.setAutoReverse(true);
        blink.play();

        // Crea un rettangolo grigio semitrasparente come sfondo
        Rectangle dimBackground = new Rectangle(stage.getWidth(), stage.getHeight());
        dimBackground.setFill(Color.rgb(50, 50, 50, 0.6)); // grigio con opacità 60%
        dimBackground.widthProperty().bind(stage.getScene().widthProperty());
        dimBackground.heightProperty().bind(stage.getScene().heightProperty());

        // Overlay con sfondo e scritta
        StackPane overlay = new StackPane(dimBackground, message);
        overlay.setAlignment(Pos.CENTER);
        overlay.setMouseTransparent(true);

        // Recupera root (deve essere uno StackPane)
        StackPane root = (StackPane) stage.getScene().getRoot();
        root.getChildren().add(overlay);

        // Rimuove l'overlay dopo 5 secondi
        PauseTransition wait = new PauseTransition(Duration.seconds(5));
        wait.setOnFinished(e -> root.getChildren().remove(overlay));
        wait.play();
    }


    @Override
    public void showBuildComplete() {
        double width = 1600;
        double height = 900;

        // Sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Testi
        Label line1 = new Label("WELL DONE!");
        line1.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 40));
        line1.setTextFill(Color.web("#FFDE69"));

        Label line2 = new Label("Spaceship built");
        line2.setFont(Font.font("Bangers", FontWeight.BOLD, 32));
        line2.setTextFill(Color.web("#FFDE69"));

        Label line3 = new Label("Get ready for launch!");
        line3.setFont(Font.font("Bangers", FontWeight.BOLD, 32));
        line3.setTextFill(Color.web("#FFDE69"));

        VBox textBox = new VBox(10, line1, line2, line3);
        textBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(backgroundView, textBox);
        StackPane.setAlignment(textBox, Pos.CENTER);

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setFullScreen(true);
    }

    @Override
    public void showCardDrawn(StartCardEvent event) {
        Platform.runLater(() -> {
        });
    }

    @Override
    public void showStardustCard(StarDustEvent event) {

    }

    @Override
    public void updateCurrGood(Goods good) {
        Platform.runLater(() -> {
            this.flightScene.updateCurrGood(good);
        });
    }

    @Override
    public void showEpidemicCard(EpidemicEvent event) {

    }


    @Override
    public void showShipLayout(String nickname, Component[][] layout){
        //serve per la TUI forse c'è modo di unificare meglio intanto non rimuovere
        return;
    }


    public void showCardDrawn(String photo) {
        double width = stage.getScene().getWidth();
        double height = stage.getScene().getHeight();

        // Immagine della carta
        ImageView cardView = new ImageView(photo);
        cardView.setFitWidth(180);
        cardView.setPreserveRatio(true);
        cardView.setMouseTransparent(true);

        // Contorno bianco con angoli smussati
        Rectangle border = new Rectangle(180, 280); // altezza proporzionale alla larghezza
        border.setArcWidth(15);
        border.setArcHeight(15);
        border.setFill(Color.WHITE);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(2);

        StackPane cardWithBorder = new StackPane(border, cardView);
        cardWithBorder.setAlignment(Pos.BOTTOM_LEFT);

        VBox cardBox = new VBox(10, cardWithBorder);
        cardBox.setAlignment(Pos.BOTTOM_LEFT);
        cardBox.setPadding(new Insets(0, 0, 0, 20)); // margine sinistro

        StackPane container = new StackPane(cardBox);
        container.setMouseTransparent(false);

        Platform.runLater(() -> {
            Node rootNode = stage.getScene().getRoot();
            if (rootNode instanceof StackPane root) {
                root.getChildren().add(container);
            }
        });
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
    public void showGoodsGained(Goods goods) {

    }

    @Override
    public void updatePlanetsOccupied(PlanetDecision event) {

    }

    @Override
    public void showGameEnded() {

    }

    @Override
    public void showScores(Map<String, Integer> finalScores) {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showTrack(Map<String, Integer> truckers, int level){
        Platform.runLater(() -> {
            this.flightScene.updateTrack(truckers);
        });
    }


    @Override
    public void showPlayerShipStatus(int position, int batteryCount, int humanCount, float cannonPower, int enginePower, Map<String, Integer> goodsByColor, int credits) {
        // Carica le icone (temporaneamente tutte uguali)
        Image engine = new Image(getClass().getResource("/it/polimi/ingsw/view/engine.png").toExternalForm());
        Image cannon = new Image(getClass().getResource("/it/polimi/ingsw/view/cannon.png").toExternalForm());
        Image human = new Image(getClass().getResource("/it/polimi/ingsw/view/human.png").toExternalForm());
        Image battery = new Image(getClass().getResource("/it/polimi/ingsw/view/engine.png").toExternalForm());
        Image pos = new Image(getClass().getResource("/it/polimi/ingsw/view/engine.png").toExternalForm());


        ImageView posIcon = new ImageView(pos);
        ImageView batteryIcon = new ImageView(battery);
        ImageView humanIcon = new ImageView(human);
        ImageView cannonIcon = new ImageView(cannon);
        ImageView engineIcon = new ImageView(engine);

        List<ImageView> mainIcons = List.of(posIcon, batteryIcon, humanIcon, cannonIcon, engineIcon);
        for (ImageView iv : mainIcons) {
            iv.setFitWidth(16);
            iv.setPreserveRatio(true);
        }

        Label posLabel = new Label(String.valueOf(position));
        Label battLabel = new Label(String.valueOf(batteryCount));
        Label humanLabel = new Label(String.valueOf(humanCount));
        Label cannonLabel = new Label(String.valueOf(cannonPower));
        Label engineLabel = new Label(String.valueOf(enginePower));

        List<Label> labels = List.of(posLabel, battLabel, humanLabel, cannonLabel, engineLabel);
        for (Label l : labels) {
            l.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            l.setTextFill(Color.WHITE);
        }

        HBox posBox = new HBox(5, posIcon, posLabel);
        HBox battBox = new HBox(5, batteryIcon, battLabel);
        HBox humanBox = new HBox(5, humanIcon, humanLabel);
        HBox cannonBox = new HBox(5, cannonIcon, cannonLabel);
        HBox engineBox = new HBox(5, engineIcon, engineLabel);

        for (HBox box : List.of(posBox, battBox, humanBox, cannonBox, engineBox)) {
            box.setAlignment(Pos.CENTER_RIGHT);
        }

        // Goods box
        HBox goodsBox = new HBox(10);
        goodsBox.setAlignment(Pos.CENTER_RIGHT);
        Image redgood = new Image(getClass().getResource("/it/polimi/ingsw/view/redgood.png").toExternalForm());
        Image yellowgood = new Image(getClass().getResource("/it/polimi/ingsw/view/yellowgood.png").toExternalForm());
        Image greengood = new Image(getClass().getResource("/it/polimi/ingsw/view/greengood.png").toExternalForm());
        Image bluegood = new Image(getClass().getResource("/it/polimi/ingsw/view/bluegood.png").toExternalForm());
        ImageView goodIcon;
        for (Map.Entry<String, Integer> entry : goodsByColor.entrySet()) {
            switch (entry.getKey().toLowerCase()) {
                case "red" -> goodIcon = new ImageView(redgood);
                case "yellow" -> goodIcon = new ImageView(yellowgood);
                case "green" -> goodIcon = new ImageView(greengood);
                case "blue" -> goodIcon = new ImageView(bluegood);
                default -> {
                    continue;
                }
            }
            goodIcon.setFitWidth(16);
            goodIcon.setPreserveRatio(true);

            Label qty = new Label(String.valueOf(entry.getValue()));
            qty.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            qty.setTextFill(Color.WHITE);

            HBox singleGood = new HBox(4, goodIcon, qty);
            singleGood.setAlignment(Pos.CENTER_RIGHT);
            goodsBox.getChildren().add(singleGood);
        }

        // Contenitore finale con sfondo
        HBox statusBar = new HBox(20, posBox, battBox, humanBox, cannonBox, engineBox, goodsBox);
        statusBar.setAlignment(Pos.CENTER_RIGHT);
        statusBar.setPadding(new Insets(10));
        statusBar.setStyle("");
        statusBar.setMaxWidth(800);
        statusBar.setMouseTransparent(true);

        Platform.runLater(() -> {
            Node rootNode = stage.getScene().getRoot();
            // Supponiamo che sia sempre uno StackPane come da tua struttura
            StackPane root = (StackPane) rootNode;

            StackPane.setAlignment(statusBar, Pos.CENTER_RIGHT);
            StackPane.setMargin(statusBar, new Insets(0, 150, -500, 0));

            if (!root.getChildren().contains(statusBar)) {
                root.getChildren().add(statusBar);
            }
        });
    }

    @Deprecated
    public void showShipLayoutOld(String nickname, Component[][] layout) {
        double width = 1600;
        double height = 900;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-1b.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        boolean[][] validCells = {
                {false, false, false, false, false, false, false,  false,  false, false, false},
                {false, false, false, false, false, false, false,  false,  false, false, false},
                {false, false, false, false, false, false, false,  false,  false, false, false},
                {false, false, false, false, false, false, false,  false,  false, false, false},
                {false, false, false, false, false, false, false,  false,  false, false, false},
                {false, false, false, false, false, false, true,  false,  true, false, false},
                {false, false, false, false, false, true,  true,  true,  true, true,  false},
                {false, false, false, false, true,  true,  true,  true, true, true,  true },
                {false, false, false, false, true,  true,  true,  true,  true, true,  true },
                {false, false, false, false, true,  true,  true,  false,  true, true,  true }
        };

        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setTranslateX(164.5);
        grid.setTranslateY(25);

        for (int row = 5; row < 10; row++) {
            for (int col = 4; col < 11; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(90, 90);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 3px; -fx-background-radius: 3px;");

                    Component c = layout[row][col];
                    if (c != null ) {
                        ImageView imageView = new ImageView(c.getFrontImage());
                        imageView.setFitWidth(90);
                        imageView.setFitHeight(89);
                        Rectangle clip = new Rectangle(89, 89);
                        clip.setArcWidth(6);
                        clip.setArcHeight(6);
                        imageView.setClip(clip);
                        cell.getChildren().add(imageView);
                    }

                    final int rIndex = row;
                    final int cIndex = col;

                    //attivo il booleano per poter avviare la link quando premo
                    cell.setOnMouseClicked(event -> {
                        if (isLinkingEnabled) {
                            viewFlow.handleInput("link " + rIndex + " " + cIndex);
                            isLinkingEnabled = false;
                        }
                        event.consume();
                    });

                } else {
                    cell.setStyle("-fx-background-color: transparent;");
                }

                grid.add(cell, col - 4, row - 5);
            }
        }

        StackPane root = new StackPane(backgroundView, grid);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setFullScreen(true);
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


    @Deprecated
    public void showAvailableComponentOld(ArrayList<Component> components) {
        //TODO: la gui non deve mostrare gli available ma deve solo aggiornarli cioè aggiungere quello discard a quelli che ci sono

        double width = 1600;
        double height = 900;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        int col = 0;
        int row = 0;
        int i = 0;
        for (Component component : components) {
            final int index = i;
            ImageView imgView = new ImageView(component.getCovered() ? component.getBackImage() : component.getFrontImage());
            imgView.setFitWidth(70);
            imgView.setFitHeight(70);

            Rectangle clip = new Rectangle(75, 75);
            clip.setArcWidth(16);
            clip.setArcHeight(16);
            imgView.setClip(clip);

            Button button = new Button();
            button.setGraphic(imgView);
            button.setPrefSize(75, 75);
            button.setStyle("-fx-background-color: transparent; -fx-border-color: #cccccc; -fx-border-radius: 8px; -fx-cursor: hand; -fx-padding: 0px;");

            button.setOnAction(e -> {
                viewFlow.handleInput("select " + index);
                //TODO inserire ritorno alla schermata precedente con la spaceship e il resto
            });

            grid.add(button, col, row);
            col++;
            if (col == 8) {
                col = 0;
                row++;
            }
            i++;
        }

        ScrollPane scrollPane = new ScrollPane(new StackPane(grid));
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Pulsante "Back"
        Button backBtn = new Button("←");
        backBtn.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 14));
        backBtn.setTextFill(Color.BLACK);
        backBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        backBtn.setCursor(Cursor.HAND);
        backBtn.setPadding(new Insets(10, 12, 10, 12));
        backBtn.setOnAction(e -> viewFlow.handleInput("back")); //TODO inserire il ritorno corretto alla schermata precedente

        // Contenitore principale
        StackPane root = new StackPane(backgroundView, scrollPane, backBtn);
        StackPane.setAlignment(backBtn, Pos.TOP_LEFT);
        StackPane.setMargin(backBtn, new Insets(20, 0, 0, 20));

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setFullScreen(true);

    }


    @Deprecated
    public void showKeptComponentsOld(ArrayList<Component> kept) {
        if (kept == null || kept.isEmpty()) return;  // Non mostra nulla se vuota

        HBox keptSlots = new HBox(10);
        keptSlots.setMaxWidth(200);
        keptSlots.setAlignment(Pos.TOP_RIGHT);

        int maxSlots = Math.min(kept.size(), 2);  // massimo 2, ma solo se esistono

        for (int i = 0; i < maxSlots; i++) {
            final int index = i;

            StackPane slot = new StackPane();
            slot.setPrefSize(70, 70);
            slot.setMinSize(70, 70);
            slot.setMaxSize(70, 70);
            slot.setStyle("-fx-background-color: transparent;");

            Rectangle border = new Rectangle(70, 70);
            border.setArcWidth(3);
            border.setArcHeight(3);
            border.setFill(Color.TRANSPARENT);
            border.setStroke(Color.WHITE);
            border.setStrokeWidth(2);

            Component comp = kept.get(i);
            if (comp != null) {
                ImageView imageView = new ImageView(comp.getFrontImage());
                imageView.setFitWidth(70);
                imageView.setFitHeight(70);

                Rectangle clip = new Rectangle(70, 70);
                clip.setArcWidth(6);
                clip.setArcHeight(6);
                imageView.setClip(clip);

                imageView.setOnMouseClicked(event -> {
                    viewFlow.handleInput("selectkept " + index);
                    isLinkingEnabled = true;
                    event.consume();
                });

                slot.getChildren().addAll(border, imageView);
            } else {
                slot.getChildren().add(border);
            }

            keptSlots.getChildren().add(slot);
        }

        StackPane.setAlignment(keptSlots, Pos.TOP_RIGHT);
        StackPane.setMargin(keptSlots, new Insets(30, 50, 0, 0));

        Platform.runLater(() -> {
            StackPane stackRoot = (StackPane) stage.getScene().getRoot();
            if (!stackRoot.getChildren().contains(keptSlots)) {
                stackRoot.getChildren().add(keptSlots);
            }
        });
    }

    public void showContinue(){
        // vorrei che mostrasse il popup che ti fa cliccare continue, serve solo per la flight
    }

    @Override
    public void welcomeOldScene(ArrayList<PlayerColors> colors, ArrayList<String> nicknames){
        // TODO: per funzionalità aggiuntiva ci pensiamo piu avanti
    }

}
