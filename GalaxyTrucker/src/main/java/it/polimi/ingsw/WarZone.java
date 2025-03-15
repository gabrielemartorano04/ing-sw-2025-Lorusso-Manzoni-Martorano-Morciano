package it.polimi.ingsw;

class WarZone extends Card {
    private List<Condition> conditionsOrder;
    private Map<Condition, Effect> penalties;

    public WarZone(boolean trialFlight, int level) {
        super("War Zone", "A dangerous conflict zone", trialFlight, level);
    }

    public List<Condition> getConditionsOrder() {
        return conditionsOrder;
    }

    public Map<Condition, Effect> getPenalties() {
        return penalties;
    }
}