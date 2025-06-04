package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.view.ViewFlow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;


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
        double width = 850;
        double height = 550;

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

