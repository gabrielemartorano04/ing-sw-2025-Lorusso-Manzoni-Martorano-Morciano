package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.view.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class GUI extends UI {

    private Stage stage;
    private Scene mainScene;

    public GUI(Stage stage) {
        this.stage = stage;
        this.mainScene = new Scene(new StackPane(), 700, 500);
        this.stage.setScene(mainScene);
        this.stage.setTitle("Galaxy Trucker");
        this.stage.setResizable(false);
        this.stage.show();
    }

    public void showWelcome() {
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/WelcomeImage.jpeg").toExternalForm());
        BackgroundImage bg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        StackPane welcomePane = new StackPane();
        welcomePane.setBackground(new Background(bg));

        Button startButton = new Button("START");
        startButton.setFont(Font.font("Bangers", 20));
        startButton.setTextFill(Color.WHITE);
        startButton.setStyle("-fx-background-color: rgba(255,222,105,0.7);");
        startButton.setOnAction(e -> showInsertNumPlayers());

        welcomePane.getChildren().add(startButton);
        StackPane.setAlignment(startButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(startButton, new Insets(0, 0, 50, 0));

        mainScene.setRoot(welcomePane);
    }

    public void showInsertNumPlayers() {
        Image backgroundImage = new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm());
        BackgroundImage bg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        VBox insertPane = new VBox(20);
        insertPane.setBackground(new Background(bg));
        insertPane.setPadding(new Insets(30));
        insertPane.setAlignment(Pos.CENTER);

        Label questionLabel = new Label("Insert the number of truckers:");
        questionLabel.setFont(Font.font("Bangers", 20));
        questionLabel.setTextFill(Color.web("#FFDE69"));

        TextField numberField = new TextField();
        numberField.setMaxWidth(200);
        numberField.setAlignment(Pos.CENTER);

        Button submitButton = new Button("INVIA");
        submitButton.setFont(Font.font("Arial", 16));
        submitButton.setDisable(true);
        submitButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

        numberField.textProperty().addListener((obs, oldText, newText) -> {
            boolean valid = !newText.trim().isEmpty();
            submitButton.setDisable(!valid);
            submitButton.setStyle(valid ? "-fx-background-color: #FFDE69;" : "-fx-background-color: gray; -fx-text-fill: white;");
        });

        submitButton.setOnAction(e -> {
            String input = numberField.getText().trim();
            if (!input.isEmpty()) {
                System.out.println("Numero giocatori inserito: " + input);
                // TODO: invia input al controller
            }
        });

        insertPane.getChildren().addAll(questionLabel, numberField, submitButton);
        mainScene.setRoot(insertPane);
    }

    @Override
    public void showChooseColor(EnumSet<PlayerColors> availableColors) {

    }

    @Override
    public void showColorTaken() {

    }

    @Override
    public void showNicknameTaken() {

    }

    @Override
    public void showPlayerJoined(String nickname) {

    }

    @Override
    public void showWaitingForPlayers(int currentPlayers) {

    }

    @Override
    public void showStartCountdown(int secondsLeft) {

    }

    @Override
    public void showSetupComplete() {

    }

    @Override
    public void showBuildPhaseStart() {

    }

    @Override
    public void showAvailableComponent(AvailableComponents components) {

    }

    @Override
    public void showKeptComponents(List<Component> kept) {

    }

    @Override
    public void showSelectComponent() {

    }

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

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            GUI gui = new GUI();
//            gui.init();
//            gui.showWelcome();
//        });
//    }
}
