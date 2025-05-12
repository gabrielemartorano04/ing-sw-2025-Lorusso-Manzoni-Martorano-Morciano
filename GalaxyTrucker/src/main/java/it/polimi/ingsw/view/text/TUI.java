package it.polimi.ingsw.view.text;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.EmptySpace;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.view.InputReader;
import it.polimi.ingsw.view.UI;

import java.util.*;
import java.util.List;

public class TUI extends UI {

    private final InputReader inputReader;

    public TUI() {
        this.inputReader = new InputReader();
    }

    @Override
    public void pause(int seconds) {
        try {
            Thread.sleep(seconds * 1000L); // pausa bloccante
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // buona pratica
        }
    }

    @Override
    public void showWelcome() {
        System.out.println("Benvenuto in Galaxy Trucker! (by Cranio creations)");
    }

    @Override
    public void showChooseColor(EnumSet<PlayerColors> availableColors) {
        System.out.println("Colori disponibili: " + String.join(", ", availableColors.toString()));
        System.out.print("Inserisci nickname e scegli un colore: ");
    }

    @Override
    public void showColorTaken() {
        System.out.println("Colore già scelto, scegline un altro.");
    }

    @Override
    public void showNicknameTaken() {
        System.out.println("Nickname già in uso, scegline un altro.");
    }

    @Override
    public void showPlayerJoined(String nickname) {
        System.out.println("Giocatore " + nickname + " si è unito alla partita.");
    }

    @Override
    public void showWaitingForPlayers(int currentPlayers) {
        System.out.println("In attesa di giocatori... (" + currentPlayers + ")");
    }

    @Override
    public void showStartCountdown(int secondsLeft) {
        System.out.println("Il gioco inizierà tra " + secondsLeft + " secondi.");
    }

    @Override
    public void showSetupComplete() {
        System.out.println("Setup completato. Inizio costruzione nave.");
    }

    @Override
    public void showBuildPhaseStart() {
        System.out.println("\nInizio della fase di BUILDING. Costruisci la tua nave!");
    }

    @Override
    public void showAvailableComponent(AvailableComponents components) {
        System.out.println("=== Componenti disponibili ===");
        for (int i = 0; i < components.getComponents().size(); i++) {
            Component c = components.getComponents().get(i);
            String info = c.getCovered()
                    ? c.getType() + " (coperto)"
                    : String.format("%s [N:%s E:%s S:%s W:%s]",
                    c.getType(), c.getLink()[0], c.getLink()[1], c.getLink()[2], c.getLink()[3]);
            System.out.println(i + ") " + info);
        }
    }

    @Override
    public void showKeptComponents(List<Component> kept) {
        if (kept!=null) {
            System.out.println("Componenti tenuti: ");
            for (Component c : kept) {
                System.out.println("- " + c.getType());
            }
        }
    }

    @Override
    public void showSelectComponent() {
        System.out.print("Seleziona il numero del componente da prendere: ");
    }

    @Override
    public void showComponentOptions() {
        System.out.println("Opzioni disponibili: [Link] [Keep] [Discard]");
        System.out.print("Scegli azione: ");
    }

    @Override
    public void showBuildOptions() {
        System.out.println("Opzioni disponibili: [Turn Hourglass] [Select Component] [Show Stack] [End]");
        System.out.print("Scegli azione: ");
    }

    @Override
    public void showDiscardComponent(Component component) {
        System.out.println("Discard component " + component.getType());
    }

    @Override
    public void showComponentPlaced(Component component, int x, int y) {
        System.out.println("Componente " + component.getType() + " piazzato in [" + x + "," + y + "]");
    }

    @Override
    public void showInvalidPlacement(String reason) {
        System.out.println("Posizionamento non valido: " + reason);
    }

    @Override
    public void showHourglassTurned() {
        System.out.println("Clessidra girata.");
    }

    @Override
    public void showTimeExpired() {
        System.out.println("Tempo scaduto!");
    }

    @Override
    public void showWaitForOthers() {
        System.out.println("Attendi che gli altri giocatori finiscano.");
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showBuildComplete() {
        System.out.println("Costruzione completata! Attesa altri giocatori");
    }

    @Override
    public void showFlightPhaseStart() {
        System.out.println("Inizio fase di volo. Preparati agli eventi spaziali!");
    }

    @Override
    public void showCardDrawn(Card card) {
        System.out.println("Carta pescata: " + card.getName());
    }

    @Override
    public void showCardEffectStart(Card card) {
        System.out.println("Effetto della carta: " + card.getDescription());
    }

    @Override
    public void showCardEffectEnd(Card card) {
        System.out.println("Carta " + card.getName() + " completata.");
    }

    @Override
    public void showTruckerMoved(String nickname, int newPosition) {
        System.out.println(nickname + " si è mosso alla posizione " + newPosition);
    }

    @Override
    public void showTruckerWithdrawn(String nickname) {
        System.out.println(nickname + " si è ritirato dal volo.");
    }

    @Override
    public void showTruckerLostAllCrew(String nickname) {
        System.out.println(nickname + " ha perso tutto l'equipaggio.");
    }

    @Override
    public void showGameEnded() {
        System.out.println("Partita terminata!");
    }

    @Override
    public void showScores(Map<String, Integer> finalScores) {
        System.out.println("Punteggi finali:");
        finalScores.forEach((name, score) -> System.out.println("- " + name + ": " + score));
    }

    @Override
    public void showWinner(String nickname) {
        System.out.println("Vincitore: " + nickname + "!");
    }

    @Override
    public void showError(String message) {
        System.out.println("ERRORE: " + message);
    }

    @Override
    public void showConnectionLost() {
        System.out.println("Connessione persa.");
    }

    @Override
    public void showInvalidCommand(String expectedFormat) {
        System.out.println("Comando non valido. Atteso: " + expectedFormat);
    }

    @Override
    public void showPlayerShipStatus(int position, int batteryCount, int humanCount,
                                     int cannonPower, int enginePower, Map<String, Integer> goods) {
        System.out.println("Posizione: " + position);
        System.out.println("Batterie: " + batteryCount);
        System.out.println("Umani: " + humanCount);
        System.out.println("Potenza fuoco: " + cannonPower);
        System.out.println("Motori: " + enginePower);

        System.out.println("Merci a bordo:");
        if (goods != null) {
            for (Map.Entry<String, Integer> entry : goods.entrySet()) {
                System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    @Override
    public void showShipLayout(String nickname, Component[][] layout) {
        System.out.println("\nLayout nave di " + nickname + ":");

        for (int i = 5; i < 10; i++) {
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];

                if (c == null) {
                    System.out.print("[   ]");
                } else {
                    switch(c){
                        case EmptySpace e->{
                            System.out.print("[ . ]");
                        }

                        default -> {
                            System.out.printf("[ %c ]", c.getType().charAt(0));
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    private String abbreviate(String name) {
        // restituisce le prime 3 lettere in maiuscolo, o meno se più corto
        return name.length() >= 3 ? name.substring(0, 3).toUpperCase() : name.toUpperCase();
    }

    @Override
    public void askIndex() {
        System.out.print("Scegli indice: ");
    }

    @Override
    public void askCoordinates() {
        System.out.print("Inserisci coordinate (x y): ");
    }

    @Override
    public void showGoodsToRemove(List<Goods> goods) {
        System.out.println("\nMerci da rimuovere:");
        for (int i = 0; i < goods.size(); i++) {
            System.out.println("[" + i + "] " + goods.get(i).getColor() + " (" + goods.get(i).getValue() + " crediti)");
        }
    }

    @Override
    public void askGoodsIndexesToRemove() {
        System.out.println("Seleziona merci da rimuovere (es. 0 1):");
    }

    @Override
    public void showGoodsGained(Goods good) {
        System.out.println("Merci ottenute:");
        System.out.println("- " + good.getColor() + " (" + good.getValue() + ")");
    }

    @Override
    public void askPlacedComponent() {
        System.out.println("\nSeleziona un componente dalla tua nave (formato: x y):");
        System.out.print("Inserisci coordinate (x y): ");
    }

    @Override
    public void showTrack(Map<String, Integer> truckers) {
        // Mappa posizione → simbolo (numero o lettera colore)
        Map<Integer, String> board = new HashMap<>();
        for (int i = 1; i <= 24; i++) {
            board.put(i, String.valueOf(i));
        }
        for (Map.Entry<String, Integer> entry : truckers.entrySet()) {
            String color = entry.getKey();
            int pos = entry.getValue();
            if (pos >= 1 && pos <= 24) {
                board.put(pos, color.substring(0, 1).toUpperCase());
            }
        }

        // Stampa (6 sopra, 4x2 lati, 6 sotto)
        System.out.printf("      [%2s] [%2s] [%2s] [%2s] [%2s] [%2s]\n",
                board.get(1), board.get(2), board.get(3), board.get(4), board.get(5), board.get(6));

        for (int i = 0; i < 4; i++) {
            System.out.printf("[%2s]                          [%2s]\n",
                    board.get(24 - i), board.get(7 + i));
        }

        System.out.printf("      [%2s] [%2s] [%2s] [%2s] [%2s] [%2s]\n",
                board.get(20), board.get(19), board.get(18), board.get(17), board.get(16), board.get(15));
    }

    @Override
    public void showBooleanOption(String message) {
        System.out.println(message + " (s/n)");
    }

    @Override
    public void showInsertNumPlayers() {
        System.out.println("\nInserisci numero di player e il livello di gioco:");
    }

    @Override
    public void updateHourglass(int secondsLeft) {

    }

    @Override
    public void showStack(ArrayList<Card> stack) {
        System.out.println("\nCarte nella pila:");

        if (stack == null || stack.isEmpty()) {
            System.out.println("[ Pila vuota ]");
            return;
        }

        for (int i = 0; i < stack.size(); i++) {
            Card card = stack.get(i);
            System.out.printf(" [%02d] %-15s (Livello %d)%n",
                    i, card.getName(), card.getLevel());
        }
    }
}