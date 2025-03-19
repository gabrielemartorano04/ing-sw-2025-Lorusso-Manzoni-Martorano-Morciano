package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.List;


public class AbandonedShip extends Card {
    private int days;
    private int credits;
    private int numHuman;

    //è il trucker che ha deciso di atterrare, è null se nessuno ha deciso di atterrare
    private Trucker truckerThatHasDecided = null;

    //è la lista di coordinate da cui rimuovere gli umani che è settata dal controller
    private ArrayList<int[]> coord;

    public AbandonedShip(boolean trialFlight, int level, int numHuman, int credits, int days, String forntImage, String backImage) {
        super("Abandoned Ship", "A drifting ghost ship",  trialFlight, level, forntImage, backImage);
        this.numHuman = numHuman;
        this.credits = credits;
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public int getCredits() {
        return credits;
    }

    public int getNumHuman() {
        return numHuman;
    }

    public void setTruckerThatHasDecided(Trucker truckerThatHasDecided) {
        this.truckerThatHasDecided = truckerThatHasDecided;
    }

    public void setCoord(ArrayList<int[]> coord) {
        this.coord = coord;
    }

    @Override
    public void cardEffect(Game game) {
        if (truckerThatHasDecided != null) {
            truckerThatHasDecided.getSpaceShip().removeNumHuman(numHuman,coord);
            game.moveBackward(truckerThatHasDecided,days);
            truckerThatHasDecided.addCosmicCredit(credits);
        }
    }
}
