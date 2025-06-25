package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents an event that involves returning a stack of cards
 */
public class ReturnStackEvent extends ServerEvent {

    /**
     * Stack of cards
     */
    private final Stack stack;

    /**
     * Event constructor
     * @param stack of cards
     */
    public ReturnStackEvent(Stack stack) {
        super(false);
        this.stack = stack;
    }

    /**
     * @return the stack of cards
     */
    public Stack getStack() {
        return stack;
    }

    /**
     * @return the message associated with the show of the stack
     */
    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder("Cards in the stack:\n");
        for (Card card : stack.getStack()) {
            message.append(card.toString()).append("\n");
        }
        return message.toString();
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
