package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.model.BuildingState;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.clientevents.ShowOtherShipEvent;
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
        // The component is already set as uncovered by the select handle, because we can't have a discard before a select
        gameController.getGame().getComponents().setComponentAsUnselected(event.getIndex());
        return new DiscardSuccessEvent(event.getIndex(), currentClient.getTruckerController().getNickname());
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
            currentClient.getTruckerController().keep(component);
//            int i = 0;
//            for(Component c: gameController.getGame().getComponents().getComponents()){
//                System.out.println(i+") tipo: "+c.getType()+" covered:"+ c.getCovered()+ " selected: "+c.getSelected()+"\n");
//                i++;
//            }
            return new KeptComponentEvent(component,gameController.getGame().getComponents().getComponents());
        } catch (InvalidComponentsException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    @Override
    public ServerEvent visit(LinkTileEvent event) {

        int index = event.getIndex();
        int x=event.getX();
        int y=event.getY();
        if(index != currentClient.getTruckerController().getSelectedComponentIndex()){
            System.out.println(currentClient.getTruckerController().getSelectedComponentIndex() + " " + event.getIndex());
            //TODO: TOGLI DOPO DEBUG
            try {
                // Simula un punto di rottura
                System.out.println("DEBUG: Errore rilevato, L'INDICE DEL COMPONENTE SELEZIONATO NON CORRISPONDE CON QUELLO DEL TRUCKER,blocco in corso...");
                Thread.sleep(Long.MAX_VALUE); // blocca il thread all'infinito
            } catch (InterruptedException e) {
                // Eventuale gestione per interrompere il blocco (facoltativa)
            }
        }
        try {
            currentClient.getTruckerController().link(event.getComponent(),x,y);
//            int i=0;
//            for(Component c1: gameController.getGame().getComponents().getComponents()){
//                System.out.println(i+") tipo: "+c1.getType()+" covered:"+ c1.getCovered()+ " selected: "+c1.getSelected()+"\n");
//                i++;
//            }
            return new ComponentLinkedEvent(event.getComponent(), x, y, gameController.getGame().getComponents().getComponents());
        }  catch (InvalidCoordinates e) {
            return new InvalidCoordinatesError(currentClient.getTruckerController().getNickname());
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
                currentClient.setTruckerController(gameController.tryAddPlayer(nickname,color));
            } catch (NicknameUsedException e) {
                numLog--;
                return new NicknameAlreadyInError(nickname,gameController.getAvailablePlayerColors());
            } catch (ColorNotAvailable e) {
                numLog--;
                return new ColorAlreadyInError(color,gameController.getAvailablePlayerColors());
            }

            // TODO: verificare che il nickname venga salvato bene dalla viewFlow con entrambi i tipi di eventi ritornati
            return new PlayerJoinedEvent(nickname);
        }
        if(numLog== gameController.getGame().getNumPlayers()){
            nickname = event.getUsername();
            PlayerColors color=event.getColor();
            try {
                currentClient.setTruckerController(gameController.tryAddPlayer(nickname,color));
            } catch (NicknameUsedException e) {
                numLog--;
                return new NicknameAlreadyInError(nickname,gameController.getAvailablePlayerColors());
            } catch (ColorNotAvailable e) {
                numLog--;
                return new ColorAlreadyInError(color,gameController.getAvailablePlayerColors());
            }
            return new StartBuildingEvent(nickname, currentClient.getTruckerController().getSpaceShip(), gameController.getGame().viewStacks(), gameController.getGame().getComponents().getComponents(), gameController.getGame().getHourglass().getSecondiRimanenti());
        }
        return new NoAvailableSpaceError();
    }

    @Override
    public ServerEvent visit(SelectTileEvent event) {
        int index=event.getIndex();
        System.out.println("\n" + index + "\n");
        if(index < 0 || index > gameController.getGame().getComponents().getComponents().size() - 1){
            return new ComponentNotAvailableError();
        }
        try {
            currentClient.getTruckerController().selectComponent(index,gameController.getGame().getComponents());
            gameController.getGame().getComponents().setComponentAsSelected(index);
            // probabilmente evitabile considerando che il fatto di essere covered o meno basta salvarlo lato view
            // ma meglio tenere view e server completamente sincronizzati per ora
            gameController.getGame().getComponents().setComponentAsUncovered(index);
            return new ComponentSelectedEvent(index, currentClient.getTruckerController().getNickname());

        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }


    @Override
    public ServerEvent visit(SelectKeptTileEvent event){
        int index=event.getIndex();
        try {
            System.out.println("\n" + "Entrato nel try di KeptTileEvent" + "\n");
            currentClient.getTruckerController().getKeptComponent(index);
            return new SuccessSelectKept(currentClient.getTruckerController().getKeptComponent(index));
        } catch (GeneralError e) {
            System.out.println("\n" + "Entrato nel catch di KeptTileEvent" + "\n");
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    @Override
    public ServerEvent visit(ShowStackEvent event) {
        int index=event.getIndex();
        TruckerController trucker=currentClient.getTruckerController();
        ArrayList<Card> stack=trucker.showStack(index,gameController.getGame());
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

    @Override
    public ServerEvent visit(ShowOtherShipEvent showOtherShipEvent) {
        String nickname=showOtherShipEvent.getNickname();
        Component[][] spaceship=null;
        for(Trucker t:gameController.getGame().getTruckers()){
            if(t.getNickname().equals(nickname)){
                spaceship=t.getSpaceShip().getComponents();
                break;
            }
        }
        return new ShowOtherShipResponse(spaceship,nickname);
    }
}