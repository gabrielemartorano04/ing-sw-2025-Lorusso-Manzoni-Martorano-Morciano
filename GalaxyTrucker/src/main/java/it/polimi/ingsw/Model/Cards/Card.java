package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Controller.GameController;

import javax.swing.ImageIcon;
import java.util.ArrayList;

/**
 * classe che rappresenta la carta in generale andrà poi estesa dalle altre carte avventura
 */

public abstract class Card {
    private final String name;
    private final String description;
    private final Integer level;
    private final boolean  trialFlight;
    private final ImageIcon frontImage;
    private final ImageIcon backImage;

    public Card(String name, String description, boolean trialFlight, Integer level, String name_frontImage, String name_backImage) {
        this.name = name;
        this.description = description;
        this.trialFlight = trialFlight;
        this.level = level;
        this.frontImage = new ImageIcon(getClass().getResource("/Cards.cards/" + name_frontImage + ".jpg"));
        this.backImage = new ImageIcon(getClass().getResource("/Cards.cards/" + name_backImage + ".jpg"));

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

    public void cardEffect(){
    }

    public abstract void cardEffect(Game game);

}