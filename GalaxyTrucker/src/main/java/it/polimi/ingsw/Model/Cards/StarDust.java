package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Trucker;

import java.util.*;

public class StarDust extends Card {
    public StarDust(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Star Dust", "A cloud of cosmic dust", trialFlight, level, frontImage, backImage);
    }

    @Override
    public void cardEffect(Game game) {
        List<Trucker> truckers = game.getTruckerList();
        Collections.reverse(truckers);
        for (Trucker trucker : truckers) {
            int exposedConnectors = trucker.getSpaceShip().countExposedConnectors();
            game.moveBackward(trucker, exposedConnectors);
        }
        Collections.reverse(truckers); //rigiro i Truckers così ritorno all'ordine originale della lista
    }
}
