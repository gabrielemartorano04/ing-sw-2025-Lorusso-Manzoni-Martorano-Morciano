package it.polimi.ingsw;

import java.util.List;


class AbandonedShip extends Card {
    private int days;
    private int credits;
    private int numHuman;
    private boolean decisionTaken = false;

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

    @Override
    public void cardEffect(Game game, GameController controller) {
        while (!decisionTaken) {
            List<Trucker> truckers = game.getTruckerList();
            for(Trucker trucker: truckers) {
                if (controller.askAbandonedShip(trucker, numHuman) == true){
                    decisionTaken = true;
                    trucker.getSpaceShip().removeNumHuman(numHuman,controller);
                    game.moveBackward(trucker,days);
                    trucker.addCosmicCredit(credits);
                }
            }
        }
        decisionTaken = false;
    }
}
