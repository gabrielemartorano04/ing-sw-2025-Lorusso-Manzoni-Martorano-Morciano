package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.DiscardSuccessEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class DiscardTileEvent extends ClientEvent {
    private Component component;
    public DiscardTileEvent(Component component) {
        super(ClientEventType.DISCARD_TILE, false);
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    @Override
    public String getMessage() {
        return "I want to discard the selected tile";
    }

    @Override
    public ServerEvent call(ClientVisitor visitor){
        ServerEvent e = visitor.visit(this);
        int i=0;
        DiscardSuccessEvent e1 = (DiscardSuccessEvent) e;
        System.out.println("Dentro call di DiscardTileEvent");
        for(Component c: e1.getAvComponents().getComponents()){
            System.out.println(i+") tipo: "+c.getType()+" covered:"+ c.getCovered()+ " selected: "+c.getSelected()+"\n");
            i++;
        }
        return visitor.visit(this);
    }
}
