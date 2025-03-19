package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Game;
import it.polimi.ingsw.GameController;
import it.polimi.ingsw.SpaceShip;
import it.polimi.ingsw.Trucker;

import java.util.ArrayList;
import java.util.List;


public class MeteorShower extends Card {
    private ArrayList<int[]> direction;
    // direction è una lista dove direction.get(i) rappresenta l'i-esimo meteorite in questa specifica MeteorShower
    // direction.get(i)[0] vale 1 se il meteorite è grande 0 se è piccolo
    // direction.get(i)[1] indice la direzione da cui arriva il meteorite (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da sx)
    // direction è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    public MeteorShower(boolean trialFlight, ArrayList<int[]> direction, int level, String frontImagae, String backImage) {
        super("Meteor Shower", "A dangerous meteor storm", trialFlight, level, frontImagae, backImage);
        this.direction = direction;
    }

    public ArrayList<int[]> getDirection() {
        return direction;
    }

    @Override
    public void cardEffect(Game game, GameController controller) {
        List<Trucker> truckers = game.getTruckerList();
        for (int[] meteor : direction) {
            boolean isBig = (meteor[0] == 1);
            int direction = meteor[1];
            int roll = game.rollDice(); // tiro dei dadi mi aspetto che sia in Game
            for (Trucker trucker : truckers) {
                SpaceShip ship = trucker.getSpaceShip();
                if (ship.isHit(roll, direction)) {
                    if (isBig) {
                        // qui assumo che il meteorite grande possa essere distrutto da un cannone ma è da rivedere
                        if (!ship.shootMeteor(direction, roll)) {
                            ship.destroyComponent(roll, direction);
                        }
                    } else {
                        // Un meteorite piccolo può rimbalzare o essere fermato dallo scudo
                        if (!ship.blockSmallMeteor(direction, roll)) {
                            ship.destroyComponent(roll, direction);
                        }
                    }
                }
            }
        }
    }
}

