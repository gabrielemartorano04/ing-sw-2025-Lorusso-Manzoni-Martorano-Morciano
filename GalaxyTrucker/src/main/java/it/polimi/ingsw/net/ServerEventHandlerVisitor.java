package it.polimi.ingsw.net;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.flightevents.*;
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
    }

    @Override
    public void visit(ComponentNotAvailableError event) {
        ui.componentNotAvailable(event);
    }

    @Override
    public void visit(ComponentSelectedEvent event) {
        viewFlow.componentAsUncovered(event.getIndex());
        viewFlow.componentAsSelected(event.getIndex());

        if(viewFlow.getNickname().equals(event.getNickname())) {
            viewFlow.setSelectedComponent(event.getIndex(), false);

            ui.componentSelected(event);
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
    public void visit(InvalidCoordinatesError event) {
        ui.invalidCoordinates(event);
    }

    @Override
    public void visit(InvalidPositionError event) {
        ui.invalidPosition(event);
    }

    @Override
    public void visit(TurnHourglassError event) {
        ui.turnHourGlassError(event);
    }

    @Override
    public void visit(HourglassTurnEvent event) {
        viewFlow.getTimerDisplay().start(event.getSeconds());

        ui.turnHourGlass(event);
    }

    @Override
    public void visit(BuildCompleteEvent event){
        viewFlow.getTimerDisplay().stop();
        viewFlow.setViewState(ViewFlow.ViewState.FILL);

        ui.buildingComplete(event);
    }

    @Override
    public void visit(KeptComponentEvent event){
        viewFlow.addKeptComponent(event.getKept());
        viewFlow.deleteSelectedComponent();

        ui.keptComponent(event);
    }

    @Override
    public void visit(TurnedComponentSuccessEvent event) {
        viewFlow.setSelectedComponent(event.getComponent());

        ui.componentTurned(event);
    }

    @Override
    public void visit(SuccessSelectKept event){
        viewFlow.setSelectedComponent(event.getComponent(), true);
        viewFlow.getSelectedKeptComponent().remove(event.getComponent());
        ui.keptSelected(event);
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
            ui.showMessage("Insert coordinates (coord <x1> <y1> <x2> <y2>...):\n");
        }
        System.out.print("> ");
    }

    @Override
    public void visit(ReturnStackEvent event){
        ui.showStack(event.getStack());
        viewFlow.setHasStack(event.getStack().getIndexOfThis());
        System.out.print("> ");
    }

    @Override
    public void visit(StackDropOk event) {
        viewFlow.setHasStack(-1);
        ui.showMessage(event.getMessage());
        System.out.print("> ");
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
        ui.showMessage("Please type 'state' to see your spaceship state\n");
        viewFlow.setTrack(event.getTrack());
        System.out.print("> ");
    }

    @Override
    public void visit(CurrentCondition event) {
        viewFlow.setParserFlightState(ParserState.CURRENT_CONDITION);
        ui.showCurrCondition(event.getCondition());
        System.out.print("> ");
    }

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
        System.out.print("> ");

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
                System.out.println(viewFlow.getNickname() + " morto");
                viewFlow.setOut();
                viewFlow.setParserFlightState(ParserState.DEAD);
            }
        }
        if(!viewFlow.getOut()){
            ui.showShipLayout(viewFlow.getNickname(), trucker.getSpaceShip().getComponents());
            viewFlow.setPosition(trucker.getPosition());
            viewFlow.setBatteryCount(trucker.getSpaceShip().getNumBatteries());
            viewFlow.setHumanCount(trucker.getSpaceShip().getNumCrew());
            viewFlow.setCannonPower(trucker.getSpaceShip().getSingleCannonPower());
            viewFlow.setEnginePower(trucker.getSpaceShip().getSingleEnginePower());
            viewFlow.setTrack(event.getTrack());
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
        System.out.print("> ");
    }

    @Override
    public void visit(EpidemicEvent event) {
        ui.showMessage("Card drawn: "+event.getCardName()+"\n Card description: "+event.getCardDescription()+"\n");
        for (Trucker t: event.getTruckers()) {
            ui.showMessage(t.getNickname() + " has lost ");
        }
        Trucker trucker=null;
        for(Trucker t:event.getTruckers()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
                break;
            }
        }
        for(String nicknames: event.getTruckersRemoved()){
            if(nicknames.equalsIgnoreCase(viewFlow.getNickname())) {
                System.out.println(viewFlow.getNickname() + " morto");
                viewFlow.setOut();
                viewFlow.setParserFlightState(ParserState.DEAD);
            }
        }
        if(!viewFlow.getOut()){
            ui.showShipLayout(viewFlow.getNickname(), trucker.getSpaceShip().getComponents());
            viewFlow.setPosition(trucker.getPosition());
            viewFlow.setBatteryCount(trucker.getSpaceShip().getNumBatteries());
            viewFlow.setHumanCount(trucker.getSpaceShip().getNumCrew());
            viewFlow.setCannonPower(trucker.getSpaceShip().getSingleCannonPower());
            viewFlow.setEnginePower(trucker.getSpaceShip().getSingleEnginePower());
            viewFlow.setTrack(event.getTrack());
            ui.showFlightPhaseStart();
            String message = "Truckers' positions order:\n";
            int i = 1;
            for (Trucker t : event.getTruckers()) {
                message += i + ") " + t.getNickname() + "\n";
                i++;
                System.out.print("> ");
            }
            ui.showMessage(message);
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");

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
                    ui.showMessage("The dices result for the meteor # " + event.getCurrIndex() + " is " + event.getDiceResult() + "\n" + event.getMessage());
                    viewFlow.setParserFlightState(ParserState.ACTIVATE_CANNONS);
                }
                case "smallmeteor" -> {
                    ui.showMessage("The dices result for the meteor # " + event.getCurrIndex() + " is " + event.getDiceResult() + "\n" + event.getMessage());
                    viewFlow.setParserFlightState(ParserState.ACTIVATE_SHIELD);
                }
                case "smallshot"-> {
                    ui.showMessage("The dices result for the shot # " + event.getCurrIndex() + " is " + event.getDiceResult() + "\n" + event.getMessage());
                    viewFlow.setParserFlightState(ParserState.ACTIVATE_SHIELD);
                }
            }
            System.out.print("> ");
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
        Trucker trucker=null;
        for(Trucker t:event.getTruckers()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
                break;
            }
        }
        for(String nicknames: event.getTruckersRemoved()){
            if(nicknames.equalsIgnoreCase(viewFlow.getNickname())) {
                System.out.println(viewFlow.getNickname() + " morto");
                viewFlow.setOut();
                viewFlow.setParserFlightState(ParserState.DEAD);
            }
        }
        if(!viewFlow.getOut()){
            ui.showShipLayout(viewFlow.getNickname(), trucker.getSpaceShip().getComponents());
            viewFlow.setPosition(trucker.getPosition());
            viewFlow.setBatteryCount(trucker.getSpaceShip().getNumBatteries());
            viewFlow.setHumanCount(trucker.getSpaceShip().getNumCrew());
            viewFlow.setCannonPower(trucker.getSpaceShip().getSingleCannonPower());
            viewFlow.setEnginePower(trucker.getSpaceShip().getSingleEnginePower());
            viewFlow.setTrack(event.getTrack());
            ui.showFlightPhaseStart();
            String message = "Truckers' positions order:\n";
            int i = 1;
            for (Trucker t : event.getTruckers()) {
                message += i + ") " + t.getNickname() + "\n";
                i++;
            }
            ui.showMessage(message);
            if (viewFlow.getNickname().equals(event.getTruckers().get(0).getNickname())) {
                viewFlow.setParserFlightState(ParserState.DRAW_CARD);
                ui.showInputFormat(ParserState.DRAW_CARD);
                System.out.print("> ");
            } else viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }

    }

    @Override
    public void visit(StartCardEvent event) {
        ui.showCardDrawn(event);
        if (event.getFirstPlayerNickname().equals(viewFlow.getNickname())) {
            System.out.println("Sono entrato, nickname view " + viewFlow.getNickname());
            ui.showMessage(event.getQuestion());
            viewFlow.setParserFlightState(event.getParserState());
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
            System.out.print("> ");
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
        System.out.print("> ");
    }

    @Override
    public void visit(TruckerWithdrawEvent event) {
        ui.showTruckerWithdrawn(event.getTruckerWithdrawNick());
        System.out.print("> ");
    }



    // === ERRORI e MESSAGGI GENERICI ===
    @Override
    public void visit(GenericErrorInsertedData event) {
        ui.showError(event.getMessage());
        System.out.print("> ");
    }

    @Override
    public void visit(NoAvailableSpaceError event) {
        ui.showError(event.getMessage());
        System.out.print("> ");
    }

    @Override
    public void visit(GenericErrorEvent event){
        ui.showError(event.getMessage());
        System.out.print("> ");
    }

    @Override
    public void visit(ShowOtherShipResponse event) {
        ui.showShipLayout(event.getNickname(), event.getSpaceship());
        System.out.print("> ");
    }




    // DEPRECATED

    @Deprecated
    public void visit(BuildingStateError event){
        ui.showError(event.getMessage());
        System.out.print("> ");
    }

    @Deprecated
    public void visit(LobbyEvent event) {
        ui.showWaitingForPlayers(event.getTruckers().size());
    }

    @Deprecated
    public void visit(CoordRequest event) {
        ui.askCoordinates();
    }

    @Deprecated
    public void visit(AvailableComponentsEvent event) {
        ui.showAvailableComponent(event.getComponents());
        ui.showSelectComponent();
        System.out.print("> ");
    }

    @Deprecated
    public void visit(TruckerLostCrewEvent event) {
    }

    @Deprecated
    public void visit(GameCreatedEvent event) {

    }

    @Deprecated
    public void visit(FirstConnectionEvent event) {

    }

    @Deprecated
    public void visit(CoordListRequest event) {

    }

    @Deprecated
    public void visit(AvailableComponentToSelectRequest event) {

    }

    @Deprecated
    public void visit(TimeExpiredEvent event){
        ui.showTimeExpired();
    }

    @Deprecated
    public void visit(ShowCardEvent event) {

    }

    @Deprecated
    public void visit(UpdateCardStateEvent event) {
        ui.showMessage(event.getMessage());
        System.out.print("> ");
    }

    @Deprecated
    public void visit(UpdateTrackEvent event) {
        viewFlow.setTrack(event.getTrack());
        System.out.print("> ");
    }

    @Deprecated
    public void visit(ShipUpdateEvent event) {
        viewFlow.setBatteryCount(event.numBatteries());
        viewFlow.setCannonPower(event.cannonPower());
        viewFlow.setShip(event.getShip());
        viewFlow.setEnginePower(event.enginePower());
        viewFlow.setPosition(event.truckerPosition());
        viewFlow.setGoodsByColor(event.getGoods());
        viewFlow.setHumanCount(event.numCrew());
        System.out.print("> ");
    }

    @Deprecated
    public void visit(IndexesRequest event) {
        ui.showGoodsToRemove(event.getGoodsToRemove());
        ui.askGoodsIndexesToRemove();
        System.out.print("> ");
    }

    @Deprecated
    public void visit(GoodsGainedEvent event){
        ui.showGoodsGained(event.getGoodGained());
        ui.askIndex();
        System.out.print("> ");
    }

    @Deprecated
    public void visit(OkEvent event){
        ui.showMessage(event.getMessage());
        System.out.print("> ");
    }

    @Deprecated
    public void visit(IntRequest event) {
        ui.askIndex();
        System.out.print("> ");
    }

    @Deprecated
    public void visit(BuildOptionsEvent event){
        ui.showBuildOptions();
    }

    @Deprecated
    public void visit(ReconnectedUserEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Deprecated
    public void visit(DisconnectedUserEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Deprecated
    public void visit(SetupCompleteEvent event) {
        ui.showSetupComplete();

        viewFlow.setViewState(ViewFlow.ViewState.BULDING);
    }

    @Deprecated
    public void visit(SelectComponentEvent event){
        ui.askPlacedComponent();
        System.out.print("> ");
    }


}
