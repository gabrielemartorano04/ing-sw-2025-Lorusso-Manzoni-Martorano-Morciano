package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Epidemic;
import it.polimi.ingsw.model.cards.OpenSpace;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.view.UI;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.function.Consumer;

public class GUIController {

    @FXML
    private StackPane rootPane;

    private GUI gui;

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void showWelcome(EnumSet<PlayerColors> availableColors) {
        double width = 850;
        double height = 550;

        // Immagine di sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/WelcomeImage.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Bottone START (interattivo)
        Button continueButton = new Button("START");
        continueButton.setFont(Font.font("Bangers", 20));
        continueButton.setTextFill(Color.WHITE);
        continueButton.setStyle("-fx-background-color: rgba(255,222,105,0.7);");

        // Azione quando si clicca il pulsante
        continueButton.setOnAction(e -> showChooseColor(availableColors));

        // Layout della schermata
        StackPane screen = new StackPane(backgroundView, continueButton);
        StackPane.setAlignment(continueButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(continueButton, new Insets(0, 0, 25, 0));

        // Sostituisce il contenuto del rootPane (FXML)
        Platform.runLater(() -> {
            rootPane.getChildren().setAll(screen);
            rootPane.setPrefSize(width, height);
        });
    }

    public void showWelcome() {

    }

    public void showInsertNumPlayers() {
        double width = 850;
        double height = 550;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        Label questionLabel = new Label("Insert the number of truckers and flight mode");
        questionLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        questionLabel.setTextFill(Color.web("#FFDE69"));

        TextField numberField = new TextField();
        numberField.setMaxWidth(200);
        numberField.setAlignment(Pos.CENTER);

        ToggleGroup levelGroup = new ToggleGroup();
        RadioButton trialButton = new RadioButton("Trial Flight");
        trialButton.setToggleGroup(levelGroup);
        trialButton.setUserData(0);
        trialButton.setTextFill(Color.WHITE);

        RadioButton fullButton = new RadioButton("Full Game");
        fullButton.setToggleGroup(levelGroup);
        fullButton.setUserData(2);
        fullButton.setTextFill(Color.WHITE);

        HBox levelBox = new HBox(20, trialButton, fullButton);
        levelBox.setAlignment(Pos.CENTER);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", 12));

        Button submitButton = new Button("START");
        submitButton.setFont(Font.font("Bangers", 16));
        submitButton.setDisable(true);
        submitButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

        numberField.textProperty().addListener((obs, oldText, newText) -> {
            boolean valid = !newText.trim().isEmpty();
            submitButton.setDisable(!valid || levelGroup.getSelectedToggle() == null);
            submitButton.setStyle((valid && levelGroup.getSelectedToggle() != null) ?
                    "-fx-background-color: #FFDE69; -fx-text-fill: black;" :
                    "-fx-background-color: gray; -fx-text-fill: white;");
        });

        levelGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            boolean valid = !numberField.getText().trim().isEmpty();
            submitButton.setDisable(newToggle == null || !valid);
            submitButton.setStyle((newToggle != null && valid) ?
                    "-fx-background-color: #FFDE69; -fx-text-fill: black;" :
                    "-fx-background-color: gray; -fx-text-fill: white;");
        });

        submitButton.setOnAction(e -> {
            String input = numberField.getText().trim();
            Toggle selected = levelGroup.getSelectedToggle();
            try {
                int num = Integer.parseInt(input);
                if (num < 2 || num > 4) {
                    errorLabel.setText("Number must be between 2 and 4.");
                } else if (selected == null) {
                    errorLabel.setText("Please select a level.");
                } else {
                    int level = (int) selected.getUserData();
                    errorLabel.setText("");
                    gui.setCurrentEvent(new CreateGameEvent(num, level));
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid number.");
            }
        });

        content.getChildren().addAll(questionLabel, numberField, levelBox, errorLabel, submitButton);

        StackPane insertPane = new StackPane(backgroundView, content);

        Platform.runLater(() -> {
            rootPane.getChildren().setAll(insertPane);
            rootPane.setPrefSize(width, height);
        });
    }

    public void showChooseColor(EnumSet<PlayerColors> availableColors) {
        double width = 850;
        double height = 550;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        Label nicknameLabel = new Label("Insert your nickname and choose a color");
        nicknameLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 24));
        nicknameLabel.setTextFill(Color.web("#FFDE69"));
        nicknameLabel.setAlignment(Pos.CENTER);

        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Enter your nickname");
        nicknameField.setMaxWidth(200);
        nicknameField.setAlignment(Pos.CENTER);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", 12));

        TilePane colorPane = new TilePane();
        colorPane.setHgap(15);
        colorPane.setVgap(15);
        colorPane.setAlignment(Pos.CENTER);

        Map<PlayerColors, StackPane> colorBoxes = new HashMap<>();
        final PlayerColors[] selectedColor = {null};

        Button doneButton = new Button("DONE");
        doneButton.setFont(Font.font("Bangers", 16));
        doneButton.setDisable(true);
        doneButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

        for (PlayerColors color : availableColors) {
            Rectangle rect = new Rectangle(60, 60);
            rect.setArcWidth(18);
            rect.setArcHeight(18);

            Color fxColor = switch (color) {
                case RED -> Color.RED;
                case GREEN -> Color.LIMEGREEN;
                case BLUE -> Color.DODGERBLUE;
                case YELLOW -> Color.GOLD;
                default -> Color.GRAY;
            };

            rect.setFill(fxColor);

            Label checkmark = new Label("✓");
            checkmark.setTextFill(Color.WHITE);
            checkmark.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            checkmark.setVisible(false);

            StackPane stack = new StackPane(rect, checkmark);
            stack.setPrefSize(60, 60);
            stack.setCursor(Cursor.HAND);

            stack.setOnMouseClicked(e -> {
                colorBoxes.values().forEach(box -> ((Label) box.getChildren().get(1)).setVisible(false));
                checkmark.setVisible(true);
                selectedColor[0] = color;

                boolean nicknameOk = !nicknameField.getText().trim().isEmpty();
                doneButton.setDisable(!nicknameOk);
                doneButton.setStyle(nicknameOk ?
                        "-fx-background-color: #FFDE69; -fx-text-fill: black;" :
                        "-fx-background-color: gray; -fx-text-fill: white;");
            });

            colorBoxes.put(color, stack);
            colorPane.getChildren().add(stack);
        }

        doneButton.setOnAction(e -> {
            String nickname = nicknameField.getText().trim();
            if (nickname.isEmpty()) {
                errorLabel.setText("Please enter a nickname.");
            } else if (selectedColor[0] == null) {
                errorLabel.setText("Please choose a color.");
            } else {
                errorLabel.setText("");
                gui.setCurrentEvent(new LoginEvent(nickname, selectedColor[0]));
            }
        });

        nicknameField.textProperty().addListener((obs, oldText, newText) -> {
            boolean nicknameOk = !newText.trim().isEmpty();
            boolean colorOk = selectedColor[0] != null;
            doneButton.setDisable(!(nicknameOk && colorOk));
            doneButton.setStyle((nicknameOk && colorOk) ?
                    "-fx-background-color: #FFDE69; -fx-text-fill: black;" :
                    "-fx-background-color: gray; -fx-text-fill: white;");
        });

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        VBox.setMargin(nicknameLabel, new Insets(0, 0, 30, 0));
        VBox.setMargin(nicknameField, new Insets(0, 0, 30, 0));
        VBox.setMargin(colorPane, new Insets(0, 0, 20, 0));
        VBox.setMargin(doneButton, new Insets(5, 0, 0, 0));
        VBox.setMargin(errorLabel, new Insets(5, 0, 0, 0));

        content.getChildren().addAll(
                nicknameLabel,
                nicknameField,
                colorPane,
                doneButton,
                errorLabel
        );

        StackPane screen = new StackPane(backgroundView, content);
        StackPane.setAlignment(content, Pos.CENTER);

        Platform.runLater(() -> {
            rootPane.getChildren().clear();
            rootPane.getChildren().add(screen);
        });
    }

    public void showColorTaken() {
        double width = rootPane.getWidth();
        double height = rootPane.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label errorLabel = new Label("ERROR! Colour already taken");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 28));
        errorLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        StackPane popup = new StackPane(overlay, errorLabel);
        popup.setMouseTransparent(true);
        StackPane.setAlignment(errorLabel, Pos.CENTER);

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(popup)) {
                rootPane.getChildren().add(popup);
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> rootPane.getChildren().remove(popup));
                delay.play();
            }
        });
    }

    public void showNicknameTaken() {
        double width = rootPane.getWidth();
        double height = rootPane.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label errorLabel = new Label("ERROR! Nickname already taken");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 28));
        errorLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        StackPane popup = new StackPane(overlay, errorLabel);
        popup.setMouseTransparent(true);
        StackPane.setAlignment(errorLabel, Pos.CENTER);

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(popup)) {
                rootPane.getChildren().add(popup);
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> rootPane.getChildren().remove(popup));
                delay.play();
            }
        });
    }

    public void showPlayerJoined(String nickname) {
        Label message = new Label(nickname + " has joined");
        message.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.8); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-background-radius: 10px;"
        );
        message.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        StackPane overlay = new StackPane(message);
        overlay.setMouseTransparent(true);
        StackPane.setAlignment(message, Pos.TOP_RIGHT);
        StackPane.setMargin(message, new Insets(15, 15, 0, 0));

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(overlay)) {
                rootPane.getChildren().add(overlay);
                PauseTransition delay = new PauseTransition(Duration.seconds(5));
                delay.setOnFinished(e -> rootPane.getChildren().remove(overlay));
                delay.play();
            }
        });
    }

    public void showWaitingForPlayers(int currentPlayers) {
        double width = 850;
        double height = 550;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        Label loadingLabel = new Label("Waiting for truckers to join");
        loadingLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 26));
        loadingLabel.setTextFill(Color.web("#FFDE69"));

        VBox content = new VBox(loadingLabel);
        content.setAlignment(Pos.CENTER);

        StackPane newRoot = new StackPane(backgroundView, content);
        StackPane.setAlignment(content, Pos.CENTER);

        Platform.runLater(() -> {
            rootPane.getChildren().setAll(newRoot.getChildren());
        });

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> loadingLabel.setText("Waiting for truckers to join")),
                new KeyFrame(Duration.seconds(0.5), e -> loadingLabel.setText("Waiting for truckers to join.")),
                new KeyFrame(Duration.seconds(1), e -> loadingLabel.setText("Waiting for truckers to join..")),
                new KeyFrame(Duration.seconds(1.5), e -> loadingLabel.setText("Waiting for truckers to join...")),
                new KeyFrame(Duration.seconds(2.0), e -> {})
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void showStartCountdown(int secondsLeft) {
        double width = 850;
        double height = 550;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        Label message = new Label("The flight will start in...");
        message.setFont(Font.font("Bangers", FontWeight.BOLD, 32));
        message.setTextFill(Color.web("#FFDE69"));

        Label countdownLabel = new Label();
        countdownLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 80));
        countdownLabel.setTextFill(Color.WHITE);

        VBox content = new VBox(30, message, countdownLabel);
        content.setAlignment(Pos.CENTER);

        StackPane newRoot = new StackPane(backgroundView, content);

        Platform.runLater(() -> {
            rootPane.getChildren().setAll(newRoot.getChildren());
        });

        Timeline timeline = new Timeline();
        for (int i = secondsLeft; i > 0; i--) {
            int current = i;
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(secondsLeft - i), e -> {
                        countdownLabel.setText(String.valueOf(current));
                        countdownLabel.setTextFill(Color.web("#FFDE69"));
                    })
            );
        }

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(secondsLeft), e -> {
                    countdownLabel.setText("GO!");
                    countdownLabel.setTextFill(Color.web("#FFDE69"));
                })
        );

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(secondsLeft + 1))
        );

        timeline.play();
    }

    public void showSetupComplete() {
        double width = 850;
        double height = 550;

        // Sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Testo centrale
        Label message = new Label("Everything is ready to play");
        message.setFont(Font.font("Bangers", FontWeight.BOLD, 40));
        message.setTextFill(Color.web("#FFDE69"));

        StackPane newRoot = new StackPane(backgroundView, message);
        StackPane.setAlignment(message, Pos.CENTER);

        Platform.runLater(() -> {
            rootPane.getChildren().setAll(newRoot.getChildren());
        });
    }

    public void showPlayers(ArrayList<String> players) {

    }

    public void showBuildPhaseStart(ArrayList<Component> availableComponents) {
        double width = 850;
        double height = 550;

        // Sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Messaggio centrale
        Label message = new Label("Start building your spaceship");
        message.setFont(Font.font("Bangers", FontWeight.BOLD, 40));
        message.setTextFill(Color.web("#FFDE69"));

        StackPane newRoot = new StackPane(backgroundView, message);
        StackPane.setAlignment(message, Pos.CENTER);

        Platform.runLater(() -> {
            rootPane.getChildren().setAll(newRoot.getChildren());

            // Dopo 3 secondi, mostra i componenti
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> showAvailableComponent(availableComponents));
            delay.play();
        });
    }

    public void showAvailableComponent(ArrayList<Component> components) {
        double width = 850;
        double height = 550;

        // Sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        int col = 0;
        int row = 0;
        for (Component component : components) {
            ImageView imgView = new ImageView(component.getCovered() ? component.getBackImage() : component.getFrontImage());
            imgView.setFitWidth(70);
            imgView.setFitHeight(70);

            Rectangle clip = new Rectangle(75, 75);
            clip.setArcWidth(16);
            clip.setArcHeight(16);
            imgView.setClip(clip);

            Button button = new Button();
            button.setGraphic(imgView);
            button.setPrefSize(75, 75);
            button.setStyle("-fx-background-color: transparent; -fx-border-color: #cccccc; -fx-border-radius: 8px; -fx-cursor: hand; -fx-padding: 0px;");

            button.setOnAction(e -> {
                gui.setSelectedComponent(component);
                showSelectComponent();  // Mostra sempre in overlay
                showDiscardComponent(component);

                if (!component.getCovered()) {
                    Dragboard db = button.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString("component");
                    db.setContent(content);
                    db.setDragView(imgView.getImage());
                }
            });

            grid.add(button, col, row);
            col++;
            if (col == 8) {
                col = 0;
                row++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(new StackPane(grid));
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        StackPane newRoot = new StackPane(backgroundView, scrollPane);

        Platform.runLater(() -> {
            rootPane.getChildren().setAll(newRoot.getChildren());
        });
    }

    public void showKeptComponents(ArrayList<Component> kept) {
        HBox keptSlots = new HBox(10);
        keptSlots.setMaxWidth(200);
        keptSlots.setAlignment(Pos.TOP_RIGHT);

        for (int i = 0; i < 2; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(80, 80);
            slot.setMinSize(80, 80);
            slot.setMaxSize(80, 80);
            slot.setStyle("-fx-background-color: transparent;");

            Rectangle border = new Rectangle(80, 80);
            border.setArcWidth(3);
            border.setArcHeight(3);
            border.setFill(Color.TRANSPARENT);
            border.setStroke(Color.WHITE);
            border.setStrokeWidth(2);

            int index = i;

            if (kept.get(i) != null) {
                Component comp = kept.get(i);
                ImageView imageView = new ImageView(comp.getFrontImage());
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);

                Rectangle clip = new Rectangle(80, 80);
                clip.setArcWidth(6);
                clip.setArcHeight(6);
                imageView.setClip(clip);

                imageView.setOnDragDetected(event -> {
                    Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString("kept:" + index);
                    db.setContent(content);
                    db.setDragView(imageView.snapshot(null, null));
                    gui.setSelectedComponent(comp); // usa il setter della GUI
                    event.consume();
                });

                slot.getChildren().addAll(border, imageView);
            } else {
                slot.getChildren().add(border);
            }

            slot.setOnDragOver(event -> {
                if (event.getGestureSource() != slot && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            slot.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    gui.setCurrentEvent(new KeepTileEvent(gui.getSelectedComponent()));
                    gui.setSelectedComponent(null);
                    event.setDropCompleted(true);
                }
                event.consume();
            });

            keptSlots.getChildren().add(slot);
        }

        StackPane.setAlignment(keptSlots, Pos.TOP_RIGHT);
        StackPane.setMargin(keptSlots, new Insets(28, 36, 0, 0));

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(keptSlots)) {
                rootPane.getChildren().add(keptSlots);
            }
        });
    }

    public void showSelectComponent() {
        Component selectedComponent = gui.getSelectedComponent();
        if (selectedComponent == null) return;

        double width = 850;
        double height = 550;

        // Overlay grigio
        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        ImageView imgView = new ImageView(selectedComponent.getFrontImage());
        imgView.setFitWidth(150);
        imgView.setFitHeight(150);

        Rectangle clip = new Rectangle(150, 150);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imgView.setClip(clip);

        StackPane popup = new StackPane(imgView);
        popup.setAlignment(Pos.CENTER);

        imgView.setOnDragDetected(e -> {
            Dragboard db = imgView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("component");
            db.setContent(content);
            db.setDragView(imgView.snapshot(null, null));

            rootPane.getChildren().removeAll(overlay, popup);
            e.consume();
        });

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(popup)) {
                rootPane.getChildren().addAll(overlay, popup);
            }
        });
    }

    public void showBuildOptions() {
        VBox optionsBox = new VBox(8);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.TOP_LEFT);

        // Stile compatto per bottoni
        Consumer<Button> styleButton = (btn) -> {
            btn.setFont(Font.font("Bangers", 12));
            btn.setTextFill(Color.BLACK);
            btn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), CornerRadii.EMPTY, Insets.EMPTY)));
            btn.setCursor(Cursor.HAND);
            btn.setPrefWidth(120);
        };

        // Pulsante Select Component
        Button selectComponentBtn = new Button("Select component");
        styleButton.accept(selectComponentBtn);
        selectComponentBtn.setOnAction(e -> {
            //TODO come devo passarli?
            gui.showAvailableComponent(null);
        });

        // Pulsante Finish Ship
        Button finishBtn = new Button("Finish spaceship");
        styleButton.accept(finishBtn);
        finishBtn.setOnAction(e -> {
            StackPane confirmation = new StackPane();
            Rectangle bg = new Rectangle(850, 550, Color.rgb(0, 0, 0, 0.4));

            VBox box = new VBox(20);
            box.setAlignment(Pos.CENTER);

            Label confirm = new Label("Confirm finish building?");
            confirm.setTextFill(Color.web("#FFDE69"));
            confirm.setFont(Font.font("Bangers", FontWeight.BOLD, 32));

            Button yes = new Button("YES");
            yes.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
            yes.setTextFill(Color.WHITE);
            yes.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            yes.setCursor(Cursor.HAND);
            yes.setPrefWidth(100);

            Button no = new Button("NO");
            no.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
            no.setTextFill(Color.WHITE);
            no.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            no.setCursor(Cursor.HAND);
            no.setPrefWidth(100);

            yes.setOnAction(ev -> {
                gui.setCurrentEvent(new EndBuildingEvent());
                rootPane.getChildren().remove(confirmation);
            });

            no.setOnAction(ev -> rootPane.getChildren().remove(confirmation));

            HBox buttons = new HBox(20, yes, no);
            buttons.setAlignment(Pos.CENTER);

            box.getChildren().addAll(confirm, buttons);
            confirmation.getChildren().addAll(bg, box);

            rootPane.getChildren().add(confirmation);
        });

        // Pulsante Show Stack
        Button stackBtn = new Button("Show stack");
        styleButton.accept(stackBtn);
        stackBtn.setOnAction(e -> {
            VBox stackChoices = new VBox(15);
            stackChoices.setAlignment(Pos.CENTER);

            for (int i = 0; i < 3; i++) {
                Button pile = new Button("Stack " + (i + 1));
                styleButton.accept(pile);
                pile.setFont(Font.font("Bangers", FontWeight.BOLD, 18));
                int index = i;
                pile.setOnAction(ev -> {
                    gui.setCurrentEvent(new ShowStackEvent(index));
                    //TODO come devo passarlo?
                    gui.showStack(null);
                    rootPane.getChildren().remove(stackChoices.getParent());
                });
                stackChoices.getChildren().add(pile);
            }

            StackPane popup = new StackPane(new Rectangle(850, 550, Color.rgb(0, 0, 0, 0.4)), stackChoices);
            popup.setOnMouseClicked(ev -> rootPane.getChildren().remove(popup));
            rootPane.getChildren().add(popup);
        });

        // Pulsante Hourglass con countdown
        Button hourglassBtn = new Button("⏳");
        hourglassBtn.setFont(Font.font("Bangers", 20));
        hourglassBtn.setTextFill(Color.BLACK);
        hourglassBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), CornerRadii.EMPTY, Insets.EMPTY)));
        hourglassBtn.setPrefWidth(70);
        hourglassBtn.setCursor(Cursor.HAND);

        hourglassBtn.setOnAction(e -> {
            hourglassBtn.setDisable(true);
            gui.setCurrentEvent(new TurnHourglassEvent());

            IntegerProperty seconds = new SimpleIntegerProperty(90);
            hourglassBtn.textProperty().bind(seconds.asString("%ds"));

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                seconds.set(seconds.get() - 1);
                if (seconds.get() <= 0) {
                    hourglassBtn.setDisable(false);
                    hourglassBtn.textProperty().unbind();
                    hourglassBtn.setText("⏳");
                }
            }));
            timeline.setCycleCount(90);
            timeline.play();
        });

        // Ordine: Select - Finish - Stack - Hourglass
        optionsBox.getChildren().addAll(selectComponentBtn, finishBtn, stackBtn, hourglassBtn);

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(optionsBox)) {
                StackPane.setAlignment(optionsBox, Pos.TOP_RIGHT);
                StackPane.setMargin(optionsBox, new Insets(18, 0, 0, 18));
                rootPane.getChildren().add(optionsBox);
            }
        });
    }

    public void showDiscardComponent(Component component) {
        gui.setSelectedComponent(component);

        Button discardButton = new Button("Discard");
        discardButton.setFont(Font.font("Bangers", FontWeight.BOLD, 20));
        discardButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        discardButton.setId("discardBtn");

        discardButton.setOnAction(e -> {
            // TODO: allineare con modifiche (es: gui.setCurrentEvent(new DiscardTileEvent(component)));
            gui.setSelectedComponent(null);

            Node toRemove = rootPane.lookup("#discardBtn");
            if (toRemove != null) {
                rootPane.getChildren().remove(toRemove.getParent());  // rimuove l'intero VBox
            }
        });

        VBox buttonBox = new VBox(discardButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(250, 0, 0, 0));

        Platform.runLater(() -> {
            if (rootPane.lookup("#discardBtn") == null) {
                rootPane.getChildren().add(buttonBox);
            }
        });
    }

    public void showComponentOptions() {

    }

    public void showComponentPlaced(Component component, int x, int y) {

    }

    public void showInvalidPlacement(String reason) {

    }

    public void showHourglassTurned() {

    }

    public void showTimeExpired() {
        // Crea la scritta TIME EXPIRED
        Label message = new Label("TIME EXPIRED");
        message.setTextFill(Color.RED);
        message.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 100));

        // Effetto lampeggiante
        FadeTransition blink = new FadeTransition(Duration.seconds(0.4), message);
        blink.setFromValue(1.0);
        blink.setToValue(0.0);
        blink.setCycleCount(Animation.INDEFINITE);
        blink.setAutoReverse(true);
        blink.play();

        // Crea un rettangolo grigio semitrasparente come sfondo
        Rectangle dimBackground = new Rectangle();
        dimBackground.setFill(Color.rgb(50, 50, 50, 0.6));
        dimBackground.widthProperty().bind(rootPane.widthProperty());
        dimBackground.heightProperty().bind(rootPane.heightProperty());

        // Overlay con sfondo e scritta
        StackPane overlay = new StackPane(dimBackground, message);
        overlay.setAlignment(Pos.CENTER);
        overlay.setMouseTransparent(true);

        Platform.runLater(() -> {
            rootPane.getChildren().add(overlay);

            // Rimuove l'overlay dopo 5 secondi
            PauseTransition wait = new PauseTransition(Duration.seconds(5));
            wait.setOnFinished(e -> rootPane.getChildren().remove(overlay));
            wait.play();
        });
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

        Rectangle overlayBg = new Rectangle();
        overlayBg.setFill(Color.rgb(0, 0, 0, 0.4));
        overlayBg.widthProperty().bind(rootPane.widthProperty());
        overlayBg.heightProperty().bind(rootPane.heightProperty());

        StackPane overlay = new StackPane(overlayBg, textBox);
        overlay.setMouseTransparent(true);
        overlay.setAlignment(Pos.CENTER);

        Timeline animation = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> dots.setText("")),
                new KeyFrame(Duration.seconds(0.5), e -> dots.setText(".")),
                new KeyFrame(Duration.seconds(1.0), e -> dots.setText("..")),
                new KeyFrame(Duration.seconds(1.5), e -> dots.setText("...")),
                new KeyFrame(Duration.seconds(2.0), e -> dots.setText(""))
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(overlay)) {
                rootPane.getChildren().add(overlay);
            }
        });
    }

    public void showBuildComplete() {
        // Sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(850);
        backgroundView.setFitHeight(550);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Testi
        Label line1 = new Label("WELL DONE!");
        line1.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 40));
        line1.setTextFill(Color.web("#FFDE69"));

        Label line2 = new Label("Spaceship built");
        line2.setFont(Font.font("Bangers", FontWeight.BOLD, 32));
        line2.setTextFill(Color.web("#FFDE69"));

        Label line3 = new Label("Get ready for launch!");
        line3.setFont(Font.font("Bangers", FontWeight.BOLD, 32));
        line3.setTextFill(Color.web("#FFDE69"));

        VBox textBox = new VBox(10, line1, line2, line3);
        textBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(backgroundView, textBox);
        StackPane.setAlignment(textBox, Pos.CENTER);

        Platform.runLater(() -> {
            rootPane.getChildren().clear();
            rootPane.getChildren().add(root);
        });
    }

    public void showFlightPhaseStart() {

    }

    public void showCardDrawn(StartCardEvent event) {
        double width = rootPane.getWidth();
        double height = rootPane.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label title = new Label("Card Drawn");
        title.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 36));
        title.setTextFill(Color.web("#FFDE69"));
        title.setEffect(new DropShadow(3, Color.BLACK));

        //TODO bisogna inserire il path
        ImageView cardView = new ImageView("");
        cardView.setFitWidth(250);
        cardView.setFitHeight(400);
        cardView.setPreserveRatio(true);
        cardView.setEffect(new DropShadow(10, Color.BLACK));

        VBox popup = new VBox(20, title, cardView);
        popup.setAlignment(Pos.CENTER);

        StackPane container = new StackPane(overlay, popup);
        container.setMouseTransparent(false);

        Platform.runLater(() -> {
            rootPane.getChildren().add(container);

            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(e -> {
                rootPane.getChildren().remove(container);
                //TODO bisogna inserire la carta
                showCardEffectStart(null);
            });
            delay.play();
        });
    }

    public void showCardEffectStart(Card card) {
        double width = rootPane.getWidth();
        double height = rootPane.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label message = new Label("Effect incoming...");
        message.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 36));
        message.setTextFill(Color.web("#FFDE69"));
        message.setEffect(new DropShadow(3, Color.BLACK));

        StackPane container = new StackPane(overlay, message);
        container.setAlignment(Pos.CENTER);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> rootPane.getChildren().remove(container));

        Platform.runLater(() -> {
            rootPane.getChildren().add(container);
            pause.play();
        });
    }

    public void showCardEffectEnd(Card card) {
        double width = rootPane.getWidth();
        double height = rootPane.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label message = new Label("Effect ended");
        message.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 36));
        message.setTextFill(Color.web("#FFDE69"));
        message.setEffect(new DropShadow(3, Color.BLACK));

        StackPane container = new StackPane(overlay, message);
        container.setAlignment(Pos.CENTER);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> rootPane.getChildren().remove(container));

        Platform.runLater(() -> {
            rootPane.getChildren().add(container);
            pause.play();
        });
    }

    public void showTruckerMoved(String nickname, int newPosition) {

    }

    public void showTruckerWithdrawn(String nickname) {
        Label message = new Label(nickname + " has withdrawn");
        message.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.8); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-background-radius: 10px;"
        );
        message.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        StackPane overlay = new StackPane(message);
        overlay.setMouseTransparent(true);
        StackPane.setAlignment(message, Pos.TOP_RIGHT);
        StackPane.setMargin(message, new Insets(15, 15, 0, 0));

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(overlay)) {
                rootPane.getChildren().add(overlay);
                PauseTransition delay = new PauseTransition(Duration.seconds(5));
                delay.setOnFinished(e -> rootPane.getChildren().remove(overlay));
                delay.play();
            }
        });
    }

    public void showTruckerLostAllCrew(String nickname) {
        Label message = new Label(nickname + " has lost all crew\nand is out");
        message.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.8); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-background-radius: 10px;"
        );
        message.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        message.setTextAlignment(TextAlignment.RIGHT);

        StackPane overlay = new StackPane(message);
        overlay.setMouseTransparent(true);
        StackPane.setAlignment(message, Pos.TOP_RIGHT);
        StackPane.setMargin(message, new Insets(15, 15, 0, 0));

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(overlay)) {
                rootPane.getChildren().add(overlay);
                PauseTransition delay = new PauseTransition(Duration.seconds(5));
                delay.setOnFinished(e -> rootPane.getChildren().remove(overlay));
                delay.play();
            }
        });
    }

    public void showGoodsGained(Goods goods) {

    }

    public void showGameEnded() {

    }

    public void showScores(Map<String, Integer> finalScores) {

    }

    public void showError(String message) {

    }

    public void showConnectionLost() {
        double width = rootPane.getWidth();
        double height = rootPane.getHeight();

        // Sfondo con immagine EmptyWallpaper
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Scritta "CONNECTION LOST"
        Label title = new Label("CONNECTION LOST!");
        title.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 48));
        title.setTextFill(Color.web("#FFDE69"));
        title.setEffect(new DropShadow(5, Color.BLACK));

        // Barrette Wi-Fi
        Rectangle bar1 = new Rectangle(10, 10, Color.web("#FFDE69"));
        Rectangle bar2 = new Rectangle(10, 20, Color.web("#FFDE69"));
        Rectangle bar3 = new Rectangle(10, 30, Color.web("#FFDE69"));
        Rectangle bar4 = new Rectangle(10, 40, Color.web("#FFDE69"));

        List<Rectangle> barsList = Arrays.asList(bar1, bar2, bar3, bar4);
        for (Rectangle bar : barsList) {
            bar.setArcWidth(4);
            bar.setArcHeight(4);
        }

        HBox bars = new HBox(8, bar1, bar2, bar3, bar4);
        bars.setAlignment(Pos.BOTTOM_CENTER);

        // Animazione barrette
        Timeline animation = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> {
                    bar1.setOpacity(1); bar2.setOpacity(0.3); bar3.setOpacity(0.3); bar4.setOpacity(0.3);
                }),
                new KeyFrame(Duration.seconds(0.5), e -> {
                    bar1.setOpacity(1); bar2.setOpacity(1); bar3.setOpacity(0.3); bar4.setOpacity(0.3);
                }),
                new KeyFrame(Duration.seconds(1), e -> {
                    bar1.setOpacity(1); bar2.setOpacity(1); bar3.setOpacity(1); bar4.setOpacity(0.3);
                }),
                new KeyFrame(Duration.seconds(1.5), e -> {
                    bar1.setOpacity(1); bar2.setOpacity(1); bar3.setOpacity(1); bar4.setOpacity(1);
                }),
                new KeyFrame(Duration.seconds(2), e -> {
                    bar1.setOpacity(1); bar2.setOpacity(1); bar3.setOpacity(1); bar4.setOpacity(1);
                })
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();

        VBox popup = new VBox(25, title, bars);
        popup.setAlignment(Pos.CENTER);

        StackPane container = new StackPane(backgroundView, popup);
        container.setMouseTransparent(false);

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(container)) {
                rootPane.getChildren().add(container);
            }
        });
    }

    public void showInvalidCommand(String expectedFormat) {

    }

    public void askIndex() {

    }

    public void askCoordinates() {

    }

    public void showGoodsToRemove(List<Goods> goods) {

    }

    public void askGoodsIndexesToRemove() {

    }

    public void askPlacedComponent() {

    }

    public void showPlayerShipStatus(int position, int batteryCount, int humanCount, int cannonPower, int enginePower, Map<String, Integer> goodsByColor) {
        // Carica le icone corrette
        ImageView posIcon = new ImageView(new Image(getClass().getResource("/it/polimi/ingsw/view/pos_icon.png").toExternalForm()));
        ImageView batteryIcon = new ImageView(new Image(getClass().getResource("/it/polimi/ingsw/view/battery_icon.png").toExternalForm()));
        ImageView humanIcon = new ImageView(new Image(getClass().getResource("/it/polimi/ingsw/view/human_icon.png").toExternalForm()));
        ImageView cannonIcon = new ImageView(new Image(getClass().getResource("/it/polimi/ingsw/view/cannon_icon.png").toExternalForm()));
        ImageView engineIcon = new ImageView(new Image(getClass().getResource("/it/polimi/ingsw/view/engine_icon.png").toExternalForm()));

        List<ImageView> mainIcons = List.of(posIcon, batteryIcon, humanIcon, cannonIcon, engineIcon);
        for (ImageView icon : mainIcons) {
            icon.setFitWidth(15);
            icon.setPreserveRatio(true);
        }

        // Etichette
        Label posLabel = new Label(String.valueOf(position));
        Label battLabel = new Label(String.valueOf(batteryCount));
        Label humanLabel = new Label(String.valueOf(humanCount));
        Label cannonLabel = new Label(String.valueOf(cannonPower));
        Label engineLabel = new Label(String.valueOf(enginePower));

        List<Label> labels = List.of(posLabel, battLabel, humanLabel, cannonLabel, engineLabel);
        for (Label label : labels) {
            label.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            label.setTextFill(Color.WHITE);
        }

        // Blocchi icona + etichetta
        HBox posBox = new HBox(5, posIcon, posLabel);
        HBox battBox = new HBox(5, batteryIcon, battLabel);
        HBox humanBox = new HBox(5, humanIcon, humanLabel);
        HBox cannonBox = new HBox(5, cannonIcon, cannonLabel);
        HBox engineBox = new HBox(5, engineIcon, engineLabel);

        List<HBox> infoBoxes = List.of(posBox, battBox, humanBox, cannonBox, engineBox);
        for (HBox box : infoBoxes) {
            box.setAlignment(Pos.CENTER);
        }

        // Beni per colore
        HBox goodsBox = new HBox(10);
        goodsBox.setAlignment(Pos.CENTER);
        for (Map.Entry<String, Integer> entry : goodsByColor.entrySet()) {
            String color = entry.getKey();
            Integer quantity = entry.getValue();

            // Ogni colore ha la propria immagine (assicurati che esista il file corrispondente)
            Image icon = new Image(getClass().getResource("/it/polimi/ingsw/view/goods_" + color + ".png").toExternalForm());
            ImageView goodIcon = new ImageView(icon);
            goodIcon.setFitWidth(15);
            goodIcon.setPreserveRatio(true);

            Label qtyLabel = new Label(String.valueOf(quantity));
            qtyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            qtyLabel.setTextFill(Color.WHITE);

            HBox singleGood = new HBox(4, goodIcon, qtyLabel);
            singleGood.setAlignment(Pos.CENTER);
            goodsBox.getChildren().add(singleGood);
        }

        // Contenitore finale
        HBox statusBar = new HBox(20, posBox, battBox, humanBox, cannonBox, engineBox, goodsBox);
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(6, 12, 6, 12));
        statusBar.setMouseTransparent(true);

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(statusBar)) {
                StackPane.setAlignment(statusBar, Pos.BOTTOM_CENTER);
                StackPane.setMargin(statusBar, new Insets(0, 0, 8, 0));
                rootPane.getChildren().add(statusBar);
            }
        });
    }

    public void showShipLayout(String nickname, Component[][] layout) {
        double width = 850;
        double height = 550;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-1b.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        boolean[][] validCells = {
                {false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, true,  false, true,  false, false},
                {false, false, false, false, false, true,  true,  true,  true,  true,  false},
                {false, false, false, false, true,  true,  true,  true,  true,  true,  true },
                {false, false, false, false, true,  true,  true,  true,  true,  true,  true },
                {false, false, false, false, true,  true,  true,  false, true,  true,  true }
        };

        GridPane grid = new GridPane();
        grid.setHgap(2.5);
        grid.setVgap(2.5);
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setTranslateX(33);
        grid.setTranslateY(26);

        for (int row = 5; row < 10; row++) {
            for (int col = 4; col < 11; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(105, 97);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 3px; -fx-background-radius: 3px;");

                    Component comp = layout[row][col];
                    if (comp != null) {
                        ImageView imageView = new ImageView(comp.getFrontImage());
                        imageView.setFitWidth(105);
                        imageView.setFitHeight(97);
                        Rectangle clip = new Rectangle(105, 97);
                        clip.setArcWidth(6);
                        clip.setArcHeight(6);
                        imageView.setClip(clip);
                        cell.getChildren().add(imageView);
                    }

                    final int r = row;
                    final int c = col;
                    cell.setOnDragOver(event -> {
                        if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                        event.consume();
                    });

                    cell.setOnDragDropped(event -> {
                        Dragboard db = event.getDragboard();
                        if (db.hasString()) {
                            //TODO inserire gli ingressi corretti
                            gui.setCurrentEvent(new LinkTileEvent(-1,-1, -1, null));
                            gui.setSelectedComponent(null);
                            event.setDropCompleted(true);
                        }
                        event.consume();
                    });
                } else {
                    cell.setStyle("-fx-background-color: transparent;");
                }

                grid.add(cell, col - 4, row - 5);  // visualizza solo la porzione centrale
            }
        }

        Platform.runLater(() -> {
            rootPane.getChildren().clear();
            rootPane.getChildren().addAll(backgroundView, grid);
        });
    }

    public void showMessage(String message) {
        Label title = new Label("New message:");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));

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
        messageBox.setMaxWidth(Region.USE_PREF_SIZE);
        messageBox.setMaxHeight(Region.USE_PREF_SIZE);

        Platform.runLater(() -> {
            StackPane.setAlignment(messageBox, Pos.TOP_RIGHT);
            StackPane.setMargin(messageBox, new Insets(20, 20, 0, 0));
            rootPane.getChildren().add(messageBox);

            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            delay.setOnFinished(e -> rootPane.getChildren().remove(messageBox));
            delay.play();
        });
    }

    public void showTrack(Map<String, Integer> truckers) {
        double width = 850;
        double height = 550;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);

        Image boardImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-5.png").toExternalForm());
        ImageView boardView = new ImageView(boardImage);
        boardView.setFitWidth(width);
        boardView.setFitHeight(height);
        boardView.setPreserveRatio(false);
        boardView.setMouseTransparent(true);

        Pane overlay = new Pane();
        double centerX = width / 2;
        double centerY = height / 2;
        double radiusX = 300;
        double radiusY = 200;

        Map<String, Color> playerColors = Map.of(
                "RED", Color.RED,
                "GREEN", Color.LIMEGREEN,
                "BLUE", Color.DODGERBLUE,
                "YELLOW", Color.GOLD
        );

        for (int i = 0; i < 24; i++) {
            double angle = 2 * Math.PI * i / 24 - Math.PI / 2;
            double x = centerX + radiusX * Math.cos(angle);
            double y = centerY + radiusY * Math.sin(angle);

            Polygon triangle = new Polygon(
                    x, y - 10,
                    x - 8, y + 6,
                    x + 8, y + 6
            );
            triangle.setFill(Color.TRANSPARENT);
            triangle.setStroke(Color.WHITE);
            triangle.setStrokeWidth(2);

            for (Map.Entry<String, Integer> entry : truckers.entrySet()) {
                if (entry.getValue() == i) {
                    String name = entry.getKey().toUpperCase();
                    triangle.setFill(playerColors.getOrDefault(name, Color.GRAY));
                    break;
                }
            }

            overlay.getChildren().add(triangle);
        }

        Platform.runLater(() -> {
            rootPane.getChildren().clear();
            rootPane.getChildren().addAll(backgroundView, boardView, overlay);
        });
    }

    public void showBooleanOption(String message) {
        BorderPane overlay = new BorderPane();
        overlay.setPickOnBounds(false);
        overlay.setMouseTransparent(false);
        overlay.setPadding(new Insets(20));

        Label label = new Label(message);
        label.setFont(Font.font("Bangers", FontWeight.BOLD, 30));
        label.setTextFill(Color.web("#FFDE69"));
        BorderPane.setAlignment(label, Pos.TOP_CENTER);
        overlay.setTop(label);

        Button acceptButton = new Button("ACCEPT");
        Button declineButton = new Button("DECLINE");

        acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        declineButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

        for (Button btn : List.of(acceptButton, declineButton)) {
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            btn.setPrefWidth(120);
            btn.setPrefHeight(40);
        }

        HBox buttonBox = new HBox(30, acceptButton, declineButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        overlay.setBottom(buttonBox);

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(overlay)) {
                rootPane.getChildren().add(overlay);
            }
        });

        acceptButton.setOnAction(e -> {
            rootPane.getChildren().remove(overlay);
            gui.setCurrentEvent(null); // TODO: sostituisci con evento di accettazione, es: new ConfirmEvent(true)
        });

        declineButton.setOnAction(e -> {
            rootPane.getChildren().remove(overlay);
            gui.setCurrentEvent(null); // TODO: sostituisci con evento di rifiuto, es: new ConfirmEvent(false)
        });
    }

    public void updateHourglass(int secondsLeft) {

    }

    public void showStack(ArrayList<Card> stack) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        overlay.setPickOnBounds(true);
        overlay.setMouseTransparent(false);

        HBox cardBox = new HBox(20);
        cardBox.setAlignment(Pos.CENTER);

        for (Card card : stack) {
            ImageView cardView = new ImageView(card.getFrontImage());
            cardView.setFitWidth(200);
            cardView.setFitHeight(320);
            cardView.setStyle("-fx-effect: dropshadow(one-pass-box, white, 6, 0, 0, 0);");
            cardBox.getChildren().add(cardView);
        }

        overlay.getChildren().add(cardBox);
        StackPane.setAlignment(cardBox, Pos.CENTER);

        overlay.setOnMouseClicked(e -> {
            rootPane.getChildren().remove(overlay);
        });

        Platform.runLater(() -> {
            if (!rootPane.getChildren().contains(overlay)) {
                rootPane.getChildren().add(overlay);
            }
        });
    }
}
