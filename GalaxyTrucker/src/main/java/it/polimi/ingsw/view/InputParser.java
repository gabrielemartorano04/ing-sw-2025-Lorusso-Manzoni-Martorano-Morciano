package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.socket.events.*;
import it.polimi.ingsw.net.socket.events.clientevents.LoginEvent;

public class InputParser {

    public Event parse(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] tokens = input.split("\\s+", 3); // dividi in massimo 2 pezzi
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "login":
                if (tokens.length < 2) {
                    System.out.println("Errore: devi specificare uno username e un colore.");
                    return null;
                }
                if (tokens.length < 3) {
                    System.out.println("Errore: devi specificare uno username e un colore.");
                    return null;
                }
                PlayerColors color=null;
                switch (tokens[2].toLowerCase()) {
                    case "blue": color = PlayerColors.BLUE; break;
                    case "red": color = PlayerColors.RED; break;
                    case "green": color = PlayerColors.GREEN; break;
                    case "yellow": color = PlayerColors.YELLOW; break;
                }
                return new LoginEvent(tokens[1].trim(),color);

            case "hello":
                if (tokens.length < 2) {
                    System.out.println("Errore: devi scrivere un messaggio.");
                    return null;
                }
                return new HelloEvent(tokens[1].trim(), EventType.HELLO_MESSAGE);

            default:
                System.out.println("Comando sconosciuto: " + command);
                return null;
        }
    }
}

