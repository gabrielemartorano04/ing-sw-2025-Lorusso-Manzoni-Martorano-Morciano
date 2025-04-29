package it.polimi.ingsw.param.results;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.cards.Effect;
import it.polimi.ingsw.model.components.Goods;

import java.util.ArrayList;
import java.util.Map;

/**
 * class used to obtain the value with its correct original type from an istance of a Result object
 */
public class ResultHandler {
    public static void handleResult(Result result) {
        switch (result) {
            case IntResult r -> {
                int val = r.getResult();
            }
            case CardStateResult r ->{
                CardState val=r.getResult();
            }
            case GoodsListResult r->{
                ArrayList<Goods> val=r.getResult();
            }
            case GoodsResult r ->{
                Goods val=r.getResult();
            }
            case CoordinatesListResult r->{
                ArrayList<int[]> val=r.getResult();
            }
            case ListOfGoodsListResult r->{
                ArrayList<ArrayList<Goods>> val=r.getResult();
            }
            case CoordinatesResult r->{
                int[] val=r.getResult();
            }
            case GoodsArrayResult r->{
                Goods[] val=r.getResult();
            }
            case ConditionsListResult r->{
                ArrayList<Condition> val=r.getResult();
            }
            case MapResult r->{
                Map<Condition, Effect> val=r.getResult();
            }
            default -> throw new IllegalStateException("Unexpected result type");
        }

    }
}

