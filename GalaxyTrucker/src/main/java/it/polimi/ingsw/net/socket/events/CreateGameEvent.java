package it.polimi.ingsw.net.socket.events;



public class CreateGameEvent extends Event {
    private final int numberOfPlayers;

    public CreateGameEvent(int numberOfPlayers) {
        super(EventType.CREATE_GAME);
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    @Override
    public String toString() {
        return "CreateGameEvent{ numberOfPlayers=" + numberOfPlayers + " }";
    }
}

