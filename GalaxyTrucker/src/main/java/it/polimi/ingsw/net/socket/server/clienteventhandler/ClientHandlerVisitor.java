package it.polimi.ingsw.net.socket.server.clienteventhandler;


import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.*;
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
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.HumanCoordinates;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.FlightStartEvent;
import it.polimi.ingsw.net.socket.events.serverevents.WaitOthersStartFlightEvent;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.*;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

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
        //todo ripetuto nel parser togliere?
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
        System.out.println(currentClient.getTruckerController().getNickname() + " è in posizione: " + currentClient.getTruckerController().getPosition());
        //inserire condizione hourglass
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
            System.out.println(currentClient.getTruckerController().getNickname() + " è in posizione: " + currentClient.getTruckerController().getPosition());
        if(endBuilding< gameController.getGame().getNumPlayers()) {
            System.out.println("You're not the last one to end");
            return new WaitOthersBuildingEvent();
        }else{
            BuildTimerManager.getInstance().stop();
            for(Trucker t: gameController.getGame().getTruckerList()){
                t.getSpaceShip().wellDone();
            }
            return new BuildCompleteEvent(gameController.getGame().getTruckerList());
        }
    }

    @Override
    public ServerEvent visit(KeepTileEvent event) {
        Component component = event.getComponent();
        try {
            currentClient.getTruckerController().keep(component);
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
        Component component=event.getComponent();
        try {
            currentClient.getTruckerController().link(component,x,y);
            for(int i = 5; i<=9; i++){
                for(int j = 4; j<=10; j++){
                    if(currentClient.getTruckerController().getSpaceShip().getComponents()[i][j] != null){
                        System.out.println(currentClient.getTruckerController().getSpaceShip().getComponents()[i][j].getType());
                    }else
                        System.out.println("null");
                }
            }
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
        Stack stack= null;
        try {
            stack = trucker.showStack(index,gameController.getGame());
            gameController.getGame().viewStacks()[event.getIndex()].setSelected();
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
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
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public ServerEvent visit(ShowTilesListEvent event) {
        return null;
    }

    @Override
    public ServerEvent visit(ShowOtherShipEvent event) {
        for(Trucker t:gameController.getGame().getTruckerList()){
            if(t.getNickname().equals(event.getNicknameRequested())){
                Component[][] original = t.getSpaceShip().getComponents();
                Component[][] copy = new Component[original.length][];
                for (int i = 0; i < original.length; i++) {
                    copy[i] = new Component[original[i].length];
                    for (int j = 0; j < original[i].length; j++) {
                        Component c = original[i][j];
                        if (c != null) {
                            copy[i][j] = c;
                        }
                    }
                }
                //
                return new ShowOtherShipResponse(t.getSpaceShip().getComponents().clone(),event.getNicknameRequested());
            }
        }
        return new GenericErrorEvent("No such trucker with that nickname",false);
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
        Component[][] spaceship=currentClient.getTruckerController().getSpaceShip().getComponents();
        int cannonPower=currentClient.getTruckerController().getSpaceShip().getSingleCannonPower();
        int enginePower=currentClient.getTruckerController().getSpaceShip().getSingleEnginePower();
        currentClient.getTruckerController().getSpaceShip().increaseLost(currentClient.getTruckerController().getTrucker().getKeptComponents().size());
        currentClient.getTruckerController().getSpaceShip().countExposedConnectors();
        int position=currentClient.getTruckerController().getPosition();
        int batteryCount=currentClient.getTruckerController().getSpaceShip().getNumBatteries();
        int humanCount=currentClient.getTruckerController().getSpaceShip().getNumHuman();
        Map<String, Integer> track = new HashMap<>();
        gameController.getGame().sortTruckers();
        for (Trucker t : gameController.getGame().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }
        //STAMPA PER DEBUG
            System.out.println("Position: " + position);
            System.out.println("Batteries: " + batteryCount);
            System.out.println("Crew: " + humanCount);
            System.out.println("Cannon power (only single): " + cannonPower);
            System.out.println("Engine power (only single): " + enginePower);

        boolean[][] validCells = new boolean[11][12];
        for (int i = 0; i < spaceship.length; i++) {
            for (int j = 0; j < spaceship[0].length; j++) {
                Component c = spaceship[i][j];
                if(c == null || !c.getType().equals("Empty Space")) {
                    validCells[i][j] = true;
                } else {
                    validCells[i][j] = false;
                }
            }
        }
        System.out.println("\nComponents details:");
        for (int i = 5; i < 10; i++) {
            for (int j = 4; j < 11; j++) {
                if (!validCells[i][j]) continue;
                Component c = spaceship[i][j];
                if (c == null) continue;

                StringBuilder details = new StringBuilder();
                switch (c) {
                    case Engine engine -> {
                        if (engine.getIsDouble())
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE ENGINE");
                        else
                            details.append("[").append(i).append(",").append(j).append("] ENGINE");
                    }
                    case Cannon cannon -> {
                        if (cannon.getIsDouble())
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                        else
                            details.append("[").append(i).append(",").append(j).append("] CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                    }
                    case Load load -> {
                        if (load.getIsSpecial()) {
                            details.append("[").append(i).append(",").append(j).append("] SPECIAL LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.getGoods().isEmpty()) {
                                details.append(" contains: ");
                                for (Goods g : load.getGoods()) {
                                    details.append(g.getColor()).append(" ");
                                }
                            } else {
                                details.append(" is empty");
                            }
                        } else {
                            details.append("[").append(i).append(",").append(j).append("] LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.getGoods().isEmpty()) {
                                details.append(" contains: ");
                                for (Goods g : load.getGoods()) {
                                    details.append(g.getColor()).append(" ");
                                }
                            } else {
                                details.append(" is empty");
                            }
                        }
                    }
                    case Shield shield -> {
                        details.append("[").append(i).append(",").append(j).append("] SHIELD (direction: ").append(shield.getDirection()).append(")");
                    }
                    case BatteryStorage battery -> {
                        details.append("[").append(i).append(",").append(j).append("] BATTERY STORAGE (Capacity: ").append(battery.getCapacity()).append(")");
                        if (battery.getQuantity() != 0) {
                            details.append(" contains ").append(battery.getQuantity()).append(battery.getQuantity() == 1 ? " battery" : " batteries");
                        } else {
                            details.append(" is empty");
                        }
                    }
                    case Cabin cabin -> {
                        details.append("[").append(i).append(",").append(j).append("] CABIN");
                        if (cabin.getQuantity() != 0) {
                            if (cabin.getAlien() != null) {
                                details.append(cabin.getQuantity()).append(" contains 1 " + cabin.getAlien().getColor() + " alien");
                            } else {
                                details.append(" contains ").append(cabin.getQuantity()).append(cabin.getQuantity() == 1 ? " human" : " humans");
                            }
                        } else {
                            details.append(" is empty");
                        }

                    }
                    case LifeSupport lifeSupport -> {
                        details.append("[").append(i).append(",").append(j).append("] ").append("LIFE SUPPORT is ").append(lifeSupport.getColor());
                    }
                    default -> {
                        details.append("[").append(i).append(",").append(j).append("] ").append(c.getType().toUpperCase());
                    }
                }
                System.out.println(details);
            }
        }

        //STAMPA PER DEBUG

        return new FlightStartEvent(position,batteryCount,humanCount,cannonPower,enginePower,track, spaceship);
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
    public ServerEvent visit(DrawCardEvent event){
        Card card;
        cardController.setCurrCard(gameController.pickCardFromDeck());
        card = cardController.getCurrCard();
        if(!gameController.getGame().getTrucker(0).getNickname().equals(event.getCurrNickname())){
            return new GenericErrorEvent("You cannot draw a card while you are not the first player",false);
        }
        if(card!=null){
            return cardController.startCard();
        }else{
            int[] scores;
            Map<String, Integer> playersScores = new HashMap<>();
            scores = gameController.getGame().computeScores(numOfPlayers);
            for(int i=0; i<numOfPlayers; i++){
                playersScores.put(gameController.getGame().getTrucker(i).getNickname(), scores[i]);
            }
            return new EndGameEvent(playersScores);
        }

    }

    @Override
    public ServerEvent visit(HumanCoordEvent event) {
        return cardController.setHumansCoordinates(event);
    }

    @Override
    public ServerEvent visit(PlotGoodEvent event) {
        return cardController.setGoodsCoordinates(event);
    }

    @Override
    public ServerEvent visit(PlanetDecisionEvent event) {
        return cardController.playerLandingPlanets(event);
    }

    @Override
    public ServerEvent visit(RollDiceEvent event) {
        return cardController.rollDice();
    }

    @Override
    public ServerEvent visit(ActivateShieldEvent event) {
        return cardController.activateShield(event);
    }

    @Override
    public ServerEvent visit(ActivateCannonsEvent event) {
        return cardController.activateCannons(event);
    }

    @Override
    public ServerEvent visit(ActivateEnginesEvent event) {
        return cardController.activateEngines(event);
    }

    @Override
    public ServerEvent visit(TruckerLandingEvent event) {
        return cardController.truckerLanding(event);
    }

    @Override
    public ServerEvent visit(RearrangeEvent event) {
        return cardController.rearrange(event);
    }

    @Override
    public ServerEvent visit(SetWinDecision event) {
        return cardController.setWinDecision(event);
    }

    @Override
    public ServerEvent visit(RemoveGoodsEvent event) {
        return cardController.removeGoods(event);
    }

    @Override
    public ServerEvent visit(RemoveAskingEvent event) {
        return cardController.removeAsking(event);
    }

    @Override
    public ServerEvent visit(RemoveBatteriesEvent event) {
        return cardController.removeBatteries(event);
    }

    @Override
    public ServerEvent visit(TurnComponentEvent turnComponentEvent) {
        Component component = turnComponentEvent.getComponent();
        component.turn();
        return new TurnedComponentSuccessEvent(component);
    }

    @Override
    public ServerEvent visit(StackDroppedEvent event){
        System.out.println("sto per mandare StackDroppedOK");
        gameController.getGame().viewStacks()[event.getCurr()].dropStack();
        return new StackDropOk("Stack numero " + event.getCurr() + "disponibile", false);
    }

    //TODO GESTIRE QUESTI EVENTI
    @Override
    public ServerEvent visit(GainCosmicCreditsResponseEvent event) {
        System.out.println("Evento non ancora gestito ");
        return null;
    }

    @Override
    public ServerEvent visit(WarzoneStartEvent event) {
        try {
            return cardController.getCurrCard().handleCurrentCondition();
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    @Override
    public ServerEvent visit(RemotionSeenEvent event) {
        if (cardController.getCurrCard().getName().equalsIgnoreCase("Slavers")) {
            return new HumanCoordinates(ServerEventType.HUMAN_COORDINATES, true, currentClient.getTruckerController().getNickname());
        }else{
            try {
                return cardController.getCurrCard().removeGoods();
            } catch (WrongCallerException e) {
                return new GenericErrorEvent(e.getMessage(), true);
            } catch (GeneralError e) {
                return new GenericErrorEvent(e.getMessage(), true);
            }
        }
    }

}