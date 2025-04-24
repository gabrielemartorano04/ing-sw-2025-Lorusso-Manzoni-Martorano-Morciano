package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;

import java.util.ArrayList;

/**
 *
 */
public class TruckerController {
    private final Trucker trucker;
    private final GameController gameController;

    public TruckerController(Trucker trucker, GameController gameController) {
        this.trucker = trucker;
        this.gameController = gameController;
    }

    public void handleInput(String command, Object... params) {
        try {
            trucker.chooseWhatToDo(command);
            switch (command) {
                case "Select Component" -> {
                    int index = (Integer) params[0];
                    Component c = gameController.selectComponent(trucker, index);
                }
                case "Keep" -> {
                    Component comp = (Component) params[0];
                    gameController.keepComponent(trucker, comp);
                }
                case "Link" -> {
                    Component comp = (Component) params[0];
                    int x = (int) params[1];
                    int y = (int) params[2];
                    gameController.linkComponent(trucker, comp, x, y);
                }
                case "Turn Hourglass" -> {
                    gameController.turnHourglass(trucker);
                }
                case "Show Stack" -> {
                    int stackIndex = (Integer) params[0];
                    ArrayList<Card> cards = gameController.showStack(trucker, stackIndex);
                }
                case "End" -> {
                    gameController.endBuildingFor(trucker);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error");
        }
    }

}
