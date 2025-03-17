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

}
