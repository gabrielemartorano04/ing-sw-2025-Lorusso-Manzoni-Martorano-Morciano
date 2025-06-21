package it.polimi.ingsw.net;


import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.events.clientevents.*;
import it.polimi.ingsw.net.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.events.clientevents.setupevents.ChoseToResume;
import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.DrawNewCardEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.ResumeGameEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.*;
import it.polimi.ingsw.net.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.FlightStartEvent;

import java.util.*;

public class ClientHandlerVisitor implements VirtualServer {
    private int numOfPlayers;
    private GameController gameController;
    private int numLog;
    private int gameLevel;
    private int endBuilding;
    private int startFlight;
    private CardController cardController;
    private GameState oldGameState;
    private int withdrawChoice;


    public ClientHandlerVisitor() {
        numLog = 0;
        endBuilding = 0;
        startFlight = 0;
        numOfPlayers = 0;
        gameLevel=0;
        withdrawChoice=0;
    }
    //se login event prendo nickname e color, controllo da model e creo evento risposta

    @Override
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }


    public int getNumOfPlayers() {
        return numOfPlayers;
    }
    public int getGameLevel(){return gameLevel;}


    //@Override
    //public void visit(EVENTO CON RISPOSTA SE VUOI CARICARE PARTITA VECCHIA O NO)
    // se la risposta è si allora manda l'evento con la partita vecchia e setta una variabile nel serverController che dica che si è scelto di ricaricare partita vecchia
    // se la risposta è no manda FirstConnection
    // A QUESTO PUNTO LA PARTITA NON ESISTE ANCORA CORRETTAMENTE


    @Override
    public ServerEvent visit(CreateGameEvent event, Client client) {
        int numPlayers= event.getNumberOfPlayers();
        this.gameLevel=event.getGameLevel();
        this.numOfPlayers = numPlayers;
        return new AvailableColorsEvent(EnumSet.allOf(PlayerColors.class), "First Player in the game, start login");
    }

    @Override
    public ServerEvent visit(DiscardTileEvent event, Client client) {
        // The component is already set as uncovered by the select handle, because we can't have a discard before a select
        gameController.getGame().getComponents().setComponentAsUnselected(event.getIndex());
        return new DiscardSuccessEvent(event.getIndex(), client.getTruckerController().getNickname());
    }

    @Override
    public ServerEvent visit(EndBuildingEvent event, Client client) {
        endBuilding++;
        System.out.println(client.getTruckerController().getNickname() + " è in posizione: " + client.getTruckerController().getPosition());
        //inserire condizione hourglass
        if(gameController.getGame().getGameLevel()==2){
            switch(endBuilding) {
                case 1-> {
                    client.getTruckerController().setPosition(6);
                    System.out.println("sono in caso 1 e sto gestendo " + client.getTruckerController().getNickname());
                }
                case 2-> {
                    client.getTruckerController().setPosition(3);
                    System.out.println("sono in caso 2 e sto gestendo " + client.getTruckerController().getNickname());

                }
                case 3-> {
                    client.getTruckerController().setPosition(1);
                    System.out.println("sono in caso 3 e sto gestendo " + client.getTruckerController().getNickname());
                }
                case 4-> {
                    client.getTruckerController().setPosition(0);
                    System.out.println("sono in caso 4 e sto gestendo " + client.getTruckerController().getNickname());

                }
            }
        }
        else{
            switch(endBuilding) {
                case 1-> {
                    client.getTruckerController().setPosition(4);
                    System.out.println("sono in caso 1 e sto gestendo " + client.getTruckerController().getNickname());
                }
                case 2-> {
                    client.getTruckerController().setPosition(2);
                    System.out.println("sono in caso 2 e sto gestendo " + client.getTruckerController().getNickname());

                }
                case 3-> {
                    client.getTruckerController().setPosition(1);
                    System.out.println("sono in caso 3 e sto gestendo " + client.getTruckerController().getNickname());
                }
                case 4-> {
                    client.getTruckerController().setPosition(0);
                    System.out.println("sono in caso 4 e sto gestendo " + client.getTruckerController().getNickname());

                }
            }
        }

            System.out.println(client.getTruckerController().getNickname() + " è in posizione: " + client.getTruckerController().getPosition());
        if(endBuilding< gameController.getGame().getNumPlayers()) {
            System.out.println("You're not the last one to end");
            return new WaitOthersBuildingEvent();
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

            return new BuildCompleteEvent(gameController.getGame().getTruckerList());

        }
    }

    @Override
    public ServerEvent visit(KeepTileEvent event, Client client) {
        Component component = event.getComponent();
        try {
            client.getTruckerController().keep(component);
            GamePersistence.saveGame(gameController);
            return new KeptComponentEvent(component,gameController.getGame().getComponents().getComponents());
        } catch (InvalidComponentsException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    @Override
    public ServerEvent visit(LinkTileEvent event, Client client) {
        int x=event.getX();
        int y=event.getY();
        Component component=event.getComponent();
        try {
            client.getTruckerController().link(component,x,y);
            GamePersistence.saveGame(gameController);
            return new ComponentLinkedEvent(event.getComponent(), x, y, gameController.getGame().getComponents().getComponents());
        }  catch (InvalidCoordinates e) {
            return new InvalidCoordinatesError(client.getTruckerController().getNickname());
        }
    }


    @Override
    public ServerEvent visit(LoginEvent event, Client client) {
        String nickname;
        numLog++;
        if(numLog< gameController.getGame().getNumPlayers()){
            nickname = event.getUsername();
            PlayerColors color = event.getColor();
            if(!ServerController.getIstance().getOldGame()) {
                try {
                    client.setTruckerController(gameController.tryAddPlayer(nickname, color));
                } catch (NicknameUsedException e) {
                    numLog--;
                    return new NicknameAlreadyInError(nickname, gameController.getAvailablePlayerColors());
                } catch (ColorNotAvailable e) {
                    numLog--;
                    return new ColorAlreadyInError(color, gameController.getAvailablePlayerColors());
                }

                return new PlayerJoinedEvent(nickname, client.getTruckerController().getSpaceShip());
            }else{
                if(gameController.getGame().checkOldPlayers(event.getUsername(), event.getColor())) {
                    client.setTruckerController(gameController.addOldPlayer(nickname, color));
                    gameController.getGame().removeOldColors(event.getColor());
                    gameController.getGame().removeOldNicknames(event.getUsername());
                    ArrayList<Trucker> allTruckers=gameController.getGame().getTruckerList();
                    allTruckers.addAll(gameController.getGame().getTruckerOut());
                    return new PlayerJoinedEvent(nickname, client.getTruckerController().getSpaceShip());
                }
                else{
                    numLog--;
                    return new GenericErrorEvent("the set of nickname and color didn't exists in the old game", false);
                }
            }
        }
        if(numLog== gameController.getGame().getNumPlayers()){
            nickname = event.getUsername();
            PlayerColors color=event.getColor();
            if(!ServerController.getIstance().getOldGame()) {
                try {
                    client.setTruckerController(gameController.tryAddPlayer(nickname, color));
                } catch (NicknameUsedException e) {
                    numLog--;
                    return new NicknameAlreadyInError(nickname, gameController.getAvailablePlayerColors());
                } catch (ColorNotAvailable e) {
                    numLog--;
                    return new ColorAlreadyInError(color, gameController.getAvailablePlayerColors());
                }
                ArrayList<String> players = new ArrayList<>();
                for (Trucker t : gameController.getGame().getTruckerList()) {
                    players.add(t.getNickname());
                }
                gameController.getGame().startHourglass();
                if (gameController.getGame().getGameLevel() == 2) {
                    return new StartBuildingEvent(nickname, client.getTruckerController().getSpaceShip(), gameController.getGame().viewStacks(), gameController.getGame().getComponents().getComponents(), gameController.getGame().getHourglass().getSecondsLeft(), players, gameController.getGame().getGameLevel());
                } else {
                    return new StartBuildingEvent(nickname, client.getTruckerController().getSpaceShip(), gameController.getGame().viewStacks(), gameController.getGame().getComponents().getComponents(), 0, players, gameController.getGame().getGameLevel());
                }
            }else{
                if(gameController.getGame().checkOldPlayers(event.getUsername(), event.getColor())) {
                    client.setTruckerController(gameController.addOldPlayer(nickname, color));
                    gameController.getGame().removeOldColors(event.getColor());
                    gameController.getGame().removeOldNicknames(event.getUsername());
                    ArrayList<Trucker> allTruckers=gameController.getGame().getTruckerList();
                    allTruckers.addAll(gameController.getGame().getTruckerOut());
                    gameController.getGame().setGameState(oldGameState);
                    gameController.setGameStarted(true);
                    System.out.println("sto mandando resume game");
                    ArrayList<String> players = new ArrayList<>();
                    for (Trucker t : gameController.getGame().getTruckerList()) {
                        players.add(t.getNickname());
                    }

                    //TODO: check che questo ha senso
                    boolean isLeader = client.getTruckerController().getTrucker() == Game.getIstance().getTrucker(0);

                    return new ResumeGameEvent(gameController.getGame().getComponents().getComponents(), nickname, allTruckers, gameController.getGame().getGameState(), gameController.getGame().getGameLevel(), gameController.getGame().getTrack(), players, isLeader);
                }else{
                    numLog--;
                    return new GenericErrorEvent("the set of nickname and color didn't exists in the old game", false);
                }
            }
        }
        return new NoAvailableSpaceError();
    }

    @Override
    public ServerEvent visit(SelectTileEvent event, Client client) {
        int index=event.getIndex();
        System.out.println("\n" + index + "\n");
        if(index < 0 || index > gameController.getGame().getComponents().getComponents().size() - 1){
            return new ComponentNotAvailableError();
        }
        try {
            client.getTruckerController().selectComponent(index,gameController.getGame().getComponents());
            gameController.getGame().getComponents().setComponentAsSelected(index);
            // probabilmente evitabile considerando che il fatto di essere covered o meno basta salvarlo lato view
            // ma meglio tenere view e server completamente sincronizzati per ora
            gameController.getGame().getComponents().setComponentAsUncovered(index);
            return new ComponentSelectedEvent(index, client.getTruckerController().getNickname());
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }


    @Override
    public ServerEvent visit(SelectKeptTileEvent event, Client client) {
        int index=event.getIndex();
        try {
            Component component=client.getTruckerController().getKeptComponent(index);
            client.getTruckerController().getTrucker().removeKeptComponent(index);
            return new SuccessSelectKept(component,index);
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    @Override
    public ServerEvent visit(ShowStackEvent event, Client client) {
        int index=event.getIndex();
        TruckerController trucker=client.getTruckerController();
        Stack stack;
        try {
            stack = trucker.showStack(index,gameController.getGame());
            gameController.getGame().viewStacks()[event.getIndex()].setSelected();
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
        return new ReturnStackEvent(stack);
    }

    @Override
    public ServerEvent visit(TurnHourglassEvent event, Client client) {
        ServerEvent tmp;
        try {
            tmp =gameController.getGame().turnHourglass();
        } catch (CannotTurnHourglassException e) {
            return new TurnHourglassError();
        }
        if (tmp != null) {
            ServerController.getIstance().startThreadHourglass();
            return tmp;
        }
        return new HourglassTurnEvent(gameController.getGame().getHourglass().getSecondsLeft());
    }

    @Override
    public ServerEvent visit(ShowOtherShipEvent event) {
        for(Trucker t:gameController.getGame().getTruckerList()){
            if(t.getNickname().equals(event.getNicknameRequested())){
                return new ShowOtherShipResponse(t.getSpaceShip().getComponents(),event.getNicknameRequested());
            }
        }
        return new GenericErrorEvent("No such trucker with that nickname",false);
    }


    @Override
    public ServerEvent visit(AlienCoordinatesRequest event, Client client) {
        ArrayList<int[]> alienCoord=client.getTruckerController().getSpaceShip().getAlienCabin();
        return new AlienCabinCoord(alienCoord);
    }

    @Override
    public ServerEvent visit(SendAlienCoord event, Client client) {
        if(event.getCoord()!=null){
            client.getTruckerController().getSpaceShip().setNumAlien(event.getCoord());
        }
        client.getTruckerController().getSpaceShip().setNumHuman();
        client.getTruckerController().getSpaceShip().setBattery();
        SpaceShip spaceship= client.getTruckerController().getSpaceShip();
        client.getTruckerController().getSpaceShip().increaseLost(client.getTruckerController().getTrucker().getKeptComponents().size());
        client.getTruckerController().getSpaceShip().countExposedConnectors();
        float cannonPower=client.getTruckerController().getSpaceShip().getSingleCannonPower();
        int enginePower=client.getTruckerController().getSpaceShip().getSingleEnginePower();
        int position=client.getTruckerController().getPosition();
        int batteryCount=client.getTruckerController().getSpaceShip().getNumBatteries();
        int humanCount=client.getTruckerController().getSpaceShip().getNumCrew();

        ArrayList<String> players = new ArrayList<>();
        for (Trucker t : gameController.getGame().getTruckerList()) {
            players.add(t.getNickname());
        }

        return new FlightStartEvent(position,batteryCount,humanCount,cannonPower,enginePower,gameController.getGame().getTrack(), spaceship, players);
    }


    @Override
    public ServerEvent visit(OkStartFlight event) {
        startFlight++;
        if(startFlight<gameController.getGame().getNumPlayers()){
            return new WaitOthersStartFlightEvent();
        }
        gameController.getGame().setGameState(GameState.FLIGHT);
        gameController.getGame().sortTruckers();
        cardController = gameController.getCardController();
        cardController.setCurrCard(gameController.pickCardFromDeck());
        return cardController.startCard();
    }

    @Override
    public ServerEvent visit(DrawCardEvent event) {
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
            scores = gameController.getGame().computeScores();
            for(int i=0; i<gameController.getGame().getTruckerList().size()-1; i++){
                playersScores.put(gameController.getGame().getTrucker(i).getNickname(), scores[i]);
            }
            for(int i=0; i<gameController.getGame().getTruckerOut().size()-1; i++){
                playersScores.put(gameController.getGame().getTruckerOut().get(i).getNickname(), scores[i]);
            }
            GamePersistence.deleteSave();
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
    public ServerEvent visit(GainCosmicCreditsResponseEvent event) {
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
        System.out.println("Turn component in server");
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


    @Override
    public ServerEvent visit(WarzoneStartEvent event) {
        try {
            return cardController.getCurrCard().handleCurrentCondition();
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    @Override
    public ServerEvent visit(RemotionSeenEvent event, Client client) {
        try {
            return cardController.getCurrCard().removeGoods();
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }

    }



    @Override
    public ServerEvent visit(DeadTrucker event){
        Card card;
        if (!gameController.getGame().getTruckerList().isEmpty()) {
            cardController.setCurrCard(gameController.pickCardFromDeck());
            card = cardController.getCurrCard();
            if (card != null) {
                GamePersistence.saveGame(gameController);
                return cardController.startCard();
            }
        }
        int[] scores;
        Map<String, Integer> playersScores = new HashMap<>();
        scores = gameController.getGame().computeScores();
        for(int i=0; i<numOfPlayers; i++){
            playersScores.put(gameController.getGame().getTruckerOut().get(i).getNickname(), scores[i]);
        }
        GamePersistence.saveGame(gameController);
        GamePersistence.deleteSave();
        return new EndGameEvent(playersScores);
    }

    @Override
    public ServerEvent visit(PongEvent event, Client client) {
        client.updateLastPong();
        return null;
    }

    @Override
    public ServerEvent visit(ChoseToResume event){
        ServerController.setOldGame(event.getChoseToResume());
        if(event.getChoseToResume()){
            gameController = GamePersistence.loadGame();
            if (gameController == null) {
                return new GenericErrorEvent("gameController nullo, impossibile ripristinare partita!", true);
            }
            if(gameController.getGame().getGameState().equals(GameState.FLIGHT)){
                cardController=gameController.getCardController();
            }
            oldGameState=gameController.getGame().getGameState();
            gameController.getGame().setGameState(GameState.SETUP);
            Game.setIstance(gameController.getGame());
            gameController.setGameStarted(false);
            gameController.getGame().createOldColors();
            gameController.getGame().createOldNicknames();
            for(int i = 0; i < gameController.getGame().getOldColors().size(); i++) {
                System.out.println("nickname: " + gameController.getGame().getOldNicknames().get(i) + " color: " + gameController.getGame().getOldColors().get(i));
            }
            return new AvailablePlayersEvent(gameController.getGame().getOldColors(), gameController.getGame().getOldNicknames());
        }else{
            GamePersistence.deleteSave();
            return new FirstConnectionEvent("First player in the game, choose the total number of players of the Game");
        }

    }

    @Override
    public ServerEvent visit(WithdrawRequest event) {
        String nickname=event.getNickname();
        withdrawChoice++;
        if(withdrawChoice<gameController.getGame().getTruckerList().size()){
            if(nickname!=null){
                for(Trucker t: gameController.getGame().getTruckerList()){
                    if(t.getNickname().equals(nickname)){
                        gameController.getGame().withdraw(t);
                        gameController.getGame().sortTruckers();
                        break;
                    }
                }
                return new TruckerWithdrawEvent(nickname);
            } else return new TruckerWithdrawEvent(null);
        }
        else {
            withdrawChoice=0;
            return new DrawNewCardEvent(gameController.getGame().getTruckerList().getFirst().getNickname());
        }
    }



}