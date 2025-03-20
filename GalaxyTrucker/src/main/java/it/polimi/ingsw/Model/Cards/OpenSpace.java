package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.List;

public class OpenSpace extends Card {
    //lista che contiene per ogni trucker la lista di coordinates dei motori doppi che vuole attivare
    //la lista esterna associa ogni puntatore a un trucker, quindi la lunghezza della lista esterna è quella di truckers.
    //mantiene l'ordine dei truckers nel fare questa associazione
    private ArrayList<ArrayList<int[]>> truckersCoordinates;

    public OpenSpace(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Open Space", "Empty vastness of space", trialFlight, level, frontImage, backImage);
    }

    public void setCoordinates(ArrayList<ArrayList<int[]>> truckersCoordinates) {
        this.truckersCoordinates = truckersCoordinates;
    }

    @Override
    public void cardEffect(Game game) {
        List<Trucker> truckers = game.getTruckerList();
        int[] enginePowerPerTrucker = new int[truckers.size()];
        int i = 0;
        for (Trucker trucker : truckers) {
            enginePowerPerTrucker[i] = trucker.getSpaceShip().getTotalEnginePower(truckersCoordinates.get(i));
            if(enginePowerPerTrucker[i] == 0){
                game.withdraw(trucker);
            }
            i++;
        }
        for (i = 0; i < enginePowerPerTrucker.length; i++) {
            game.moveForward(truckers.get(i), enginePowerPerTrucker[i]);
        }
    }
}

