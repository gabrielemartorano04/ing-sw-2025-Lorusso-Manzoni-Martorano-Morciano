package it.polimi.ingsw;
import java.util.List;
//
class Planets extends Card {
    private List<List<Goods>> planets;
    private int daysLoss;

    public Planets(boolean trialFlight, int daysLoss, int level) {
        super("Planets", "A new planetary system discovered", trialFlight, level);
        this.daysLoss = daysLoss;
    }

    public List<List<Goods>> getPlanet() {
        return planets;
    }
    @Override
    public void cardEffect(Game game, GameController controller) {

    }
}