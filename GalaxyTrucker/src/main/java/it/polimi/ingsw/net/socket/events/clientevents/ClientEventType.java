package it.polimi.ingsw.net.socket.events.clientevents;

/**
 * enum used to indicate the type of event we are considering
 */
public enum ClientEventType {
    LOGIN,
    CREATE_GAME,
    START_BUILDING,
    TURN_HOURGLASS,
    SELECT_TILE,
    SELECT_KEPT_TILE,
    LINK_TILE,
    END_BUILDING,
    SEND_INFO_EVENT,
    SHOW_STACK,
    KEEP_TILE,
    DISCARD_TILE,
    SHOW_TILES,
    SHOW_OTHER_SHIP,
    TURN_COMPONENT, DROP_STACK, REQUEST
}
