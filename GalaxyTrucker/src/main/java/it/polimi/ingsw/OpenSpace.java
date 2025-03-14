package it.polimi.ingsw;
import java.util.List;

class OpenSpace extends Card {
    public OpenSpace(boolean trialFlight) {
        super("Open Space", "Empty vastness of space", trialFlight);
    }

    @Override
    public void cardEffect(Game game, GameController controller) {
        List<Trucker> truckers = game.getTruckerList();
        int[] daysGained = new int[truckers.size()];
        int i = 0;
        for (Trucker trucker : truckers) {
            daysGained[i] = trucker.getSpaceShip().getEnginePower();
            i++;
        }
        for (i = 0; i < daysGained.length; i--) {
            game.moveTrucker(truckers.get(i), daysGained[i]);
        }
    }
}

