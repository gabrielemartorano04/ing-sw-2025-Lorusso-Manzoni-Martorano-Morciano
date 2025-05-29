package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class RearrangeEvent extends ClientEvent {

    private int[] coord1, coord2;
    private int value1, value2;
    private boolean stop;
    private String nickname;

    public RearrangeEvent(int[] coord1, int[] coord2, int value1, int value2, boolean stop,String nickname) {

        this.coord1 = coord1;
        this.coord2 = coord2;
        this.value1 = value1;
        this.value2 = value2;
        this.stop = stop;
        this.nickname = nickname;
    }

    public int[] getCoord1() {
        return coord1;
    }

    public int[] getCoord2() {
        return coord2;
    }

    public int getValue1() {
        return value1;
    }

    public int getValue2() {
        return value2;
    }

    public boolean getStop() {
        return stop;
    }

    @Override
    public ServerEvent call(VirtualServer visitor) {
        return visitor.visit(this);
    }

    public String getNickname() {
        return nickname;
    }
}
