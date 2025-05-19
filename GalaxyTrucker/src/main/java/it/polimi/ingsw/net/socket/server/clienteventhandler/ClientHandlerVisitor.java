package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.clientevents.AlienCoordinatesRequest;
import it.polimi.ingsw.net.socket.events.clientevents.GeneriErrorFromClient;
import it.polimi.ingsw.net.socket.events.clientevents.SendAlienCoord;
import it.polimi.ingsw.net.socket.events.clientevents.ShowOtherShipEvent;
import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.AlienCabinCoord;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.FlightStartEvent;
import it.polimi.ingsw.net.socket.events.serverevents.WaitOthersStartFlightEvent;
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
    private int endBuilding;
    private int startFlight;
    private CardController cardController;
    public ClientHandlerVisitor() {
        numLog = 0;
        endBuilding = 0;
        startFlight = 0;
        currentClient = null;
        numOfPlayers = 0;
        gameLevel=0;
        cardController = new CardController();

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
        System.out.println(currentClient.getTruckerController().getNickname());
        return new DiscardSuccessEvent(event.getIndex(), currentClient.getTruckerController().getNickname());
    }

    @Override
    public ServerEvent visit(EndBuildingEvent event) {
        endBuilding++;
        //inserire condizione hourglass
        if(endBuilding<gameController.getGame().getNumPlayers()) {
            switch(endBuilding) {
                case 1: currentClient.getTruckerController().setPosition(6);
                case 2: currentClient.getTruckerController().setPosition(3);
                case 3: currentClient.getTruckerController().setPosition(1);
                case 4: currentClient.getTruckerController().setPosition(0);
            }
            System.out.println("You're not the last one to end");
            return new WaitOthersBuildingEvent();
        }
        else {
            BuildTimerManager.getInstance().stop();
            for(Trucker t: gameController.getGame().getTruckerList()){
                t.getSpaceShip().wellDone();
            }
            return new BuildCompleteEvent();
        }
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
        try {
            currentClient.getTruckerController().link(event.getComponent(),x,y);
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
            ArrayList<String> players=new ArrayList<>();
            for(Trucker t:gameController.getGame().getTruckerList()){
                players.add(t.getNickname());
            }
            gameController.getGame().startHourglass();
            return new StartBuildingEvent(nickname, currentClient.getTruckerController().getSpaceShip(), gameController.getGame().viewStacks(), gameController.getGame().getComponents().getComponents(), gameController.getGame().getHourglass().getSecondiRimanenti(),players);
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
            System.out.println("\n" + "Entered inside try of KeptTileEvent" + "\n");
            Component component=currentClient.getTruckerController().getKeptComponent(index);
            currentClient.getTruckerController().getTrucker().removeKeptComponent(index);
            return new SuccessSelectKept(component);
        } catch (GeneralError e) {
            System.out.println("\n" + "Entered inside catch of KeptTileEvent" + "\n");
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
        ServerEvent serverEvent=null;
        try {
            serverEvent =gameController.getGame().turnHourglass();
        } catch (CannotTurnHourglassException e) {
            return new TurnHourglassError();
        }
        if (serverEvent != null) {
            ServerController.getIstance().startThreadHourglass();
            return serverEvent;
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
    public ServerEvent visit(ShowTilesListEvent event) {
        return null;
    }

    @Override
    public ServerEvent visit(ShowOtherShipEvent event) {
        String nickname=event.getNickname();
        Component[][] spaceship=null;
        for(Trucker t:gameController.getGame().getTruckerList()){
            if(t.getNickname().equals(nickname)){
                spaceship=t.getSpaceShip().getComponents();
                break;
            }
        }

        return new ShowOtherShipResponse(spaceship,nickname);
    }

    @Override
    public ServerEvent visit(GeneriErrorFromClient event){
        return new GenericErrorEvent("error \n>",false);
    }

    @Override
    public ServerEvent visit(AlienCoordinatesRequest event) {
        ArrayList<int[]> alienCoord=currentClient.getTruckerController().getSpaceShip().getAlienCabin();
        return new AlienCabinCoord(alienCoord);
    }

    @Override
    public ServerEvent visit(SendAlienCoord event) {
        currentClient.getTruckerController().getSpaceShip().setNumAlien(event.getCoord());
        currentClient.getTruckerController().getSpaceShip().setNumHuman();
        currentClient.getTruckerController().getSpaceShip().setBattery();
        currentClient.getTruckerController().getSpaceShip().getGoodsCapacity();
        int cannonPower=currentClient.getTruckerController().getSpaceShip().getSingleCannonPower();
        int enginePower=currentClient.getTruckerController().getSpaceShip().getSingleEnginePower();
        currentClient.getTruckerController().getSpaceShip().increaseLost(currentClient.getTruckerController().getTrucker().getKeptComponents().size());
        currentClient.getTruckerController().getSpaceShip().countExposedConnectors();
        currentClient.getTruckerController().getSpaceShip().getProtection();
        int position=currentClient.getTruckerController().getPosition();
        int batteryCount=currentClient.getTruckerController().getSpaceShip().getNumBatteries();
        int humanCount=currentClient.getTruckerController().getSpaceShip().getNumHuman();

        return new FlightStartEvent(position,batteryCount,humanCount,cannonPower,enginePower);
    }

    @Override
    public ServerEvent visit(OkStartFlight event) {
        startFlight++;
        if(startFlight<gameController.getGame().getNumPlayers()){
            return new WaitOthersStartFlightEvent();
        }
        cardController.setCurrCard(gameController.pickCardFromDeck());

        return cardController.startCard();
    }

    @Override
    public ServerEvent visit(SendHumanCoordEvent event) {
        return null;
    }

    @Override
    public ServerEvent visit(SendPlotGoodEvent event) {
        return null;
    }

    @Override
    public ServerEvent visit(SendPlanetDecisionEvent event) {
        return null;
    }

    @Override
    public ServerEvent visit(SendRollDiceEvent event) {
        return null;
    }

    @Override
    public ServerEvent visit(SendActivateShieldEvent event) {
        return null;
    }

    @Override
    public ServerEvent visit(SendActivateCannonsEvent event) {
        return null;
    }

    @Override
    public ServerEvent visit(SendActivateEnginesEvent event) {
        return null;
    }

}