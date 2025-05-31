package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.buildingevents.BuildCompleteEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.AvailableColorsEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.FirstConnectionEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;
import it.polimi.ingsw.net.ClientHandlerVisitor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
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
    }

    public static ServerController getIstance() {
        return istance;
    }

    public static void setIstance(ServerController istance) {
        ServerController.istance = istance;
    }


    public synchronized void addClient(Client client) {
        if (clients.isEmpty()) {
            FirstConnectionEvent event = new FirstConnectionEvent(
                    "First player in the game, choose the total number of players of the Game"
            );
            System.out.println("Ho aggiunto il client" + client.getClientHandler());
            clientHandleEvent(client.getClientHandler(), event);

        }else{
            AvailableColorsEvent event = new AvailableColorsEvent(gameController.getAvailablePlayerColors(),
                    "Login");
            clientHandleEvent(client.getClientHandler(), event);
        }
        client.setId(idToSet);
        idToSet++;
        clients.add(client);

    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client disconnected and removed from the game.");
    }

    public void addEventToQueue(ClientEvent event, ClientHandlerSocket client) {
        for(Client c : clients) {
            try {
                if(c.getId()==client.getId()) {
                    eventQueue.offer(new EventWithSource(event, c));
                    return;
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public EventWithSource generateSourceForRmi(ClientEvent event, int id){
        for(Client c : clients) {
            if(c.getId()==id) {
                return new EventWithSource(event, c);
            }
        }
        throw new RuntimeException("No client found with id: " + id);
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
        Integer id = null;
        try {
            id = eventWithSource.getClient().getClientHandler().getId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Object lock;
        if(userLocks.containsKey(id)) {
            lock = userLocks.get(id);
        }else{
            userLocks.put(id,new Object());
            lock = userLocks.get(id);
        }
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
                clientHandleEvent(client.getClientHandler(), response);
                return;
            }
        }


        if(response.isForAll()){
            broadcastEvent(response);
        }else{
            clientHandleEvent(client.getClientHandler(), response);
        }
    }


    public void broadcastEvent(ServerEvent event) {
        for(Client c: clients){
            clientHandleEvent(c.getClientHandler(), event);
        }
    }

    private void clientHandleEvent(ClientHandler c, ServerEvent e){
        try {
            c.handleEvent(e);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
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

    public GameController getGameController() {
        return gameController;
    }
}

