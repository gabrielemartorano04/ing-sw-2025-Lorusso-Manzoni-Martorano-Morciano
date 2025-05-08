package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

import java.util.ArrayList;

public class ReturnStackEvent extends ServerEvent {
    private ArrayList<Card> stack;
    public ReturnStackEvent(ArrayList<Card> stack) {
        super(ServerEventType.SHOW_STACK,false);
        this.stack = stack;
    }
    public ArrayList<Card> getStack() {
        return stack;
    }
    @Override
    public String getMessage() {
        String message = "Cards in the stack:\n";
        for (Card card : stack) {
            message += card.toString() + "\n";
        }
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
