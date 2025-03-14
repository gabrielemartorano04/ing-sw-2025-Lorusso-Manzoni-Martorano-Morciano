package it.polimi.ingsw;

class Slavers extends Card {
    private int cannonRequired;
    private int credits;
    private int days;
    private int astronautsLost;

    public Slavers(boolean trialFlight) {
        super("Slavers", "Intergalactic slave traders", trialFlight);
    }

    public int getCannonRequired() {
        return cannonRequired;
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
}