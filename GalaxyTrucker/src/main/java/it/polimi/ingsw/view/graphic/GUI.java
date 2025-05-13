package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.view.UI;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class GUI extends UI {

    private Stage stage;
    private Scene mainScene;

    public GUI(Stage stage) {
        this.stage = stage;
        this.mainScene = new Scene(new StackPane(), 800, 600);
        this.stage.setScene(mainScene);
        this.stage.setTitle("Galaxy Trucker");
        this.stage.setResizable(false);
        this.stage.show();
    }

    @Override
    public void showWelcome() {
        double width = 800;
        double height = 550;

        // Immagine di sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/WelcomeImage.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Bottone START (solo visivo)
        Button continueButton = new Button("START");
        continueButton.setFont(Font.font("Bangers", 20));
        continueButton.setTextFill(Color.WHITE);
        continueButton.setStyle("-fx-background-color: rgba(255,222,105,0.7);");

        // Layout
        StackPane root = new StackPane(backgroundView, continueButton);
        StackPane.setAlignment(continueButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(continueButton, new Insets(0, 0, 25, 0));

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.centerOnScreen();
        stage.show();
    }


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

        Label questionLabel = new Label("Insert the number of truckers");
        questionLabel.setFont(Font.font("Bangers", javafx.scene.text.FontWeight.BOLD, 20));
        questionLabel.setTextFill(Color.web("#FFDE69"));

        TextField numberField = new TextField();
        numberField.setMaxWidth(200);
        numberField.setAlignment(Pos.CENTER);

        Button submitButton = new Button("START");
        submitButton.setFont(Font.font("Bangers", 16));
        submitButton.setDisable(true);
        submitButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

        numberField.textProperty().addListener((obs, oldText, newText) -> {
            boolean valid = !newText.trim().isEmpty();
            submitButton.setDisable(!valid);
            submitButton.setStyle(valid ? "-fx-background-color: #FFDE69;" : "-fx-background-color: gray; -fx-text-fill: white;");
        });

        submitButton.setOnAction(e -> {
            String input = numberField.getText().trim();
        });

        content.getChildren().addAll(questionLabel, numberField, submitButton);

        StackPane insertPane = new StackPane();
        insertPane.getChildren().addAll(backgroundView, content);

        stage.getScene().setRoot(insertPane);
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

        // Scritta in alto
        Label titleLabel = new Label("Choose your colour");
        titleLabel.setFont(Font.font("Bangers", javafx.scene.text.FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#FFDE69"));
        titleLabel.setAlignment(Pos.CENTER);

        // Griglia dei colori
        TilePane colorPane = new TilePane();
        colorPane.setHgap(15);
        colorPane.setVgap(15);
        colorPane.setAlignment(Pos.CENTER);

        for (PlayerColors color : availableColors) {
            Rectangle rect = new Rectangle(60, 60);
            rect.setArcWidth(18);
            rect.setArcHeight(18);

            Color fxColor;
            switch (color) {
                case RED -> fxColor = Color.RED;
                case GREEN -> fxColor = Color.LIMEGREEN;
                case BLUE -> fxColor = Color.DODGERBLUE;
                case YELLOW -> fxColor = Color.GOLD;
                default -> fxColor = Color.GRAY;
            }

            rect.setFill(fxColor);

            // Button trasparente sopra il rettangolo
            Button colorButton = new Button();
            colorButton.setPrefSize(60, 60);
            colorButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            colorButton.setGraphic(rect);

            colorButton.setOnAction(e -> {
                // clientHandler.sendEvent(new ColorChosenEvent(color));
            });

            colorPane.getChildren().add(colorButton);
        }

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.getChildren().addAll(titleLabel, colorPane);

        StackPane root = new StackPane(backgroundView, content);
        StackPane.setAlignment(content, Pos.CENTER);

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @Override
    public void showColorTaken() {

    }

    @Override
    public void showNicknameTaken() {

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

    }

    @Override
    public void showSetupComplete() {

    }

    @Override
    public void showBuildPhaseStart(AvailableComponents availableComponents) {

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
        backgroundView.setMouseTransparent(true);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER); // Centra il contenuto nella GridPane

        int col = 0;
        int row = 0;
        for (Component component : components.getComponents()) {
            ImageView imgView = new ImageView(
                    component.getCovered() ? component.getBackImage() : component.getFrontImage()
            );
            imgView.setFitWidth(100);
            imgView.setFitHeight(100);

            Rectangle clip = new Rectangle(100, 100);
            clip.setArcWidth(16);
            clip.setArcHeight(16);
            imgView.setClip(clip);

            Button button = new Button();
            button.setGraphic(imgView);
            button.setPrefSize(100, 100);
            button.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-border-color: #cccccc;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-cursor: hand;" +
                            "-fx-padding: 0px;"
            );

            button.setOnAction(e -> {
                System.out.println("Hai cliccato: " + component.getType());
                // gestisci selezione...
            });

            grid.add(button, col, row);
            col++;
            if (col == 6) {
                col = 0;
                row++;
            }
        }

        StackPane centeredGrid = new StackPane(grid); // Avvolge la GridPane per centrarla
        centeredGrid.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(centeredGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        StackPane root = new StackPane(backgroundView, scrollPane);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @Override
    public void showKeptComponents(List<Component> kept) {

    }

    @Override
    public void showSelectComponent() {

    }
    //Viene chiamato subito dopo available components (ma non devi farlo tu) e attraverso drag and drop, quando viene selezionato il componente si chiude l'elenco dei componenti creato in showAvailableCompionent e viene mostrata la nave alla quale si può agganciare

    @Override
    public void showBuildOptions() {

    }

    @Override
    public void showDiscardComponent(Component component) {

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

        // Sfondo
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/cardboard-1b.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        // Matrice attiva (celle cliccabili)
        boolean[][] validCells = {
                {false, false, true,  false,  true, false, false},
                {false, true,  true,  true,  true, true,  false},
                {true,  true,  true,  true,  true, true,  true },
                {true,  true,  true,  true,  true, true,  true },
                {true,  true,  true,  false,  true, true,  true }
        };

        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                if (validCells[row][col]) {
                    StackPane cell = new StackPane();
                    cell.setPrefSize(100, 110);
                    cell.setStyle(
                            "-fx-background-color: rgba(255,255,255,0.1);" +
                                    "-fx-border-color: white;" +
                                    "-fx-border-radius: 12px;" +
                                    "-fx-background-radius: 12px;"
                    );

                    // Mostra componente se presente
                    Component c = layout[row][col];
                    if (c != null && !c.getCovered()) {
                        ImageView imageView = new ImageView(c.getFrontImage());
                        imageView.setFitWidth(70);
                        imageView.setFitHeight(70);

                        // Clip con angoli smussati
                        Rectangle clip = new Rectangle(65, 65);
                        clip.setArcWidth(16);
                        clip.setArcHeight(16);
                        imageView.setClip(clip);

                        cell.getChildren().add(imageView);
                    }

                    grid.add(cell, col, row);
                } else {
                    // Spazio vuoto per mantenere la griglia centrata
                    Region spacer = new Region();
                    spacer.setPrefSize(80, 80);
                    grid.add(spacer, col, row);
                }
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

    }

    @Override
    public void showBooleanOption(String message) {

    }

    @Override
    public void updateHourglass(int secondsLeft) {

    }

    @Override
    public void showStack(ArrayList<Card> stack) {

    }
}
