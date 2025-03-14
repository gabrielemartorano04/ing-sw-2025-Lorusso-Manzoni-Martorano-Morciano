package it.polimi.ingsw;

class Smugglers extends Card {
    private int cannonRequired;
    private int goodsLost;
    private int days;
    private Goods[] goodsGained = new Goods[3];

    public Smugglers(boolean trialFlight) {
        super("Smugglers", "Illegal traders of goods", trialFlight);
    }

    public int getCannonRequired() {
        return cannonRequired;
    }

    public int getGoodsLost() {
        return goodsLost;
    }

    public Goods[] getGoodsGained() {
        return goodsGained;
    }

    public int getDays() {
        return days;
    }
}
