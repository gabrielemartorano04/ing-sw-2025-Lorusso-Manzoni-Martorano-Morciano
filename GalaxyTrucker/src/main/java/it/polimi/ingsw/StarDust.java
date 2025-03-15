package it.polimi.ingsw;
import java.util.*;

class StarDust extends Card {
    public StarDust(boolean trialFlight, int level) {
        super("Star Dust", "A cloud of cosmic dust", trialFlight, level);
    }

    @Override
    public void cardEffect(Game game, GameController controller) {
        List<Trucker> truckers = game.getTruckerList();
        Collections.reverse(truckers);
        for (Trucker trucker : truckers) {
            int exposedConnectors = trucker.getSpaceShip().countExposedConnectors();
            game.moveTrucker(trucker, exposedConnectors);
        }
        Collections.reverse(truckers); //rigiro i Truckers così
    }
    
}
