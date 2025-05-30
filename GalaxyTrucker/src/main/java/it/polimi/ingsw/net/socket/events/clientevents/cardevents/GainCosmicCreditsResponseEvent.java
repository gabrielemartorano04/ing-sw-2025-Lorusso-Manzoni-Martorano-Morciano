package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class GainCosmicCreditsResponseEvent extends ClientEvent {
    private boolean answer;
    public GainCosmicCreditsResponseEvent(boolean answer) {
        this.answer = answer;
    }

    public boolean getAnswer() {
        return answer;
    }


    @Override
    public void call(VirtualServer visitor) {
        return visitor.visit(this);
    }

