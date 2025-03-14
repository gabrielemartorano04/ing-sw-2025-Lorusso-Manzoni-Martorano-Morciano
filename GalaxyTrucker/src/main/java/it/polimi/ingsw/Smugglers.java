package it.polimi.ingsw;

import java.util.List;

//secondo me tutti i get sono inutili

class Smugglers extends Card {
    private int cannonRequired;
    private int goodsLost;
    private int days;
    private Goods[] goodsGained = new Goods[3];
    private boolean isDefeated = false;

    public Smugglers(boolean trialFlight, int cannonRequired, int goodsLost, int days, Goods[] goodsGained) {
        super("Smugglers", "Illegal traders of goods", trialFlight);
        this.cannonRequired = cannonRequired;
        this.goodsLost = goodsLost;
        this.days = days;
        this.goodsGained = goodsGained;
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

    @Override
    public void cardEffect(Game game, GameController controller) {
        List<Trucker> truckers = game.getTruckerList();
        for(Trucker trucker: truckers) {
            if(isDefeated){
                break;
            }
            else{
                if(trucker.getSpaceShip().getTotalEnginePower()>cannonRequired){
                    isDefeated = true;

                }
            }
        }
    }
}
