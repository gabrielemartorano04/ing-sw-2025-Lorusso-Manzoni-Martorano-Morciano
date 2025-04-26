package it.polimi.ingsw.net.events;

public class LoginEvent extends Event {
    private final String username;

    public LoginEvent(String username) {
        super(EventType.LOGIN);
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
