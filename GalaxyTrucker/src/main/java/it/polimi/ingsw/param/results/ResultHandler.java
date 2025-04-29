package it.polimi.ingsw.param.results;

/**
 * class used to obtain the value with its correct original type from an istance of a Result object
 */
public class ResultHandler {
    public static void handleResult(Result result) {
        switch (result) {
            case StringResult sr -> {
                String val = sr.getValue();
            }
            case IntResult ir -> {
                int val = ir.getValue();
            }
            default -> throw new IllegalStateException("Unexpected result type");
        }
    }
}

