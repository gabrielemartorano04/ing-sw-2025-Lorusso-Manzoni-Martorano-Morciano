package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.DiscardTileEvent;
import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.KeepTileEvent;
import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.LinkTileEvent;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.SendBooleanEvent;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.view.UI;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

public class GUI extends UI {

    private Stage stage;
    private Scene mainScene;
    private ClientEvent currentEvent;
    private Component selectedComponent;

    public GUI(Stage stage) {
        this.stage = stage;
        this.mainScene = new Scene(new StackPane(), 800, 600);
        this.stage.setScene(mainScene);
        this.stage.setTitle("Galaxy Trucker");
        this.stage.setResizable(false);
        this.stage.show();
    }

    public void setCurrentEvent(ClientEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    public ClientEvent getCurrentEvent() {
        return currentEvent;
    }

//    @Override
//    public void showFirstWelcome() {
//        double width = 800;
//        double height = 550;
//
//        // Immagine di sfondo
//        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/WelcomeImage.jpeg").toExternalForm());
//        ImageView backgroundView = new ImageView(backgroundImage);
//        backgroundView.setFitWidth(width);
//        backgroundView.setFitHeight(height);
//        backgroundView.setPreserveRatio(false);
//        backgroundView.setMouseTransparent(true);
//
//        // Bottone START (interattivo)
//        Button continueButton = new Button("START");
//        continueButton.setFont(Font.font("Bangers", 20));
//        continueButton.setTextFill(Color.WHITE);
//        continueButton.setStyle("-fx-background-color: rgba(255,222,105,0.7);");
//
//        // Azione quando si clicca il pulsante
//        continueButton.setOnAction(e -> {
//            showInsertNumPlayers();
//        });
//
//        // Layout
//        StackPane root = new StackPane(backgroundView, continueButton);
//        StackPane.setAlignment(continueButton, Pos.BOTTOM_CENTER);
//        StackPane.setMargin(continueButton, new Insets(0, 0, 25, 0));
//
//        Scene scene = new Scene(root, width, height);
//        stage.setScene(scene);
//        stage.setWidth(width);
//        stage.setHeight(height);
//        stage.centerOnScreen();
//        stage.show();
//    }

    @Override
    public void showWelcome(EnumSet<PlayerColors> availableColors) {
        double width = 800;
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
        continueButton.setOnAction(e -> {
            showChooseColor(availableColors);
        });

        // Layout
        StackPane root = new StackPane(backgroundView, continueButton);
        StackPane.setAlignment(continueButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(continueButton, new Insets(0, 0, 25, 0));

        if (stage.getScene() == null) {
            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(root);
        }
        stage.setWidth(width);
        stage.setHeight(height);
        stage.centerOnScreen();
        stage.show();
    }


    @Override
    public void showWelcome() {

    }

    @Override
    public void showInsertNumPlayers() {
        double width = 800;
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
        questionLabel.setFont(Font.font("Bangers", javafx.scene.text.FontWeight.BOLD, 20));
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
                    setCurrentEvent(new CreateGameEvent(num, level));
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid number.");
            }
        });

        content.getChildren().addAll(questionLabel, numberField, levelBox, errorLabel, submitButton);

        StackPane insertPane = new StackPane(backgroundView, content);
        if (stage.getScene() == null) {
            Scene scene = new Scene(insertPane, width, height);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(insertPane);
        }
        stage.centerOnScreen();
    }

    @Override
    public void showChooseColor(EnumSet<PlayerColors> availableColors) {
        double width = 800;
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
                setCurrentEvent(new LoginEvent(nickname, selectedColor[0]));
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

        // Controlla la distanza tra i singoli elementi
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

        StackPane root = new StackPane(backgroundView, content);
        StackPane.setAlignment(content, Pos.CENTER);

        if (stage.getScene() == null) {
            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(root);
        }
        stage.centerOnScreen();
    }

    @Override
    public void showColorTaken() {
        // Overlay grigio trasparente
        Rectangle overlay = new Rectangle(stage.getScene().getWidth(), stage.getScene().getHeight());
        overlay.setFill(Color.rgb(0, 0, 0, 0.4)); // grigio semi-trasparente

        // Messaggio di errore
        Label errorLabel = new Label("ERROR! Color already taken");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 28));
        errorLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        StackPane popup = new StackPane(overlay, errorLabel);
        popup.setMouseTransparent(true); // non blocca i click
        StackPane.setAlignment(errorLabel, Pos.CENTER);

        // Recupera il root corrente
        Parent currentRoot = stage.getScene().getRoot();
        if (currentRoot instanceof Pane parent) {
            parent.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) change -> {
                // evita il crash se la GUI cambia in mezzo
                if (!parent.getChildren().contains(popup)) return;
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> parent.getChildren().remove(popup));
                delay.play();
            });

            parent.getChildren().add(popup);
        }
    }

    @Override
    public void showNicknameTaken() {
        // Overlay grigio trasparente
        Rectangle overlay = new Rectangle(stage.getScene().getWidth(), stage.getScene().getHeight());
        overlay.setFill(Color.rgb(0, 0, 0, 0.4)); // grigio semi-trasparente

        // Messaggio di errore
        Label errorLabel = new Label("ERROR! Nickname already taken");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 28));
        errorLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        StackPane popup = new StackPane(overlay, errorLabel);
        popup.setMouseTransparent(true); // non blocca i click
        StackPane.setAlignment(errorLabel, Pos.CENTER);

        // Recupera il root corrente
        Parent currentRoot = stage.getScene().getRoot();
        if (currentRoot instanceof Pane parent) {
            parent.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) change -> {
                // evita il crash se la GUI cambia in mezzo
                if (!parent.getChildren().contains(popup)) return;
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> parent.getChildren().remove(popup));
                delay.play();
            });

            parent.getChildren().add(popup);
        }
    }

    @Override
    public void showPlayerJoined(String nickname) {
        // Etichetta del messaggio
        Label message = new Label(nickname + " has joined");
        message.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.8); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-background-radius: 10px;"
        );
        message.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));

        // Notifica in StackPane dedicato per posizionamento
        StackPane notificationWrapper = new StackPane(message);
        notificationWrapper.setMouseTransparent(true);
        StackPane.setAlignment(message, Pos.TOP_RIGHT);
        StackPane.setMargin(message, new Insets(15, 15, 0, 0));

        // Prendi il root e fai cast diretto
        StackPane root = (StackPane) stage.getScene().getRoot();
        root.getChildren().add(notificationWrapper);

        // Rimuovi la notifica dopo 5 secondi
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(e -> root.getChildren().remove(notificationWrapper));
        delay.play();
    }

    @Override
    public void showWaitingForPlayers(int currentPlayers) {
        double width = 800;
        double height = 550;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        Label loadingLabel = new Label("Waiting for truckers to join");
        loadingLabel.setFont(Font.font("Bangers", javafx.scene.text.FontWeight.BOLD, 26));
        loadingLabel.setTextFill(Color.web("#FFDE69"));
        loadingLabel.setAlignment(Pos.CENTER);

        // VBox per centratura verticale
        VBox content = new VBox(loadingLabel);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(backgroundView, content);
        StackPane.setAlignment(content, Pos.CENTER);

        Scene scene = new Scene(root, width, height);
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.centerOnScreen();
        });

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> loadingLabel.setText("Waiting for truckers to join")),
                new KeyFrame(Duration.seconds(0.5), e -> loadingLabel.setText("Waiting for truckers to join.")),
                new KeyFrame(Duration.seconds(1), e -> loadingLabel.setText("Waiting for truckers to join..")),
                new KeyFrame(Duration.seconds(1.5), e -> loadingLabel.setText("Waiting for truckers to join...")),
                new KeyFrame(Duration.seconds(2.0),   e -> {})
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    public void showStartCountdown(int secondsLeft) {
        double width = 800;
        double height = 550;

        // Sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Testo statico
        Label message = new Label("The flight will start in...");
        message.setFont(Font.font("Bangers", FontWeight.BOLD, 32));
        message.setTextFill(Color.web("#FFDE69"));

        // Etichetta countdown
        Label countdownLabel = new Label();
        countdownLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 80));
        countdownLabel.setTextFill(Color.WHITE);

        VBox content = new VBox(30, message, countdownLabel);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(backgroundView, content);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();

        // Timeline per il countdown
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

        // Mostra GO!
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(secondsLeft), e -> {
                    countdownLabel.setText("GO!");
                    countdownLabel.setTextFill(Color.web("#FFDE69"));
                })
        );

        // Mantiene visibile "GO!" per un altro secondo
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(secondsLeft + 1))
        );

        timeline.play();
    }

    @Override
    public void showSetupComplete() {
        double width = 800;
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

        StackPane root = new StackPane(backgroundView, message);
        StackPane.setAlignment(message, Pos.CENTER);

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @Override
    public void showBuildPhaseStart(AvailableComponents availableComponents) {
        double width = 800;
        double height = 550;

        // Sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Testo centrale
        Label message = new Label("Start building your spaceship");
        message.setFont(Font.font("Bangers", FontWeight.BOLD, 40));
        message.setTextFill(Color.web("#FFDE69"));

        StackPane root = new StackPane(backgroundView, message);
        StackPane.setAlignment(message, Pos.CENTER);

        // Mostra la scena (solo se non esiste già)
        if (stage.getScene() == null) {
            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(root);
        }
        stage.centerOnScreen();

        // Dopo 3 secondi, mostra i componenti disponibili
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> showAvailableComponent(availableComponents));
        delay.play();
    }

    @Override
    public void showAvailableComponent(AvailableComponents components) {
        double width = 800;
        double height = 550;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        int col = 0;
        int row = 0;
        for (Component component : components.getComponents()) {
            ImageView imgView = new ImageView(component.getCovered() ? component.getBackImage() : component.getFrontImage());
            imgView.setFitWidth(100);
            imgView.setFitHeight(100);

            Rectangle clip = new Rectangle(100, 100);
            clip.setArcWidth(16);
            clip.setArcHeight(16);
            imgView.setClip(clip);

            Button button = new Button();
            button.setGraphic(imgView);
            button.setPrefSize(100, 100);
            button.setStyle("-fx-background-color: transparent; -fx-border-color: #cccccc; -fx-border-radius: 8px; -fx-cursor: hand; -fx-padding: 0px;");

            button.setOnAction(e -> {
                selectedComponent = component;
                if (component.getCovered()) {
                    showSelectComponent();
                } else {
                    // Start drag directly
                    Dragboard db = button.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString("component");
                    db.setContent(content);
                    db.setDragView(imgView.getImage());
                }
            });

            grid.add(button, col, row);
            col++;
            if (col == 6) {
                col = 0;
                row++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(new StackPane(grid));
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        StackPane root = new StackPane(backgroundView, scrollPane);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @Override
    public void showKeptComponents(Component[] kept) {
        HBox keptSlots = new HBox(10);
        keptSlots.setMaxWidth(200);
        keptSlots.setAlignment(Pos.TOP_RIGHT);

        for (int i = 0; i < 2; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(85, 85);
            slot.setMinSize(85, 85);
            slot.setMaxSize(85, 85);
            slot.setStyle("-fx-background-color: transparent;");

            Rectangle border = new Rectangle(85, 85);
            border.setArcWidth(12);
            border.setArcHeight(12);
            border.setFill(Color.TRANSPARENT);
            border.setStroke(Color.WHITE);
            border.setStrokeWidth(2);

            int index = i;

            if (i < kept.size() && kept.get(i) != null) {
                Component comp = kept.get(i);
                ImageView imageView = new ImageView(comp.getFrontImage());
                imageView.setFitWidth(85);
                imageView.setFitHeight(85);

                Rectangle clip = new Rectangle(85, 85);
                clip.setArcWidth(16);
                clip.setArcHeight(16);
                imageView.setClip(clip);

                imageView.setOnDragDetected(event -> {
                    Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString("kept:" + index);
                    db.setContent(content);
                    db.setDragView(imageView.snapshot(null, null));
                    selectedComponent = comp;
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
                    setCurrentEvent(new KeepTileEvent(selectedComponent));
                    selectedComponent = null;
                    event.setDropCompleted(true);
                }
                event.consume();
            });

            keptSlots.getChildren().add(slot);
        }

        StackPane.setAlignment(keptSlots, Pos.TOP_RIGHT);
        StackPane.setMargin(keptSlots, new Insets(25, 30, 0, 0));

        Platform.runLater(() -> {
            Node rootNode = stage.getScene().getRoot();
            if (rootNode instanceof StackPane stackRoot) {
                if (!stackRoot.getChildren().contains(keptSlots)) {
                    stackRoot.getChildren().add(keptSlots);
                }
            }
        });
    }

    @Override
    public void showSelectComponent() {
        double width = 800;
        double height = 550;
        if (selectedComponent == null) return;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        ImageView imgView = new ImageView(selectedComponent.getFrontImage());
        imgView.setFitWidth(150);
        imgView.setFitHeight(150);
        Rectangle clip = new Rectangle(150, 150);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imgView.setClip(clip);

        // Drag and drop da questa view
        imgView.setOnDragDetected(e -> {
            Dragboard db = imgView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("component");
            db.setContent(content);
            db.setDragView(imgView.snapshot(null, null));
            e.consume();
        });

        // Pulsante per scartare
        Button discardButton = new Button("DISCARD");
        discardButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white; -fx-font-weight: bold;");
        discardButton.setOnAction(e -> {
            setCurrentEvent(new DiscardTileEvent(selectedComponent));
        });

        layout.getChildren().addAll(imgView, discardButton);

        StackPane root = new StackPane(backgroundView, layout);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @Override
    public void showBuildOptions() {

    }

    @Override
    public void showDiscardComponent(Component component) {
        selectedComponent = component;
        showSelectComponent();
    }

    @Override
    public void showComponentOptions() {

    }

    @Override
    public void showComponentPlaced(Component component, int x, int y) {

    }

    @Override
    public void showInvalidPlacement(String reason) {

    }

    @Override
    public void showHourglassTurned() {

    }

    @Override
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
        Rectangle dimBackground = new Rectangle(stage.getWidth(), stage.getHeight());
        dimBackground.setFill(Color.rgb(50, 50, 50, 0.6)); // grigio con opacità 60%
        dimBackground.widthProperty().bind(stage.getScene().widthProperty());
        dimBackground.heightProperty().bind(stage.getScene().heightProperty());

        // Overlay con sfondo e scritta
        StackPane overlay = new StackPane(dimBackground, message);
        overlay.setAlignment(Pos.CENTER);
        overlay.setMouseTransparent(true);

        // Recupera root (deve essere uno StackPane)
        StackPane root = (StackPane) stage.getScene().getRoot();
        root.getChildren().add(overlay);

        // Rimuove l'overlay dopo 5 secondi
        PauseTransition wait = new PauseTransition(Duration.seconds(5));
        wait.setOnFinished(e -> root.getChildren().remove(overlay));
        wait.play();
    }

    @Override
    public void showWaitForOthers() {

    }

    @Override
    public void showBuildComplete() {

    }

    @Override
    public void showFlightPhaseStart() {

    }

    @Override
    public void showCardDrawn(Card card) {

    }

    @Override
    public void showCardEffectStart(Card card) {

    }

    @Override
    public void showCardEffectEnd(Card card) {

    }

    @Override
    public void showTruckerMoved(String nickname, int newPosition) {

    }

    @Override
    public void showTruckerWithdrawn(String nickname) {
        // Etichetta del messaggio
        Label message = new Label(nickname + " has withdrawn");
        message.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.8); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-background-radius: 10px;"
        );
        message.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));

        // Notifica in StackPane dedicato per posizionamento
        StackPane notificationWrapper = new StackPane(message);
        notificationWrapper.setMouseTransparent(true);
        StackPane.setAlignment(message, Pos.TOP_RIGHT);
        StackPane.setMargin(message, new Insets(15, 15, 0, 0));

        // Prendi il root e fai cast diretto
        StackPane root = (StackPane) stage.getScene().getRoot();
        root.getChildren().add(notificationWrapper);

        // Rimuovi la notifica dopo 5 secondi
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(e -> root.getChildren().remove(notificationWrapper));
        delay.play();
    }

    @Override
    public void showTruckerLostAllCrew(String nickname) {

    }

    @Override
    public void showGoodsGained(Goods goods) {

    }

    @Override
    public void showGameEnded() {

    }

    @Override
    public void showScores(Map<String, Integer> finalScores) {

    }

    @Override
    public void showWinner(String nickname) {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showConnectionLost() {

    }

    @Override
    public void showInvalidCommand(String expectedFormat) {

    }

    @Override
    public void askIndex() {

    }

    @Override
    public void askCoordinates() {

    }

    @Override
    public void showGoodsToRemove(List<Goods> goods) {

    }

    @Override
    public void askGoodsIndexesToRemove() {

    }

    @Override
    public void askPlacedComponent() {

    }

    @Override
    public void showPlayerShipStatus(int position, int batteryCount, int humanCount, int cannonPower, int enginePower, Map<String, Integer> goodsByColor) {

    }

    @Override
    public void showShipLayout(String nickname, Component[][] layout) {
        double width = 800;
        double height = 550;

        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-1b.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        boolean[][] validCells = {
                {false, false, false, false, false, false, false,  false,  false, false, false},
                {false, false, false, false, false, false, false,  false,  false, false, false},
                {false, false, false, false, false, false, false,  false,  false, false, false},
                {false, false, false, false, false, false, false,  false,  false, false, false},
                {false, false, false, false, false, false, false,  false,  false, false, false},
                {false, false, false, false, false, false, true,  false,  true, false, false},
                {false, false, false, false, false, true,  true,  true,  true, true,  false},
                {false, false, false, false, true,  true,  true,  true, true, true,  true },
                {false, false, false, false, true,  true,  true,  true,  true, true,  true },
                {false, false, false, false, true,  true,  true,  false,  true, true,  true }
        };

        GridPane grid = new GridPane();
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setAlignment(Pos.CENTER);

        for (int row = 5; row < 10; row++) {
            for (int col = 4; col < 11; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(103, 97);

                if (validCells[row][col]) {
                    cell.setStyle("-fx-border-color: white; -fx-border-radius: 4px; -fx-background-radius: 4px;");

                    Component c = layout[row][col];
                    if (c != null ) {
                        ImageView imageView = new ImageView(c.getFrontImage());
                        imageView.setFitWidth(70);
                        imageView.setFitHeight(70);
                        Rectangle clip = new Rectangle(65, 65);
                        clip.setArcWidth(16);
                        clip.setArcHeight(16);
                        imageView.setClip(clip);
                        cell.getChildren().add(imageView);
                    }

                    final int r = row;
                    final int cIndex = col;
                    cell.setOnDragOver(event -> {
                        if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                        event.consume();
                    });

                    cell.setOnDragDropped(event -> {
                        Dragboard db = event.getDragboard();
                        if (db.hasString()) {
                            setCurrentEvent(new LinkTileEvent(selectedComponent, r + 4, cIndex + 5));
                            selectedComponent = null;
                            event.setDropCompleted(true);
                        }
                        event.consume();
                    });
                } else {
                    cell.setStyle("-fx-background-color: transparent;");
                }

                grid.add(cell, col, row);
            }
        }

        StackPane root = new StackPane(backgroundView, grid);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showTrack(Map<String, Integer> truckers) {
        double width = 800;
        double height = 550;

        // 1. Sfondo standard
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);

        // 2. Immagine della board (centrata sopra)
        Image boardImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-5.png").toExternalForm());
        ImageView boardView = new ImageView(boardImage);
        boardView.setFitWidth(width);
        boardView.setFitHeight(height);
        boardView.setPreserveRatio(false);
        boardView.setMouseTransparent(true);

        // 3. Overlay dei triangoli
        Pane overlay = new Pane();
        double centerX = width / 2;
        double centerY = height / 2;
        double radiusX = 300;
        double radiusY = 200;

        // Mappa colori giocatori (puoi renderla dinamica)
        Map<String, Color> playerColors = Map.of(
                "RED", Color.RED,
                "GREEN", Color.LIMEGREEN,
                "BLUE", Color.DODGERBLUE,
                "YELLOW", Color.GOLD
        );

        // 4. Disegna i triangoli
        for (int i = 0; i < 24; i++) {
            double angle = 2 * Math.PI * i / 24 - Math.PI / 2;
            double x = centerX + radiusX * Math.cos(angle);
            double y = centerY + radiusY * Math.sin(angle);

            Polygon triangle = new Polygon();
            triangle.getPoints().addAll(
                    x, y - 10,
                    x - 8, y + 6,
                    x + 8, y + 6
            );

            triangle.setFill(Color.TRANSPARENT);
            triangle.setStroke(Color.WHITE);
            triangle.setStrokeWidth(2);

            // Colora se qualcuno è su questa posizione
            for (Map.Entry<String, Integer> entry : truckers.entrySet()) {
                if (entry.getValue() == i) {
                    String name = entry.getKey().toUpperCase();
                    triangle.setFill(playerColors.getOrDefault(name, Color.GRAY));
                    break;
                }
            }

            overlay.getChildren().add(triangle);
        }

        // 5. Stack finale
        StackPane root = new StackPane(backgroundView, boardView, overlay);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @Override
    public void showBooleanOption(String message) {
        // Contenitore dell'overlay completo
        BorderPane overlay = new BorderPane();
        overlay.setPickOnBounds(false);
        overlay.setMouseTransparent(false);
        overlay.setPadding(new Insets(20));

        // Titolo in alto
        Label label = new Label(message);
        label.setFont(Font.font("Bangers", FontWeight.BOLD, 30));
        label.setTextFill(Color.web("#FFDE69"));
        BorderPane.setAlignment(label, Pos.TOP_CENTER);
        overlay.setTop(label);

        // Pulsanti
        Button acceptButton = new Button("ACCEPT");
        Button declineButton = new Button("DECLINE");

        acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Verde
        declineButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;"); // Rosso

        for (Button btn : List.of(acceptButton, declineButton)) {
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            btn.setPrefWidth(120);
            btn.setPrefHeight(40);
        }

        HBox buttonBox = new HBox(30, acceptButton, declineButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        overlay.setBottom(buttonBox);

        // Recupera il root corrente (StackPane)
        StackPane root = (StackPane) stage.getScene().getRoot();
        root.getChildren().add(overlay);

        // Callback alla tua logica (da definire tu)
        acceptButton.setOnAction(e -> {
            root.getChildren().remove(overlay);
            setCurrentEvent(new SendBooleanEvent(true));
        });

        declineButton.setOnAction(e -> {
            root.getChildren().remove(overlay);
            setCurrentEvent(new SendBooleanEvent(false));
        });
    }

    @Override
    public void updateHourglass(int secondsLeft) {

    }

    @Override
    public void showStack(ArrayList<Card> stack) {
        // Crea overlay grigio semi-trasparente
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        overlay.setPickOnBounds(true); // intercetta i click
        overlay.setMouseTransparent(false);

        // Contenitore carte in orizzontale
        HBox cardBox = new HBox(20);
        cardBox.setAlignment(Pos.CENTER);

        for (Card card : stack) {
            ImageView cardView = new ImageView(card.getFrontImage());
            cardView.setFitWidth(200);
            cardView.setFitHeight(320);

            // Eventuale bordo o ombra
            cardView.setStyle("-fx-effect: dropshadow(one-pass-box, white, 6, 0, 0, 0);");

            cardBox.getChildren().add(cardView);
        }

        overlay.getChildren().add(cardBox);
        StackPane.setAlignment(cardBox, Pos.CENTER);

        // Click per rimuovere overlay
        overlay.setOnMouseClicked(e -> {
            StackPane root = (StackPane) stage.getScene().getRoot();
            root.getChildren().remove(overlay);
        });

        // Aggiunta al layout esistente
        Scene currentScene = stage.getScene();
        if (currentScene.getRoot() instanceof StackPane root) {
            root.getChildren().add(overlay);
        } else {
            // Se root non è StackPane, avvolgilo
            StackPane newRoot = new StackPane(currentScene.getRoot(), overlay);
            currentScene.setRoot(newRoot);
        }
    }
}
