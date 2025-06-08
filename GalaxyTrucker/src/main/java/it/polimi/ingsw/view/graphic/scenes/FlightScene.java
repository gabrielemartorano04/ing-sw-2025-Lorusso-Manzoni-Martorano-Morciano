package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.view.ViewFlow;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
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
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.TOP_RIGHT);
        grid.setTranslateX(-57);
        grid.setTranslateY(74);

        for (int row = 5; row < 10; row++) {
            for (int col = 4; col < 11; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(109, 109);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 6px; -fx-background-radius: 6px;");

                    Component c = layout[row][col];
                    if (c != null) {
                        ImageView imageView = new ImageView(c.getFrontImage());
                        imageView.setFitWidth(109);
                        imageView.setFitHeight(109);
                        Rectangle clip = new Rectangle(109, 109);
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
        StackPane root = (StackPane) scene.getRoot();

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

        AnchorPane anchorPane = new AnchorPane(box);
        anchorPane.setPickOnBounds(false); // lascia passare i click al di fuori del box
        AnchorPane.setBottomAnchor(box, 20.0);
        AnchorPane.setLeftAnchor(box, 20.0);

        // Comportamento "NO"
        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            root.getChildren().remove(anchorPane);
        });

        // Comportamento "YES"
        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert the coordinates of double engines and battery storages");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));

            Label hintLabel = new Label("Type '<x1> <y1>, <x2> <y2>, ... , battery, <xs1> <ys1> - <numofbatteries>, ...'");
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
        });

        root.getChildren().add(anchorPane);
    }

    public void showActivateCannonsInput() {
        StackPane root = (StackPane) scene.getRoot();

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

        AnchorPane anchorPane = new AnchorPane(box);
        anchorPane.setPickOnBounds(false); // lascia passare i click al di fuori del box
        AnchorPane.setBottomAnchor(box, 20.0);
        AnchorPane.setLeftAnchor(box, 20.0);

        // Comportamento "NO"
        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            root.getChildren().remove(anchorPane);
        });

        // Comportamento "YES"
        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert the coordinates of double cannons and battery storages");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));

            Label hintLabel = new Label("Type '<x1> <y1>, <x2> <y2>, ... , battery, <xs1> <ys1> - <numofbatteries>, ...'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 2 3, 4 5, battery, 6 7 - 2");
            inputField.setPrefWidth(250);

            Button confirmBtn = new Button("Confirm");
            confirmBtn.setFont(Font.font("Bangers",  14));
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
        });

        root.getChildren().add(anchorPane);
    }

    public void showActivateShieldInput() {
        StackPane root = (StackPane) scene.getRoot();

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

        AnchorPane anchorPane = new AnchorPane(box);
        anchorPane.setPickOnBounds(false); // lascia passare i click al di fuori del box
        AnchorPane.setBottomAnchor(box, 20.0);
        AnchorPane.setLeftAnchor(box, 20.0);

        // Comportamento "NO"
        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            root.getChildren().remove(anchorPane);
        });

        // Comportamento "YES"
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
        });

        root.getChildren().add(anchorPane);
    }

    public void showLandInput() {
        StackPane root = (StackPane) scene.getRoot();

        // VBox per contenere il messaggio e i pulsanti
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

        // Posiziona VBox in basso a sinistra usando un AnchorPane
        AnchorPane anchorPane = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 20.0);
        AnchorPane.setLeftAnchor(box, 20.0);

        yes.setOnAction(ev -> {
            viewFlow.handleInput("y");
            root.getChildren().remove(anchorPane);
        });

        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            root.getChildren().remove(anchorPane);
        });

        root.getChildren().add(anchorPane);
    }

    public void showHumanCoordInput() {
        StackPane root = (StackPane) scene.getRoot();

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        AnchorPane anchorPane = new AnchorPane(box);
        anchorPane.setPickOnBounds(false); // lascia passare i click al di fuori del box
        AnchorPane.setBottomAnchor(box, 20.0);
        AnchorPane.setLeftAnchor(box, 20.0);

        box.getChildren().clear();

        Label insertLabel = new Label("Insert the coordinates of the cabin from which to remove the crew members");
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

        root.getChildren().add(anchorPane);
    }

    public void showPlotGoodInput() {
        StackPane root = (StackPane) scene.getRoot();

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label confirm = new Label("Would you like to gain the goods?");
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

        AnchorPane anchorPane = new AnchorPane(box);
        anchorPane.setPickOnBounds(false); // lascia passare i click al di fuori del box
        AnchorPane.setBottomAnchor(box, 20.0);
        AnchorPane.setLeftAnchor(box, 20.0);

        // Comportamento "NO"
        no.setOnAction(ev -> {
            viewFlow.handleInput("n");
            root.getChildren().remove(anchorPane);
        });

        // Comportamento "YES"
        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert the coordinates of the loads and the position inside");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));

            Label hintLabel = new Label("Type '<x> <y>, <position>'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 2 3, 3");
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
        });

        root.getChildren().add(anchorPane);
    }

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

        Label resultLabel = new Label("");
        resultLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 60));
        resultLabel.setTextFill(Color.WHITE);
        resultLabel.setVisible(false);

        box.getChildren().addAll(diceLabel, rollButton);

        StackPane overlayPane = new StackPane(overlay, box);

        root.getChildren().add(overlayPane);

        // === Azione pulsante ===
        rollButton.setOnAction(ev -> {
            root.getChildren().remove(overlayPane);
            viewFlow.handleInput("roll");
        });
    }

    public void showPlanetDecisionInput() {
        StackPane root = (StackPane) scene.getRoot();

        // VBox per contenere il messaggio e i pulsanti
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

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

        box.getChildren().addAll(confirm, buttons);

        // Posiziona VBox in basso a sinistra usando un AnchorPane
        AnchorPane anchorPane = new AnchorPane(box);
        AnchorPane.setBottomAnchor(box, 20.0);
        AnchorPane.setLeftAnchor(box, 20.0);

        yes.setOnAction(ev -> {
            box.getChildren().clear();

            Label insertLabel = new Label("Insert the index of the planet you want to land on");
            insertLabel.setTextFill(Color.web("#FFDE69"));
            insertLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));

            Label hintLabel = new Label("Type '<index>'");
            hintLabel.setTextFill(Color.LIGHTGRAY);
            hintLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            TextField inputField = new TextField();
            inputField.setPromptText("Ex. 2");
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
        });

        no.setOnAction(ev -> {
            viewFlow.handleInput("-1");
            root.getChildren().remove(anchorPane);
        });

        root.getChildren().add(anchorPane);
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
            resultBox.setAlignment(Pos.CENTER_LEFT);
            resultBox.setPadding(new Insets(15));

            StackPane.setAlignment(resultBox, Pos.CENTER_LEFT);
            StackPane.setMargin(resultBox, new Insets(20));

            root.getChildren().add(resultBox);
        }));

        rollAnimation.play();
    }

    //TODO aggiungere che deve sparire quando si chiude l'insert index di planet decision
    public void showPlanets(ArrayList<Trucker> planets) {
        // === Contenitore verticale per la lista ===
        VBox planetBox = new VBox(8);
        planetBox.setAlignment(Pos.BOTTOM_CENTER);
        planetBox.setPadding(new Insets(12));

        // === Titolo ===
        Label title = new Label("List of available planets");
        title.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#FFDE69"));
        planetBox.getChildren().add(title);

        // === Riga per ogni pianeta ===
        for (int i = 0; i < planets.size(); i++) {
            Trucker t = planets.get(i);
            String status = (t == null) ? "free" : t.getNickname();

            Text planetLabel = new Text("Planet " + i + ": ");
            planetLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 16));
            planetLabel.setFill(Color.WHITE);

            Text statusText = new Text(status);
            statusText.setFont(Font.font("Bangers", FontWeight.NORMAL, 16));
            statusText.setFill(Color.WHITE);

            TextFlow line = new TextFlow(planetLabel, statusText);
            line.setTextAlignment(TextAlignment.CENTER);
            planetBox.getChildren().add(line);
        }

        // === Posizionamento in basso al centro ===
        StackPane.setAlignment(planetBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(planetBox, new Insets(0, 0, 30, 0));

        // === Aggiungi al root ===
        root.getChildren().add(planetBox);
    }
}
