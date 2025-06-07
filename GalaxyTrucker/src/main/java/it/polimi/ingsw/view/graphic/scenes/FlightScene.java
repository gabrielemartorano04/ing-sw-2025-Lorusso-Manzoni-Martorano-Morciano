package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.cards.Card;
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
import java.util.stream.Stream;

public class FlightScene {
    private final String nickname;
    private final Component[][] layout;
    private final boolean[][] validCells;
    private final StackPane[][] cellMatrix;
    private Scene scene;
    private GridPane grid;
    private final StackPane root;
    private final ViewFlow viewFlow;
    private Button rulesBtn;
    private Button otherBtn;


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
    }

    public Scene createScene(ArrayList<String> playersNames, int level) {
        double width = 1600;
        double height = 900;

        // Sfondo
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
            }
        }
        root.getChildren().setAll(backgroundView, grid);
        scene = new Scene(root, width, height);
        Consumer<Button> styleButton = createDefaultButtonStyler();

        addRulesButtonToScene(styleButton);
        addShowOtherButtonToScene(viewFlow, styleButton, playersNames);
        attachOptionsBoxToScene(scene);

        return scene;
    }

    private void attachOptionsBoxToScene(Scene scene) {
        VBox optionsBox = new VBox(10);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.TOP_LEFT);

        optionsBox.getChildren().addAll(otherBtn, rulesBtn);

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

    public Scene getScene() {
        return scene;
    }

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

    public void clearCell(int row, int col) {
        if (!validCells[row][col]) return;
        StackPane cell = cellMatrix[row][col];
        if (cell != null) {
            cell.getChildren().clear();
        }
    }

    public void showStateInput() {

    }

    public void showActivateEngineInput() {

    }

    public void showActivateCannonsInput() {

    }

    public void showActivateShieldInput() {

    }

    public void showLandInput() {
        StackPane root = (StackPane) scene.getRoot();

        // VBox per contenere il messaggio e i pulsanti
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10));
        box.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.6), new CornerRadii(10), Insets.EMPTY)));

        Label confirm = new Label("Would you like to land?");
        confirm.setTextFill(Color.web("#FFDE69"));
        confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 24));

        Button yes = new Button("YES");
        yes.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        yes.setTextFill(Color.WHITE);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), Insets.EMPTY)));
        yes.setCursor(Cursor.HAND);
        yes.setPrefWidth(80);

        Button no = new Button("NO");
        no.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
        no.setTextFill(Color.WHITE);
        no.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
        no.setCursor(Cursor.HAND);
        no.setPrefWidth(80);

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER_LEFT);

        box.getChildren().addAll(confirm, buttons);

        // Posiziona VBox in basso a sinistra usando un AnchorPane
        AnchorPane anchorPane = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 20.0);
        AnchorPane.setLeftAnchor(box, 20.0);

        yes.setOnAction(ev -> {
            viewFlow.handleInput("end");
            root.getChildren().remove(anchorPane);
        });

        no.setOnAction(ev -> root.getChildren().remove(anchorPane));

        root.getChildren().add(anchorPane);
    }

    public void showHumanCoordInput() {

    }

    public void showPlotGoodInput() {

    }

    public void showRollDiceInput() {

    }

    public void showPlanetDecisionInput() {

    }

    public void showDrawCardInput() {

    }

    public void showRemotionConfirmedInput() {

    }

    public void showRemoveBatteriesInput() {

    }

    public void showRemoveGoodsInput() {

    }

    public void showRearrangeInput() {

    }

    public void showCosmicCreditsInput() {

    }

    public void showWarzoneInput() {

    }
}
