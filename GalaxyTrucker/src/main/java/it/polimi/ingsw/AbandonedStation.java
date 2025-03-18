package it.polimi.ingsw;
import java.util.ArrayList;
import java.util.List;


class AbandonedStation extends Card {
    private int days;
    private int numHuman;
    private boolean decisionTaken;
    private ArrayList<Goods> goods;

    public AbandonedStation(boolean trialFlight, int level, ArrayList<Goods> goods, int days, int numHuman,String frontImage, String backIMage) {
        super("Abandoned Station", "A derelict space station", trialFlight, level, frontImage, backIMage);
        this.decisionTaken = false;
        this.days = days;
        this.numHuman = numHuman;
        this.goods = goods;
    }

    public int getDays() {
        return days;
    }

    public int getNumHumans() {
        return numHuman;
    }

    public List<Goods> getGoods() {
        return goods;
    }
    @Override
    public void cardEffect(Game game, GameController controller) {
        while (!decisionTaken){
            List<Trucker> truckers = game.getTruckerList();
            for(Trucker trucker: truckers) {
                if (trucker.getSpaceShip().getNumHuman() > numHuman) {
                    if (controller.ask(trucker, goods) == true) {
                        decisionTaken = true;
                        trucker.getSpaceShip().addGoods(goods);
                        game.moveTrucker(trucker, days);
                    }
                }
            }
        }
        decisionTaken = false;
    }
}