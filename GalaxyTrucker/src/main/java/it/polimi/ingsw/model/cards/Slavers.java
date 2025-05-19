package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.GainCosmicCredits;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.HumanCoordinates;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.utilities.exceptions.NotEnoughBatteries;


import java.io.Serializable;
import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that manages the single card slavers
 * In order of position, starting from the leader, each trucker decides which double cannon wants to activate, using batteries, for the calculation of the cannon power.
 * The first trucker that has a cannon power which is higher than the slavers one, defeat them and decide if he/she wants, losing days of flight, to gain cosmic credits.
 * If the trucker's cannon power is equal to the slavers' one, nothing happens to the trucker, but the slavers continue to attack
 * For all the defeated truckers, they have to remove some crew members
 */
public class Slavers extends Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int slaversPower;
    private final int credits;
    private final int days;
    private final int astronautsLost;

    private CardState cardState;
    private int indexOfCurrentTrucker;
    private Trucker winnerTrucker; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private ArrayList<Trucker> defeatedTruckers;
    private boolean winDecision;

    private ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveHumans;
    // contiene le coordinate delle cabine scelte dall'utente per rimuovere gli umani. Queste coordinate sono organizzate
    // in liste di liste. La lista esterna è una per ogni trucker, la lista interna contiene la lista di coordinate per quel
    // trucker, mantenute nell'ordine dei trucker.

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
    public Slavers(boolean trialFlight, int level, int credits, int days, int astronautsLost, int slaversPower, String frontImage, String backImage ) {
        super("Slavers", "Intergalactic slave traders", trialFlight, level, frontImage, backImage);
        this.credits = credits;
        this.days = days;
        this.astronautsLost = astronautsLost;
        this.slaversPower = slaversPower;
        this.cardState = CardState.ACTIVATE_CANNONS;
        this.truckersCoordinatesToRemoveHumans = new ArrayList<>();
        this.indexOfCurrentTrucker = 0;
        this.winnerTrucker = null;
        this.defeatedTruckers = new ArrayList<>();
        this.winDecision = false;

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


    @Override
    public ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Do you want to activate your doubleCannons?",
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
                this.getCardState()
        );
    }

    private ServerEvent fightIsWin() throws GeneralError {
        if (Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() > slaversPower) {
            winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
            cardState = CardState.COSMIC_CREDITS;
            return new GainCosmicCredits(true, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), credits);
        }else if(Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() == slaversPower){
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()) {
                if(defeatedTruckers.isEmpty()){
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }else{
                    cardState = CardState.HUMANSTOREMOVE;
                    indexOfCurrentTrucker = 0;
                    return new HumanCoordinates(ServerEventType.HUMAN_COORDINATES, false, this.cardState, defeatedTruckers.getFirst().getNickname());
                }
            }else{
                indexOfCurrentTrucker++;
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, this.cardState, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }

        }else {
            defeatedTruckers.add(Game.getIstance().getTrucker(indexOfCurrentTrucker));
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()) {
                if(defeatedTruckers.isEmpty()){
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }else{
                    cardState = CardState.HUMANSTOREMOVE;
                    indexOfCurrentTrucker = 0;
                    return new HumanCoordinates(ServerEventType.HUMAN_COORDINATES, false, this.cardState, defeatedTruckers.getFirst().getNickname());
                }
            }else{
                indexOfCurrentTrucker++;
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, this.cardState, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }
        }
    }

    public ServerEvent activateCannons(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws GeneralError, InvalidComponentsException, NotEnoughBatteries {
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

    public ServerEvent setWinDecision(boolean answer)throws GeneralError{
        if(cardState!=CardState.COSMIC_CREDITS){
            throw new GeneralError("Card state is not COSMIC_CREDITS");
        }
        winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
        winDecision = answer;
        return cardEffect();
    }

    /**
     * Sets, for each trucker, the coordinates of the cabin where he/she want to remove the crew members
     * @param coordinates list of lists of arrays of coordinates
     */
    public ServerEvent setHumansCoordinates(ArrayList<int[]> coordinates) throws GeneralError, InvalidCoordinates {
        if (cardState != CardState.HUMANSTOREMOVE) {
            throw new GeneralError("Card state in not HUMANSTOREMOVE");
        }
        if(!defeatedTruckers.isEmpty() || indexOfCurrentTrucker<Game.getIstance().getTruckerList().size()) {
            try {
                defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().checkCabin(coordinates, astronautsLost);
                this.truckersCoordinatesToRemoveHumans.add(coordinates);
                playersDecisions++;
            } catch (InvalidCoordinates e) {
                throw new InvalidCoordinates(e.getMessage());
            }
            if (playersDecisions==numPlayer) {
                if(winnerTrucker !=null) {
                    cardState = CardState.CHOOSE_TO_GAIN;
                }else cardState = CardState.ESECUZIONE;
            }
        }else{
            if(winnerTrucker !=null) {
                cardState = CardState.CHOOSE_TO_GAIN;
            }else cardState = CardState.ESECUZIONE;
        }
        return cardState;
    }

    public CardState setChooseToGain(Boolean choice) throws GeneralError {
        if (cardState != CardState.GAIN) {
            throw new GeneralError("Card state is not GAIN");
        }
        this.choiceToGain = choice;
        cardState = CardState.ESECUZIONE;
        return cardState;
    }

    /**
     * Applies the changes provided by the card
     */
    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if (cardState != CardState.ESECUZIONE) {
            throw new GeneralError("Card state is not ESECUZIONE");
        }
        int i = 0;
        if(winnerTrucker !=null && winDecision){
            winnerTrucker.addCosmicCredit(credits);
            Game.getIstance().moveBackward(winnerTrucker, days);
        }
        if(!defeatedTruckers.isEmpty()) {
            for (Trucker trucker : defeatedTruckers) {
                trucker.getSpaceShip().removeNumHuman(truckersCoordinatesToRemoveHumans.get(i));
                i++;
            }
        }
        cardState=CardState.EXIT;
        return cardState;
    }



}