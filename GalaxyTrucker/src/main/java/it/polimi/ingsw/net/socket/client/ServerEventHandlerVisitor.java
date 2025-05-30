package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.VirtualView;
import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.AlienCabinCoord;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.*;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.text.TUI;

import java.util.ArrayList;


public class ServerEventHandlerVisitor implements VirtualView {
    private final UI ui;
    private final ViewFlow viewFlow;
    public ServerEventHandlerVisitor(ViewFlow viewFlow){
        this.viewFlow = viewFlow;
        ui = new TUI();
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
        if(!viewFlow.getNameAlreadySet()){
            viewFlow.setNickname(event.getUsername());
        }

    }

    @Override
    public void visit(NicknameAlreadyInError event) {
        ui.showNicknameTaken();
        ui.showChooseColor(event.getColors());
    }

    @Override
    public void visit(AvailableColorsEvent event) {
        ui.showWelcome();
        System.out.println("I received: "+ event.getEventType());
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
        //potrebbe non funzionare come player joined
        if(!viewFlow.getNameAlreadySet()){
            System.out.println("Inside");
            viewFlow.setNickname(event.getNickname());
        }
        ui.showPlayers(event.getPlayers());
        viewFlow.setViewState(ViewFlow.ViewState.BULDING);
        viewFlow.setShip(event.getSpaceShip().getComponents());
        viewFlow.setStacks(event.getStacks());
        viewFlow.fillAvailableComponents(event.getComponents());
        ui.showBuildPhaseStart(event.getComponents());
        ui.showBuildOptions();
        viewFlow.getTimerDisplay().start(event.getSeconds());
    }
    @Override
    public void visit(ComponentLinkedEvent event) {
        viewFlow.getShip()[event.getX()][event.getY()]=event.getComponent();
        viewFlow.deleteSelectedComponent();
        ui.showComponentPlaced(event.getComponent(), event.getX(), event.getY());
        ui.showAvailableComponent(event.getAvComponents());
    }

    @Override
    public void visit(ComponentNotAvailableError event) {
        ui.showError(event.getMessage());
        ui.showAvailableComponent(viewFlow.getAvailableComponents());
        ui.showMessage("Please select a component between 0 and " + (viewFlow.getAvailableComponents().size()-1));
    }

    @Override
    public void visit(ComponentSelectedEvent event) {
        viewFlow.componentAsUncovered(event.getIndex());
        viewFlow.componentAsSelected(event.getIndex());
        if(viewFlow.getNickname().equals(event.getNickname())) {
            viewFlow.setSelectedComponent(event.getIndex(), false);
            ui.showComponentSelected(viewFlow.getSelectedComponent());
            ui.showComponentOptions();
        }
    }

    @Override
    public void visit(DiscardSuccessEvent event) {
        if(viewFlow.getNickname().equals(event.getNickname())){
            ui.showDiscardComponent(viewFlow.getSelectedComponent());
            viewFlow.deleteSelectedComponent();
        }
        viewFlow.componentAsUnselected(event.getIndex());
        ui.showAvailableComponent(viewFlow.getAvailableComponents());
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
        ui.showAvailableComponent(event.getComponents());
        ui.showSelectComponent();
    }

    @Override
    public void visit(NotYourTurnError event) {
    }

    @Override
    public void visit(NumberOfPlayersError event) {
    }

    @Override
    public void visit(PlacedComponentRequest event) {

    }

    @Override
    public void visit(TruckerLostCrewEvent event) {
    }

    @Override
    public void visit(HourglassTurnEvent event) {
        viewFlow.getTimerDisplay().start(event.getSeconds());
    }

    @Override
    public void visit(GameCreatedEvent event) {

    }

    @Override
    public void visit(FirstConnectionEvent event) {

    }

    @Override
    public void visit(CoordListRequest event) {

    }

    @Override
    public void visit(AvailableComponentToSelectRequest event) {

    }

    @Override
    public void visit(TimeExpiredEvent event){
        ui.showTimeExpired();
    }

    @Override
    public void visit(BuildCompleteEvent event){
        viewFlow.getTimerDisplay().stop();
        ui.showMessage("Your updated ship\n");
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        ui.showBuildComplete();
        ui.showMessage("Type 'alien' to see where you can place aliens");
        viewFlow.setViewState(ViewFlow.ViewState.FILL);
        //ui.showMessage("It's time to fill the spaceship\n");
    }


    @Override
    public void visit(KeptComponentEvent event){
        viewFlow.addKeptComponent(event.getKept());
        viewFlow.deleteSelectedComponent();
        ui.showAvailableComponent(event.getAvComponents());
    }

    @Override
    public void visit(TurnedComponentSuccessEvent event) {
        viewFlow.setSelectedComponent(event.getComponent());
        ui.showComponentSelected(event.getComponent());
    }

    @Override
    public void visit(CurrentCondition currentCondition) {

    }


    @Override
    public void visit(BuildOptionsEvent event){
        ui.showBuildOptions();
    }

    @Override
    public void visit(SuccessSelectKept event){
        viewFlow.setSelectedComponent(event.getComponent(), true);
        viewFlow.getSelectedKeptComponent().remove(event.getComponent());
        ui.showComponentSelected(viewFlow.getSelectedComponent());
    }

    @Override
    public void visit(ShowOtherShipResponse event) {
        ui.showShipLayout(event.getNickname(), event.getSpaceship());
    }

    @Override
    public void visit(WaitOthersBuildingEvent event){
        viewFlow.setEnd();
        ui.showWaitForOthers();
    }

    @Override
    public void visit(AlienCabinCoord event) {
        ArrayList<int[]> coord=event.getAlienCabinCoord();

        if(event.getAlienCabinCoord().isEmpty()){
            ui.showMessage("you can't place any alien. Please type 'coord' to go on\n");
        }
        else {
            String message="";
            for(int[] c:coord){
                message+="x: "+c[0]+" y: "+c[1]+"\n";
            }
            ui.showMessage("\nList of cabin available for aliens:\n" + message);
            ui.showMessage("Insert coordinates (coord <x1> <y1> <x2> <y2>...):");
        }
    }

    // === PHASE 4: Fase di Volo ===
    @Override
    public void visit(SelectComponentEvent event){
        ui.askPlacedComponent();
    }

    @Override
    public void visit(ShowCardEvent event) {

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
        viewFlow.setPosition(event.getPosition());
        viewFlow.setBatteryCount(event.getBatteryCount());
        viewFlow.setHumanCount(event.getHumanCount());
        viewFlow.setCannonPower(event.getCannonPower());
        viewFlow.setEnginePower(event.getEnginePower());
        ui.showShipLayout(viewFlow.getNickname(), event.getSpaceShip());
        viewFlow.setShip(event.getSpaceShip());
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        viewFlow.setViewState(ViewFlow.ViewState.FLIGTH);
        viewFlow.setStartFlightForAll();
        ui.showFlightPhaseStart();
        ui.showMessage("Please type 'state' to see your spaceship state\n");
        viewFlow.setTrack(event.getTrack());
    }

    @Override
    public void visit(IntRequest event) {
        ui.askIndex();
    }

    // === PHASE 5: Fine Gioco ===
    @Override
    public void visit(EndGameEvent event) {
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
    public void visit(ReturnStackEvent event){
            ui.showStack(event.getStack());
            viewFlow.setHasStack(event.getStack().getIndexOfThis());
    }

    @Override
    public void visit(BuildingStateError event){
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(GenericErrorEvent event){
        ui.showError(event.getMessage());
    }




    @Override
    public void visit(WaitOthersStartFlightEvent event) {
        viewFlow.setStartFlight();
        ui.showWaitForOthers();
    }

    // === CARD EVENTS ===

    @Override
    public void visit(BooleanDecision event) {
        if (event.getNickname().equals(viewFlow.getNickname())){
            System.out.println("Sono entrato, nickname view " + viewFlow.getNickname());
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(viewFlow.getParserFlightState());

        }
        else {
            System.out.println("Sono entrato, nickname view " + viewFlow.getNickname());
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }

    }

    @Override
    public void visit(CardFinished event) {
        Trucker trucker=null;
        for(Trucker t:event.getTruckersList()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
                break;
            }
        }
        for(String nicknames: event.getTruckersRemoved()){
            if(nicknames.equalsIgnoreCase(viewFlow.getNickname()) && trucker!=null) {
                System.out.println(viewFlow.getNickname() + " morto");
                viewFlow.setOut();
            }
        }
        if(!viewFlow.getOut()){
            ui.showShipLayout(viewFlow.getNickname(), trucker.getSpaceShip().getComponents());
            viewFlow.setPosition(trucker.getPosition());
            viewFlow.setBatteryCount(trucker.getSpaceShip().getNumBatteries());
            viewFlow.setHumanCount(trucker.getSpaceShip().getNumHuman());
            viewFlow.setCannonPower(trucker.getSpaceShip().getSingleCannonPower());
            viewFlow.setEnginePower(trucker.getSpaceShip().getSingleEnginePower());
            ui.showFlightPhaseStart();
            String message = "Truckers' positions order:\n";
            int i = 1;
            for (Trucker t : event.getTruckersList()) {
                message += i + ") " + t.getNickname() + "\n";
                i++;
            }
            ui.showMessage(message);
            if (viewFlow.getNickname().equals(event.getTruckersList().get(0).getNickname())) {
                viewFlow.setParserFlightState(ParserState.DRAW_CARD);
                ui.showInputFormat(ParserState.DRAW_CARD);
            } else viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }
    }

    @Override
    public void visit(EpidemicEvent event) {
        ui.showMessage("Card drawn: "+event.getCardName()+"\n Card description: "+event.getCardDescription()+"\n");
        for (Trucker t: event.getTruckers()) {
            ui.showMessage(t.getNickname() + " has lost ");
        }
        viewFlow.setParserFlightState(ParserState.DRAW_CARD);
    }

    @Override
    public void visit(HumanCoordinates event) {
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.HUMAN_COORD);
            ui.showInputFormat(ParserState.HUMAN_COORD);
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(PlanetDecision event) {
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.PLANET_DECISION);
            ui.showInputFormat(ParserState.PLANET_DECISION);
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(PlotGood event) {
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.PLOT_GOOD);
            ui.showInputFormat(ParserState.PLOT_GOOD);

        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(RollDiceResult event) {
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage("The dices result is " + event.getDiceResult() + "\n" + event.getMessage());
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(StarDustEvent event) {
        ui.showMessage("Card drawn: "+event.getCardName().toUpperCase()+"\n Card description: "+event.getCardDescription()+"\n");
        for (Trucker t: event.getTruckers()) {
            ui.showMessage(t.getNickname() + " has lost " + t.getSpaceShip().countExposedConnectors() + String.format(t.getSpaceShip().countExposedConnectors() == 1 ? " day\n" : " days\n"));
        }
        viewFlow.setParserFlightState(ParserState.DRAW_CARD);
    }

    @Override
    public void visit(StartCardEvent event) {
        ui.showCardDrawn(event);
        if (event.getFirstPlayerNickname().equals(viewFlow.getNickname())) {
            System.out.println("Sono entrato, nickname view " + viewFlow.getNickname());
            ui.showMessage(event.getQuestion());
            viewFlow.setParserFlightState(event.getParserState());
        } else {
            System.out.println("Sono entrato, nickname view " + viewFlow.getNickname());

            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(RearrangeEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(GainCosmicCredits event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(InvalidCoordinatesEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(NotEnoughBatterieEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(InvalidComponentsExceptionEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(InvalidPositionExceptionEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(PlanetNotFreeExceptionEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(InvalidGoodChosenEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(NotEnoughGoodsChosenEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(WrongNumberOfBatteriesSelectedEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(RemotionConfirmed event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(RemoveBatteries event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(RemoveGoods event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(StackDropOk event) {
        viewFlow.setHasStack(-1);
        ui.showMessage(event.getMessage());
    }

}
