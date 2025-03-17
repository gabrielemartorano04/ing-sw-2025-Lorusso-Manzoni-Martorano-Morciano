package it.polimi.ingsw;

import java.util.List;

//secondo me tutti i get sono inutil

class Smugglers extends Card {
    private int smugglersPower;
    private int goodsLost;
    private int days;
    private Goods[] goodsGained = new Goods[3];
    private boolean isDefeated = false;

    public Smugglers(boolean trialFlight, int smugglersPower, int goodsLost, int days, Goods[] goodsGained, int level, String frontName, String backName) {
        super("Smugglers", "Illegal traders of goods", trialFlight, level, frontName, backName);
        this.smugglersPower = smugglersPower;
        this.goodsLost = goodsLost;
        this.days = days;
        this.goodsGained = goodsGained;
    }

    public int getSmugglersPower() {
        return smugglersPower;
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
                if(trucker.getSpaceShip().getTotalCannonPower(controller)> smugglersPower){
                    isDefeated = true;
                    boolean answer = controller.ask(trucker);
                    if (answer == true){
                        trucker.getSpaceShip().addGoods(goodsGained);
                        game.moveTrucker(trucker, days);
                    }
                } else if (trucker.getSpaceShip().getTotalCannonPower(controller) < smugglersPower){
                    trucker.getSpaceShip().removeGoods(goodsLost);
                }
            }
        }
        isDefeated = false;
    }
}
