package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
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
import java.util.HashMap;
import java.util.Map;

public class Planets extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<ArrayList<Goods>> planets;
    private final int daysLoss;
    private CardState cardState;

    private final ArrayList<Trucker> planetsOccupied;
    private final ArrayList<Trucker> truckersLanded;
    private int indexOfTrucker;

    private Goods currentGood;
    private final ArrayList<Goods> currentGoodsList;
    private ArrayList<Goods> goodsPassed;
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
    public Planets(boolean trialFlight, int daysLoss, int level, ArrayList<ArrayList<Goods>> planets, String frontImage, String backImage) {
        super("Planets", "A new planetary system discovered", trialFlight, level, frontImage, backImage);
        goodsPassed = new ArrayList<>();
        planetsOccupied = new ArrayList<>();
        for (int i = 0; i < planets.size(); i++) {
            this.planetsOccupied.add(null);
        }
        truckersLanded = new ArrayList<>();
        this.daysLoss = daysLoss;
        this.planets = planets;
        this.currGoodIndex = 0;
        this.indexOfTrucker = 0;
        this.cardState =CardState.PIANETI;
        this.currentGood = null;
        this.currentGoodsList = new ArrayList<>();
        this.goodsPassed = new ArrayList<>();

    }

    public CardState getCardState() {
        return cardState;
    }


    @Override
    public ServerEvent startCard() {
        StartCardEvent startCardEvent =
                new StartCardEvent(
                        this.getName(),
                        this.getDescription(),
                        Game.getIstance().getTruckerList().getFirst().getNickname(),
                        "vuoi atterrare (se no scrivere -1) e dove (indice pianeta)?",
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
                        ParserState.PLANET_DECISION
                );
        startCardEvent.setBank(Game.getIstance().getBank());
        return startCardEvent;
    }

    @Override
    public ServerEvent playerLandingPlanets(int indexOfPlanet) throws GeneralError, PlanetNotFreeException {
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
                indexOfTrucker = 0;
                currentGoodsList.addAll(planets
                        .get(planetsOccupied
                                .indexOf(truckersLanded
                                        .get(indexOfTrucker))));
                currentGoodsList.addAll(goodsPassed);
                if (indexOfTrucker < truckersLanded.size() - 1) {
                    for (Goods currentGood : planets.get(planetsOccupied.indexOf(truckersLanded.get(indexOfTrucker)))) {
                        if (Game.getIstance().getBank().getGoodsInBank(currentGood.getValue()) > 0) {
                            // abbiamo la merce corrente
                            this.cardState = CardState.GOODSTOADD;
                            return new PlotGood(true, truckersLanded.get(indexOfTrucker).getNickname(),truckersLanded.get(indexOfTrucker).getSpaceShip(),currentGood);
                        }
                    }
                    indexOfTrucker++;
                    return new PlanetDecision(true, planetsOccupied, Game.getIstance().getTrucker(indexOfTrucker).getNickname(),Game.getIstance().getTrucker(indexOfTrucker).getSpaceShip());
                }
                cardState = CardState.ESECUZIONE;
                return cardEffect();
            }
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }
        indexOfTrucker++;
        return new PlanetDecision(true, planetsOccupied, Game.getIstance().getTrucker(indexOfTrucker).getNickname(),Game.getIstance().getTrucker(indexOfTrucker).getSpaceShip());
    }

    @Override
    public ServerEvent setGoodsCoordinates(Boolean stop, int[] goodsCoordinates, int positionInLoad) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.GOODSTOADD) {
            throw new GeneralError("Card state is not GOODSTOADD");
        }
        if(!stop) {
            if (goodsCoordinates == null || !truckersLanded.get(indexOfTrucker).getSpaceShip().checkLoad(goodsCoordinates)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                Goods g; // bene che mi viene passato dalla nave di trucker se nel load c'era già qualcosa
                currentGood.setX(goodsCoordinates[0]);
                currentGood.setY(goodsCoordinates[1]);
                g=truckersLanded.get(indexOfTrucker).getSpaceShip().addGood(currentGood, goodsCoordinates, positionInLoad, Game.getIstance().getBank());
                if (g!=null){
                    goodsPassed.add(g);
                }
            } catch (InvalidPositionException e) {
                cardState = CardState.GOODSTOADD;
                throw e;
            }
        }else{
            goodsPassed.add(currentGood);
        }
        if(currGoodIndex >= currentGoodsList.size()){
            cardState = CardState.REARRANGE;
            return new RearrangeEvent(true, this.truckersLanded.get(indexOfTrucker).getNickname());
        }
        else{
            currGoodIndex++;
            //TODO: Check che non ci sia loop infinito
            do {
                currentGood = currentGoodsList.get(currGoodIndex);
                if (Game.getIstance().getBank().getGoodsInBank(currentGood.getValue()) >= 1) {
                    return new PlotGood(true, truckersLanded.get(indexOfTrucker).getNickname(),truckersLanded.get(indexOfTrucker).getSpaceShip(), currentGood);
                } else {
                    currGoodIndex++;
                    if(currGoodIndex>=currentGoodsList.size()) {
                        cardState = CardState.REARRANGE;
                        return new RearrangeEvent(true, this.truckersLanded.get(indexOfTrucker).getNickname());
                    }
                }
            }while(true);
        }
    }

    @Override
    public ServerEvent rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean stop) throws GeneralError, InvalidCoordinates, InvalidPositionException{
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
            // Tutti hanno finito
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        } else {
            // Passa al prossimo giocatore
            indexOfTrucker++;
            //creao nuova lista
            currentGoodsList.addAll(planets.get(planetsOccupied.indexOf(truckersLanded.get(indexOfTrucker))));
            currentGoodsList.addAll(goodsPassed);
            cardState = CardState.GOODSTOADD;
            return new PlanetDecision(true, planetsOccupied, Game.getIstance().getTrucker(indexOfTrucker).getNickname(),Game.getIstance().getTrucker(indexOfTrucker).getSpaceShip());
        }
    }

    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if (this.cardState != CardState.ESECUZIONE) {
            throw new GeneralError("CardEffect not callable in the state: " + this.cardState);
        }
        // muovo in dietro i giocatori ma partendo dal più indietro nella plancia non dal Leader
        for(int i = truckersLanded.size() - 1; i>=0; i--){
            Game.getIstance().moveBackward(truckersLanded.get(i), daysLoss);
        }
        cardState = CardState.EXIT;
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

