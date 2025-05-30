//// Classe RMI del client, il server la usa per inviare eventi
//package it.polimi.ingsw.net.rmi.client;
//
//import it.polimi.ingsw.net.rmi.RmiClientInterface;
//import it.polimi.ingsw.net.socket.events.serverevents.*;
//import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.*;
//import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
//import it.polimi.ingsw.net.socket.events.serverevents.flightevents.*;
//import it.polimi.ingsw.net.socket.events.serverevents.setupevents.*;
//import it.polimi.ingsw.view.ViewFlow;
//
//import java.rmi.RemoteException;
//import java.rmi.server.UnicastRemoteObject;
//
//public class RmiClient extends UnicastRemoteObject implements RmiClientInterface {
//    private final ViewFlow view;
//
//    public RmiClient(ViewFlow view) throws RemoteException {
//        super();
//        this.view = view;
//    }
//
//    @Override
//    public void receiveMessage(ServerEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(LobbyEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(PlayerJoinedEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(NicknameAlreadyInError event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(AvailableColorsEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(ColorAlreadyInError event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(ReconnectedUserEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(DisconnectedUserEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(SetupCompleteEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(StartBuildingEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(ComponentLinkedEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(ComponentNotAvailableError event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(ComponentSelectedEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(DiscardSuccessEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(CoordRequest event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(InvalidCoordinatesError event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(InvalidPositionError event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(UpdateBuildingStateEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(TurnHourglassError event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(NotYourTurnError event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(NumberOfPlayersError event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(PlacedComponentRequest event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(TruckerLostCrewEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(HourglassTurnEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(GameCreatedEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(FirstConnectionEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(CoordListRequest event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(AvailableComponentToSelectRequest event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(TimeExpiredEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(BuildCompleteEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(KeptComponentEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(TurnedComponentSuccessEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(CurrentCondition currentCondition) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(BuildOptionsEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(SuccessSelectKept event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(ShowOtherShipResponse event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(WaitOthersBuildingEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(AlienCabinCoord event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(SelectComponentEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(ShipUpdateEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(FlightStartEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(EndGameEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(TruckerWithdrawEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(GenericErrorInsertedData event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(NoAvailableSpaceError event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(ReturnStackEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(GenericErrorEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(WaitOthersStartFlightEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(BooleanDecision event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(CardFinished event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(EpidemicEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(HumanCoordinates event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(PlanetDecision event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(PlotGood event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(RollDiceResult event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(StarDustEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(StartCardEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(RearrangeEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(GainCosmicCredits event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(InvalidCoordinatesEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(NotEnoughBatterieEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(InvalidComponentsExceptionEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(InvalidPositionExceptionEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(PlanetNotFreeExceptionEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(InvalidGoodChosenEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(NotEnoughGoodsChosenEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(WrongNumberOfBatteriesSelectedEvent event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(RemotionConfirmed event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(RemoveBatteries event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(RemoveGoods event) throws RemoteException {
//
//    }
//
//    @Override
//    public void visit(StackDropOk event) throws RemoteException {
//
//    }
//}
