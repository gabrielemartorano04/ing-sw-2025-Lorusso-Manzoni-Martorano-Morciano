package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.SpaceShip;
import it.polimi.ingsw.Model.Trucker;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class WarZone extends Card {
    private CardState state;
    private int currConditionIndex;
    private ArrayList<Condition> conditionsOrder;
    private Condition currCondition;
    private Trucker weakestTrucker;
    private Map<Condition, Effect> penalties;
    private ArrayList<int[]> shoots;
    // shoots è una lista dove shoots.get(i) rappresenta l'i-esimo colpo in questa specifica carta Warzone
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da dx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    // memorizza le coordinate da cui rimuovere l'equipaggio in caso la penitenza sia togli num di equipaggio
    private ArrayList<int[]> coordinatesToRemoveCrew;

    // memorizza le coordinate da cui rimuovere le merci in caso la penitenza sia togli num di merci
    private ArrayList<int[]> coordinatesToRemoveGoods;


    public WarZone(boolean trialFlight, int level, ArrayList<Condition> conditionsOrder, Map<Condition, Effect> penalties, ArrayList<int[]> shoots ,String frontImage, String backImage) {
        super("War Zone", "A dangerous conflict zone", trialFlight, level, frontImage, backImage);
        this.conditionsOrder = conditionsOrder;
        this.penalties = penalties;
        this.shoots = shoots;
        this.state = CardState.WARZONE;
        this.currConditionIndex = 0;
        this.weakestTrucker = null;
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


    public void combatZone(Game game, ArrayList<ArrayList<int[]>> truckersCannonsCoordinates, ArrayList<ArrayList<int[]>> truckersEnginesCoordinates){
        if(state != CardState.WARZONE){
            throw new IllegalStateException("CombatZone not callable in the state " + state);
        }
        if(game.getTruckerList().size() == 1){
            state = CardState.EXIT;
            return;
        }
        this.currCondition = conditionsOrder.get(currConditionIndex);
        currConditionIndex++;
        switch(currCondition){
            case HUMANS -> {
                state = CardState.FIND_WEAKEST;
                findWeakestTrucker(game, new ArrayList<>());
            }
            case CANNONS -> {
                state = CardState.FIND_WEAKEST;
                findWeakestTrucker(game, truckersCannonsCoordinates);
            }
            case ENGINES -> {
                state = CardState.FIND_WEAKEST;
                findWeakestTrucker(game, truckersEnginesCoordinates);
            }
        }
    }


    // questo metodo dovrà essere chiamato dal Controller per capire a ogni condizione il weakestTrucker
    // truckersCoordinates serve perché quando devo calcolare il weakest in base a potenza di fuoco o motrice
    // devo dare in ingresso a findWeakestTrucker la lista delle coordinate delle componenti che ogni giocatore
    // ha deciso di attivare
    public Trucker findWeakestTrucker(Game game, ArrayList<ArrayList<int[]>> truckersCoordinates) {
        ArrayList<Trucker> truckers = game.getTruckerList();
        if(state != CardState.FIND_WEAKEST){
            throw new IllegalStateException("FindWeakestTrucker not callable in the state " + state);
        }

        if(currCondition != Condition.HUMANS && truckersCoordinates.isEmpty()){
            throw new RuntimeException("Not expecting an empty list of Coordinates with the condition " + currCondition);
        }

        Trucker weakestTrucker = null;
        int i = 0;
        int minValueCurrCondition = Integer.MAX_VALUE;
        for (Trucker trucker : truckers) {
            int currValue = switch (currCondition) {
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
        this.weakestTrucker = weakestTrucker;
        if(penalties.get(currCondition).getType().equals(TypeOfEffect.LOSE_DAYS)){
            state = CardState.ESECUZIONE;
            cardEffect(game);
        }else{
            state = CardState.ATTESA_RISPOSTE;
        }
        return this.weakestTrucker;
    }


    public void setBeforeEffect(ArrayList<int[]> coordinatesGoods, ArrayList<int[]> coordinatesCrew){
        if(state != CardState.ATTESA_RISPOSTE){
            throw new IllegalStateException("setBeforeEffect not callable in the state " + state);
        }
        if((coordinatesGoods == null && coordinatesCrew == null) || (coordinatesCrew.isEmpty() && coordinatesGoods.isEmpty())){
            throw new RuntimeException("Not expecting an empty list of Coordinates with the effect " + penalties.get(currCondition).getType());
        }
        if(coordinatesGoods != null && !coordinatesGoods.isEmpty()){
            this.coordinatesToRemoveGoods = coordinatesGoods;
        }
        else if (coordinatesCrew != null && !coordinatesCrew.isEmpty()){
            this.coordinatesToRemoveCrew = coordinatesCrew;
        }
    }


    @Override
    public void cardEffect(Game game) {
        if (weakestTrucker != null) {
            // devo gestire le ulteriori richieste per i casi dove vieni sparato perché devo chiedere al giocatore
            // se vuole attivare lo scudo ogni volta che il colpo è piccolo
            // e per il caso dove devi perdere equipaggio che devi scegliere da dove perdere umani o alieni
            Effect effect = penalties.get(currCondition);
            switch(effect.getType()){
                case LOSE_DAYS -> game.moveBackward(weakestTrucker, effect.getValue());
                case LOSE_HUMANS -> {
                    weakestTrucker.getSpaceShip().removeNumHuman(coordinatesToRemoveCrew);
                }
                case LOSE_GOODS -> weakestTrucker.getSpaceShip().removeGoods(effect.getValue(), coordinatesToRemoveGoods);
                case GET_SHOT -> {
                    // DA FINIRE DOPO: devo fare l'alternanza tra uno stato e l'altro
                    SpaceShip ship = weakestTrucker.getSpaceShip();
                    for (int[] shot : shoots) {
                        boolean isHeavy = (shot[0] == 1);
                        int direction = shot[1];
                        int roll = game.rollDice();
                        if (ship.isHit(roll, direction)) {
                            if (isHeavy) {
                                ship.destroyComponent(roll, direction);
                            } else {
                                if (!ship.blockLightCannonShot(direction, roll, askController.askToUseShield(weakestTrucker, roll, direction))) {
                                    ship.destroyComponent(roll, direction);
                                }
                            }
                        }
                    }
                }

            }
        }
    }


}