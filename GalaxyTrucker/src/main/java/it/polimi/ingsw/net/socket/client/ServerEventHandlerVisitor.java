package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.text.TUI;


public class ServerEventHandlerVisitor implements ServerEventVisitor{
    private final UI ui=new TUI();
    private final ViewFlow viewFlow;
    ServerEventHandlerVisitor(ViewFlow viewFlow){
        this.viewFlow = viewFlow;
    }
    @Override
    public void visit(ServerEvent event) {}

    // === PHASE 1: Lobby ===
    @Override
    public void visit(LobbyEvent event) {
        ui.showWaitingForPlayers(event.getTruckers().size());
        ui.showStartCountdown(event.getCountdown());
    }

    @Override
    public void visit(PlayerJoinedEvent event) {
        ui.showPlayerJoined(event.getUsername());
    }

    @Override
    public void visit(NicknameAlreadyInError event) {
        ui.showNicknameTaken();
        ui.showInsertNickname();
        ui.showChooseColor(event.getColors());
    }

    @Override
    public void visit(AvailableColorsEvent event) {
        ui.showInsertNickname();
        ui.showChooseColor(event.getColors());
    }

    @Override
    public void visit(ColorAlreadyInError event) {
        ui.showError(event.getMessage());
        ui.showColorTaken();
        ui.showChooseColor(event.getAvailableColors());
        ui.showInsertNickname();
    }

    @Override
    public void visit(ReconnectedUserEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(DisconnectedUserEvent event) {
        ui.showMessage(event.getMessage());
    }

    // === PHASE 2: Setup ===


    @Override
    public void visit(SetupCompleteEvent event) {
        ui.showSetupComplete();
        viewFlow.setViewState(ViewFlow.ViewState.BULDING);
    }

    // === PHASE 3: Costruzione nave ===
    @Override
    public void visit(StartBuildingEvent event) {
        ui.showBuildPhaseStart();
    }

    @Override
    public void visit(ComponentLinkedEvent event) {
        ui.showComponentPlaced(event.getComponent(), event.getX(), event.getY());
    }

    @Override
    public void visit(ComponentNotAvailableError event) {
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(ComponentSelectedEvent event) {

    }

    @Override
    public void visit(CoordRequest event) {
        ui.askCoordinates();
    }

    @Override
    public void visit(InvalidCoordinatesError event) {
        ui.showError(event.getMessage());
        ui.askCoordinates();
    }

    @Override
    public void visit(InvalidPositionError event) {
        ui.showError(event.getMessage());
        ui.askCoordinates();
    }

    @Override
    public void visit(UpdateBuildingStateEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(TimeExpiredEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(TurnHourglassError event) {
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(AvailableComponentsEvent event) {
        viewFlow.setAvComponents(event.getComponents());
    }

    @Override
    public void visit(BuildCompleteEvent event){
        viewFlow.setViewState(ViewFlow.ViewState.FLIGTH);
        ui.showBuildComplete();
    }

    // === PHASE 4: Fase di Volo ===
    @Override
    public void visit(ShowCardEvent event) {
        ui.showCardDrawn(event.getCard());
    }

    @Override
    public void visit(UpdateCardStateEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(UpdateTrackEvent event) {
        viewFlow.setTrack(event.getTrack());
    }

    @Override
    public void visit(ShipUpdateEvent event) {
        viewFlow.setBatteryCount(event.numBatteries());
        viewFlow.setCannonPower(event.cannonPower());
        viewFlow.setShip(event.getShip());
        viewFlow.setEnginePower(event.enginePower());
        viewFlow.setPosition(event.truckerPosition());
        viewFlow.setGoodsByColor(event.getGoods());
        viewFlow.setHumanCount(event.numCrew());
    }

    @Override
    public void visit(IndexesRequest event) {
        ui.askGoodsIndexesToRemove();
    }

    @Override
    public void visit(IntRequest event) {
        ui.askStackIndex();
    }

    @Override
    public void visit(CheckShipsEvent event) {
        ui.showMessage(event.getMessage());
    }

    // === PHASE 5: Fine Gioco ===
    @Override
    public void visit(EndGameEvent event) {
        ui.showWinner(event.getWinner());
        ui.showScores(event.getScores());
        ui.showGameEnded();
    }

    // === ERRORI e MESSAGGI GENERICI ===
    @Override
    public void visit(GenericErrorInsertedData event) {
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(NoAvailableSpaceError event) {
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(BooleanRequest event) {
        ui.showBooleanOption(event.getMessage());
    }
}
