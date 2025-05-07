package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientHandlerVisitor;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerController {
    private final ArrayList<ClientHandlerServer> clients = new ArrayList<>();
    private int nextId = 0;
    private final ClientHandlerVisitor visitor;

    private final BlockingQueue<EventWithSource> eventQueue = new LinkedBlockingQueue<>();

    public ServerController() {
        startDispatcher();
        this.visitor = new ClientHandlerVisitor();
    }

    public void handleEvent(ClientEvent event, ClientHandlerServer client) {
        eventQueue.offer(new EventWithSource(event, client));
    }

    public void startDispatcher() {
        Thread dispatcher = new Thread(() -> {
            while (true){
                try {
                    EventWithSource entry = eventQueue.take();
                    processEvent(entry, visitor);
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
        ClientEvent event = eventWithSource.getEvent();
        ClientHandlerServer client = eventWithSource.getClient();

        ServerEvent response = event.call(visitor);
        if(response.isForAll()){
            for(ClientHandlerServer c: clients){
                c.sendEvent(response);
            }
        }else{
            client.sendEvent(response);
        }
    }
}

