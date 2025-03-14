package it.polimi.ingsw;
import java.util.ArrayList;
import java.util.List;


class MeteorShower extends Card {
    private List<int[]> direction;
    // direction è una lista dove direction.get(i) rappresenta l'i-esimo meteorite in questa specifica MeteorShower
    // direction.get(i)[0] vale 1 se il meteorite è grande 0 se è piccolo
    // direction.get(i)[1] indice la direzione da cui arriva il meteorite (1 = dall'alto, 2 = da sx, 3 = dal basso, 4 = da dx)
    // direction è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    public MeteorShower(boolean trialFlight, List<int[]> direction) {
        super("Meteor Shower", "A dangerous meteor storm", trialFlight);
        this.direction = direction;
    }

    public List<int[]> getDirection() {
        return direction;
    }

    @Override
    public void cardEffect(Game game, GameController controller) {
        List<Trucker> truckers = game.getTruckerList();
        for (int[] meteor : direction) {
            boolean isBig = (meteor[0] == 1);
            int direction = meteor[1];
            for (Trucker trucker : truckers) {
                SpaceShip ship = trucker.getSpaceship();
                int roll = game.rollDice(); // tiro dei dadi mi aspetto che sia in Game

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

