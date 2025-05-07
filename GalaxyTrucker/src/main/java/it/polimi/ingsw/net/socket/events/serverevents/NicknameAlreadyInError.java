package it.polimi.ingsw.net.socket.events.serverevents;


/**
 * Event from server to client. A player tried to join the game but the chosen nickname is already used.
 */
public class NicknameAlreadyInError extends ServerEvent {
    String username,message;
    public NicknameAlreadyInError(String username) {
        super(ServerEventType.NICKNAME_ALREADY_IN);
        this.username = username;
        message = username + " is already in use";
    }
    public String getUsername() {
        return username;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
