package it.polimi.ingsw.net.socket.events.serverevents;

public class TruckerWithdrawEvent extends ServerEvent{
    private String message, nickname;

    public TruckerWithdrawEvent(String nickname) {
        super(ServerEventType.WITHDRAW, true);
        message = nickname + " has withdrawn from the game";
        this.nickname = nickname;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getTruckerWithdrawNick() {
        return nickname;
    }
}
