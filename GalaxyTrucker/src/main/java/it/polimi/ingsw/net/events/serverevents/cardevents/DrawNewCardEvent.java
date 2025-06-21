package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

public class DrawNewCardEvent extends ServerEvent {
    private String leaderNickname;
    public DrawNewCardEvent(String leaderNickname) {
        super(ServerEventType.NEW_CARD,true);
        this.leaderNickname= leaderNickname;
    }

    public String getLeaderNickname() {
        return leaderNickname;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String showInfoForOthers() {
        return leaderNickname+" is about to draw the next card\n";
    }
}
