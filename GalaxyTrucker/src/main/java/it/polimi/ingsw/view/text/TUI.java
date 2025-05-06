package it.polimi.ingsw.view.text;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.view.UI;

import java.util.*;

public class TUI extends UI {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void showWelcome() {
        System.out.println("Benvenuto in Galaxy Trucker! (by Cranio creations)");
    }

    @Override
    public void showInsertNickname() {
        System.out.print("Inserisci il tuo nickname: ");
    }

    @Override
    public void showChooseColor(List<String> availableColors) {
        System.out.println("Colori disponibili: " + String.join(", ", availableColors));
        System.out.print("Scegli un colore: ");
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
    public void showAvailableComponent(Component component) {
        System.out.println("Componenti selezionato:");
        System.out.println(component.getType());
    }

    @Override
    public void showKeptComponents(List<Component> kept) {
        System.out.println("Componenti tenuti: ");
        for (Component c : kept) {
            System.out.println("- " + c.getType());
        }
    }

    @Override
    public void showSelectComponent() {
        System.out.print("Seleziona il numero del componente da prendere: ");
    }

    @Override
    public void showBuildOptions() {
        System.out.println("Opzioni disponibili: [Link] [Keep] [Discard]");
        System.out.print("Scegli azione: ");
    }

    @Override
    public void showComponentPlaced(Component component, int x, int y) {
        System.out.println("✅ Componente " + component.getType() + " piazzato in [" + x + "," + y + "]");
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
    public void showBuildComplete() {
        System.out.println("Costruzione completata!");
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
        System.out.println("📊 Punteggi finali:");
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
    public void showPlayerShipStatus(String nickname, int position, int batteryCount, int humanCount, int alienCount,
                                     int cannonPower, int shieldPower, int enginePower, Map<String, Integer> goods) {
        System.out.println("\nStato della nave di " + nickname + ":");
        System.out.println("Posizione: " + position);
        System.out.println("Batterie: " + batteryCount);
        System.out.println("Umani: " + humanCount + ", 👽 Alieni: " + alienCount);
        System.out.println("Potenza fuoco: " + cannonPower);
        System.out.println("Scudi: " + shieldPower);
        System.out.println("Motori: " + enginePower);

        System.out.println("Merci a bordo:");
        for (Map.Entry<String, Integer> entry : goods.entrySet()) {
            System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
        }
    }

    @Override
    public void showShipLayout(String nickname, Component[][] layout) {
        System.out.println("\nLayout nave di " + nickname + ":");

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                Component c = layout[i][j];
                if (c != null) {
                    System.out.print("[" + abbreviate(c.getType()) + "]");
                } else {
                    System.out.print("[   ]");
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
    public String askNickname() {
        System.out.print("Inserisci il tuo nickname: ");
        return scanner.nextLine().trim();
    }

    @Override
    public PlayerColors askColor() {
        System.out.println("Colori disponibili: YELLOW, RED, GREEN, BLUE");
        while (true) {
            System.out.print("Scegli un colore: ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return PlayerColors.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Colore non valido. Riprova.");
            }
        }
    }

    @Override
    public int askStackIndex() {
        System.out.print("Scegli lo stack (0-3): ");
        while (true) {
            try {
                int index = Integer.parseInt(scanner.nextLine().trim());
                if (index >= 0 && index <= 3) return index;
                else System.out.print("Indice non valido. Riprova: ");
            } catch (NumberFormatException e) {
                System.out.print("Input non valido. Riprova: ");
            }
        }
    }

    @Override
    public int[] askCoordinates() {
        System.out.print("Inserisci coordinate (x y): ");
        while (true) {
            String[] parts = scanner.nextLine().trim().split("\\s+");
            if (parts.length == 2) {
                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    return new int[]{x, y};
                } catch (NumberFormatException e) {
                    System.out.print("Valori non numerici. Riprova: ");
                }
            } else {
                System.out.print("Formato errato. Inserisci due numeri separati da spazio: ");
            }
        }
    }

    @Override
    public void showGoodsToRemove(List<Goods> goods) {
        System.out.println("\nMerci da rimuovere:");
        for (int i = 0; i < goods.size(); i++) {
            System.out.println("[" + i + "] " + goods.get(i).getColor() + " (" + goods.get(i).getValue() + " crediti)");
        }
    }

    @Override
    public List<Integer> askGoodsIndexesToRemove(int numToRemove) {
        System.out.println("Seleziona " + numToRemove + " merci da rimuovere (es. 0 1):");
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                String[] parts = input.split("\\s+");
                if (parts.length != numToRemove) {
                    System.out.println("⚠️ Devi selezionare esattamente " + numToRemove + " merci.");
                    continue;
                }
                List<Integer> indexes = new ArrayList<>();
                for (String s : parts) {
                    indexes.add(Integer.parseInt(s));
                }
                return indexes;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Input non valido. Inserisci numeri separati da spazio.");
            }
        }
    }

    @Override
    public void showGoodsGained(List<Goods> goods) {
        System.out.println("Merci ottenute:");
        for (Goods g : goods) {
            System.out.println("- " + g.getColor() + " (" + g.getValue() + ")");
        }
    }

    @Override
    public Component askComponentFromStack(List<Component> stack) {
        System.out.println("\nComponenti disponibili nello stack:");
        for (int i = 0; i < stack.size(); i++) {
            System.out.println("[" + i + "] " + stack.get(i).getType());
        }

        System.out.print("Seleziona un componente dallo stack: ");
        while (true) {
            try {
                int index = Integer.parseInt(scanner.nextLine().trim());
                if (index >= 0 && index < stack.size()) {
                    return stack.get(index);
                } else {
                    System.out.print("Indice non valido. Riprova: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Input non numerico. Riprova: ");
            }
        }
    }

    @Override
    public int[] askPlacedComponent(Component[][] layout) {
        System.out.println("\nSeleziona un componente dalla tua nave (formato: x y):");

        while (true) {
            try {
                System.out.print("Inserisci coordinate (x y): ");
                String[] parts = scanner.nextLine().trim().split("\\s+");
                if (parts.length != 2) throw new NumberFormatException();

                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);

                if (x < 0 || x >= layout.length || y < 0 || y >= layout[0].length || layout[x][y] == null) {
                    System.out.println("Nessun componente in quella posizione. Riprova.");
                    continue;
                }
                return new int[]{x, y};

            } catch (NumberFormatException e) {
                System.out.println("Input non valido. Inserisci due numeri interi separati da spazio.");
            }
        }
    }
}