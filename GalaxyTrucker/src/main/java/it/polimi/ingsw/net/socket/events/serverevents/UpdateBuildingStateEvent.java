package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.BuildingState;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event server to client
 */
public class UpdateBuildingStateEvent extends ServerEvent {
    private BuildingState state;

    public UpdateBuildingStateEvent(BuildingState state) {
        super(ServerEventType.UPDATE_STATE);
        this.state = state;
    }
    public BuildingState getState() {
        return state;
    }
    public String getMessage() {
        return "Current building state is: " + state.toString();
    }
}
