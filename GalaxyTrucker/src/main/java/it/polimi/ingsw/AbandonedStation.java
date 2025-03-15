package it.polimi.ingsw;
import java.util.List;


class AbandonedStation extends Card {
    private int days;
    private int numHuman;
    private boolean decisionTaken;
    private List<Goods> goods;

    public AbandonedStation(boolean trialFlight, int level) {
        super("Abandoned Station", "A derelict space station", trialFlight, level);
        this.decisionTaken = false;
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