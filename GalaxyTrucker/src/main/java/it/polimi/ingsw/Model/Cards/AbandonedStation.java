package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Trucker;
import it.polimi.ingsw.Model.Components.*;

import java.util.ArrayList;
import java.util.List;


public class AbandonedStation extends Card {
    private int days;
    private int numHuman;
    private ArrayList<Goods> goods;

    //è il trucker che ha deciso di atterrare, è null se nessuno ha deciso di atterrare
    private Trucker truckerThatHasDecided = null;

    //coordinates sono le coordinate dei Load dove inserire le merci. Viene settato dal Card controller,
    // se è vuoto vuol dire che nessun giocatore ha voluto accettare la carta
    private ArrayList<int[]> coordinates;

    public AbandonedStation(boolean trialFlight, int level, ArrayList<Goods> goods, int days, int numHuman,String frontImage, String backIMage) {
        super("Abandoned Station", "A derelict space station", trialFlight, level, frontImage, backIMage);
        this.days = days;
        this.numHuman = numHuman;
        this.goods = goods;
    }

    public int getDays() {
        return days;
    }

    public int getNumHuman() {
        return numHuman;
    }

    public ArrayList<Goods> getGoods() {
        return goods;
    }

    public void setTruckerThatHasDecided(Trucker truckerThatHasDecided) {
        this.truckerThatHasDecided = truckerThatHasDecided;
    }

    public void setCoord(ArrayList<int[]> coord) {
        this.coordinates = coord;
    }

    @Override
    public void cardEffect(Game game) {
        truckerThatHasDecided.getSpaceShip().addGoods(goods,coordinates);
        game.moveBackward(truckerThatHasDecided, days);
    }
}