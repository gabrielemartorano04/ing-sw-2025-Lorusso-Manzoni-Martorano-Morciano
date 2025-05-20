package it.polimi.ingsw.view.text;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.cards.Effect;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.StartCardEvent;
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
    public void showWelcome() {
        System.out.println("Welcome in Galaxy Trucker! (by Cranio creations)");
    }

    @Override
    public void showWelcome(EnumSet<PlayerColors> availableColors) {

    }

    @Override
    public void showChooseColor(EnumSet<PlayerColors> availableColors) {
        System.out.println("Colours available: " + String.join(", ", availableColors.toString()));
        System.out.print("Insert nickname and choose a colour: ");
    }

    @Override
    public void showColorTaken() {
        System.out.println("Colour already chosen, choose another one.");
    }

    @Override
    public void showNicknameTaken() {
        System.out.println("Nickname already taken, choose another one.");
    }

    @Override
    public void showPlayerJoined(String nickname) {
        System.out.println("Congratulation " + nickname + "! You've joined the game!.");
    }

    @Override
    public void showWaitingForPlayers(int currentPlayers) {
        System.out.println("Waiting for other truckers... (" + currentPlayers + ")");
    }

    @Override
    public void showStartCountdown(int secondsLeft) {
        System.out.println("the game will start in " + secondsLeft + " seconds.");
    }

    @Override
    public void showSetupComplete() {
        System.out.println("SETUP completed. Start building your spaceship.");
    }

    @Override
    public void showPlayers(ArrayList<String> players) {
        System.out.println("Players in the game:");
        for(String player : players) {
            System.out.println(player);
        }
    }

    @Override
    public void showBuildPhaseStart(ArrayList<Component> availableComponents) {
        System.out.println("\nStart BUILDING phase. Build your spaceship!");
        showAvailableComponent(availableComponents);
    }

    @Override
    public void showAvailableComponent(ArrayList<Component> components) {
        System.out.println("=== Available components ===");
        int i = 0;
        int numCols = 3;

        for (int originalIndex = 0; originalIndex < components.size(); originalIndex++) {
            Component c = components.get(originalIndex);

            if (!c.getSelected()) {
                String info;
                if (c.getCovered()) {
                    info = "covered";
                } else {
                    info = String.format("%s [N:%s E:%s S:%s W:%s]",
                            c.getType(), c.getLink()[0], c.getLink()[1], c.getLink()[2], c.getLink()[3]);
                }

                System.out.printf("%2d) %-30s", i, info);

                if ((i + 1) % numCols == 0 || originalIndex == components.size() - 1) {
                    System.out.println();
                }
            }
            i++;
        }
    }

    @Override
    public void showKeptComponents(ArrayList<Component> kept) {
        if (!kept.isEmpty()) {
            System.out.println("Components kept: ");
            for (int i=0; i<kept.size(); i++) {
                System.out.println("- " + kept.get(i).getType());
            }
        }
    }

    @Override
    public void showSelectComponent() {
        System.out.print("Select the index of the component you want to link: ");
    }

    @Override
    public void showComponentOptions() {
        System.out.println("Options: [Link x y (to link the component in the position x y of your spaceship)] [Keep] [Discard]");
        System.out.print("Choose your action: ");
    }

    @Override
    public void showBuildOptions() {
        System.out.println("Options: [Hourglass] [select x] [Stack x] [End] [selectKept x]");
        System.out.print("Choose your action: ");
    }

    @Override
    public void showDiscardComponent(Component component) {
        System.out.println("Discard component " + component.getType());
    }

    @Override
    public void showComponentPlaced(Component component, int x, int y) {
        System.out.println("Component " + component.getType() + " linked in [" + x + "," + y + "]");
    }

    @Override
    public void showInvalidPlacement(String reason) {
        System.out.println("Place not correct: " + reason);
    }

    @Override
    public void showHourglassTurned() {
        System.out.println("Hourglass Turned.");
    }

    @Override
    public void showTimeExpired() {
        System.out.println("Time expired, end of turn.!");
    }

    @Override
    public void showWaitForOthers() {
        System.out.println("Wait for other players to finish building.");
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showBuildComplete() {
        System.out.println("Building completed!");
    }

    @Override
    public void showFlightPhaseStart() {
        System.out.println("Start FLIGHT phase. Get ready for space events!");
    }

    @Override
    public void showCardDrawn(StartCardEvent event) {
        System.out.println("Card drawn: " + event.getCardName());
        System.out.println("Card description: " + event.getCardDescription());
        if(event.getPower() != -1) System.out.println(event.getPower() + " enemy cannon power");
        if(event.getMeteorites() != null) {
            String message = "Meteorites arriving:\n";
            int j = 0;
            for(int[] list: event.getMeteorites()){
                for(int i: list){
                    if(i == 0) message += "Meteorite " + j + " arriving from the top\n";
                    else if(i == 1) message += "Meteorite " + j + " arriving from the right side\n";
                    else if(i == 2) message += "Meteorite " + j + " arriving from the bottom\n";
                    else if(i == 3) message += "Meteorite " + j + " arriving from the left side\n";
                }
                j++;
            }
        }
        if(event.getShoots() != null) {
            String message = "Shoots arriving:\n";
            int j = 0;
            for(int[] list: event.getMeteorites()){
                for(int i: list){
                    if(i == 0) message += "Shoots " + j + " arriving from the top\n";
                    else if(i == 1) message += "Shoots " + j + " arriving from the right side\n";
                    else if(i == 2) message += "Shoots " + j + " arriving from the bottom\n";
                    else if(i == 3) message += "Shoots " + j + " arriving from the left side\n";
                }
                j++;
            }
        }
        if(event.getPlanets() != null) {
            String message = "Planets to visit:\n";
            int i = 0;
            for (List<Goods> list: event.getPlanets()){
                int red = 0, yellow = 0, green = 0, blue = 0;
                message += "Planets " + i + ": ";
                i++;
                for(Goods g: list){
                    if(g.getValue() == 1) blue++;
                    else if(g.getValue() == 2) green++;
                    else if(g.getValue() == 3) yellow++;
                    else if(g.getValue() == 4) red++;
                }
                message += red + " red goods, " + yellow + " yellow goods, " + green + " green goods, " + blue + " blue goods";
            }
        }
        if(event.getPenalties() != null && event.getConditionsOrder() != null) {
            String message = "Conditions and penalties to apply:\n";
            for (Condition condition : event.getConditionsOrder()) {
                Effect effect = event.getPenalties().get(condition);
                message += condition.toString() + " → " + effect.toString() + "\n";
            }
        }
        if(event.getDays() != -1) System.out.println(event.getDays() + " days to lose");
        if(event.getAstronautsLost() != -1) System.out.println(event.getAstronautsLost() + " crew members to lose");
        if(event.getGoodsLost() != -1) System.out.println(event.getGoodsLost() + " goods to lose");
        if(event.getCredits() != -1) System.out.println(event.getCredits() + " credits to gain");
        if(event.getNumHumans() != -1) System.out.println(event.getNumHumans() + " humans required");
        if(event.getGoods() != null) {
            int red = 0, yellow = 0, green = 0, blue = 0;
            for (Goods g : event.getGoods()) {
                if(g.getValue() == 1) blue++;
                if(g.getValue() == 2) green++;
                if(g.getValue() == 3) yellow++;
                if(g.getValue() == 4) red++;
            }
            System.out.println("Goods to gain: " + red + " red goods, " + yellow + " yellow goods, " + green + " green goods, " + blue + " blue goods");
        }
        if(event.getBank() != null) System.out.println("Bank deposit: " + event.getBank().getGoodsInBank(1) + " red goods, " + event.getBank().getGoodsInBank(2) + " yellow goods, " + event.getBank().getGoodsInBank(3) + " green goods, " + event.getBank().getGoodsInBank(4) + " blue goods");
        //TODO AGGIUNGI QUESTION sse nickname uguale

    }

    @Override
    public void showCardEffectStart(Card card) {
        System.out.println("Card effect: " + card.getDescription());
    }

    @Override
    public void showCardEffectEnd(Card card) {
        System.out.println("Card " + card.getName() + " Completed.");
    }

    @Override
    public void showTruckerMoved(String nickname, int newPosition) {
        System.out.println(nickname + " moved to " + newPosition);
    }

    @Override
    public void showTruckerWithdrawn(String nickname) {
        System.out.println(nickname + " withdraw.");
    }

    @Override
    public void showTruckerLostAllCrew(String nickname) {
        System.out.println(nickname + " lost all crew members.");
    }

    @Override
    public void showGameEnded() {
        System.out.println("Game ended. Thanks for playing!");
    }

    @Override
    public void showScores(Map<String, Integer> finalScores) {
        System.out.println("Final scores:");
        finalScores.forEach((name, score) -> System.out.println("- " + name + ": " + score));
        System.out.println("Winner: " + finalScores.keySet().iterator().next() + "!");
    }

    @Override
    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    @Override
    public void showConnectionLost() {
        System.out.println("Lost connection.");
    }

    @Override
    public void showInvalidCommand(String expectedFormat) {
        System.out.println("Invalid command. Expected: " + expectedFormat);
    }

    @Override
    public void showPlayerShipStatus(int position, int batteryCount, int humanCount,
                                     int cannonPower, int enginePower, Map<String, Integer> goods) {
        System.out.println("Position: " + position);
        System.out.println("Batteries: " + batteryCount);
        System.out.println("Crew: " + humanCount);
        System.out.println("Cannon power (only single): " + cannonPower);
        System.out.println("Engine power (only single): " + enginePower);

        System.out.println("Goods on the ship:");
        if (goods != null) {
            for (Map.Entry<String, Integer> entry : goods.entrySet()) {
                System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    @Override
    public void showShipLayout(String nickname, Component[][] layout) {
        System.out.println("\nShip layout of " + nickname + ":");

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
        System.out.print("Choose an index: ");
    }

    @Override
    public void askCoordinates() {
        System.out.print("Insert coordinates (x y): ");
    }

    @Override
    public void showGoodsToRemove(List<Goods> goods) {
        System.out.println("\nGoods to remove:");
        for (int i = 0; i < goods.size(); i++) {
            System.out.println("[" + i + "] " + goods.get(i).getColor() + " (" + goods.get(i).getValue() + " crediti)");
        }
    }

    @Override
    public void askGoodsIndexesToRemove() {
        System.out.println("Select goods to remove (ex. 0 1):");
    }

    @Override
    public void showGoodsGained(Goods good) {
        System.out.println("Goods gained:");
        System.out.println("- " + good.getColor() + " (" + good.getValue() + ")");
    }

    @Override
    public void askPlacedComponent() {
        System.out.println("\nSelect a component from your spaceship (format: x y):");
        System.out.print("Insert conrdinates (x y): ");
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
        System.out.println("\nInsert the number of players and the game level:");
    }

    @Override
    public void updateHourglass(int secondsLeft) {

    }

    @Override
    public void showStack(ArrayList<Card> stack) {
        System.out.println("\nCards in the stack:");

        if (stack == null || stack.isEmpty()) {
            System.out.println("[ Empty stack ]");
            return;
        }

        for (int i = 0; i < stack.size(); i++) {
            Card card = stack.get(i);
            System.out.printf(" [%02d] %-15s (Level %d)%n",
                    i, card.getName(), card.getLevel());
        }
    }

    @Override
    public void showComponentSelected(Component c) {
        String info;

        if ("engine".equalsIgnoreCase(c.getType())) {
            Engine engine = (Engine) c;
            info = String.format("ENGINE [N:%s E:%s S:%s W:%s] (Double: %s)\n",
                    engine.getLink()[0], engine.getLink()[1], engine.getLink()[2], engine.getLink()[3],
                    engine.getIsDouble());
        } else if ("cannon".equalsIgnoreCase(c.getType())) {
            Cannon cannon = (Cannon) c;
            info = String.format("CANNON [N:%s E:%s S:%s W:%s] (Double: %s)\n",
                    cannon.getLink()[0], cannon.getLink()[1], cannon.getLink()[2], cannon.getLink()[3],
                    cannon.getIsDouble());
        } else {
            info = String.format("%s [N:%s E:%s S:%s W:%s]\n",
                    c.getType(), c.getLink()[0], c.getLink()[1], c.getLink()[2], c.getLink()[3]);
        }

        System.out.print(info);
    }
}