package it.polimi.ingsw.net;


import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.events.clientevents.AlienCoordinatesRequest;
import it.polimi.ingsw.net.events.clientevents.GeneriErrorFromClient;
import it.polimi.ingsw.net.events.clientevents.SendAlienCoord;
import it.polimi.ingsw.net.events.clientevents.ShowOtherShipEvent;
import it.polimi.ingsw.net.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.setupevents.*;
import it.polimi.ingsw.net.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.HumanCoordinates;
import it.polimi.ingsw.net.events.serverevents.flightevents.FlightStartEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class ClientHandlerVisitor implements VirtualServer {
    private int numOfPlayers;
    private GameController gameController;
    private int numLog;
    private Client currentClient;
    private int gameLevel;
    private int endBuilding;
    private int startFlight;
    private final CardController cardController;
    private ServerEvent serverEvent;

    public ClientHandlerVisitor() {
        numLog = 0;
        endBuilding = 0;
        startFlight = 0;
        currentClient = null;
        numOfPlayers = 0;
        gameLevel=0;
        cardController = new CardController();
        serverEvent=null;
    }
    //se login event prendo nickname e color, controllo da model e creo evento risposta

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
    public int getGameLevel(){return gameLevel;}

    @Override
    public void visit(CreateGameEvent event) {
        int numPlayers= event.getNumberOfPlayers();
        this.gameLevel=event.getGameLevel();
        //todo ripetuto nel parser togliere?
        if (numPlayers<0 || numPlayers>4) {
            serverEvent= new NumberOfPlayersError();
            return;
        }
        this.numOfPlayers = numPlayers;
        serverEvent=new AvailableColorsEvent(EnumSet.allOf(PlayerColors.class), "First Player in the game, start login");
    }

    @Override
    public void visit(DiscardTileEvent event) {
        // The component is already set as uncovered by the select handle, because we can't have a discard before a select
        gameController.getGame().getComponents().setComponentAsUnselected(event.getIndex());
        serverEvent=new DiscardSuccessEvent(event.getIndex(), currentClient.getTruckerController().getNickname());
    }

    @Override
    public void visit(EndBuildingEvent event) {
        endBuilding++;
        System.out.println(currentClient.getTruckerController().getNickname() + " è in posizione: " + currentClient.getTruckerController().getPosition());
        //inserire condizione hourglass
        if(gameController.getGame().getGameLevel()==2){
            switch(endBuilding) {
                case 1-> {
                    currentClient.getTruckerController().setPosition(6);
                    System.out.println("sono in caso 1 e sto gestendo " + currentClient.getTruckerController().getNickname());
                }
                case 2-> {
                    currentClient.getTruckerController().setPosition(3);
                    System.out.println("sono in caso 2 e sto gestendo " + currentClient.getTruckerController().getNickname());

                }
                case 3-> {
                    currentClient.getTruckerController().setPosition(1);
                    System.out.println("sono in caso 3 e sto gestendo " + currentClient.getTruckerController().getNickname());
                }
                case 4-> {
                    currentClient.getTruckerController().setPosition(0);
                    System.out.println("sono in caso 4 e sto gestendo " + currentClient.getTruckerController().getNickname());

                }
            }
        }
        else{
            switch(endBuilding) {
                case 1-> {
                    currentClient.getTruckerController().setPosition(4);
                    System.out.println("sono in caso 1 e sto gestendo " + currentClient.getTruckerController().getNickname());
                }
                case 2-> {
                    currentClient.getTruckerController().setPosition(2);
                    System.out.println("sono in caso 2 e sto gestendo " + currentClient.getTruckerController().getNickname());

                }
                case 3-> {
                    currentClient.getTruckerController().setPosition(1);
                    System.out.println("sono in caso 3 e sto gestendo " + currentClient.getTruckerController().getNickname());
                }
                case 4-> {
                    currentClient.getTruckerController().setPosition(0);
                    System.out.println("sono in caso 4 e sto gestendo " + currentClient.getTruckerController().getNickname());

                }
            }
        }

            System.out.println(currentClient.getTruckerController().getNickname() + " è in posizione: " + currentClient.getTruckerController().getPosition());
        if(endBuilding< gameController.getGame().getNumPlayers()) {
            System.out.println("You're not the last one to end");
            serverEvent= new WaitOthersBuildingEvent();
        }else{
            BuildTimerManager.getInstance().stop();
            if(gameLevel==2){
                for(Trucker t: gameController.getGame().getTruckerList()){
                    t.getSpaceShip().wellDone();
                }
            }
            else{
                for(Trucker t: gameController.getGame().getTruckerList()){
                    t.getSpaceShip().wellDoneForTrialFlight();
                }
            }

            serverEvent= new BuildCompleteEvent(gameController.getGame().getTruckerList());

        }
    }

    @Override
    public void visit(KeepTileEvent event) {
        Component component = event.getComponent();
        try {
            currentClient.getTruckerController().keep(component);
            serverEvent= new KeptComponentEvent(component,gameController.getGame().getComponents().getComponents());
        } catch (InvalidComponentsException e) {
            serverEvent= new GenericErrorEvent(e.getMessage(), false);
        }
    }

    @Override
    public void visit(LinkTileEvent event) {
        int x=event.getX();
        int y=event.getY();
        Component component=event.getComponent();
        try {
            currentClient.getTruckerController().link(component,x,y);
            serverEvent= new ComponentLinkedEvent(event.getComponent(), x, y, gameController.getGame().getComponents().getComponents());
        }  catch (InvalidCoordinates e) {
            serverEvent= new InvalidCoordinatesError(currentClient.getTruckerController().getNickname());
        }
    }


    @Override
    public void visit(LoginEvent event) {
        String nickname;
        numLog++;
        if(numLog< gameController.getGame().getNumPlayers()){
            nickname = event.getUsername();
            PlayerColors color=event.getColor();
            try {
                currentClient.setTruckerController(gameController.tryAddPlayer(nickname,color));
            } catch (NicknameUsedException e) {
                numLog--;
                serverEvent= new NicknameAlreadyInError(nickname,gameController.getAvailablePlayerColors());
                return;
            } catch (ColorNotAvailable e) {
                numLog--;
                serverEvent= new ColorAlreadyInError(color,gameController.getAvailablePlayerColors());
                return;
            }

            serverEvent= new PlayerJoinedEvent(nickname);
            return;
        }
        if(numLog== gameController.getGame().getNumPlayers()){
            nickname = event.getUsername();
            PlayerColors color=event.getColor();
            try {
                currentClient.setTruckerController(gameController.tryAddPlayer(nickname,color));
            } catch (NicknameUsedException e) {
                numLog--;
                serverEvent= new NicknameAlreadyInError(nickname,gameController.getAvailablePlayerColors());
                return;
            } catch (ColorNotAvailable e) {
                numLog--;
                serverEvent= new ColorAlreadyInError(color,gameController.getAvailablePlayerColors());
                return;
            }
            ArrayList<String> players=new ArrayList<>();
            for(Trucker t:gameController.getGame().getTruckerList()){
                players.add(t.getNickname());
            }
            gameController.getGame().startHourglass();
            if (gameController.getGame().getGameLevel()==2){
                serverEvent= new StartBuildingEvent(nickname, currentClient.getTruckerController().getSpaceShip(), gameController.getGame().viewStacks(), gameController.getGame().getComponents().getComponents(), gameController.getGame().getHourglass().getSecondiRimanenti(),players, gameController.getGame().getGameLevel());
            }
            else{
                serverEvent= new StartBuildingEvent(nickname, currentClient.getTruckerController().getSpaceShip(), gameController.getGame().viewStacks(), gameController.getGame().getComponents().getComponents(), 0,players, gameController.getGame().getGameLevel());
            }
            return;
        }
        serverEvent= new NoAvailableSpaceError();
    }

    @Override
    public void visit(SelectTileEvent event) {
        int index=event.getIndex();
        System.out.println("\n" + index + "\n");
        if(index < 0 || index > gameController.getGame().getComponents().getComponents().size() - 1){
            serverEvent= new ComponentNotAvailableError();
            return;
        }
        try {
            currentClient.getTruckerController().selectComponent(index,gameController.getGame().getComponents());
            gameController.getGame().getComponents().setComponentAsSelected(index);
            // probabilmente evitabile considerando che il fatto di essere covered o meno basta salvarlo lato view
            // ma meglio tenere view e server completamente sincronizzati per ora
            gameController.getGame().getComponents().setComponentAsUncovered(index);
            serverEvent= new ComponentSelectedEvent(index, currentClient.getTruckerController().getNickname());
        } catch (GeneralError e) {
            serverEvent= new GenericErrorEvent(e.getMessage(), false);
        }
    }


    @Override
    public void visit(SelectKeptTileEvent event){
        int index=event.getIndex();
        try {
            System.out.println("\nEntered inside try of KeptTileEvent\n");
            Component component=currentClient.getTruckerController().getKeptComponent(index);
            currentClient.getTruckerController().getTrucker().removeKeptComponent(index);
            serverEvent= new SuccessSelectKept(component);
        } catch (GeneralError e) {
            System.out.println("\nEntered inside catch of KeptTileEvent\n");
            serverEvent= new GenericErrorEvent(e.getMessage(), false);
        }
    }

    @Override
    public void visit(ShowStackEvent event) {
        int index=event.getIndex();
        TruckerController trucker=currentClient.getTruckerController();
        Stack stack;
        try {
            stack = trucker.showStack(index,gameController.getGame());
            gameController.getGame().viewStacks()[event.getIndex()].setSelected();
        } catch (GeneralError e) {
            serverEvent= new GenericErrorEvent(e.getMessage(), false);
            return;
        }
        serverEvent= new ReturnStackEvent(stack);
    }

    @Override
    public void visit(TurnHourglassEvent event) {
        ServerEvent tmp;
        try {
            tmp =gameController.getGame().turnHourglass();
        } catch (CannotTurnHourglassException e) {
            serverEvent= new TurnHourglassError();
            return;
        }
        if (tmp != null) {
            ServerController.getIstance().startThreadHourglass();
            serverEvent= tmp;
            return;
        }
        serverEvent= new HourglassTurnEvent(gameController.getGame().getHourglass().getSecondiRimanenti());
    }

    @Override
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void visit(ShowOtherShipEvent event) {
        for(Trucker t:gameController.getGame().getTruckerList()){
            if(t.getNickname().equals(event.getNicknameRequested())){
                serverEvent= new ShowOtherShipResponse(t.getSpaceShip().getComponents(),event.getNicknameRequested());
                return;
            }
        }
        serverEvent= new GenericErrorEvent("No such trucker with that nickname",false);
    }

    @Override
    public void visit(GeneriErrorFromClient event){
        serverEvent= new GenericErrorEvent("error \n>",false);
    }

    @Override
    public void visit(AlienCoordinatesRequest event) {
        ArrayList<int[]> alienCoord=currentClient.getTruckerController().getSpaceShip().getAlienCabin();
        serverEvent= new AlienCabinCoord(alienCoord);
    }

    @Override
    public void visit(SendAlienCoord event) {
        if(event.getCoord()!=null){
            currentClient.getTruckerController().getSpaceShip().setNumAlien(event.getCoord());
        }
        currentClient.getTruckerController().getSpaceShip().setNumHuman();
        currentClient.getTruckerController().getSpaceShip().setBattery();
        Component[][] spaceship= currentClient.getTruckerController().getSpaceShip().getComponents();
        currentClient.getTruckerController().getSpaceShip().increaseLost(currentClient.getTruckerController().getTrucker().getKeptComponents().size());
        currentClient.getTruckerController().getSpaceShip().countExposedConnectors();
        int cannonPower=currentClient.getTruckerController().getSpaceShip().getSingleCannonPower();
        int enginePower=currentClient.getTruckerController().getSpaceShip().getSingleEnginePower();
        int position=currentClient.getTruckerController().getPosition();
        int batteryCount=currentClient.getTruckerController().getSpaceShip().getNumBatteries();
        int humanCount=currentClient.getTruckerController().getSpaceShip().getNumHuman();
        Map<String, Integer> track = new HashMap<>();
        gameController.getGame().sortTruckers();
        for (Trucker t : gameController.getGame().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }
        serverEvent= new FlightStartEvent(position,batteryCount,humanCount,cannonPower,enginePower,track, spaceship);
    }


    @Override
    public void visit(OkStartFlight event) {
        startFlight++;
        if(startFlight<gameController.getGame().getNumPlayers()){
            serverEvent= new WaitOthersStartFlightEvent();
            return;
        }
        cardController.setCurrCard(gameController.pickCardFromDeck());
        serverEvent= cardController.startCard();
    }

    @Override
    public void visit(DrawCardEvent event){
        Card card;
        cardController.setCurrCard(gameController.pickCardFromDeck());
        card = cardController.getCurrCard();
        if(!gameController.getGame().getTrucker(0).getNickname().equals(event.getCurrNickname())){
            serverEvent= new GenericErrorEvent("You cannot draw a card while you are not the first player",false);
            return;
        }
        if(card!=null){
            serverEvent= cardController.startCard();
        }else{
            int[] scores;
            Map<String, Integer> playersScores = new HashMap<>();
            scores = gameController.getGame().computeScores(numOfPlayers);
            for(int i=0; i<numOfPlayers; i++){
                playersScores.put(gameController.getGame().getTrucker(i).getNickname(), scores[i]);
            }
            serverEvent= new EndGameEvent(playersScores);
        }

    }

    @Override
    public void visit(HumanCoordEvent event) {
        serverEvent= cardController.setHumansCoordinates(event);
    }

    @Override
    public void visit(PlotGoodEvent event) {
        serverEvent= cardController.setGoodsCoordinates(event);
    }

    @Override
    public void visit(PlanetDecisionEvent event) {
        serverEvent= cardController.playerLandingPlanets(event);
    }

    @Override
    public void visit(RollDiceEvent event) {
        serverEvent= cardController.rollDice();
    }

    @Override
    public void visit(ActivateShieldEvent event) {
        serverEvent= cardController.activateShield(event);
    }

    @Override
    public void visit(ActivateCannonsEvent event) {
        serverEvent= cardController.activateCannons(event);
    }

    @Override
    public void visit(ActivateEnginesEvent event) {
        serverEvent= cardController.activateEngines(event);
    }

    @Override
    public void visit(TruckerLandingEvent event) {
        serverEvent= cardController.truckerLanding(event);
    }

    @Override
    public void visit(RearrangeEvent event) {
        serverEvent= cardController.rearrange(event);
    }

    @Override
    public void visit(SetWinDecision event) {
        serverEvent= cardController.setWinDecision(event);
    }

    @Override
    public void visit(RemoveGoodsEvent event) {
        serverEvent= cardController.removeGoods(event);
    }

    @Override
    public void visit(RemoveAskingEvent event) {
        serverEvent= cardController.removeAsking(event);
    }

    @Override
    public void visit(RemoveBatteriesEvent event) {
        serverEvent= cardController.removeBatteries(event);
    }

    @Override
    public void visit(TurnComponentEvent turnComponentEvent) {
        Component component = turnComponentEvent.getComponent();
        component.turn();
        serverEvent= new TurnedComponentSuccessEvent(component);
    }

    @Override
    public void visit(StackDroppedEvent event){
        System.out.println("sto per mandare StackDroppedOK");
        gameController.getGame().viewStacks()[event.getCurr()].dropStack();
        serverEvent= new StackDropOk("Stack numero " + event.getCurr() + "disponibile", false);
    }

    //TODO GESTIRE QUESTI EVENTI
    //adesso questi metodi sono void, quindi quando vuoi ritornare un ServerEvent fai serverEvent= new EventoCheVuoi(dati); e poi return;
    @Override
    public void visit(GainCosmicCreditsResponseEvent event) {
        System.out.println("Evento non ancora gestito ");
    }

    @Override
    public void visit(WarzoneStartEvent event) {
        try {
            serverEvent= cardController.getCurrCard().handleCurrentCondition();
        } catch (WrongCallerException | GeneralError e) {
            serverEvent= new GenericErrorEvent(e.getMessage(), false);
        }
    }

    @Override
    public void visit(RemotionSeenEvent event) {
        if (cardController.getCurrCard().getName().equalsIgnoreCase("Slavers")) {
            serverEvent= new HumanCoordinates(ServerEventType.HUMAN_COORDINATES, true, currentClient.getTruckerController().getNickname());
        }else{
            try {
                serverEvent= cardController.getCurrCard().removeGoods();
            } catch (WrongCallerException | GeneralError e) {
                serverEvent= new GenericErrorEvent(e.getMessage(), true);
            }
        }
    }

    public ServerEvent getServerEvent(){
        return serverEvent;
    }

    @Override
    public void visit(DeadTrucker event){
        Card card;
        if (!gameController.getGame().getTruckerList().isEmpty()) {
            cardController.setCurrCard(gameController.pickCardFromDeck());
            card = cardController.getCurrCard();
            if (card != null) {
                serverEvent = cardController.startCard();
                return;
            }
        }
        int[] scores;
        Map<String, Integer> playersScores = new HashMap<>();
        scores = gameController.getGame().computeScores(numOfPlayers);
        for(int i=0; i<numOfPlayers; i++){
            playersScores.put(gameController.getGame().getTruckerOut().get(i).getNickname(), scores[i]);
        }
        serverEvent= new EndGameEvent(playersScores);
    }

}