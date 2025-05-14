package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.BuildingState;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Cannon;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Engine;
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
        gameController.getGame().getComponents().addComponent(component);
        currentClient.getTrucker().setBuildingState(BuildingState.BUILDING);
        int i=0;
        for(Component c: gameController.getGame().getComponents().getComponents()){
            System.out.println(i+") tipo: "+c.getType()+" covered:"+ c.getCovered()+ " selected: "+c.getSelected()+"\n");
            i++;
        }
        return new DiscardSuccessEvent(gameController.getGame().getComponents().getComponents());
    }

    @Override
    public ServerEvent visit(EndBuildingEvent event) {
        gameController.getGame().startFlightPhase();
        return new CheckShipsEvent();
    }

    @Override
    public ServerEvent visit(KeepTileEvent event) {
        Component component = event.getComponent();
        currentClient.getTrucker().setBuildingState(BuildingState.KEEP);
        try {
            currentClient.getTrucker().keep(component,gameController.getGame());
            int i = 0;
            for(Component c: gameController.getGame().getComponents().getComponents()){
                System.out.println(i+") tipo: "+c.getType()+" covered:"+ c.getCovered()+ " selected: "+c.getSelected()+"\n");
                i++;
            }
            return new KeptComponentEvent(component,gameController.getGame().getComponents().getComponents());
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
            int i=0;
            for(Component c: gameController.getGame().getComponents().getComponents()){
                System.out.println(i+") tipo: "+c.getType()+" covered:"+ c.getCovered()+ " selected: "+c.getSelected()+"\n");
                i++;
            }
            return new ComponentLinkedEvent(component, x, y, gameController.getGame().getComponents().getComponents());
        } catch (InvalidStateException e) {
            return new BuildingStateError();
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesError(trucker.getNickname());
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
            return new StartBuildingEvent(nickname, currentClient.getTrucker().getSpaceShip(), gameController.getGame().viewStacks(), gameController.getGame().getComponents().getComponents(), gameController.getGame().getHourglass().getSecondiRimanenti());
        }
        return new NoAvailableSpaceError();
    }

    @Override
    public ServerEvent visit(SelectTileEvent event) {
        int index=event.getIndex();
        System.out.println("\n" + index + "\n");
        if(index < 0 || index > gameController.getGame().getComponents().getComponents().size() - 1){
            return new ComponentNotAvailableError(gameController.getGame().getComponents().getComponents());
        }
        Trucker trucker=currentClient.getTrucker();
        trucker.setBuildingState(BuildingState.SELECT_COMPONENT);
        Component component=null;
        try {
            component = trucker.selectComponent(index,gameController.getGame().getComponents());
            component.setSelected(true);
        } catch (InvalidComponentsException e) {
            return new ComponentNotAvailableError(gameController.getGame().getComponents().getComponents());
        } catch (InvalidStateException e) {
            return new BuildingStateError();
        }
        int i=0;
        for(Component c: gameController.getGame().getComponents().getComponents()){
            System.out.println(i+") tipo: "+c.getType()+" covered:"+ c.getCovered()+ " selected: "+c.getSelected()+"\n");
            i++;
            String info;

            if ("engine".equalsIgnoreCase(c.getType())) {
                Engine engine = (Engine) c;
                info = String.format("ENGINE [N:%s E:%s S:%s W:%s] (Double: %s)\n",
                        engine.getLink()[0], engine.getLink()[1], engine.getLink()[2], engine.getLink()[3],
                        engine.getIsDouble());
            } else if ("cannon".equalsIgnoreCase(c.getType())) {
                Cannon cannon = (Cannon) c;
                info = String.format("CANNON [N:%s E:%s S:%s W:%s] (Double: %s)\n",
                        cannon.getLink()[0], cannon.getLink()[1], cannon.getLink()[2], cannon.getLink()[3],
                        cannon.getIsDouble());
            } else {
                info = String.format("%s [N:%s E:%s S:%s W:%s]\n",
                        c.getType(), c.getLink()[0], c.getLink()[1], c.getLink()[2], c.getLink()[3]);
            }

            System.out.println(info);
        }
        return new ComponentSelectedEvent(component);
    }

    @Override
    public ServerEvent visit(ShowStackEvent event) {
        int index=event.getIndex();
        Trucker trucker=currentClient.getTrucker();
        trucker.setBuildingState(BuildingState.SHOW_STACK);
        ArrayList<Card> stack=trucker.showStack(index,gameController.getGame());
        return new ReturnStackEvent(stack);
    }

    @Override
    public ServerEvent visit(TurnHourglassEvent event) {
        try {
            currentClient.getTrucker().setBuildingState(BuildingState.TURN_HOURGLASS);
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