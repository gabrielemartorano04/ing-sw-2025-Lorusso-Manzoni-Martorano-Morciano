package it.polimi.ingsw;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

class WarZone extends Card {
    private List<Condition> conditionsOrder;
    private Map<Condition, Effect> penalties;
    private ArrayList<int[]> shoots;
    // shoots è una lista dove direction.get(i) rappresenta l'i-esimo colpo in questa specifica carta Pirates
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da dx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    public WarZone(boolean trialFlight, int level, List<Condition> conditionsOrder, Map<Condition, Effect> penalties, ArrayList<int[]> shoots ,String frontImage, String backImage) {
        super("War Zone", "A dangerous conflict zone", trialFlight, level, frontImage, backImage);
        this.conditionsOrder = conditionsOrder;
        this.penalties = penalties;
        this.shoots = shoots;
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
        ArrayList<Trucker> truckers = game.getTruckerList();
        Trucker weakestTrucker = null;
        for (Condition condition : conditionsOrder) {
            weakestTrucker = findWeakestTrucker(truckers, condition, controller);
            if (weakestTrucker != null) {
                applyEffect(weakestTrucker, penalties.get(condition), controller, game);
            }
        }
    }

    private Trucker findWeakestTrucker(List<Trucker> truckers, Condition condition, GameController controller) {
        Trucker weakestTrucker = null;
        int minValueCurrCondition = Integer.MAX_VALUE;
        for (Trucker trucker : truckers) {
            int currValue = switch (condition) {
                case HUMANS -> trucker.getSpaceShip().getNumHuman();
                case CANNONS -> trucker.getSpaceShip().getTotalCannonPower(controller);
                case ENGINES -> trucker.getSpaceShip().getTotalEnginePower(controller);
            };

            if (currValue < minValueCurrCondition || (currValue == minValueCurrCondition && trucker.getPosition() > weakestTrucker.getPosition())) {
                minValueCurrCondition = currValue;
                weakestTrucker = trucker;
            }
        }
        return weakestTrucker;
    }


    private void applyEffect(Trucker trucker, Effect effect, GameController controller, Game game) {
        if (effect == null) return;

        switch (effect.getType()) {
            case LOSE_DAYS -> game.moveTrucker(trucker, effect.getValue());
            case LOSE_HUMANS -> trucker.getSpaceShip().removeNumHuman(effect.getValue());
            case GET_SHOT -> trucker.getSpaceShip().applyShots(effect.getValue(), controller);
            case LOSE_GOODS ->
        }
    }
}