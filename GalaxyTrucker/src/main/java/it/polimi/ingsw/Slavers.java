package it.polimi.ingsw;

import java.util.List;

class Slavers extends Card {
    private int slaversPower;
    private int credits;
    private int days;
    private int astronautsLost;
    private boolean isDefeated = false;

    public Slavers(boolean trialFlight, int level) {
        super("Slavers", "Intergalactic slave traders", trialFlight, level);
    }

    public int getSlaversPower() {
        return slaversPower;
    }

    public int getAstronautsLost() {
        return astronautsLost;
    }

    public int getDays() {
        return days;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public void cardEffect(Game game, GameController controller) {
        List<Trucker> truckers = game.getTruckerList();
        for(Trucker trucker: truckers) {
            if(isDefeated){
                break;
            }
            else{
                if(trucker.getSpaceShip().getTotalCannonPower(controller)> slaversPower){
                    isDefeated = true;
                    boolean answer = controller.ask(trucker);
                    if (answer == true){
                        trucker.addCosmicCredit(credits);
                        game.moveTrucker(trucker, days);
                    }
                } else if (trucker.getSpaceShip().getTotalCannonPower(controller) < slaversPower){
                    trucker.getSpaceShip().removeAstronauts(astronautsLost);
                }
            }
        }
        isDefeated = false;
    }
}