package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.view.ViewFlow;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * The FlightScene class represents the graphical user interface component for the flight
 * scene in the game. It manages the layout, components, and user interactions during gameplay
 */
public class FlightScene {

    /**
     * A 2D array representing the layout of components in the ship
     */
    private Component[][] layout;

    /**
     * Represents a 2D boolean matrix indicating the validity state of cells in a ship layout
     */
    private final boolean[][] validCells;

    /**
     * Represents the main graphical scene which serves as the root
     * of the graphical user interface
     */
    private Scene scene;

    /**
     * Represents the layout grid for the flight scene
     */
    private GridPane grid;

    /**
     * Represents the principal pane that serves as the root container for the user interface elements
     */
    private final StackPane root;

    /**
     * Represents the flow logic of the game
     */
    private final ViewFlow viewFlow;

    /**
     * Represents the rule button that allows the user to open th PDF rules
     */
    private Button rulesBtn;

    /**
     * Represents the show other ship button that allows the user to see other players' spaceship
     */
    private Button otherBtn;

    /**
     * Represents the more button that allows the user to see a detailed description of player's spaceship
     */
    private Button moreButton;

    /**
     * A StackPane that serves as a container for displaying card images
     */
    private StackPane cardImageContainer;

    /**
     * A VBox container used to hold and organize the game's option buttons
     */
    private VBox optionsBox;

    /**
     * A VBox element used to display the result of a roll dice
     */
    private VBox rollDiceResultBox;

    private VBox remainingcardsBox;

    /**
     * A field which is used to fetch images of goods based on their associated value or color
     */
    private final GoodsImagesGetter goodsImagesGetter;

    /**
     * Holds the image representation of a good in the game
     */
    private Image goodImage;

    /**
     * A list of planets  used to manage and display information about the current card's planets
     */
    private ArrayList<Trucker> planets;

    /**
     * Represents a list of goods associated with the remotion
     */
    private ArrayList<Goods> goodsList;

    /**
     * Level of the game
     */
    private final int level;

    /**
     * This variable determines whether the roll dice overlay is currently visible on the screen
     */
    private boolean isRollDiceOverlayVisible;

    /**
     * Represents a label that likely displays information related to spaceship status
     */
    private Label posLabel, battLabel, humanLabel, cannonLabel, engineLabel, creditLabel, lostLabel,
                  battLabelForOther, humanLabelForOther, cannonLabelForOther, engineLabelForOther;

    /**
     * A map that associates a good with its corresponding label
     */
    private final Map<String, Label> goodLabels;

    /**
     * A VBox container representing the status bar
     */
    private VBox statusBar;

    /**
     * A graphical layer that overlays the primary content and is shown always on top
     */
    private final StackPane overlayLayer;

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
     * Constructor of the flight scene
     * @param layout matrix of the ship
     * @param viewFlow logic of the game
     * @param root principal pane of the scene
     */
    public FlightScene(Component[][] layout, ViewFlow viewFlow, StackPane root) {
        this.layout = layout;
        this.root = root;
        this.viewFlow = viewFlow;
        this.level = viewFlow.getLevel();
        this.validCells = new boolean[11][12];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Component c = layout[i][j];
                validCells[i][j] = c == null || !c.getType().equals("Empty Space");
            }
        }
        this.goodsImagesGetter = new GoodsImagesGetter();
        this.planets = new ArrayList<>();
        isRollDiceOverlayVisible = false;
        goodLabels = new HashMap<>();
        overlayLayer = new StackPane();
    }

    /**
     * Create the scene in its principal elements
     * @param playersNames list of players' nickname
     * @param level of the game
     * @return the actual scene to show
     */
    public Scene createScene(ArrayList<String> playersNames, int level) {
        double width = 1600;
        double height = 900;

        Image backgroundImage;
        if (level == 0) {
            backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/cardboard-2.jpeg")).toExternalForm());
        } else {
            backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/cardboard-2b.jpeg")).toExternalForm());
        }

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(true);
        backgroundView.setMouseTransparent(true);

        root.getChildren().clear();
        root.getChildren().addAll(
                backgroundView,
                overlayLayer
        );

        overlayLayer.setMouseTransparent(true);
        overlayLayer.setPickOnBounds(false);

        updateShipLayout();
        initializeStatusBar();
        updateStatusBar();

        scene = new Scene(root, width, height);

        Consumer<Button> styleButton = createDefaultButtonStyler();

        addRulesButtonToScene(styleButton);
        initCardPlaceholder();
        addShowOtherButtonToScene(viewFlow, styleButton, playersNames);
        attachOptionsBoxToScene(scene);

        return scene;
    }

    /**
     * Sets the ship layout for the current scene.
     * @param ship a 2D array representing the components of the ship's layout
     */
    public void setShip(Component[][] ship){
        this.layout = ship;
    }

    /**
     * Restores the interactive components of the scene, reordering their accessibility and functionality
     */
    private void restoreInteractiveLayers() {
        overlayLayer.setMouseTransparent(true);
        root.getChildren().removeAll(optionsBox, statusBar);
        root.getChildren().addAll(optionsBox, statusBar);
    }

    /**
     * Box with the options button of the game
     * @param scene where to add the buttons
     */
    private void attachOptionsBoxToScene(Scene scene) {
        optionsBox = new VBox(10);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.TOP_LEFT);
        optionsBox.getChildren().addAll(otherBtn, rulesBtn);
        optionsBox.setPickOnBounds(false);

        Platform.runLater(() -> {
            StackPane root = (StackPane) scene.getRoot();
            if (!root.getChildren().contains(optionsBox)) {
                StackPane.setAlignment(optionsBox, Pos.TOP_RIGHT);
                StackPane.setMargin(optionsBox, new Insets(20, 0, 0, 15));
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
     * Initializes a card placeholder in the scene
     */
    public void initCardPlaceholder() {
        if (scene == null || scene.getRoot() == null) return;

        double imageWidth = 200;
        double imageHeight = 308;

        Rectangle border = new Rectangle(imageWidth, imageHeight);
        border.setArcWidth(16);
        border.setArcHeight(16);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(2);
        border.setMouseTransparent(true);

        cardImageContainer = new StackPane(border);
        cardImageContainer.setPrefSize(imageWidth, imageHeight);
        cardImageContainer.setAlignment(Pos.CENTER);
        cardImageContainer.setTranslateX(-420);
        cardImageContainer.setMouseTransparent(true);
        cardImageContainer.setUserData("cardContainer");

        StackPane.setAlignment(cardImageContainer, Pos.CENTER);
        root.getChildren().add(cardImageContainer);
    }

    /**
     * Initializes the status bar by setting up the visual elements, including labels, icons, and containers
     */
    private void initializeStatusBar() {
        Image engine = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/engine.png")).toExternalForm());
        Image cannon = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/cannon.png")).toExternalForm());
        Image human = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/human.png")).toExternalForm());
        Image battery = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/battery.png")).toExternalForm());
        Image pos = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/position.png")).toExternalForm());
        Image credit = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/credit.png")).toExternalForm());
        Image lost = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/lost.png")).toExternalForm());

        posLabel = createStyledLabel();
        battLabel = createStyledLabel();
        humanLabel = createStyledLabel();
        cannonLabel = createStyledLabel();
        engineLabel = createStyledLabel();
        creditLabel = createStyledLabel();
        lostLabel = createStyledLabel();

        ImageView posIcon = new ImageView(pos);
        posIcon.setFitWidth(20);
        posIcon.setPreserveRatio(true);

        ImageView batteryIcon = new ImageView(battery);
        batteryIcon.setFitWidth(30);
        batteryIcon.setFitHeight(50);
        batteryIcon.setPreserveRatio(false);

        ImageView humanIcon = new ImageView(human);
        humanIcon.setFitWidth(40);
        humanIcon.setPreserveRatio(true);

        ImageView cannonIcon = new ImageView(cannon);
        cannonIcon.setFitWidth(20);
        cannonIcon.setPreserveRatio(true);

        ImageView engineIcon = new ImageView(engine);
        engineIcon.setFitWidth(16);
        engineIcon.setPreserveRatio(true);

        ImageView creditIcon = new ImageView(credit);
        creditIcon.setFitWidth(25);
        creditIcon.setPreserveRatio(true);

        ImageView lostIcon = new ImageView(lost);
        lostIcon.setFitWidth(25);
        lostIcon.setPreserveRatio(true);

        HBox posBox = new HBox(0, posIcon, posLabel);
        HBox battBox = new HBox(0, batteryIcon, battLabel);
        HBox humanBox = new HBox(0, humanIcon, humanLabel);
        HBox cannonBox = new HBox(0, cannonIcon, cannonLabel);
        HBox engineBox = new HBox(5, engineIcon, engineLabel);
        HBox creditBox = new HBox(0, creditIcon, creditLabel);
        HBox lostBox = new HBox(0, lostIcon, lostLabel);


        for (HBox box : List.of(posBox, battBox, humanBox, cannonBox, engineBox, creditBox, lostBox)) {
            box.setAlignment(Pos.CENTER_RIGHT);
        }

        HBox goodsBox = new HBox(10);
        goodsBox.setAlignment(Pos.CENTER_RIGHT);

        Map<String, Image> goodImages = Map.of(
                "red", new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/redgood.png")).toExternalForm()),
                "yellow", new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/yellowgood.png")).toExternalForm()),
                "green", new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/greengood.png")).toExternalForm()),
                "blue", new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/bluegood.png")).toExternalForm())
        );

        List<String> orderedColors = List.of("red", "yellow", "green", "blue");

        for (String color : orderedColors) {
            Image image = goodImages.get(color);
            ImageView goodIcon = new ImageView(image);
            goodIcon.setFitWidth(35);
            goodIcon.setPreserveRatio(true);

            Label qtyLabel = createStyledLabel();
            goodLabels.put(color, qtyLabel);

            HBox singleGood = new HBox(0, goodIcon, qtyLabel);
            singleGood.setAlignment(Pos.CENTER_RIGHT);

            goodsBox.getChildren().add(singleGood);
        }

        moreButton = new Button("More...");
        moreButton.setFont(Font.font("Arial", 16));
        moreButton.setTextFill(Color.BLACK);
        moreButton.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        moreButton.setCursor(Cursor.HAND);
        moreButton.setOnAction(e -> showMore());

        HBox resourceBox = new HBox(20, posBox, battBox, humanBox, cannonBox, engineBox, goodsBox, creditBox, lostBox);
        resourceBox.setAlignment(Pos.CENTER);
        resourceBox.setPadding(new Insets(5));

        statusBar = new VBox(5, resourceBox, moreButton);
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(10));
        statusBar.setMaxWidth(800);
        statusBar.setMouseTransparent(false);

        Platform.runLater(() -> {
            StackPane rootPane = (StackPane) root.getScene().getRoot();
            StackPane.setAlignment(statusBar, Pos.CENTER_RIGHT);
            StackPane.setMargin(statusBar, new Insets(0, 40, -550, 0));
            if (!rootPane.getChildren().contains(statusBar)) {
                rootPane.getChildren().add(statusBar);
            }
        });
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
                if (nickname.equals(viewFlow.getNickname())) continue;
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
     * Creates and returns a status bar for the given spaceship
     * @param ship the spaceship for which the status bar is to be created
     * @return a VBox containing the spaceship's status bar
     */
    private VBox createStatusBarForOtherShip(SpaceShip ship) {
        Image engineForOther = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/engine.png")).toExternalForm());
        Image cannonForOther = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/cannon.png")).toExternalForm());
        Image humanForOther = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/human.png")).toExternalForm());
        Image batteryForOther = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/battery.png")).toExternalForm());

        battLabelForOther = createStyledLabel();
        humanLabelForOther = createStyledLabel();
        cannonLabelForOther = createStyledLabel();
        engineLabelForOther = createStyledLabel();

        ImageView batteryIconForOther = new ImageView(batteryForOther);
        batteryIconForOther.setFitWidth(30);
        batteryIconForOther.setFitHeight(50);
        batteryIconForOther.setPreserveRatio(false);

        ImageView humanIconForOther = new ImageView(humanForOther);
        humanIconForOther.setFitWidth(40);
        humanIconForOther.setPreserveRatio(true);

        ImageView cannonIconForOther = new ImageView(cannonForOther);
        cannonIconForOther.setFitWidth(20);
        cannonIconForOther.setPreserveRatio(true);

        ImageView engineIconForOther = new ImageView(engineForOther);
        engineIconForOther.setFitWidth(16);
        engineIconForOther.setPreserveRatio(true);

        HBox battForOtherBox = new HBox(0, batteryIconForOther, battLabelForOther);
        HBox humanForOtherBox = new HBox(0, humanIconForOther, humanLabelForOther);
        HBox cannonForOtherBox = new HBox(0, cannonIconForOther, cannonLabelForOther);
        HBox engineForOtherBox = new HBox(5, engineIconForOther, engineLabelForOther);

        battLabelForOther.setText(": " + ship.getNumBatteries());
        humanLabelForOther.setText(": " + ship.getNumCrew());
        cannonLabelForOther.setText(": " + ship.getSingleCannonPower());
        engineLabelForOther.setText(": " + ship.getSingleEnginePower());

        for (HBox box : List.of(battForOtherBox, humanForOtherBox, cannonForOtherBox, engineForOtherBox)) {
            box.setAlignment(Pos.CENTER);
        }

        HBox goodsBox = new HBox(10);
        goodsBox.setAlignment(Pos.CENTER);
        Map<String, Integer> goods = ship.getGoodsByColor();
        Map<String, String> colorToImg = Map.of(
                "red", "/it/polimi/ingsw/view/redgood.png",
                "yellow", "/it/polimi/ingsw/view/yellowgood.png",
                "green", "/it/polimi/ingsw/view/greengood.png",
                "blue", "/it/polimi/ingsw/view/bluegood.png"
        );
        for (String color : List.of("red", "yellow", "green", "blue")) {
            ImageView icon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(colorToImg.get(color))).toExternalForm()));
            icon.setFitWidth(35);
            icon.setPreserveRatio(true);
            Label qty = createStyledLabel();
            qty.setText(": " + goods.getOrDefault(color, 0));
            HBox g = new HBox(5, icon, qty);
            g.setAlignment(Pos.CENTER);
            goodsBox.getChildren().add(g);
        }

        HBox valuesRow = new HBox(25, battForOtherBox, humanForOtherBox, cannonForOtherBox, engineForOtherBox);
        valuesRow.setAlignment(Pos.CENTER);

        VBox statusBarForOther = new VBox(10, valuesRow, goodsBox);
        statusBarForOther.setAlignment(Pos.CENTER);
        statusBarForOther.setPadding(new Insets(15));
        statusBarForOther.setMaxWidth(800);

        return statusBarForOther;
    }

    /**
     * Displays another ship layout on the screen
     * @param ship of the player
     * @param nickname of the player
     */
    public void showOtherShip(SpaceShip ship, String nickname) {
        statusBar.setOpacity(0);
        moreButton.setDisable(true);

        Component[][] layout = ship.getComponents();

        Image trasparent = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/transparent.png")).toExternalForm());

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

        VBox statusBarForShip = createStatusBarForOtherShip(ship);
        VBox popupWithLabel = new VBox(20, nameLabel, shipPopup, statusBarForShip);
        popupWithLabel.setAlignment(Pos.CENTER);

        root.setOnMouseClicked(e -> {
            root.getChildren().remove(overlay);
            root.getChildren().remove(popupWithLabel);
            statusBar.setOpacity(1);
            moreButton.setDisable(false);
            restoreInteractiveLayers();
        });

        root.getChildren().add(popupWithLabel);
    }


    /**
     * Displays the current condition to the user in an overlay popup
     * @param condition the current condition to display, such as HUMANS, ENGINES, or CANNONS
     * @param isLeader  a boolean indicating whether the user is the leader
     */
    public void showCurrCondition(Condition condition, boolean isLeader) {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label confirm = new Label("Current condition");
        confirm.setTextFill(Color.web("#FFDE69"));
        confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 25));

        Image conditionImage = switch (condition) {
            case HUMANS -> new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/human.png")).toExternalForm());
            case ENGINES -> new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/engine.png")).toExternalForm());
            case CANNONS -> new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/cannon.png")).toExternalForm());
        };

        ImageView conditionView = new ImageView(conditionImage);
        conditionView.setPreserveRatio(true);
        conditionView.setMouseTransparent(true);
        if(condition == Condition.CANNONS) {
            conditionView.setFitWidth(30);
        } else if(condition == Condition.HUMANS) {
            conditionView.setFitWidth(50);
        } else {
            conditionView.setFitWidth(25);
        }

        box.getChildren().addAll(confirm, conditionView);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setLeftAnchor(box, 200.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        if (isLeader) {
            Button continueBtn = new Button("Start condition");
            continueBtn.setFont(Font.font("Bangers", 18));
            continueBtn.setTextFill(Color.BLACK);
            continueBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            continueBtn.setCursor(Cursor.HAND);
            continueBtn.setPadding(new Insets(10, 20, 10, 20));

            continueBtn.setOnAction(e -> {
                viewFlow.handleInput("warzone");
                closePopup.run();
            });

            box.getChildren().add(continueBtn);
            AnchorPane.setBottomAnchor(box, 120.0);
        } else {
            AnchorPane.setBottomAnchor(box, 180.0);
        }
    }

    /**
     * Returns the current scene
     * @return the current Scene object
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Displays an input prompt to the user that collects the "state" input
     */
    public void showStateInput() {
        viewFlow.handleInput("state");
    }

    /**
     * Updates the layout of the ship's grid displayed
     */
    public void updateShipLayout() {
        Image trasparent = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/transparent.png")).toExternalForm());

        if (grid != null) {
            root.getChildren().remove(grid);
        }

        grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.TOP_RIGHT);
        grid.setTranslateX(-57);
        grid.setTranslateY(74);

        grid.setMouseTransparent(true);


        for (int row = 5; row < 10; row++) {
            for (int col = 4; col < 11; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(107, 107);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 6px; -fx-background-radius: 6px;");

                    Component c = layout[row][col];
                    if (c != null) {
                        ImageView imageView = new ImageView(c.getFrontImage());
                        imageView.setFitWidth(107);
                        imageView.setFitHeight(107);
                        Rectangle clip = new Rectangle(107, 107);
                        clip.setArcWidth(12);
                        clip.setArcHeight(12);
                        imageView.setClip(clip);
                        imageView.setRotate(c.getRotationAngle());
                        cell.getChildren().add(imageView);
                    } else {
                        ImageView imageView = new ImageView(trasparent);
                        imageView.setFitWidth(107);
                        imageView.setFitHeight(107);
                        Rectangle clip = new Rectangle(107, 107);
                        clip.setArcWidth(12);
                        clip.setArcHeight(12);
                        imageView.setClip(clip);
                        cell.getChildren().add(imageView);
                    }
                } else {
                    cell.setStyle("-fx-background-color: transparent;");
                }

                grid.add(cell, col - 4, row - 5);
            }
        }
        if (!isRollDiceOverlayVisible) {
            root.getChildren().add(grid);
        } else {
            root.getChildren().add(root.getChildren().indexOf(overlayLayer), grid);
        }
    }

    /**
     * Updates the visual representation of a card in the scene by replacing or adding the card image
     * to the designated container
     * @param card the card object containing details and images to be displayed
     */
    public void updateCardInScene(Card card) {
        if (cardImageContainer == null || card == null) return;

        double imageWidth = 200;
        double imageHeight = 308;

        ImageView cardView = new ImageView(card.getFrontImage());
        cardView.setFitWidth(imageWidth);
        cardView.setFitHeight(imageHeight);
        cardView.setPreserveRatio(true);
        cardView.setMouseTransparent(true);

        Rectangle clip = new Rectangle(imageWidth, imageHeight);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        cardView.setClip(clip);

        Node border = cardImageContainer.getChildren().isEmpty() ? null : cardImageContainer.getChildren().getFirst();
        cardImageContainer.getChildren().setAll(border, cardView);

        restoreInteractiveLayers();
    }

    /**
     * Displays a confirmation popup asking the user if they want to land
     */
    public void showLandInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label confirm = new Label("Would you like to land?");
        confirm.setTextFill(Color.web("#FFDE69"));
        confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 25));

        Button yes = new Button("YES");
        yes.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        yes.setTextFill(Color.WHITE);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
        yes.setCursor(Cursor.HAND);
        yes.setPrefWidth(100);

        Button no = new Button("NO");
        no.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        no.setTextFill(Color.WHITE);
        no.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        no.setCursor(Cursor.HAND);
        no.setPrefWidth(100);

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER);
        box.getChildren().addAll(confirm, buttons);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 180.0);
        AnchorPane.setLeftAnchor(box, 170.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        yes.setOnAction(ev -> {
            viewFlow.handleInput("y");
            closePopup.run();
        });

        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            closePopup.run();
        });
    }

    /**
     * Displays a popup interface allowing the user to activate double engines
     * with an option to input specific coordinates for the operation
     */
    public void showActivateEngineInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label confirm = new Label("Would you like to activate double engines?");
        confirm.setTextFill(Color.web("#FFDE69"));
        confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 25));

        Button yes = new Button("YES");
        yes.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        yes.setTextFill(Color.WHITE);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
        yes.setCursor(Cursor.HAND);
        yes.setPrefWidth(100);

        Button no = new Button("NO");
        no.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        no.setTextFill(Color.WHITE);
        no.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        no.setCursor(Cursor.HAND);
        no.setPrefWidth(100);

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER);
        box.getChildren().addAll(confirm, buttons);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 180.0);
        AnchorPane.setLeftAnchor(box, 70.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            closePopup.run();
        });

        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert the coordinates of double engines and battery storages");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));

            Label hintLabel = new Label("Type '<row> <col>, ..., battery, <row> <col> - <num>, ...'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 2 3, 4 5, battery, 6 7 - 2");
            inputField.setPrefWidth(250);

            Label errorLabel = new Label();
            errorLabel.setTextFill(Color.RED);
            errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            errorLabel.setVisible(false);

            Button confirmBtn = new Button("Confirm");
            confirmBtn.setFont(Font.font("Bangers", 14));
            confirmBtn.setTextFill(Color.BLACK);
            confirmBtn.setCursor(Cursor.HAND);
            confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));


            Runnable sendInput = () -> {
                String text = inputField.getText().trim();
                if (isValidActivateWithBattery(text)) {
                    viewFlow.handleInput("y, " + text);
                    closePopup.run();
                } else {
                    errorLabel.setText("Invalid input! Format: '<row> <col>, ..., battery, <row> <col> - <num>'");
                    errorLabel.setVisible(true);
                }
            };


            inputField.setOnAction(e -> sendInput.run());
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);
            box.getChildren().addAll(insertLabel, hintLabel, inputBox, errorLabel);
            box.setTranslateX(-20);
            box.setTranslateY(30);
        });
    }

    /**
     * Displays a popup interface allowing the user to activate double cannons
     * with an option to input specific coordinates for the operation
     */
    public void showActivateCannonsInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label confirm = new Label("Would you like to activate double cannons?");
        confirm.setTextFill(Color.web("#FFDE69"));
        confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 25));

        Button yes = new Button("YES");
        yes.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        yes.setTextFill(Color.WHITE);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
        yes.setCursor(Cursor.HAND);
        yes.setPrefWidth(100);

        Button no = new Button("NO");
        no.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        no.setTextFill(Color.WHITE);
        no.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        no.setCursor(Cursor.HAND);
        no.setPrefWidth(100);

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER);
        box.getChildren().addAll(confirm, buttons);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 180.0);
        AnchorPane.setLeftAnchor(box, 70.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            closePopup.run();
        });

        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert the coordinates of double cannons and battery storages");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));

            Label hintLabel = new Label("Type '<row> <col>, ..., battery, <row> <col> - <num>, ...'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 2 3, 4 5, battery, 6 7 - 2");
            inputField.setPrefWidth(250);

            Label errorLabel = new Label();
            errorLabel.setTextFill(Color.RED);
            errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            errorLabel.setVisible(false);

            Button confirmBtn = new Button("Confirm");
            confirmBtn.setFont(Font.font("Bangers", 14));
            confirmBtn.setTextFill(Color.BLACK);
            confirmBtn.setCursor(Cursor.HAND);
            confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

            Runnable sendInput = () -> {
                String text = inputField.getText().trim();
                if (isValidActivateWithBattery(text)) {
                    viewFlow.handleInput("y, " + text);
                    closePopup.run();
                } else {
                    errorLabel.setText("Invalid input! Format: '<row> <col>, ..., battery, <row> <col> - <num>'");
                    errorLabel.setVisible(true);
                }
            };

            inputField.setOnAction(e -> sendInput.run());
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);

            box.getChildren().addAll(insertLabel, hintLabel, inputBox, errorLabel);
            box.setTranslateX(-20);
            box.setTranslateY(30);
        });
    }

    /**
     * Validates an input string to determine if it follows the required format for activation
     * with battery coordinates and data
     * @param input the string to be validated
     * @return if the input is valid, according to the specified format or not
     */
    private boolean isValidActivateWithBattery(String input) {
        String[] parts = input.split("\\s*,\\s*");
        if (parts.length < 3) return false;

        int batteryIndex = -1;

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].trim().equalsIgnoreCase("battery")) {
                batteryIndex = i;
                break;
            }
        }

        if (batteryIndex == -1 || batteryIndex == parts.length - 1) return false;

        for (int i = 0; i < batteryIndex; i++) {
            String[] coords = parts[i].trim().split("\\s+");
            if (coords.length != 2) return false;
            try {
                Integer.parseInt(coords[0]);
                Integer.parseInt(coords[1]);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        for (int i = batteryIndex + 1; i < parts.length; i++) {
            String[] batteryParts = parts[i].trim().split("\\s*-\\s*");
            if (batteryParts.length != 2) return false;

            String[] coord = batteryParts[0].trim().split("\\s+");
            if (coord.length != 2) return false;

            try {
                Integer.parseInt(coord[0]);
                Integer.parseInt(coord[1]);
                Integer.parseInt(batteryParts[1].trim());
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    /**
     * Displays a popup interface allowing the user to activate shields
     * with an option to input specific coordinates for the operation
     */
    public void showActivateShieldInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label confirm = new Label("Would you like to activate a shield?");
        confirm.setTextFill(Color.web("#FFDE69"));
        confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 25));

        Button yes = new Button("YES");
        yes.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        yes.setTextFill(Color.WHITE);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
        yes.setCursor(Cursor.HAND);
        yes.setPrefWidth(100);

        Button no = new Button("NO");
        no.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        no.setTextFill(Color.WHITE);
        no.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        no.setCursor(Cursor.HAND);
        no.setPrefWidth(100);

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER);
        box.getChildren().addAll(confirm, buttons);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 180.0);
        AnchorPane.setLeftAnchor(box, 110.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            closePopup.run();
        });

        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert the coordinates of the shield and the battery storage");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));

            Label hintLabel = new Label("Type '<row> <col>, <rowBatt> <colBatt>'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 2 3, 4 5");
            inputField.setPrefWidth(250);

            Label errorLabel = new Label();
            errorLabel.setTextFill(Color.RED);
            errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            errorLabel.setVisible(false);

            Button confirmBtn = new Button("Confirm");
            confirmBtn.setFont(Font.font("Bangers", 14));
            confirmBtn.setTextFill(Color.BLACK);
            confirmBtn.setCursor(Cursor.HAND);
            confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

            Runnable sendInput = () -> {
                String text = inputField.getText().trim();
                if (isValidActivateShield(text)) {
                    viewFlow.handleInput("y, " + text);
                    closePopup.run();
                } else {
                    errorLabel.setText("Invalid input! Use format: '<row> <col>, <rowBatt> <colBatt>' with numbers");
                    errorLabel.setVisible(true);
                }
            };

            inputField.setOnAction(e -> sendInput.run());
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);

            box.getChildren().addAll(insertLabel, hintLabel, inputBox, errorLabel);
            box.setTranslateX(-50);
            box.setTranslateY(30);
        });
    }

    /**
     * Validates the given input string to determine whether it conforms to the expected format
     * for activating a shield
     * @param input the string to be validated
     * @return if the input matches the required format and contains valid integer coordinates or not
     */
    private boolean isValidActivateShield(String input) {
        String[] parts = input.split("\\s*,\\s*");
        if (parts.length != 2) return false;

        try {
            String[] shieldCoord = parts[0].trim().split("\\s+");
            String[] batteryCoord = parts[1].trim().split("\\s+");

            if (shieldCoord.length != 2 || batteryCoord.length != 2) return false;

            Integer.parseInt(shieldCoord[0]);
            Integer.parseInt(shieldCoord[1]);
            Integer.parseInt(batteryCoord[0]);
            Integer.parseInt(batteryCoord[1]);

        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Displays a user input popup that allows the user to enter coordinates and corresponding numbers
     * for defining specific cabin configurations
     */
    public void showHumanCoordInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.setMaxWidth(400);

        Label insertLabel = new Label("Insert the coordinates of the cabins to remove the number of crew members indicated");
        insertLabel.setTextFill(Color.web("#FFDE69"));
        insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        insertLabel.setTextAlignment(TextAlignment.CENTER);
        insertLabel.setWrapText(true);

        Label hintLabel = new Label("Type '<row> <col> - <number>, ...'");
        hintLabel.setTextFill(Color.LIGHTGRAY);
        hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
        hintLabel.setWrapText(true);

        TextField inputField = new TextField();
        inputField.setPromptText("Ex. 2 3 - 1, 4 5 - 2");
        inputField.setPrefWidth(300);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        errorLabel.setVisible(false);

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setFont(Font.font("Bangers", 14));
        confirmBtn.setTextFill(Color.BLACK);
        confirmBtn.setCursor(Cursor.HAND);
        confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

        HBox inputBox = new HBox(10, inputField, confirmBtn);
        inputBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(insertLabel, hintLabel, inputBox, errorLabel);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 130.0);
        AnchorPane.setLeftAnchor(box, 100.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        Runnable sendInput = () -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                if (isValidHumanCoord(text)) {
                    viewFlow.handleInput(text);
                    closePopup.run();
                } else {
                    errorLabel.setText("Invalid input format! Try again.");
                    errorLabel.setVisible(true);
                }
            }
        };

        inputField.setOnAction(e -> sendInput.run());
        confirmBtn.setOnAction(e -> sendInput.run());
    }

    /**
     * Validates the given input string to determine whether it represents valid human coordinates
     * based on the required format
     * @param input the string to validate
     * @return if the input string follows the expected format and contains valid integers or not
     */
    private boolean isValidHumanCoord(String input) {
        String[] groups = input.split("\\s*,\\s*");
        for (String group : groups) {
            String[] parts = group.split("\\s*-\\s*");
            if (parts.length != 2) return false;

            String[] coord = parts[0].trim().split("\\s+");
            if (coord.length != 2) return false;

            try {
                Integer.parseInt(coord[0].trim());
                Integer.parseInt(coord[1].trim());
                Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Displays a pop-up overlay for user input related to gaining a good
     */
    public void showPlotGoodInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label confirm = new Label("Would you like to gain this good?");
        confirm.setTextFill(Color.web("#FFDE69"));
        confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 25));

        ImageView goodView = new ImageView(goodImage);
        goodView.setFitWidth(50);
        goodView.setPreserveRatio(true);
        goodView.setMouseTransparent(true);

        Button yes = new Button("YES");
        yes.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        yes.setTextFill(Color.WHITE);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
        yes.setCursor(Cursor.HAND);
        yes.setPrefWidth(100);

        Button no = new Button("NO");
        no.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        no.setTextFill(Color.WHITE);
        no.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        no.setCursor(Cursor.HAND);
        no.setPrefWidth(100);

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER);
        box.getChildren().addAll(confirm, goodView, buttons);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 180.0);
        AnchorPane.setLeftAnchor(box, 120.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            closePopup.run();
        });

        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert coordinates and internal position");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));

            ImageView goodView2 = new ImageView(goodImage);
            goodView2.setFitWidth(50);
            goodView2.setPreserveRatio(true);
            goodView2.setMouseTransparent(true);

            Label xLabel = new Label("Row:");
            xLabel.setTextFill(Color.web("#FFDE69"));
            xLabel.setFont(Font.font("Bangers", 15));

            Label yLabel = new Label("Col:");
            yLabel.setTextFill(Color.web("#FFDE69"));
            yLabel.setFont(Font.font("Bangers", 15));

            Label posLabel = new Label("Position:");
            posLabel.setTextFill(Color.web("#FFDE69"));
            posLabel.setFont(Font.font("Bangers", 15));

            TextField xField = new TextField();
            xField.setPromptText("row");
            xField.setPrefWidth(50);

            TextField yField = new TextField();
            yField.setPromptText("col");
            yField.setPrefWidth(50);

            TextField posField = new TextField();
            posField.setPromptText("pos");
            posField.setPrefWidth(50);

            Label errorLabel = new Label("Invalid input: please enter valid numbers ≥ 0");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            errorLabel.setVisible(false);

            HBox inputBox = new HBox(10, xLabel, xField, yLabel, yField, posLabel, posField);
            inputBox.setAlignment(Pos.CENTER);

            Button cancelBtn = new Button("Undo");
            cancelBtn.setFont(Font.font("Bangers", 14));
            cancelBtn.setCursor(Cursor.HAND);
            cancelBtn.setTextFill(Color.WHITE);
            cancelBtn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(8), Insets.EMPTY)));

            Button sendBtn = new Button("Confirm");
            sendBtn.setFont(Font.font("Bangers", 14));
            sendBtn.setCursor(Cursor.HAND);
            sendBtn.setTextFill(Color.WHITE);
            sendBtn.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(8), Insets.EMPTY)));

            HBox sendBox = new HBox(15, cancelBtn, sendBtn);
            sendBox.setAlignment(Pos.CENTER);

            box.getChildren().addAll(insertLabel, goodView2, inputBox, errorLabel, sendBox);
            box.setTranslateX(15);
            box.setTranslateY(50);

            cancelBtn.setOnAction(e -> closePopup.run());

            sendBtn.setOnAction(e -> {
                String xText = xField.getText().trim();
                String yText = yField.getText().trim();
                String posText = posField.getText().trim();

                if (isValidPlotGoodInput(xText, yText, posText)) {
                    viewFlow.handleInput("y, " + xText + " " + yText + ", " + posText);
                    closePopup.run();
                } else {
                    errorLabel.setVisible(true);
                }
            });

            posField.setOnAction(e -> sendBtn.fire());
        });
    }

    /**
     * Validates the given input strings to determine if they represent valid coordinates and position values
     * @param xText the string representing the row-coordinate
     * @param yText the string representing the column-coordinate
     * @param posText the string representing the position value
     * @return if all inputs are valid non-negative integers or not
     */
    private boolean isValidPlotGoodInput(String xText, String yText, String posText) {
        try {
            int x = Integer.parseInt(xText.trim());
            int y = Integer.parseInt(yText.trim());
            int pos = Integer.parseInt(posText.trim());

            return x >= 0 && y >= 0 && pos >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Displays a popup interface for selecting a planet to land on or choosing not to land
     */
    public void showPlanetDecisionInput() {
        VBox mainBox = new VBox(15);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(10));

        Label chooseLabel = new Label("Choose a planet to land on:");
        chooseLabel.setTextFill(Color.web("#FFDE69"));
        chooseLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 22));

        VBox planetListBox = new VBox(8);
        planetListBox.setAlignment(Pos.CENTER);

        AnchorPane popupContent = new AnchorPane(mainBox);
        AnchorPane.setBottomAnchor(mainBox, 180.0);
        AnchorPane.setLeftAnchor(mainBox, 110.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        for (int i = 0; i < planets.size(); i++) {
            Trucker t = planets.get(i);
            String status = (t == null) ? "free" : t.getNickname();

            Text planetLabel = new Text("Planet " + (i + 1) + ": ");
            planetLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
            planetLabel.setFill(Color.WHITE);

            Text statusText = new Text(status);
            statusText.setFont(Font.font("Bangers", FontWeight.NORMAL, 16));
            statusText.setFill(t == null ? Color.LIGHTGREEN : Color.RED);

            Button selectBtn = new Button("Land");
            selectBtn.setFont(Font.font("Bangers", 13));
            selectBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            selectBtn.setTextFill(Color.BLACK);
            selectBtn.setCursor(Cursor.HAND);
            selectBtn.setPrefWidth(80);
            selectBtn.setDisable(t != null);

            int finalI = i;
            selectBtn.setOnAction(e -> {
                viewFlow.handleInput(String.valueOf(finalI));
                closePopup.run();
            });

            HBox line = new HBox(10, selectBtn, planetLabel, statusText);
            line.setAlignment(Pos.CENTER);
            planetListBox.getChildren().add(line);
        }

        Button dontLandBtn = new Button("Don't land");
        dontLandBtn.setFont(Font.font("Bangers", 16));
        dontLandBtn.setTextFill(Color.BLACK);
        dontLandBtn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        dontLandBtn.setCursor(Cursor.HAND);
        dontLandBtn.setPrefWidth(160);
        dontLandBtn.setOnAction(ev -> {
            viewFlow.handleInput("-1");
            closePopup.run();
        });

        mainBox.getChildren().addAll(chooseLabel, planetListBox, dontLandBtn);
        mainBox.setTranslateX(55);
        if(planets.size() == 2) {
            mainBox.setTranslateY(60);
        } else if(planets.size() == 3) {
            mainBox.setTranslateY(90);
        } else {
            mainBox.setTranslateY(130);
        }
    }

    /**
     * Displays a pop-up input interface for the "Draw Card" action
     */
    public void showDrawCardInput() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        Button continueBtn = new Button("Draw next card");
        continueBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
        continueBtn.setTextFill(Color.BLACK);
        continueBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        continueBtn.setCursor(Cursor.HAND);
        continueBtn.setPadding(new Insets(10, 20, 10, 20));

        container.getChildren().add(continueBtn);

        AnchorPane popupContent = new AnchorPane(container);
        AnchorPane.setBottomAnchor(container, 210.0);
        AnchorPane.setLeftAnchor(container, 215.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        continueBtn.setOnAction(e -> {
            viewFlow.handleInput("draw");
            closePopup.run();
        });
    }

    /**
     * Displays a popup to initiate warzone card
     */
    public void showWarzoneInput() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        Button continueBtn = new Button("Start warzone");
        continueBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
        continueBtn.setTextFill(Color.BLACK);
        continueBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        continueBtn.setCursor(Cursor.HAND);
        continueBtn.setPadding(new Insets(10, 20, 10, 20));

        container.getChildren().add(continueBtn);

        AnchorPane popupContent = new AnchorPane(container);
        AnchorPane.setBottomAnchor(container, 210.0);
        AnchorPane.setLeftAnchor(container, 215.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        continueBtn.setOnAction(e -> {
            viewFlow.handleInput("warzone");
            closePopup.run();
        });
    }

    /**
     * Displays a confirmation popup indicating that a remotion action has been confirmed
     */
    public void showRemotionConfirmedInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label title = new Label("Remotion confirmed");
        title.setFont(Font.font("Bangers", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#FFDE69"));

        Button continueBtn = new Button("Continue");
        continueBtn.setFont(Font.font("Bangers", 18));
        continueBtn.setTextFill(Color.BLACK);
        continueBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        continueBtn.setCursor(Cursor.HAND);
        continueBtn.setPadding(new Insets(10, 20, 10, 20));

        box.getChildren().addAll(title, continueBtn);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 180.0);
        AnchorPane.setLeftAnchor(box, 195.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        continueBtn.setOnAction(e -> {
            viewFlow.handleInput("continue");
            closePopup.run();
        });
    }

    /**
     * Displays a popup input dialogue for the user to specify the coordinates and quantities
     * of batteries to be removed
     */
    public void showRemoveBatteriesInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label insertLabel = new Label("Remove " + viewFlow.getNumOfBatteriesToRemove() + " batteries, insert coordinates");
        insertLabel.setTextFill(Color.web("#FFDE69"));
        insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        insertLabel.setTextAlignment(TextAlignment.CENTER);
        insertLabel.setWrapText(true);

        Label hintLabel = new Label("Type '<row> <col> - <num>, ...'");
        hintLabel.setTextFill(Color.LIGHTGRAY);
        hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

        TextField inputField = new TextField();
        inputField.setPromptText("Ex. 2 3 - 1, 4 5 - 2");
        inputField.setPrefWidth(250);

        Label errorLabel = new Label("Invalid input. Format: <row> <col> - <num>, ... (all ≥ 0)");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        errorLabel.setVisible(false);

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setFont(Font.font("Bangers", 14));
        confirmBtn.setTextFill(Color.BLACK);
        confirmBtn.setCursor(Cursor.HAND);
        confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

        HBox inputBox = new HBox(10, inputField, confirmBtn);
        inputBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(insertLabel, hintLabel, inputBox, errorLabel);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 150.0);
        AnchorPane.setLeftAnchor(box, 100.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        Runnable sendInput = () -> {
            String text = inputField.getText().trim();
            if (isValidRemoveBatteriesInput(text)) {
                errorLabel.setVisible(false);
                viewFlow.handleInput(text);
                closePopup.run();
            } else {
                errorLabel.setVisible(true);
            }
        };

        inputField.setOnAction(e -> sendInput.run());
        confirmBtn.setOnAction(e -> sendInput.run());
    }

    /**
     * Validates if the input string is properly formatted for removing batteries
     * @param input the input string to validate
     * @return if the input string has the expected format and contains valid numerical values or not
     */
    private boolean isValidRemoveBatteriesInput(String input) {
        if (input == null || input.trim().isEmpty()) return false;

        String[] entries = input.split("\\s*,\\s*");
        for (String entry : entries) {
            String[] parts = entry.trim().split("\\s*-\\s*");
            if (parts.length != 2) return false;

            String[] coords = parts[0].trim().split("\\s+");
            if (coords.length != 2) return false;

            try {
                int x = Integer.parseInt(coords[0].trim());
                int y = Integer.parseInt(coords[1].trim());
                int qty = Integer.parseInt(parts[1].trim());

                if (x < 0 || y < 0 || qty < 0) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    /**
     * Updates the goods list with the provided list of goods to remove.
     * @param goodsList list of goods representing the goods to be updated in the list.
     */
    public void updateGoodsListToRemove(ArrayList<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    /**
     * Displays a popup window allowing the user to remove goods from a list
     */
    public void showRemoveGoodsInput() {
        Label chooseLabel = new Label("Select the goods you want to remove:");
        chooseLabel.setTextFill(Color.web("#FFDE69"));
        chooseLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 22));

        GridPane goodsGrid = new GridPane();
        goodsGrid.setAlignment(Pos.CENTER);
        goodsGrid.setHgap(30);
        goodsGrid.setVgap(12);

        ScrollPane scrollPane = new ScrollPane(goodsGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setFont(Font.font("Bangers", 16));
        confirmBtn.setTextFill(Color.BLACK);
        confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        confirmBtn.setCursor(Cursor.HAND);
        confirmBtn.setPrefWidth(160);

        VBox centerBox = new VBox(15, chooseLabel, scrollPane, confirmBtn);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setPadding(new Insets(0));

        AnchorPane popupContent = new AnchorPane();
        AnchorPane.setTopAnchor(centerBox, 620.0);
        AnchorPane.setLeftAnchor(centerBox, 100.0);

        popupContent.getChildren().add(centerBox);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        StringBuilder selectedIndexes = new StringBuilder();

        for (int i = 0; i < goodsList.size(); i++) {
            Goods g = goodsList.get(i);
            int x = g.getX();
            int y = g.getY();

            String color = g.getColor().toLowerCase();
            Image img = switch (color) {
                case "red" -> new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/redgood.png")).toExternalForm());
                case "yellow" -> new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/yellowgood.png")).toExternalForm());
                case "green" -> new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/greengood.png")).toExternalForm());
                case "blue" -> new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/bluegood.png")).toExternalForm());
                default -> null;
            };

            ImageView icon = new ImageView(img);
            icon.setFitWidth(35);
            icon.setPreserveRatio(true);

            Label coordLabel = new Label("at [" + x + "," + y + "]");
            coordLabel.setFont(Font.font("Bangers", 16));
            coordLabel.setTextFill(Color.WHITE);

            Button selectBtn = new Button("Remove");
            selectBtn.setFont(Font.font("Bangers", 13));
            selectBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            selectBtn.setTextFill(Color.BLACK);
            selectBtn.setCursor(Cursor.HAND);
            selectBtn.setPrefWidth(80);

            int finalI = i;
            selectBtn.setOnAction(e -> {
                selectBtn.setDisable(true);
                selectedIndexes.append(finalI).append(", ");
            });

            HBox line = new HBox(10, selectBtn, icon, coordLabel);
            line.setAlignment(Pos.CENTER_LEFT);

            int row = i / 2;
            int col = i % 2;
            goodsGrid.add(line, col, row);
        }

        confirmBtn.setOnAction(e -> {
            if (!selectedIndexes.isEmpty()) {
                String result = selectedIndexes.toString().replaceAll(",\\s*$", "");
                viewFlow.handleInput(result);
                closePopup.run();
            }
        });
    }

    /**
     * Displays a rearrange input popup to let user change good arrangement
     */
    public void showRearrangeInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label confirm = new Label("Would you like to rearrange a good?");
        confirm.setTextFill(Color.web("#FFDE69"));
        confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 25));

        Button yes = new Button("YES");
        yes.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        yes.setTextFill(Color.WHITE);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
        yes.setCursor(Cursor.HAND);
        yes.setPrefWidth(100);

        Button no = new Button("NO");
        no.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        no.setTextFill(Color.WHITE);
        no.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        no.setCursor(Cursor.HAND);
        no.setPrefWidth(100);

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER);
        box.getChildren().addAll(confirm, buttons);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 190.0);
        AnchorPane.setLeftAnchor(box, 105.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            closePopup.run();
        });

        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert the old index, the new index and the position of the good you want to rearrange");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
            insertLabel.setTextAlignment(TextAlignment.CENTER);
            insertLabel.setMaxWidth(500);
            insertLabel.setWrapText(true);

            Label hintLabel = new Label("Type '<rowOld> <colOld> - <posOld>, <rowNew> <colNew> - <posNew>'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 1 2 - 3, 5 6 - 1");
            inputField.setPrefWidth(250);

            Label errorLabel = new Label("Invalid format. Try: '<rowOld> <colOld> - <posOld>, <rowNew> <colNew> - <posNew>'");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            errorLabel.setVisible(false);

            Button confirmBtn = new Button("Confirm");
            confirmBtn.setFont(Font.font("Bangers", 14));
            confirmBtn.setTextFill(Color.BLACK);
            confirmBtn.setCursor(Cursor.HAND);
            confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

            Runnable sendInput = () -> {
                String text = inputField.getText().trim();
                if (isValidRearrangeInput(text)) {
                    errorLabel.setVisible(false);
                    viewFlow.handleInput("y, " + text);
                    closePopup.run();
                } else {
                    errorLabel.setVisible(true);
                }
            };

            inputField.setOnAction(e -> sendInput.run());
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);

            box.getChildren().addAll(insertLabel, hintLabel, inputBox, errorLabel);
            box.setTranslateX(-100);
            box.setTranslateY(70);
        });
    }

    /**
     * Validates the input string to ensure it adheres to a specific format
     * necessary for rearrangement operations
     * @param input the string to validate
     * @return if the input string meets the expected format and contains
     *         valid, non-negative integers for coordinates and values or not
     */
    private boolean isValidRearrangeInput(String input) {
        if (input == null || input.trim().isEmpty()) return false;

        String[] parts = input.split("\\s*,\\s*");
        if (parts.length != 2) return false;

        for (String part : parts) {
            String[] split = part.trim().split("\\s*-\\s*");
            if (split.length != 2) return false;

            String[] coords = split[0].trim().split("\\s+");
            if (coords.length != 2) return false;

            try {
                int x = Integer.parseInt(coords[0].trim());
                int y = Integer.parseInt(coords[1].trim());
                int val = Integer.parseInt(split[1].trim());

                if (x < 0 || y < 0 || val < 0) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Displays a popup that prompts the user to gain a specified number of cosmic credits
     * at the cost of a certain number of days
     */
    public void showCosmicCreditsInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label confirm = new Label("Gain " + viewFlow.getCosmicCreditsToGain() + " cosmic credits and lose " + viewFlow.getDaysLost() + " days?");
        confirm.setTextFill(Color.web("#FFDE69"));
        confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 25));
        confirm.setTextAlignment(TextAlignment.CENTER);
        confirm.setMaxWidth(350);
        confirm.setWrapText(true);

        Button yes = new Button("YES");
        yes.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        yes.setTextFill(Color.WHITE);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
        yes.setCursor(Cursor.HAND);
        yes.setPrefWidth(100);

        Button no = new Button("NO");
        no.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        no.setTextFill(Color.WHITE);
        no.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        no.setCursor(Cursor.HAND);
        no.setPrefWidth(100);

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER);

        box.getChildren().addAll(confirm, buttons);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 160.0);
        AnchorPane.setLeftAnchor(box, 115.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        yes.setOnAction(ev -> {
            viewFlow.handleInput("y");
            closePopup.run();
        });

        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            closePopup.run();
        });
    }

    /**
     * Displays a popup dialog that prompts the user to confirm if they want to withdraw
     */
    public void showAskWithdraw() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label title = new Label("Do you want to withdraw?");
        title.setTextFill(Color.web("#FFDE69"));
        title.setFont(Font.font("Bangers", FontWeight.BOLD, 25));

        Button yes = new Button("YES");
        yes.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        yes.setTextFill(Color.WHITE);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
        yes.setCursor(Cursor.HAND);
        yes.setPrefWidth(100);

        Button no = new Button("NO");
        no.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        no.setTextFill(Color.WHITE);
        no.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        no.setCursor(Cursor.HAND);
        no.setPrefWidth(100);

        Button confirm = new Button("Confirm");
        confirm.setFont(Font.font("Bangers", 16));
        confirm.setTextFill(Color.BLACK);
        confirm.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        confirm.setCursor(Cursor.HAND);
        confirm.setPrefWidth(100);
        confirm.setOpacity(0.5);

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER);

        VBox popupBox = new VBox(15, title, buttons, confirm);
        popupBox.setAlignment(Pos.CENTER);
        popupBox.setPadding(new Insets(15));

        AnchorPane popupContent = new AnchorPane(popupBox);
        AnchorPane.setBottomAnchor(popupBox, 140.0);
        AnchorPane.setLeftAnchor(popupBox, 150.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        AtomicReference<String> response = new AtomicReference<>(null);

        yes.setOnAction(e -> {
            response.set("y");
            confirm.setOpacity(1.0);
            yes.setOpacity(1.0);
            no.setOpacity(0.5);
        });

        no.setOnAction(e -> {
            response.set("n");
            confirm.setOpacity(1.0);
            no.setOpacity(1.0);
            yes.setOpacity(0.5);
        });

        confirm.setOnAction(e -> {
            if (response.get() == null) {
                return;
            }
            viewFlow.handleInput(response.get());
            closePopup.run();
        });
    }

    /**
     * Displays a popup window prompting the user to roll the dice
     */
    public void showRollDiceInput() {
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label diceLabel = new Label("🎲 Roll the dices!");
        diceLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 25));
        diceLabel.setTextFill(Color.web("#FFDE69"));

        Button rollButton = new Button("Roll Dices");
        rollButton.setFont(Font.font("Bangers", 18));
        rollButton.setTextFill(Color.BLACK);
        rollButton.setCursor(Cursor.HAND);
        rollButton.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

        box.getChildren().addAll(diceLabel, rollButton);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 180.0);
        AnchorPane.setLeftAnchor(box, 200.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        rollButton.setOnAction(e -> {
            viewFlow.handleInput("roll");
            closePopup.run();
        });
    }

    /**
     * Displays the result of rolling a dice using a graphical animation
     * @param result of the dice roll to be displayed after the animation
     */
    public void showRollDiceResult(int result) {
        isRollDiceOverlayVisible = true;
        Rectangle overlay = new Rectangle();
        overlay.setWidth(1600);
        overlay.setHeight(900);
        overlay.setFill(Color.rgb(0, 0, 0, 0.5));
        overlay.setMouseTransparent(false);

        Label diceLabel = new Label("🎲 Rolling...");
        diceLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 35));
        diceLabel.setTextFill(Color.web("#FFDE69"));

        Label resultLabel = new Label("");
        resultLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 60));
        resultLabel.setTextFill(Color.WHITE);

        VBox box = new VBox(20, diceLabel, resultLabel);
        box.setAlignment(Pos.CENTER);

        StackPane overlayPane = new StackPane(overlay, box);
        root.getChildren().add(overlayPane);

        Timeline rollAnimation = new Timeline();
        Random rand = new Random();

        for (int i = 0; i < 15; i++) {
            int delay = i * 100;
            rollAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(delay), e -> resultLabel.setText(String.valueOf(rand.nextInt(6) + 1))));
        }

        rollAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(1500), e -> resultLabel.setText(String.valueOf(result))));

        rollAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(4500), e -> {
            isRollDiceOverlayVisible = false;
            root.getChildren().remove(overlayPane);

            if (rollDiceResultBox != null) {
                root.getChildren().remove(rollDiceResultBox);
            }

            Label title = new Label("🎲 Dices result");
            title.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
            title.setTextFill(Color.web("#FFDE69"));

            Label finalValue = new Label(String.valueOf(result));
            finalValue.setFont(Font.font("Bangers", FontWeight.BOLD, 36));
            finalValue.setTextFill(Color.WHITE);

            VBox resultBox = new VBox(5, title, finalValue);
            resultBox.setAlignment(Pos.TOP_CENTER);
            resultBox.setPadding(new Insets(15));
            resultBox.setTranslateX(-430);
            resultBox.setMouseTransparent(true);

            StackPane.setAlignment(resultBox, Pos.TOP_CENTER);
            StackPane.setMargin(resultBox, new Insets(20));

            rollDiceResultBox = resultBox;
            root.getChildren().add(resultBox);
        }));

        rollAnimation.play();
    }

    /**
     * Displays a message on the screen with a title and content in a styled message box
     * positioned at the top-right corner
     * @param message the text content to be displayed in the message box
     */
    public void showMessage(String message) {
        Label title = new Label("New message:");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label content = new Label(message);
        content.setTextFill(Color.WHITE);
        content.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
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

            PauseTransition delay = new PauseTransition(Duration.seconds(6));
            delay.setOnFinished(e -> root.getChildren().remove(messageBox));
            delay.play();
        });
    }

    /**
     * Updates the track position rankings
     * @param truckers map of truckers' positions
     */
    public void updateTrack(Map<String, Integer> truckers) {
        if (scene == null || scene.getRoot() == null) return;

        StackPane root = (StackPane) scene.getRoot();

        root.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("trackBox"));

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(truckers.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int maxPosition = sorted.getFirst().getValue();

        VBox trackBox = new VBox(10);
        trackBox.setId("trackBox");
        trackBox.setAlignment(Pos.CENTER_LEFT);
        trackBox.setPadding(new Insets(10));
        trackBox.setMouseTransparent(true);

        Label title = new Label("Track Position");
        title.setFont(Font.font("Bangers", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#FFDE69"));
        trackBox.getChildren().add(title);

        for (int i = 0; i < sorted.size(); i++) {
            int pos = i + 1;
            String name = sorted.get(i).getKey();
            int current = sorted.get(i).getValue();

            Text posText = new Text(pos + "°  ");
            posText.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
            posText.setFill(Color.WHITE);

            String displayName = name.length() > 10 ? name.substring(0, 9) + "…" : name;
            Text nameText = new Text(displayName);
            nameText.setFont(Font.font("Bangers", FontWeight.NORMAL, 18));
            nameText.setFill(Color.WHITE);

            Text daysText = new Text();
            if (i > 0) {
                int diff = maxPosition - current;
                daysText.setText("  (+" + diff + " days)");
                daysText.setFont(Font.font("Bangers", FontWeight.NORMAL, 14));
                daysText.setFill(Color.WHITE);
            }

            TextFlow line = new TextFlow(posText, nameText, daysText);
            trackBox.getChildren().add(line);
        }

        StackPane.setAlignment(trackBox, Pos.CENTER_LEFT);
        StackPane.setMargin(trackBox, new Insets(0, 0, 0, 20));

        root.getChildren().add(trackBox);
    }

    /**
     * Updates the list of planets occupied
     * @param planets the list of the planets to be updated
     */
    public void updatePlanetsOccupied(ArrayList<Trucker> planets) {
        this.planets = planets;
    }

    /**
     * Displays a popup encouraging the user to take the big shot
     */
    public void showBigShot() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        Label message = new Label("Ready for the big shot?");
        message.setFont(Font.font("Bangers", FontWeight.BOLD, 22));
        message.setTextFill(Color.WHITE);
        message.setWrapText(true);
        message.setAlignment(Pos.CENTER);

        Button continueBtn = new Button("Continue");
        continueBtn.setFont(Font.font("Bangers", 18));
        continueBtn.setTextFill(Color.BLACK);
        continueBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        continueBtn.setCursor(Cursor.HAND);
        continueBtn.setPadding(new Insets(10, 20, 10, 20));

        container.getChildren().addAll(message, continueBtn);

        AnchorPane popupContent = new AnchorPane(container);
        AnchorPane.setBottomAnchor(container, 180.0);
        AnchorPane.setLeftAnchor(container, 185.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        continueBtn.setOnAction(e -> {
            viewFlow.handleInput("continue");
            closePopup.run();
        });
    }


    /**
     * Displays a wait overlay to indicate that the user needs to wait for other participants or processes
     */
    public void showWaitForOthers() {
        createWaitOverlayIfAbsent();

        Platform.runLater(() -> {
            if (!root.getChildren().contains(waitOverlay)) {
                root.getChildren().add(waitOverlay);
                if (waitAnimation != null) waitAnimation.play();
            }
        });
    }

    /**
     * Hides the "wait for others" overlay and stops any associated animations
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
     * Creates a wait overlay displaying a message indicating the application is waiting
     * for other truckers to finish
     */
    private void createWaitOverlayIfAbsent() {
        if (waitOverlay != null) return;

        Label message = new Label("Waiting for other truckers to finish");
        message.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 32));
        message.setTextFill(Color.web("#FFDE69"));

        Label dots = new Label(".");
        dots.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 32));
        dots.setTextFill(Color.web("#FFDE69"));

        HBox textBox = new HBox(5, message, dots);
        textBox.setAlignment(Pos.CENTER);

        Rectangle overlayBg = new Rectangle();
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
    }

    /**
     * Updates the current good image based on the provided good
     * @param good the good whose value is used to fetch and update the image
     */
    public void updateCurrGood(Goods good) {
        this.goodImage = goodsImagesGetter.getGoodImage(good.getValue());
    }

    /**
     * Updates the status bar with the latest information provided by the view flow
     */
    public void updateStatusBar() {
        posLabel.setText(": " + viewFlow.getPosition());
        battLabel.setText(": " + viewFlow.getBatteryCount());
        humanLabel.setText(": " + viewFlow.getHumanCount());
        cannonLabel.setText(": " + viewFlow.getCannonPower());
        engineLabel.setText(": " + viewFlow.getEnginePower());
        creditLabel.setText(": " + viewFlow.getCredits());
        lostLabel.setText(": " + viewFlow.getLostComponents());

        for (String color : goodLabels.keySet()) {
            int value = viewFlow.getGoodsByColor().getOrDefault(color, 0);
            goodLabels.get(color).setText(": " + value);
        }
    }

    /**
     * Creates and returns a styled Label with predefined font, size, and color
     * @return a styled Label
     */
    private Label createStyledLabel() {
        Label l = new Label();
        l.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        l.setTextFill(Color.WHITE);
        return l;
    }

    /**
     * Displays additional detailed information about the components on the scene
     * by overlaying the existing interface
     */
    private void showMore() {

        Image transparent = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/transparent.png")).toExternalForm());

        root.getChildren().removeIf(n -> "moreOverlay".equals(n.getId()));

        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.getScene().widthProperty());
        overlay.heightProperty().bind(root.getScene().heightProperty());
        overlay.setFill(Color.rgb(0, 0, 0, 0.7));

        Label title = new Label("Components details:");
        title.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 30));
        title.setTextAlignment(TextAlignment.LEFT);
        title.setTextFill(Color.web("#FFDE69"));

        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        VBox textBox = new VBox(10);
        textBox.setAlignment(Pos.TOP_LEFT);

        Trucker t = new Trucker("n", PlayerColors.BLUE);
        t.initSpaceShip(2);
        Component[][] layout = viewFlow.getShip().getComponents();
        StringBuilder desc = new StringBuilder();
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                Component c = layout[i][j];
                if (c != null && !formatComponent(c, i, j).isBlank()) {
                    switch(c) {
                        case Load ignored -> desc.append(formatComponent(c, i, j)).append("\n");
                        case BatteryStorage ignored -> desc.append(formatComponent(c, i, j)).append("\n");
                        case Cabin ignored -> desc.append(formatComponent(c, i, j)).append("\n");
                        case Cannon ignored -> desc.append(formatComponent(c, i, j)).append("\n");
                        case Engine ignored -> desc.append(formatComponent(c, i, j)).append("\n");
                        case Shield ignored -> desc.append(formatComponent(c, i, j)).append("\n");
                        default -> {}
                    }
                }
            }
        }
        Label l = new Label(desc.toString());
        l.setTextFill(Color.WHITE);
        l.setFont(Font.font("Bangers", 18));
        l.setTextAlignment(TextAlignment.LEFT);
        l.setWrapText(true);
        textBox.getChildren().add(l);

        GridPane overlayGrid = new GridPane();
        overlayGrid.setHgap(2);
        overlayGrid.setVgap(2);
        overlayGrid.setAlignment(Pos.TOP_RIGHT);
        overlayGrid.setTranslateX(-57);
        overlayGrid.setTranslateY(74);

        for (int row = 5; row < 10; row++) {
            for (int col = 4; col < 11; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(107, 107);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 6px; -fx-background-radius: 6px;");
                    Component c = layout[row][col];
                    if (c != null) {
                        ImageView imageView = new ImageView(c.getFrontImage());
                        imageView.setFitWidth(107);
                        imageView.setFitHeight(107);
                        Rectangle clip = new Rectangle(107, 107);
                        clip.setArcWidth(12);
                        clip.setArcHeight(12);
                        imageView.setClip(clip);
                        imageView.setRotate(c.getRotationAngle());
                        cell.getChildren().add(imageView);
                    } else {
                        ImageView imageView = new ImageView(transparent);
                        imageView.setFitWidth(107);
                        imageView.setFitHeight(107);
                        Rectangle clip = new Rectangle(107, 107);
                        clip.setArcWidth(12);
                        clip.setArcHeight(12);
                        imageView.setClip(clip);
                        cell.getChildren().add(imageView);
                    }
                } else {
                    cell.setStyle("-fx-background-color: transparent;");
                }

                overlayGrid.add(cell, col - 4, row - 5);
            }
        }

        VBox descriptionBox = new VBox(20, titleBox, textBox);
        descriptionBox.setMaxWidth(600);
        descriptionBox.setAlignment(Pos.TOP_CENTER);
        descriptionBox.setPadding(new Insets(20));

        ScrollPane scroll = new ScrollPane(descriptionBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scroll.setPadding(new Insets(20));
        scroll.setMaxWidth(600);
        scroll.setMaxHeight(500);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        StackPane overlayContainer = new StackPane(overlay, overlayGrid, scroll);
        overlayContainer.setId("moreOverlay");
        StackPane.setAlignment(scroll, Pos.CENTER_LEFT);
        StackPane.setMargin(scroll, new Insets(0, 0, 0, 120));
        StackPane.setAlignment(overlayGrid, Pos.TOP_RIGHT);

        root.setOnMouseClicked(e -> root.getChildren().remove(overlayContainer));
        scroll.setOnMouseClicked(Event::consume);

        root.getChildren().add(overlayContainer);
    }

    /**
     * Formats and returns a detailed description of a given component based on its type
     * and other specific properties
     * @param c the component
     * @param i the row index for the component's position
     * @param j the column index for the component's position
     * @return a formatted string describing the component
     */
    private String formatComponent(Component c, int i, int j) {
        if (c == null) return "";

        StringBuilder details = new StringBuilder();
        switch (c) {
            case Load load -> {
                if (load.getIsSpecial()) {
                    details.append("[").append(i).append(",").append(j).append("] SPECIAL LOAD ");
                    if (!load.isEmpty()) {
                        details.append(" contains: ");
                        int count = 0;
                        for (Goods g : load.getGoods()) {
                            if(g.getValue() == -1) {
                                details.append(g.getColor()).append(" {").append(count).append("}");
                            } else {
                                details.append("empty {").append(count).append("}");
                            }
                            if (count < load.getGoods().size()) {
                                details.append(", ");
                            }
                            count++;
                        }
                    } else {
                        details.append(" is empty");
                    }
                } else {
                    details.append("[").append(i).append(",").append(j).append("] LOAD ");
                    if (!load.isEmpty()) {
                        details.append(" contains: ");
                        int count = 0;
                        for (Goods g : load.getGoods()) {
                            if(g.getValue() == -1) {
                                details.append(g.getColor()).append(" {").append(count).append("}");
                            } else {
                                details.append("empty {").append(count).append("}");
                            }
                            if (count < load.getGoods().size()) {
                                details.append(", ");
                            }
                            count++;
                        }
                    } else {
                        details.append(" is empty");
                    }
                }
            }
            case BatteryStorage battery -> {
                details.append("[").append(i).append(",").append(j).append("] BATTERY STORAGE ");
                if(battery.getQuantity() != 0) {
                    details.append(" contains ").append(battery.getQuantity()).append(battery.getQuantity() == 1 ? " battery" : " batteries");
                } else {
                    details.append(" is empty");
                }
            }
            case Cabin cabin -> {
                details.append("[").append(i).append(",").append(j).append("] CABIN");
                if(cabin.getQuantity() != 0){
                    if (cabin.getAlien() != null) {
                        details.append(" contains 1 ").append(cabin.getAlien().getColor()).append(" alien");
                    } else {
                        details.append(" contains ").append(cabin.getQuantity()).append(cabin.getQuantity() == 1 ? " human" : " humans");
                    }
                } else {
                    details.append(" is empty");
                }
            }
            case Cannon cannon -> {
                if(cannon.getIsDouble()) {
                    if(cannon.getIsActive()) {
                        details.append("[").append(i).append(",").append(j).append("] DOUBLE CANNON is active");
                    } else {
                        details.append("[").append(i).append(",").append(j).append("] DOUBLE CANNON is inactive");
                    }
                }
            }
            case Engine engine -> {
                if(engine.getIsDouble()) {
                    if(engine.getIsActive()) {
                        details.append("[").append(i).append(",").append(j).append("] DOUBLE ENGINE is active");
                    } else {
                        details.append("[").append(i).append(",").append(j).append("] DOUBLE ENGINE is inactive");
                    }
                }
            }
            case Shield shield -> {
                if(shield.getIsActive()) {
                    details.append("[").append(i).append(",").append(j).append("] SHIELD is active");
                } else {
                    details.append("[").append(i).append(",").append(j).append("] SHIELD is inactive");
                }
            }
            default -> {}
        }

        return details.toString();
    }

    /**
     * Clears the result of the dice roll by removing the result display box
     * from the parent container and resetting its reference to null
     */
    public void clearRollDiceResult() {
        if (rollDiceResultBox != null) {
            root.getChildren().remove(rollDiceResultBox);
            rollDiceResultBox = null;
        }
    }

    /**
     * Displays an error message as a popup overlay on the screen with a specified message
     * @param message the error message to be displayed in the popup
     */
    public void showError(String message) {
        double width = 1600;
        double height = 900;

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
     * Displays the remaining number of cards
     * @param remainingCards the number of cards left to be displayed
     */
    public void showRemainingCards(int remainingCards) {
        if(remainingcardsBox != null) {
            remainingcardsBox.setOpacity(0);
        }

        Label label = new Label("Remaining\ncards");
        label.setFont(Font.font("Bangers", FontWeight.BOLD, 24));
        label.setTextFill(Color.web("#FFDE69"));
        label.setTextAlignment(TextAlignment.CENTER);

        Label count = new Label(String.valueOf(remainingCards));
        count.setFont(Font.font("Bangers", FontWeight.BOLD, 30));
        count.setTextFill(Color.WHITE);
        count.setTextAlignment(TextAlignment.CENTER);

        VBox remainingCardsBox = new VBox(5, label, count);
        remainingCardsBox.setAlignment(Pos.CENTER);
        remainingCardsBox.setPadding(new Insets(10));
        remainingCardsBox.setMaxWidth(Region.USE_PREF_SIZE);
        remainingCardsBox.setMaxHeight(Region.USE_PREF_SIZE);

        Platform.runLater(() -> {
            StackPane.setAlignment(remainingCardsBox, Pos.CENTER);
            StackPane.setMargin(remainingCardsBox, new Insets(-430, 0, 0, -840));
            root.getChildren().add(remainingCardsBox);
        });
    }
}
