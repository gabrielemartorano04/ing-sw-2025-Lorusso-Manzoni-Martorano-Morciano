package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.view.ViewFlow;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.util.List;
import java.util.ArrayList;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javafx.scene.control.ScrollPane;



public class BuildingScene {
    private final ViewFlow viewFlow;
    private Scene scene;
    private final StackPane root;


    private final String nickname;
    private final Component[][] layout;
    private final boolean[][] validCells;
    private final StackPane[][] cellMatrix;
    private GridPane grid;

    private final ArrayList<StackPane> keptSlotsRef;
    private final ArrayList<ImageView> keptImages;
    private HBox keptSlots;

    private Button finishBtn;
    private Button stackBtn;
    private Button rulesBtn;
    private Button otherBtn;
    private Button hourglassBtn;
    private Button selectBtn;

    private Pane linkOverlay;
    private boolean isLinkEnabled;
    Button turnBtn;
    Button keepBtn;
    Button discardBtn;
    Button linkBtn;

    private List<Node> shipScreenContent;

    private VBox componentSelectedBox;
    private boolean componentSelectedExists = false;

    public BuildingScene(String nickname, Component[][] layout, ViewFlow viewFlow, StackPane root) {
        this.nickname = nickname;
        this.layout = layout;
        this.cellMatrix = new StackPane[10][11];
        this.keptSlotsRef = new ArrayList<>();
        this.keptImages = new ArrayList<>();
        this.viewFlow = viewFlow;
        this.isLinkEnabled = false;
        this.componentSelectedExists = false;
        this.root = root;

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

        // Griglia nave
        grid = new GridPane();
        grid.setHgap(3.5);
        grid.setVgap(3.5);
        grid.setAlignment(Pos.CENTER);
        grid.setTranslateX(1);
        grid.setTranslateY(-10);

        for (int row = 5; row < 10; row++) {
            for (int col = 4; col < 11; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(136, 136);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 6px; -fx-background-radius: 6px;");

                    Component c = layout[row][col];
                    if (c != null) {
                        ImageView imageView = new ImageView(c.getFrontImage());
                        imageView.setFitWidth(136);
                        imageView.setFitHeight(136);
                        Rectangle clip = new Rectangle(136, 136);
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
                int rowIndex = row;
                int colIndex = col;
                if(isLinkEnabled){
                    cell.setOnMouseClicked(e -> {
                        if (isLinkEnabled && validCells[rowIndex][colIndex]) {
                            viewFlow.handleInput("Link " + rowIndex + " " + colIndex);
                            exitLinkMode();
                        }
                    });

                }

            }
        }

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
            border.setStroke(Color.WHITE);
            border.setStrokeWidth(2);

            slot.getChildren().add(border);
            int index = i;
            slot.setOnMouseClicked(e -> {
                viewFlow.handleInput("selectKept " + index);
                isLinkEnabled = true;
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
        addComponentSelectedSlot();

        attachOptionsBoxToScene(scene, level);

        linkOverlay = new Pane();
        linkOverlay.setPrefSize(width, height);
        linkOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        linkOverlay.setVisible(false);
        root.getChildren().add(linkOverlay);

        return scene;
    }

    private void attachOptionsBoxToScene(Scene scene, int level) {
        VBox optionsBox = new VBox(10);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.TOP_LEFT);

        if(level ==0){
            optionsBox.getChildren().addAll(selectBtn, stackBtn, otherBtn, finishBtn, rulesBtn);
        }else{
            optionsBox.getChildren().addAll(selectBtn, stackBtn, otherBtn, finishBtn, rulesBtn, hourglassBtn);
        }

        Platform.runLater(() -> {
            StackPane root = (StackPane) scene.getRoot();
            if (!root.getChildren().contains(optionsBox)) {
                StackPane.setAlignment(optionsBox, Pos.TOP_RIGHT);
                StackPane.setMargin(optionsBox, new Insets(20, 0, 0, 5));
                root.getChildren().add(optionsBox);
            }
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
                    viewFlow.handleInput("stack " + index);
                    //TODO aggiungere che una volta scelto lo stack si mostrino le carte
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
        });

        this.hourglassBtn = hourglassBtn;
    }

    private void addSelectComponentButton(Consumer<Button> styleButton) {
        Button selectComponentBtn = new Button("Select component");
        styleButton.accept(selectComponentBtn);

        selectComponentBtn.setOnAction(e -> {
            shipScreenContent = new ArrayList<>(root.getChildren());

            ArrayList<Component> components = new AvailableComponents().getComponents();
            //TODO: ora tieni components per vedere ma poi sarà showAvailableComponent(viewFlow.getAvailableComponents());
            loadComponentsView(components);
        });

        this.selectBtn = selectComponentBtn;
    }

    public void loadComponentsView(ArrayList<Component> components) {
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
                viewFlow.handleInput("select " + index);

                //TODO aggiungere la giusta schermata per il posizionamento del componente
                // Rimane su questa schermata, o torna se vuoi
            });

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
            if (shipScreenContent != null) {
                root.getChildren().setAll(shipScreenContent);
            }
        });

        StackPane componentView = new StackPane(backgroundView, scrollPane, backBtn);
        StackPane.setAlignment(backBtn, Pos.TOP_LEFT);
        StackPane.setMargin(backBtn, new Insets(35, 0, 0, 125));

        root.getChildren().setAll(componentView);
    }

    private void addComponentSelectedSlot() {
        double borderSize = 180;
        double imageSize = 180;

        // Slot vuoto (solo contorno bianco)
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

        VBox buttonsBox = createSelectComponentButtons();
        buttonsBox.setAlignment(Pos.CENTER);

        componentSelectedBox = new VBox(10, imageContainer, buttonsBox);
        componentSelectedBox.setPadding(new Insets(0, 0, -400, -1243));
        componentSelectedBox.setAlignment(Pos.CENTER);

        StackPane.setAlignment(componentSelectedBox, Pos.CENTER);
        StackPane.setMargin(componentSelectedBox, new Insets(0, 0, 25, 15));
        root.getChildren().add(componentSelectedBox);
    }


    private VBox createSelectComponentButtons() {
        Consumer<Button> styleYellowButton = btn -> {
            btn.setFont(Font.font("Bangers", 18));
            btn.setTextFill(Color.BLACK);
            btn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            btn.setCursor(Cursor.HAND);
            btn.setPrefWidth(180);
        };

        Consumer<Button> styleRedButton = btn -> {
            btn.setFont(Font.font("Bangers", 18));
            btn.setTextFill(Color.WHITE);
            btn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
            btn.setCursor(Cursor.HAND);
            btn.setPrefWidth(180);
        };

        Button turnBtn = new Button("Turn");
        Button keepBtn = new Button("Keep");
        Button discardBtn = new Button("Discard");
        Button linkBtn = new Button("Link");

        styleYellowButton.accept(turnBtn);
        styleYellowButton.accept(keepBtn);
        styleRedButton.accept(discardBtn);
        styleYellowButton.accept(linkBtn);

        boolean disable = !componentSelectedExists;
        for (Button btn : List.of(turnBtn, keepBtn, discardBtn, linkBtn)) {
            btn.setDisable(disable);
            btn.setOpacity(disable ? 0.5 : 1.0);
        }

        turnBtn.setOnAction(e -> {
            if (!turnBtn.isDisabled()) {
                viewFlow.handleInput("Turn");
            }
        });

        keepBtn.setOnAction(e -> {
            if (!keepBtn.isDisabled()) {
                viewFlow.handleInput("Keep");
            }
        });

        discardBtn.setOnAction(e -> {
            if (!discardBtn.isDisabled()) {
                viewFlow.handleInput("Discard");
            }
        });

        linkBtn.setOnAction(e -> {
            isLinkEnabled = true;

            linkOverlay.setVisible(true);

            grid.toFront();

            Stream.of(turnBtn, keepBtn, discardBtn).forEach(btn -> {
                btn.setDisable(true);
                btn.setOpacity(0.5);
            });
        });

        VBox buttonsBox = new VBox(8, turnBtn, linkBtn, keepBtn, discardBtn);
        buttonsBox.setAlignment(Pos.CENTER);
        this.turnBtn = turnBtn;
        this.keepBtn = keepBtn;
        this.discardBtn = discardBtn;
        this.linkBtn = linkBtn;

        return buttonsBox;
    }

    public void insertSelectedComponent(Component selected) {
        this.componentSelectedExists = true;

        ImageView componentView = new ImageView(selected.getFrontImage());
        componentView.setFitWidth(178);
        componentView.setPreserveRatio(true);
        componentView.setMouseTransparent(true);

        Rectangle clip = new Rectangle(178, 178);
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        componentView.setClip(clip);

        // Inserisce l'immagine nel contenitore esistente
        StackPane imageContainer = (StackPane) componentSelectedBox.getChildren().get(0);
        imageContainer.getChildren().add(componentView);

        // Rimuove la vecchia box dei bottoni e la sostituisce con una abilitata
        componentSelectedBox.getChildren().remove(1);
        componentSelectedBox.getChildren().add(createSelectComponentButtons());
    }


    public void loadShipView(){
        root.getChildren().setAll(shipScreenContent);
    }


    private void exitLinkMode() {
        isLinkEnabled = false;
        linkOverlay.setVisible(false);

        Stream.of(turnBtn, keepBtn, discardBtn).forEach(btn -> {
            btn.setDisable(false);
            btn.setOpacity(1.0);
        });
    }


    public Scene getScene() {
        return scene;
    }

    /**
     * Inserisce un'immagine (es. componente) nella cella specificata
     */
    public void insertComponentImageToShip(int row, int col, ImageView imageView) {
        if (!validCells[row][col]) return;
        StackPane cell = cellMatrix[row][col];
        if (cell != null) {
            cell.getChildren().clear();

            imageView.setFitWidth(136);
            imageView.setFitHeight(136);
            Rectangle clip = new Rectangle(136, 136);
            clip.setArcWidth(6);
            clip.setArcHeight(6);
            imageView.setClip(clip);

            cell.getChildren().add(imageView);
        }
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

    public void addKeptComponent(int index, Component component, ViewFlow viewFlow) {
        if (index < 0 || index >= keptSlotsRef.size()) return;

        StackPane slot = keptSlotsRef.get(index);
        slot.getChildren().removeIf(n -> n instanceof ImageView);  // Rimuove l'immagine esistente

        ImageView imageView = new ImageView(component.getFrontImage());
        imageView.setFitWidth(110);
        imageView.setFitHeight(110);
        Rectangle clip = new Rectangle(110, 110);
        clip.setArcWidth(6);
        clip.setArcHeight(6);
        imageView.setClip(clip);

        final int finalIndex = index;
        imageView.setOnMouseClicked(event -> {
            viewFlow.handleInput("selectkept " + finalIndex);
            isLinkEnabled = true;
            event.consume();
        });

        slot.getChildren().add(imageView);
        keptImages.set(index, imageView);
    }

    public void removeKeptComponent(int index) {
        if (index < 0 || index >= keptSlotsRef.size()) return;

        StackPane slot = keptSlotsRef.get(index);
        slot.getChildren().removeIf(n -> n.getClass() == ImageView.class);
        keptImages.set(index, null);
    }

    public ArrayList<StackPane> getKeptSlotsRef() {
        return keptSlotsRef;
    }
}

