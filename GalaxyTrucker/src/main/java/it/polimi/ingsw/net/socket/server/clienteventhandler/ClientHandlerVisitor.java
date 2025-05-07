package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.socket.events.clientevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.utilities.exceptions.ColorNotAvailable;
import it.polimi.ingsw.utilities.exceptions.NicknameUsedException;

public class ClientHandlerVisitor implements ClientVisitor {
    private GameController gameController;

    public ClientHandlerVisitor() {

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
    public ServerEvent visit(LoginEvent event) {
        String nickname = event.getUsername();
        PlayerColors color=event.getColor();
        try {
            gameController.tryAddPlayer(nickname,color);
        } catch (NicknameUsedException e) {
            return new NicknameAlreadyInError(nickname);
        } catch (ColorNotAvailable e) {
            return new ColorAlreadyInError(color,gameController.getAvailablePlayerColors());
        }
        return new PlayerJoinedEvent(nickname);
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