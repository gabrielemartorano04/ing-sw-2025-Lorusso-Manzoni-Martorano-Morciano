package it.polimi.ingsw.net;

import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.*;

import java.rmi.RemoteException;

public interface VirtualView {

    void visit(LobbyEvent event) throws RemoteException;

    void visit(PlayerJoinedEvent event) throws RemoteException;

    void visit(NicknameAlreadyInError event) throws RemoteException;

    void visit(AvailableColorsEvent event) throws RemoteException;

    void visit(ColorAlreadyInError event) throws RemoteException;

    void visit(ReconnectedUserEvent event) throws RemoteException;

    void visit(DisconnectedUserEvent event) throws RemoteException;

    void visit(SetupCompleteEvent event) throws RemoteException;

    void visit(StartBuildingEvent event) throws RemoteException;

    void visit(ComponentLinkedEvent event) throws RemoteException;

    void visit(ComponentNotAvailableError event) throws RemoteException;

    void visit(ComponentSelectedEvent event) throws RemoteException;

    void visit(DiscardSuccessEvent event) throws RemoteException;

    void visit(CoordRequest event) throws RemoteException; //TODO: lo usiamo mai?

    void visit(InvalidCoordinatesError event) throws RemoteException;

    void visit(InvalidPositionError event) throws RemoteException;

    void visit(UpdateBuildingStateEvent event) throws RemoteException;

    void visit(TurnHourglassError event) throws RemoteException;

    void visit(AvailableComponentsEvent event);

    void visit(NotYourTurnError event) throws RemoteException;

    void visit(NumberOfPlayersError event) throws RemoteException;

    void visit(PlacedComponentRequest event) throws RemoteException;

    void visit(TruckerLostCrewEvent event) throws RemoteException;

    void visit(HourglassTurnEvent event) throws RemoteException;

    void visit(GameCreatedEvent event) throws RemoteException;

    void visit(FirstConnectionEvent event) throws RemoteException;

    void visit(CoordListRequest event) throws RemoteException;

    void visit(AvailableComponentToSelectRequest event) throws RemoteException;

    void visit(TimeExpiredEvent event) throws RemoteException;

    void visit(BuildCompleteEvent event) throws RemoteException;

    void visit(KeptComponentEvent event) throws RemoteException;

    void visit(TurnedComponentSuccessEvent event) throws RemoteException;

    void visit(CurrentCondition currentCondition) throws RemoteException;

    void visit(BuildOptionsEvent event) throws RemoteException;

    void visit(SuccessSelectKept event) throws RemoteException;

    void visit(ShowOtherShipResponse event) throws RemoteException;

    void visit(WaitOthersBuildingEvent event) throws RemoteException;

    void visit(AlienCabinCoord event) throws RemoteException;

    void visit(SelectComponentEvent event) throws RemoteException;

    void visit(ShowCardEvent event);

    void visit(UpdateCardStateEvent event);

    void visit(UpdateTrackEvent event);

    void visit(ShipUpdateEvent event) throws RemoteException;

    void visit(IndexesRequest event);

    void visit(GoodsGainedEvent event);

    void visit(OkEvent event);

    void visit(FlightStartEvent event) throws RemoteException;

    void visit(IntRequest event);

    void visit(EndGameEvent event) throws RemoteException;

    void visit(TruckerWithdrawEvent event) throws RemoteException;

    void visit(GenericErrorInsertedData event) throws RemoteException;

    void visit(NoAvailableSpaceError event) throws RemoteException;

    void visit(ReturnStackEvent event) throws RemoteException;

    void visit(BuildingStateError event);

    void visit(GenericErrorEvent event) throws RemoteException;

    void visit(WaitOthersStartFlightEvent event) throws RemoteException;

    void visit(BooleanDecision event) throws RemoteException;

    void visit(CardFinished event) throws RemoteException;

    void visit(EpidemicEvent event) throws RemoteException;

    void visit(HumanCoordinates event) throws RemoteException;

    void visit(PlanetDecision event) throws RemoteException;

    void visit(PlotGood event) throws RemoteException;

    void visit(RollDiceResult event) throws RemoteException;

    void visit(StarDustEvent event) throws RemoteException;

    void visit(StartCardEvent event) throws RemoteException;

    void visit(RearrangeEvent event) throws RemoteException;

    void visit(GainCosmicCredits event) throws RemoteException;

    void visit(InvalidCoordinatesEvent event) throws RemoteException;

    void visit(NotEnoughBatterieEvent event) throws RemoteException;

    void visit(InvalidComponentsExceptionEvent event) throws RemoteException;

    void visit(InvalidPositionExceptionEvent event) throws RemoteException;

    void visit(PlanetNotFreeExceptionEvent event) throws RemoteException;

    void visit(InvalidGoodChosenEvent event) throws RemoteException;

    void visit(NotEnoughGoodsChosenEvent event) throws RemoteException;

    void visit(WrongNumberOfBatteriesSelectedEvent event) throws RemoteException;

    void visit(RemotionConfirmed event) throws RemoteException;

    void visit(RemoveBatteries event) throws RemoteException;

    void visit(RemoveGoods event) throws RemoteException;

    public void visit(StackDropOk event) throws RemoteException;

}
