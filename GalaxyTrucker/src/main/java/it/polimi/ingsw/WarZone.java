package it.polimi.ingsw;
import java.util.List;
import java.util.Map;

class WarZone extends Card {
    private List<Condition> conditionsOrder;
    private Map<Condition, Effect> penalties;
    private int humansLost;
    private int daysLost;
    private int goodsLost;
    private List<int[]> shoots;
    // shoots è una lista dove direction.get(i) rappresenta l'i-esimo colpo in questa specifica carta Pirates
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da dx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    public WarZone(boolean trialFlight, int level, List<Condition> conditionsOrder, Map<Condition, Effect> penalties) {
        super("War Zone", "A dangerous conflict zone", trialFlight, level, "", "");
        this.conditionsOrder = conditionsOrder;
        this.penalties = penalties;
    }
    public List<Condition> getConditionsOrder() {
        return conditionsOrder;
    }
    public Map<Condition, Effect> getPenalties() {
        return penalties;
    }

    // in caso di parità di criteri subisce la penitenza quello più avanti nel percorso quindi quando cerco
    // il weakestTrucker nei confronti uso il minore stretto in modo che a parità di potenza di fuoco/motrice o
    // numero di umani scelgo sempre il trucker più a sinistra nella lista cioè quello più avanti sulla plancia
    @Override
    public void cardEffect(Game game, GameController controller) {
        List<Trucker> truckers = game.getTruckerList();
        Trucker weakestTrucker = null;
        int minCurrCondition;
        for(Condition condition : conditionsOrder) {
            if (condition == condition.HUMANS) {
                minCurrCondition = truckers.getFirst().getSpaceShip().getNumHuman();
                for(Trucker trucker : truckers) {
                    if(trucker.equals(truckers.getFirst())){
                        continue;
                    }
                    else {
                        if (trucker.getSpaceShip().getNumHuman() < minCurrCondition) {
                            minCurrCondition = trucker.getSpaceShip().getNumHuman();
                            weakestTrucker = trucker;
                        }
                    }
                }
                if(weakestTrucker != null) {

                }
            } else if (condition == condition.CANNONS) {
                minCurrCondition = truckers.getFirst().getSpaceShip().getTotalCannonPower(controller);
                int currTotalCannonPower;
                for(Trucker trucker : truckers) {
                    if(trucker.equals(truckers.getFirst())) {
                        continue;
                    }
                    else {
                        currTotalCannonPower = trucker.getSpaceShip().getTotalCannonPower(controller);
                        if (currTotalCannonPower < minCurrCondition) {
                            minCurrCondition = currTotalCannonPower;
                            weakestTrucker = trucker;
                        }
                    }
                }
                if(weakestTrucker != null) {

                }
            } else if (condition == condition.ENGINES){
                minCurrCondition = truckers.getFirst().getSpaceShip().getTotalEnginePower(controller);
                int currTotalEnginePower;
                for(Trucker trucker : truckers) {
                    if(trucker.equals(truckers.getFirst())) {
                        continue;
                    }
                    else {
                        currTotalEnginePower = trucker.getSpaceShip().getTotalEnginePower(controller);
                        if (currTotalEnginePower < minCurrCondition) {
                            minCurrCondition = currTotalEnginePower;
                            weakestTrucker = trucker;
                        }
                    }
                }
                if(weakestTrucker != null) {

                }
            }
        }
    }
}