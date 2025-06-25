package it.polimi.ingsw.view;

import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public interface EventReceiver {
    void onEventReceived(ServerEvent event);
}
