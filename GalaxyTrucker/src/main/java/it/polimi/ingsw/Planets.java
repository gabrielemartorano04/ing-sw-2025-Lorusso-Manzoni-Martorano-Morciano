package it.polimi.ingsw;
import java.util.ArrayList;
import java.util.List;
//
class Planets extends Card {
    private List<List<Goods>> planets;
    private int daysLoss;

    public Planets(boolean trialFlight, int daysLoss, int level, List<List<Goods>> planets, String frontImage, String backImage) {
        super("Planets", "A new planetary system discovered", trialFlight, level, frontImage, backImage);
        this.daysLoss = daysLoss;
        this.planets = planets;
    }

    public List<List<Goods>> getPlanet() {
        return planets;
    }

    @Override
    public void cardEffect(Game game, GameController controller) {
        List<Trucker> truckers = game.getTruckerList();
        List<Boolean> landed = new ArrayList<>(truckers.size());
        for (int i = 0; i < truckers.size(); i++) {
            landed.add(false);
        }
        for (Trucker trucker : truckers) {
            SpaceShip ship = trucker.getSpaceShip();
            if (controller.askPlayerToLand(trucker)) {
                int planetIndex = controller.askPlayerToChoosePlanet(trucker, planets);
                if (planetIndex != -1) {  //assumo che askPlayerToChoosePlanet ritorni -1 se la risposta è falso
                    landed.set(planetIndex, true);
                    game.moveTrucker(trucker, daysLoss);
                    ship.addGoods(getPlanet().get(planetIndex));
                }
            }
        }

    }
}