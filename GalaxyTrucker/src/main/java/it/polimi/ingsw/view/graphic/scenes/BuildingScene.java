package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.view.ViewFlow;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.function.Consumer;


public class BuildingScene {
    private final String nickname;
    private final Component[][] layout;
    private final boolean[][] validCells;
    private final StackPane[][] cellMatrix;
    private final ArrayList<StackPane> keptSlotsRef;
    private final ArrayList<ImageView> keptImages;
    private GridPane grid;
    private Scene scene;
    private HBox keptSlots;
    private final ViewFlow viewFlow;
    private Button finishBtn;
    private Button stackBtn;


    public BuildingScene(String nickname, Component[][] layout, ViewFlow viewFlow) {
        this.nickname = nickname;
        this.layout = layout;
        this.cellMatrix = new StackPane[10][11];
        this.keptSlotsRef = new ArrayList<>();
        this.keptImages = new ArrayList<>();
        this.viewFlow = viewFlow;

        this.validCells = new boolean[][]{
                {false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, true, false, true, false, false},
                {false, false, false, false, false, true, true, true, true, true, false},
                {false, false, false, false, true, true, true, true, true, true, true},
                {false, false, false, false, true, true, true, true, true, true, true},
                {false, false, false, false, true, true, true, false, true, true, true}
        };
    }

    public Scene createScene() {
        double width = 1600;
        double height = 900;

        // Sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-1b.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Griglia nave
        grid = new GridPane();
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
                    if (c != null) {
                        ImageView imageView = new ImageView(c.getFrontImage());
                        imageView.setFitWidth(90);
                        imageView.setFitHeight(89);
                        Rectangle clip = new Rectangle(89, 89);
                        clip.setArcWidth(6);
                        clip.setArcHeight(6);
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

        // Slot vuoti per i kept
        keptSlots = new HBox(10);
        keptSlots.setMaxWidth(200);
        keptSlots.setAlignment(Pos.TOP_RIGHT);
        keptSlotsRef.clear();
        keptImages.clear();

        for (int i = 0; i < 2; i++) {
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

            slot.getChildren().add(border);
            keptSlots.getChildren().add(slot);
            keptSlotsRef.add(slot);
            keptImages.add(null);  // Nessuna immagine inizialmente
        }

        StackPane.setAlignment(keptSlots, Pos.TOP_RIGHT);
        StackPane.setMargin(keptSlots, new Insets(30, 50, 0, 0));

        StackPane root = new StackPane(backgroundView, grid, keptSlots);
        scene = new Scene(root, width, height);
        addFinishButtonToScene(scene, viewFlow);
        addStackButtonToScene(scene, viewFlow);
        attachOptionsBoxToScene(scene);
        return scene;
    }

    private void attachOptionsBoxToScene(Scene scene) {
        VBox optionsBox = new VBox(8);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.TOP_LEFT);

        optionsBox.getChildren().addAll(stackBtn, finishBtn);  // Aggiungi qui altri bottoni se ne hai

        Platform.runLater(() -> {
            StackPane root = (StackPane) scene.getRoot();
            if (!root.getChildren().contains(optionsBox)) {
                StackPane.setAlignment(optionsBox, Pos.TOP_RIGHT);
                StackPane.setMargin(optionsBox, new Insets(20, 0, 0, 5));
                root.getChildren().add(optionsBox);
            }
        });
    }

    private Scene addFinishButtonToScene(Scene scene, ViewFlow viewFlow) {
        StackPane root = (StackPane) scene.getRoot(); // Assumiamo che root sia StackPane

        // VBox contenitore dei bottoni
        VBox optionsBox = new VBox(8);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.TOP_LEFT);

        // Stile comune
        Consumer<Button> styleButton = (btn) -> {
            btn.setFont(Font.font("Bangers", 12));
            btn.setTextFill(Color.BLACK);
            btn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            btn.setCursor(Cursor.HAND);
            btn.setPrefWidth(120);
        };

        // === Bottone Finish ===
        Button finishBtn = new Button("Finish spaceship");
        styleButton.accept(finishBtn);

        finishBtn.setOnAction(e -> {
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

        optionsBox.getChildren().add(finishBtn);

        // Posiziona in alto a sinistra nella scena
        StackPane.setAlignment(optionsBox, Pos.TOP_LEFT);
        StackPane.setMargin(optionsBox, new Insets(20, 0, 0, 5));

        root.getChildren().add(optionsBox);
        this.finishBtn = finishBtn;
        return scene;
    }

    private Scene addStackButtonToScene(Scene scene, ViewFlow viewFlow) {
        StackPane root = (StackPane) scene.getRoot(); // Assumiamo che sia uno StackPane

        VBox container = new VBox(8);
        container.setAlignment(Pos.TOP_LEFT);
        container.setPadding(new Insets(10));

        // Stile comune ai bottoni
        Consumer<Button> styleButton = (btn) -> {
            btn.setFont(Font.font("Bangers", 12));
            btn.setTextFill(Color.BLACK);
            btn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            btn.setCursor(Cursor.HAND);
            btn.setPrefWidth(120);
        };

        // === Bottone "Show stack"
        Button stackBtn = new Button("Show stack");
        styleButton.accept(stackBtn);

        stackBtn.setOnAction(e -> {
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
                    root.getChildren().remove(root.lookup("#popupOverlay"));
                });
                stackChoices.getChildren().add(pile);
            }

            StackPane popup = new StackPane(bg, stackChoices);
            popup.setId("popupOverlay");
            popup.setOnMouseClicked(ev -> root.getChildren().remove(popup));

            root.getChildren().add(popup);
        });

        container.getChildren().add(stackBtn);

        StackPane.setAlignment(container, Pos.TOP_LEFT);
        StackPane.setMargin(container, new Insets(20, 0, 0, 140)); // leggermente spostato

        root.getChildren().add(container);
        this.stackBtn = stackBtn;
        return scene;
    }





    public Scene getScene() {
        return scene;
    }

    /**
     * Inserisce un'immagine (es. componente) nella cella specificata
     */
    public void insertComponentImage(int row, int col, ImageView imageView) {
        if (!validCells[row][col]) return;
        StackPane cell = cellMatrix[row][col];
        if (cell != null) {
            cell.getChildren().clear();

            imageView.setFitWidth(90);
            imageView.setFitHeight(89);
            Rectangle clip = new Rectangle(89, 89);
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
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        Rectangle clip = new Rectangle(70, 70);
        clip.setArcWidth(6);
        clip.setArcHeight(6);
        imageView.setClip(clip);

        final int finalIndex = index;
        imageView.setOnMouseClicked(event -> {
            viewFlow.handleInput("selectkept " + finalIndex);
            event.consume();
        });

        slot.getChildren().add(imageView);
        keptImages.set(index, imageView);
    }

    public void removeKeptComponent(int index) {
        if (index < 0 || index >= keptSlotsRef.size()) return;

        StackPane slot = keptSlotsRef.get(index);
        slot.getChildren().removeIf(n -> n instanceof ImageView);
        keptImages.set(index, null);
    }

    public ArrayList<StackPane> getKeptSlotsRef() {
        return keptSlotsRef;
    }
}

