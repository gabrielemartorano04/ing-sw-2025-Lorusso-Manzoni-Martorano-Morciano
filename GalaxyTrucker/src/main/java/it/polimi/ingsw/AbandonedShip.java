package it.polimi.ingsw;

import java.util.List;


class AbandonedShip extends Card {
    private int days;
    private int credits;
    private int numHuman;
    private boolean decisionTaken;

    public AbandonedShip(boolean trialFlight, boolean decisionTaken) {
        super("Abandoned Ship", "A drifting ghost ship",  trialFlight);
        this.decisionTaken = false;
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
                if (controller.ask(trucker, numHuman) == true){
                    decisionTaken = true;
                    trucker.getSpaceShip().removeHuman(numHuman);
                    game.moveTrucker(trucker,days);
                    trucker.addCredit(credits);
                }
            }
        }
        decisionTaken = false;
    }
}
