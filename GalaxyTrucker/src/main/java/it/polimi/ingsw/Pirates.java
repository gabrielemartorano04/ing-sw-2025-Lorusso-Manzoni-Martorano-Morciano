package it.polimi.ingsw;

class Pirates extends Card {
    private int credits;
    private int days;
    private int cannonRequired;
    private boolean[] shoots = new boolean[3];

    public Pirates(boolean trialFlight, int level) {
        super("Pirates", "Dangerous space raiders", trialFlight, level);
    }

    public int getCredits() {
        return credits;
    }

    public int getDays() {
        return days;
    }

    public int getCannonRequired() {
        return cannonRequired;
    }

    public boolean[] getShoots() {
        return shoots;
    }

    @Override
    public void cardEffect(Game game, GameController controller) {

    }

}
