package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;

public class AskController {
    public int askActiveComponents(int doubleC){
        //il controller chiedere quanti componenti doppi vuole attivare sapendo che ne ha doubleC
        return 0; //return componenti che vuole attivare
    }

    public ArrayList<int[]> askDoubleEnginesToActivate(){
        //chiede all'utente quali motori doppi vuole attivare e ritorna la lista di coordinate dei cannoni da attivare
        ArrayList<int[]> coordinates = new ArrayList<>();
        return coordinates;
    }

    public ArrayList<int[]> askDoubleCannonToActivate(){
        //chiede all'utente quali cannoni doppi vuole attivare e ritorna la lista di coordinate dei cannoni da attivare
        ArrayList<int[]> coordinates = new ArrayList<>();
        return coordinates;
    }

    public ArrayList<int[]> askBatteryStorageToRemoveFrom(int numOfBatteriesToEliminate){
        //il controller chiede i componenti di tipo batteryStorage da cui vogliamo togliere le batterie e anche quante batterie
        //rimuovere da ognuno di essi
        // in questo caso quindi batteryCoordinates sarà una lista di array di dimensione 3:
        // i primi due elementi di ogni array sono le coordinate del Battery storage da cui eliminare le batterie
        // mentre il terzo elemento indica quante batterie eliminare da quell'elemento

        //io passerò il numero di batterie totali tramite il CardController calcolando il numero di componenti che sono stati attivati
        // poi dentro questa funzione andrà controllato che la somma di batteryCoordinates.get(i)[3] sia uguale a numOfBatteriesToEliminate
        ArrayList<int[]> batteryCoordinates = new ArrayList<>();
        return batteryCoordinates;
    }

    public ArrayList<int[]> askComponentGoods(){
        return null;
        //il controller chiede i componenti di tipo Load in cui vogliamo mettere le batterie
    }
    public ArrayList<int[]> askCabin(){
        return null;
    }

    public ArrayList<int[]> askAbandonedShip(Trucker trucker, int numHuman){
        //dato un trucker e un numero di umani numHumans che dovrebbe perdere
        //un trucker decide se perdere gli umani, in tal caso fornisce le coordinate delle cabine da cui rimuoverle,
        // se invece nessun trucker vuole accettare la carta coord sarà una lista vuota
        ArrayList<int[]> coord = new ArrayList<>();
        return coord;
    }

    public boolean askSmugglers(Trucker trucker){
        return false;
    }

    public ArrayList<int[]> askAbandonedStation(Trucker trucker, ArrayList<Goods> goods){
        //un trucker deve dire se perdere giorni di volo in cambio di una lista di merci
        //se decide di farlo indica le coordinate dei Load dove inserire le merci, altrimenti se nessuno vuol coord sarà vuota
        ArrayList<int[]> coord = new ArrayList<>();
        return coord;
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

