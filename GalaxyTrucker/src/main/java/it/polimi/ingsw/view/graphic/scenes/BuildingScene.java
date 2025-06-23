package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Cannon;
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
import java.util.stream.Stream;


public class BuildingScene {
    private final ViewFlow viewFlow;
    private Scene scene;
    private final StackPane root;
    private int level;

    private final String nickname;
    private final Component[][] layout;
    private final boolean[][] validCells;
    private final StackPane[][] cellMatrix;
    private GridPane grid;

    private final ArrayList<StackPane> keptSlotsRef;
    private final ArrayList<ImageView> keptImages;
    private HBox keptSlots;
    private int keptFillLevel;

    private Button finishBtn;
    private Button stackBtn;
    private Button rulesBtn;
    private Button otherBtn;
    private Button hourglassBtn;
    private Button selectBtn;
    private Button selectKeptBtn;

    private int turnToDo;
    private Button turnBtn;
    private Button keepBtn;
    private Button discardBtn;
    private Button linkBtn;

    private List<Node> shipScreenContent;
    private boolean inShipView;
    private boolean isComponentViewVisible;

    private Map<Button, Boolean> previousButtonStates = new HashMap<>();

    private VBox componentSelectedBox;
    private boolean componentSelectedExists;

    private StackPane waitOverlay;
    private Timeline waitAnimation;

    private StackPane currentStackOverlay;
    private ArrayList<int[]> alienCoords;

    public BuildingScene(String nickname, Component[][] layout, ViewFlow viewFlow, StackPane root) {
        this.nickname = nickname;
        this.layout = layout;
        this.cellMatrix = new StackPane[10][11];
        this.keptSlotsRef = new ArrayList<>();
        this.keptImages = new ArrayList<>();
        this.viewFlow = viewFlow;
        this.componentSelectedExists = false;
        this.root = root;
        this.inShipView = true;
        this.isComponentViewVisible = false;
        this.keptFillLevel = 0;
        this.previousButtonStates = new HashMap<>();
        this.turnToDo = 0;

        this.validCells = new boolean[11][12];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Component c = layout[i][j];
                validCells[i][j] = c == null || !c.getType().equals("Empty Space");
            }
        }
    }

    //TODO aggiungere per ogni componente un bottone in modo che premendo parla la link con la posizione x,y
    public Scene createScene(ArrayList<String> playersNames, int level) {
        double width = 1600;
        double height = 900;

        // Sfondo
        this.level = level;
        Image backgroundImage;
        if(level == 0){
            backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-1.jpeg").toExternalForm());
        } else {
            backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-1b.jpeg").toExternalForm());
        }
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(true);
        backgroundView.setMouseTransparent(true);

        buildShipGrid(true, null);

        // Slot vuoti per i kept
        keptSlots = new HBox(15);
        keptSlots.setMaxWidth(200);
        keptSlots.setAlignment(Pos.TOP_RIGHT);
        keptSlotsRef.clear();
        keptImages.clear();


        for (int i = 0; i < 2; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(110, 110);
            slot.setStyle("-fx-background-color: transparent;");

            Rectangle border = new Rectangle(110, 110);
            border.setArcWidth(6);
            border.setArcHeight(6);
            border.setFill(Color.TRANSPARENT);
            border.setStroke(Color.TRANSPARENT);
            border.setStrokeWidth(1);

            slot.getChildren().add(border);
            int index = i;
            slot.setOnMouseClicked(e -> {
                viewFlow.handleInput("selectKept " + index);
            });
            keptSlots.getChildren().add(slot);
            keptSlotsRef.add(slot);
            keptImages.add(null);
        }

        StackPane.setAlignment(keptSlots, Pos.TOP_RIGHT);
        StackPane.setMargin(keptSlots, new Insets(0, 235, 590, 0));


        root.getChildren().setAll(backgroundView, grid, keptSlots);
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
        attackselectComponentButtons(scene);

        return scene;
    }

    public void buildShipGrid(boolean buildNew, Component[][] ship) {
        // Se grid esiste già, la rimuovo dal root
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
                cell.setPrefSize(136, 136);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 6px; -fx-background-radius: 6px;");

                    Component c = buildNew ? layout[row][col] : ship[row][col];
                    if (c != null) {
                        ImageView imageView = new ImageView(c.getFrontImage());
                        imageView.setFitWidth(136);
                        imageView.setFitHeight(136);
                        Rectangle clip = new Rectangle(136, 136);
                        clip.setArcWidth(12);
                        clip.setArcHeight(12);
                        imageView.setClip(clip);
                        imageView.setRotate(c.getRotationAngle()); // Per uniformità con l’altro metodo
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
        if(!buildNew){
            this.showMessage("Ship checked by the Server, invalid components may have been removed");
        }
    }




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



    public Scene getScene() {
        return scene;
    }

    public void insertComponentToKepts(Component component) {
        if (keptFillLevel >= keptImages.size()) {
            return; // tutti gli slot sono pieni, non dovrebbe succedere
        }

        this.resetComponentSelectedBox();

        ImageView componentView = new ImageView(component.getFrontImage());
        componentView.setFitWidth(110);
        componentView.setPreserveRatio(true);
        componentView.setMouseTransparent(true);

        Rectangle clip = new Rectangle(110, 110);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        componentView.setClip(clip);

        // Trova il primo slot libero
        int indexToInsert = -1;
        for (int i = 0; i < keptImages.size(); i++) {
            if (keptImages.get(i) == null) {
                indexToInsert = i;
                break;
            }
        }

        if (indexToInsert == -1) {
            return; // nessuno slot libero, dovrebbe essere evitato a monte
        }

        StackPane slot = keptSlotsRef.get(indexToInsert);
        slot.getChildren().clear();
        slot.getChildren().add(componentView);

        Rectangle border = new Rectangle(110, 110);
        border.setArcWidth(6);
        border.setArcHeight(6);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(1);
        slot.getChildren().add(border);

        keptImages.set(indexToInsert, componentView);
        keptFillLevel++;
        this.selectKeptBtn.setDisable(false);
        this.selectKeptBtn.setOpacity(1.0);
    }

    public void selectKeptComponent(int index) {
        if (index < 0 || index >= keptImages.size()) return;
        ImageView keptView = keptImages.get(index);
        // Solo se c'è un kept valido e lo slot di selezione è vuoto
        if (keptView != null) {
            keptFillLevel--;
            this.insertSelectedComponent(keptView,false);
            // Rimuovi dallo slot kept
            this.disableSelectButtons(false);
            keptImages.set(index, null);
            keptSlotsRef.get(index).getChildren().clear();
            Rectangle border = new Rectangle(110, 110);
            border.setArcWidth(6);
            border.setArcHeight(6);
            border.setFill(Color.TRANSPARENT);
            border.setStroke(Color.TRANSPARENT);
            border.setStrokeWidth(1);

            keptSlotsRef.get(index).getChildren().add(border);
            if(keptFillLevel== 0){
                this.selectKeptBtn.setDisable(true);
                this.selectKeptBtn.setOpacity(0.5);
            }
        }

    }

    public void setTurnToDo(int turn) {
        this.turnToDo = turn;
    }
    public void insertSelectedComponent(ImageView componentView, boolean toTurn) {
        if (!inShipView && shipScreenContent != null) {
            isComponentViewVisible = false;
            root.getChildren().setAll(shipScreenContent);
            inShipView = true;
        }

        this.componentSelectedExists = true;
        disableSelectButtons(false);
        if(keptFillLevel==2){
            this.keepBtn.setDisable(true);
            this.keepBtn.setOpacity(0.5);
        }

        componentView.setFitWidth(176);
        componentView.setPreserveRatio(true);
        componentView.setMouseTransparent(true);

        if (toTurn) {
            turnToDo = (turnToDo + 1) % 4;
            componentView.setRotate(90 * turnToDo);
        }


        Rectangle clip = new Rectangle(176, 176);
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        componentView.setClip(clip);


        StackPane imageContainer = (StackPane) componentSelectedBox.getChildren().get(0);
        imageContainer.getChildren().add(componentView);

    }

    public void insertComponentInGrid(Component component, int row, int col) {

        this.componentSelectedExists = false;
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

        layout[row][col] = component; // aggiorna anche il model
    }

    /**
     * Rimuove qualsiasi componente grafico dalla cella specificata
     */
    public void clearCell(int row, int col) {
        if (!validCells[row][col]) return;
        StackPane cell = cellMatrix[row][col];
        if (cell != null) {
            cell.getChildren().clear();
        }
    }

    /**
     * Restituisce la `StackPane` di una cella, utile se devi settare eventi
     */
    public StackPane getCell(int row, int col) {
        return validCells[row][col] ? cellMatrix[row][col] : null;
    }

    public ArrayList<StackPane> getKeptSlotsRef() {
        return keptSlotsRef;
    }

    public void resetComponentSelectedBox() {
        turnToDo = 0;

        componentSelectedBox.getChildren().clear();
        this.componentSelectedExists = false;
        this.disableSelectButtons(true); // non hai piu un componente in mano

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

    public void showPopUpError(String message) {
        double width = root.getWidth();
        double height = root.getHeight();

        // Overlay grigio trasparente
        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        // Etichetta con il messaggio
        Label messageLabel = new Label(message);
        messageLabel.setTextFill(Color.RED);
        messageLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 35));
        messageLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        // Contenitore del popup
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


    public void refreshComponentsViewIfVisible() {
        if (isComponentViewVisible) {
            loadComponentsView(viewFlow.getAvailableComponents());
        }
    }





    private void attachOptionsBoxToScene(Scene scene, int level) {
        VBox optionsBox = new VBox(10);
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

    private Consumer<Button> createDefaultButtonStyler() {
        return (btn) -> {
            btn.setFont(Font.font("Bangers", 18));
            btn.setTextFill(Color.BLACK);
            btn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            btn.setCursor(Cursor.HAND);
            btn.setPrefWidth(180);
        };
    }

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

    private void addHourglassButtonToScene(ViewFlow viewFlow) {
        Button hourglassBtn = new Button("⏳");
        hourglassBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        hourglassBtn.setTextFill(Color.BLACK);
        hourglassBtn.setCursor(Cursor.HAND);
        hourglassBtn.setPrefWidth(70);
        hourglassBtn.setPrefHeight(30);
        hourglassBtn.setBackground(new Background(new BackgroundFill(
                Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));

        hourglassBtn.setOnAction(e -> {
            viewFlow.handleInput("hourglass");
        });

        this.hourglassBtn = hourglassBtn;
    }

    public void startHourglassTimer() {
        hourglassBtn.setDisable(true);

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
                hourglassBtn.setEffect(null); // remove overlay
            }
        }));
        countdown.setCycleCount(90);
        countdown.play();
    }


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

    private void addSelectKeptButton(ViewFlow viewFlow, Consumer<Button> styleButton) {
        this.selectKeptBtn = new Button("SelectKept");
        styleButton.accept(selectKeptBtn);
        selectKeptBtn.setOnAction(e -> {
            if (componentSelectedExists) {
                // Overlay grigio trasparente a tutto schermo
                Rectangle fullOverlay = new Rectangle();
                fullOverlay.widthProperty().bind(root.widthProperty());
                fullOverlay.heightProperty().bind(root.heightProperty());
                fullOverlay.setFill(Color.rgb(0, 0, 0, 0.5));
                fullOverlay.setMouseTransparent(false); // blocca interazione

                // Messaggio di avviso
                Label warningLabel = new Label("You have already selected a component!");
                warningLabel.setTextFill(Color.RED);
                warningLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 35));
                warningLabel.setMouseTransparent(true);
                warningLabel.setWrapText(true);
                warningLabel.setTextAlignment(TextAlignment.CENTER);

                // Contenitore overlay + messaggio
                StackPane overlayLayer = new StackPane(fullOverlay, warningLabel);
                overlayLayer.setAlignment(Pos.CENTER);
                overlayLayer.setPickOnBounds(true); // riceve eventi sullo sfondo

                // Aggiungi overlay al root principale
                root.getChildren().add(overlayLayer);

                // Rimuovi overlay dopo 3 secondi
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
//            popup.setBackground(new Background(new BackgroundFill(Color.web("#333333"), new CornerRadii(15), Insets.EMPTY)));

            Label messageLabel = new Label();
            messageLabel.setTextFill(Color.web("#FFDE69"));
            messageLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
            messageLabel.setWrapText(true);
            messageLabel.setTextAlignment(TextAlignment.CENTER);

            TextField indexField = new TextField();
            indexField.setPromptText("Index (0 or 1)");
            indexField.setPrefWidth(100);
            indexField.setVisible(false);  // Mostrato solo se ci sono due kept
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
            for (int i = 0; i < keptImages.size(); i++) {
                if (keptImages.get(i) != null) {
                    countKept++;
                    availableIndices.add(i);
                }
            }

            if (countKept == 0) {
                messageLabel.setText("No kept found!");
                sendBtn.setDisable(true);
            } else if (countKept == 1) {
                int onlyIndex = availableIndices.get(0);
                messageLabel.setText("You have only 1 kept, it will be automatically selected");
                messageLabel.setWrapText(true);
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
                    if (keptImages.get(idx) == null) {
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


    public void loadComponentsView(ArrayList<Component> components) {
        this.isComponentViewVisible = true;
        double width = 1600;
        double height = 900;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
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

                // === Contenitore popup (senza sfondo) ===
                VBox popup = new VBox(18); // più spaziatura
                popup.setAlignment(Pos.CENTER);
                popup.setPadding(new Insets(15));
                popup.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE); // altezza dinamica
                // Niente background: niente riquadro colorato

                // === Testo centrale ===
                Label confirmLabel = new Label("Select this component?");
                confirmLabel.setTextFill(Color.web("#FFDE69"));
                confirmLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 28)); // ingrandito
                confirmLabel.setWrapText(true);
                confirmLabel.setTextAlignment(TextAlignment.CENTER);
                confirmLabel.setAlignment(Pos.CENTER);

                // === Immagine del componente ===
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

                // Bordo bianco attorno all'immagine
                Rectangle border = new Rectangle(200, 200);
                border.setArcWidth(20);
                border.setArcHeight(20);
                border.setFill(Color.TRANSPARENT);
                border.setStroke(Color.WHITE);
                border.setStrokeWidth(1);
                border.setMouseTransparent(true);

                // Contenitore immagine + bordo
                StackPane imageWithBorder = new StackPane(componentImage, border);
                imageWithBorder.setAlignment(Pos.CENTER);

                // === Pulsanti ===
                Button undoBtn = new Button("Undo");
                undoBtn.setFont(Font.font("Bangers", FontWeight.BOLD, 18)); // ingrandito
                undoBtn.setCursor(Cursor.HAND);
                undoBtn.setTextFill(Color.WHITE);
                undoBtn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(8), Insets.EMPTY)));

                Button selectBtnComponentsView = new Button("Select");
                selectBtnComponentsView.setFont(Font.font("Bangers", FontWeight.BOLD, 18)); // ingrandito
                selectBtnComponentsView.setCursor(Cursor.HAND);
                selectBtnComponentsView.setTextFill(Color.WHITE);
                selectBtnComponentsView.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(8), Insets.EMPTY))); // verde

                HBox buttons = new HBox(20, undoBtn, selectBtnComponentsView); // più spazio
                buttons.setAlignment(Pos.CENTER);

                // === Composizione finale ===
                popup.getChildren().addAll(confirmLabel, imageWithBorder, buttons);

                // === Posizionamento centrato sullo schermo ===
                AnchorPane.setTopAnchor(popup, (root.getHeight() - 300) / 2); // più altezza ora
                AnchorPane.setLeftAnchor(popup, (root.getWidth() - 250) / 2);

                overlay.getChildren().add(popup);
                root.getChildren().add(overlay);

                // === Gestione click ===
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

    private void createComponentSelectedButtons(Consumer<Button> styleButton) {
        this.turnBtn = new Button("Turn");
        this.keepBtn = new Button("Keep");
        this.discardBtn = new Button("Discard");
        this.linkBtn = new Button("Link");

        styleButton.accept(turnBtn);
        styleButton.accept(keepBtn);
        styleButton.accept(linkBtn);

        discardBtn.setFont(Font.font("Bangers", 18));
        discardBtn.setTextFill(Color.WHITE);
        discardBtn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        discardBtn.setCursor(Cursor.HAND);
        discardBtn.setPrefWidth(180);

        this.disableSelectButtons(true);

        turnBtn.setOnAction(e -> {
                viewFlow.handleInput("Turn");
            }
        );
        keepBtn.setOnAction(e -> viewFlow.handleInput("Keep"));
        discardBtn.setOnAction(e -> {
            componentSelectedExists = false;
            viewFlow.handleInput("Discard");
        });
        linkBtn.setOnAction(ev -> {
            AnchorPane overlay = new AnchorPane();
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
            overlay.setPickOnBounds(true);

            VBox popup = new VBox(12);
            popup.setAlignment(Pos.CENTER);
            popup.setPadding(new Insets(15));
            popup.setPrefSize(200, 250); // quadrato e più piccolo
            //popup.setBackground(new Background(new BackgroundFill(Color.web("#333333"), new CornerRadii(15), Insets.EMPTY)));

            Label insertLabel = new Label("Choose the coordinates to link the component");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
            insertLabel.setWrapText(true);
            insertLabel.setTextAlignment(TextAlignment.CENTER);

            // Due campi affiancati per x e y
            TextField xField = new TextField();
            xField.setPromptText("X");
            xField.setPrefWidth(50);
            xField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) xField.setText(newVal.replaceAll("[^\\d]", ""));
            });

            TextField yField = new TextField();
            yField.setPromptText("Y");
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

            popup.getChildren().addAll(insertLabel, coordBox, errorLabel, buttons);

            // Allinea il popup: verticale centro, orizzontale destra
            AnchorPane.setTopAnchor(popup, (root.getHeight() - 250) / 2);
            AnchorPane.setRightAnchor(popup, 10.0); // più vicino al bordo destro

            overlay.getChildren().add(popup);
            root.getChildren().add(overlay);

            Runnable removePopup = () -> root.getChildren().remove(overlay);

            cancelBtn.setOnAction(e -> removePopup.run());

            sendBtn.setOnAction(e -> {
                String xText = xField.getText().trim();
                String yText = yField.getText().trim();

                try {
                    int x = Integer.parseInt(xText);
                    int y = Integer.parseInt(yText);

                    if(viewFlow.getLevel() == 2) {
                        if (x < 4 || x > 10 || y < 5 || y > 9) {
                            errorLabel.setVisible(true);
                            return;
                        }
                    }else{
                        if(x < 5 || x > 9 || y < 5 || y > 9){
                            errorLabel.setVisible(true);
                            return;
                        }
                    }


                    errorLabel.setVisible(false);
                    viewFlow.handleInput("link " + x + " " + y);
                    removePopup.run();
                } catch (NumberFormatException ex) {
                    errorLabel.setVisible(true);
                }
            });

            // ENTER = invia (se focus su yField)
            yField.setOnAction(e -> sendBtn.fire());
        });

    }

    private void attackselectComponentButtons(Scene scene) {
        VBox componentBox = new VBox(10);
        componentBox.setPadding(new Insets(0, 0, 50, 15));
        componentBox.setAlignment(Pos.BOTTOM_LEFT);

        componentBox.getChildren().addAll(turnBtn, linkBtn, keepBtn, discardBtn);

        Platform.runLater(() -> {
            StackPane root = (StackPane) scene.getRoot();
            if (!root.getChildren().contains(componentBox)) {
                StackPane.setAlignment(componentBox, Pos.BOTTOM_LEFT);
                StackPane.setMargin(componentBox, new Insets(0, 0, 0, 0));
                root.getChildren().add(componentBox);
            }
            componentBox.setPickOnBounds(false);

        });

    }

    public void loadShipView() {
        root.getChildren().setAll(shipScreenContent);
    }

    public void showOtherShip(Component[][] layout, String nickname) {
        disableAndSaveAllButtonStates();

        // === Immagine della nave ===
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

        // === Griglia componenti ===
        GridPane grid = new GridPane();
        grid.setHgap(1.5);
        grid.setVgap(1.5);
        grid.setAlignment(Pos.CENTER);
        grid.setTranslateY(-1);

        for (int row = 5; row < 10; row++) {
            for (int col = 4; col < 11; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(87.5, 87.5);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 4px; -fx-background-radius: 4px;");
                    Component c = layout[row][col];
                    if (c != null) {
                        ImageView iv = new ImageView(c.getFrontImage());
                        iv.setFitWidth(87.5);
                        iv.setFitHeight(87.5);
                        iv.setRotate(c.getRotationAngle());
                        Rectangle r = new Rectangle(87.5, 87.5);
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

        // === Contenitore nave + griglia ===
        StackPane shipPopup = new StackPane(backgroundView, grid);
        shipPopup.setAlignment(Pos.CENTER);
        shipPopup.setMaxSize(679, 500);
        shipPopup.setMouseTransparent(false);

        // === Label con nickname ===
        Label nameLabel = new Label(nickname + "'s Ship");
        nameLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 32));
        nameLabel.setTextFill(Color.web("#FFDE69"));
        nameLabel.setAlignment(Pos.CENTER);

        // === Contenitore verticale: label sopra + nave sotto ===
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

        // === Aggiunta sopra la UI ===
        root.getChildren().add(popupWithLabel);
    }

    private void restoreAllButtonStates() {
        for (Map.Entry<Button, Boolean> entry : previousButtonStates.entrySet()) {
            Button btn = entry.getKey();
            boolean wasEnabled = entry.getValue();
            btn.setDisable(!wasEnabled);
            btn.setOpacity(wasEnabled ? 1.0 : 0.5);
        }
        previousButtonStates.clear(); // reset per evitare conflitti futuri
    }

    private void disableAndSaveAllButtonStates() {
        List<Button> buttons = Arrays.asList(
                finishBtn, stackBtn, rulesBtn, otherBtn, hourglassBtn,
                selectBtn, selectKeptBtn, turnBtn, keepBtn, discardBtn, linkBtn
        );

        previousButtonStates.clear();

        for (Button btn : buttons) {
            previousButtonStates.put(btn, !btn.isDisabled()); // true se era abilitato
            btn.setDisable(true);
            btn.setOpacity(0.5);
        }
    }

    public void showWaitForOthers() {
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

    public void showMessage(String message) {
        // Titolo (bold)
        Label title = new Label("New message:");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Messaggio (normale)
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

    public void showStack(Stack stack) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        overlay.setPickOnBounds(true);
        overlay.setMouseTransparent(false);

        HBox cardBox = new HBox(20);
        cardBox.setAlignment(Pos.CENTER_LEFT); // allineamento a sinistra per lo scroll

        for (Card card : stack.getStack()) {
            ImageView cardView = new ImageView(card.getFrontImage());
            cardView.setFitWidth(200);
            cardView.setFitHeight(320);
            cardView.setStyle("-fx-effect: dropshadow(one-pass-box, white, 6, 0, 0, 0);");
            cardBox.getChildren().add(cardView);
        }

        // ScrollPane che contiene le carte
        ScrollPane scrollPane = new ScrollPane(cardBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setPadding(new Insets(100, 50, 50, 50)); // spazio intorno alle carte

        // Pulsante "←" sempre visibile in alto a sinistra
        Button backBtn = new Button("←");
        backBtn.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 30));
        backBtn.setTextFill(Color.BLACK);
        backBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        backBtn.setCursor(Cursor.HAND);
        backBtn.setPadding(new Insets(12, 18, 12, 18));

        StackPane.setAlignment(backBtn, Pos.TOP_LEFT);
        StackPane.setMargin(backBtn, new Insets(30));

        backBtn.setOnAction(e -> {
            viewFlow.handleInput("dropStack");
        });

        overlay.getChildren().addAll(scrollPane, backBtn);

        Platform.runLater(() -> {
            root.getChildren().add(overlay);
            this.currentStackOverlay = overlay;
        });
    }

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

    public void updateAlienCabinCoord(ArrayList<int[]> alienCoords) {
        this.alienCoords = alienCoords;
    }

    public void showAlienCabin() {
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
                // Disabilita e oscura il bottone
                placeBtn.setDisable(true);
                placeBtn.setStyle("-fx-background-color: #999999;");
                // Aggiungi le coordinate al comando
                commandBuilder.append(" ").append(x).append(" ").append(y).append(" ").append(colorName);
            });

            HBox line = new HBox(10, coordText, placeBtn);
            line.setAlignment(Pos.CENTER_RIGHT);
            alienListBox.getChildren().add(line);
        }

        // Pulsante finale di conferma
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


    public void showTimeExpired(int expirationCount) {
        if (scene == null || root == null) return;

        // Crea la scritta TIME EXPIRED + count
        Label message = new Label("TIME EXPIRED\n(" + "For " + expirationCount + " times)");
        message.setTextFill(Color.RED);
        message.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 80));
        message.setTextAlignment(TextAlignment.CENTER);

        // Effetto lampeggiante
        FadeTransition blink = new FadeTransition(Duration.seconds(0.4), message);
        blink.setFromValue(1.0);
        blink.setToValue(0.0);
        blink.setCycleCount(Animation.INDEFINITE);
        blink.setAutoReverse(true);
        blink.play();

        // Timeline per fermare il lampeggio dopo 3 secondi
        Timeline stopBlink = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            blink.stop();
            message.setOpacity(1.0); // assicurati che resti visibile dopo il blink
        }));
        stopBlink.play();

        Rectangle dimBackground = new Rectangle();
        dimBackground.setFill(Color.rgb(0, 0, 0, 0.6));
        dimBackground.widthProperty().bind(scene.widthProperty());
        dimBackground.heightProperty().bind(scene.heightProperty());

        StackPane overlay = new StackPane(dimBackground, message);
        overlay.setAlignment(Pos.CENTER);
        overlay.setMouseTransparent(true);

        root.getChildren().add(overlay);

        PauseTransition wait = new PauseTransition(Duration.seconds(2.5));
        wait.setOnFinished(e -> root.getChildren().remove(overlay));
        wait.play();
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

