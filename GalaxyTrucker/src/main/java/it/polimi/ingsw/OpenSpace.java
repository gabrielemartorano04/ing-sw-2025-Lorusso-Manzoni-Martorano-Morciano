package it.polimi.ingsw;
import java.util.List;

class OpenSpace extends Card {
    public OpenSpace(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Open Space", "Empty vastness of space", trialFlight, level, frontImage, backImage);
    }

    @Override
    public void cardEffect(Game game, GameController controller) {
        List<Trucker> truckers = game.getTruckerList();
        int[] daysGained = new int[truckers.size()];
        int i = 0;
        for (Trucker trucker : truckers) {
            daysGained[i] = trucker.getSpaceShip().getTotalEnginePower();
            if(daysGained[i] == 0){
                game.withdraw(trucker);
            }
            i++;
        }
        for (i = 0; i < daysGained.length; i--) {
            game.moveTrucker(truckers.get(i), daysGained[i]);
        }
    }
}

