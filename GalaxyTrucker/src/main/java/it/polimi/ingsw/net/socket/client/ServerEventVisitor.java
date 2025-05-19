package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.AlienCabinCoord;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.*;

public interface ServerEventVisitor {

    void visit(AvailableColorsEvent event);
    void visit(ColorAlreadyInError event);
    void visit(ComponentLinkedEvent event);
    void visit(ComponentNotAvailableError event);
    void visit(ComponentSelectedEvent event);
    void visit(DisconnectedUserEvent event);

    void visit(FlightStartEvent event);

    void visit(IntRequest event);

    void visit(EndGameEvent event);

    void visit(TruckerWithdrawEvent event);

    void visit(GenericErrorInsertedData event);

    void visit(CoordRequest event);

    void visit(InvalidCoordinatesError event);
    void visit(NicknameAlreadyInError event);
    void visit(NoAvailableSpaceError event);
    void visit(PlayerJoinedEvent event);
    void visit(ReconnectedUserEvent event);

    void visit(ShowCardEvent event);

    void visit(UpdateCardStateEvent event);

    void visit(UpdateTrackEvent event);

    void visit(ShipUpdateEvent event);

    void visit(BuildCompleteEvent event);

    void visit(KeptComponentEvent event);

    void visit(BuildOptionsEvent event);

    void visit(AlienCabinCoord event);

    void visit(SelectComponentEvent event);

    void visit(StartBuildingEvent event);
    void visit(TimeExpiredEvent event);
    void visit(TurnHourglassError event);

    void visit(InvalidPositionError event);

    void visit(UpdateBuildingStateEvent event);

    void visit(DiscardSuccessEvent event);

    void visit(LobbyEvent event);

    void visit(IndexesRequest event);

    void visit(GoodsGainedEvent event);

    void visit(OkEvent event);

    void visit(SetupCompleteEvent event);

    void visit(AvailableComponentsEvent event);

    void visit(NotYourTurnError notYourTurnError);

    void visit(NumberOfPlayersError numberOfPlayersError);

    void visit(ReturnStackEvent returnStackEvent);

    void visit(PlacedComponentRequest placedComponentRequest);

    void visit(TruckerLostCrewEvent truckerLostCrewEvent);

    void visit(HourglassTurnEvent event);

    void visit(GameCreatedEvent event);

    void visit(FirstConnectionEvent event);


    void visit(CoordListRequest coordListRequest);

    void visit(AvailableComponentToSelectRequest event);

    void visit(BuildingStateError event);

    void visit(GenericErrorEvent event);

    void visit(SuccessSelectKept event);

    void visit(ShowOtherShipResponse event);

    void visit(WaitOthersBuildingEvent event);

    void visit(WaitOthersStartFlightEvent event);

    void visit(BooleanDecision event);

    void visit(CardFinished event);

    void visit(EpidemicEvent event);

    void visit(HumanCoordinates event);

    void visit(PlanetDecision event);

    void visit(PlotGood event);

    void visit(RollDice event);

    void visit(StarDustEvent event);

    void visit(StartCardEvent event);

}