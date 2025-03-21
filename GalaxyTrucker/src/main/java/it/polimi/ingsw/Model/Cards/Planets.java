package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.SpaceShip;
import it.polimi.ingsw.Model.Trucker;

import java.lang.reflect.Array;
import java.util.ArrayList;

//
public class Planets extends Card {
    private ArrayList<ArrayList<Goods>> planets;
    private int daysLoss;

    // viene settato dal Card Controller e playersLanded[i] vale 1 se quel player è atterato se no 0
    private int[] playersLanded;


    // solita convenzione per salvarmi le coordinate di dove aggiungere le merci per ogni trucker
    private ArrayList<ArrayList<int[]>> truckersCoordinates;


    public Planets(boolean trialFlight, int daysLoss, int level, ArrayList<ArrayList<Goods>> planets, String frontImage, String backImage) {
        super("Planets", "A new planetary system discovered", trialFlight, level, frontImage, backImage);
        this.daysLoss = daysLoss;
        this.planets = planets;
    }

    public ArrayList<ArrayList<Goods>> getPlanets() {
        return planets;
    }

    public void setTruckersCoordinates(ArrayList<ArrayList<int[]>> truckersCoordinates) {
        this.truckersCoordinates = truckersCoordinates;
    }

    public void setPlayersLanded(int[] playersLanded) {
        this.playersLanded = playersLanded;
    }

    @Override
    public void cardEffect(Game game) {
        int i = 0;
        ArrayList<Trucker> truckers = game.getTruckerList();

        // riempo le merci a tutti tanto chi non è atterrato su un pianeta o non ha voluto caricare nessuna merce
        // avrà truckersCoordinates.get(i) come lista vuota e addGoods non fa nulla
        for(Trucker trucker : truckers){
            trucker.getSpaceShip().addGoods(planets.get(i), truckersCoordinates.get(i));
            i++;
        }

        // muovo in dietro i giocatori ma partendo dal più indietro nella plancia non dal Leader
        for(i = playersLanded.length - 1; i>=0; i--){
            if(playersLanded[i] == 1){
                game.moveBackward(truckers.get(i), daysLoss);
            }
        }

    }
}