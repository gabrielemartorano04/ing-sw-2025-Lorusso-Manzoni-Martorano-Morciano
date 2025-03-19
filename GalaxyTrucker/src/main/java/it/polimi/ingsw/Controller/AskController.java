package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;

public class AskController {
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
        //dato un trucker e un numero di umani numHumans che dovrebbe perdere
        // il trucker deve dire se è vantaggioso o meno
        return false;
    }

    public boolean askSmugglers(Trucker trucker){
        return false;
    }

    public boolean askAbandonedStation(Trucker trucker, ArrayList<Goods> goods){
        //il trucker deve dire se perdere giorni di volo in cambio di una lista di merci
        return false;
    }

    public boolean askPirates(Trucker trucker){
        //il trucker deve dire se è vantaggioso o meno perdere giorni in cambio di crediti cosmici
        return false;
    }

    public boolean askPlayerToLand(Trucker trucker){
        //il trucker deve dire se atterrare su un pianeta per acquisire le relative merci
        return false;
    }

    public int askPlayerToChoosePlanet(Trucker trucker, ArrayList<ArrayList<Goods>> planets, ArrayList<Boolean> landed ){
        //il trucker deve dire su quale pianeta vuole atterrare per acquisirne le merci
        return 0;
    }

    public boolean askSlavers(Trucker trucker){
        //il trucker deve dire se è vantaggioso o meno perdere giorni in cambio di crediti cosmici
        return false;
    }

    public boolean askUseBattery(){
        //il trucker deve dire se vuole utilizzare una batteria per attivare un doubleComponent
        return false;
    }

    public int askNumPlayer(){
        //chiede quanti giocatori parteciperanno al gioco
        return 0;
    }
}

