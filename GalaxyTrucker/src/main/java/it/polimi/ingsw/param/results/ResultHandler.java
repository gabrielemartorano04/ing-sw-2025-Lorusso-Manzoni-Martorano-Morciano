package it.polimi.ingsw.param.results;

/**
 * class used to obtain the value with its correct original type from an istance of a Result object
 *
 * possible use of this class: we can use handleResult to generate the events or handle the results that the controller
 * returns to the server controller
 */
public class ResultHandler {
    public static void handleResult(Result result) {
        switch (result) {
            case IntResult ir -> {
                int val = ir.getResult();
            }
            default -> throw new IllegalStateException("Unexpected result type");
        }
    }
}

