package it.polimi.ingsw;

import java.util.ArrayList;

public class GameController {
    public int askActiveComponents(int doubleC){
        //il controller chiedere quanti componenti doppi vuole attivare sapendo che ne ha doubleC
        return 0; //return componenti che vuole attivare
    }
    public ArrayList<int[]> askComponentBattery(){
        return null;
        //il controller chiede i componenti di tipo batteryStorage da cui vogliamo togliere le batterie
    }
    public ArrayList<int[]> askComponentGoods(){
        return null;
        //il controller chiede i componenti di tipo Load in cui vogliamo mettere le batterie
    }
    public ArrayList<int[]> askCabin(){
        return null;
    }
    public boolean askAbandonedShip(Trucker trucker, int numHuman){
        //dato un trucker e un numero di umani che numHumans che dovrebber perdere
        // il trucker deve dire se è vantaggioso o meno
        return false;
    }

    public boolean askSmugglers(Trucker trucker){
        return false;
    }

}
