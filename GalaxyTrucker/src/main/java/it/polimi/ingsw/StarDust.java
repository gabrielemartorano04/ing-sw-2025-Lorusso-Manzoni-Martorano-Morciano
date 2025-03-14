package it.polimi.ingsw;
import java.util.*

class StarDust extends Card {
    public StarDust(boolean trialFlight) {
        super("Star Dust", "A cloud of cosmic dust", trialFlight);
    }

    @Override
    public void cardEffect(Game game) {
        List<Trucker> truckers = game.getTruckerList();
        Collections.reverse(truckers);
        for (Trucker trucker : truckers) {
            int exposedConnectors = trucker.getSpaceShip().countExposedConnectors();
            game.moveTrucker(trucker, exposedConnectors);
        }
        Collections.reverse(truckers); //rigiro i Truckers così
    }
    
}
