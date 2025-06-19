package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import java.util.function.Consumer;

public class FlightScene {
    private final String nickname;
    private Component[][] layout;
    private final boolean[][] validCells;
    private final StackPane[][] cellMatrix;
    private Scene scene;
    private GridPane grid;
    private final StackPane root;
    private final ViewFlow viewFlow;
    private Button rulesBtn;
    private Button otherBtn;
    private Button withdrawButton;
    private Button moreButton;

    private StackPane cardImageContainer;

    private VBox optionsBox;

    private GoodsImagesGetter goodsImagesGetter;
    private Image goodImage;
    private ArrayList<Trucker> planets;

    private Label posLabel, battLabel, humanLabel, cannonLabel, engineLabel, creditLabel;
    private Map<String, Label> goodLabels = new HashMap<>();
    private VBox statusBar;

    private final StackPane overlayLayer = new StackPane(); // sopra tutto


    private StackPane waitOverlay;
    private Timeline waitAnimation;

    /**
     * constructor of the flight scene
     * @param nickname name of the trucker
     * @param layout matrix of the ship
     * @param viewFlow logic of the game
     * @param root principal pane of the scene
     */
    public FlightScene(String nickname, Component[][] layout, ViewFlow viewFlow, StackPane root) {
        this.nickname = nickname;
        this.layout = layout;
        this.root = root;
        this.viewFlow = viewFlow;
        this.cellMatrix = new StackPane[10][11];
        this.validCells = new boolean[11][12];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Component c = layout[i][j];
                validCells[i][j] = c == null || !c.getType().equals("Empty Space");
            }
        }
        this.goodsImagesGetter = new GoodsImagesGetter();
    }

    /**
     * create the scene in its principal elements
     * @param playersNames list of players' name
     * @param level level of the game
     * @return the actual scene to show
     */
    public Scene createScene(ArrayList<String> playersNames, int level) {
        double width = 1600;
        double height = 900;

        Image backgroundImage;
        if (level == 0) {
            backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-2.jpeg").toExternalForm());
        } else {
            backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-2b.jpeg").toExternalForm());
        }

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(true);
        backgroundView.setMouseTransparent(true);

        root.getChildren().clear();
        root.getChildren().addAll(
                backgroundView,
                overlayLayer // container per popup/overlay (inizialmente vuoto)
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
        addWithdrawButtonToScene(viewFlow, styleButton);
        attachOptionsBoxToScene(scene);

        return scene;
    }

    public void setShip(Component[][] ship){
        this.layout = ship;
    }

    private void restoreInteractiveLayers() {
        overlayLayer.setMouseTransparent(true);
        root.getChildren().removeAll(optionsBox, statusBar);
        root.getChildren().addAll(optionsBox, statusBar);
    }

    public void printNodeTree(Node node, int level) {
        String indent = "  ".repeat(level);
        System.out.println(indent + "- " + node + " | id: " + node.getId() + " | class: " + node.getClass().getSimpleName());

        if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                printNodeTree(child, level + 1);
            }
        }
    }


    /**
     * box with the options button of the game
     * @param scene scene where to add the buttons
     */
    private void attachOptionsBoxToScene(Scene scene) {
        optionsBox = new VBox(10);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.TOP_LEFT);
        optionsBox.getChildren().addAll(otherBtn, rulesBtn, withdrawButton);
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
     * style used equal for every button in the scene
     * @return the button
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
     * show the general status of the ship and the possibility to see the specific component status
     */
    private void initializeStatusBar() {
        Image engine = new Image(getClass().getResource("/it/polimi/ingsw/view/engine.png").toExternalForm());
        Image cannon = new Image(getClass().getResource("/it/polimi/ingsw/view/cannon.png").toExternalForm());
        Image human = new Image(getClass().getResource("/it/polimi/ingsw/view/human.png").toExternalForm());
        Image battery = engine; // Usa immagine engine per battery
        Image pos = engine;     // Usa immagine engine per position
        Image credit = engine;  // Usa immagine engine per credits

        posLabel = createStyledLabel();
        battLabel = createStyledLabel();
        humanLabel = createStyledLabel();
        cannonLabel = createStyledLabel();
        engineLabel = createStyledLabel();
        creditLabel = createStyledLabel();

        ImageView posIcon = new ImageView(pos);
        posIcon.setFitWidth(20);
        posIcon.setPreserveRatio(true);

        ImageView batteryIcon = new ImageView(battery);
        batteryIcon.setFitWidth(20);
        batteryIcon.setPreserveRatio(true);

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
        creditIcon.setFitWidth(16);
        creditIcon.setPreserveRatio(true);

        HBox posBox = new HBox(0, posIcon, posLabel);
        HBox battBox = new HBox(0, batteryIcon, battLabel);
        HBox humanBox = new HBox(0, humanIcon, humanLabel);
        HBox cannonBox = new HBox(0, cannonIcon, cannonLabel);
        HBox engineBox = new HBox(5, engineIcon, engineLabel);
        HBox creditBox = new HBox(5, creditIcon, creditLabel);


        for (HBox box : List.of(posBox, battBox, humanBox, cannonBox, engineBox, creditBox)) {
            box.setAlignment(Pos.CENTER_RIGHT);
        }

        // Goods
        HBox goodsBox = new HBox(10);
        goodsBox.setAlignment(Pos.CENTER_RIGHT);
        Map<String, Image> goodImages = Map.of(
                "red", new Image(getClass().getResource("/it/polimi/ingsw/view/redgood.png").toExternalForm()),
                "yellow", new Image(getClass().getResource("/it/polimi/ingsw/view/yellowgood.png").toExternalForm()),
                "green", new Image(getClass().getResource("/it/polimi/ingsw/view/greengood.png").toExternalForm()),
                "blue", new Image(getClass().getResource("/it/polimi/ingsw/view/bluegood.png").toExternalForm())
        );

        for (String color : goodImages.keySet()) {
            ImageView goodIcon = new ImageView(goodImages.get(color));
            goodIcon.setFitWidth(35);
            goodIcon.setPreserveRatio(true);
            Label qtyLabel = createStyledLabel();
            goodLabels.put(color.toLowerCase(), qtyLabel);
            HBox singleGood = new HBox(0, goodIcon, qtyLabel);
            singleGood.setAlignment(Pos.CENTER_RIGHT);
            goodsBox.getChildren().add(singleGood);
        }

        moreButton = new Button("More...");
        moreButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        moreButton.setTextFill(Color.BLACK);
        moreButton.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        moreButton.setCursor(Cursor.HAND);
        moreButton.setOnAction(e -> showMore());

        HBox resourceBox = new HBox(20, posBox, battBox, humanBox, cannonBox, engineBox, goodsBox, creditBox);
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
     * specific button for the withdraw of the player
     * @param viewFlow to call the handleInput
     * @param styleButton the style to use to make all buttons equal
     */
    private void addWithdrawButtonToScene(ViewFlow viewFlow, Consumer<Button> styleButton) {
        Button withdrawButton = new Button("Withdraw");
        styleButton.accept(withdrawButton);

        withdrawButton.setOnAction(e -> {
            StackPane root = (StackPane) scene.getRoot();

            Rectangle bg = new Rectangle();
            bg.widthProperty().bind(scene.widthProperty());
            bg.heightProperty().bind(scene.heightProperty());
            bg.setFill(Color.rgb(0, 0, 0, 0.4));

            VBox box = new VBox(20);
            box.setAlignment(Pos.CENTER);

            Label confirm = new Label("Confirm to withdraw the flight?");
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
                viewFlow.handleInput("withdraw");
                root.getChildren().remove(confirmation);
            });

            no.setOnAction(ev -> root.getChildren().remove(confirmation));

            HBox buttons = new HBox(20, yes, no);
            buttons.setAlignment(Pos.CENTER);

            box.getChildren().addAll(confirm, buttons);

            root.getChildren().add(confirmation);
        });

        this.withdrawButton = withdrawButton;
    }

    /**
     * button to open rules
     * @param styleButton to make all the buttons be equal
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
     * to see other players ship
     * @param viewFlow to call handleInput
     * @param styleButton to make all the buttons equal
     * @param playersNames list of all players' name
     */
    private void addShowOtherButtonToScene(ViewFlow viewFlow, Consumer<Button> styleButton, ArrayList<String> playersNames) {
        Button otherBtn = new Button("Show others ship");
        styleButton.accept(otherBtn);

        otherBtn.setOnAction(e -> {
            VBox nicknameChoices = new VBox(15);
            nicknameChoices.setAlignment(Pos.CENTER);

            for (String nickname : playersNames) {
                Button nickBtn = new Button(nickname);
                styleButton.accept(nickBtn);
                nickBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 18));

                nickBtn.setOnAction(ev -> {
                    viewFlow.handleInput("ship " + nickname);
                    //TODO aggiungere che una volta premuto si mostrino le navi degli altri
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

    public void showCurrContidion(Condition condition) {
        //TODO
    }


    public Scene getScene() {
        return scene;
    }

    /**
     * button to start the flight, equals to 'state' in TUI
     */
    public void showStateInput() {
        viewFlow.handleInput("state");
    }

    /**
     * used to create the first ship matrix, but also to modify it based on what happened during flight
     */
    public void updateShipLayout() {
        // se grid esiste già, la rimuovo dal root
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
     * Inserisce l'immagine della carta nel contenitore già esistente.
     * @param card Carta da visualizzare
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

        // Conserva il bordo esistente (primo elemento) e aggiungi la carta sopra
        Node border = cardImageContainer.getChildren().isEmpty() ? null : cardImageContainer.getChildren().get(0);
        cardImageContainer.getChildren().setAll(border, cardView);

        restoreInteractiveLayers();
    }

    /**
     * ask if you want to land on an abandoned card
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

        // Attiva overlay per disabilitare temporaneamente i layer interattivi
        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        // Gestione click dei pulsanti
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
     * Ask if you want to activate engines and then ask for the coordinates.
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

        // Attiva overlay
        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        // Funzione per chiudere e ripristinare interattività
        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        // "NO" => invia n
        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            closePopup.run();
        });

        // "YES" => chiede coordinate
        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert the coordinates of double engines and battery storages");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));

            Label hintLabel = new Label("Type '<x1> <y1>, <x2> <y2>, ..., battery, <xs1> <ys1> - <num>, ...'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 2 3, 4 5, battery, 6 7 - 2");
            inputField.setPrefWidth(250);

            Button confirmBtn = new Button("Confirm");
            confirmBtn.setFont(Font.font("Bangers", 14));
            confirmBtn.setTextFill(Color.BLACK);
            confirmBtn.setCursor(Cursor.HAND);
            confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

            Runnable sendInput = () -> {
                String text = inputField.getText().trim();
                if (!text.isEmpty()) {
                    viewFlow.handleInput("y, " + text);
                    closePopup.run();
                }
            };

            inputField.setOnAction(e -> sendInput.run());
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);

            box.getChildren().addAll(insertLabel, hintLabel, inputBox);
            box.setTranslateX(-20);
        });
    }

    /**
     * Ask if you want to activate cannons and then ask for the coordinates.
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

        // Attiva overlay
        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        // Chiudi e ripristina interattività
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

            Label hintLabel = new Label("Type '<x1> <y1>, <x2> <y2>, ..., battery, <xs1> <ys1> - <num>, ...'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 2 3, 4 5, battery, 6 7 - 2");
            inputField.setPrefWidth(250);

            Button confirmBtn = new Button("Confirm");
            confirmBtn.setFont(Font.font("Bangers", 14));
            confirmBtn.setTextFill(Color.BLACK);
            confirmBtn.setCursor(Cursor.HAND);
            confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

            Runnable sendInput = () -> {
                String text = inputField.getText().trim();
                if (!text.isEmpty()) {
                    viewFlow.handleInput("y, " + text);
                    closePopup.run();
                }
            };

            inputField.setOnAction(e -> sendInput.run());
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);

            box.getChildren().addAll(insertLabel, hintLabel, inputBox);
            box.setTranslateX(-20);
        });
    }

    /**
     * Ask if you want to activate shields and then ask for the coordinates.
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

        // Attiva overlay e mostra il contenuto
        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        // Funzione di chiusura del popup e ripristino
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

            Label hintLabel = new Label("Type '<x> <y>, <x> <y>'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 2 3, 4 5");
            inputField.setPrefWidth(250);

            Button confirmBtn = new Button("Confirm");
            confirmBtn.setFont(Font.font("Bangers", 14));
            confirmBtn.setTextFill(Color.BLACK);
            confirmBtn.setCursor(Cursor.HAND);
            confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

            Runnable sendInput = () -> {
                String text = inputField.getText().trim();
                if (!text.isEmpty()) {
                    viewFlow.handleInput("y, " + text);
                    closePopup.run();
                }
            };

            inputField.setOnAction(e -> sendInput.run());
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);

            box.getChildren().addAll(insertLabel, hintLabel, inputBox);
            box.setTranslateX(-50);
        });
    }

    /**
     * Ask from where to remove humans.
     */
    public void showHumanCoordInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.setMaxWidth(400);

        Label insertLabel = new Label("Insert the coordinates of the cabin from which to remove the crew members");
        insertLabel.setTextFill(Color.web("#FFDE69"));
        insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        insertLabel.setTextAlignment(TextAlignment.CENTER);
        insertLabel.setWrapText(true);

        Label hintLabel = new Label("Type '<x> <y>, <x> <y>'");
        hintLabel.setTextFill(Color.LIGHTGRAY);
        hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

        TextField inputField = new TextField();
        inputField.setPromptText("Ex. 2 3, 4 5");
        inputField.setPrefWidth(250);

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setFont(Font.font("Bangers", 14));
        confirmBtn.setTextFill(Color.BLACK);
        confirmBtn.setCursor(Cursor.HAND);
        confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

        HBox inputBox = new HBox(10, inputField, confirmBtn);
        inputBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(insertLabel, hintLabel, inputBox);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 150.0);
        AnchorPane.setLeftAnchor(box, 100.0);

        // Mostra overlay per bloccare interazione
        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        // Funzione di chiusura
        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        // Azione invio testo
        Runnable sendInput = () -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                viewFlow.handleInput("y, " + text);
                closePopup.run();
            }
        };

        inputField.setOnAction(e -> sendInput.run());
        confirmBtn.setOnAction(e -> sendInput.run());
    }

    /**
     * Show the good and ask if you want to gain it and where.
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

        // Blocca gli altri layer temporaneamente
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

            Label xLabel = new Label("X:");
            xLabel.setTextFill(Color.web("#FFDE69"));
            xLabel.setFont(Font.font("Bangers", 15));

            Label yLabel = new Label("Y:");
            yLabel.setTextFill(Color.web("#FFDE69"));
            yLabel.setFont(Font.font("Bangers", 15));

            Label posLabel = new Label("Position:");
            posLabel.setTextFill(Color.web("#FFDE69"));
            posLabel.setFont(Font.font("Bangers", 15));

            TextField xField = new TextField();
            xField.setPromptText("x");
            xField.setPrefWidth(50);
            xField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) xField.setText(newVal.replaceAll("[^\\d]", ""));
            });

            TextField yField = new TextField();
            yField.setPromptText("y");
            yField.setPrefWidth(50);
            yField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) yField.setText(newVal.replaceAll("[^\\d]", ""));
            });

            TextField posField = new TextField();
            posField.setPromptText("pos");
            posField.setPrefWidth(50);
            posField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) posField.setText(newVal.replaceAll("[^\\d]", ""));
            });

            HBox inputBox = new HBox(10, xLabel, xField, yLabel, yField, posLabel, posField);
            inputBox.setAlignment(Pos.CENTER);

            Label errorLabel = new Label("Invalid coordinates or position");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(false);

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

                try {
                    int x = Integer.parseInt(xText);
                    int y = Integer.parseInt(yText);
                    int pos = Integer.parseInt(posText);

                    if (x < 0 || y < 0 || pos < 0) {
                        errorLabel.setVisible(true);
                        return;
                    }

                    errorLabel.setVisible(false);
                    viewFlow.handleInput("y, " + x + " " + y + ", " + pos);
                    closePopup.run();
                } catch (NumberFormatException ex) {
                    errorLabel.setVisible(true);
                }
            });

            posField.setOnAction(e -> sendBtn.fire());
        });
    }

    /**
     * Make player decide on which planet to land.
     */
    public void showPlanetDecisionInput() {
        VBox mainBox = new VBox(15);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(10));

        Label confirm = new Label("Would you like to land on a planet?");
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

        mainBox.getChildren().addAll(confirm, buttons);

        AnchorPane popupContent = new AnchorPane(mainBox);
        AnchorPane.setBottomAnchor(mainBox, 180.0);
        AnchorPane.setLeftAnchor(mainBox, 110.0);

        // Applica overlay
        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        no.setOnAction(ev -> {
            viewFlow.handleInput("-1");
            closePopup.run();
        });

        yes.setOnAction(ev -> {
            mainBox.getChildren().clear();

            Label chooseLabel = new Label("Choose a planet to land on:");
            chooseLabel.setTextFill(Color.web("#FFDE69"));
            chooseLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 22));

            VBox planetListBox = new VBox(8);
            planetListBox.setAlignment(Pos.CENTER);

            for (int i = 0; i < planets.size(); i++) {
                Trucker t = planets.get(i);
                String status = (t == null) ? "free" : t.getNickname();

                Text planetLabel = new Text("Planet " + (i + 1) + ": ");
                planetLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
                planetLabel.setFill(Color.WHITE);

                Text statusText = new Text(status);
                statusText.setFont(Font.font("Bangers", FontWeight.NORMAL, 16));
                statusText.setFill(t == null ? Color.LIGHTGREEN : Color.LIGHTCORAL);

                Button selectBtn = new Button("Land");
                selectBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 13));
                selectBtn.setTextFill(Color.BLACK);
                selectBtn.setStyle("-fx-background-color: #FFDE69;");
                selectBtn.setCursor(Cursor.HAND);
                selectBtn.setPrefWidth(80);
                selectBtn.setDisable(t != null); // disable if occupied

                int finalI = i;
                selectBtn.setOnAction(e -> {
                    viewFlow.handleInput(String.valueOf(finalI));
                    closePopup.run();
                });

                HBox line = new HBox(10, selectBtn, planetLabel, statusText);
                line.setAlignment(Pos.CENTER);
                planetListBox.getChildren().add(line);
            }

            mainBox.getChildren().addAll(chooseLabel, planetListBox);
            mainBox.setTranslateX(55);
            mainBox.setTranslateY(50);
        });
    }
    /**
     * Show the button to draw the next card with blocking overlay.
     */
    public void showDrawCardInput() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10)); // evita padding negativo, usa ancore

        Button continueBtn = new Button("Draw next card");
        continueBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
        continueBtn.setTextFill(Color.BLACK);
        continueBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        continueBtn.setCursor(Cursor.HAND);
        continueBtn.setPadding(new Insets(10, 20, 10, 20));

        container.getChildren().add(continueBtn);

        AnchorPane popupContent = new AnchorPane(container);
        AnchorPane.setBottomAnchor(container, 150.0);
        AnchorPane.setLeftAnchor(container, 100.0);

        // Abilita overlay (blocca il mouse dietro)
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
     * Show the button to start the warzone card with blocking overlay.
     */
    public void showWarzoneInput() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10)); // padding standard, niente negativo

        Button continueBtn = new Button("Start warzone");
        continueBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
        continueBtn.setTextFill(Color.BLACK);
        continueBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        continueBtn.setCursor(Cursor.HAND);
        continueBtn.setPadding(new Insets(10, 20, 10, 20));

        container.getChildren().add(continueBtn);

        AnchorPane popupContent = new AnchorPane(container);
        AnchorPane.setBottomAnchor(container, 150.0); // regola la posizione come preferisci
        AnchorPane.setLeftAnchor(container, 100.0);

        // Abilita overlay per bloccare interazioni sotto
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



    // DA SISTEMARE CON LA NUOVA LOGICA PER GESTIRE OVERLAY

    /**
     * confirmation of the remotion and button to continue the game
     */
    public void showRemotionConfirmedInput() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER); // scritta + bottone centrati tra loro
        container.setPadding(new Insets(0, 0, -450, -840)); // margine dal basso

        Label title = new Label("Remotion confirmed");
        title.setFont(Font.font("Bangers", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#FFDE69"));

        Button continueBtn = new Button("Continue");
        continueBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
        continueBtn.setTextFill(Color.BLACK);
        continueBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        continueBtn.setCursor(Cursor.HAND);
        continueBtn.setPadding(new Insets(10, 20, 10, 20));

        continueBtn.setOnAction(e -> {
            viewFlow.handleInput("continue");
            root.getChildren().remove(container);
        });

        container.getChildren().addAll(title, continueBtn);

        StackPane.setAlignment(container, Pos.CENTER); // posizionato in basso, ma centrato
        root.getChildren().add(container);
    }

    /**
     * asks for coordinates from where to remove batteries
     */
    public void showRemoveBatteriesInput() {
        StackPane root = (StackPane) scene.getRoot();

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        AnchorPane anchorPane = new AnchorPane(box);
        anchorPane.setPickOnBounds(false); // lascia passare i click al di fuori del box
        AnchorPane.setBottomAnchor(box, 170.0);
        AnchorPane.setLeftAnchor(box, 60.0);

        box.getChildren().clear();

        Label insertLabel = new Label("Insert the coordinates of the batteries you want to remove");
        insertLabel.setTextFill(Color.web("#FFDE69"));
        insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        insertLabel.setTextAlignment(TextAlignment.CENTER);
        insertLabel.setWrapText(true);

        Label hintLabel = new Label("Type '<x> <y> - <quantity>'");
        hintLabel.setTextFill(Color.LIGHTGRAY);
        hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

        TextField inputField = new TextField();
        inputField.setPromptText("Ex. 2 3, 1");
        inputField.setPrefWidth(250);

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setFont(Font.font("Bangers", 14));
        confirmBtn.setTextFill(Color.BLACK);
        confirmBtn.setCursor(Cursor.HAND);
        confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

        // Azione comune per invio
        Runnable sendInput = () -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                viewFlow.handleInput(text);
                root.getChildren().remove(anchorPane);
            }
        };

        // ENTER sulla TextField
        inputField.setOnAction(e -> sendInput.run());

        // Clic su Confirm
        confirmBtn.setOnAction(e -> sendInput.run());

        HBox inputBox = new HBox(10, inputField, confirmBtn);
        inputBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(insertLabel, hintLabel, inputBox);

        root.getChildren().add(anchorPane);
    }

    /**
     * ask from where to remove the good
     */
    public void showRemoveGoodsInput() {
        StackPane root = (StackPane) scene.getRoot();

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        AnchorPane anchorPane = new AnchorPane(box);
        anchorPane.setPickOnBounds(false); // lascia passare i click al di fuori del box
        AnchorPane.setBottomAnchor(box, 170.0);
        AnchorPane.setLeftAnchor(box, 90.0);

        box.getChildren().clear();

        Label insertLabel = new Label("Insert the index of the good you want to remove");
        insertLabel.setTextFill(Color.web("#FFDE69"));
        insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        insertLabel.setTextAlignment(TextAlignment.CENTER);
        insertLabel.setWrapText(true);

        Label hintLabel = new Label("Type '<index>'");
        hintLabel.setTextFill(Color.LIGHTGRAY);
        hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

        TextField inputField = new TextField();
        inputField.setPromptText("Ex. 2");
        inputField.setPrefWidth(200);

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setFont(Font.font("Bangers", 14));
        confirmBtn.setTextFill(Color.BLACK);
        confirmBtn.setCursor(Cursor.HAND);
        confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

        // Azione comune per invio
        Runnable sendInput = () -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                viewFlow.handleInput(text);
                root.getChildren().remove(anchorPane);
            }
        };

        // ENTER sulla TextField
        inputField.setOnAction(e -> sendInput.run());

        // Clic su Confirm
        confirmBtn.setOnAction(e -> sendInput.run());

        HBox inputBox = new HBox(10, inputField, confirmBtn);
        inputBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(insertLabel, hintLabel, inputBox);

        root.getChildren().add(anchorPane);
    }

    /**
     * let the player the possibility to rearrange goods
     */
    public void showRearrangeInput() {
        StackPane root = (StackPane) scene.getRoot();

        // VBox per contenere il messaggio e i pulsanti
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

        // Posiziona VBox in basso a sinistra usando un AnchorPane
        AnchorPane anchorPane = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 180.0);
        AnchorPane.setLeftAnchor(box, 105.0);

        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert the old index, the new index and the position of the good you want to rearrange");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
            insertLabel.setTextAlignment(TextAlignment.CENTER);
            insertLabel.setMaxWidth(500);
            insertLabel.setWrapText(true);

            Label hintLabel = new Label("Type '<x1> <y1> - <value1>, <x2> <y2> - <value2>'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 1 2 - 3, 5 6 - 1");
            inputField.setPrefWidth(250);

            Button confirmBtn = new Button("Confirm");
            confirmBtn.setFont(Font.font("Bangers", 14));
            confirmBtn.setTextFill(Color.BLACK);
            confirmBtn.setCursor(Cursor.HAND);
            confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

            // Azione comune per invio
            Runnable sendInput = () -> {
                String text = inputField.getText().trim();
                if (!text.isEmpty()) {
                    viewFlow.handleInput("y, " + text);
                    root.getChildren().remove(anchorPane);
                }
            };

            // ENTER sulla TextField
            inputField.setOnAction(e -> sendInput.run());

            // Clic su Confirm
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);

            box.getChildren().addAll(insertLabel, hintLabel, inputBox);
            box.setTranslateX(-50);
            box.setTranslateY(40);

        });

        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            root.getChildren().remove(anchorPane);
        });

        root.getChildren().add(anchorPane);
    }

    /**
     * asks if player want to gain the cosmic credits
     */
    public void showCosmicCreditsInput() {
        StackPane root = (StackPane) scene.getRoot();

        // VBox per contenere il messaggio e i pulsanti
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label confirm = new Label("Would you like to gain cosmic credits and lose days?");
        confirm.setTextFill(Color.web("#FFDE69"));
        confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 25));
        confirm.setTextAlignment(TextAlignment.CENTER);
        confirm.setMaxWidth(400);
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
        box.setTranslateX(-420);
        box.setTranslateY(230);

        yes.setOnAction(ev -> {
            viewFlow.handleInput("y");
            root.getChildren().remove(box);
        });

        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            root.getChildren().remove(box);
        });

        root.getChildren().add(box);
    }

    /**
     * show the button to roll the dice
     */
    public void showRollDiceInput() {
        StackPane root = (StackPane) scene.getRoot();

        // === Overlay grigio trasparente ===
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(scene.widthProperty());
        overlay.heightProperty().bind(scene.heightProperty());
        overlay.setFill(Color.rgb(0, 0, 0, 0.5));
        overlay.setMouseTransparent(false); // blocca interazioni sotto

        // === Contenitore centrale ===
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);

        Label diceLabel = new Label("🎲 Roll the dices!");
        diceLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 35));
        diceLabel.setTextFill(Color.web("#FFDE69"));

        Button rollButton = new Button("Roll Dices");
        rollButton.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        rollButton.setTextFill(Color.BLACK);
        rollButton.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        rollButton.setCursor(Cursor.HAND);

        box.getChildren().addAll(diceLabel, rollButton);

        StackPane overlayPane = new StackPane(overlay, box);

        root.getChildren().add(overlayPane);

        // === Azione pulsante ===
        rollButton.setOnAction(ev -> {
            root.getChildren().remove(overlayPane);
            viewFlow.handleInput("roll");
        });
    }

    /**
     * show the roll dice result and show it
     * @param result result of the roll
     */
    public void showRollDiceResult(int result) {
        StackPane root = (StackPane) scene.getRoot();

        // === Overlay grigio trasparente ===
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(scene.widthProperty());
        overlay.heightProperty().bind(scene.heightProperty());
        overlay.setFill(Color.rgb(0, 0, 0, 0.5));
        overlay.setMouseTransparent(false); // blocca interazioni sotto

        // === Testo "🎲 Roll the dice!" ===
        Label diceLabel = new Label("🎲 Rolling...");
        diceLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 35));
        diceLabel.setTextFill(Color.web("#FFDE69"));

        // === Numero che cambia rapidamente ===
        Label resultLabel = new Label("");
        resultLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 60));
        resultLabel.setTextFill(Color.WHITE);

        VBox box = new VBox(20, diceLabel, resultLabel);
        box.setAlignment(Pos.CENTER);

        StackPane overlayPane = new StackPane(overlay, box);
        root.getChildren().add(overlayPane);

        // === Animazione: mostra numeri casuali per 1.5s ===
        Timeline rollAnimation = new Timeline();
        Random rand = new Random();

        for (int i = 0; i < 15; i++) {
            int delay = i * 100;
            rollAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(delay), e -> {
                resultLabel.setText(String.valueOf(rand.nextInt(6) + 1));
            }));
        }

        // === Mostra il risultato effettivo alla fine ===
        rollAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(1500), e -> {
            resultLabel.setText(String.valueOf(result));
        }));

        // === Dopo altri 3s: rimuovi overlay e mostra risultato fisso ===
        rollAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(4500), e -> {
            root.getChildren().remove(overlayPane);

            // === Box fisso in basso a sinistra ===
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

            root.getChildren().add(resultBox);
        }));

        rollAnimation.play();
    }

    /**
     * updates the planet to show in planetDecision
     * @param planets list of planets of the card
     */
    public void updatePlanetsOccupied(ArrayList<Trucker> planets) {
        this.planets = planets;
    }

    /**
     * scene for players who finished the game
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
     * show in the top-right corner a popup with the message
     * @param message message to show
     */
    public void showMessage(String message) {
        // Titolo (bold)
        Label title = new Label("New message:");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Messaggio (normale)
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

        // Assicura dimensione del VBox
        messageBox.setMaxWidth(Region.USE_PREF_SIZE);
        messageBox.setMaxHeight(Region.USE_PREF_SIZE);

        Platform.runLater(() -> {
            Node rootNode = scene.getRoot();

            if (rootNode instanceof StackPane root) {
                // Posiziona il messaggio in alto a destra
                StackPane.setAlignment(messageBox, Pos.TOP_RIGHT);
                StackPane.setMargin(messageBox, new Insets(20, 20, 0, 0));
                root.getChildren().add(messageBox);

                // Rimozione automatica dopo 10 secondi
                PauseTransition delay = new PauseTransition(Duration.seconds(10));
                delay.setOnFinished(e -> root.getChildren().remove(messageBox));
                delay.play();
            }
        });
    }

    /**
     * updates the curr good to show in showPlotGoodInput
     * @param good good to show
     */
    public void updateCurrGood(Goods good) {
        this.goodImage = goodsImagesGetter.getGoodImage(good.getValue());
    }

    /**
     * show on screen the positions of the truckers and the relative days distance
     * @param truckers map of nickname and related position
     */
    public void updateTrack(Map<String, Integer> truckers) {
        if (scene == null || scene.getRoot() == null) return;

        StackPane root = (StackPane) scene.getRoot();

        // Rimuove eventuale trackBox precedente
        root.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("trackBox"));

        // Ordina per posizione decrescente
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(truckers.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue())); // ordine decrescente

        // Salva la posizione massima (del primo)
        int maxPosition = sorted.get(0).getValue();

        // Contenitore verticale
        VBox trackBox = new VBox(10);
        trackBox.setId("trackBox");
        trackBox.setAlignment(Pos.CENTER_LEFT);
        trackBox.setPadding(new Insets(10));
        trackBox.setMouseTransparent(true);

        // Titolo
        Label title = new Label("Track Position");
        title.setFont(Font.font("Bangers", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#FFDE69"));
        trackBox.getChildren().add(title);

        // Aggiungi righe in ordine
        for (int i = 0; i < sorted.size(); i++) {
            int pos = i + 1;
            String name = sorted.get(i).getKey();
            int current = sorted.get(i).getValue();

            Text posText = new Text(pos + "°  ");
            posText.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
            posText.setFill(Color.WHITE);

            Text nameText = new Text(name);
            nameText.setFont(Font.font("Bangers", FontWeight.NORMAL, 20));
            nameText.setFill(Color.WHITE);

            Text daysText = new Text();
            if (i > 0) {
                int diff = maxPosition - current;
                daysText.setText("  (+" + diff + " days)");
                daysText.setFont(Font.font("Bangers", FontWeight.NORMAL, 15));
                daysText.setFill(Color.WHITE);
            }

            TextFlow line = new TextFlow(posText, nameText, daysText);
            trackBox.getChildren().add(line);
        }

        // Posiziona a sinistra al centro
        StackPane.setAlignment(trackBox, Pos.CENTER_LEFT);
        StackPane.setMargin(trackBox, new Insets(0, 0, 0, 20));

        root.getChildren().add(trackBox);
    }

    public void updateStatusBar() {
        posLabel.setText(": " + viewFlow.getPosition());
        battLabel.setText(": " + viewFlow.getBatteryCount());
        humanLabel.setText(": " + viewFlow.getHumanCount());
        cannonLabel.setText(": " + viewFlow.getCannonPower());
        engineLabel.setText(": " + viewFlow.getEnginePower());
        creditLabel.setText(": " + viewFlow.getCredits());

        for (Map.Entry<String, Integer> entry : viewFlow.getGoodsByColor().entrySet()) {
            String color = entry.getKey().toLowerCase();
            if (goodLabels.containsKey(color)) {
                goodLabels.get(color).setText(": " + entry.getValue());
            }
        }
    }

    private Label createStyledLabel() {
        Label l = new Label();
        l.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        l.setTextFill(Color.WHITE);
        return l;
    }

    /**
     * connected with shipStatus that show the specific status of the single components
     */
    private void showMore() {
        // Rimuove overlay precedente
        root.getChildren().removeIf(n -> "moreOverlay".equals(n.getId()));

        // Overlay scuro trasparente
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.getScene().widthProperty());
        overlay.heightProperty().bind(root.getScene().heightProperty());
        overlay.setFill(Color.rgb(0, 0, 0, 0.7));

        // === TITOLO CENTRATO ===
        Label title = new Label("Components details:");
        title.setTextFill(Color.web("#FFDE69"));
        title.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 30));
        title.setTextAlignment(TextAlignment.LEFT);

        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        // === CONTENUTO SINISTRA ===
        VBox textBox = new VBox(10);
        textBox.setAlignment(Pos.TOP_LEFT);

        // Componenti della nave
        Trucker t = new Trucker("n", PlayerColors.BLUE);
        t.initSpaceShip(2);
        SpaceShip ship;
        try {
            ship = createShip1(t.getSpaceShip());
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }

        Component[][] layout = ship.getComponents();
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                Component c = layout[i][j];
                if (c != null && !"Empty Space".equals(c.getType())
                        && ("Load".equals(c.getType()) || "Battery Storage".equals(c.getType()) || "Cabin".equals(c.getType()))) {
                    String desc = formatComponent(c, i, j);
                    Label l = new Label(desc);
                    l.setTextFill(Color.WHITE);
                    l.setFont(Font.font("Bangers", 18));
                    l.setTextAlignment(TextAlignment.LEFT);
                    l.setWrapText(true);
                    textBox.getChildren().add(l);
                }
            }
        }

        // === CREA E POSIZIONA LA GRIGLIA NELLO STESSO MODO ===
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
                    }
                } else {
                    cell.setStyle("-fx-background-color: transparent;");
                }

                overlayGrid.add(cell, col - 4, row - 5);
            }
        }

        // === BOX COMPLETO ===
        VBox descriptionBox = new VBox(20, titleBox, textBox);
        descriptionBox.setMaxWidth(600);
        descriptionBox.setAlignment(Pos.TOP_CENTER); // centra solo verticalmente
        descriptionBox.setPadding(new Insets(20));

        // ScrollPane trasparente
        ScrollPane scroll = new ScrollPane(descriptionBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scroll.setPadding(new Insets(20));
        scroll.setMaxWidth(400);
        scroll.setMaxHeight(500);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        StackPane overlayContainer = new StackPane(overlay, overlayGrid, scroll);
        overlayContainer.setId("moreOverlay");
        StackPane.setAlignment(scroll, Pos.CENTER_LEFT);
        StackPane.setMargin(scroll, new Insets(0, 0, 0, 180));
        StackPane.setAlignment(overlayGrid, Pos.TOP_RIGHT);

        root.setOnMouseClicked(e -> root.getChildren().remove(overlayContainer));
        scroll.setOnMouseClicked(e -> e.consume());

        root.getChildren().add(overlayContainer);
    }

    /**
     * connected to showMore that create the string to show for every single component
     * @param c component
     * @param i position x
     * @param j position y
     * @return
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
                        for (Goods g : load.getGoods()) {
                            details.append(g.getColor()).append(" ");
                        }
                    } else {
                        details.append(" is empty");
                    }
                } else {
                    details.append("[").append(i).append(",").append(j).append("] LOAD ");
                    if (!load.isEmpty()) {
                        details.append(" contains: ");
                        for (Goods g : load.getGoods()) {
                            details.append(g.getColor()).append(" ");
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
            default -> {
            }
        }

        return details.toString();
    }

    public SpaceShip createShip1(SpaceShip ship) throws InvalidCoordinates {
        ship = new SpaceShip();
        ship.initSpaceShip(2, PlayerColors.RED);
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


}
