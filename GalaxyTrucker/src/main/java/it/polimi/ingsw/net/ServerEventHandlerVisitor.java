package it.polimi.ingsw.net;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.flightevents.*;
import it.polimi.ingsw.net.events.serverevents.setupevents.*;
import it.polimi.ingsw.view.*;


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
        ui.showPlayerJoined(event.getUsername());

        if(!viewFlow.getNameAlreadySet()){
            viewFlow.setNickname(event.getUsername());
        }

    }


    // === PHASE 2: Building ===

    @Override
    public void visit(StartBuildingEvent event) {
        if(!viewFlow.getNameAlreadySet()){
            viewFlow.setNickname(event.getNickname());
        }
        viewFlow.setLevel(event.getLevel());
        viewFlow.setViewState(ViewFlow.ViewState.BULDING);
        viewFlow.setShip(event.getSpaceShip().getComponents());
        viewFlow.fillAvailableComponents(event.getComponents());

        ui.startBuildingScene(event);

        if(viewFlow.getLevel()==2) {
            viewFlow.getTimerDisplay().start(event.getSeconds());
        }
    }

    @Override
    public void visit(ComponentLinkedEvent event) {
        viewFlow.getShip()[event.getX()][event.getY()]=event.getComponent();
        viewFlow.deleteSelectedComponent();

        ui.componentLinked(event);
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
        }
        if (viewFlow.getSelectedKeptComponent() == null) {
            ui.showBuildOptions();
        }
    }

    @Override
    public void visit(ComponentNotAvailableError event) {
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
        }
        if (viewFlow.getSelectedKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.componentNotAvailable(event);
    }

    @Override
    public void visit(ComponentSelectedEvent event) {
        viewFlow.componentAsUncovered(event.getIndex());
        viewFlow.componentAsSelected(event.getIndex());

        if(viewFlow.getNickname().equals(event.getNickname())) {
            viewFlow.setSelectedComponent(event.getIndex(), false);

            ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
            if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
                ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
            }
            ui.componentSelected(event);
        }
    }

    @Override
    public void visit(DiscardSuccessEvent event) {
        if(viewFlow.getNickname().equals(event.getNickname())){
            ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
            if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
                ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
            }
            if (viewFlow.getSelectedKeptComponent() == null) {
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
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
        }
        if (viewFlow.getSelectedKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.invalidCoordinates(event);
    }

    @Override
    public void visit(InvalidPositionError event) {
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
        }
        if (viewFlow.getSelectedKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.invalidPosition(event);
    }

    @Override
    public void visit(TurnHourglassError event) {
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
        }
        if (viewFlow.getSelectedKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.turnHourGlassError(event);
    }

    @Override
    public void visit(HourglassTurnEvent event) {
        viewFlow.getTimerDisplay().start(event.getSeconds());
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
        }
        if (viewFlow.getSelectedKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.turnHourGlass(event);
    }

    @Override
    public void visit(BuildCompleteEvent event){
        viewFlow.getTimerDisplay().stop();
        viewFlow.setViewState(ViewFlow.ViewState.FILL);
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        ui.buildingComplete(event);
    }

    @Override
    public void visit(KeptComponentEvent event){
        viewFlow.addKeptComponent(event.getKept());
        viewFlow.deleteSelectedComponent();
        ui.keptComponent(event);
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
        }
        if (viewFlow.getSelectedKeptComponent() == null) {
            ui.showBuildOptions();
        }
    }

    @Override
    public void visit(TurnedComponentSuccessEvent event) {
        viewFlow.setSelectedComponent(event.getComponent());
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
        }
        if (viewFlow.getSelectedKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.componentTurned(event);
    }

    @Override
    public void visit(SuccessSelectKept event){
        viewFlow.setSelectedComponent(event.getComponent(), true);
        viewFlow.getSelectedKeptComponent().remove(event.getIndex());
        if (!viewFlow.getSelectedKeptComponent().isEmpty()) {
            ui.showKeptComponents(viewFlow.getSelectedKeptComponent());
        }
        if (viewFlow.getSelectedKeptComponent() == null) {
            ui.showBuildOptions();
        }
        ui.keptSelected(event);
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());

    }

    @Override
    public void visit(WaitOthersBuildingEvent event){
        viewFlow.setEnd();
        ui.showWaitForOthers();
    }

    @Override
    public void visit(WaitOthersStartFlightEvent event) {
        viewFlow.setStartFlight();
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
        viewFlow.setTrack(event.getTrack());
        viewFlow.setParserFlightState(ParserState.STATE);
        ui.showInputFormat(viewFlow.getParserFlightState());
    }

    @Override
    public void visit(CurrentCondition event) {
        viewFlow.setParserFlightState(ParserState.CURRENT_CONDITION);
        ui.showCurrCondition(event);
    }

    @Override
    public void visit(BooleanDecision event) {
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
        Trucker trucker=null;
        for(Trucker t:event.getTruckersList()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
                break;
            }
        }
        for(String nicknames: event.getTruckersRemoved()){
            if(nicknames.equalsIgnoreCase(viewFlow.getNickname())) {
                ui.showDead();
                viewFlow.setOut();
                viewFlow.setParserFlightState(ParserState.DEAD);
            }
        }
        if(!viewFlow.getOut()){
            viewFlow.setPosition(trucker.getPosition());
            viewFlow.setBatteryCount(trucker.getSpaceShip().getNumBatteries());
            viewFlow.setHumanCount(trucker.getSpaceShip().getNumCrew());
            viewFlow.setCannonPower(trucker.getSpaceShip().getSingleCannonPower());
            viewFlow.setEnginePower(trucker.getSpaceShip().getSingleEnginePower());
            viewFlow.setTrack(event.getTrack());
            ui.showShipLayout(viewFlow.getNickname(), trucker.getSpaceShip().getComponents());
            ui.cardFinished(event);
            if (viewFlow.getNickname().equals(event.getTruckersList().get(0).getNickname())) {
                viewFlow.setParserFlightState(ParserState.DRAW_CARD);
                ui.showInputFormat(ParserState.DRAW_CARD);
            } else viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }
    }

    @Override
    public void visit(EpidemicEvent event) {
        Trucker trucker=null;
        for(Trucker t:event.getTruckers()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
                break;
            }
        }
        for(String nicknames: event.getTruckersRemoved()){
            if(nicknames.equalsIgnoreCase(viewFlow.getNickname())) {
                ui.showDead();
                viewFlow.setOut();
                viewFlow.setParserFlightState(ParserState.DEAD);
            }
        }
        if(!viewFlow.getOut()){
            viewFlow.setPosition(trucker.getPosition());
            viewFlow.setBatteryCount(trucker.getSpaceShip().getNumBatteries());
            viewFlow.setHumanCount(trucker.getSpaceShip().getNumCrew());
            viewFlow.setCannonPower(trucker.getSpaceShip().getSingleCannonPower());
            viewFlow.setEnginePower(trucker.getSpaceShip().getSingleEnginePower());
            viewFlow.setTrack(event.getTrack());
            ui.showShipLayout(viewFlow.getNickname(), trucker.getSpaceShip().getComponents());
            ui.epidemic(event);
            if (viewFlow.getNickname().equals(event.getTruckers().get(0).getNickname())) {
                viewFlow.setParserFlightState(ParserState.DRAW_CARD);
                ui.showInputFormat(ParserState.DRAW_CARD);
            } else viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }
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
            switch (event.getHitType()) {
                case "bigmeteor" -> {
                    ui.showResult("The dices result for the meteor # " + event.getCurrIndex() + " is " + event.getDiceResult() + "\n" + event.getMessage());
                    viewFlow.setParserFlightState(ParserState.ACTIVATE_CANNONS);
                }
                case "smallmeteor" -> {
                    ui.showResult("The dices result for the meteor # " + event.getCurrIndex() + " is " + event.getDiceResult() + "\n" + event.getMessage());
                    viewFlow.setParserFlightState(ParserState.ACTIVATE_SHIELD);
                }
                case "smallshot"-> {
                    ui.showResult("The dices result for the shot # " + event.getCurrIndex() + " is " + event.getDiceResult() + "\n" + event.getMessage());
                    viewFlow.setParserFlightState(ParserState.ACTIVATE_SHIELD);
                }
            }
            ui.showInputFormat(viewFlow.getParserFlightState());
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(StarDustEvent event) {

        Trucker trucker=null;
        for(Trucker t:event.getTruckers()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
                break;
            }
        }
        for(String nicknames: event.getTruckersRemoved()){
            if(nicknames.equalsIgnoreCase(viewFlow.getNickname())) {
                ui.showDead();
                viewFlow.setOut();
                viewFlow.setParserFlightState(ParserState.DEAD);
            }
        }
        if(!viewFlow.getOut()){
            viewFlow.setPosition(trucker.getPosition());
            viewFlow.setBatteryCount(trucker.getSpaceShip().getNumBatteries());
            viewFlow.setHumanCount(trucker.getSpaceShip().getNumCrew());
            viewFlow.setCannonPower(trucker.getSpaceShip().getSingleCannonPower());
            viewFlow.setEnginePower(trucker.getSpaceShip().getSingleEnginePower());
            viewFlow.setTrack(event.getTrack());
            ui.showShipLayout(viewFlow.getNickname(), trucker.getSpaceShip().getComponents());
            ui.stardust(event);
            if (viewFlow.getNickname().equals(event.getTruckers().get(0).getNickname())) {
                viewFlow.setParserFlightState(ParserState.DRAW_CARD);
                ui.showInputFormat(ParserState.DRAW_CARD);
            } else viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }

    }

    @Override
    public void visit(StartCardEvent event) {
        ui.showCardDrawn(event);
        if (event.getFirstPlayerNickname().equals(viewFlow.getNickname())) {
            ui.showMessage(event.getQuestion());
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        } else {
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
    public void visit(GenericErrorEvent event){
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(ShowOtherShipResponse event) {
        ui.showShipLayout(event.getNickname(), event.getSpaceship());
    }

    @Override
    public void visit(FirstConnectionEvent event) {

    }

    @Override
    public void visit(PingEvent event){

    }

    @Override
    public void visit(ResumeGameEvent event) {
        Trucker trucker=null;
        for(Trucker t: event.getTrucker()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
            }
        }
        if(trucker!=null) {
            viewFlow.setParserFlightState(ParserState.STATE);
            switch (event.getGameState()) {
                case FLIGHT -> viewFlow.setViewState(ViewFlow.ViewState.FLIGTH);
                case ENDED -> viewFlow.setViewState(ViewFlow.ViewState.END);
                case BUILDING -> viewFlow.setViewState(ViewFlow.ViewState.BULDING);

            }
            viewFlow.setLevel(event.getLevel());
            if(event.getTrack()!=null) {
                viewFlow.setTrack(event.getTrack());
            }
            viewFlow.setOut();
            viewFlow.setEnd();
            viewFlow.setBatteryCount(trucker.getSpaceShip().getNumBatteries());
            viewFlow.setCannonPower(trucker.getSpaceShip().getSingleCannonPower());
            //viewFlow.setGoodsByColor(trucker.getSpaceShip().getGoodsList());
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

            viewFlow.setHumanCount(trucker.getSpaceShip().getNumCrew());
            viewFlow.setPosition(trucker.getPosition());
            viewFlow.setShip(trucker.getSpaceShip().getComponents());
        }
    }

    @Override
    public void visit(ChooseResumeEvent chooseResumeEvent) {
        ui.showMessage(chooseResumeEvent.getMessage());
    }

    @Override
    public void visit(AvailablePlayersEvent availablePlayersEvent) {
        viewFlow.setViewState(ViewFlow.ViewState.START);
        ui.welcomeOldScene(availablePlayersEvent.getColors(), availablePlayersEvent.getNicknames());
    }
}
