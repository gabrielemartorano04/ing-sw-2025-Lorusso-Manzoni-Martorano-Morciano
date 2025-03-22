package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.SpaceShip;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages the single card meteor shower
 * In the order of representation of the meteors on the card, the leader roll the dices for each meteor to determinate the row or column in which the meteor will hit.
 * If it's a small meteor, the trucker doesn't have to worry if he/she has a well assembled ship, otherwise can use a battery to activate a shield oriented in that direction to protect the ship.
 * If it's a big meteor, the trucker can use, or activate if is double, a cannon to destroy the meteor; a cannon protects only one column or 3 row (his own and the 2 adjacent ones) if rotated;
 */
public class MeteorShower extends Card {
    private final ArrayList<int[]> direction;
    // direction è una lista dove direction.get(i) rappresenta l'i-esimo meteorite in questa specifica MeteorShower
    // direction.get(i)[0] vale 1 se il meteorite è grande 0 se è piccolo
    // direction.get(i)[1] indice la direzione da cui arriva il meteorite (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da sx)
    // direction è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso


    /**
     * Meteor shower constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param direction list of directions of the meteors
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public MeteorShower(boolean trialFlight, ArrayList<int[]> direction, int level, String frontImage, String backImage) {
        super("Meteor Shower", "A dangerous meteor storm", trialFlight, level, frontImage, backImage);
        this.direction = direction;
    }

    /**
     * Returns the list of arrays indicating the direction of each meteor.
     * Each position of the array represents a direction:
     * position 0 = top
     * position 1 = right side
     * position 2 = bottom
     * position 3 = left side
     * @return list of direction
     */
    public ArrayList<int[]> getDirection() {
        return direction;
    }


    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        //per ora non c'è perché è tutto nel Card Controller
    }
}

