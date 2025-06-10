package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
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
    private final int slaversPower;
    private final int credits;
    private final int days;
    private final int astronautsLost;

    private CardState cardState;
    private int indexOfCurrentTrucker;
    private Trucker winnerTrucker; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private final ArrayList<Trucker> defeatedTruckers;
    private boolean winDecision;

    private final ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveHumans;
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
                "Choose if you want to activate the double cannons. If yes type the coordinates of the cannons then 'battery' and battery storage(s) coordinates . Type 'y, <x1> <y1>, <x2> <y2>,..., battery, <xs1> <ys1> - <numofbatteries>,...' or 'n': ",
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
                ParserState.ACTIVATE_CANNONS
        );
    }

    private ServerEvent fightIsWin() throws GeneralError {
        if (Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() > slaversPower) {
            winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
            cardState = CardState.COSMIC_CREDITS;
            return new GainCosmicCredits(true, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), credits, days);
        }else if(Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() == slaversPower){
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
                if(defeatedTruckers.isEmpty()){
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }else{
                    cardState = CardState.HUMANSTOREMOVE;
                    indexOfCurrentTrucker = 0;
                    return new HumanCoordinates(ServerEventType.HUMAN_COORDINATES, true, defeatedTruckers.getFirst().getNickname());
                }
            }else{
                indexOfCurrentTrucker++;
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, ParserState.ACTIVATE_CANNONS, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }

        }else {
            defeatedTruckers.add(Game.getIstance().getTrucker(indexOfCurrentTrucker));
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
                cardState = CardState.HUMANSTOREMOVE;
                indexOfCurrentTrucker = 0;
                return new HumanCoordinates(ServerEventType.HUMAN_COORDINATES, true, defeatedTruckers.getFirst().getNickname());
            }else{
                indexOfCurrentTrucker++;
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, ParserState.ACTIVATE_CANNONS, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }
        }
    }

    @Override
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

    @Override
    public ServerEvent setWinDecision(boolean answer)throws GeneralError{
        if(cardState!=CardState.COSMIC_CREDITS){
            throw new GeneralError("Card state is not COSMIC_CREDITS");
        }
        winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
        winDecision = answer;
        cardState = CardState.ESECUZIONE;
        return cardEffect();
    }

    /**
     * Sets, for each trucker, the coordinates of the cabin where he/she want to remove the crew members
     * @param coordinates list of lists of arrays of coordinates
     */
    @Override
    public ServerEvent setHumansCoordinates(ArrayList<int[]> coordinates) throws GeneralError, InvalidCoordinates {
        if (cardState != CardState.HUMANSTOREMOVE) {
            throw new GeneralError("Card state in not HUMANSTOREMOVE");
        }
        if(defeatedTruckers
                .get(indexOfCurrentTrucker)
                .getSpaceShip()
                .getNumCrew()
                <=astronautsLost){
            defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().removeAllHumans();
            defeatedTruckers.remove(indexOfCurrentTrucker);
            if(indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
                cardState = CardState.ESECUZIONE;
                return cardEffect();
            }
            indexOfCurrentTrucker++;
            return new RemotionConfirmed(ServerEventType.HUMAN_REMOVED,defeatedTruckers.get(indexOfCurrentTrucker - 1).getNickname(), defeatedTruckers.get(indexOfCurrentTrucker - 1).getSpaceShip(),"Batteries removed, press continue!");
        }
        try {
            defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().checkCabin(coordinates, astronautsLost);
            this.truckersCoordinatesToRemoveHumans.add(coordinates);
            indexOfCurrentTrucker++;
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        }
        if (indexOfCurrentTrucker==Game.getIstance().getTruckerList().size()-1) {
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }
        return new HumanCoordinates(ServerEventType.HUMAN_COORDINATES, true, defeatedTruckers.getFirst().getNickname());
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
        ArrayList<String> truckerRemoved;
        truckerRemoved = Game.getIstance().endTurn();
        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }
        GamePersistence.saveGame(GameController.getIstance());
        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, track);
    }


}