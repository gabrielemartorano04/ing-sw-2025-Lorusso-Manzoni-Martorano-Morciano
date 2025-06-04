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

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private boolean isLinkEnabled;
    private Button finishBtn;
    private Button stackBtn;
    private Button rulesBtn;


    public BuildingScene(String nickname, Component[][] layout, ViewFlow viewFlow) {
        this.nickname = nickname;
        this.layout = layout;
        this.cellMatrix = new StackPane[10][11];
        this.keptSlotsRef = new ArrayList<>();
        this.keptImages = new ArrayList<>();
        this.viewFlow = viewFlow;
        this.isLinkEnabled = false;

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

    //TODO aggiungere per ogni componente un bottone in modo che premendo parla la link con la posizione x,y
    public Scene createScene() {
        double width = 1600;
        double height = 900;

        // Sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-1b.jpeg").toExternalForm());
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
        keptSlots = new HBox(15);
        keptSlots.setMaxWidth(200);
        keptSlots.setAlignment(Pos.TOP_RIGHT);
        keptSlotsRef.clear();
        keptImages.clear();

        if(!keptSlotsRef.isEmpty()) {
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
                keptSlots.getChildren().add(slot);
                keptSlotsRef.add(slot);
                keptImages.add(null);  // Nessuna immagine inizialmente
            }

            StackPane.setAlignment(keptSlots, Pos.TOP_RIGHT);
            StackPane.setMargin(keptSlots, new Insets(0, 235, 590, 0));
        }

        StackPane root = new StackPane(backgroundView, grid, keptSlots);
        scene = new Scene(root, width, height);
        VBox optionsBox = prepareButtonArea(scene);
        Consumer<Button> styleButton = createDefaultButtonStyler();


        addFinishButtonToScene(optionsBox, viewFlow, styleButton);
        addStackButtonToScene(optionsBox, viewFlow, styleButton);
        addRulesButtonToScene(optionsBox, styleButton);
        attachOptionsBoxToScene(scene);
        return scene;
    }

    private void attachOptionsBoxToScene(Scene scene) {
        VBox optionsBox = new VBox(10);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.TOP_LEFT);

        optionsBox.getChildren().addAll(stackBtn, finishBtn, rulesBtn);

        Platform.runLater(() -> {
            StackPane root = (StackPane) scene.getRoot();
            if (!root.getChildren().contains(optionsBox)) {
                StackPane.setAlignment(optionsBox, Pos.TOP_RIGHT);
                StackPane.setMargin(optionsBox, new Insets(20, 0, 0, 5));
                root.getChildren().add(optionsBox);
            }
        });
    }

    private VBox prepareButtonArea(Scene scene) {
        StackPane root = (StackPane) scene.getRoot();

        VBox optionsBox = new VBox(10);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.TOP_LEFT);

        root.getChildren().add(optionsBox);

        return optionsBox;
    }

    private Consumer<Button> createDefaultButtonStyler() {
        return (btn) -> {
            btn.setFont(Font.font("Bangers", 15));
            btn.setTextFill(Color.BLACK);
            btn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
            btn.setCursor(Cursor.HAND);
            btn.setPrefWidth(180);
        };
    }

    private void addFinishButtonToScene(VBox optionsBox, ViewFlow viewFlow, Consumer<Button> styleButton) {
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
        optionsBox.getChildren().add(finishBtn);
    }

    private void addStackButtonToScene(VBox optionsBox, ViewFlow viewFlow, Consumer<Button> styleButton) {
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
        optionsBox.getChildren().add(stackBtn);
    }

    private void addRulesButtonToScene(VBox optionsBox, Consumer<Button> styleButton) {
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
        optionsBox.getChildren().add(rulesBtn);
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

