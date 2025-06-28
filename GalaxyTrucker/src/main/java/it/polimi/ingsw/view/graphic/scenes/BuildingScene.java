package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.view.ViewFlow;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a configurable scene for managing the game interface
 * where players can build and manage components of a ship in a grid-based layout
 */
public class BuildingScene {

    /**
     * Manages the transitions and flow between different views
     */
    private final ViewFlow viewFlow;

    /**
     * Represents the Scene object used to define the visual
     * composition and layout of the BuildingScene user interface
     */
    private Scene scene;

    /**
     * Root graphical container for the class. The StackPane serves as the base element
     * for managing and organizing the layout of the scene's user interface components
     */
    private final StackPane root;

    /**
     * Level of the game
     */
    private int level;

    /**
     * A 2D array representing the layout of the spaceship or grid in the building scene
     */
    private final Component[][] layout;

    /**
     * A 2D boolean array representing the validity of cells within the grid
     */
    private final boolean[][] validCells;

    /**
     * A 2D matrix of StackPane used to represent the cells of a grid
     */
    private final StackPane[][] cellMatrix;

    /**
     * Represents a graphical grid structure of the ship layout.
     */
    private GridPane grid;

    /**
     * A horizontal container that holds the slots for components
     * that have been kept during the building process
     */
    private HBox keptBox;

    /**
     * Represents the level of fill or occupancy maintained in a specific context
     */
    private int keptFillLevel;

    /**
     * Represents the Finish button that allows the user to finalize their actions
     * and proceed to the next step in the application's workflow
     */
    private Button finishBtn;

    /**
     * Represents the rules button that allows the user to finalize their actions
     * and proceed to the next step in the application's workflow
     */
    private Button stackBtn;

    /**
     * Represents the rule button that allows the user to open the PDF rules
     */
    private Button rulesBtn;

    /**
     * Represents the show other ship button that allows the user to see other players' spaceship
     */
    private Button otherBtn;

    /**
     * Represents the hourglass button that allows the user to turn the hourglass
     */
    private Button hourglassBtn;

    /**
     * Represents the select new component button that allows the user to choose a component
     * from the list of available components
     */
    private Button selectBtn;

    /**
     * Represents the select kept component button that allows the user to choose a component
     * from the list of kept components
     */
    private Button selectKeptBtn;

    /**
     * Represents the turn button that allows the user to graphically turn the component
     */
    private Button turnBtn;

    /**
     * Represents the keep button that allows the user to move the component to the kept list
     */
    private Button keepBtn;

    /**
     * Represents the discard button that allows the user to discard the component and put it
     * back in the available components list
     */
    private Button discardBtn;

    /**
     * Represents the link button that allows the user to attach the component to the spaceship grid
     */
    private Button linkBtn;

    /**
     * Represents the link button that allows the user to remove the invalid component from the spaceship
     */
    private Button removeBtn;

    /**
     * Represents the list of graphical content elements displayed in the ship screen
     */
    private List<Node> shipScreenContent;

    /**
     * A boolean flag indicating whether the ship view is currently active or displayed
     */
    private boolean inShipView;

    /**
     * Represents whether the component view is currently visible in the building scene
     */
    private boolean isComponentViewVisible;

    /**
     * A map that keeps track of the previous states of buttons
     */
    private final Map<Button, Boolean> previousButtonStates;

    /**
     * A VBox instance representing the container for display of components
     */
    private VBox componentSelectedBox;

    /**
     * A VBox layout container that contains button to handle the selected component
     */
    private VBox componentBox;

    /**
     * A VBox instance representing the container for remotion of a component
     */
    private VBox removeBox;

    /**
     * A Vbox that contains options buttons
     */
    private VBox optionsBox;

    /**
     * A Vbox that contains link coordinates request popup
     */
    private VBox popupLink;

    /**
     * Indicates whether a component has been selected to determine the current state of
     * component selection, such as enabling or disabling certain actions
     */
    private boolean componentSelectedExists;

    /**
     * Represents an overlay displayed on the screen while waiting for
     * an important action to complete or for other players in the game to
     * perform their actions
     */
    private StackPane waitOverlay;

    /**
     * Represents a timeline animation used for displaying a "waiting" overlay
     */
    private Timeline waitAnimation;

    /**
     * Represents the current overlay displayed on the stack pane
     */
    private StackPane currentStackOverlay;

    /**
     * List of coordinates alien cabins
     */
    private ArrayList<int[]> alienCoords;

    /**
     * True if the hourglass Timer has started
     */
    private boolean isHourglassTimerStarted;

    /**
     * Event constructor
     * @param layout a 2D array representing the ship grid
     * @param viewFlow an instance of viewFlow used for managing scene transitions
     * @param root the root stackPane serving as the parent container for the scene elements
     */
    public BuildingScene(Component[][] layout, ViewFlow viewFlow, StackPane root) {
        this.layout = layout;
        this.cellMatrix = new StackPane[10][11];
        this.viewFlow = viewFlow;
        this.componentSelectedExists = false;
        this.root = root;
        this.inShipView = true;
        this.isComponentViewVisible = false;
        this.keptFillLevel = 0;
        this.previousButtonStates = new HashMap<>();
        this.validCells = new boolean[11][12];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Component c = layout[i][j];
                validCells[i][j] = c == null || !c.getType().equals("Empty Space");
            }
        }

        this.isHourglassTimerStarted = false;
    }

    /**
     * Creates and returns a new scene configured for the specified gameplay state
     * @param playersNames a list of player names to be displayed or used in the scene
     * @param level of the game
     * @return a Scene object configured with the appropriate layout, controls, and style for the specified level and players
     */
    public Scene createScene(ArrayList<String> playersNames, int level) {
        double width = 1600;
        double height = 900;

        this.level = level;
        Image backgroundImage;
        if(level == 0){
            backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/cardboard-1.jpeg")).toExternalForm());
        } else {
            backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/cardboard-1b.jpeg")).toExternalForm());
        }
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(true);
        backgroundView.setMouseTransparent(true);

        buildShipGrid(true, null);
        buildKeptBox();

        root.getChildren().setAll(backgroundView, grid, keptBox);
        scene = new Scene(root, width, height);
        Consumer<Button> styleButton = createDefaultButtonStyler();

        addFinishButtonToScene(viewFlow, styleButton);
        addStackButtonToScene(viewFlow, styleButton);
        addRulesButtonToScene(styleButton);
        addShowOtherButtonToScene(viewFlow, styleButton, playersNames);
        if(level == 2){
            addHourglassButtonToScene(viewFlow);
        }
        addSelectComponentButton(styleButton);
        addSelectKeptButton(viewFlow,styleButton);
        attachOptionsBoxToScene(scene, level);

        addComponentSelectedSlot();
        createComponentSelectedButtons(styleButton);
        attackSelectComponentButtons(scene);

        return scene;
    }

    /**
     * Builds and displays a ship grid on the scene
     * @param buildNew a boolean indicating whether to construct a new ship grid (true) or use
     *                 an existing ship grid layout (false)
     * @param ship     a 2D array representing the ship layout
     */
    public void buildShipGrid(boolean buildNew, Component[][] ship) {

        Image trasparent = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/transparent.png")).toExternalForm());

        if (grid != null) {
            root.getChildren().remove(grid);
        }

        grid = new GridPane();
        grid.setHgap(3.5);
        grid.setVgap(3.5);
        grid.setAlignment(Pos.CENTER);
        grid.setTranslateX(1);
        grid.setTranslateY(-9);

        for (int row = 5; row < 10; row++) {
            for (int col = 4; col < 11; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(134.5, 134.5);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 6px; -fx-background-radius: 6px;");

                    Component c = buildNew ? layout[row][col] : ship[row][col];
                    if (c != null) {
                        ImageView imageView = new ImageView(c.getFrontImage());
                        imageView.setFitWidth(134.5);
                        imageView.setFitHeight(134.5);
                        Rectangle clip = new Rectangle(134.5, 134.5);
                        clip.setArcWidth(12);
                        clip.setArcHeight(12);
                        imageView.setClip(clip);
                        imageView.setRotate(c.getRotationAngle());
                        cell.getChildren().add(imageView);
                    } else {
                        ImageView imageView = new ImageView(trasparent);
                        imageView.setFitWidth(134.5);
                        imageView.setFitHeight(134.5);
                        imageView.setMouseTransparent(false);
                        Rectangle clip = new Rectangle(134.5, 134.5);
                        clip.setArcWidth(12);
                        clip.setArcHeight(12);
                        imageView.setClip(clip);
                        cell.getChildren().add(imageView);
                    }
                } else {
                    cell.setStyle("-fx-background-color: transparent;");
                }

                grid.add(cell, col - 4, row - 5);
                cellMatrix[row][col] = cell;
            }
        }

        root.getChildren().add(grid);
    }

    /**
     * Disables or enables the select buttons and adjusts their appearance based on the provided parameter
     * @param disable a boolean indicating whether to disable (true) or enable (false) the select buttons
     */
    private void disableSelectButtons(boolean disable) {
        double opacity = disable ? 0.5 : 1.0;

        discardBtn.setDisable(disable);
        discardBtn.setOpacity(opacity);

        linkBtn.setDisable(disable);
        linkBtn.setOpacity(opacity);

        turnBtn.setDisable(disable);
        turnBtn.setOpacity(opacity);

        keepBtn.setDisable(disable);
        keepBtn.setOpacity(opacity);
    }

    /**
     * @return the current Scene object
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Builds or rebuilds the kept components box and displays it on the scene.
     */
    public void buildKeptBox() {
        if (keptBox != null) {
            root.getChildren().remove(keptBox);
        }

        keptFillLevel = viewFlow.getKeptComponent().size();

        if (keepBtn != null && keptFillLevel == 2) {
            keepBtn.setDisable(true);
            keepBtn.setOpacity(0.5);
        }

        if (selectKeptBtn != null && keptFillLevel > 0) {
            selectKeptBtn.setDisable(false);
            selectKeptBtn.setOpacity(1);
        }

        keptBox = new HBox(15);
        keptBox.setMaxWidth(200);
        keptBox.setAlignment(Pos.TOP_RIGHT);

        ArrayList<Component> keptComponents = viewFlow.getKeptComponent();



        for (int i = 0; i < 2; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(110, 110);
            slot.setStyle("-fx-background-color: transparent;");

            if (i < keptComponents.size() && keptComponents.get(i) != null) {
                Component component = keptComponents.get(i);
                ImageView componentView = new ImageView(component.getFrontImage());
                componentView.setFitWidth(110);
                componentView.setPreserveRatio(true);
                componentView.setMouseTransparent(true);
                componentView.setRotate(component.getRotationAngle());

                Rectangle clip = new Rectangle(110, 110);
                clip.setArcWidth(12);
                clip.setArcHeight(12);
                componentView.setClip(clip);

                slot.getChildren().add(componentView);
            }

            Rectangle border = new Rectangle(110, 110);
            border.setArcWidth(6);
            border.setArcHeight(6);
            border.setFill(Color.TRANSPARENT);
            border.setStroke(Color.WHITE);
            border.setStrokeWidth(1);
            slot.getChildren().add(border);

            keptBox.getChildren().add(slot);
        }

        StackPane.setAlignment(keptBox, Pos.TOP_RIGHT);
        StackPane.setMargin(keptBox, new Insets(0, 235, 590, 0));
        root.getChildren().add(keptBox);
    }

    /**
     * Selects a kept component from the specified index and inserts it into the selected component slot
     * @param index of the component selected from the kept component list
     */
    public void selectKeptComponent(int index) {
        if (index < 0 || index >= keptFillLevel) {
            return;
        }

        keptFillLevel--;

        this.insertSelectedComponent(viewFlow.getSelectedComponent());

        this.disableSelectButtons(false);
        this.discardBtn.setOpacity(0.5);
        this.discardBtn.setDisable(true);

        if(keptFillLevel== 0){
            this.selectKeptBtn.setDisable(true);
            this.selectKeptBtn.setOpacity(0.5);
        }

        buildKeptBox();

    }

    /**
     * Inserts a selected component into the UI and adjusts its appearance based on the provided parameters
     * @param component the selected component to be inserted
     */
    public void insertSelectedComponent(Component component) {
        if (!inShipView && shipScreenContent != null) {
            isComponentViewVisible = false;
            root.getChildren().setAll(shipScreenContent);
            inShipView = true;
        }

        this.componentSelectedExists = true;
        this.hourglassBtn.setDisable(true);
        this.hourglassBtn.setOpacity(0.5);

        disableSelectButtons(false);
        if(keptFillLevel==2){
            this.keepBtn.setDisable(true);
            this.keepBtn.setOpacity(0.5);
        }

        ImageView componentView = new ImageView(component.getFrontImage());
        componentView.setFitWidth(176);
        componentView.setPreserveRatio(true);
        componentView.setMouseTransparent(true);
        componentView.setRotate(component.getRotationAngle());

        Rectangle clip = new Rectangle(176, 176);
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        componentView.setClip(clip);


        StackPane imageContainer = (StackPane) componentSelectedBox.getChildren().getFirst();
        imageContainer.getChildren().add(componentView);

    }

    /**
     * Inserts a specified component into the grid at a given row and column
     * @param component the Component to be inserted into the grid
     * @param row the row index of the grid where the component should be placed
     * @param col the column index of the grid where the component should be placed
     */
    public void insertComponentInGrid(Component component, int row, int col) {

        this.componentSelectedExists = false;
        this.hourglassBtn.setDisable(false);
        this.hourglassBtn.setOpacity(1);

        if (component == null || !validCells[row][col]) return;
        resetComponentSelectedBox();

        StackPane cell = cellMatrix[row][col];
        cell.getChildren().clear();

        ImageView imageView = new ImageView(component.getFrontImage());
        imageView.setFitWidth(136);
        imageView.setFitHeight(136);
        imageView.setRotate(component.getRotationAngle());

        Rectangle clip = new Rectangle(136, 136);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        imageView.setClip(clip);

        cell.getChildren().add(imageView);

        layout[row][col] = component;
    }

    /**
     * Resets the component selection box to its default state by clearing its children,
     * disabling relevant selection buttons, and adding a placeholder image container
     */
    public void resetComponentSelectedBox() {

        componentSelectedBox.getChildren().clear();
        this.componentSelectedExists = false;
        this.hourglassBtn.setDisable(false);
        this.hourglassBtn.setOpacity(1);

        this.disableSelectButtons(true);

        Rectangle border = new Rectangle(178, 178);
        border.setArcWidth(16);
        border.setArcHeight(16);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(2);
        border.setMouseTransparent(true);

        StackPane imageContainer = new StackPane(border);
        imageContainer.setPrefSize(178, 178);
        imageContainer.setAlignment(Pos.CENTER);

        componentSelectedBox.getChildren().add(imageContainer);
    }

    /**
     * Displays a popup error message over the scene
     * @param message the error message to be displayed in the popup.
     */
    public void showPopUpError(String message) {
        double width = root.getWidth();
        double height = root.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label messageLabel = new Label(message);
        messageLabel.setTextFill(Color.RED);
        messageLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 35));
        messageLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        StackPane popup = new StackPane(overlay, messageLabel);
        popup.setMouseTransparent(true);
        StackPane.setAlignment(messageLabel, Pos.CENTER);

        Platform.runLater(() -> {
            if (!root.getChildren().contains(popup)) {
                root.getChildren().add(popup);
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> root.getChildren().remove(popup));
                delay.play();
            }
        });
    }

    /**
     * Displays a popup message indicating that a component is unavailable because it has
     * been selected by another player
     */
    public void showComponentUnavailablePopup() {
        if (!isComponentViewVisible) return;

        double width = root.getWidth();
        double height = root.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.TRANSPARENT);
        overlay.setMouseTransparent(true);

        Label messageLabel = new Label("Component Not Available, another player has just selected it");
        messageLabel.setTextFill(Color.web("#FFDE69"));
        messageLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 30));
        messageLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");
        messageLabel.setWrapText(true);
        messageLabel.setAlignment(Pos.CENTER);

        StackPane popup = new StackPane(overlay, messageLabel);
        popup.setMouseTransparent(true);
        StackPane.setAlignment(messageLabel, Pos.BOTTOM_CENTER);
        StackPane.setMargin(messageLabel, new Insets(0, 0, 50, 0));

        Platform.runLater(() -> {
            if (!root.getChildren().contains(popup)) {
                root.getChildren().add(popup);
                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(e -> root.getChildren().remove(popup));
                delay.play();
            }
        });
    }

    /**
     * Refreshes the components view if it is currently visible
     */
    public void refreshComponentsViewIfVisible() {
        if (isComponentViewVisible) {
            loadComponentsView(viewFlow.getAvailableComponents());
        }
    }

    /**
     * Attaches an options' box to the specified scene with buttons configured based on the given game level
     * @param scene to which the options' box will be attached
     * @param level of the game
     */
    private void attachOptionsBoxToScene(Scene scene, int level) {
        this.optionsBox = new VBox(10);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.TOP_LEFT);

        if(level ==0){
            optionsBox.getChildren().addAll(selectBtn, selectKeptBtn, otherBtn, finishBtn, rulesBtn);
        }else{
            optionsBox.getChildren().addAll(selectBtn, selectKeptBtn, stackBtn, otherBtn, finishBtn, rulesBtn, hourglassBtn);
        }

        Platform.runLater(() -> {
            StackPane root = (StackPane) scene.getRoot();
            if (!root.getChildren().contains(optionsBox)) {
                StackPane.setAlignment(optionsBox, Pos.TOP_RIGHT);
                StackPane.setMargin(optionsBox, new Insets(20, 0, 0, 5));
                root.getChildren().add(optionsBox);
            }
            optionsBox.setPickOnBounds(false);
        });
    }

    /**
     * Creates and returns a default button styler
     * @return a Consumer<Button> that applies predefined styling to a given button
     */
    private Consumer<Button> createDefaultButtonStyler() {
        return (btn) -> {
            btn.setFont(Font.font("Bangers", 18));
            btn.setTextFill(Color.BLACK);
            btn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            btn.setCursor(Cursor.HAND);
            btn.setPrefWidth(180);
        };
    }

    /**
     * Adds a finish button to the scene, which allows the user to confirm the completion
     * of their spaceship
     * @param viewFlow an instance of ViewFlow used for managing scene transitions
     * @param styleButton a Consumer<Button> function to apply specific styling to the button
     */
    private void addFinishButtonToScene(ViewFlow viewFlow, Consumer<Button> styleButton) {
        Button finishBtn = new Button("Finish spaceship");
        styleButton.accept(finishBtn);

        finishBtn.setOnAction(e -> {
            StackPane root = (StackPane) scene.getRoot();

            Rectangle bg = new Rectangle();
            bg.widthProperty().bind(scene.widthProperty());
            bg.heightProperty().bind(scene.heightProperty());
            bg.setFill(Color.rgb(0, 0, 0, 0.4));

            VBox box = new VBox(20);
            box.setAlignment(Pos.CENTER);

            Label confirm = new Label("Confirm finish building?");
            confirm.setTextFill(Color.web("#FFDE69"));
            confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 32));

            Button yes = new Button("YES");
            yes.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
            yes.setTextFill(Color.WHITE);
            yes.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
            yes.setCursor(Cursor.HAND);
            yes.setPrefWidth(100);

            Button no = new Button("NO");
            no.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
            no.setTextFill(Color.WHITE);
            no.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
            no.setCursor(Cursor.HAND);
            no.setPrefWidth(100);

            StackPane confirmation = new StackPane(bg, box);

            yes.setOnAction(ev -> {
                viewFlow.handleInput("end");
                root.getChildren().remove(confirmation);
            });

            no.setOnAction(ev -> root.getChildren().remove(confirmation));

            HBox buttons = new HBox(20, yes, no);
            buttons.setAlignment(Pos.CENTER);

            box.getChildren().addAll(confirm, buttons);

            root.getChildren().add(confirmation);
        });

        this.finishBtn = finishBtn;
    }

    /**
     * Adds a show stack button to the scene, which allows the user to see the stacks of cards
     * @param viewFlow an instance of ViewFlow used for managing scene transitions
     * @param styleButton a Consumer<Button> function to apply specific styling to the button
     */
    private void addStackButtonToScene(ViewFlow viewFlow, Consumer<Button> styleButton) {
        Button stackBtn = new Button("Show stack");
        styleButton.accept(stackBtn);

        stackBtn.setOnAction(e -> {
            StackPane root = (StackPane) scene.getRoot();

            Rectangle bg = new Rectangle();
            bg.widthProperty().bind(scene.widthProperty());
            bg.heightProperty().bind(scene.heightProperty());
            bg.setFill(Color.rgb(0, 0, 0, 0.4));

            VBox stackChoices = new VBox(15);
            stackChoices.setAlignment(Pos.CENTER);

            for (int i = 0; i < 3; i++) {
                Button pile = new Button("Stack " + (i + 1));
                styleButton.accept(pile);
                pile.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
                final int index = i;
                pile.setOnAction(ev -> {
                    viewFlow.handleInput("stack " + (index+1));
                    root.getChildren().remove(root.lookup("#popupOverlay"));
                });
                stackChoices.getChildren().add(pile);
            }

            StackPane popup = new StackPane(bg, stackChoices);
            popup.setId("popupOverlay");
            popup.setOnMouseClicked(ev -> root.getChildren().remove(popup));

            root.getChildren().add(popup);
        });

        this.stackBtn = stackBtn;
    }

    /**
     * Adds a rule button to the scene, which allows the user to open PDF rules
     * @param styleButton a Consumer<Button> function to apply specific styling to the button
     */
    private void addRulesButtonToScene(Consumer<Button> styleButton) {
        Button rulesBtn = new Button("Rules");
        styleButton.accept(rulesBtn);

        rulesBtn.setOnAction(e -> {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("it/polimi/ingsw/view/galaxy-trucker-rules-en.pdf")) {
                if (is == null) {
                    System.out.println("File not found");
                    return;
                }

                File tempPdf = File.createTempFile("galaxy-trucker-rules", ".pdf");
                tempPdf.deleteOnExit();

                try (FileOutputStream os = new FileOutputStream(tempPdf)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }

                Desktop.getDesktop().open(tempPdf);

            } catch (IOException e1) {
                System.out.println("Error in file opening");
            }
        });

        this.rulesBtn = rulesBtn;
    }

    /**
     * Adds a show others spaceship button to the scene, which allows the user to see other truckers' spaceship
     * @param viewFlow an instance of ViewFlow used for managing scene transitions
     * @param styleButton a Consumer<Button> function to apply specific styling to the button
     * @param playersNames a list of players' nicknames
     */
    private void addShowOtherButtonToScene(ViewFlow viewFlow, Consumer<Button> styleButton, ArrayList<String> playersNames) {
        Button otherBtn = new Button("Show others ship");
        styleButton.accept(otherBtn);

        otherBtn.setOnAction(e -> {
            VBox nicknameChoices = new VBox(15);
            nicknameChoices.setAlignment(Pos.CENTER);

            for (String nickname : playersNames) {
                if(nickname.equals(viewFlow.getNickname())) continue;
                Button nickBtn = new Button(nickname);
                styleButton.accept(nickBtn);
                nickBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 18));

                nickBtn.setOnAction(ev -> {
                    viewFlow.handleInput("ship " + nickname);
                    ((StackPane) scene.getRoot()).getChildren().remove(nicknameChoices.getParent());
                });

                nicknameChoices.getChildren().add(nickBtn);
            }

            StackPane popup = new StackPane(
                    new Rectangle(1600, 900, Color.rgb(0, 0, 0, 0.4)),
                    nicknameChoices
            );
            popup.setOnMouseClicked(ev -> ((StackPane) scene.getRoot()).getChildren().remove(popup));
            ((StackPane) scene.getRoot()).getChildren().add(popup);
        });

        this.otherBtn = otherBtn;
    }

    /**
     * Adds an hourglass button to the scene, which allows the user to turn the hourglass
     * @param viewFlow an instance of ViewFlow used for managing scene transitions
     */
    private void addHourglassButtonToScene(ViewFlow viewFlow) {
        Button hourglassBtn = new Button("⏳");
        hourglassBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        hourglassBtn.setTextFill(Color.BLACK);
        hourglassBtn.setCursor(Cursor.HAND);
        hourglassBtn.setPrefWidth(70);
        hourglassBtn.setPrefHeight(30);
        hourglassBtn.setBackground(new Background(new BackgroundFill(
                Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

        hourglassBtn.setOnAction(e -> viewFlow.handleInput("hourglass"));

        this.hourglassBtn = hourglassBtn;
    }

    /**
     * Starts the hourglass countdown timer and visually updates the corresponding button to reflect
     * the timer state
     */
    public void startHourglassTimer() {
        if(isHourglassTimerStarted){
            return;
        }

        hourglassBtn.setDisable(true);
        isHourglassTimerStarted =true;

        ColorAdjust grayOverlay = new ColorAdjust();
        grayOverlay.setBrightness(-0.5);
        hourglassBtn.setEffect(grayOverlay);

        final int[] secondsLeft = {90};
        hourglassBtn.setText(secondsLeft[0] + "s");

        Timeline countdown = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            secondsLeft[0]--;
            if (secondsLeft[0] > 0) {
                hourglassBtn.setText(secondsLeft[0] + "s");
            } else {
                hourglassBtn.setText("⏳");
                hourglassBtn.setDisable(false);
                hourglassBtn.setEffect(null);
                isHourglassTimerStarted = false;
            }
        }));
        countdown.setCycleCount(90);
        countdown.play();
    }

    /**
     * Stops the hourglass timer and resets the related UI state
     */
    public void stopHourglassTimer() {
        hourglassBtn.setText("⏳");
        hourglassBtn.setDisable(false);
        isHourglassTimerStarted = false;
        hourglassBtn.setEffect(null);
    }

    /**
     * Adds a Select component button to the scene, which allows the user to select a new component
     * from the available components list
     * @param styleButton a Consumer<Button> function to apply specific styling to the button
     */
    private void addSelectComponentButton(Consumer<Button> styleButton) {
        Button selectComponentBtn = new Button("Select component");
        styleButton.accept(selectComponentBtn);

        selectComponentBtn.setOnAction(e -> {
            shipScreenContent = new ArrayList<>(root.getChildren());
            this.inShipView = false;
            loadComponentsView(viewFlow.getAvailableComponents());
        });

        this.selectBtn = selectComponentBtn;
    }

    /**
     * Adds a keep button to the scene, which allows the user to select a kept component
     * from the kept component list
     * @param viewFlow an instance of ViewFlow used for managing scene transitions
     * @param styleButton a Consumer<Button> function to apply specific styling to the button
     */
    private void addSelectKeptButton(ViewFlow viewFlow, Consumer<Button> styleButton) {
        this.selectKeptBtn = new Button("SelectKept");
        styleButton.accept(selectKeptBtn);
        selectKeptBtn.setOnAction(e -> {
            if (componentSelectedExists) {
                Rectangle fullOverlay = new Rectangle();
                fullOverlay.widthProperty().bind(root.widthProperty());
                fullOverlay.heightProperty().bind(root.heightProperty());
                fullOverlay.setFill(Color.rgb(0, 0, 0, 0.5));
                fullOverlay.setMouseTransparent(false);

                Label warningLabel = new Label("You have already selected a component!");
                warningLabel.setTextFill(Color.RED);
                warningLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 35));
                warningLabel.setMouseTransparent(true);
                warningLabel.setWrapText(true);
                warningLabel.setTextAlignment(TextAlignment.CENTER);

                StackPane overlayLayer = new StackPane(fullOverlay, warningLabel);
                overlayLayer.setAlignment(Pos.CENTER);
                overlayLayer.setPickOnBounds(true);

                root.getChildren().add(overlayLayer);

                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(event -> root.getChildren().remove(overlayLayer));
                delay.play();

                return;
            }
            AnchorPane overlay = new AnchorPane();
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
            overlay.setPickOnBounds(true);

            VBox popup = new VBox(12);
            popup.setAlignment(Pos.CENTER);
            popup.setPadding(new Insets(20));
            popup.setPrefSize(230, 200);

            Label messageLabel = new Label();
            messageLabel.setTextFill(Color.web("#FFDE69"));
            messageLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
            messageLabel.setWrapText(true);
            messageLabel.setTextAlignment(TextAlignment.CENTER);

            TextField indexField = new TextField();
            indexField.setPromptText("Index (0 or 1)");
            indexField.setPrefWidth(100);
            indexField.setVisible(false);
            indexField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("[01]?")) {
                    indexField.setText(oldVal);
                }
            });

            Label errorLabel = new Label("Invalid index!");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(false);

            Button cancelBtn = new Button("Undo");
            cancelBtn.setFont(Font.font("Bangers", 14));
            cancelBtn.setCursor(Cursor.HAND);
            cancelBtn.setTextFill(Color.WHITE);
            cancelBtn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(8), Insets.EMPTY)));

            Button sendBtn = new Button("Select");
            sendBtn.setFont(Font.font("Bangers", 14));
            sendBtn.setCursor(Cursor.HAND);
            sendBtn.setTextFill(Color.WHITE);
            sendBtn.setBackground(new Background(new BackgroundFill(Color.web("#00A676"), new CornerRadii(8), Insets.EMPTY)));

            HBox buttons = new HBox(15, cancelBtn, sendBtn);
            buttons.setAlignment(Pos.CENTER);

            popup.getChildren().addAll(messageLabel, indexField, errorLabel, buttons);

            AnchorPane.setTopAnchor(popup, (root.getHeight() - 250) / 2);
            AnchorPane.setRightAnchor(popup, 10.0);
            overlay.getChildren().add(popup);
            root.getChildren().add(overlay);

            Runnable removePopup = () -> root.getChildren().remove(overlay);
            cancelBtn.setOnAction(ev -> removePopup.run());

            int countKept = 0;
            List<Integer> availableIndices = new ArrayList<>();
            for (int i = 0; i < viewFlow.getKeptComponent().size(); i++) {
                if (viewFlow.getKeptComponent().get(i) != null) {
                    countKept++;
                    availableIndices.add(i);
                }
            }

            if (countKept == 0) {
                messageLabel.setText("No kept found!");
                sendBtn.setDisable(true);
            } else if (countKept == 1) {
                int onlyIndex = availableIndices.getFirst();
                messageLabel.setText("You have only 1 kept, it will be automatically selected");
                messageLabel.setWrapText(true);
                messageLabel.setPrefWidth(180);
                indexField.setVisible(false);
                sendBtn.setOnAction(ev -> {
                    viewFlow.handleInput("selectkept " + onlyIndex);
                    removePopup.run();
                });
            } else {
                messageLabel.setText("Select the kept component (0 or 1)");
                indexField.setVisible(true);
                sendBtn.setOnAction(ev -> {
                    String input = indexField.getText().trim();
                    if (!input.equals("0") && !input.equals("1")) {
                        errorLabel.setVisible(true);
                        return;
                    }
                    int idx = Integer.parseInt(input);
                    if (viewFlow.getKeptComponent().get(idx) == null) {
                        errorLabel.setText("No component in that slot");
                        errorLabel.setVisible(true);
                        return;
                    }
                    viewFlow.handleInput("selectkept " + idx);
                    removePopup.run();
                });
            }
        });

        selectKeptBtn.setDisable(true);

    }

    /**
     * Displays a view of multiple components in a grid with interactive buttons for each component
     * @param components the list of available components to be displayed in the view
     */
    public void loadComponentsView(ArrayList<Component> components) {
        this.isComponentViewVisible = true;
        double width = 1600;
        double height = 900;

        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg")).toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(true);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        int col = 0, row = 0;
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            final int index = i;

            ImageView imgView = new ImageView(component.getCovered() ? component.getBackImage() : component.getFrontImage());
            imgView.setFitWidth(90);
            imgView.setFitHeight(90);

            Rectangle clip = new Rectangle(90, 90);
            clip.setArcWidth(16);
            clip.setArcHeight(16);
            imgView.setClip(clip);

            Button button = new Button();
            button.setGraphic(imgView);
            button.setPrefSize(90, 90);
            button.setStyle("-fx-background-color: transparent; -fx-border-color: #cccccc; -fx-border-radius: 8px; -fx-cursor: hand; -fx-padding: 0px;");

            button.setOnAction(e -> {
                AnchorPane overlay = new AnchorPane();
                overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
                overlay.setPickOnBounds(true);

                VBox popup = new VBox(18);
                popup.setAlignment(Pos.CENTER);
                popup.setPadding(new Insets(15));
                popup.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

                Label confirmLabel = new Label("Select this component?");
                confirmLabel.setTextFill(Color.web("#FFDE69"));
                confirmLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 28));
                confirmLabel.setWrapText(true);
                confirmLabel.setTextAlignment(TextAlignment.CENTER);
                confirmLabel.setAlignment(Pos.CENTER);

                ImageView componentImage;
                if(component.getCovered()){
                    componentImage = new ImageView(component.getBackImage());
                } else {
                    componentImage = new ImageView(component.getFrontImage());
                }
                componentImage.setFitWidth(200);
                componentImage.setFitHeight(200);
                componentImage.setPreserveRatio(true);
                componentImage.setMouseTransparent(true);

                Rectangle border = new Rectangle(200, 200);
                border.setArcWidth(20);
                border.setArcHeight(20);
                border.setFill(Color.TRANSPARENT);
                border.setStroke(Color.WHITE);
                border.setStrokeWidth(1);
                border.setMouseTransparent(true);

                StackPane imageWithBorder = new StackPane(componentImage, border);
                imageWithBorder.setAlignment(Pos.CENTER);

                Button undoBtn = new Button("Undo");
                undoBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
                undoBtn.setCursor(Cursor.HAND);
                undoBtn.setTextFill(Color.WHITE);
                undoBtn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(8), Insets.EMPTY)));

                Button selectBtnComponentsView = new Button("Select");
                selectBtnComponentsView.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
                selectBtnComponentsView.setCursor(Cursor.HAND);
                selectBtnComponentsView.setTextFill(Color.WHITE);
                selectBtnComponentsView.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(8), Insets.EMPTY))); // verde

                HBox buttons = new HBox(20, undoBtn, selectBtnComponentsView);
                buttons.setAlignment(Pos.CENTER);

                popup.getChildren().addAll(confirmLabel, imageWithBorder, buttons);

                AnchorPane.setTopAnchor(popup, (root.getHeight() - 300) / 2);
                AnchorPane.setLeftAnchor(popup, (root.getWidth() - 250) / 2);

                overlay.getChildren().add(popup);
                root.getChildren().add(overlay);

                Runnable removePopup = () -> root.getChildren().remove(overlay);

                undoBtn.setOnAction(ev -> removePopup.run());

                selectBtnComponentsView.setOnAction(ev -> {
                    viewFlow.handleInput("select " + index);
                    removePopup.run();
                });
            });

            if (component.getSelected()) {
                button.setDisable(true);
                button.setOpacity(0.4);
            }

            grid.add(button, col, row);
            if (++col == 8) {
                col = 0;
                row++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(new StackPane(grid));
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Button backBtn = new Button("←");
        backBtn.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 30));
        backBtn.setTextFill(Color.BLACK);
        backBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        backBtn.setCursor(Cursor.HAND);
        backBtn.setPadding(new Insets(12, 18, 12, 18));

        backBtn.setOnAction(e -> {
            this.isComponentViewVisible = false;
            if (shipScreenContent != null) {
                this.inShipView = true;
                root.getChildren().setAll(shipScreenContent);
            }
        });

        StackPane componentView = new StackPane(backgroundView, scrollPane, backBtn);
        StackPane.setAlignment(backBtn, Pos.TOP_LEFT);
        StackPane.setMargin(backBtn, new Insets(35, 0, 0, 125));

        root.getChildren().setAll(componentView);
    }

    /**
     * Creates and adds a visual slot for the currently selected component to the scene
     */
    private void addComponentSelectedSlot() {
        double borderSize = 178;

        Rectangle border = new Rectangle(borderSize, borderSize);
        border.setArcWidth(16);
        border.setArcHeight(16);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(2);
        border.setMouseTransparent(true);

        StackPane imageContainer = new StackPane(border);
        imageContainer.setPrefSize(borderSize, borderSize);
        imageContainer.setAlignment(Pos.CENTER);

        componentSelectedBox = new VBox(10);
        componentSelectedBox.setPadding(new Insets(0, 0, -250, -1229));
        componentSelectedBox.setAlignment(Pos.CENTER);
        componentSelectedBox.getChildren().add(imageContainer);

        StackPane.setAlignment(componentSelectedBox, Pos.CENTER);
        StackPane.setMargin(componentSelectedBox, new Insets(0, 0, 0, 0));
        root.getChildren().add(componentSelectedBox);
    }

    /**
     * Rimuove l'immagine del componente selezionato da componentSelectedBox,
     * preservando il bordo, se non c'è componente selezionato in viewFlow.
     */
    public void clearSelectedComponentImageIfNone() {
        if (viewFlow.getSelectedComponent() == null && componentSelectedBox != null) {
            StackPane imageContainer = (StackPane) componentSelectedBox.getChildren().getFirst();

            while (imageContainer.getChildren().size() > 1) {
                imageContainer.getChildren().remove(1);
            }

            componentSelectedExists = false;
            disableSelectButtons(true);
            this.hourglassBtn.setDisable(true);
            this.hourglassBtn.setOpacity(0.5);
        }
    }

    /**
     * Creates and initializes the component selection buttons, applies styling to them,
     * and sets their event handlers for specific actions
     * @param styleButton a Consumer<Button> function to apply specific styling to the button
     */
    private void createComponentSelectedButtons(Consumer<Button> styleButton) {
        this.turnBtn = new Button("Turn");
        this.keepBtn = new Button("Keep");
        this.discardBtn = new Button("Discard");
        this.linkBtn = new Button("Link");
        this.linkBtn.setDisable(true);
        this.linkBtn.setOpacity(0.5);

        styleButton.accept(turnBtn);
        styleButton.accept(keepBtn);
        styleButton.accept(linkBtn);

        discardBtn.setFont(Font.font("Bangers", 18));
        discardBtn.setTextFill(Color.WHITE);
        discardBtn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        discardBtn.setCursor(Cursor.HAND);
        discardBtn.setPrefWidth(180);

        this.disableSelectButtons(true);

        turnBtn.setOnAction(e -> viewFlow.handleInput("Turn")
        );
        keepBtn.setOnAction(e -> viewFlow.handleInput("Keep"));
        discardBtn.setOnAction(e -> {
            componentSelectedExists = false;
            this.hourglassBtn.setDisable(false);
            this.hourglassBtn.setOpacity(1);

            viewFlow.handleInput("Discard");
        });
        linkBtn.setOnAction(ev -> {
            AnchorPane overlay = new AnchorPane();
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
            overlay.setPickOnBounds(true);

            popupLink = new VBox(12);
            popupLink.setAlignment(Pos.CENTER);
            popupLink.setPadding(new Insets(15));
            popupLink.setPrefSize(200, 250);

            Label insertLabel = new Label("Choose the coordinates to link the component");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
            insertLabel.setWrapText(true);
            insertLabel.setTextAlignment(TextAlignment.CENTER);

            TextField xField = new TextField();
            xField.setPromptText("row");
            xField.setPrefWidth(50);
            xField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) xField.setText(newVal.replaceAll("[^\\d]", ""));
            });

            TextField yField = new TextField();
            yField.setPromptText("col");
            yField.setPrefWidth(50);
            yField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) yField.setText(newVal.replaceAll("[^\\d]", ""));
            });

            HBox coordBox = new HBox(10, xField, yField);
            coordBox.setAlignment(Pos.CENTER);

            Label errorLabel = new Label("Invalid coordinates");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(false);

            Button cancelBtn = new Button("Undo");
            cancelBtn.setFont(Font.font("Bangers", 14));
            cancelBtn.setCursor(Cursor.HAND);
            cancelBtn.setTextFill(Color.WHITE);
            cancelBtn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(8), Insets.EMPTY)));

            Button sendBtn = new Button("Link");
            sendBtn.setFont(Font.font("Bangers", 14));
            sendBtn.setCursor(Cursor.HAND);
            sendBtn.setTextFill(Color.WHITE);
            sendBtn.setBackground(new Background(new BackgroundFill(Color.web("#00A676"), new CornerRadii(8), Insets.EMPTY))); // verde

            HBox buttons = new HBox(15, cancelBtn, sendBtn);
            buttons.setAlignment(Pos.CENTER);

            popupLink.getChildren().addAll(insertLabel, coordBox, errorLabel, buttons);

            AnchorPane.setTopAnchor(popupLink, (root.getHeight() - 250) / 2);
            AnchorPane.setRightAnchor(popupLink, 10.0);

            overlay.getChildren().add(popupLink);
            root.getChildren().add(overlay);

            Runnable removePopup = () -> root.getChildren().remove(overlay);

            cancelBtn.setOnAction(e -> removePopup.run());

            sendBtn.setOnAction(e -> {
                String xText = xField.getText().trim();
                String yText = yField.getText().trim();

                try {
                    int row = Integer.parseInt(xText);
                    int col = Integer.parseInt(yText);

                    if(viewFlow.getLevel() == 2) {
                        if (row < 5 || row > 9 || col < 4 || col > 10) {
                            errorLabel.setVisible(true);
                            return;
                        }
                    }else{
                        if(row < 5 || row > 9 || col < 5 || col > 9){
                            errorLabel.setVisible(true);
                            return;
                        }
                    }


                    errorLabel.setVisible(false);
                    viewFlow.handleInput("link " + row + " " + col);
                    removePopup.run();
                    if(viewFlow.getFirstLinked()) {
                        this.linkBtn.setDisable(false);
                        this.linkBtn.setOpacity(1);
                    }
                } catch (NumberFormatException ex) {
                    errorLabel.setVisible(true);
                }
            });

            yField.setOnAction(e -> sendBtn.fire());
        });

    }

    /**
     * Attaches a set of component selection buttons to the provided scene
     * @param scene  to which the component selection buttons will be added
     */
    private void attackSelectComponentButtons(Scene scene) {
        this.componentBox = new VBox(10);
        this.componentBox.setPadding(new Insets(0, 0, 50, 15));
        this.componentBox.setAlignment(Pos.BOTTOM_LEFT);

        this.componentBox.getChildren().addAll(turnBtn, linkBtn, keepBtn, discardBtn);

        Platform.runLater(() -> {
            StackPane root = (StackPane) scene.getRoot();
            if (!root.getChildren().contains(this.componentBox)) {
                StackPane.setAlignment(this.componentBox, Pos.BOTTOM_LEFT);
                StackPane.setMargin(this.componentBox, new Insets(0, 0, 0, 0));
                root.getChildren().add(this.componentBox);
            }
            this.componentBox.setPickOnBounds(false);

        });

    }

    /**
     * Displays another ship layout on the screen
     * @param layout a 2D array representing the layout of the ship
     * @param nickname of the player
     */
    public void showOtherShip(Component[][] layout, String nickname) {

        Image trasparent = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/transparent.png")).toExternalForm());

        disableAndSaveAllButtonStates();

        AnchorPane overlay = new AnchorPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        overlay.setPickOnBounds(true);

        root.getChildren().add(overlay);

        ImageView backgroundView;
        if (level == 0) {
            backgroundView = new ImageView(new Image(Objects.requireNonNull(
                    getClass().getResource("/it/polimi/ingsw/view/cardboard-3.jpeg")).toExternalForm()));
        } else {
            backgroundView = new ImageView(new Image(Objects.requireNonNull(
                    getClass().getResource("/it/polimi/ingsw/view/cardboard-3b.jpeg")).toExternalForm()));
        }

        backgroundView.setFitWidth(679);
        backgroundView.setFitHeight(492);
        backgroundView.setPreserveRatio(true);
        backgroundView.setMouseTransparent(true);

        Rectangle clip = new Rectangle(679, 492);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        backgroundView.setClip(clip);

        GridPane grid = new GridPane();
        grid.setHgap(1.5);
        grid.setVgap(1.5);
        grid.setAlignment(Pos.CENTER);
        grid.setTranslateY(-1);

        for (int row = 5; row < 10; row++) {
            for (int col = 4; col < 11; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(87, 87);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 4px; -fx-background-radius: 4px;");
                    Component c = layout[row][col];
                    if (c != null) {
                        ImageView iv = new ImageView(c.getFrontImage());
                        iv.setFitWidth(87);
                        iv.setFitHeight(87);
                        iv.setRotate(c.getRotationAngle());
                        Rectangle r = new Rectangle(87, 87);
                        r.setArcWidth(8);
                        r.setArcHeight(8);
                        iv.setClip(r);
                        cell.getChildren().add(iv);
                    } else {
                        ImageView iv = new ImageView(trasparent);
                        iv.setFitWidth(87);
                        iv.setFitHeight(87);
                        Rectangle r = new Rectangle(87, 87);
                        r.setArcWidth(8);
                        r.setArcHeight(8);
                        iv.setClip(r);
                        cell.getChildren().add(iv);
                    }
                } else {
                    cell.setStyle("-fx-background-color: transparent;");
                }

                grid.add(cell, col - 4, row - 5);
            }
        }

        StackPane shipPopup = new StackPane(backgroundView, grid);
        shipPopup.setAlignment(Pos.CENTER);
        shipPopup.setMaxSize(679, 500);
        shipPopup.setMouseTransparent(false);

        String displayName = nickname.endsWith("s") ? nickname + "'" : nickname + "'s";
        Label nameLabel = new Label(displayName + " spaceship");
        nameLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 32));
        nameLabel.setTextFill(Color.web("#FFDE69"));
        nameLabel.setAlignment(Pos.CENTER);

        VBox popupWithLabel = new VBox(10, nameLabel, shipPopup);
        popupWithLabel.setAlignment(Pos.CENTER);

        root.setOnMouseClicked(e -> {
            root.getChildren().remove(overlay);
            root.getChildren().remove(popupWithLabel);
            restoreAllButtonStates();
            if(shipScreenContent != null) {
                isComponentViewVisible = false;
                root.getChildren().setAll(shipScreenContent);
            }
        });

        root.getChildren().add(popupWithLabel);
    }

    /**
     * Restores the state of all buttons that were previously saved in the `previousButtonStates` map
     */
    private void restoreAllButtonStates() {
        for (Map.Entry<Button, Boolean> entry : previousButtonStates.entrySet()) {
            Button btn = entry.getKey();
            boolean wasEnabled = entry.getValue();
            btn.setDisable(!wasEnabled);
            btn.setOpacity(wasEnabled ? 1.0 : 0.5);
        }
        previousButtonStates.clear();
    }

    /**
     * Disables a predefined set of buttons in the scene and saves their current enabled/disabled states
     * in the `previousButtonStates` map for potential restoration later
     */
    private void disableAndSaveAllButtonStates() {
        List<Button> buttons = Arrays.asList(
                finishBtn, stackBtn, rulesBtn, otherBtn, hourglassBtn,
                selectBtn, selectKeptBtn, turnBtn, keepBtn, discardBtn, linkBtn
        );

        previousButtonStates.clear();

        for (Button btn : buttons) {
            previousButtonStates.put(btn, !btn.isDisabled());
            btn.setDisable(true);
            btn.setOpacity(0.5);
        }
    }

    /**
     * Displays a wait overlay indicating that the application is waiting for other users
     * to complete an action
     */
    public void showWaitForOthers() {
        disableAndSaveAllButtonStates();

        Label message = new Label("Waiting for other truckers to finish");
        message.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 32));
        message.setTextFill(Color.web("#FFDE69"));

        Label dots = new Label(".");
        dots.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 32));
        dots.setTextFill(Color.web("#FFDE69"));

        HBox textBox = new HBox(5, message, dots);
        textBox.setAlignment(Pos.CENTER);

        Rectangle overlayBg = new Rectangle(root.getWidth(), root.getHeight());
        overlayBg.setFill(Color.rgb(0, 0, 0, 0.4));
        overlayBg.widthProperty().bind(root.widthProperty());
        overlayBg.heightProperty().bind(root.heightProperty());

        waitOverlay = new StackPane(overlayBg, textBox);
        waitOverlay.setMouseTransparent(true);
        waitOverlay.setAlignment(Pos.CENTER);

        waitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> dots.setText("")),
                new KeyFrame(Duration.seconds(0.5), e -> dots.setText(".")),
                new KeyFrame(Duration.seconds(1.0), e -> dots.setText("..")),
                new KeyFrame(Duration.seconds(1.5), e -> dots.setText("...")),
                new KeyFrame(Duration.seconds(2.0), e -> dots.setText(""))
        );
        waitAnimation.setCycleCount(Animation.INDEFINITE);
        waitAnimation.play();

        Platform.runLater(() -> {
            if (!root.getChildren().contains(waitOverlay)) {
                root.getChildren().add(waitOverlay);
            }
        });
    }

    /**
     * Removes the wait overlay and stops the associated animation, ensuring a clean state in the UI
     */
    public void hideWaitForOthers() {
        Platform.runLater(() -> {
            if (waitOverlay != null) {
                root.getChildren().remove(waitOverlay);
                waitOverlay = null;
            }
            if (waitAnimation != null) {
                waitAnimation.stop();
                waitAnimation = null;
            }
        });
    }

    /**
     * Displays a styled message box in the top-right corner of the application window
     * @param message  text to display within the message box
     */
    public void showMessage(String message) {
        Label title = new Label("New message:");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label content = new Label(message);
        content.setTextFill(Color.WHITE);
        content.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        content.setTextAlignment(TextAlignment.RIGHT);

        VBox messageBox = new VBox(2, title, content);
        messageBox.setAlignment(Pos.CENTER_RIGHT);

        messageBox.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.85); " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-background-radius: 10px;"
        );

        messageBox.setMaxWidth(Region.USE_PREF_SIZE);
        messageBox.setMaxHeight(Region.USE_PREF_SIZE);

        Platform.runLater(() -> {
            StackPane.setAlignment(messageBox, Pos.TOP_RIGHT);
            StackPane.setMargin(messageBox, new Insets(20, 20, 0, 0));
            root.getChildren().add(messageBox);

            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            delay.setOnFinished(e -> root.getChildren().remove(messageBox));
            delay.play();
        });
    }

    /**
     * Displays a visual representation of a stack of cards as an overlay over the current scene
     * @param stack the stack of cards to be displayed
     */
    public void showStack(Stack stack) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        overlay.setPickOnBounds(true);
        overlay.setMouseTransparent(false);

        HBox cardBox = new HBox(20);
        cardBox.setAlignment(Pos.CENTER);

        for (Card card : stack.getStack()) {
            ImageView cardView = new ImageView(card.getFrontImage());
            cardView.setFitWidth(200);
            cardView.setFitHeight(320);
            cardView.setStyle("-fx-effect: dropshadow(one-pass-box, white, 6, 0, 0, 0);");
            cardBox.getChildren().add(cardView);
        }

        Button backBtn = new Button("←");
        backBtn.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 30));
        backBtn.setTextFill(Color.BLACK);
        backBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        backBtn.setCursor(Cursor.HAND);
        backBtn.setPadding(new Insets(12, 18, 12, 18));

        StackPane.setAlignment(backBtn, Pos.TOP_LEFT);
        StackPane.setMargin(backBtn, new Insets(30));

        backBtn.setOnAction(e -> viewFlow.handleInput("dropStack"));

        overlay.setOnMouseClicked(e -> {});

        overlay.getChildren().addAll(cardBox, backBtn);

        Platform.runLater(() -> {
            root.getChildren().add(overlay);
            this.currentStackOverlay = overlay;
        });
    }

    /**
     * Hides the current stack overlay and restores the ship screen content if applicable
     */
    public void hideStackOverlay() {
        if (currentStackOverlay != null) {
            Platform.runLater(() -> {
                root.getChildren().remove(currentStackOverlay);
                currentStackOverlay = null;

                if (shipScreenContent != null) {
                    isComponentViewVisible = false;
                    this.inShipView = true;
                    root.getChildren().setAll(shipScreenContent);
                }
            });
        }
    }

    /**
     * Updates the coordinates of the alien cabin using the provided list of coordinates.
     * @param alienCoords a list alien cabins' coordinates
     */
    public void updateAlienCabinCoord(ArrayList<int[]> alienCoords) {
        this.alienCoords = alienCoords;
    }

    /**
     * Displays a user interface element where the user can place aliens and confirm their placement
     */
    public void showAlienCabin() {
        if(popupLink != null) {
            popupLink.setOpacity(0);
        }

        if (isComponentViewVisible && shipScreenContent != null) {
            root.getChildren().setAll(shipScreenContent);
        }
        disableAndSaveAllButtonStates();
        VBox alienBox = new VBox(10);
        alienBox.setAlignment(Pos.CENTER_RIGHT);
        alienBox.setPadding(new Insets(0, 0, 25, 25));
        alienBox.setMouseTransparent(false);

        Label title = new Label("Place an alien:");
        title.setFont(Font.font("Bangers", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#FFDE69"));

        VBox alienListBox = new VBox(8);
        alienListBox.setAlignment(Pos.CENTER_RIGHT);

        StringBuilder commandBuilder = new StringBuilder("coord");

        for (int[] alienCoord : alienCoords) {
            int x = alienCoord[0];
            int y = alienCoord[1];
            int colorCode = alienCoord[2];
            String colorName = (colorCode == 0) ? "purple" : "brown";

            Text coordText = new Text("[" + x + "," + y + "] " + colorName);
            coordText.setFont(Font.font("Bangers", FontWeight.NORMAL, 16));
            coordText.setFill(Color.WHITE);

            Button placeBtn = new Button("Place");
            placeBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 13));
            placeBtn.setTextFill(Color.BLACK);
            placeBtn.setStyle("-fx-background-color: #FFDE69;");
            placeBtn.setCursor(Cursor.HAND);
            placeBtn.setPrefWidth(70);

            placeBtn.setOnAction(e -> {
                placeBtn.setDisable(true);
                placeBtn.setStyle("-fx-background-color: #999999;");
                commandBuilder.append(" ").append(x).append(" ").append(y).append(" ").append(colorName);
            });

            HBox line = new HBox(10, coordText, placeBtn);
            line.setAlignment(Pos.CENTER_RIGHT);
            alienListBox.getChildren().add(line);
        }

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 14));
        confirmBtn.setTextFill(Color.BLACK);
        confirmBtn.setStyle("-fx-background-color: #FFDE69;");
        confirmBtn.setCursor(Cursor.HAND);
        confirmBtn.setPrefWidth(120);

        confirmBtn.setOnAction(e -> {
            viewFlow.handleInput(commandBuilder.toString());
            root.getChildren().remove(alienBox);
        });

        alienBox.getChildren().addAll(title, alienListBox, confirmBtn);
        StackPane.setAlignment(alienBox, Pos.CENTER_RIGHT);
        StackPane.setMargin(alienBox, new Insets(0, 15, 0, 0));
        root.getChildren().add(alienBox);
    }

    /**
     * Displays a "TIME EXPIRED" message with a blinking effect, with a count
     * indicating how many times the timer has expired
     * @param expirationCount the number of times the expiration event has occurred.
     */
    public void showTimeExpired(int expirationCount) {
        if (scene == null || root == null) return;

        Label message = new Label("TIME EXPIRED!\n" + "For the " + expirationCount + " time");
        message.setTextFill(Color.RED);
        message.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 80));
        message.setTextAlignment(TextAlignment.CENTER);

        FadeTransition blink = new FadeTransition(Duration.seconds(0.4), message);
        blink.setFromValue(1.0);
        blink.setToValue(0.0);
        blink.setCycleCount(Animation.INDEFINITE);
        blink.setAutoReverse(true);
        blink.play();

        Timeline stopBlink = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            blink.stop();
            message.setOpacity(1.0);
        }));
        stopBlink.play();

        Rectangle dimBackground = new Rectangle();
        dimBackground.setFill(Color.rgb(0, 0, 0, 0.6));
        dimBackground.widthProperty().bind(scene.widthProperty());
        dimBackground.heightProperty().bind(scene.heightProperty());

        StackPane overlay = new StackPane(dimBackground, message);
        overlay.setAlignment(Pos.CENTER);
        overlay.setMouseTransparent(false);

        root.getChildren().add(overlay);

        PauseTransition wait = new PauseTransition(Duration.seconds(2.5));
        wait.setOnFinished(e -> root.getChildren().remove(overlay));
        wait.play();
    }

    /**
     * Displays an error message centered on the screen
     * @param message the error message to display
     */
    public void showError(String message) {
        double width = root.getWidth();
        double height = root.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label errorLabel = new Label(message);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 35));
        errorLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        StackPane popup = new StackPane(overlay, errorLabel);
        popup.setMouseTransparent(true);
        StackPane.setAlignment(errorLabel, Pos.CENTER);

        Platform.runLater(() -> {
            if (!root.getChildren().contains(popup)) {
                root.getChildren().add(popup);

                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(e -> root.getChildren().remove(popup));
                delay.play();
            }
        });
    }

    /**
     * Remove the selectedComponentBox setting it as not visible and not able
     */
    private void removeSelectedComponentBox() {
        root.getChildren().remove(componentSelectedBox);
        root.getChildren().remove(componentBox);
        disableSelectButtons(true);
        linkBtn.setOpacity(0);
        discardBtn.setOpacity(0);
        turnBtn.setOpacity(0);
        keepBtn.setOpacity(0);
        finishBtn.setOpacity(0.5);
        finishBtn.setDisable(true);
        hourglassBtn.setOpacity(0);
        hourglassBtn.setDisable(true);
        selectBtn.setOpacity(0.5);
        selectBtn.setDisable(true);
        selectKeptBtn.setOpacity(0.5);
        selectKeptBtn.setDisable(true);
        stackBtn.setOpacity(0.5);
        stackBtn.setDisable(true);
        otherBtn.setDisable(false);
        rulesBtn.setDisable(false);
    }

    /**
     * Adds the remove button with its title to the scene to let the client remove a component
     */
    public void showRemove() {
        if (removeBtn != null) {
            removeRemoveBox();
        }

        removeSelectedComponentBox();

        removeBox = new VBox(10);
        removeBox.setAlignment(Pos.CENTER);
        removeBox.setPadding(new Insets(15));
        removeBox.setPrefWidth(180);

        Label title = new Label("INVALID COMPONENT POSITION!");
        title.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
        title.setTextFill(Color.RED);
        title.setPrefWidth(180);
        title.setWrapText(true);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);

        Label subtitle = new Label("Remove it to make the spaceship correct");
        subtitle.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
        subtitle.setTextFill(Color.WHITE);
        subtitle.setPrefWidth(180);
        subtitle.setWrapText(true);
        subtitle.setTextAlignment(TextAlignment.CENTER);
        subtitle.setAlignment(Pos.CENTER);

        VBox textBox = new VBox(0, title, subtitle);
        textBox.setAlignment(Pos.CENTER);
        textBox.setPadding(new Insets(15));
        textBox.setPrefWidth(180);

        this.removeBtn = new Button("Remove");

        removeBtn.setFont(Font.font("Bangers", 18));
        removeBtn.setTextFill(Color.BLACK);
        removeBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        removeBtn.setCursor(Cursor.HAND);
        removeBtn.setPrefWidth(180);

        removeBox.getChildren().addAll(textBox, removeBtn);

        removeBox.setTranslateX(-615);
        removeBox.setTranslateY(150);

        removeBtn.setOnAction(ev -> {
            removeBtn.setDisable(true);
            removeBtn.setOpacity(0.5);
            AnchorPane overlay = new AnchorPane();
            overlay.setPickOnBounds(true);

            VBox popup = new VBox(12);
            popup.setAlignment(Pos.CENTER);
            popup.setPadding(new Insets(15));
            popup.setPrefSize(200, 250);

            Label insertLabel = new Label("Remove invalid component");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
            insertLabel.setWrapText(true);
            insertLabel.setTextAlignment(TextAlignment.CENTER);

            TextField xField = new TextField();
            xField.setPromptText("row");
            xField.setPrefWidth(50);
            xField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) xField.setText(newVal.replaceAll("[^\\d]", ""));
            });

            TextField yField = new TextField();
            yField.setPromptText("col");
            yField.setPrefWidth(50);
            yField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) yField.setText(newVal.replaceAll("[^\\d]", ""));
            });

            HBox coordBox = new HBox(10, xField, yField);
            coordBox.setAlignment(Pos.CENTER);

            Label errorLabel = new Label("Invalid coordinates");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(false);

            Button cancelBtn = new Button("Undo");
            cancelBtn.setFont(Font.font("Bangers", 14));
            cancelBtn.setCursor(Cursor.HAND);
            cancelBtn.setTextFill(Color.BLACK);
            cancelBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(8), Insets.EMPTY)));

            Button sendBtn = new Button("Remove");
            sendBtn.setFont(Font.font("Bangers", 14));
            sendBtn.setCursor(Cursor.HAND);
            sendBtn.setTextFill(Color.WHITE);
            sendBtn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(8), Insets.EMPTY))); // verde

            HBox buttons = new HBox(15, cancelBtn, sendBtn);
            buttons.setAlignment(Pos.CENTER);

            popup.getChildren().addAll(insertLabel, coordBox, errorLabel, buttons);

            AnchorPane.setTopAnchor(popup, (root.getHeight() - 240) / 2);
            AnchorPane.setRightAnchor(popup, 10.0);

            overlay.getChildren().add(popup);
            root.getChildren().add(overlay);

            Runnable removePopup = () -> root.getChildren().remove(overlay);

            cancelBtn.setOnAction(e -> removePopup.run());

            sendBtn.setOnAction(e -> {
                String xText = xField.getText().trim();
                String yText = yField.getText().trim();

                try {
                    int row = Integer.parseInt(xText);
                    int col = Integer.parseInt(yText);

                    if(viewFlow.getLevel() == 2) {
                        if (row < 5 || row > 9 || col < 4 || col > 10) {
                            errorLabel.setVisible(true);
                            return;
                        }
                    }else{
                        if(row < 5 || row > 9 || col < 5 || col > 9){
                            errorLabel.setVisible(true);
                            return;
                        }
                    }


                    errorLabel.setVisible(false);
                    viewFlow.handleInput("remove " + row + " " + col);
                    removePopup.run();
                    removeBtn.setDisable(false);
                    removeBtn.setOpacity(1);
                } catch (NumberFormatException ex) {
                    errorLabel.setVisible(true);
                }
            });

            yField.setOnAction(e -> sendBtn.fire());
        });

        root.getChildren().removeAll(removeBox, optionsBox);
        root.getChildren().addAll(removeBox, optionsBox);
    }

    /**
     * Removes the removeBox from the scene
     */
    private void removeRemoveBox() {
        if (removeBox != null) {
            root.getChildren().remove(removeBox);
        }
        removeBox = null;
    }
}

