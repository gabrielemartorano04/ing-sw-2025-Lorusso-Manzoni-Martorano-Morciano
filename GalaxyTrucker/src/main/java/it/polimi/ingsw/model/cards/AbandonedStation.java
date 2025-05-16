package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.callers.AbandonedShipController;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.StartCardEvent;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.utilities.exceptions.GeneralError;

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
    private int playerDecisions;

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
        super("Abandoned Station", "A derelict space station", trialFlight, level, frontImage, backIMage, new AbandonedShipController());
        this.days = days;
        this.numHuman = numHuman;
        this.goods = goods;
        this.cardState = CardState.ATTESA_RISPOSTE;
        this.playerDecisions=0;
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
    public ServerEvent startCard(String currPlayerNickname) {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                currPlayerNickname,
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
                null
        );
    }

    /**
     * Sets the only trucker that has chosen to take the station
     * @param trucker: current trucker that is deciding whether to land or not
     * @param landing: choice taken
     * @param numPlayer: num of player in the game
     * @param bank: bank of the game
     */
    public CardState truckerLanding(Trucker trucker, boolean landing, int numPlayer, Bank bank) throws GeneralError {
        if (cardState != CardState.ATTESA_RISPOSTE) {
            throw new GeneralError("stato non valido");
        }
        if(landing) {
            this.truckerThatHasDecided = trucker;
            do {
                if (bank.getGoodsInBank(goods.get(currGood).getValue()) >= 1) {
                    cardState = CardState.CHOOSE_TO_GAIN;
                } else {
                    currGood++;
                    if(currGood>=goods.size()) {
                        cardState = CardState.ESECUZIONE;
                    }
                }
            }while(currGood<goods.size() && cardState!=CardState.CHOOSE_TO_GAIN);
        }else{
            if (playerDecisions == numPlayer){
                cardState = CardState.ESECUZIONE;
            }else{
                cardState = CardState.ATTESA_RISPOSTE;
            }
        }
        playerDecisions++;
        return cardState;
    }

    public Goods setChoiceToGain(boolean choice) throws GeneralError {
        if(cardState!=CardState.CHOOSE_TO_GAIN){
            throw new GeneralError("Card state is not CHOOSE_TO_GAIN");
        }
        if (choice) {
            cardState = CardState.GOODSTOADD;
            return goods.get(currGood);
        }else {
            if(currGood==goods.size()-1) {
                cardState = CardState.REARRANGE;
            } else currGood++;
            return null;
        }
    }

    /**
     * Sets the coordinates of the loads where the winner trucker wants to add the goods gained
     * @param winnerCoordinatesToAddGood list of coordinatest
     */
    public CardState setWinnerCoordinatesToAddGoods(int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws GeneralError, InvalidCoordinates  {
        if (cardState != CardState.GOODSTOADD) {
            throw new GeneralError("Card state is not GOODSTOADD");
        }
        if (winnerCoordinatesToAddGood==null || !trucker.getSpaceShip().checkLoad(winnerCoordinatesToAddGood)) { //check load deve controllare il singolo componente
            throw new InvalidCoordinates("Not goods deposit");
        }
        try{
            trucker.getSpaceShip().addGood(goods.get(currGood), winnerCoordinatesToAddGood, value, bank);
            bank.removeGoodsInBank(goods.get(currGood).getValue());
        }catch (InvalidPositionException e) {
            cardState = CardState.GOODSTOADD;
        }
        if(currGood == goods.size()){
            cardState = CardState.REARRANGE;
        }
        else{
            currGood++;
            do {
                if (bank.getGoodsInBank(goods.get(currGood).getValue()) >= 1) {
                    cardState = CardState.CHOOSE_TO_GAIN;
                } else {
                    currGood++;
                    if(currGood>=goods.size()) {
                        cardState = CardState.REARRANGE;
                    }
                }
            }while(currGood<goods.size() && cardState!=CardState.CHOOSE_TO_GAIN);
        }
        return cardState;

    }

    // si potrebbe non metter contiune e procedere se tutto e a null
    public CardState rearrange(Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.REARRANGE) {
            throw new GeneralError("Card state is not REARRANGE");
        }
        if (!contiune) {
            if (coord1 == null && !trucker.getSpaceShip().checkLoad(coord1) && coord2 == null && !trucker.getSpaceShip().checkLoad(coord2)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                trucker.getSpaceShip().swapGoods(coord1, value1, coord2, value2);
            } catch (InvalidPositionException e) {
                cardState = CardState.REARRANGE;
                throw new InvalidPositionException("invalid positions");
            }
        } else{
            cardState = CardState.ESECUZIONE;
        }
        return cardState;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public CardState cardEffect(Game game) throws GeneralError {
        if (cardState != CardState.ESECUZIONE) {
            throw new GeneralError("risposte mancanti");
        }
        game.moveBackward(truckerThatHasDecided, days);
        cardState = CardState.EXIT;
        this.setIsCardFinished(true);
        return cardState;
    }


    public CardState getCardState() {
        return cardState;
    }

    public void setCardState(CardState cardState) {
        this.cardState = cardState;
    }
}