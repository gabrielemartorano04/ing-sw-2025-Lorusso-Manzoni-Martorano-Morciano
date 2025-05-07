package it.polimi.ingsw.net.socket.events.serverevents;

/**
 * Event from server to client when the component is correctly linked to the spaceship
 */
public class ComponentLinkedEvent extends ServerEvent {
    String message;
    String componentName;
    int cordx;
    int cordy;
    public ComponentLinkedEvent(String component, int x, int y) {
        super(ServerEventType.COMPONENT_LINKED, false);
        componentName = component;
        cordx = x;
        cordy = y;
        message = "Component correctly linked";
    }
    @Override
    public String getMessage() {
        return message;
    }
    public String getComponent() {return componentName;}
    public int getX() {return cordx;}
    public int getY() {return cordy;}
}
