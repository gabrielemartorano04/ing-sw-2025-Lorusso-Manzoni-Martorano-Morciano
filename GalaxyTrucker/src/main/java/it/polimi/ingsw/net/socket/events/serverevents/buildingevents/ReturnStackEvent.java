package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class ReturnStackEvent extends ServerEvent {
    private Stack stack;
    public ReturnStackEvent(Stack stack) {
        super(ServerEventType.SHOW_STACK,false);
        this.stack = stack;
    }

    public Stack getStack() {
        return stack;
    }

    @Override
    public String getMessage() {
        String message = "Cards in the stack:\n";
        for (Card card : stack.getStack()) {
            message += card.toString() + "\n";
        }
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
