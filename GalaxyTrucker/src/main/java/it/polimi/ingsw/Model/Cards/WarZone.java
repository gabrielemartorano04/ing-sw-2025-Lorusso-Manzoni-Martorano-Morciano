package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.SpaceShip;
import it.polimi.ingsw.Model.Trucker;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class WarZone extends Card {
    private ArrayList<Condition> conditionsOrder;
    private Map<Condition, Effect> penalties;
    private ArrayList<int[]> shoots;
    // shoots è una lista dove shoots.get(i) rappresenta l'i-esimo colpo in questa specifica carta Warzone
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da dx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    // variabili settate dal controller per salvare le varie ask fatte dal controller

    // memorizza le coordinate da cui rimuovere l'equipaggio in caso la penitenza sia togli num di equipaggio
    private ArrayList<int[]> coordinatesToRemoveCrew;

    // memorizza le coordinate da cui rimuovere le merci in caso la penitenza sia togli num di merci
    private ArrayList<int[]> coordinatesToRemoveGoods;


    public WarZone(boolean trialFlight, int level, ArrayList<Condition> conditionsOrder, Map<Condition, Effect> penalties, ArrayList<int[]> shoots ,String frontImage, String backImage) {
        super("War Zone", "A dangerous conflict zone", trialFlight, level, frontImage, backImage);
        this.conditionsOrder = conditionsOrder;
        this.penalties = penalties;
        this.shoots = shoots;
    }
    public ArrayList<Condition> getConditionsOrder() {
        return conditionsOrder;
    }

    public Map<Condition, Effect> getPenalties() {
        return penalties;
    }

    public ArrayList<int[]> getShoots(){
        return shoots;
    }


    @Override
    public void cardEffect(Game game) {
        //per ora non lo uso perchè è letteralmente tutto dentro il controller per questa carta
    }


    // questo metodo dovrà essere chiamato dal Controller per capire a ogni condizione il weakestTrucker
    // truckersCoordinates serve perché quando devo calcolare il weakest in base a potenza di fuoco o motrice
    // devo dare in ingresso a findWeakestTrucker la lista delle coordinate delle componenti che ogni giocatore
    // ha deciso di attivare
    public Trucker findWeakestTrucker(List<Trucker> truckers, Condition condition, ArrayList<ArrayList<int[]>> truckersCoordinates) {
        Trucker weakestTrucker = null;
        int i = 0;
        int minValueCurrCondition = Integer.MAX_VALUE;
        for (Trucker trucker : truckers) {
            int currValue = switch (condition) {
                case HUMANS -> trucker.getSpaceShip().getNumHuman();
                case CANNONS -> trucker.getSpaceShip().getTotalCannonPower(truckersCoordinates.get(i));
                case ENGINES -> trucker.getSpaceShip().getTotalEnginePower(truckersCoordinates.get(i));
            };

            // in caso di parità di criteri subisce la penitenza quello più avanti nel percorso quindi quando cerco
            // il weakestTrucker nei confronti uso il minore stretto in modo che a parità di potenza di fuoco/motrice o
            // numero di umani scelgo sempre il trucker più a sinistra nella lista cioè quello più avanti sulla plancia
            if (currValue < minValueCurrCondition) {
                minValueCurrCondition = currValue;
                weakestTrucker = trucker;
            }
            i++;
        }
        i = 0;
        return weakestTrucker;
    }


}