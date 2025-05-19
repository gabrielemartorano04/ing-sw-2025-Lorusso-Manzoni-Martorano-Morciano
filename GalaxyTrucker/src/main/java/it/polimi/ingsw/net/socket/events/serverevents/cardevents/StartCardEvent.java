package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.cards.Effect;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Event sent when a new card is drawn and needs to be shown to all clients.
 * Only the client with matching nickname should answer, if applicable.
 */
public class StartCardEvent extends CardEvent {
    private final String cardName;
    private final String cardDescription;
    private final String firstPlayerNickname;
    private final String question;

    private Bank bank;

    private final int days;
    private final int credits;
    private final int numHumans;
    private final int power;
    private final int astronautsLost;
    private final int goodsLost;

    private final List<Goods> goods;
    private final List<int[]> meteorites;
    private final List<int[]> shoots;
    private final ArrayList<ArrayList<Goods>> planets;
    private final List<Condition> conditionsOrder;
    private final Map<Condition, Effect> penalties;

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
            CardState cardState
    ) {
        super(ServerEventType.START_CARD, true, cardState, null);
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
    }

    // Getters
    public String getCardName() {
        return cardName;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public String getFirstPlayerNickname() {
        return firstPlayerNickname;
    }

    public int getDays() {
        return days;
    }

    public int getCredits() {
        return credits;
    }

    public int getNumHumans() {
        return numHumans;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public List<int[]> getMeteorites() {
        return meteorites;
    }

    public int getPower() {
        return power;
    }

    public int getAstronautsLost() {
        return astronautsLost;
    }

    public int getGoodsLost() {
        return goodsLost;
    }

    public List<int[]> getShoots() {
        return shoots;
    }

    public ArrayList<ArrayList<Goods>> getPlanets() {
        return planets;
    }

    public List<Condition> getConditionsOrder() {
        return conditionsOrder;
    }

    public Map<Condition, Effect> getPenalties() {
        return penalties;
    }

    public String getQuestion() {
        return question;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers(){
        return firstPlayerNickname+" is deciding the answer to the following question: "+question;
    }

}
