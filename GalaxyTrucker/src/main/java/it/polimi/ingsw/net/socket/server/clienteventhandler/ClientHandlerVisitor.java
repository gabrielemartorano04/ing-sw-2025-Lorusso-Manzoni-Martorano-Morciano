package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.BuildingState;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.*;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;
import it.polimi.ingsw.utilities.exceptions.*;

import java.util.ArrayList;
import java.util.EnumSet;

public class ClientHandlerVisitor implements ClientVisitor {
    private int numOfPlayers;
    private GameController gameController;
    private int numLog;
    private ClientHandlerSocket currentClient;
    private int gameLevel;

    public ClientHandlerVisitor() {
        numLog = 0;
        currentClient = null;
        numOfPlayers = 0;
        gameLevel=0;
    }
    //se login event prendo nickname e color, controllo da model e creo evento risposta

    public void setClientHandlerServer(ClientHandlerSocket clientHandlerServer) {
        currentClient = clientHandlerServer;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
    public int getGameLevel(){return gameLevel;}

    @Override
    public ServerEvent visit(CreateGameEvent event) {
        int numPlayers= event.getNumberOfPlayers();
        gameLevel=event.getGameLevel();
        if (numPlayers<0 || numPlayers>4) {
            return new NumberOfPlayersError();
        }
        this.numOfPlayers = numPlayers;
        this.gameLevel = gameLevel;
        return new AvailableColorsEvent(EnumSet.allOf(PlayerColors.class), "First Player in the game, start login");
    }

    @Override
    public ServerEvent visit(DiscardTileEvent event) {
        Component component = event.getComponent();
        component.setCovered(false);
        component.setSelected(false);
        gameController.getGame().getComponents().getComponents().add(component);
        return new DiscardSuccessEvent();
    }

    @Override
    public ServerEvent visit(EndBuildingEvent event) {
        gameController.getGame().startFlightPhase();
        return new CheckShipsEvent();
    }

    @Override
    public ServerEvent visit(KeepTileEvent event) {
        Component component = event.getComponent();
        try {
            currentClient.getTrucker().keep(component,gameController.getGame());
            return new KeptComponentEvent(component);
        } catch (InvalidComponentsException e) {
            return new NoAvailableSpaceError();
        }
        catch (InvalidStateException e) {
            return new BuildingStateError();
        }
    }

    @Override
    public ServerEvent visit(LinkTileEvent event) {
        Component component = event.getComponent();
        int x=event.getX();
        int y=event.getY();
        Trucker trucker= currentClient.getTrucker();
        trucker.setBuildingState(BuildingState.LINK);
        try {
            trucker.link(component,x,y);
            return new ComponentLinkedEvent(component, x, y);
        } catch (InvalidStateException e) {
            return new BuildingStateError();
        }
    }

    @Override
    public ServerEvent visit(LoginEvent event) {
        String nickname="";
        numLog++;
        if(numLog< gameController.getGame().getNumPlayers()){
            nickname = event.getUsername();
            PlayerColors color=event.getColor();
            try {
                currentClient.setTrucker(gameController.tryAddPlayer(nickname,color));
            } catch (NicknameUsedException e) {
                numLog--;
                return new NicknameAlreadyInError(nickname,gameController.getAvailablePlayerColors());
            } catch (ColorNotAvailable e) {
                numLog--;
                return new ColorAlreadyInError(color,gameController.getAvailablePlayerColors());
            }
            return new PlayerJoinedEvent(nickname);
        }
        if(numLog== gameController.getGame().getNumPlayers()){
            nickname = event.getUsername();
            PlayerColors color=event.getColor();
            try {
                currentClient.setTrucker(gameController.tryAddPlayer(nickname,color));
            } catch (NicknameUsedException e) {
                numLog--;
                return new NicknameAlreadyInError(nickname,gameController.getAvailablePlayerColors());
            } catch (ColorNotAvailable e) {
                numLog--;
                return new ColorAlreadyInError(color,gameController.getAvailablePlayerColors());
            }
            return new StartBuildingEvent(nickname, currentClient.getTrucker().getSpaceShip(), gameController.getGame().viewStacks(), gameController.getGame().getComponents(), gameController.getGame().getHourglass().getSecondiRimanenti());
        }
        return new NoAvailableSpaceError();
    }

    @Override
    public ServerEvent visit(SelectTileEvent event) {
        int index=event.getIndex();
        Component component=gameController.getGame().getComponents().getComponents().get(index);
        gameController.getGame().getComponents().getComponents().get(index).setSelected(true);
        gameController.getGame().getComponents().getComponents().remove(component);
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
        try {
            gameController.getGame().turnHourglass();
        } catch (CannotTurnHourglassException e) {
            return new TurnHourglassError();
        }
        return new HourglassTurnEvent(gameController.getGame().getHourglass().getSecondiRimanenti());
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

    @Override
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public ServerEvent visit(ShowTilesListEvent showTilesListEvent) {
        return null;
    }
}