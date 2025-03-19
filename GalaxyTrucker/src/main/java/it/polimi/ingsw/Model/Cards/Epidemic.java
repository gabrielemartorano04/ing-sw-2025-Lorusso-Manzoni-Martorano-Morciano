package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Game;
import it.polimi.ingsw.GameController;
import it.polimi.ingsw.Trucker;

import java.util.List;

public class Epidemic extends Card {
    public Epidemic(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Epidemic", "A contagious outbreak", trialFlight, level, frontImage, backImage);
    }
    @Override
    public void cardEffect(Game game, GameController controller) {
        List <Trucker> truckers = game.getTruckerList();
        for(Trucker trucker: truckers) {
            trucker.getSpaceShip().epidemic();
        }
    }
}
