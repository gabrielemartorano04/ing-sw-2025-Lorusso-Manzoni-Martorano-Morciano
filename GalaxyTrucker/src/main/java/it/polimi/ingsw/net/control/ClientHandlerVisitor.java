package it.polimi.ingsw.net.control;


import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.events.clientevents.*;
import it.polimi.ingsw.net.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.events.clientevents.setupevents.ChooseToResume;
import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.DrawNewCardEvent;
import it.polimi.ingsw.net.events.serverevents.ResumeGameEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.TruckerWithdrawEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.*;
import it.polimi.ingsw.net.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.FlightStartEvent;

import java.util.*;

/**
 * visitor that handles all the incoming events from clients after their connection and returns the response
 * that the server has to send in broadcast or to the specific client
 */
public class ClientHandlerVisitor implements VirtualServer {

    /**
     * number of players in the current game
     */
    private int numOfPlayers;

    /**
     * gameController of this game, which is a singleton
     */
    private GameController gameController;

    /**
     * current number of clients logged, in order to know when the game can actually start
     */
    private int numLog;

    /**
     * game Level of the current game
     */
    private int gameLevel;

    /**
     * number of players that have ended the building phase
     */
    private int endBuilding;

    /**
     * number of players ready to start the flight phase
     */
    private int startFlight;

    /**
     * controller of the cards for the flight phase
     */
    private CardController cardController;

    /**
     * state of the old game if players want to load the old game
     */
    private GameState oldGameState;

    /**
     * number of players that have withdrawn
     */
    private int withdrawChoice;

    /**
     * constructor of the class
     */
    public ClientHandlerVisitor() {
        numLog = 0;
        endBuilding = 0;
        startFlight = 0;
        numOfPlayers = 0;
        gameLevel=0;
        withdrawChoice=0;
    }

    /**
     * sets the game controller
     *
     * @param gameController controller of the game
     */
    @Override
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * @return number of players in this game
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * @return level of this game
     */
    public int getGameLevel(){return gameLevel;}



    // === PHASE 1: Login and game creation or entrance to an old game ===


    /**
     * Handles the creation of a new game by storing the number of players and game level,
     * and returns the available colors for the first player.
     *
     * @param event  the event containing game setup information
     * @param client the client requesting the game creation
     * @return an event with available colors for player selection
     */
    @Override
    public ServerEvent visit(CreateGameEvent event, Client client) {
        int numPlayers= event.getNumberOfPlayers();
        this.gameLevel=event.getGameLevel();
        this.numOfPlayers = numPlayers;
        return new AvailableColorsEvent(EnumSet.allOf(PlayerColors.class), "First Player in the game, start login");
    }

    /**
     * Handles a login request by validating the player's nickname and color,
     * adding the player to the game if valid. Also handles the restorage and login
     * of a player to and old game if players decided to reload an old game
     *
     * @param event  the login event containing user credentials
     * @param client the client attempting to log in
     * @return a ServerEvent indicating the result (success, error, or game start)
     */
    @Override
    public ServerEvent visit(LoginEvent event, Client client) {
        String nickname;
        numLog++;
        if(numLog< gameController.getGame().getNumPlayers()){
            nickname = event.getUsername();
            PlayerColors color = event.getColor();
            if(!ServerController.getIstance().getOldGame()) {
                return tryAddNewPlayer(client,nickname,color);
            }else{
                return tryAddOldPlayer(client,nickname,color);
            }
        }
        if(numLog== gameController.getGame().getNumPlayers()){
            nickname = event.getUsername();
            PlayerColors color=event.getColor();

            return handleLastLogin(event,client,nickname,color);
        }
        return new NoAvailableSpaceError();
    }

    /**
     * tries to add a player to the old Game
     *
     * @param client client to log
     * @param nickname nickname chosen
     * @param color color chosen
     * @return serverEvent to send to client
     */
    private ServerEvent tryAddOldPlayer(Client client, String nickname, PlayerColors color){
        if(gameController.getGame().checkOldPlayers(nickname, color)) {
            client.setTruckerController(gameController.addOldPlayer(nickname, color));
            gameController.getGame().removeOldColors(color);
            gameController.getGame().removeOldNicknames(nickname);
            ArrayList<Trucker> allTruckers=gameController.getGame().getTruckerList();
            allTruckers.addAll(gameController.getGame().getTruckerOut());
            return new PlayerJoinedEvent(nickname, client.getTruckerController().getSpaceShip());
        }
        else{
            numLog--;
            return new GenericErrorEvent("the set of nickname and color didn't exists in the old game", false);
        }
    }

    /**
     * tries to add a player to the new Game
     *
     * @param client client to log
     * @param nickname nickname chosen
     * @param color color chosen
     * @return serverEvent to send to client
     */
    private ServerEvent tryAddNewPlayer(Client client, String nickname, PlayerColors color){
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
    }

    /**
     * handles the last player to the game, if the log is successful the game can start
     *
     * @param client client to log
     * @param nickname nickname chosen
     * @param color color chosen
     * @return serverEvent to send to all clients if the log is successful or to this client if
     *         there were some errors
     */
    private ServerEvent handleLastLogin(LoginEvent event, Client client, String nickname, PlayerColors color) {
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
                return new StartBuildingEvent(nickname, client.getTruckerController().getSpaceShip(), gameController.getGame().getComponents().getComponents(), gameController.getGame().getHourglass().getSecondsLeft(), players, gameController.getGame().getGameLevel());
            } else {
                return new StartBuildingEvent(nickname, client.getTruckerController().getSpaceShip(), gameController.getGame().getComponents().getComponents(), 0, players, gameController.getGame().getGameLevel());
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
                ArrayList<String> players = new ArrayList<>();
                for (Trucker t : gameController.getGame().getTruckerList()) {
                    players.add(t.getNickname());
                }

                if (gameController.getGame().getGameState() == GameState.BUILDING &&  gameController.getGame().getHourglass() != null) {
                    gameController.getGame().getHourglass().setTimeTurns(0);
                }

                return new ResumeGameEvent(gameController.getGame().getComponents().getComponents(), nickname, allTruckers, gameController.getGame().getGameState(), gameController.getGame().getGameLevel(), gameController.getGame().getTrack(), players);
            }else{
                numLog--;
                return new GenericErrorEvent("the set of nickname and color didn't exists in the old game", false);
            }
        }
    }

    /**
     * Handles the client's decision to resume a previously saved game or start a new one
     * If the client chooses to resume:
     * - Attempts to load the saved game from persistence.
     * - Prepares the game state for resumption (sets SETUP, restores old nicknames and colors).
     * - Returns an event with the list of old players for reconnection.
     * If the client chooses not to resume:
     * - Deletes any saved game data and signals the start of a new game
     *
     * @param event the resume decision sent by the client
     * @return a ServerEvent indicating the next action (resume game or start new)
     */
    @Override
    public ServerEvent visit(ChooseToResume event){
        ServerController.setOldGame(event.getChooseToResume());
        if(event.getChooseToResume()){
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



    // === PHASE 2: Building ===

    /**
     * Marks the selected component as unselected based on the given index, which is
     * the index of the component in the available components list
     *
     * @param event the discard event containing the component index
     * @param client the client performing the action
     * @return confirmation event of successful discard
     */
    @Override
    public ServerEvent visit(DiscardTileEvent event, Client client) {
        gameController.getGame().getComponents().setComponentAsUnselected(event.getIndex());
        return new DiscardSuccessEvent(event.getIndex(), client.getTruckerController().getNickname());
    }

    /**
     * Attempts to keep the selected component for the player
     * Saves game state upon success
     *
     * @param event the event containing the component to keep
     * @param client the client performing the action
     * @return success event or error if the component is invalid
     */
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

    /**
     * Attempts to link a component to the player's spaceship at a specified position
     * Saves game state upon success
     *
     * @param event the event with component and coordinates
     * @param client the client performing the action
     * @return success event or error if coordinates are invalid
     */
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

    /**
     * Handles the selection of a component from the pool
     * Marks it as selected and uncovered if valid
     *
     * @param event the event containing the component index
     * @param client the client performing the action
     * @return success event or error if index is invalid
     */
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

    /**
     * Selects a previously kept component and removes it from the player's kept list
     *
     * @param event the event with the index of the kept component
     * @param client the client performing the action
     * @return event containing the selected component or error if invalid
     */
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

    /**
     * Reveals the stack at the specified index to the player
     * Marks the stack as selected on the server
     *
     * @param event the event with the stack index
     * @param client the client requesting the view
     * @return event with the stack contents or error if invalid
     */
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

    /**
     * Handles the event to rotate the current selected component of a player
     *
     * @param turnComponentEvent the event containing the component to be rotated
     * @return an event indicating that the component was successfully turned
     */
    @Override
    public ServerEvent visit(TurnComponentEvent turnComponentEvent) {
        Component component = turnComponentEvent.getComponent();
        component.turn();
        return new TurnedComponentSuccessEvent(component);
    }

    /**
     * Handles the event where a stack of cards is dropped by a player in the building phase
     * Drops the stack at the given index and acknowledges the drop.
     *
     * @param event the event containing the index of the stack to be dropped
     * @return an acknowledgment event confirming that the stack was dropped
     */
    @Override
    public ServerEvent visit(StackDroppedEvent event){
        gameController.getGame().viewStacks()[event.getCurr()].dropStack();
        return new StackDropOk(false);
    }

    /**
     * Turns the hourglass, if it is turned for the second time and by the last player it ends the building phase
     *
     * @param event the hourglass turn request
     * @param client the client performing the action
     * @return success event, error if not allowed, or build complete if last player
     */
    @Override
    public ServerEvent visit(TurnHourglassEvent event, Client client) {
        ServerEvent tmp;
        try {
            tmp =gameController.getGame().turnHourglass(client.getTruckerController().getNickname());
        } catch (CannotTurnHourglassException e) {
            return new TurnHourglassError();
        }
        if (tmp != null) {
            endBuilding++;
            setPositionToTrucker(client);
            if(endBuilding == gameController.getGame().getNumPlayers()){
                return new BuildCompleteEvent(gameController.getGame().getTruckerList(), gameController.getIllegalShipNames());
            }else{
                ServerController.getIstance().startThreadHourglass();
                return tmp;
            }

        }
        return new HourglassTurnEvent(gameController.getGame().getHourglass().getSecondsLeft());
    }


    // === PHASE 2.1: Filling the ship  ===

    /**
     * assigns position to the current client for the flight phase
     *
     * @param client current client
     */
    private void setPositionToTrucker(Client client){
        if(gameController.getGame().getGameLevel()==2){
            switch(endBuilding) {
                case 1-> {
                    client.getTruckerController().setPosition(6);
                }
                case 2-> {
                    client.getTruckerController().setPosition(3);
                }
                case 3-> {
                    client.getTruckerController().setPosition(1);
                }
                case 4-> {
                    client.getTruckerController().setPosition(0);
                }
            }
        }
        else{
            switch(endBuilding) {
                case 1-> {
                    client.getTruckerController().setPosition(4);
                }
                case 2-> {
                    client.getTruckerController().setPosition(2);
                }
                case 3-> {
                    client.getTruckerController().setPosition(1);
                }
                case 4-> {
                    client.getTruckerController().setPosition(0);
                }
            }
        }
    }

    /**
     * Handles the end of the spaceship building phase for a client
     * Assigns a launch position based on the current game level and the number of players who have finished
     * If all players are done, stops the build timer and notifies completion with an appropriate event
     *
     * @param event  the end-of-building event sent by the client
     * @param client the client who finished building
     * @return an event indicating to wait for others players if not all players are done,
     *         otherwise the events that indicates the build phase is completed for all players
     */
    @Override
    public ServerEvent visit(EndBuildingEvent event, Client client) {
        endBuilding++;
        setPositionToTrucker(client);

        if(endBuilding< gameController.getGame().getNumPlayers()) {
            return new WaitOthersBuildingEvent();
        }else{
            BuildTimerManager.getInstance().stop();
            return new BuildCompleteEvent(gameController.getGame().getTruckerList(),  gameController.getIllegalShipNames());

        }
    }

    /** handles the remotion of a tile from a player ship */
    @Override
    public ServerEvent visit(RemoveTile event, Client client){
        if(client.getTruckerController().getSpaceShip().isLegalAfterRemove(event.getX(), event.getY())){
            ArrayList<int[]> alienCoord = client.getTruckerController().getSpaceShip().getAlienCabin();
            return new AlienCabinCoord(alienCoord, client.getTruckerController().getSpaceShip());
        }else{
            return new ShipNotLegal(client.getTruckerController().getSpaceShip());
        }
    }

    /**
     * Handles the request for retrieving the coordinates of the alien cabin from the client's spaceship
     *
     * @param event  the event requesting the alien cabin coordinates
     * @param client the client making the request
     * @return event containing the alien cabin coordinates
     */
    @Override
    public ServerEvent visit(AlienCoordinatesRequest event, Client client) {
        ArrayList<int[]> alienCoord=client.getTruckerController().getSpaceShip().getAlienCabin();
        return new AlienCabinCoord(alienCoord, client.getTruckerController().getSpaceShip());
    }

    /**
     * Handles the event where the client sends the coordinates of the aliens in the spaceship
     * Updates the spaceship with the received alien coordinates, sets the number of humans and battery,
     * updates lost components and exposed connectors, then starts the flight for this client
     *
     * @param event  the event containing the alien coordinates
     * @param client the client sending the coordinates
     * @return event with the updated spaceship and track information
     */
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
        int position=client.getTruckerController().getPosition();


        ArrayList<String> players = new ArrayList<>();
        for (Trucker t : gameController.getGame().getTruckerList()) {
            players.add(t.getNickname());
        }

        return new FlightStartEvent(position,gameController.getGame().getTrack(), spaceship, players);
    }

    /**
     * Handles the fact that the sender is ready for the flight phase.
     * If all players are ready for the flight phase then it starts the first card of the deck
     *
     * @param event event stating that the sender is ready for the flight phase
     * @return an event indicating to wait to the client if not all players have finished, otherwise
     *          the event containing the information of the first card of the flight phase
     */
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


    // === PHASE 3: Flight ===

    /**
     * Handles the event for drawing a new card from the deck.
     * Only the first player in the list is allowed to draw. If the deck is empty,
     * final scores are computed and the game ends.
     *
     * @param event the event requesting to draw a card
     * @return an event to start the new card, an error if not the current player's turn, or an end game event
     */
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

    /**
     * Handles the event to set the coordinates for the remotion of humans
     * in the current card.
     *
     * @param event the event containing human coordinates
     * @return the resulting event after setting the coordinates
     */
    @Override
    public ServerEvent visit(HumanCoordEvent event) {
        return cardController.removeHumans(event);
    }

    /**
     * Handles the event to set the coordinates of the goods for the current card.
     *
     * @param event the event containing goods coordinates
     * @return the resulting event after setting the coordinates
     */
    @Override
    public ServerEvent visit(PlotGoodEvent event) {
        return cardController.addGood(event);
    }

    /**
     * Handles the event where a player decides which planet to land on.
     *
     * @param event the event containing the planet landing decision
     * @return the resulting event after processing the landing decision
     */
    @Override
    public ServerEvent visit(PlanetDecisionEvent event) {
        return cardController.playerLandingPlanets(event);
    }

    /**
     * Handles the event to roll the dice for the current card.
     *
     * @param event the dice roll event
     * @return the resulting event after the dice are rolled
     */
    @Override
    public ServerEvent visit(RollDiceEvent event) {
        return cardController.rollDice();
    }

    /**
     * Handles the event to activate shields during the current card.
     *
     * @param event the event requesting shield activation
     * @return the resulting event after activating the shields
     */
    @Override
    public ServerEvent visit(ActivateShieldEvent event) {
        return cardController.activateShield(event);
    }

    /**
     * Handles the event to activate cannons during the current card.
     *
     * @param event the event requesting cannon activation
     * @return the resulting event after activating the cannons
     */
    @Override
    public ServerEvent visit(ActivateCannonsEvent event) {
        return cardController.activateCannons(event);
    }

    /**
     * Handles the event to activate engines during the current card.
     *
     * @param event the event requesting engine activation
     * @return the resulting event after activating the engines
     */
    @Override
    public ServerEvent visit(ActivateEnginesEvent event) {
        return cardController.activateEngines(event);
    }

    /**
     * Handles the event of a trucker landing on an Abandoned Station or Abandoned Ship.
     *
     * @param event the landing event
     * @return the resulting event after processing the landing
     */
    @Override
    public ServerEvent visit(TruckerLandingEvent event) {
        return cardController.truckerLanding(event);
    }

    /**
     * Handles the event to rearrange goods in the player's ship.
     *
     * @param event the rearrangement event
     * @return the resulting event after processing the rearrangement
     */
    @Override
    public ServerEvent visit(RearrangeEvent event) {
        return cardController.rearrange(event);
    }

    /**
     * Handles the player's decision in response to gaining cosmic credits.
     *
     * @param event the event containing the player's decision
     * @return the resulting event after processing the decision of the winner
     */
    @Override
    public ServerEvent visit(GainCosmicCreditsResponseEvent event) {
        return cardController.setWinDecision(event);
    }

    /**
     * Notifies the client that he can choose which goods to remove
     *
     * @param event the event requesting goods removal
     * @return the event containing the list of goods from which the player can decide the goods
     * that will be removed
     *
     */
    @Override
    public ServerEvent visit(RemoveGoodsEvent event) {
        return cardController.removeGoods(event);
    }

    /**
     * Handles the event where the player has decided which goods to remove.
     *
     * @param event the event representing the decision of the client
     * @return the resulting event after handling the remotion
     */
    @Override
    public ServerEvent visit(RemoveAskingEvent event) {
        return cardController.removeAsking(event);
    }

    /**
     * Handles the event to remove batteries from the player's ship.
     *
     * @param event the event requesting battery removal
     * @return the resulting event after removing the batteries
     */
    @Override
    public ServerEvent visit(RemoveBatteriesEvent event) {
        return cardController.removeBatteries(event);
    }

    /**
     * Manages the current warzone condition of the current warzone card
     *
     * @param event the warzone start event
     * @return the resulting event based on the condition
     */
    @Override
    public ServerEvent visit(WarzoneStartEvent event) {
        return cardController.handleCurrentCondition();
    }

    /**
     * Applies the current big Shot in the current card
     * @param event event that means client are ready for the big shot
     * @return serverEvent for clients
     */
    @Override
    public ServerEvent visit(ApplyBigShoot event){
        try {
            return this.cardController.getCurrCard().cardEffect();
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(),true);
        }
    }


    /**
     * Handles the event indicating that the player has seen the remotion of goods or batteries from his/her ship.
     *
     * @param event  the event confirming the player has seen the component
     * @param client the client triggering the event
     * @return the resulting event based on the outcome of removeGoods
     */
    @Override
    public ServerEvent visit(RemotionSeenEvent event, Client client) {
        try {
            return cardController.getCurrCard().removeGoods();
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }

    }

    /**
     * Handles the event where a player chooses whether to withdraw or continue.
     * Tracks each player's decision. When all have responded, proceeds to the next phase.
     *
     * @param event the withdrawal request containing the nickname and choice
     * @return a TruckerWithdrawEvent if more players still need to respond,
     *         or a DrawNewCardEvent if all players have responded
     */
    @Override
    public ServerEvent visit(WithdrawRequest event) {
        String nickname=event.getNickname();
        boolean choice=event.getChoice();


        System.out.println("Withdraw number " + withdrawChoice);

        if(withdrawChoice<gameController.getGame().getTruckerList().size()){
            if(choice){
                for(Trucker t: gameController.getGame().getTruckerList()){
                    if(t.getNickname().equals(nickname)){
                        gameController.getGame().withdraw(t);
                        gameController.getGame().sortTruckers();
                        break;
                    }
                }
            }else{
                withdrawChoice++;
            }

            if(Game.getIstance().getTruckerList().isEmpty()){
                int[] scores;
                Map<String, Integer> playersScores = new HashMap<>();
                scores = gameController.getGame().computeScores();
                for(int i=0; i<numOfPlayers; i++){
                    playersScores.put(gameController.getGame().getTruckerOut().get(i).getNickname(), scores[i]);
                }
                GamePersistence.saveGame(gameController);
                GamePersistence.deleteSave();
                return new EndGameEvent(playersScores);
            }else{
                if(withdrawChoice==Game.getIstance().getTruckerList().size()){
                    withdrawChoice=0;
                    if(!Game.getIstance().getTruckerList().isEmpty()){
                        return new DrawNewCardEvent(gameController.getGame().getTruckerList().getFirst().getNickname());
                    }else{
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
                }
                return new TruckerWithdrawEvent(nickname,choice);
            }
        }
        else {
            withdrawChoice=0;
            if(!Game.getIstance().getTruckerList().isEmpty()){
                return new DrawNewCardEvent(gameController.getGame().getTruckerList().getFirst().getNickname());
            }else{
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
        }
    }

    /**
     * Handles the event to show another player's ship to the requesting client.
     *
     * @param event the event containing the nickname of the player whose ship should be shown
     * @return an event containing the other player's ship components,
     *         or an error if the nickname is not found
     */
    @Override
    public ServerEvent visit(ShowOtherShipEvent event) {
        for(Trucker t:gameController.getGame().getTruckerList()){
            if(t.getNickname().equals(event.getNicknameRequested())){
                return new ShowOtherShipResponse(t.getSpaceShip(),event.getNicknameRequested());
            }
        }
        return new GenericErrorEvent("No such trucker with that nickname",false);
    }

//    /**
//     * Handles the event confirming that the player has seen his/her death.
//     * If there are still players remaining, a new card is drawn and the game continues.
//     * If not, final scores are computed and the game ends.
//     *
//     * @param event the event that confirms the set of the view of the client in Dead status
//     * @return an event to start the next card or an end game event if the game is over
//     */
//    @Override
//    public ServerEvent visit(DeadTrucker event){
//        Card card;
//        if (!gameController.getGame().getTruckerList().isEmpty()) {
//            cardController.setCurrCard(gameController.pickCardFromDeck());
//            card = cardController.getCurrCard();
//            if (card != null) {
//                GamePersistence.saveGame(gameController);
//                return cardController.startCard();
//            }
//        }
//        int[] scores;
//        Map<String, Integer> playersScores = new HashMap<>();
//        scores = gameController.getGame().computeScores();
//        for(int i=0; i<numOfPlayers; i++){
//            playersScores.put(gameController.getGame().getTruckerOut().get(i).getNickname(), scores[i]);
//        }
//        GamePersistence.saveGame(gameController);
//        GamePersistence.deleteSave();
//        return new EndGameEvent(playersScores);
//    }

    /**
     * Handles a pong response from a client, used to monitor client connectivity.
     *
     * @param event  the pong event
     * @param client the client that sent the pong
     * @return always returns null as no further action is needed
     */
    @Override
    public ServerEvent visit(PongEvent event, Client client) {
        client.updateLastPong();
        return null;
    }




}