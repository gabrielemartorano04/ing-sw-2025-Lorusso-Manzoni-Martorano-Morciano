package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.clientevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.utilities.exceptions.ColorNotAvailable;
import it.polimi.ingsw.utilities.exceptions.NicknameUsedException;

import java.util.ArrayList;

public class ClientHandlerVisitor implements ClientVisitor {
    private GameController gameController;
    private int numLog;
    public ClientHandlerVisitor() {
        numLog = 0;
    }
    //se login event prendo nickname e color, controllo da model e creo evento risposta


    @Override
    public ServerEvent visit(CreateGameEvent event) {
        int numPlayers= event.getNumberOfPlayers();
        if (numPlayers<2 || numPlayers>4) {
            return new NumberOfPlayersError();
        }
        gameController=new GameController(numPlayers);
        return new OkEvent("Game Created correctly");
    }

    @Override
    public ServerEvent visit(DiscardTileEvent event) {

        return null;
    }

    @Override
    public ServerEvent visit(EndBuildingEvent event) {

        return null;
    }

    @Override
    public ServerEvent visit(KeepTileEvent event) {

        return null;
    }

    @Override
    public ServerEvent visit(LinkTileEvent event) {

        return null;
    }

    @Override
    public ServerEvent visit(LoginEvent event) {
        if(numLog< gameController.getGame().getNumPlayers()){
            String nickname = event.getUsername();
            PlayerColors color=event.getColor();
            try {
                gameController.tryAddPlayer(nickname,color);
            } catch (NicknameUsedException e) {
                return new NicknameAlreadyInError(nickname,gameController.getAvailablePlayerColors());
            } catch (ColorNotAvailable e) {
                return new ColorAlreadyInError(color,gameController.getAvailablePlayerColors());
            }
            return new PlayerJoinedEvent(nickname);
        }
        return new NoAvailableSpaceError();
    }

    @Override
    public ServerEvent visit(SelectTileEvent event) {
        int index=event.getIndex();
        Component component=gameController.getGame().getComponents().getComponents().get(index);
        gameController.getGame().getComponents().remove(index);
        return new ComponentSelectedEvent(component);
    }

    @Override
    public ServerEvent visit(ShowStackEvent event) {
        int index=event.getIndex();
        ArrayList<Card> stack= gameController.showStack(index);
        return new ReturnStackEvent(stack);
    }

    @Override
    public ServerEvent visit(TurnHourglassEvent event) {

        return null;
    }

    @Override
    public ServerEvent visit(SendCoordEvent event) {

        return null;
    }

    @Override
    public ServerEvent visit(SendBooleanEvent event) {

        return null;
    }

    @Override
    public ServerEvent visit(SendCoordListEvent event) {

        return null;
    }

    @Override
    public ServerEvent visit(SendIndexesEvent event) {

        return null;
    }

    @Override
    public ServerEvent visit(SendIntEvent event) {

        return null;
    }
}