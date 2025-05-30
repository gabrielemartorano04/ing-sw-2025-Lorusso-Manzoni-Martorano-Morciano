package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

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

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
