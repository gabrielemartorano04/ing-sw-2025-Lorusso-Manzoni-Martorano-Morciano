package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.EndGameEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.exceptions.PlanetNotFreeException;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Planets extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * List of planets of the card
     */
    private final ArrayList<ArrayList<Goods>> planets;

    /**
     * flight days a player loses if he lands on a planet
     */
    private final int daysLoss;

    /**
     * current state of the card, helper for debug and checking consistency
     */
    private CardState cardState;

    /**
     * list of planets occupied. If the element in the list is null the planet is free otherwise
     * it's occupied by that trucker
     */
    private final ArrayList<Trucker> planetsOccupied;

    /**
     * list of players that decided to land on a planet
     */
    private ArrayList<Trucker> truckersLanded;

    /**
     * index of the current player to handle the fact that clients have to respect a specific turn
     */
    private int indexOfTrucker;

    /**
     * current good to gain by a player
     */
    private Goods currentGood;

    /**
     * current list of goods to be gained by a player
     */
    private final ArrayList<Goods> currentGoodsList;

    /**
     * list of goods passed by a player to the next one if some goods have been discarded by the current player
     */
    private ArrayList<Goods> goodsPassed;

    /**
     * index of the current good in the currentGoodsList
     */
    private int currGoodIndex;


    /**
     * Planets constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param daysLoss number of days of flight to lose
     * @param planets list of lists of goods to gain
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public Planets(boolean trialFlight, int daysLoss, int level, ArrayList<ArrayList<Goods>> planets, String frontImage, String backImage, int id) {
        super("Planets", "A new planetary system discovered", trialFlight, level, frontImage, backImage, id);
        goodsPassed = new ArrayList<>();
        planetsOccupied = new ArrayList<>();
        truckersLanded = new ArrayList<>();
        this.daysLoss = daysLoss;
        this.planets = planets;
        this.currGoodIndex = 0;
        this.indexOfTrucker = 0;
        this.cardState =CardState.PIANETI;
        this.currentGood = null;
        this.currentGoodsList = new ArrayList<>();
        this.goodsPassed = new ArrayList<>();

        for (int i = 0; i < this.planets.size(); i++) {
            this.planetsOccupied.add(null);
        }
    }

    public CardState getCardState() {
        return cardState;
    }


    /**
     * @return an event containing all the card details
     */
    @Override
    public synchronized ServerEvent startCard() {
        StartCardEvent startCardEvent =
                new StartCardEvent(
                        this.getName(),
                        this.getDescription(),
                        Game.getIstance().getTruckerList().getFirst().getNickname(),
                        "Do you want to land (if no type -1) and where (index planet)?",
                        -1,
                        -1,
                        -1,
                        null,
                        null,
                        -1,
                        -1,
                        -1,
                        null,
                        planets,
                        null,
                        null,
                        ParserState.PLANET_DECISION,
                        planetsOccupied,
                        Game.getIstance().getTrack(),
                        this
                );
        startCardEvent.setBank(Game.getIstance().getBank());
        return startCardEvent;
    }

    /**
     * handle the landing of the current player on a planet based on the indexOfPlanet
     *
     * @param indexOfPlanet: equals -1 if the current player doesn't want to land otherwise it containes the index of
     *                     the Planet where to land
     * @return serverEvent to send to clients
     * @throws PlanetNotFreeException: that planet is already occupied
     */
    @Override
    public synchronized ServerEvent playerLandingPlanets(int indexOfPlanet) throws GeneralError, PlanetNotFreeException {

        if(cardState != CardState.PIANETI){
            throw new GeneralError("Can't land in the state: " + this.cardState);
        }
        if(indexOfPlanet < -1 || indexOfPlanet >= this.planets.size()){
            throw new GeneralError("No planets at the index: " + indexOfPlanet);
        }
        if(planetsOccupied.contains(Game.getIstance().getTrucker(indexOfTrucker))){
            throw new GeneralError("This trucker has already chosen");
        }
        if(indexOfPlanet >= 0 && planetsOccupied.get(indexOfPlanet)!=null){
            throw new PlanetNotFreeException("You choose a planet that was already occupied");
        }
        if(indexOfPlanet != -1){
            planetsOccupied.set(indexOfPlanet, Game.getIstance().getTrucker(indexOfTrucker));
            truckersLanded.add(Game.getIstance().getTrucker(indexOfTrucker));
        }

        if(indexOfTrucker == Game.getIstance().getTruckerList().size() -1){
            if(!truckersLanded.isEmpty()) {
                return handleLandedTruckersGoodsLoading();
            }
            else{
                cardState = CardState.ESECUZIONE;
                return cardEffect();
            }
        }
        indexOfTrucker++;
        return new PlanetDecision(true, planetsOccupied, Game.getIstance().getTrucker(indexOfTrucker).getNickname(),Game.getIstance().getTrucker(indexOfTrucker).getSpaceShip(), Game.getIstance().getTruckerList());
    }


    /**
     * Initializes the loading phase with the first trucker who landed.
     * If there's at least one good available in the bank, transitions to GOODSTOADD.
     * Otherwise, proceeds with the next trucker or finishes the card effect.
     *
     * @return ServerEvent to send to client
     */
    private ServerEvent handleLandedTruckersGoodsLoading() throws GeneralError {
        indexOfTrucker = 0;
        currentGoodsList.addAll(planets.get(planetsOccupied.indexOf(truckersLanded.get(indexOfTrucker))));
        currentGoodsList.addAll(goodsPassed);

        if (indexOfTrucker < truckersLanded.size()) {
            for (Goods g : planets.get(planetsOccupied.indexOf(truckersLanded.get(indexOfTrucker)))) {
                if (Game.getIstance().getBank().getGoodsInBank(g.getValue()) > 0) {
                    this.cardState = CardState.GOODSTOADD;
                    this.currentGood = g;
                    return new PlotGood(true, truckersLanded.get(indexOfTrucker).getNickname(),truckersLanded.get(indexOfTrucker).getSpaceShip(),g, Game.getIstance().getTruckerList());
                }
            }
            indexOfTrucker++;
            return new PlanetDecision(true, planetsOccupied, Game.getIstance().getTrucker(indexOfTrucker).getNickname(),Game.getIstance().getTrucker(indexOfTrucker).getSpaceShip(), Game.getIstance().getTruckerList());
        }
        else{
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }
    }

    /**
     *
     * Adds the current good to the current trucker spaceship or handles the evolution of the card
     *
     * @param wantsToLoadGood true if the current player wants this good false otherwise
     * @param goodsCoordinates list of coordinatest
     * @param positionInLoad position in the load to insert the good
     * @return serverEvent to send to clients
     * @throws InvalidCoordinates coordinates are not of a load
     * @throws InvalidPositionException not a special load but that good was special
     */
    @Override
    public synchronized ServerEvent addGood(Boolean wantsToLoadGood, int[] goodsCoordinates, int positionInLoad) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.GOODSTOADD) {
            throw new GeneralError("Card state is not GOODSTOADD");
        }

        if(wantsToLoadGood) {
            if (goodsCoordinates == null || !truckersLanded.get(indexOfTrucker).getSpaceShip().checkLoad(goodsCoordinates)) {
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                Goods goodInTheShip; // bene che mi viene passato dalla nave di trucker se nel load c'era già qualcosa
                Goods goodToAdd = currentGood;
                goodToAdd.setX(goodsCoordinates[0]);
                goodToAdd.setY(goodsCoordinates[1]);

                goodInTheShip = truckersLanded.get(indexOfTrucker).getSpaceShip().addGood(goodToAdd, goodsCoordinates, positionInLoad, Game.getIstance().getBank());

                if (goodInTheShip!=null){
                    goodsPassed.add(goodInTheShip);
                }

            } catch (InvalidPositionException e) {
                cardState = CardState.GOODSTOADD;
                throw e;
            }
        }else{
            goodsPassed.add(currentGood);
        }
        if(currGoodIndex >= currentGoodsList.size()-1){
            cardState = CardState.REARRANGE;
            return new RearrangeEvent(true, this.truckersLanded.get(indexOfTrucker).getNickname(), Game.getIstance().getTruckerList());
        }
        else{
            currGoodIndex++;
            return proceedToNextAvailableGoodOrRearrange();
        }
    }


    /**
     * returns the next good available in the bank, that comes from the list of goods to gain from the winner.
     * If the bank is empty then it proceeds to rearrange.
     * @return serverEvent to send to clients
     */
    private ServerEvent proceedToNextAvailableGoodOrRearrange() {
        while (currGoodIndex < currentGoodsList.size()) {
            currentGood = currentGoodsList.get(currGoodIndex);
            if (Game.getIstance().getBank().getGoodsInBank(currentGood.getValue()) >= 1) {
                cardState = CardState.GOODSTOADD;
                return new PlotGood(true, truckersLanded.get(indexOfTrucker).getNickname(), truckersLanded.get(indexOfTrucker).getSpaceShip(), currentGood, Game.getIstance().getTruckerList());
            }
            currGoodIndex++;
        }
        cardState = CardState.REARRANGE;
        return new RearrangeEvent(true, truckersLanded.get(indexOfTrucker).getNickname(), Game.getIstance().getTruckerList());
    }


    /**
     * switch 2 goods to rearrange them in the ship
     * @param coord1 initial position
     * @param coord2 final position
     * @param value1 initial internal position
     * @param value2 final internat position
     * @param stop when to end the swaps
     * @return serverEvent to send to clients
     * @throws InvalidCoordinates coordinates are not valid
     * @throws InvalidPositionException tried to put a special good in a not special load
     */
    @Override
    public synchronized ServerEvent rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean stop) throws GeneralError, InvalidCoordinates, InvalidPositionException{
        if(cardState != CardState.REARRANGE){
            throw new GeneralError("Card state is not REARRANGE");
        }
        if (!stop) {
            if (coord1 == null && !truckersLanded.get(indexOfTrucker).getSpaceShip().checkLoad(coord1) && coord2 == null && !truckersLanded.get(indexOfTrucker).getSpaceShip().checkLoad(coord2)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                truckersLanded.get(indexOfTrucker).getSpaceShip().swapGoods(coord1, value1, coord2, value2);
            } catch (InvalidPositionException e) {
                cardState = CardState.REARRANGE;
                throw new InvalidPositionException("invalid positions");
            }
        }
        if (indexOfTrucker + 1 >= truckersLanded.size()) {
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }
        else {
            indexOfTrucker++;
            currGoodIndex=0;
            currentGoodsList.clear();
            currentGoodsList.addAll(planets.get(planetsOccupied.indexOf(truckersLanded.get(indexOfTrucker))));
            currentGoodsList.addAll(goodsPassed);
            currentGood=currentGoodsList.get(currGoodIndex);
            cardState = CardState.GOODSTOADD;

            return new PlotGood(true, truckersLanded.get(indexOfTrucker).getNickname(),truckersLanded.get(indexOfTrucker).getSpaceShip(), currentGood, Game.getIstance().getTruckerList());
        }
    }

    /**
     * Applies the changes provided by the card:
     * it moves backwards all the players that have decided to land
     *
     * @return serverEvent to send to clients
     */
    @Override
    public synchronized ServerEvent cardEffect() throws GeneralError {
        if (this.cardState != CardState.ESECUZIONE) {
            throw new GeneralError("CardEffect not callable in the state: " + this.cardState);
        }

        for(int i = truckersLanded.size() - 1; i>=0; i--){
            Game.getIstance().moveBackward(truckersLanded.get(i), daysLoss);
        }

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

    public ArrayList<ArrayList<Goods>> getPlanets() {
        return planets;
    }

    public void setState(CardState cardState) {
        this.cardState = cardState;
    }

    public int getIndexOfTrucker() {
        return indexOfTrucker;
    }

    public ArrayList<Goods> getCurrentGoodsList() {
        return currentGoodsList;
    }

    public int getCurrGoodIndex() {
        return currGoodIndex;
    }

    public ArrayList<Trucker> getTruckersLanded() {
        return truckersLanded;
    }

    public ArrayList<Trucker> getPlanetsOccupied() {
        return planetsOccupied;
    }

    public void setTruckersLanded(ArrayList<Trucker> truckerLanded) {
        this.truckersLanded = truckerLanded;
    }

    public void setCurrGood(Goods goods) {
        this.currentGood = goods;
    }
}

