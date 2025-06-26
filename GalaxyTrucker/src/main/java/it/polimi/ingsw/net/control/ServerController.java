package it.polimi.ingsw.net.control;


import it.polimi.ingsw.controller.BuildTimerManager;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.EndGameEvent;
import it.polimi.ingsw.net.events.serverevents.buildingevents.BuildCompleteEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.AvailableColorsEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.AvailablePlayersEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.ChooseResumeEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.FirstConnectionEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


/**
 * Core server-side controller for the client-server architecture of the game.
 * This class is responsible for managing all connected clients and handling the events
 * they generate during the game. Events are processed using the visitor pattern.
 * Responsibilities include:
 * - Managing the list of connected clients
 * - Processing and dispatching events from clients using ClientHandlerVisitor
 * - Synchronizing access to shared game state on a per-client basis
 * - Creating and coordinating the GameController to control game logic
 * - Loading saved games and handling resume functionality when available
 * - Monitoring client activity through a periodic heartbeat check
 * - Broadcasting server-side events to all clients when needed
 * Events are queued and processed concurrently using a fixed thread pool.
 * For each event, the visitor determines the appropriate logic, updates the model,
 * and returns a ServerEvent to notify one or more clients of the result.
 */

public class ServerController {

    /**
     * list of clients connected to the server
     */
    private final ArrayList<Client> clients;

    /**
     * visitor that handles all events coming from clients
     */
    private final ClientHandlerVisitor visitor;

    /**
     * true if the game has been created
     */
    private boolean gameCreated;

    /**
     * controller of the game class
     */
    private GameController gameController;

    /**
     * istance of the class, since it is a singleton
     */
    private static ServerController istance;

    /**
     * current id that will be set at the current client
     */
    private int idToSet;

    /**
     * true if the game is not ended
     */
    private boolean gameRunning;

    /**
     * true if clients have decided to load an OldGame
     */
    private static boolean oldGame;

    /**
     * queue of the events coming from clients that are using socket as type of connection
     */
    private final BlockingQueue<EventWithSource> eventQueue = new LinkedBlockingQueue<>();

    /**
     * pool of thread that handles the incoming event
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    /**
     * Map of locks that associates a lock object to each client (identified by their ID).
     * This ensures that all events from a given client are handled one at a time and in order,
     * maintaining consistency in how the client's view is updated.
     */
    private final Map<Integer, Object> userLocks = new ConcurrentHashMap<>();

    /**
     * hearbeat scheduler to implement the ping pong verification of clients' connections
     */
    private ScheduledExecutorService heartbeatScheduler;


    /**
     * constructor of the class
     */
    public ServerController() {
        this.gameRunning = false;
        startDispatcher();
        this.visitor = new ClientHandlerVisitor();
        this.clients = new ArrayList<>();
        this.gameCreated = false;
        this.idToSet = 0;
        setIstance(this);
        startHeartbeat();
    }

    /**
     * @return the unique istance of the ServerController class
     */
    public static ServerController getIstance() {
        return istance;
    }

    /**
     * sets the istance of this class in order to use getIstance
     *
     * @param istance unique istance of the class
     */
    public static void setIstance(ServerController istance) {
        ServerController.istance = istance;
    }

    /**
     * set the choice of resuming an old game or not made by the first client to connect
     *
     * @param oldGame choice made the client
     */
    public static void setOldGame(Boolean oldGame) {
        ServerController.oldGame = oldGame;
    }

    /**
     * Returns whether a previously saved game is being resumed.
     *
     * @return true if the game is being resumed, false otherwise
     */
    public boolean getOldGame(){
        return oldGame;
    }

    /**
     * @return true if the game is still being played
     */
    public boolean isGameRunning() {
        return gameRunning;
    }

    /**
     * adds a new client to the list of clients in the game, and updates the client's connected view with
     * the first with the first event based on the case that the client is the first to enter the game or not
     *
     * @param client client that wants to connect to the server
     */
    public synchronized void addClient(Client client) {
        if (clients.isEmpty()) {
            client.setId(idToSet);
            idToSet++;
            clients.add(client);
            //TODO rimettere questo al posto del false nell'if sotto GamePersistence.hasSaveFile()
            if(GamePersistence.hasSaveFile()){
                ChooseResumeEvent chooseResumeEvent = new ChooseResumeEvent();
                client.updateClient(chooseResumeEvent);
            }else {
                FirstConnectionEvent event = new FirstConnectionEvent(
                        "First player in the game, choose the total number of players of the Game"
                );
                client.updateClient(event);
            }

        }else{
            System.out.println(idToSet);
            client.setId(idToSet);
            idToSet++;
            clients.add(client);
            if(oldGame){
                AvailablePlayersEvent event = new AvailablePlayersEvent(Game.getIstance().getOldColors(), Game.getIstance().getOldNicknames());
                client.updateClient(event);
            }else {
                AvailableColorsEvent event = new AvailableColorsEvent(gameController.getAvailablePlayerColors(),
                        "Login");
                client.updateClient(event);
            }
        }
    }

    /**
     * removes all the clients passed from the list of the clients in the game because either
     * the game is ended or those clients are not responding to the ping-pong messages
     *
     * @param toRemove list of clients that have to be removed
     */
    public synchronized void removeClient(ArrayList<Client> toRemove) {
        clients.removeAll(toRemove);
        //TODO: da verificare se rimuove bene cosi
        System.out.println("Client disconnected and removed from the game.");
    }

    /**
     * Adds a client event to the event queue to be processed.
     *
     * @param event the event generated by the client
     * @param id the ID of the client who generated the event
     */
    public void addEventToQueue(ClientEvent event, int id) {
        for(Client c : clients) {
            if(c.getId()==id) {
                eventQueue.offer(new EventWithSource(event, c));
                return;
            }
        }
    }

    /**
     * Creates an EventWithSource object for a client using RMI,
     * pairing the event with the corresponding client.
     *
     * @param event the event generated by the client
     * @param id the ID of the client
     * @return the event paired with its source client
     */
    public EventWithSource generateSourceForRmi(ClientEvent event, int id) {
        for(Client c : clients) {
            if(c.getId() == id) {
                return new EventWithSource(event, c);
            }
        }
        return null;
    }

    /**
     * Submits an event for asynchronous processing using a thread from the pool.
     * Used by both socket and RMI clients.
     *
     * @param entry the event and its source client to process
     */
    public void launchThreadEvent(EventWithSource entry) {
        executorService.submit(() -> {
            if(entry!=null){
                processEvent(entry, visitor);
            }
        });
    }


    /**
     * Starts the dispatcher thread responsible for processing events from the socket-based clients.
     * It continuously waits for events from the event queue and delegates them for processing
     * using a worker thread from the thread pool.
     */
    public void startDispatcher() {
        Thread dispatcher = new Thread(() -> {
            while (true){
                try {
                    EventWithSource entry = eventQueue.take();
                    launchThreadEvent(entry);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        dispatcher.setDaemon(true);
        dispatcher.start();
    }

    /**
     * Processes a single client event in a thread-safe manner.
     * Each client is associated with a lock to ensure their events are handled
     * one at a time and in the order they were received. This guarantees consistency
     * in the game state and in how responses are delivered to the client.
     *
     * @param eventWithSource the event along with the client who sent it
     * @param visitor the visitor used to handle the event
     */
    public void processEvent(EventWithSource eventWithSource, ClientHandlerVisitor visitor) {
        int id = eventWithSource.getClient().getId();
        Object lock;
        if (!userLocks.containsKey(id)) {
            userLocks.put(id, new Object());
        }
        lock = userLocks.get(id);
        synchronized (lock) {
            handleEventLogic(eventWithSource, visitor);
        }

    }

    /**
     * Executes the actual logic for a client event using the visitor.
     * If this is the first valid event and the game hasn't been created yet, it initializes
     * the GameController using the settings provided by the visitor.
     * The resulting ServerEvent is either sent to all clients or only to the source client,
     * depending on the event's properties.
     *
     * @param source the client event and its originating client
     * @param visitor the visitor responsible for handling the event logic
     */
    private void handleEventLogic(EventWithSource source, ClientHandlerVisitor visitor) {
        ClientEvent event = source.getEvent();
        Client client = source.getClient();

        ServerEvent response = event.call(visitor, client);


        if (!gameCreated && visitor.getNumOfPlayers() > 0) {
            try {
                this.gameController = new GameController(visitor.getNumOfPlayers(), visitor.getGameLevel());
                visitor.setGameController(this.gameController);
                this.gameCreated = true;
            } catch (Exception e) {
                client.updateClient(response);
                return;
            }
        }

        if (response != null) {
            if (response.isForAll()) {
                broadcastEvent(response);
            } else {
                client.updateClient(response);
            }
        }
    }

    /**
     * Sends a server event to all connected clients.
     *
     * @param e the event to broadcast
     */
    public synchronized void broadcastEvent(ServerEvent e) {
        for(Client c : clients) {
            c.updateClient(e);
        }
    }

    /**
     * Starts the hourglass timer for the building phase.
     * If the hourglass is ended and there are still players that haven't ended their building phase
     * this method initializes their positions, ensuring they are placed in valid locations.
     * It also calls wellDone() methods on each spaceship depending on the game level,
     * then broadcasts a BuildCompleteEvent to all clients.
     */
    public void startThreadHourglass(){
        BuildTimerManager.getInstance().start(this, 90);
        if(!BuildTimerManager.getInstance().isRunning()){
            boolean[] pos=new boolean[]{false, false, false, false};
            for(Trucker t: gameController.getGame().getTruckerList()){
                if(t.getPosition()!=-1){
                    switch (t.getPosition()){
                        case 6: pos[0]=true;break;
                        case 3: pos[1]=true;break;
                        case 1: pos[2]=true;break;
                        case 0: pos[3]=true;break;
                    }
                }
            }
            for(Trucker t: gameController.getGame().getTruckerList()){
                if(t.getPosition()==-1){
                    for(int i=0;i<pos.length;i++){
                        if(!pos[i]){
                            switch (i){
                                case 0: t.setPosition(6);pos[i]=true;break;
                                case 1: t.setPosition(3);pos[i]=true;break;
                                case 2: t.setPosition(1);pos[i]=true;break;
                                case 3: t.setPosition(0);pos[i]=true;break;
                            }
                        }
                    }
                }
                if(gameController.getGame().getGameLevel()==2)
                    t.getSpaceShip().wellDone();
                else t.getSpaceShip().wellDoneForTrialFlight();
            }
            broadcastEvent(new BuildCompleteEvent(gameController.getGame().getTruckerList()));
        }
    }

    /**
     * Starts a scheduled heartbeat task to monitor client activity.
     * Every few seconds, it checks whether each client has timed out.
     * Timed-out clients are disconnected and removed from the game.
     * If any client is removed, the game is ended and scores are computed.
     */
    private void startHeartbeat() {
        heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
        heartbeatScheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            ArrayList<Client> toRemove = new ArrayList<>();
            for (Client client : new ArrayList<>(clients)) {
                if (client.isTimedOut(now)) {
                    client.closeConnection();
                    toRemove.add(client);
                } else {
                    client.notifyPing();
                }

            }
            finishGame(toRemove);
        }, 10, 5, TimeUnit.SECONDS);
    }

    /**
     * Finalizes the game if any client was removed due to timeout or disconnection.
     * Computes the final scores of all players and sends an EndGameEvent to all clients.
     *
     * @param toRemove the list of clients to be removed from the game
     */
    public void finishGame(ArrayList<Client> toRemove) {
        if(!toRemove.isEmpty()){
            this.gameRunning = true;
            this.removeClient(toRemove);
            int[] scores;
            Map<String, Integer> playersScores = new HashMap<>();
            scores = Game.getIstance().computeScores();
            for (int i = 0; i < Game.getIstance().getTruckerList().size() - 1; i++) {
                if (Game.getIstance().getTrucker(i).getNickname() != null) {
                    playersScores.put(Game.getIstance().getTrucker(i).getNickname(), scores[i]);
                } else {
                    playersScores.put("giocatore " + i, scores[i]);
                }
            }
            for (int i = 0; i < Game.getIstance().getTruckerOut().size() - 1; i++) {
                playersScores.put(Game.getIstance().getTruckerOut().get(i).getNickname(), scores[i]);
            }

            this.broadcastEvent(new EndGameEvent(playersScores));
        }
    }

    /**
     * Gracefully shuts down the server controller, closing all clients,
     * stopping heartbeat monitoring and thread executors.
     */
    public void shutdown() {
        System.out.println("[ServerController] Shutting down ServerController...");

        synchronized (clients) {
            for (Client c : clients) {
                c.closeConnection();
            }
            clients.clear();
        }

        if (heartbeatScheduler != null && !heartbeatScheduler.isShutdown()) {
            heartbeatScheduler.shutdownNow();
        }

        executorService.shutdownNow();

        if (gameCreated && gameRunning && gameController != null) {
            GamePersistence.saveGame(gameController);
        }
    }

}

