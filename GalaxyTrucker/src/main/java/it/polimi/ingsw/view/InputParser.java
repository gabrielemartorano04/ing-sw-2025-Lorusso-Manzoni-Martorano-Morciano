package it.polimi.ingsw.view;

import it.polimi.ingsw.net.events.Event;
import it.polimi.ingsw.net.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.events.clientevents.HelloEvent;
import it.polimi.ingsw.net.events.clientevents.LoginEvent;

public class InputParser {

    public Event parse(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] tokens = input.split("\\s+", 2); // dividi in massimo 2 pezzi
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "login":
                if (tokens.length < 2) {
                    System.out.println("Errore: devi specificare uno username.");
                    return null;
                }
                return new LoginEvent(tokens[1].trim());

            case "hello":
                if (tokens.length < 2) {
                    System.out.println("Errore: devi scrivere un messaggio.");
                    return null;
                }
                return new HelloEvent(tokens[1].trim(), ClientEventType.HELLO_MESSAGE);

            default:
                System.out.println("Comando sconosciuto: " + command);
                return null;
        }
    }
}

