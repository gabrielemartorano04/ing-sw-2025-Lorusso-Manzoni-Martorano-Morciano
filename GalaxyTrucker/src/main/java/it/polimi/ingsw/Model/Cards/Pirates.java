package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.SpaceShip;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.List;


public class Pirates extends Card {
    private int credits;
    private int days;
    private int piratesPower;

    // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private Trucker winnerTrucker = null;
    private ArrayList<Trucker> defeatedTruckers;
    private ArrayList<int[]> shoots;
    // shoots è una lista dove direction.get(i) rappresenta l'i-esimo colpo in questa specifica carta Pirates
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da sx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    public Pirates(boolean trialFlight, int level, int credits, int days, int piratesPower, ArrayList<int[]> shoots ,String frontImage, String backImage) {
        super("Pirates", "Dangerous space raiders", trialFlight, level, frontImage, backImage);
        this.credits = credits;
        this.days = days;
        this.piratesPower = piratesPower;
        this.shoots = shoots;
    }

    public int getCredits() {
        return credits;
    }

    public int getDays() {
        return days;
    }

    public int getPiratesPower() {
        return piratesPower;
    }

    public ArrayList<int[]> getShoots() {
        return shoots;
    }

    public void setDefeatedTruckers(ArrayList<Trucker> defeatedTruckers) {
        this.defeatedTruckers = defeatedTruckers;
    }

    public void setWinnerTrucker(Trucker winnerTrucker) {
        this.winnerTrucker = winnerTrucker;
    }

    @Override
    public void cardEffect(Game game) {
        if (winnerTrucker != null) {
            winnerTrucker.addCosmicCredit(credits);
            game.moveBackward(winnerTrucker, days);
        }

        //colpisco le navi solo se c'è qualcuno da colpire
        if (defeatedTruckers.size() > 0) {
            for (int[] shoot : shoots) {
                int roll = game.rollDice();
                boolean isBig = (shoot[0] == 1);
                int direction = shoot[1];
                for (Trucker trucker : defeatedTruckers) {
                    SpaceShip ship = trucker.getSpaceShip();
                    if (ship.isHit(roll, direction)) {
                        if (isBig) {
                            // colpo grande non può essere stoppato quindi devo distruggere il componente
                            ship.destroyComponent(roll, direction);
                        } else {
                            // Un colpo piccolo può solo essere fermato con lo scudo
                            if (!ship.blockSmallShoot(direction, roll)) {
                                ship.destroyComponent(roll, direction);
                            }
                        }
                    }
                }
            }
        }
    }


}
