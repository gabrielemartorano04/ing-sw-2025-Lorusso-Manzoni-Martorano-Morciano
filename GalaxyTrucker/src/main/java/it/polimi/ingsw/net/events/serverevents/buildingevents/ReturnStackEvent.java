package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class ReturnStackEvent extends ServerEvent {
    private Stack stack;
    public ReturnStackEvent(Stack stack) {
        super(false);
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
