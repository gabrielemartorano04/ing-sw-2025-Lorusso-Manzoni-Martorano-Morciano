package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Game;
import it.polimi.ingsw.GameController;
import it.polimi.ingsw.Trucker;

import java.util.*;

public class StarDust extends Card {
    public StarDust(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Star Dust", "A cloud of cosmic dust", trialFlight, level, frontImage, backImage);
    }

    @Override
    public void cardEffect(Game game, GameController controller) {
        List<Trucker> truckers = game.getTruckerList();
        Collections.reverse(truckers);
        for (Trucker trucker : truckers) {
            int exposedConnectors = trucker.getSpaceShip().countExposedConnectors();
            game.moveBackward(trucker, exposedConnectors);
        }
        Collections.reverse(truckers); //rigiro i Truckers così
    }
    
}
