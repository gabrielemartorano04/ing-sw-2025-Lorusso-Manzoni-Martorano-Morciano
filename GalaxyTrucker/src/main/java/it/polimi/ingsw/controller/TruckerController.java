package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;

import java.util.HashMap;
import java.util.Map;

public class TruckerController {
    private final Trucker trucker;
    private final GameController gameController;

    // Ultimi parametri salvati
    private Component selectedComponent;
    private int selectedIndex;
    private int selectedX;
    private int selectedY;
    private int selectedStackIndex;

    private final Map<String, Runnable> commandMap = new HashMap<>();

    public TruckerController(Trucker trucker, GameController gameController) {
        this.trucker = trucker;
        this.gameController = gameController;

        // Popola la commandMap
        commandMap.put("Select Component", this::handleSelectComponent);
        commandMap.put("Keep", this::handleKeep);
        commandMap.put("Link", this::handleLink);
        commandMap.put("Turn Hourglass", this::handleTurnHourglass);
        commandMap.put("Show Stack", this::handleShowStack);
        commandMap.put("End", this::handleEnd);
    }

    // Metodo principale chiamato dalla rete / view
    public void handleInput(String command) throws IllegalArgumentException {
        Runnable action = commandMap.get(command);
        if (action != null) {
            action.run();
        } else {
            throw new IllegalArgumentException("Unknown command: " + command);
        }
    }

    // --- Metodi specifici di gestione comandi ---

    private void handleSelectComponent() throws IllegalArgumentException {
        try {
            trucker.chooseWhatToDo("Select Component");
            Component c = gameController.selectComponent(trucker, selectedIndex);
            selectedComponent = c; // salvo il componente selezionato
        } catch (Exception e) {
            throw new IllegalArgumentException("illegal component: " + selectedComponent);
        }
    }

    private void handleKeep() throws IllegalArgumentException {
        try {
            trucker.chooseWhatToDo("Keep");
            gameController.keepComponent(trucker, selectedComponent);
        } catch (Exception e) {
            throw new IllegalArgumentException("illegal component: " + selectedComponent);
        }
    }

    private void handleLink() throws IllegalArgumentException {
        try {
            trucker.chooseWhatToDo("Link");
            gameController.linkComponent(trucker, selectedComponent, selectedX, selectedY);
        } catch (Exception e) {
           throw new IllegalArgumentException("illegal link");
        }
    }

    private void handleTurnHourglass() throws IllegalArgumentException {
        try {
            trucker.chooseWhatToDo("Turn Hourglass");
            gameController.turnHourglass(trucker);
        } catch (Exception e) {
            throw new IllegalArgumentException("can't turn hourglass");
        }
    }

    private void handleShowStack() throws IllegalArgumentException {
        try {
            trucker.chooseWhatToDo("Show Stack");
            var cards = gameController.showStack(trucker, selectedStackIndex);
        } catch (Exception e) {
           throw new IllegalArgumentException("can't show stack");
        }
    }

    private void handleEnd() throws IllegalStateException {
        try {
            trucker.chooseWhatToDo("End");
            gameController.endBuildingFor(trucker);
        } catch (Exception e) {
            throw new IllegalStateException("can't end building");
        }
    }

    // --- Setters per i parametri (chiamati dalla rete / VirtualClientHandler) ---

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
    }

    public void setSelectedComponent(Component component) {
        this.selectedComponent = component;
    }

    public void setSelectedCoordinates(int x, int y) {
        this.selectedX = x;
        this.selectedY = y;
    }

    public void setSelectedStackIndex(int stackIndex) {
        this.selectedStackIndex = stackIndex;
    }
}