package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.text.TUI;

public class ServerEventHandlerVisitor implements ServerEventVisitor{
    private final UI ui=new TUI();

    @Override
    public void visit(AvailableColorsEvent event) {
        ui.showChooseColor(event.getColors());
    }

    @Override
    public void visit(BooleanRequest event) {

    }

    @Override
    public void visit(CheckShipsEvent event) {

    }

    @Override
    public void visit(ColorAlreadyInError event) {
        ui.showError(event.getMessage());
        ui.showColorTaken();
        ui.showChooseColor(event.ge());
    }

    @Override
    public void visit(ComponentLinkedEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(ComponentNotAvailableError event) {
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(ComponentSelectedEvent event) {

    }

    @Override
    public void visit(DisconnectedUserEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(EndGameEvent event) {
        ui.showWinner(event.getWinner());
        ui.showScores(event.getScores());
        ui.showGameEnded();
    }

    @Override
    public void visit(GenericErrorInsertedData event) {
        ui.showError(event.getMessage());
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
    public void visit(NicknameAlreadyInError event) {
        ui.showNicknameTaken();
        ui.showInsertNickname();
    }

    @Override
    public void visit(NoAvailableSpaceError event) {
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(PlayerJoinedEvent event) {
        ui.showPlayerJoined(event.getUsername());
    }

    @Override
    public void visit(ReconnectedUserEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(ShipUpdateEvent event) {

    }

    @Override
    public void visit(ShowCardEvent event) {
        ui.showCardDrawn(event.getCard());
    }

    @Override
    public void visit(StartBuildingEvent event) {
        ui.showBuildPhaseStart();
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
    public void visit(UpdateBuildingStateEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(UpdateCardStateEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(CoordRequest event){
        ui.askCoordinates();
    }

    @Override
    public void visit(LobbyEvent event) {
        ui.showWaitingForPlayers(event.getTruckers().size());
        ui.showStartCountdown(event.getCountdown());
    }

    @Override
    public void visit(IndexesRequest event){
        ui.askGoodsIndexesToRemove();
    };

    @Override
    public void visit(IntRequest event) {
        ui.askStackIndex();
    }

    public void


}
