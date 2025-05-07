package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.BuildingState;

/**
 * Event server to client
 */
public class UpdateBuildingStateEvent extends ServerEvent {
    private BuildingState state;

    public UpdateBuildingStateEvent(BuildingState state) {
        super(ServerEventType.UPDATE_STATE, false);
        this.state = state;
    }
    public BuildingState getState() {
        return state;
    }
    @Override
    public String getMessage() {
        return "Current building state is: " + state.toString();
    }
}
