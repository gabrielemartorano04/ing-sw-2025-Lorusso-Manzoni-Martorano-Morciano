package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
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
import it.polimi.ingsw.view.graphic.scenes.ResultScene;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;

/**
 * The GUI class represents the graphical user interface for the application.
 * This class extends the UI class and is responsible for managing and displaying
 * various game scenes and events
 */
public class GUI implements UI {

    /**
     * Represents the primary used in the GUI for rendering and managing scenes
     */
    private Stage stage;

    /**
     * Represents the login scene in the graphical user interface
     */
    private LoginScene loginScene;

    /**
     * Represents the scene where the building phase of the game takes place
     */
    private BuildingScene buildingScene;

    /**
     * Represents the scene where the flight phase of the game takes place
     */
    private FlightScene flightScene;

    /**
     * Represents the scene where the result phase of the game takes place
     */
    private ResultScene resultScene;

    /**
     * Represents the ViewFlow component associated with the GUI
     */
    public final ViewFlow viewFlow;

    /**
     * This variable determines whether the warzone feature or view
     * should be displayed in the GUI
     */
    private boolean showWarzone;

    /**
     * Constructs a GUI instance that initializes the graphical user interface of the game
     * @param viewFlow an instance of {@code ViewFlow} that handles the flow of the game view
     */
    public GUI(ViewFlow viewFlow) {
        this.viewFlow = viewFlow;
        this.showWarzone = true;
    }

    /**
     * Initializes the graphical user interface for the application, setting up the primary stage
     * @param primaryStage the primary stage for the application on which the scenes and other GUI elements are displayed
     */
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

    /**
     * Displays the user interface for selecting the number of players and game mode
     */
    @Override
    public void showInsertNumPlayers() {
        Platform.runLater(() -> {
            double width = 1600;
            double height = 900;

            Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg")).toExternalForm());
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

    /**
     * Displays a graphical interface prompting the user to choose between resuming an existing game or starting a new game.
     */
    @Override
    public void showChooseResume() {
        Platform.runLater(() -> {
            double width = 1600;
            double height = 900;

            ImageView backgroundView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg")).toExternalForm()));
            backgroundView.setFitWidth(width);
            backgroundView.setFitHeight(height);
            backgroundView.setPreserveRatio(true);
            backgroundView.setMouseTransparent(true);

            Label messageLabel = new Label("There is an old game to resume");
            messageLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 32));
            messageLabel.setTextFill(Color.web("#FFDE69"));
            messageLabel.setAlignment(Pos.CENTER);

            Button resumeBtn = new Button("Resume");
            resumeBtn.setFont(Font.font("Bangers", 20));
            resumeBtn.setTextFill(Color.BLACK);
            resumeBtn.setCursor(Cursor.HAND);
            resumeBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            resumeBtn.setPrefWidth(160);
            resumeBtn.setOnAction(e -> viewFlow.handleInput("y"));

            Button newGameBtn = new Button("New Game");
            newGameBtn.setFont(Font.font("Bangers", 20));
            newGameBtn.setTextFill(Color.BLACK);
            newGameBtn.setCursor(Cursor.HAND);
            newGameBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            newGameBtn.setPrefWidth(160);
            newGameBtn.setOnAction(e -> viewFlow.handleInput("n"));

            HBox buttonBox = new HBox(30, resumeBtn, newGameBtn);
            buttonBox.setAlignment(Pos.CENTER);

            VBox content = new VBox(40, messageLabel, buttonBox);
            content.setAlignment(Pos.CENTER);
            content.setPadding(new Insets(40));

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

    /**
     * Resumes the building scene in the GUI, setting up and displaying the related graphical interface
     * @param playerNames a list of player names participating in the game, used to customize the building scene
     */
    @Override
    public void resumeBuildingScene(ArrayList<String> playerNames){
        Platform.runLater(() -> {
            buildingScene=new BuildingScene(viewFlow.getShip().getComponents(),viewFlow, new StackPane());
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

    /**
     * Resumes the flight scene in the GUI, setting up and displaying the related graphical interface
     * @param playerNames a list of player names participating in the game, used to customize the flight scene
     */
    @Override
    public void resumeFlightScene(ArrayList<String> playerNames) {
        Platform.runLater(() -> {
            this.flightScene = new FlightScene(viewFlow.getShip().getComponents(), viewFlow, new StackPane());
            stage.setScene(this.flightScene.createScene(playerNames, viewFlow.getLevel()));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
        });

    }

    /**
     * Initializes the login scene in the GUI, allowing the user to re-enter an ongoing game
     * by setting up the scene with the provided player colors and nicknames.
     * @param colors a list of {@code PlayerColors} representing the colors associated with each player in the game
     * @param nicknames a list of {@code String} representing the nicknames of the players in the game
     */
    @Override
    public void welcomeOldScene(ArrayList<PlayerColors> colors, ArrayList<String> nicknames) {
        Platform.runLater(() -> {
            this.loginScene = new LoginScene(viewFlow);
            stage.setScene(this.loginScene.createScene(true, nicknames,colors));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
        });
    }

    /**
     * Displays the welcome scene in the graphical user interface, initializing and setting up
     * the login scene with the available player color options provided in the event
     * @param event an instance of {@code AvailableColorsEvent} containing the set of available
     *              player colors and a message associated with the color selection process
     */
    @Override
    public void welcomeScene(AvailableColorsEvent event){
        Platform.runLater(() -> {
            this.loginScene = new LoginScene(viewFlow);
            stage.setScene(this.loginScene.createScene(false, null,null));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
        });
    }

    /**
     * Handles the event indicating that the selected nickname is already in use by another player
     * @param event an instance of containing details about the error,
     *              including the username that is already taken and available colors for selection
     */
    @Override
    public void nickNameAlreadyTaken(NicknameAlreadyInError event){
        Platform.runLater(() -> this.loginScene.showNicknameTakenPopup());
    }

    /**
     * Handles the event when a player attempts to select a color that is already in use
     * @param event an instance of containing the details of the color selection error,
     *              including the color that is already taken.
     */
    @Override
    public void colorAlreadyTaken(ColorAlreadyInError event){
        Platform.runLater(() -> {
            loginScene.showColorTakenPopup();
            loginScene.markColorAsUnavailable(event.getColor());
        });
    }

    /**
     * Displays a notification indicating that a player has joined the game.
     * @param nickname the nickname of the player who has joined
     */
    @Override
    public void showPlayerJoined(String nickname) {
        this.loginScene.showPlayerJoined(nickname);
    }

    /**
     * Starts the building scene in the graphical user interface, setting up and displaying
     * the related interface for players to construct their spaceships.
     * @param event an instance of containing details about the current game state,
     *              including the list of players, game level, and initial setup for the building phase
     */
    @Override
    public void startBuildingScene(StartBuildingEvent event){
        Platform.runLater(() -> {
            this.loginScene.hideWaitForOthers();

            buildingScene=new BuildingScene(viewFlow.getShip().getComponents(),viewFlow, new StackPane());
            stage.setScene(this.buildingScene.createScene(event.getPlayers(), viewFlow.getLevel()));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
        });
    }

    /**
     * Resets the component selection box on the building scene in the graphical user interface.
     * @param component the component that the user has chosen to discard
     */
    @Override
    public void showDiscardComponent(Component component) {
        Platform.runLater(() -> this.buildingScene.resetComponentSelectedBox());
    }

    /**
     * Handles the event where a component has been successfully linked to a specific
     * position on the game board. Updates the building scene grid with the linked component.
     * @param event the containing the details of the linked component, including its
     *              position (row and column) and the component itself
     */
    @Override
    public void componentLinked(ComponentLinkedEvent event){
        Platform.runLater(() -> this.buildingScene.insertComponentInGrid(event.getComponent(),event.getX(),event.getY()));
    }

    /**
     * Displays the specified card stack within the graphical user interface
     * @param stack the object representing the stack of cards to be displayed
     */
    @Override
    public void showStack(Stack stack) {
        Platform.runLater(() -> this.buildingScene.showStack(stack));
    }

    /**
     * Handles the event indicating that the selected component is not available for selection
     * @param event an instance of containing details about the unavailability of the selected component
     */
    @Override
    public void componentNotAvailable(ComponentNotAvailableError event){
        Platform.runLater(()-> {
            this.buildingScene.refreshComponentsViewIfVisible();
            this.buildingScene.showComponentUnavailablePopup();
        });
    }

    /**
     * Handles the event triggered when a player selects a component.
     * @param event an instance of containing details about the selected component,
     *              including its index and the nickname of the player who selected it
     */
    @Override
    public void componentSelected(ComponentSelectedEvent event){
        Platform.runLater(() -> {
            ImageView component = new ImageView(this.viewFlow.getSelectedComponent().getFrontImage());
            this.buildingScene.insertSelectedComponent(component, false);
        });
    }

    /**
     * Handles the event triggered when a player provides invalid coordinates during the game.
     * @param event an instance of containing details about the event, including the username
     *              of the player who provided invalid coordinates and an error message.
     */
    @Override
    public void invalidCoordinates(InvalidCoordinatesError event) {
        Platform.runLater(() -> this.buildingScene.showPopUpError(event.getMessage()));
    }

    /**
     * Handles the event indicating that the hourglass cannot be turned at the current moment in the game
     * @param event an instance of containing the error message
     */
    @Override
    public void turnHourGlassError(TurnHourglassError event) {
        Platform.runLater(() -> this.buildingScene.showPopUpError(event.getMessage()));
    }

    /**
     * Turns the hourglass timer in the building scene for a specified duration
     * @param seconds the number of seconds for the hourglass timer to run
     */
    @Override
    public void turnHourGlass(int seconds){
        Platform.runLater(() -> this.buildingScene.startHourglassTimer());
    }

    /**
     * Displays a notification indicating that the time has expired in the building scene
     * @param expirationCount the number of times the time expired has occurred
     */
    @Override
    public void showTimeExpired(int expirationCount) {
        Platform.runLater(() -> this.buildingScene.showTimeExpired(expirationCount));
    }

    /**
     * Handles the event triggered when a component is kept by a player.
     * @param event an instance of containing details about the kept component
     */
    @Override
    public void keptComponent(KeptComponentEvent event){
        Platform.runLater(() -> this.buildingScene.insertComponentToKepts(event.getKept()));
    }

    /**
     * Handles the event triggered when a component has been successfully rotated.
     * @param event an instance of containing details about the component that has been rotated
     */
    @Override
    public void componentTurned(TurnedComponentSuccessEvent event){
        Platform.runLater(() -> {
            ImageView component = new ImageView(this.viewFlow.getSelectedComponent().getFrontImage());
            this.buildingScene.insertSelectedComponent(component, true);
        });
    }

    /**
     * Handles the event triggered when a component kept by the player is selected.
     * @param event an instance of containing details about the selected kept component, including its index
     */
    @Override
    public void keptSelected(SuccessSelectKept event){
        Platform.runLater(() -> this.buildingScene.selectKeptComponent(event.getIndex()));
    }

    /**
     * Refreshes the view of available components in the building scene if the component view is currently visible
     */
    @Override
    public void refreshAvailableComponents(){
        Platform.runLater(() -> this.buildingScene.refreshComponentsViewIfVisible());
    }

    /**
     * Refreshes the view of available components in the building scene if the component view is currently visible.
     * @param components a list of objects representing the components that are currently available
     *                   for selection or interaction in the building scene
     */
    @Override
    public void showAvailableComponent(ArrayList<Component> components) {
        Platform.runLater(() -> this.buildingScene.refreshComponentsViewIfVisible());
    }

    /**
     * Displays the components that have been kept by the player.
     * @param kept a list of objects that represent the components
     *             currently kept by the player during the game
     */
    @Override
    public void showKeptComponents(ArrayList<Component> kept) {
        // nella gui non deve fare nulla forse c'è un modo migliore di unificare ma per ora tengo cosi
    }

    /**
     * Handles the event triggered when the stack drop phase is complete.
     * @param event an instance of containing details of the drop stack event.
     */
    @Override
    public void dropStack(StackDropOk event){
        Platform.runLater(() -> this.buildingScene.hideStackOverlay());
    }

    /**
     * Handles the completion of the building process.
     * @param event the event indicating that the building phase is complete
     */
    @Override
    public void buildingComplete(BuildCompleteEvent event){
        Platform.runLater(() -> {
            this.buildingScene.stopHourglassTimer();
            this.buildingScene.hideWaitForOthers();
            this.buildingScene.buildShipGrid(false, viewFlow.getShip().getComponents());
            for(String s: event.getNickNamesOfIllegalShips()){
                if(viewFlow.getNickname().equals(s)){
                    this.viewFlow.setViewState(ViewFlow.ViewState.REMOVE);
                    this.showRemove();
                }
            }
            if(viewFlow.getViewState() != ViewFlow.ViewState.REMOVE){
                if(viewFlow.getLevel()==2){
                    viewFlow.handleInput("alien");

                }
                else{
                    viewFlow.handleInput("continue");
                }
            }
        });
    }

    /**
     * shows client's view to let him remove a component from the ship
     */
    @Override
    public void showRemove(){
       Platform.runLater(() -> {
           this.buildingScene.showRemove();
       });
    }

    /**
     * Handles the AlienCabinCoord event and updates the building scene with the new alien cabin coordinates.
     * @param event the event containing the alien cabin coordinates to be processed
     */
    @Override
    public void askAlienCabin(AlienCabinCoord event){
        Platform.runLater(() -> {
            this.buildingScene.updateAlienCabinCoord(event.getAlienCabinCoord());
            this.buildingScene.showAlienCabin();
        });
    }

    /**
     * Transitions the application to the flight scene based on the provided FlightStartEvent.
     * @param event the FlightStartEvent containing information about the players and game state
     */
    @Override
    public void flightScene(FlightStartEvent event) {
        Platform.runLater(() -> {
            this.buildingScene.hideWaitForOthers();

            this.flightScene = new FlightScene(viewFlow.getShip().getComponents(), viewFlow, new StackPane());
            stage.setScene(this.flightScene.createScene(event.getPlayers(), viewFlow.getLevel()));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();

            this.flightScene.showStateInput();

        });
    }

    /**
     * Displays the current condition in the flight scene, updating the state based
     * on the provided condition and leader status
     * @param condition the current condition to be displayed
     * @param isLeader a boolean indicating whether the user is the leader
     */
    @Override
    public void showCurrCondition(Condition condition, boolean isLeader) {
        Platform.runLater(() -> {
            this.flightScene.clearRollDiceResult();
            this.flightScene.showCurrCondition(condition, isLeader);
        });
    }

    /**
     * Handles the card-finished event and updates the flight scene
     * with new ship components, status bar information, and track details.
     * @param event the CardFinished event containing information about the
     *              card completion that triggers the updates in the flight scene
     */
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

    /**
     * Displays the track information for a specified level and updates the UI accordingly.
     * @param truckers a map where the keys are trucker identifiers (String) and the values are their associated data (Integer)
     * @param level the level for which the track information should be displayed
     */
    @Override
    public void showTrack(Map<String, Integer> truckers, int level){
        Platform.runLater(() -> this.flightScene.updateTrack(truckers));
    }

    /**
     * Displays the card drawn during the StartCardEvent and updates the UI accordingly.
     * @param event the StartCardEvent containing the details of the drawn card and conditions' order
     */
    @Override
    public void showCardDrawn(StartCardEvent event) {
        Platform.runLater(() -> {
            this.flightScene.hideWaitForOthers();
            this.flightScene.updateCardInScene(event.getCard());
            if(event.getPlanetsOccupied()!= null){
                this.flightScene.updatePlanetsOccupied(event.getPlanetsOccupied());
            }
            if(event.getConditionsOrder() != null){
                this.showWarzone = false;
                this.flightScene.showCurrCondition(event.getConditionsOrder().getFirst(), true);

            }
        });
    }

    /**
     * Triggers an epidemic event by running the necessary operations on the platform's UI thread
     * @param event the EpidemicEvent that contains information about the epidemic to be handled
     */
    @Override
    public  void epidemic(EpidemicEvent event){
        Platform.runLater(() -> this.showTrack(viewFlow.getTrack(), viewFlow.getLevel()));
    }

    /**
     * Displays the Stardust card in the flight scene based on the provided event
     * @param event the StarDustEvent containing the card to be displayed
     */
    @Override
    public void showStardustCard(StarDustEvent event) {
        Platform.runLater(() -> {
            this.flightScene.hideWaitForOthers();
            this.flightScene.updateCardInScene(event.getCard());
        });
    }

    /**
     * Updates the current good in the flight scene.
     * @param good the Goods object to be updated in the flight scene
     */
    @Override
    public void updateCurrGood(Goods good) {
        Platform.runLater(() -> this.flightScene.updateCurrGood(good));
    }

    /**
     * Updates the goods list to be removed
     * @param goodsList A list of goods to be removed.
     */
    @Override
    public void updateGoodsListToRemove(ArrayList<Goods> goodsList) {
        Platform.runLater(() -> {
            this.flightScene.updateGoodsListToRemove(goodsList);
        });
    }

    /**
     * Displays an epidemic card in the flight scene.
     * @param event the EpidemicEvent containing the card to be displayed
     */
    @Override
    public void showEpidemicCard(EpidemicEvent event) {
        Platform.runLater(() -> {
            this.flightScene.hideWaitForOthers();
            this.flightScene.updateCardInScene(event.getCard());
        });
    }

    /**
     * Handles the StarDust event by updating the flight scene and displaying the
     * appropriate track and level information on the platform's UI thread
     * @param event the StarDustEvent that contains the details of the event
     */
    @Override
    public void stardust(StarDustEvent event){
        Platform.runLater(() -> {
            this.flightScene.setShip(viewFlow.getShip().getComponents());
            this.flightScene.updateShipLayout();
            this.flightScene.updateStatusBar();
            this.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        });
    }

    /**
     * Displays input fields on the user interface based on the current parser state.
     * @param parserState the current state of the parser determining which input format to show.
     */
    @Override
    public void showInputFormat(ParserState parserState) {
        Platform.runLater(() -> {

            this.flightScene.hideWaitForOthers();

            switch (parserState){
                case ACTIVATE_ENGINES ->  {
                    if(viewFlow.shipHasDoubleEngines()) {
                        this.flightScene.showActivateEngineInput();
                    }
                }
                case ACTIVATE_CANNONS -> {
                    if(viewFlow.shipHasDoubleCannons()) {
                        this.flightScene.showActivateCannonsInput();
                    }
                }
                case ACTIVATE_SHIELD -> {
                    if(viewFlow.shipHasShields()){
                        this.flightScene.showActivateShieldInput();
                    }
                }
                case LAND -> this.flightScene.showLandInput();
                case HUMAN_COORD -> this.flightScene.showHumanCoordInput();
                case PLOT_GOOD -> {
                    if(viewFlow.shipHasLoad()){
                        this.flightScene.showPlotGoodInput();
                    }
                }
                case ROLL_DICE -> this.flightScene.showRollDiceInput();
                case PLANET_DECISION-> this.flightScene.showPlanetDecisionInput();
                case DRAW_CARD -> this.flightScene.showDrawCardInput();
                case REMOTION_CONFIRMED -> this.flightScene.showRemotionConfirmedInput();
                case REMOVE_BATTERIES -> {
                    if(viewFlow.getShip().getNumBatteries()>0){
                        this.flightScene.showRemoveBatteriesInput();
                    }
                }
                case REMOVE_GOODS -> this.flightScene.showRemoveGoodsInput();
                case REARRANGE -> {
                    if(viewFlow.shipHasGoods()) {
                        this.flightScene.showRearrangeInput();
                    }
                }
                case COSMIC_CREDITS -> this.flightScene.showCosmicCreditsInput();
                case WARZONE -> {
                    if(this.showWarzone){
                        this.flightScene.showWarzoneInput();
                    }
                    this.showWarzone = true;
                }
                case ASK_WITHDRAW -> this.flightScene.showAskWithdraw();
                case DISPLAY_BIG_SHOOT -> this.flightScene.showBigShot();

            }
        });
    }

    /**
     * Displays the result of a dice roll on the flight scene interface.
     * @param event the RollDiceResult event containing the result of the dice roll
     */
    @Override
    public void showResult(RollDiceResult event){
        System.out.println("Chiamo roll dice result");
        Platform.runLater(() -> {
            this.flightScene.clearRollDiceResult();
            this.flightScene.showRollDiceResult(event.getDiceResult());
        });
    }

    /**
     * Displays a notification or message indicating that the user or player has died
     */
    @Override
    public void showDead(){
        this.flightScene.showError("You died");
    }

    /**
     * Displays the "Wait For Others" screen based on the current view state of the application
     */
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

    /**
     * Displays a message on the current scene depending on the current view state.
     * @param message the message to be displayed
     */
    @Override
    public void showMessage(String message) {
        if(viewFlow.getViewState() == ViewFlow.ViewState.BULDING){
            this.buildingScene.showMessage(message);
        }else if(viewFlow.getViewState() == ViewFlow.ViewState.FLIGTH){
            this.flightScene.showMessage(message);
        }
    }

    /**
     * Displays the layout of the other ship on the appropriate scene based on the current view state
     * @param nickname the nickname of the other ship's owner
     * @param ship of the player
     */
    @Override
    public void showOtherShipLayout(String nickname, SpaceShip ship) {
        if (viewFlow.getViewState() == ViewFlow.ViewState.BULDING) {
            Platform.runLater(() -> this.buildingScene.showOtherShip(ship.getComponents(), nickname));
        } else if (viewFlow.getViewState() == ViewFlow.ViewState.FLIGTH) {
            Platform.runLater(() -> this.flightScene.showOtherShip(ship, nickname));
        }
    }

    /**
     * Displays build options to the user
     */
    @Override
    public void showBuildOptions() {
        // Nella gui non deve fare nulla serve per la tui sarebbe da unificare meglio
    }

    /**
     * Displays the ship's layout in the GUI
     * @param nickname the nickname of the user currently interacting with the ship view
     * @param layout a 2D array representing the components of the ship's layout
     */
    @Override
    public void showShipLayout(String nickname, Component[][] layout){
        Platform.runLater(() -> {
           if(viewFlow.getViewState() == ViewFlow.ViewState.FLIGTH){
               this.flightScene.setShip(viewFlow.getShip().getComponents());
               this.flightScene.updateShipLayout();
           }else if (viewFlow.getViewState() == ViewFlow.ViewState.REMOVE || viewFlow.getViewState() == ViewFlow.ViewState.FILL){
               this.buildingScene.buildShipGrid(false, layout);
           }
        });
    }

    /**
     * Displays the number of cards left to draw in the GUI
     */
    @Override
    public void showNumCardLeft() {
        flightScene.showRemainingCards(viewFlow.getCardsToGo());
    }

    /**
     * Displays a message indicating that a trucker has withdrawn.
     * @param nickname the nickname of the trucker who has withdrawn
     */
    @Override
    public void showTruckerWithdrawn(String nickname) {
        Platform.runLater(() -> this.flightScene.showMessage(nickname + " has withdrawn"));
    }

    /**
     * Updates the planets occupied based on the provided PlanetDecision event
     * @param planetsOccupied information about the planets currently occupied
     */
    @Override
    public void updatePlanetsOccupied(ArrayList<Trucker> planetsOccupied) {
        Platform.runLater(() -> this.flightScene.updatePlanetsOccupied(planetsOccupied));
    }

    /**
     * Displays the game-ended state by invoking the appropriate UI update on the JavaFX application thread.
     */
    @Override
    public void showGameEnded() {
        Platform.runLater(() -> this.resultScene.showEndMessage());
    }

    /**
     * Displays the final scores of the game and transitions the scene
     * to the result screen.
     * @param finalScores a map containing the players' names as keys and their respective scores as values
     */
    @Override
    public void showScores(Map<String, Integer> finalScores) {
        Platform.runLater(() -> {

            resultScene=new ResultScene(new StackPane());
            stage.setScene(this.resultScene.createScene(finalScores));
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
        });
    }

    /**
     * Displays an error message on the current scene based on the application's view state.
     * @param message the error message to be displayed
     */
    @Override
    public void showError(String message) {
        if(viewFlow.getViewState() == ViewFlow.ViewState.START){
            this.loginScene.showError(message);
        }
        if(viewFlow.getViewState() == ViewFlow.ViewState.BULDING){
            this.buildingScene.showError(message);
        }else if(viewFlow.getViewState() == ViewFlow.ViewState.FLIGTH){
            this.flightScene.showError(message);
        }
    }

    /**
     * Updates and displays the player's ship status on the status bar
     */
    @Override
    public void showPlayerShipStatus() {
        Platform.runLater(() -> this.flightScene.updateStatusBar());
    }
}
