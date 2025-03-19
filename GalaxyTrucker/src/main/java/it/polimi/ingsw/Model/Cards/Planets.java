package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.SpaceShip;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;

//
public class Planets extends Card {
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
        for (Trucker trucker : truckers) {
            SpaceShip ship = trucker.getSpaceShip();
            if (controller.askPlayerToLand(trucker)) {
                int planetIndex;
                planetIndex = controller.askPlayerToChoosePlanet(trucker, planets, landed);
                if (planetIndex != -1 && !landed.get(planetIndex)) {  //assumo che askPlayerToChoosePlanet ritorni -1 se la risposta è falso
                    landed.set(planetIndex, true);
                    game.moveBackward(trucker, daysLoss);
                    ship.addGoods(getPlanet().get(planetIndex), controller);
                }
            }
        }
    }
}