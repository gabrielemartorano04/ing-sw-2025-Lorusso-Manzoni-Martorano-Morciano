package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.ShowStackEvent;
import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.*;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.text.TUI;


public class ServerEventHandlerVisitor implements ServerEventVisitor{
    private final UI ui=new TUI();
    private final ViewFlow viewFlow;
    public ServerEventHandlerVisitor(ViewFlow viewFlow){
        this.viewFlow = viewFlow;
    }

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
        ui.showChooseColor(event.getColors());
    }

    @Override
    public void visit(AvailableColorsEvent event) {
        ui.showWelcome();
        System.out.println("Ho ricevuto: "+ event.getEventType());
        ui.showChooseColor(event.getColors());
    }

    @Override
    public void visit(ColorAlreadyInError event) {
        ui.showError(event.getMessage());
        ui.showColorTaken();
        ui.showChooseColor(event.getAvailableColors());
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
        viewFlow.setViewState(ViewFlow.ViewState.BULDING);
        viewFlow.setShip(event.getSpaceShip().getComponents());
        viewFlow.setStacks(event.getStacks());
        ui.showBuildPhaseStart(event.getComponents());
        ui.showBuildOptions();
        viewFlow.getTimerDisplay().start();
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
        viewFlow.setSelectedComponent(event.getComponent());
        ui.showComponentOptions();
    }

    @Override
    public void visit(DiscardSuccessEvent event) {
        ui.showDiscardComponent(viewFlow.getSelectedComponent());
        viewFlow.deleteSelectedComponent();
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
        ui.showInvalidPlacement(event.getMessage());
        ui.askCoordinates();
    }

    @Override
    public void visit(UpdateBuildingStateEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(TurnHourglassError event) {
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(AvailableComponentsEvent event) {
        viewFlow.setAvComponents(event.getComponents());
        ui.showSelectComponent();
    }

    @Override
    public void visit(NotYourTurnError notYourTurnError) {

    }

    @Override
    public void visit(NumberOfPlayersError numberOfPlayersError) {

    }

    @Override
    public void visit(PlacedComponentRequest placedComponentRequest) {

    }

    @Override
    public void visit(TruckerLostCrewEvent truckerLostCrewEvent) {

    }

    @Override
    public void visit(HourglassTurnEvent event) {
        viewFlow.getTimerDisplay().start();
    }

    @Override
    public void visit(GameCreatedEvent gameCreatedEvent) {

    }

    @Override
    public void visit(FirstConnectionEvent firstConnectionEvent) {

    }

    @Override
    public void visit(CoordListRequest coordListRequest) {

    }

    @Override
    public void visit(AvailableComponentToSelectRequest availableComponentToSelectRequest) {

    }

    @Override
    public void visit(TimeExpiredEvent event){
        ui.showTimeExpired();
    }

    @Override
    public void visit(BuildCompleteEvent event){
        viewFlow.setViewState(ViewFlow.ViewState.FLIGTH);
        ui.showBuildComplete();
        ui.showWaitForOthers();
    }

    @Override
    public void visit(KeptComponentEvent event){
        viewFlow.addKeptComponent(event.getKept());
    }

    @Override
    public void visit(BuildOptionsEvent event){
        ui.showBuildOptions();
    }

    // === PHASE 4: Fase di Volo ===
    @Override
    public void visit(SelectComponentEvent event){
        ui.askPlacedComponent();
    }

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
        ui.showGoodsToRemove(event.getGoodsToRemove());
        ui.askGoodsIndexesToRemove();
    }

    @Override
    public void visit(GoodsGainedEvent event){
        ui.showGoodsGained(event.getGoodGained());
        ui.askIndex();
    }

    @Override
    public void visit(OkEvent event){
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(FlightStartEvent event){
        ui.showFlightPhaseStart();
    }

    @Override
    public void visit(IntRequest event) {
        ui.askIndex();
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

    @Override
    public void visit(TruckerWithdrawEvent event) {
        ui.showTruckerWithdrawn(event.getTruckerWithdrawNick());
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

    @Override
    public void visit(ReturnStackEvent event){
        ui.showStack(event.getStack());
    }
}
