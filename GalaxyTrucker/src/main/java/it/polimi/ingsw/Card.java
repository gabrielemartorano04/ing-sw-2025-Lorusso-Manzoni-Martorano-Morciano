package it.polimi.ingsw;
import javax.swing.ImageIcon;

/**
 * classe che rappresenta la carta in generale andrà poi estesa dalle altre carte avventura
 */

public class Card {
    private final String name;
    private final String description;
    private final Integer level;
    private final boolean  trialFlight;
    private final ImageIcon image;

    public Card(String name, String description, boolean trialFlight, Integer level) {
        this.name = name;
        this.description = description;
        this.trialFlight = trialFlight;
        this.level = level;
        this.image = new ImageIcon();
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

    public void cardEffect(Game game, GameController controller) {

    }
}