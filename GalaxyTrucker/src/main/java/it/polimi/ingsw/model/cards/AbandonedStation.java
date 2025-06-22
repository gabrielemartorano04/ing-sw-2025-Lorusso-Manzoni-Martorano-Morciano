package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that manages the single card abandoned ship
 * In order of position, starting from the leader, the trucker decides, checking the crew members requirement, if he/she wants to lose days of flight in exchange for goods.
 * Only one trucker car take the station and is possible that nobody wants to take it
 */
public class AbandonedStation extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * number of flight days to lose for the player that decided to land
     */
    private final int days;

    /**
     * number of crew members you need in order to be eligible to land
     */
    private final int numHuman;

    /**
     * goods that a player can obtain as reward for landing in this AbandonedStation
     */
    private final ArrayList<Goods> goods;

    /**
     * player that has decided to land if exists a client that has decided to land
     */
    private Trucker truckerThatHasDecided = null;

    /**
     * current state of the card, helper for debug and checking consistency
     */
    private CardState cardState;

    /**
     * index of the current good from the list of goods to be obtained
     */
    private int currGood;

    /**
     * index of the current player to handle the fact that clients have to respect a specific turn
     */
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
    public AbandonedStation(boolean trialFlight, int level, ArrayList<Goods> goods, int days, int numHuman,String frontImage, String backIMage, int id) {
        super("Abandoned Station", "A derelict space station", trialFlight, level, frontImage, backIMage, id);
        this.days = days;
        this.numHuman = numHuman;
        this.goods = goods;
        this.cardState = CardState.LAND;
        this.indexOfCurrTrucker =0;
        this.currGood=0;
    }

    /**
     * @return an event containing all the card details
     */
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
                ParserState.LAND,
                this
        );
    }

    /**
     * sets truckerThatHasDecided based on the answer by the current client and updates the next client if landing is false
     * @param landing: choice taken by the current client
     */
    @Override
    public ServerEvent truckerLanding(boolean landing) throws GeneralError {
        if (cardState != CardState.LAND) {
            throw new GeneralError("stato non valido");
        }
        if(landing) {
            this.truckerThatHasDecided = Game.getIstance().getTrucker(indexOfCurrTrucker);
            do {
                if (Game.getIstance().getBank().getGoodsInBank(goods.get(currGood).getValue()) >= 1) {
                    cardState = CardState.GOODSTOADD;
                    return new PlotGood(true, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(),Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip(),goods.get(currGood));
                } else {
                    currGood++;
                    if(currGood==goods.size()) {
                        cardState = CardState.ESECUZIONE;
                        return cardEffect();
                    }
                }
            }while(true);

        }else{
            if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()-1){
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
    public ServerEvent setGoodsCoordinates(Boolean answer, int[] goodsCoordinates, int positionInLoad) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.GOODSTOADD) {
            throw new GeneralError("Card state is not GOODSTOADD");
        }
        if(!answer){
            if (currGood == goods.size() - 1) {
                System.out.println("Entro ");
                cardState = CardState.REARRANGE;
                return new RearrangeEvent(false, this.truckerThatHasDecided.getNickname());
            } else {
                currGood++;
                //TODO: Check che non ci sia loop infinito
                do {
                    if (Game.getIstance().getBank().getGoodsInBank(goods.get(currGood).getValue()) >= 1) {
                        cardState = CardState.GOODSTOADD;
                        return new PlotGood(true, truckerThatHasDecided.getNickname(), truckerThatHasDecided.getSpaceShip(),goods.get(currGood));
                    } else {
                        currGood++;
                        if (currGood == goods.size()) {
                            cardState = CardState.REARRANGE;
                            return new RearrangeEvent(false, this.truckerThatHasDecided.getNickname());
                        }
                    }
                } while (true);
            }
        }else {
            if (!truckerThatHasDecided.getSpaceShip().checkLoad(goodsCoordinates)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                // addGood fa anche la remove dalla bank della merce corrente
                goods.get(currGood).setX(goodsCoordinates[0]);
                goods.get(currGood).setY(goodsCoordinates[1]);
                truckerThatHasDecided.getSpaceShip().addGood(goods.get(currGood), goodsCoordinates, positionInLoad, Game.getIstance().getBank());
            } catch (InvalidPositionException e) {
                cardState = CardState.GOODSTOADD;
                throw e;
            }
            if (currGood == goods.size() - 1) {
                System.out.println("Entro ");
                cardState = CardState.REARRANGE;
                return new RearrangeEvent(false, this.truckerThatHasDecided.getNickname());
            } else {
                currGood++;
                //TODO: Check che non ci sia loop infinito
                do {
                    if (Game.getIstance().getBank().getGoodsInBank(goods.get(currGood).getValue()) >= 1) {
                        cardState = CardState.GOODSTOADD;
                        return new PlotGood(true, truckerThatHasDecided.getNickname(), truckerThatHasDecided.getSpaceShip(),goods.get(currGood));
                    } else {
                        currGood++;
                        if (currGood == goods.size()) {
                            cardState = CardState.REARRANGE;
                            return new RearrangeEvent(false, this.truckerThatHasDecided.getNickname());
                        }
                    }
                } while (true);
            }
        }
    }


    /**
     * switch 2 goods to rearrange them in the ship
     * @param coord1 initial position
     * @param coord2 final position
     * @param value1 initial internal position
     * @param value2 final internat position
     * @param stop when to end the swaps
     * @return the server event to
     * @throws GeneralError
     * @throws InvalidCoordinates
     * @throws InvalidPositionException
     */
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
                return new RearrangeEvent(false, this.truckerThatHasDecided.getNickname());
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
        if(truckerThatHasDecided != null) {
            Game.getIstance().moveBackward(truckerThatHasDecided, days);
        }
        ArrayList<Trucker> truckerRemoved;
        truckerRemoved = Game.getIstance().endTurn();

        GamePersistence.saveGame(GameController.getIstance());
        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, Game.getIstance().getTrack());
    }

}

