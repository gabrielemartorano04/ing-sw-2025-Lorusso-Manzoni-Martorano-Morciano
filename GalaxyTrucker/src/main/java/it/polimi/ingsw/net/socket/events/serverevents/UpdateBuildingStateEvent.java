package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.BuildingState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

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

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
