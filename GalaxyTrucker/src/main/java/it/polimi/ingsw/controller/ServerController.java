package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.buildingevents.BuildCompleteEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.AvailableColorsEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.FirstConnectionEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.ClientHandlerVisitor;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.*;


public class ServerController {
    private final ArrayList<Client> clients;
    private final ClientHandlerVisitor visitor;
    private boolean gameCreated;
    private GameController gameController;
    private static ServerController istance;
    private int idToSet;


    private final BlockingQueue<EventWithSource> eventQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // 4 worker thread
    private final Map<Integer, Object> userLocks = new ConcurrentHashMap<>();


    public ServerController() {
        startDispatcher();
        this.visitor = new ClientHandlerVisitor();
        this.clients = new ArrayList<>();
        this.gameCreated = false;
        this.idToSet = 0;
        setIstance(this);
        startHeartbeat();
    }

    public static ServerController getIstance() {
        return istance;
    }

    public static void setIstance(ServerController istance) {
        ServerController.istance = istance;
    }


    public synchronized void addClient(Client client) {
        if (clients.isEmpty()) {
            client.setId(idToSet);
            idToSet++;
            clients.add(client);

            FirstConnectionEvent event = new FirstConnectionEvent(
                    "First player in the game, choose the total number of players of the Game"
            );
            client.updateClient(event);

        }else{
            System.out.println(idToSet);
            client.setId(idToSet);
            idToSet++;
            clients.add(client);

            AvailableColorsEvent event = new AvailableColorsEvent(gameController.getAvailablePlayerColors(),
                    "Login");
            client.updateClient(event);
        }
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client disconnected and removed from the game.");
    }

    public void addEventToQueue(ClientEvent event, int id) {
        for(Client c : clients) {
            if(c.getId()==id) {
                eventQueue.offer(new EventWithSource(event, c));
                return;
            }
        }
    }

    public EventWithSource generateSourceForRmi(ClientEvent event, int id) {
        for(Client c : clients) {
            System.out.println(c.getId());
            if(c.getId() == id) {
                return new EventWithSource(event, c);
            }
        }
        // per debug
        throw new RuntimeException("No Rmi Client found");
    }

    // questo metodo serve sia per la socket (chiamato dentro il dispatcher) che per l'RMI
    // il client tramite RMI server deve chiamare questo metodo per gestire l'evento che ha appena creato
    public void launchThreadEvent(EventWithSource entry) {
        executorService.submit(() -> {
            try {
                processEvent(entry, visitor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    // startDispatcher serve solo per la socket perché con la socket mettiamo gli eventi nella coda
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

    private void handleEventLogic(EventWithSource source, ClientHandlerVisitor visitor) {
        ClientEvent event = source.getEvent();
        Client client = source.getClient();

        visitor.setCurrentClient(client);
        event.call(visitor);
        ServerEvent response =visitor.getServerEvent();


        if (!gameCreated && visitor.getNumOfPlayers() > 0) {
            try {
                this.gameController = new GameController(visitor.getNumOfPlayers(), visitor.getGameLevel());
                visitor.setGameController(this.gameController);
                this.gameCreated = true;
            } catch (Exception e) {
                e.printStackTrace();
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


    public void broadcastEvent(ServerEvent event) {
        for(Client c: clients){
            c.updateClient(event);
        }
    }

    public void broadcastEventExceptSome(ServerEvent event, int clientId) {
        for(Client c: clients){
            if(c.getId()!=clientId){
                c.updateClient(event);
            }
        }
    }


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
            //TODO:TOGLI DOPO DEBUG
            StringBuilder message= new StringBuilder();
            for(Trucker t: gameController.getGame().getTruckerList()){
                message.append("Posizione di ").append(t.getNickname()).append(" : ").append(t.getPosition()).append("\n");
                t.getSpaceShip().wellDone();
            }
            System.out.println(message);
            broadcastEvent(new BuildCompleteEvent(gameController.getGame().getTruckerList()));
        }
    }

    private void startHeartbeat() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            ArrayList<Client> toRemove = new ArrayList<>();

            for (Client client : new ArrayList<>(clients)) {
                if (client.getSocketClient().isTimedOut(now)) {
                    System.out.println("Client " + client.getId() + " timed out. Removing...");
                    client.getSocketClient().close();
                    toRemove.add(client);
                } else {
                    client.notifyPing();
                }

            }

            for (Client c : toRemove) {
                removeClient((ClientHandler) c);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}

