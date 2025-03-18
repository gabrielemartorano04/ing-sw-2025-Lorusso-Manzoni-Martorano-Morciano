package it.polimi.ingsw;
import java.util.ArrayList;
import java.util.List;
//
class Planets extends Card {
    private ArrayList<ArrayList<Goods>> planets;
    private int daysLoss;

    public Planets(boolean trialFlight, int daysLoss, int level, ArrayList<ArrayList<Goods>> planets, String frontImage, String backImage) {
        super("Planets", "A new planetary system discovered", trialFlight, level, frontImage, backImage);
        this.daysLoss = daysLoss;
        this.planets = planets;
    }

    public ArrayList<ArrayList<Goods>> getPlanet() {
        return planets;
    }

    @Override
    public void cardEffect(Game game, GameController controller) {
        ArrayList<Trucker> truckers = game.getTruckerList();
        ArrayList<Boolean> landed = new ArrayList<>(truckers.size());
        for (int i = 0; i < truckers.size(); i++) {
            landed.add(false);
        }
        //devo aggiungere che non puoi atterrare su un pianeta già occupato
        for (Trucker trucker : truckers) {
            SpaceShip ship = trucker.getSpaceShip();
            if (controller.askPlayerToLand(trucker)) {
                int planetIndex = controller.askPlayerToChoosePlanet(trucker, planets);
                if (planetIndex != -1 && !landed.get(planetIndex)) {  //assumo che askPlayerToChoosePlanet ritorni -1 se la risposta è falso
                    landed.set(planetIndex, true);
                    game.moveBackward(trucker, daysLoss);
                    ship.addGoods(getPlanet().get(planetIndex), controller);
                }
            }
        }

    }
}