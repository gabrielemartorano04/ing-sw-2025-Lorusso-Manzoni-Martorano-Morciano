package it.polimi.ingsw;

/**
 * classe che rappresenta la carta in generale andrà poi estesa dalle altre carte avventura
 */

public class Card {
    private String name;
    private String description;
    private Integer level;
    private boolean trialFlight;

    public Card(String name, String description, boolean trialFlight) {
        this.name = name;
        this.description = description;
        this.trialFlight = trialFlight;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void cardEffect(Game game) {

    }
}