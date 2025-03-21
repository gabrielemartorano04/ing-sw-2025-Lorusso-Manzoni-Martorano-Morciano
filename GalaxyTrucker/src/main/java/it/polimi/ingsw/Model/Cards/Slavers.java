package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.List;

public class Slavers extends Card {
    private int slaversPower;
    private int credits;
    private int days;
    private int astronautsLost;

    // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private Trucker winnerTrucker = null;
    private ArrayList<Trucker> defeatedTruckers;

    // contiene le coordinate delle cabine scelte dall'utente per rimuovere gli umani. Queste coordinate sono organizzate
    // in liste di liste. La lista esterna è una per ogni trucker, la lista interna contiene la lista di coordinate per quel
    // trucker, mantenute nell'ordine dei trucker.
    private ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveHumans;

    public Slavers(boolean trialFlight, int level, int credits, int days, int astronautsLost, int slaversPower, String frontImage, String backImage ) {
        super("Slavers", "Intergalactic slave traders", trialFlight, level, frontImage, backImage);
        this.credits = credits;
        this.days = days;
        this.astronautsLost = astronautsLost;
        this.slaversPower = slaversPower;
    }

    public int getSlaversPower() {
        return slaversPower;
    }

    public int getAstronautsLost() {
        return astronautsLost;
    }

    public int getDays() {
        return days;
    }

    public int getCredits() {
        return credits;
    }

    public void setDefeatedTruckers(ArrayList<Trucker> defeatedTruckers) {
        this.defeatedTruckers = defeatedTruckers;
    }

    public void setWinnerTrucker(Trucker winnerTrucker) {
        this.winnerTrucker = winnerTrucker;
    }

    public void setTruckersCoordinatesToRemoveHumans(ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveHumans) {
        this.truckersCoordinatesToRemoveHumans = truckersCoordinatesToRemoveHumans;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        int i = 0;
        if(winnerTrucker!=null){
            winnerTrucker.addCosmicCredit(credits);
            game.moveBackward(winnerTrucker, days);
        }
        if(!defeatedTruckers.isEmpty()) {
            for (Trucker trucker : defeatedTruckers) {
                trucker.getSpaceShip().removeNumHuman(astronautsLost, truckersCoordinatesToRemoveHumans.get(i));
                i++;
            }
        }
    }

}