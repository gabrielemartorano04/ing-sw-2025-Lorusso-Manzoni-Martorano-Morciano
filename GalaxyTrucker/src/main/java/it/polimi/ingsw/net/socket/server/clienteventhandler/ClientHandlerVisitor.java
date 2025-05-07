package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.controller.CardControllerNew;
import it.polimi.ingsw.net.socket.events.clientevents.*;

public class ClientHandlerVisitor implements ClientVisitor {
    private final CardControllerNew cardController;

    public ClientHandlerVisitor(CardControllerNew cardController) {
        this.cardController = cardController;
    }
    //se login event prendo nickname e color, controllo da model e creo evento risposta


    @Override
    public void visit(CreateGameEvent event) {

    }

    @Override
    public void visit(DiscardTileEvent event) {

    }

    @Override
    public void visit(EndBuildingEvent event) {

    }

    @Override
    public void visit(KeepTileEvent event) {

    }

    @Override
    public void visit(LinkTileEvent event) {

    }

    @Override
    public void visit(LoginEvent event) {

    }

    @Override
    public void visit(SelectTileEvent event) {

    }

    @Override
    public void visit(ShowStackEvent event) {

    }

    @Override
    public void visit(TurnHourglassEvent event) {

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