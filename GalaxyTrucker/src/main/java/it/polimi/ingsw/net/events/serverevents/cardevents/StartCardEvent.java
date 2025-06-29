package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.cards.Effect;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents the event triggered when a new card starts in the game
 */
public class StartCardEvent extends CardEvent {

    /**
     * Name of the card
     */
    private final String cardName;

    /**
     * Description of the card
     */
    private final String cardDescription;

    /**
     * Nickname of the first player
     */
    private final String firstPlayerNickname;

    /**
     * Question of the card
     */
    private final String question;

    /**
     * Type of the card
     */
    private final Card card;

    /**
     * Bank with all the available goods
     */
    private Bank bank;

    /**
     * Days to lose
     */
    private final int days;

    /**
     * Credits to gain
     */
    private final int credits;

    /**
     * Number of crew members required
     */
    private final int numHumans;

    /**
     * Enemy power required
     */
    private final int power;

    /**
     * Number of crew members to lose
     */
    private final int astronautsLost;

    /**
     * Number of goods to lose
     */
    private final int goodsLost;

    /**
     * List of goods to gain
     */
    private final List<Goods> goods;

    /**
     * List of meteorites that will hit the ship
     */
    private final List<int[]> meteorites;

    /**
     * List of shoots that will hit the ship
     */
    private final List<int[]> shoots;

    /**
     * List of card's planets' goods
     */
    private final ArrayList<ArrayList<Goods>> planets;

    /**
     * List of warzone card's conditions
     */
    private final List<Condition> conditionsOrder;

    /**
     * Map of effects associated with their conditions
     */
    private final Map<Condition, Effect> penalties;

    /**
     * Map of the trucker's positions
     */
    private final Map<String,Integer> track;

    /**
     * List of card's planets associated with a trucker
     */
    private final ArrayList<Trucker> planetsOccupied;

    /**
     * Event constructor
     * @param cardName name of the card
     * @param cardDescription description of the card
     * @param firstPlayerNickname name of the first player
     * @param question of the card
     * @param days to lose
     * @param credits to gain
     * @param numHumans number of crew members required
     * @param goods list of goods to gain
     * @param meteorites list meteorites that will hit the spaceship
     * @param power required to defeat the enemy
     * @param astronautsLost number of crew members to lose
     * @param goodsLost number of goods to lose
     * @param shoots list shoots that will hit the spaceship
     * @param planets list of card's planets' goods
     * @param conditionsOrder list of warzone card's conditions
     * @param penalties map of effects associated with their conditions
     * @param parserState state of the parser
     * @param planetsOccupied list of card's planets associated with a trucker
     * @param map of truckers' positions
     * @param card type of the card
     */
    public StartCardEvent(
            String cardName,
            String cardDescription,
            String firstPlayerNickname,
            String question,
            int days,
            int credits,
            int numHumans,
            List<Goods> goods,
            List<int[]> meteorites,
            int power,
            int astronautsLost,
            int goodsLost,
            List<int[]> shoots,
            ArrayList<ArrayList<Goods>> planets,
            List<Condition> conditionsOrder,
            Map<Condition, Effect> penalties,
            ParserState parserState,
            ArrayList<Trucker> planetsOccupied,
            Map<String,Integer> map,
            Card card
    ) {
        super(true, parserState, null, new ArrayList<>());
        this.cardName = cardName;
        this.cardDescription = cardDescription;
        this.firstPlayerNickname = firstPlayerNickname;
        this.days = days;
        this.credits = credits;
        this.numHumans = numHumans;
        this.goods = goods;
        this.meteorites = meteorites;
        this.power = power;
        this.astronautsLost = astronautsLost;
        this.goodsLost = goodsLost;
        this.shoots = shoots;
        this.planets = planets;
        this.conditionsOrder = conditionsOrder;
        this.penalties = penalties;
        this.question = question;
        this.planetsOccupied = planetsOccupied;
        this.track = map;
        this.card = card;
    }

    /**
     * @return the name of the card
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * @return the description of the card
     */
    public String getCardDescription() {
        return cardDescription;
    }

    /**
     * @return the nickname of the firts player
     */
    public String getFirstPlayerNickname() {
        return firstPlayerNickname;
    }

    /**
     * @return the number of days to lose
     */
    public int getDays() {
        return days;
    }

    /**
     * @return the number of credits to gain
     */
    public int getCredits() {
        return credits;
    }

    /**
     * @return the number of crew members required
     */
    public int getNumHumans() {
        return numHumans;
    }

    /**
     * @return the list of goods to gain
     */
    public List<Goods> getGoods() {
        return goods;
    }

    /**
     * @return the list of meteorites that will hit the spaceship
     */
    public List<int[]> getMeteorites() {
        return meteorites;
    }

    /**
     * @return the enemy power required
     */
    public int getPower() {
        return power;
    }

    /**
     * @return the number of crew members to lose
     */
    public int getAstronautsLost() {
        return astronautsLost;
    }

    /**
     * @return number of goods to lose
     */
    public int getGoodsLost() {
        return goodsLost;
    }

    /**
     * @return the list of shoots that will hit the spaceship
     */
    public List<int[]> getShoots() {
        return shoots;
    }

    /**
     * @return the list of the card's planets' goods
     */
    public ArrayList<ArrayList<Goods>> getPlanets() {
        return planets;
    }

    /**
     * @return the list of warzone card's conditions
     */
    public List<Condition> getConditionsOrder() {
        return conditionsOrder;
    }

    /**
     * @return the map of effects associated with their conditions
     */
    public Map<Condition, Effect> getPenalties() {
        return penalties;
    }

    /**
     * @return the question of the card
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @return the bank with all the available goods
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * Sets the bank for the current event
     * @param bank the bank object with all the available goods
     */
    public void setBank(Bank bank) {
        this.bank = bank;
    }

    /**
     * @return the type of card
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * @return list of card's planets associated with a trucker
     */
    public ArrayList<Trucker> getPlanetsOccupied() {
        return planetsOccupied;
    }

    /**
     * @return the map of truckers' positions
     */
    public Map<String,Integer> getTrack() {
        return track;
    }

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers(){
        return firstPlayerNickname+" is deciding "+ question;
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
