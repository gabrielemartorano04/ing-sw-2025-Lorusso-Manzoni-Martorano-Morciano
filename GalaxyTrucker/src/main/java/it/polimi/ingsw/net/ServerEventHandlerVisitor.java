package it.polimi.ingsw.net;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.setupevents.*;
import it.polimi.ingsw.view.*;

import java.util.ArrayList;


public class ServerEventHandlerVisitor implements VirtualView {
    private final UI ui;
    private final ViewFlow viewFlow;
    public ServerEventHandlerVisitor(ViewFlow viewFlow, UI ui){
        this.viewFlow = viewFlow;
        this.ui = ui;
    }


    // === PHASE 1: Lobby ===

    @Override
    public void visit(AvailableColorsEvent event) {
        viewFlow.setViewState(ViewFlow.ViewState.START);
        ui.welcomeScene(event);
    }

    @Override
    public void visit(NicknameAlreadyInError event) {
        ui.nickNameAlreadyTaken(event);
    }

    @Override
    public void visit(ColorAlreadyInError event) {
        ui.colorAlreadyTaken(event);
    }

    @Override
    public void visit(PlayerJoinedEvent event) {
        viewFlow.setAlreadyLogged();
        ui.showPlayerJoined(event.getUsername());
        ui.showWaitForOthers();

        if(!viewFlow.getNameAlreadySet()){
            viewFlow.setNickname(event.getUsername());
            viewFlow.setShip(event.getSpaceShip());
        }

    }


    // === PHASE 2: Building ===

    @Override
    public void visit(StartBuildingEvent event) {
        if(!viewFlow.getNameAlreadySet()){
            viewFlow.setNickname(event.getNickname());
            viewFlow.setShip(event.getSpaceShip());
        }

        viewFlow.setLevel(event.getLevel());
        viewFlow.setViewState(ViewFlow.ViewState.BULDING);
        viewFlow.fillAvailableComponents(event.getComponents());

        ui.startBuildingScene(event);

        if(viewFlow.getLevel()==2) {
            viewFlow.getTimerDisplay().start(event.getSeconds());
        }
    }

    @Override
    public void visit(ComponentLinkedEvent event) {
        viewFlow.setFirstLinked();
        viewFlow.getShip().getComponents()[event.getX()][event.getY()]=event.getComponent();
        viewFlow.deleteSelectedComponent();
        ui.componentLinked(event);
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        if (!viewFlow.getKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getKeptComponent());
        }
        if (viewFlow.getKeptComponent() == null) {
            ui.showBuildOptions();
        }
    }

    @Override
    public void visit(ComponentNotAvailableError event) {
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        if (!viewFlow.getKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getKeptComponent());
        }
        if (viewFlow.getKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.componentNotAvailable(event);
    }

    @Override
    public void visit(ComponentSelectedEvent event) {
        viewFlow.componentAsUncovered(event.getIndex());
        viewFlow.componentAsSelected(event.getIndex());
        ui.refreshAvailableComponents();
        if(viewFlow.getNickname().equals(event.getNickname())) {
            viewFlow.setSelectedComponent(event.getIndex(), false);

            ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
            if (!viewFlow.getKeptComponent().isEmpty()) {
                ui.showKeptComponents(viewFlow.getKeptComponent());
            }
            ui.componentSelected(event);
        }
    }

    @Override
    public void visit(DiscardSuccessEvent event) {
        if(viewFlow.getNickname().equals(event.getNickname())){
            ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
            if (!viewFlow.getKeptComponent().isEmpty()) {
                ui.showKeptComponents(viewFlow.getKeptComponent());
            }
            if (viewFlow.getKeptComponent() == null) {
                ui.showBuildOptions();
            }
            ui.showDiscardComponent(viewFlow.getSelectedComponent());
            viewFlow.deleteSelectedComponent();
            ui.showAvailableComponent(viewFlow.getAvailableComponents());
        }
        viewFlow.componentAsUnselected(event.getIndex());

    }

    @Override
    public void visit(InvalidCoordinatesError event) {
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        if (!viewFlow.getKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getKeptComponent());
        }
        if (viewFlow.getKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.invalidCoordinates(event);
    }

    @Override
    public void visit(TurnHourglassError event) {
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        if (!viewFlow.getKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getKeptComponent());
        }
        if (viewFlow.getKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.turnHourGlassError(event);
    }

    @Override
    public void visit(HourglassTurnEvent event) {
        viewFlow.getTimerDisplay().start(event.getSeconds());
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        if (!viewFlow.getKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getKeptComponent());
        }
        if (viewFlow.getKeptComponent() == null) {
            ui.showBuildOptions();
        }
    }

    @Override
    public void visit(BuildCompleteEvent event){
        viewFlow.getTimerDisplay().stop();
        viewFlow.setViewState(ViewFlow.ViewState.FILL);

        for(Trucker t: event.getTruckers()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                viewFlow.setShip(t.getSpaceShip());
            }
        }

        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.buildingComplete(event);
    }

    @Override
    public void visit(KeptComponentEvent event){
        viewFlow.addKeptComponent(event.getKept());
        viewFlow.deleteSelectedComponent();
        ui.keptComponent(event);
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        if (!viewFlow.getKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getKeptComponent());
        }
        if (viewFlow.getKeptComponent() == null) {
            ui.showBuildOptions();
        }
    }

    @Override
    public void visit(TurnedComponentSuccessEvent event) {
        System.out.println("Turn andato a buon finite, visitor del client");
        viewFlow.setSelectedComponent(event.getComponent());
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        if (!viewFlow.getKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getKeptComponent());
        }
        if (viewFlow.getKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.componentTurned(event);
    }

    @Override
    public void visit(SuccessSelectKept event){
        viewFlow.setSelectedComponent(event.getComponent(), true);
        ui.keptSelected(event);

        viewFlow.getKeptComponent().remove(event.getIndex());
        if (!viewFlow.getKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getKeptComponent());
        }
        if (viewFlow.getKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());

    }

    @Override
    public void visit(WaitOthersBuildingEvent event){
        viewFlow.setEnd();
        ui.showWaitForOthers();
    }

    @Override
    public void visit(WaitOthersStartFlightEvent event) {
        //viewFlow.setStartFlight();
        System.out.println("WaitOthersStartFlight");
        ui.showWaitForOthers();
    }

    @Override
    public void visit(AlienCabinCoord event) {
        ui.askAlienCabin(event);
    }

    @Override
    public void visit(ReturnStackEvent event){
        viewFlow.setHasStack(event.getStack().getIndexOfThis());
        ui.showStack(event.getStack());
    }

    @Override
    public void visit(StackDropOk event) {
        viewFlow.setHasStack(-1);
        ui.dropStack(event);
    }


    // === PHASE 3: Flight ===

    @Override
    public void visit(FlightStartEvent event){
        viewFlow.setPosition(event.getPosition());
        viewFlow.setShip(event.getSpaceShip());
        viewFlow.setViewState(ViewFlow.ViewState.FLIGTH);
        viewFlow.setStartFlightForAll();
        viewFlow.setTrack(event.getTrack());
        viewFlow.setShip(event.getSpaceShip());

        ui.flightScene(event);

    }

    @Override
    public void visit(CurrentCondition event) {
        boolean isLeader = false;

        if(event.getTrack()!=null){
            viewFlow.setTrack(event.getTrack());
            ui.showTrack(event.getTrack(), viewFlow.getLevel());
        }

        handleDeath(event.getTruckersRemoved());

        if(!viewFlow.isOut()){
            updatesFromFlight(event.getTruckers());
        }

        if(event.getNickname().equals(viewFlow.getNickname())){
            viewFlow.setParserFlightState(ParserState.CURRENT_CONDITION);
            isLeader = true;
        }
        else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }

        ui.showCurrCondition(event.getCondition(), isLeader);
        ui.showInputFormat(viewFlow.getParserFlightState());
    }

    @Override
    public void visit(BooleanDecision event) {

        updatesFromFlight(event.getTruckers());

        if (event.getNickname().equals(viewFlow.getNickname())){
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(viewFlow.getParserFlightState());
        }
        else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(CardFinished event) {
        handleDeath(event.getTruckersRemoved());

        if(!viewFlow.isOut()){
            updatesFromFlight(event.getTruckersRemoved());
        }

        Trucker trucker=null;
        for(Trucker t:event.getTruckers()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
                break;
            }
        }

        if(!viewFlow.isOut()){
            viewFlow.setCredits(trucker.getCosmicCredit());
            viewFlow.setPosition(trucker.getPosition());

            viewFlow.setTrack(event.getTrack());
            viewFlow.setShip(trucker.getSpaceShip());

            ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
            ui.showPlayerShipStatus();

            ui.cardFinished(event);
            viewFlow.setParserFlightState(ParserState.ASK_WITHDRAW);
            ui.showInputFormat(viewFlow.getParserFlightState());

        }
    }

    @Override
    public void visit(DrawNewCardEvent event) {
        if (viewFlow.getNickname().equals(event.getLeaderNickname())) {
            viewFlow.setParserFlightState(ParserState.DRAW_CARD);
            ui.showInputFormat(ParserState.DRAW_CARD);
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.showInfoForOthers());
        }
    }

    @Override
    public void visit(EpidemicEvent event) {
        ui.showEpidemicCard(event);

        handleDeath(event.getTruckersRemoved());

        viewFlow.setTrack(event.getTrack());
        if(!viewFlow.isOut()) {
            updatesFromFlight(event.getTruckers());

            if (viewFlow.getNickname().equals(event.getTruckers().getFirst().getNickname())) {
                viewFlow.setParserFlightState(ParserState.DRAW_CARD);
            } else viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }

        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.epidemic(event);

        if(!viewFlow.getParserFlightState().equals(ParserState.DO_NOTHING)){
            ui.showInputFormat(viewFlow.getParserFlightState());
        }
    }

    @Override
    public void visit(HumanCoordinates event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());

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
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());

        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.PLANET_DECISION);
            ui.updatePlanetsOccupied(event);
            ui.showInputFormat(ParserState.PLANET_DECISION);
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(PlotGood event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());

        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.PLOT_GOOD);
            ui.updateCurrGood(event.getGood());
            ui.showInputFormat(ParserState.PLOT_GOOD);
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(RollDiceResult event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());

        ui.showResult(event);
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(viewFlow.getParserFlightState());
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(StarDustEvent event) {
        ui.showStardustCard(event);
        viewFlow.setTrack(event.getTrack());

        handleDeath(event.getTruckersRemoved());

        if(!viewFlow.isOut()){
            updatesFromFlight(event.getTruckers());
            if (viewFlow.getNickname().equals(event.getTruckers().getFirst().getNickname())) {
                viewFlow.setParserFlightState(ParserState.DRAW_CARD);
                ui.showInputFormat(ParserState.DRAW_CARD);
            } else viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }

        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.stardust(event);

        if(!viewFlow.getParserFlightState().equals(ParserState.DO_NOTHING)){
            ui.showInputFormat(viewFlow.getParserFlightState());
        }

    }

    @Override
    public void visit(StartCardEvent event) {
        ui.showCardDrawn(event);
        viewFlow.decreaseCardsToGo();
        System.out.println("Number of cards to draw: " + viewFlow.getCardsToGo());
        if (event.getFirstPlayerNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }


    }

    @Override
    public void visit(RearrangeEvent event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());

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
            viewFlow.setCosmicCreditsToGain(event.getCosmicCredits());
            viewFlow.setDaysLost(event.getDaysLost());
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
            ui.showError(event.getMessage());
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
            ui.showError(event.getMessage());
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
            ui.showError(event.getMessage());
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
            ui.showError(event.getMessage());
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
            ui.showError(event.getMessage());
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
            ui.showError(event.getMessage());
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
            ui.showError(event.getMessage());
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
            ui.showError(event.getMessage());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(RemotionConfirmed event) {

        if(event.getNickname().equals(viewFlow.getNickname())) {
            updatesFromFlight(event.getTruckers());
            ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());

            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }


    @Override
    public void visit(RemoveBatteries event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());

        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            viewFlow.setNumOfBatteriesToRemove(event.getBatteriesToRemove());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(RemoveGoods event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());

        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.updateGoodsListToRemove(event.getGoodsList());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }



    // === PHASE 5: Fine Gioco ===
    @Override
    public void visit(EndGameEvent event) {
        ui.showScores(event.getScores());
        ui.showGameEnded();
        viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        viewFlow.setViewState(ViewFlow.ViewState.END);
    }

    @Override
    public void visit(TruckerWithdrawEvent event) {
        boolean choice=event.getChoice();
        if(choice){
            if(event.getNickname().equalsIgnoreCase(viewFlow.getNickname())) {
                viewFlow.setParserFlightState(ParserState.DEAD);
                ui.showDead();
            }
            else ui.showTruckerWithdrawn(event.getNickname());
        }
        else{
            if(event.getNickname().equalsIgnoreCase(viewFlow.getNickname())) {
                viewFlow.setParserFlightState(ParserState.DO_NOTHING);
                ui.showWaitForOthers();
            }
        }
    }



    // === ERRORI e MESSAGGI GENERICI ===


    @Override
    public void visit(NoAvailableSpaceError event) {
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(GenericErrorEvent event){
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(ShowOtherShipResponse event) {
        ui.showOtherShipLayout(event.getNickname(), event.getSpaceship());
    }

    @Override
    public void visit(FirstConnectionEvent event) {
        ui.showInsertNumPlayers();
        viewFlow.setViewState(ViewFlow.ViewState.FIRST);
    }


    @Override
    public void visit(ResumeGameEvent event) {
        Trucker trucker=null;
        if(viewFlow.getNickname()==null) {
            viewFlow.setNickname(event.getNickname());
        }
        boolean isLeader = false;
        if(viewFlow.getNickname().equalsIgnoreCase(event.getTruckers().getFirst().getNickname())) {
            isLeader = true;
        }
        for(Trucker t: event.getTruckers()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
            }
        }

        if(trucker == null){
            // solo per debug, NON deve mai succedere
            throw new RuntimeException("Trucker is null");
        }

        try {
            if (trucker.getKeptComponent(0) != null) {
                viewFlow.addKeptComponent(trucker.getKeptComponent(0));
            }
        }catch (GeneralError e){
            ui.showError(e.getMessage());
        }
        try {
            if(trucker.getKeptComponent(1)!=null) {
                viewFlow.addKeptComponent(trucker.getKeptComponent(1));
            }
        }catch (GeneralError e){
            ui.showError(e.getMessage());
        }

        if(trucker.getSelectedComponent() != null) {
            viewFlow.setSelectedComponent(trucker.getSelectedComponent());
        }

        viewFlow.setParserFlightState(ParserState.STATE);
        viewFlow.setLevel(event.getLevel());
        if(event.getTrack()!=null) {
            viewFlow.setTrack(event.getTrack());
        }

        viewFlow.setPosition(trucker.getPosition());
        viewFlow.setShip(trucker.getSpaceShip());

        switch (event.getGameState()) {
            case FLIGHT -> {
                viewFlow.setViewState(ViewFlow.ViewState.FLIGTH);
                viewFlow.setStartFlightForAll();
                viewFlow.setStartFlight(false);
                viewFlow.setEnd();
                ui.resumeFlightScene(event.getPlayerNames());

                if(isLeader){
                    viewFlow.setParserFlightState(ParserState.DRAW_CARD);
                    ui.showInputFormat(ParserState.DRAW_CARD);
                }else{
                    viewFlow.setParserFlightState(ParserState.DO_NOTHING);
                }
            }
            case ENDED -> viewFlow.setViewState(ViewFlow.ViewState.END);
            case BUILDING -> {
                viewFlow.setViewState(ViewFlow.ViewState.BULDING);
                viewFlow.fillAvailableComponents(event.getComponents());
                ui.resumeBuildingScene(event.getPlayerNames());
            }

        }

    }

    @Override
    public void visit(ChooseResumeEvent chooseResumeEvent) {
        viewFlow.setViewState(ViewFlow.ViewState.CHOOSE_OLD);
        ui.showChooseResume();
    }

    @Override
    public void visit(AvailablePlayersEvent availablePlayersEvent) {
        if(viewFlow.getNickname()==null) {
            viewFlow.setViewState(ViewFlow.ViewState.START);
        }else return;
        ui.welcomeOldScene(availablePlayersEvent.getColors(), availablePlayersEvent.getNicknames());
    }

    @Override
    public void handleDeath(ArrayList<Trucker> truckersRemoved) {
        for (Trucker trucker : truckersRemoved) {
            if (trucker.getNickname().equalsIgnoreCase(viewFlow.getNickname())) {
                viewFlow.setCredits(trucker.getCosmicCredit());
                viewFlow.setPosition(trucker.getPosition());
                viewFlow.setShip(trucker.getSpaceShip());

                viewFlow.setOut();
                viewFlow.setParserFlightState(ParserState.DEAD);

                ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());
                ui.showDead();

                break;
            }
        }
    }

    private void updatesFromFlight (ArrayList<Trucker> truckers){
        for(Trucker t:truckers){
            if(t.getNickname().equals(viewFlow.getNickname())){
                viewFlow.setCredits(t.getCosmicCredit());
                viewFlow.setPosition(t.getPosition());
                viewFlow.setShip(t.getSpaceShip());
                break;
            }
        }
    }


    @Override
    public void visit(PingEvent e){
        viewFlow.respondToPing();
    }




}
