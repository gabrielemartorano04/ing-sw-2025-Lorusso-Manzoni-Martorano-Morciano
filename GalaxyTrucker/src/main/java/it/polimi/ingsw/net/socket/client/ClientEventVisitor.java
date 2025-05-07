package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.socket.events.serverevents.*;

public interface ClientEventVisitor {
    void visit(AvailableColorsEvent event);
    void visit(BooleanRequest event);
    void visit(CheckShipsEvent event);
    void visit(ColorAlreadyInError event);
    void visit(ComponentLinkedEvent event);
    void visit(ComponentNotAvailableError event);
    void visit(ComponentSelectedEvent event);
    void visit(DisconnectedUserEvent event);
    void visit(EndGameEvent event);
    void visit(GenericErrorInsertedData event);
    void visit(InvalidCoordinatesError event);
    void visit(InvalidPositionError event);
    void visit(NicknameAlreadyInError event);
    void visit(NoAvailableSpaceError event);
    void visit(PlayerJoinedEvent event);
    void visit(ReconnectedUserEvent event);
    void visit(ShipStateUpdateEvent event);
    void visit(ShipUpdateEvent event);
    void visit(ShowCardEvent event);
    void visit(StartBuildingEvent event);
    void visit(TimeExpiredEvent event);
    void visit(TurnHourglassError event);
    void visit(UpdateBuildingStateEvent event);
    void visit(UpdateCardStateEvent event);
}