package it.polimi.ingsw.model.servereventhandler;

import it.polimi.ingsw.controller.CardControllerNew;
import it.polimi.ingsw.net.socket.events.clientevents.*;

public class EventHandlerVisitor implements EventVisitor {
    private final CardControllerNew cardController;

    public EventHandlerVisitor(CardControllerNew cardController) {
        this.cardController = cardController;
    }

    @Override
    public void visit(SendCoordEvent event) {

    }

    @Override
    public void visit(SendBooleanEvent event) {

    }

    @Override
    public void visit(SendCoordListEvent event) {

    }

    @Override
    public void visit(SendIndexesEvent event) {

    }

    @Override
    public void visit(SendIntEvent event) {

    }
}
