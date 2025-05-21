package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.view.ParserState;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that manages the single card abandoned ship
 * In order of position, starting from the leader, the trucker decides, checking the crew members requirement, if he/she wants to lose days of flight in exchange for goods.
 * Only one trucker car take the station and is possible that nobody wants to take it
 */
public class AbandonedStation extends Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int days;
    private final int numHuman;
    private final ArrayList<Goods> goods;
    private Trucker truckerThatHasDecided = null; //è il trucker che ha deciso di atterrare, è null se nessuno ha deciso di atterrare
    private ArrayList<int[]> coordinates = null; //coordinates sono le coordinate dei Load dove inserire le merci. Viene settato dal Card controller, se è vuoto vuol dire che nessun giocatore ha voluto accettare la carta
    private CardState cardState;
    private int currGood;
    private int indexOfCurrTrucker;

    /**
     * Abandoned station constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param goods list of goods to gain
     * @param days number of days of flight to lose
     * @param numHuman number of crew members required
     * @param frontImage front image of the card
     * @param backIMage back image of the card
     */
    public AbandonedStation(boolean trialFlight, int level, ArrayList<Goods> goods, int days, int numHuman,String frontImage, String backIMage) {
        super("Abandoned Station", "A derelict space station", trialFlight, level, frontImage, backIMage);
        this.days = days;
        this.numHuman = numHuman;
        this.goods = goods;
        this.cardState = CardState.LAND;
        this.indexOfCurrTrucker =0;
        this.currGood=0;
    }


    /**
     * Returns the number of days of flight to lose
     * @return days to lose
     */
    public int getDays() {
        return days;
    }

    /**
     * Returns the number of crew members required
     * @return crew members to lose
     */
    public int getNumHuman() {
        return numHuman;
    }

    /**
     * Returns the list of goods to gain
     * @return goods to gain
     */
    public ArrayList<Goods> getGoods() {
        return goods;
    }

    @Override
    public ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Do you want to land on this Abandoned Station?",
                days,
                -1,
                numHuman,
                goods,
                null,
                -1,
                -1,
                -1,
                null,
                null,
                null,
                null,
                ParserState.LAND
        );
    }

    /**
     * Sets the only trucker that has chosen to take the station
     * @param landing: choice taken
     */
    @Override
    public ServerEvent truckerLanding(boolean landing) throws GeneralError {
        if (cardState != CardState.LAND) {
            throw new GeneralError("stato non valido");
        }
        if(landing) {
            this.truckerThatHasDecided = Game.getIstance().getTrucker(indexOfCurrTrucker);
            //TODO: TEST CHE NON CI SIA UN LOOP INFINITO
            do {
                if (Game.getIstance().getBank().getGoodsInBank(goods.get(currGood).getValue()) >= 1) {
                    cardState = CardState.GOODSTOADD;
                    return new PlotGood(true, cardState, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), goods.get(currGood));
                } else {
                    currGood++;
                    if(currGood==goods.size()) {
                        cardState = CardState.ESECUZIONE;
                        return cardEffect();
                    }
                }
            }while(true);

        }else{
            if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()){
                cardState = CardState.ESECUZIONE;
                return cardEffect();
            }
            indexOfCurrTrucker++;
            return new BooleanDecision(ServerEventType.LAND, true, ParserState.LAND, "Do you want to land on this AbandonedStation?", Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
        }
    }


    /**
     * Sets the coordinates of the loads where the winner trucker wants to add the goods gained
     * @param goodsCoordinates list of coordinatest
     */
    @Override
    public ServerEvent setGoodsCoordinates(Boolean stop, int[] goodsCoordinates, int positionInLoad) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.GOODSTOADD) {
            throw new GeneralError("Card state is not GOODSTOADD");
        }
        if (!truckerThatHasDecided.getSpaceShip().checkLoad(goodsCoordinates)) { //check load deve controllare il singolo componente
            throw new InvalidCoordinates("Not goods deposit");
        }
        if(stop){
            cardState = CardState.REARRANGE;
            return new RearrangeEvent(false, this.getCardState(), this.truckerThatHasDecided.getNickname());
        }else {
            try {
                // addGood fa anche la remove dalla bank della merce corrente
                truckerThatHasDecided.getSpaceShip().addGood(goods.get(currGood), goodsCoordinates, positionInLoad, Game.getIstance().getBank());
            } catch (InvalidPositionException e) {
                cardState = CardState.GOODSTOADD;
                throw e;
            }
            if (currGood == goods.size() - 1) { // ci va il -1 qui!!!
                cardState = CardState.REARRANGE;
                return new RearrangeEvent(false, this.getCardState(), this.truckerThatHasDecided.getNickname());
            } else {
                currGood++;
                //TODO: Check che non ci sia loop infinito
                do {
                    if (Game.getIstance().getBank().getGoodsInBank(goods.get(currGood).getValue()) >= 1) {
                        cardState = CardState.CHOOSE_TO_GAIN;
                        return new PlotGood(false, this.getCardState(), truckerThatHasDecided.getNickname(), goods.get(currGood));
                    } else {
                        currGood++;
                        if (currGood == goods.size()) {
                            cardState = CardState.REARRANGE;
                            return new RearrangeEvent(false, this.getCardState(), this.truckerThatHasDecided.getNickname());
                        }
                    }
                } while (true);
            }
        }
    }

    // si potrebbe non metter contiune e procedere se tutto e a null
    @Override
    public ServerEvent rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean stop) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.REARRANGE) {
            throw new GeneralError("Card state is not REARRANGE");
        }
        if (!stop) {
            if (coord1 == null && !truckerThatHasDecided.getSpaceShip().checkLoad(coord1) && coord2 == null && !truckerThatHasDecided.getSpaceShip().checkLoad(coord2)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                truckerThatHasDecided.getSpaceShip().swapGoods(coord1, value1, coord2, value2);
                return new RearrangeEvent(false, this.getCardState(), this.truckerThatHasDecided.getNickname());
            } catch (InvalidPositionException e) {
                cardState = CardState.REARRANGE;
                throw new InvalidPositionException("invalid positions");
            }
        } else{
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }
    }

    /**
     * Applies the changes provided by the card
     */
    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if (cardState != CardState.ESECUZIONE) {
            throw new GeneralError("risposte mancanti");
        }
        Game.getIstance().moveBackward(truckerThatHasDecided, days);
        cardState = CardState.EXIT;
        this.setIsCardFinished(true);
        Game.getIstance().endTurn();
        return new CardFinished(true, this.getCardState(), Game.getIstance().getTruckerList());
    }


    public CardState getCardState() {
        return cardState;
    }

    public void setCardState(CardState cardState) {
        this.cardState = cardState;
    }
}