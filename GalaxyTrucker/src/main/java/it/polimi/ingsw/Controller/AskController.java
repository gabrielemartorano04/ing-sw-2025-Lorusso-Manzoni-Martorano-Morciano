package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.PlayerColors;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;

public class AskController {

    public boolean askToUseShield(Trucker trucker, int roll, int direction){
        // chiede all'utente (solo se si tratta di un colpo piccolo o di un meteorite piccolo)
        // se vuole attivare uno scudo per proteggersi

        boolean answer = false;
        return answer;
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

    public ArrayList<int[]> askWhereToAddGoods(Trucker trucker, Goods[] goods){
        return new ArrayList<>();
        //il controller chiede i componenti di tipo Load in cui vogliamo mettere le merci
        // POTREBBE ANCHE RETURNARE UN VETTORE VUOTO MA NON DEVE POTER RETURNARE NULL
    }

    public ArrayList<int[]> askWhereToAddGoodsList(Trucker trucker, ArrayList<Goods> goods){
        return new ArrayList<>();
        //il controller chiede i componenti di tipo Load in cui vogliamo mettere le merci
        // POTREBBE ANCHE RETURNARE UN VETTORE VUOTO MA NON DEVE POTER RETURNARE NULL

        // fa la stessa cosa del WhereToAddGoods ma prende in ingresso una lista invece che un vettore
        // SAREBBE MEGLIO UNIRLE CAMBIANDO LA DEFINIZIONE DI COME MEMORIZZO LE MERCI DENTRO SMUGGLERS MA IN TANTO
        // TENGO COSI
    }

    public ArrayList<int[]> askWhereToRemoveGoods(Trucker trucker, int goodsLost){
        return null;
        //il controller chiede i componenti di tipo Load in cui vogliamo rimuovere le merci
    }

    public ArrayList<int[]> askCabinToRemovePeople(Trucker trucker, int numPeople){
        //dato un trucker e un numero di umani numHumans che dovrebbe perdere
        //un trucker decide se perdere gli umani, in tal caso fornisce le coordinate delle cabine da cui rimuoverle,
        // se invece nessun trucker vuole accettare la carta coord sarà una lista vuota

        //coord sarà del tipo coord.get(i)[0] = coordinata x della cabina, coord.get(i)[1] = coordinata y della cabina
        // coord.get(i)[2] = numero di umani da rimuovere in quella cabina
        ArrayList<int[]> coord = new ArrayList<>();
        return coord;
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

    public int askPlanets(Trucker trucker, ArrayList<ArrayList<Goods>> planets, int[] planetsLanded){
        //restituisce l'indice del pianeta scelto tra i planets, se il player non ha scelto nessun pianeta restituisce -1
        // Per controllare se il pianeta è già stato scelto
        // si usa il vettore planetesLanded dove planetsLanded[i] = 1 se il pianeta i-esimo è stato scelto
        // funziona perché planetsLanded è della stessa lunghezza di planets

        // MI ASPETTO CHE VENGA FATTO SCEGLIERE SOLO TRA I PIANETI DOVE planetsLanded[i] VALE 0
        int indexOfPlanet = -1;
        return indexOfPlanet;
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

    public String askNickname(){
        return null;
    }

    public PlayerColors askColor(){
        return null;
    }
}

