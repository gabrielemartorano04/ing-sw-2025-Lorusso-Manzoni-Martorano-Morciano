package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.socket.events.serverevents.*;

public interface ServerEventVisitor {

    void visit(AvailableColorsEvent event);
    void visit(BooleanRequest event);
    void visit(CheckShipsEvent event);
    void visit(ColorAlreadyInError event);
    void visit(ComponentLinkedEvent event);
    void visit(ComponentNotAvailableError event);
    void visit(ComponentSelectedEvent event);
    void visit(DisconnectedUserEvent event);
    void visit(EndGameEvent event);

    void visit(TruckerWithdrawEvent event);

    void visit(GenericErrorInsertedData event);
    void visit(InvalidCoordinatesError event);
    void visit(InvalidPositionError event);
    void visit(NicknameAlreadyInError event);
    void visit(NoAvailableSpaceError event);
    void visit(PlayerJoinedEvent event);
    void visit(ReconnectedUserEvent event);
    void visit(ShipUpdateEvent event);

    void visit(BuildCompleteEvent event);

    void visit(KeptComponentEvent event);

    void visit(BuildOptionsEvent event);

    // === PHASE 4: Fase di Volo ===
    void visit(SelectComponentEvent event);

    void visit(ShowCardEvent event);
    void visit(StartBuildingEvent event);
    void visit(TimeExpiredEvent event);
    void visit(TurnHourglassError event);
    void visit(UpdateBuildingStateEvent event);
    void visit(UpdateCardStateEvent event);

    void visit(DiscardSuccessEvent event);

    void visit(CoordRequest event);

    void visit(LobbyEvent event);
    void visit(IndexesRequest event);

    void visit(GoodsGainedEvent event);

    void visit(OkEvent event);

    void visit(FlightStartEvent event);

    void visit(IntRequest event);
    void visit(UpdateTrackEvent event);
    void visit(SetupCompleteEvent event);
    void visit(AvailableComponentsEvent event);

    void visit(ReturnStackEvent returnStackEvent);

    void visit(PlacedComponentRequest placedComponentRequest);
}