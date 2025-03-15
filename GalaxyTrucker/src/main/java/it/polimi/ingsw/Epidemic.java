package it.polimi.ingsw;
import java.util.List;

class Epidemic extends Card {
    public Epidemic(boolean trialFlight, int level) {
        super("Epidemic", "A contagious outbreak", trialFlight, level);
    }
    @Override
    public void cardEffect(Game game, GameController controller) {
        List <Trucker> truckers = game.getTruckerList();
        for(Trucker trucker: truckers) {
            trucker.getSpaceShip().epidemic();
        }
    }
}
