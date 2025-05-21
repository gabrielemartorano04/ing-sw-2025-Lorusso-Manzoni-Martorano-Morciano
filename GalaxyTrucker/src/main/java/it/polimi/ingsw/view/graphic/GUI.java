package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.view.ParserState;
import it.polimi.ingsw.view.UI;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class GUI extends UI {

    private Stage stage;
    private GUIController controller;
    private ClientEvent currentEvent;
    private Component selectedComponent;

    public GUI(Stage stage, GUIController controller) {
        this.stage = stage;
        initFXML();
    }

    private void initFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/GUI.fxml"));
            StackPane root = loader.load();
            controller = loader.getController();
            controller.setGUI(this);

            Scene scene = new Scene(root, 850, 550);
            stage.setScene(scene);
            stage.setTitle("Galaxy Trucker");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentEvent(ClientEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    public ClientEvent getCurrentEvent() {
        return currentEvent;
    }

    public Component getSelectedComponent() {
        return selectedComponent;
    }

    public void setSelectedComponent(Component selectedComponent) {
        this.selectedComponent = selectedComponent;
    }

    @Override
    public void showWelcome() {
        Platform.runLater(() -> controller.showWelcome());
    }

    @Override
    public void showWelcome(EnumSet<PlayerColors> availableColors) {
        Platform.runLater(() -> controller.showWelcome(availableColors));
    }

    @Override
    public void showInsertNumPlayers() {
        Platform.runLater(() -> controller.showInsertNumPlayers());
    }

    @Override
    public void showChooseColor(EnumSet<PlayerColors> availableColors) {
        Platform.runLater(() -> controller.showChooseColor(availableColors));
    }

    @Override
    public void showColorTaken() {
        Platform.runLater(() -> controller.showColorTaken());
    }

    @Override
    public void showNicknameTaken() {
        Platform.runLater(() -> controller.showNicknameTaken());
    }

    @Override
    public void showPlayerJoined(String nickname) {
        Platform.runLater(() -> controller.showPlayerJoined(nickname));
    }

    @Override
    public void showWaitingForPlayers(int currentPlayers) {
        Platform.runLater(() -> controller.showWaitingForPlayers(currentPlayers));
    }

    @Override
    public void showStartCountdown(int secondsLeft) {
        Platform.runLater(() -> controller.showStartCountdown(secondsLeft));
    }

    @Override
    public void showSetupComplete() {
        Platform.runLater(() -> controller.showSetupComplete());
    }

    @Override
    public void showPlayers(ArrayList<String> players) {
        Platform.runLater(() -> controller.showPlayers(players));
    }

    @Override
    public void showBuildPhaseStart(ArrayList<Component> availableComponents) {
        Platform.runLater(() -> controller.showBuildPhaseStart(availableComponents));
    }

    @Override
    public void showAvailableComponent(ArrayList<Component> components) {
        Platform.runLater(() -> controller.showAvailableComponent(components));
    }

    @Override
    public void showKeptComponents(ArrayList<Component> kept) {
        Platform.runLater(() -> controller.showKeptComponents(kept));
    }

    @Override
    public void showSelectComponent() {
        Platform.runLater(() -> controller.showSelectComponent());
    }

    @Override
    public void showBuildOptions() {
        Platform.runLater(() -> controller.showBuildOptions());
    }

    @Override
    public void showDiscardComponent(Component component) {
        Platform.runLater(() -> controller.showDiscardComponent(component));
    }

    @Override
    public void showComponentOptions() {
        Platform.runLater(() -> controller.showComponentOptions());
    }

    @Override
    public void showComponentPlaced(Component component, int x, int y) {
        Platform.runLater(() -> controller.showComponentPlaced(component, x, y));
    }

    @Override
    public void showInvalidPlacement(String reason) {
        Platform.runLater(() -> controller.showInvalidPlacement(reason));
    }

    @Override
    public void showHourglassTurned() {
        Platform.runLater(() -> controller.showHourglassTurned());
    }

    @Override
    public void showTimeExpired() {
        Platform.runLater(() -> controller.showTimeExpired());
    }

    @Override
    public void showWaitForOthers() {
        Platform.runLater(() -> controller.showWaitForOthers());
    }

    @Override
    public void showBuildComplete() {
        Platform.runLater(() -> controller.showBuildComplete());
    }

    @Override
    public void showFlightPhaseStart() {
        Platform.runLater(() -> controller.showFlightPhaseStart());
    }

    @Override
    public void showCardDrawn(StartCardEvent event) {
        Platform.runLater(() -> controller.showCardDrawn(event));
    }

    @Override
    public void showCardEffectStart(Card card) {
        Platform.runLater(() -> controller.showCardEffectStart(card));
    }

    @Override
    public void showCardEffectEnd(Card card) {
        Platform.runLater(() -> controller.showCardEffectEnd(card));
    }

    @Override
    public void showTruckerMoved(String nickname, int newPosition) {
        Platform.runLater(() -> controller.showTruckerMoved(nickname, newPosition));
    }

    @Override
    public void showTruckerWithdrawn(String nickname) {
        Platform.runLater(() -> controller.showTruckerWithdrawn(nickname));
    }

    @Override
    public void showTruckerLostAllCrew(String nickname) {
        Platform.runLater(() -> controller.showTruckerLostAllCrew(nickname));
    }

    @Override
    public void showGoodsGained(Goods goods) {
        Platform.runLater(() -> controller.showGoodsGained(goods));
    }

    @Override
    public void showGameEnded() {
        Platform.runLater(() -> controller.showGameEnded());
    }

    @Override
    public void showScores(Map<String, Integer> finalScores) {
        Platform.runLater(() -> controller.showScores(finalScores));
    }

    @Override
    public void showError(String message) {
        Platform.runLater(() -> controller.showError(message));
    }

    @Override
    public void showConnectionLost() {
        Platform.runLater(() -> controller.showConnectionLost());
    }

    @Override
    public void showInvalidCommand(String expectedFormat) {
        Platform.runLater(() -> controller.showInvalidCommand(expectedFormat));
    }

    @Override
    public void askIndex() {
        Platform.runLater(() -> controller.askIndex());
    }

    @Override
    public void askCoordinates() {
        Platform.runLater(() -> controller.askCoordinates());
    }

    @Override
    public void showGoodsToRemove(List<Goods> goods) {
        Platform.runLater(() -> controller.showGoodsToRemove(goods));
    }

    @Override
    public void askGoodsIndexesToRemove() {
        Platform.runLater(() -> controller.askGoodsIndexesToRemove());
    }

    @Override
    public void askPlacedComponent() {
        Platform.runLater(() -> controller.askPlacedComponent());
    }

    @Override
    public void showPlayerShipStatus(int position, int batteryCount, int humanCount, int cannonPower, int enginePower, Map<String, Integer> goodsByColor) {
        Platform.runLater(() -> controller.showPlayerShipStatus(position, batteryCount, humanCount, cannonPower, enginePower, goodsByColor));
    }

    @Override
    public void showShipLayout(String nickname, Component[][] layout) {
        Platform.runLater(() -> controller.showShipLayout(nickname, layout));
    }

    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> controller.showMessage(message));
    }

    @Override
    public void showTrack(Map<String, Integer> truckers) {
        Platform.runLater(() -> controller.showTrack(truckers));
    }

    @Override
    public void showBooleanOption(String message) {
        Platform.runLater(() -> controller.showBooleanOption(message));
    }

    @Override
    public void updateHourglass(int secondsLeft) {
        Platform.runLater(() -> controller.updateHourglass(secondsLeft));
    }

    @Override
    public void showStack(ArrayList<Card> stack) {
        Platform.runLater(() -> controller.showStack(stack));
    }

    @Override
    public void showComponentSelected(Component component) {
        Platform.runLater(() -> controller.showComponentSelected(component));
    }

    @Override
    public void showInputFormat(ParserState parserState) {
        Platform.runLater(() -> controller.showInputFormat(parserState));
    }
}