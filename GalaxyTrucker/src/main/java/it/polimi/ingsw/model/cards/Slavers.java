package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.EndGameEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.view.ParserState;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SequencedCollection;

/**
 * Class that manages the single card slavers
 * In order of position, starting from the leader, each trucker decides which double cannon wants to activate, using batteries, for the calculation of the cannon power.
 * The first trucker that has a cannon power which is higher than the slavers one, defeat them and decide if he/she wants, losing days of flight, to gain cosmic credits.
 * If the trucker's cannon power is equal to the slavers' one, nothing happens to the trucker, but the slavers continue to attack
 * For all the defeated truckers, they have to remove some crew members
 */
public class Slavers extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * slavers cannon power
     */
    private final int slaversPower;

    /**
     * cosmic credits to gain
     */
    private final int credits;

    /**
     * flight days to lose
     */
    private final int days;

    /**
     * number of members of the crew to lose
     */
    private final int astronautsLost;

    private CardState cardState;

    /**
     * index of the current player to handle the fact that clients have to respect a specific turn
     */
    private int indexOfCurrentTrucker;

    /**
     * player that has defeated the slavers
     */
    private Trucker winnerTrucker;

    /**
     * list of all defeated players
     */
    private final ArrayList<Trucker> defeatedTruckers;


    /**
     * Slavers constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param credits number of cosmic credit to gain
     * @param days number of days of flight to lose
     * @param astronautsLost number of crew member to lose
     * @param slaversPower cannon power required
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public Slavers(boolean trialFlight, int level, int credits, int days, int astronautsLost, int slaversPower, String frontImage, String backImage,int id) {
        super("Slavers", "Intergalactic slave traders", trialFlight, level, frontImage, backImage,id);
        this.credits = credits;
        this.days = days;
        this.astronautsLost = astronautsLost;
        this.slaversPower = slaversPower;
        this.cardState = CardState.ACTIVATE_CANNONS;
        this.indexOfCurrentTrucker = 0;
        this.winnerTrucker = null;
        this.defeatedTruckers = new ArrayList<>();

    }

    /**
     * Returns the cannon power required
     * @return cannon power required
     */
    public int getSlaversPower() {
        return slaversPower;
    }

    /**
     * Returns the number of crew members to lose
     * @return crew members to lose
     */
    public int getAstronautsLost() {
        return astronautsLost;
    }

    /**
     * Returns the number of days of flight to lose
     * @return days to lose
     */
    public int getDays() {
        return days;
    }

    public CardState getCardState(){
        return cardState;
    }

    /**
     * Returns the number of cosmic credits to gain
     * @return credit to gain
     */
    public int getCredits() {
        return credits;
    }

    /**
     * @return an event containing all the card details
     */
    @Override
    public synchronized ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "whether to activate double cannons ",
                days,
                credits,
                -1,
                null,
                null,
                slaversPower,
                astronautsLost,
                -1,
                null,
                null,
                null,
                null,
                ParserState.ACTIVATE_CANNONS,
                null,
                Game.getIstance().getTrack(),
                this
        );
    }

    /**
     * Handles the current fight between slavers and a player.
     *
     * @return serverEvent to send to clients
     */
    private ServerEvent fightIsWin() throws GeneralError {
        float power = Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower();

        if (power > slaversPower) {
            return handleVictory();
        } else if (power == slaversPower) {
            return handleDraw();
        } else {
            return handleDefeat();
        }
    }

    /**
     * handle the victory of the fight
     *
     * @return ServerEvent to send to clients
     */
    private ServerEvent handleVictory() {
        winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
        cardState = CardState.COSMIC_CREDITS;
        return new GainCosmicCredits(true, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), credits, days);
    }

    /**
     * handle the draw of the fight
     *
     * @return ServerEvent to send to clients
     */
    private ServerEvent handleDraw() throws GeneralError{
        if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
            if(defeatedTruckers.isEmpty()){
                cardState = CardState.ESECUZIONE;
                return cardEffect();
            }else{
                cardState = CardState.HUMANSTOREMOVE;
                indexOfCurrentTrucker = 0;
                return new HumanCoordinates(true, defeatedTruckers.getFirst().getNickname(), defeatedTruckers.getFirst().getSpaceShip(), Game.getIstance().getTruckerList()
                );
            }
        }else{
            indexOfCurrentTrucker++;
            return new BooleanDecision(true, ParserState.ACTIVATE_CANNONS, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), Game.getIstance().getTruckerList());
        }
    }

    /**
     * handle the defeat of the fight
     *
     * @return ServerEvent to send to clients
     */
    private ServerEvent handleDefeat() throws GeneralError {
        defeatedTruckers.add(Game.getIstance().getTrucker(indexOfCurrentTrucker));
        if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
            cardState = CardState.HUMANSTOREMOVE;
            indexOfCurrentTrucker = 0;
            return new HumanCoordinates(true, defeatedTruckers.getFirst().getNickname(),defeatedTruckers.getFirst().getSpaceShip(), Game.getIstance().getTruckerList() );
        }else{
            indexOfCurrentTrucker++;
            return new BooleanDecision(true, ParserState.ACTIVATE_CANNONS, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), Game.getIstance().getTruckerList());
        }
    }


    /**
     * Sets for each trucker the coordinates of the double cannons that he/she wants to activate
     *
     * @param activate: true if the current player wants to activate any double Cannons, false otherwise
     * @param coord: coordinates of the doubleCannons to activate
     * @param batteryCoord: coordinates of the batteryStorages from which remove the batteries
     * @return serverEvent to send to clients
     * @throws InvalidComponentsException coordinates do not match to their component type
     * @throws NotEnoughBatteries not enoughBatteries indicated to activate the doubleCannons selected
     */
    @Override
    public synchronized ServerEvent activateCannons(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws GeneralError, InvalidComponentsException, NotEnoughBatteries {
        if(cardState!=CardState.ACTIVATE_CANNONS){
            throw new GeneralError("Card state is not ATTESA_RISPOSTE");
        }
        if(!activate){
            return fightIsWin();
        }else{
            if(Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getNumDoubleCannon()==0){
                return fightIsWin();
            }else{
                if (!Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().checkDoubleCannon(coord)){
                    throw new InvalidComponentsException("Not all are double cannons");
                }else if(!Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().checkBatteryStorage(batteryCoord)){
                    throw new InvalidComponentsException("Not all are batteries");
                }else
                {
                    Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().activateDoubleCannons(coord, batteryCoord);
                    return fightIsWin();
                }
            }
        }
    }


    /**
     * sets the decision taken by the winner if there is one
     *
     * @param answer: decision taken
     * @return serverEvent to send to clients
     * @throws GeneralError: not in the right state
     */
    @Override
    public synchronized ServerEvent gainCredits(boolean answer)throws GeneralError{
        if(cardState!=CardState.COSMIC_CREDITS){
            throw new GeneralError("Card state is not COSMIC_CREDITS");
        }
        if(answer){
            winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
            winnerTrucker.addCosmicCredit(credits);
            Game.getIstance().moveBackward(winnerTrucker, days);
        }
        if(defeatedTruckers.isEmpty()){
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }else{
            cardState = CardState.HUMANSTOREMOVE;
            indexOfCurrentTrucker = 0;
            return new HumanCoordinates(true, defeatedTruckers.getFirst().getNickname(),defeatedTruckers.getFirst().getSpaceShip(), Game.getIstance().getTruckerList());
        }

    }


    /**
     * Sets the coordinates of the cabins from which the player that decided to land wants to
     * remove the crew members required, after checking if they are consistent
     * @param coordinates list of arrays of coordinates of the cabins
     *
     * @return serverEvent to send to clients
     */
    @Override
    public synchronized ServerEvent removeHumans(ArrayList<int[]> coordinates) throws GeneralError, InvalidCoordinates {
        if (cardState != CardState.HUMANSTOREMOVE) {
            throw new GeneralError("Card state in not HUMANSTOREMOVE");
        }
        if(defeatedTruckers.getFirst().getSpaceShip().getNumCrew() <= astronautsLost){

            defeatedTruckers.getFirst().getSpaceShip().removeAllHumans();

            defeatedTruckers.removeFirst();

            if(defeatedTruckers.isEmpty()) {
                cardState = CardState.ESECUZIONE;
                return cardEffect();
            }
            return new HumanCoordinates(true,defeatedTruckers.getFirst().getNickname(), defeatedTruckers.getFirst().getSpaceShip(), Game.getIstance().getTruckerList());
        }
        else{
            try {
                defeatedTruckers.getFirst().getSpaceShip().checkCabin(coordinates, astronautsLost);
                defeatedTruckers.getFirst().getSpaceShip().removeNumHuman(coordinates,astronautsLost);

                defeatedTruckers.removeFirst();

                if (defeatedTruckers.isEmpty()) {
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }
                return new HumanCoordinates(true, defeatedTruckers.getFirst().getNickname(),defeatedTruckers.getFirst().getSpaceShip(), Game.getIstance().getTruckerList());

            } catch (InvalidCoordinates e) {
                throw new InvalidCoordinates(e.getMessage());
            }
        }
    }

    /**
     * deactivate every type of component from every player's ship
     */
    private void deactivateTruckersSpaceShip(){
        for(Trucker trucker: Game.getIstance().getTruckerList()){
            trucker.getSpaceShip().deactivateDoubleCannons();
            trucker.getSpaceShip().deactivateDoubleEngines();
            trucker.getSpaceShip().deActivateShields();
        }
    }

    /**
     * Applies the changes provided by the card
     *
     * @return serverEvent to send to clients
     */
    @Override
    public synchronized ServerEvent cardEffect() throws GeneralError {
        if (cardState != CardState.ESECUZIONE) {
            throw new GeneralError("Card state is not ESECUZIONE");
        }

        deactivateTruckersSpaceShip();

        ArrayList<Trucker> truckerRemoved;
        truckerRemoved = Game.getIstance().endTurn();

        GamePersistence.saveGame(GameController.getIstance());

        if(Game.getIstance().getTruckerList().isEmpty()){
            int[] scores;
            Map<String, Integer> playersScores = new HashMap<>();
            scores = Game.getIstance().computeScores();
            for(int i=0; i<Game.getIstance().getNumPlayers(); i++){
                playersScores.put(Game.getIstance().getTruckerOut().get(i).getNickname(), scores[i]);
            }
            GamePersistence.deleteSave();
            return new EndGameEvent(playersScores);
        }

        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, Game.getIstance().getTrack());
    }


    public void setState(CardState cardState) {
        this.cardState = cardState;
    }

    public void addDefeatedTruckers(Trucker defeatedTruckers) {
        this.defeatedTruckers.add(defeatedTruckers);
    }

    public int getIndexOfCurrentTrucker() {
        return indexOfCurrentTrucker;
    }

    public ArrayList<Trucker> getDefeatedTruckers() {
        return defeatedTruckers;
    }

    public void setIndexOfCurrentTrucker(int indexOfCurrentTrucker) {
        this.indexOfCurrentTrucker = indexOfCurrentTrucker;
    }
}