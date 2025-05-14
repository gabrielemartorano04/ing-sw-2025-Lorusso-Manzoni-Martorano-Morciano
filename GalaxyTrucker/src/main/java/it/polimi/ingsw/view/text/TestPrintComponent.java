package it.polimi.ingsw.view.text;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.components.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TestPrintComponent {
    private AvailableComponents availableComponents = new AvailableComponents();

    // Mappa da indice stampato a indice reale, usata per interagire con l'utente
    private HashMap<Integer, Integer> visualToActualIndex;

    public void showAvailableComponents() {
        ArrayList<Component> components = availableComponents.getComponents();
        System.out.println("=== Componenti disponibili ===");
        int i = 0;
        int numCols = 3;
        visualToActualIndex = new HashMap<>();

        for (int originalIndex = 0; originalIndex < components.size(); originalIndex++) {
            Component c = components.get(originalIndex);

            if (!c.getSelected()) {
                String info;
                if (c.getCovered()) {
                    info = "coperto";
                } else {
                    info = String.format("%s [N:%s E:%s S:%s W:%s]",
                            c.getType(), c.getLink()[0], c.getLink()[1], c.getLink()[2], c.getLink()[3]);
                }

                System.out.printf("%2d) %-30s", i, info);
                visualToActualIndex.put(i, originalIndex);

                if ((i + 1) % numCols == 0 || originalIndex == components.size() - 1) {
                    System.out.println();
                }

                i++;
            }
        }

        System.out.println("Mappatura indice visualizzato → indice reale: " + visualToActualIndex);
    }

    public void runInteractiveSession() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            showAvailableComponents();

            System.out.print("\nInserisci l'indice del componente da selezionare (oppure 'exit' per uscire): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                running = false;
            } else {
                try {
                    int selectedVisualIndex = Integer.parseInt(input);
                    if (visualToActualIndex.containsKey(selectedVisualIndex)) {
                        int actualIndex = visualToActualIndex.get(selectedVisualIndex);
                        availableComponents.setComponentAsSelected(actualIndex);
                        System.out.println("✔ Componente selezionato correttamente!\n");
                    } else {
                        System.out.println("❌ Indice non valido.\n");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Input non valido. Inserisci un numero o 'exit'.\n");
                }
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        TestPrintComponent tester = new TestPrintComponent();
        tester.runInteractiveSession();
    }
}
