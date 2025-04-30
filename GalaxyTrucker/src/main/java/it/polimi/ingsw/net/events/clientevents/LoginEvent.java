package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.events.Event;

public class LoginEvent extends Event {
    private final String username;

    public LoginEvent(String username) {
        super(ClientEventType.LOGIN);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "LoginEvent{ username='" + username + "' }";
    }
}
