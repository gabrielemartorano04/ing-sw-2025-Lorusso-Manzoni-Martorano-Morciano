package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.AbandonedShip;
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
    private VBox rollDiceResultBox;

    private GoodsImagesGetter goodsImagesGetter;
    private Image goodImage;
    private ArrayList<Trucker> planets;
    private Card currCard;

    private Label posLabel, battLabel, humanLabel, cannonLabel, engineLabel, creditLabel, lostLabel;
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
        Image engine = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/engine.png")).toExternalForm());
        Image cannon = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/cannon.png")).toExternalForm());
        Image human = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/human.png")).toExternalForm());
        Image battery = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/battery.png")).toExternalForm());
        Image pos = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/position.png")).toExternalForm());
        Image credit = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/credit.png")).toExternalForm());
        Image lost = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/cannon.png")).toExternalForm());

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

        // Goods
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
            Image image = goodImages.get(color); // accedi in modo sicuro e ordinato
            ImageView goodIcon = new ImageView(image);
            goodIcon.setFitWidth(35);
            goodIcon.setPreserveRatio(true);

            Label qtyLabel = createStyledLabel();
            goodLabels.put(color, qtyLabel); // salva riferimento

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

    /**
     * Mostra un popup con la condizione corrente e, se il giocatore è il leader, anche un pulsante per continuare.
     *
     * @param condition la condizione da mostrare
     * @param isLeader  true se il giocatore può avviare la condizione
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
            default -> throw new IllegalArgumentException("Condizione sconosciuta: " + condition);
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

        // Attiva overlay e blocca i layer sotto
        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        // Definizione anticipata per evitare errori di riferimento
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
                viewFlow.handleInput("continue");
                closePopup.run();
            });

            box.getChildren().add(continueBtn);
            AnchorPane.setBottomAnchor(box, 120.0);
        } else {
            AnchorPane.setBottomAnchor(box, 180.0);
        }
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
                    errorLabel.setText("Invalid input! Format: '<x1> <y1>, ..., battery, <xb1> <yb1> - <n>'");
                    errorLabel.setVisible(true);
                }
            };


            inputField.setOnAction(e -> sendInput.run());
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);
            box.getChildren().addAll(insertLabel, hintLabel, inputBox, errorLabel);
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

            Label hintLabel = new Label("Type '<x1> <y1>, <x2> <y2>, ..., battery, <xb1> <yb1> - <num>, ...'");
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
                    errorLabel.setText("Invalid input! Format: '<x> <y>, ..., battery, <xb> <yb> - <n>'");
                    errorLabel.setVisible(true);
                }
            };

            inputField.setOnAction(e -> sendInput.run());
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);

            box.getChildren().addAll(insertLabel, hintLabel, inputBox, errorLabel);
            box.setTranslateX(-20);
        });
    }

    // used by both activateEngine and Cannons
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

        // Check coords before 'battery'
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

        // Check batteries after 'battery'
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

            Label hintLabel = new Label("Type '<x> <y>, <xB> <yB>'");
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
                    errorLabel.setText("Invalid input! Use format: '<x> <y>, <xB> <yB>' with numbers");
                    errorLabel.setVisible(true);
                }
            };

            inputField.setOnAction(e -> sendInput.run());
            confirmBtn.setOnAction(e -> sendInput.run());

            HBox inputBox = new HBox(10, inputField, confirmBtn);
            inputBox.setAlignment(Pos.CENTER);

            box.getChildren().addAll(insertLabel, hintLabel, inputBox, errorLabel);
            box.setTranslateX(-50);
        });
    }

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
     * Ask you to put the coordinates of the cabins to remove Humans.
     * */
    public void showHumanCoordInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.setMaxWidth(500);

        Label insertLabel = new Label("Insert the coordinates of the cabins to remove the number of crew members indicated");
        insertLabel.setTextFill(Color.web("#FFDE69"));
        insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        insertLabel.setTextAlignment(TextAlignment.CENTER);
        insertLabel.setWrapText(true);

        Label hintLabel = new Label("Type '<x1> <y1> - <number>, <x2> <y2> - <number>, ...'");
        hintLabel.setTextFill(Color.LIGHTGRAY);
        hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
        hintLabel.setWrapText(true);

        TextField inputField = new TextField();
        inputField.setPromptText("Ex. 2 3 - 1, 4 5 - 2");
        inputField.setPrefWidth(300);

        // Errore visibile solo se serve
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

            TextField yField = new TextField();
            yField.setPromptText("y");
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
     * Make player decide on which planet to land.
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

        // === Bottone "Don't land" ===
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

        // === Composizione finale ===
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

    /**
     * Show a confirmation of the remotion with a continue button.
     */
    public void showRemotionConfirmedInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label title = new Label("Remotion confirmed");
        title.setFont(Font.font("Bangers", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#FFDE69"));

        Button continueBtn = new Button("Continue");
        continueBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
        continueBtn.setTextFill(Color.BLACK);
        continueBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        continueBtn.setCursor(Cursor.HAND);
        continueBtn.setPadding(new Insets(10, 20, 10, 20));

        box.getChildren().addAll(title, continueBtn);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 180.0);
        AnchorPane.setLeftAnchor(box, 100.0);

        // Blocca interazioni sottostanti
        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        // Chiusura popup e ripristino interazione
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
     * Ask for coordinates from where to remove batteries.
     */
    public void showRemoveBatteriesInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label insertLabel = new Label("Insert the coordinates of the batteries you want to remove");
        insertLabel.setTextFill(Color.web("#FFDE69"));
        insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        insertLabel.setTextAlignment(TextAlignment.CENTER);
        insertLabel.setWrapText(true);

        Label hintLabel = new Label("Type '<x> <y> - <quantity>, <x2> <y2> - <quantity>, ...'");
        hintLabel.setTextFill(Color.LIGHTGRAY);
        hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

        TextField inputField = new TextField();
        inputField.setPromptText("Ex. 2 3 - 1, 4 5 - 2");
        inputField.setPrefWidth(300);

        Label errorLabel = new Label("Invalid input. Format: <x> <y> - <qty>, ... (all ≥ 0)");
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
        AnchorPane.setBottomAnchor(box, 170.0);
        AnchorPane.setLeftAnchor(box, 60.0);

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
     * Ask from where to remove the good.
     */
    public void showRemoveGoodsInput() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

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

        HBox inputBox = new HBox(10, inputField, confirmBtn);
        inputBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(insertLabel, hintLabel, inputBox);

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 170.0);
        AnchorPane.setLeftAnchor(box, 90.0);

        overlayLayer.setMouseTransparent(false);
        overlayLayer.getChildren().setAll(popupContent);

        Runnable closePopup = () -> {
            overlayLayer.getChildren().clear();
            restoreInteractiveLayers();
        };

        Runnable sendInput = () -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                viewFlow.handleInput(text);
                closePopup.run();
            }
        };

        inputField.setOnAction(e -> sendInput.run());
        confirmBtn.setOnAction(e -> sendInput.run());
    }

    /**
     * Let the player rearrange goods with confirmation and coordinate input.
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
        AnchorPane.setBottomAnchor(box, 180.0);
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
            box.setTranslateY(40);
        });
    }

    /**
     * Asks if player wants to gain cosmic credits and lose days.
     */
    public void showCosmicCreditsInput() {
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

        AnchorPane popupContent = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 160.0); // Stessa posizione del popup "rearrange"
        AnchorPane.setLeftAnchor(box, 105.0);

        // Applica overlay che blocca l’interazione
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
     * Show the button to roll the dice in the standard popup position.
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
        AnchorPane.setBottomAnchor(box, 120.0);
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
     * Show the roll dice result with animation and fixed display.
     * @param result result of the dice roll
     */
    public void showRollDiceResult(int result) {
        // === Overlay grigio trasparente ===
        Rectangle overlay = new Rectangle();
        overlay.setWidth(scene.getWidth());
        overlay.setHeight(scene.getHeight());
        overlay.setFill(Color.rgb(0, 0, 0, 0.5));
        overlay.setMouseTransparent(false); // blocca interazioni sotto

        // === Testo iniziale ===
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

        // === Animazione numeri casuali per 1.5s ===
        Timeline rollAnimation = new Timeline();
        Random rand = new Random();

        for (int i = 0; i < 15; i++) {
            int delay = i * 100;
            rollAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(delay), e -> {
                resultLabel.setText(String.valueOf(rand.nextInt(6) + 1));
            }));
        }

        // === Mostra il risultato reale ===
        rollAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(1500), e -> {
            resultLabel.setText(String.valueOf(result));
        }));

        // === Dopo 3s: rimuove overlay e mostra risultato fisso ===
        rollAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(4500), e -> {
            root.getChildren().remove(overlayPane);

            // Rimuove il precedente risultato se presente
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
     * updates the curr good to show in showPlotGoodInput
     * @param good good to show
     */
    public void updateCurrGood(Goods good) {
        this.goodImage = goodsImagesGetter.getGoodImage(good.getValue());
    }

    public void updateStatusBar() {
        posLabel.setText(": " + viewFlow.getPosition());
        battLabel.setText(": " + viewFlow.getBatteryCount());
        humanLabel.setText(": " + viewFlow.getHumanCount());
        cannonLabel.setText(": " + viewFlow.getCannonPower());
        engineLabel.setText(": " + viewFlow.getEnginePower());
        creditLabel.setText(": " + viewFlow.getCredits());
        lostLabel.setText(": " + viewFlow.getLostComponents());

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
        title.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 30));
        title.setTextAlignment(TextAlignment.LEFT);
        title.setStyle("-fx-border-color: #FFDE69;");

        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        // === CONTENUTO SINISTRA ===
        VBox textBox = new VBox(10);
        textBox.setAlignment(Pos.TOP_LEFT);

        // Componenti della nave
        Trucker t = new Trucker("n", PlayerColors.BLUE);
        t.initSpaceShip(2);
        Component[][] layout = viewFlow.getShip().getComponents();
        StringBuilder desc = new StringBuilder();
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                Component c = layout[i][j];
                if (c != null && !"Empty Space".equals(c.getType()) && ("Load".equals(c.getType()) || "Battery Storage".equals(c.getType()) || "Cabin".equals(c.getType()) || "Cannon".equals(c.getType()) || "Engine".equals(c.getType()) || "Shield".equals(c.getType()))) {
                    if(!formatComponent(c, i, j).isBlank()) {
                        desc.append(formatComponent(c, i, j)).append("\n");
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
        scroll.setMaxWidth(600);
        scroll.setMaxHeight(500);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        StackPane overlayContainer = new StackPane(overlay, overlayGrid, scroll);
        overlayContainer.setId("moreOverlay");
        StackPane.setAlignment(scroll, Pos.CENTER_LEFT);
        StackPane.setMargin(scroll, new Insets(0, 0, 0, 120));
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
            default -> {
            }
        }

        return details.toString();
    }

    /**
     * Removes the fixed dice result box from the screen, if present.
     */
    public void clearRollDiceResult() {
        if (rollDiceResultBox != null) {
            root.getChildren().remove(rollDiceResultBox);
            rollDiceResultBox = null;
        }
    }

    public void showError(String message) {
        double width = root.getWidth();
        double height = root.getHeight();

        // Sfondo trasparente NON interattivo
        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        // Testo di errore visibile
        Label errorLabel = new Label(message);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 35));
        errorLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        // Contenitore popup
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


}
