package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Trucker;

import java.util.List;

public class Epidemic extends Card {
    public Epidemic(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Epidemic", "A contagious outbreak", trialFlight, level, frontImage, backImage);
    }
    @Override
    public void cardEffect(Game game) {
        List <Trucker> truckers = game.getTruckerList();
        for(Trucker trucker: truckers) {
            trucker.getSpaceShip().epidemic();
        }
    }
}
