package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidPositionException;
import it.polimi.ingsw.utilities.exceptions.PlanetNotFreeException;

import java.io.Serializable;
import java.util.ArrayList;

public class Planets extends Card implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<ArrayList<Goods>> planets;
    private ArrayList<Trucker> planetsOccupied;
    private int daysLoss;
    private ArrayList<Trucker> truckersLanded;
    private int indexOfPlanet; // se -1 => non atterra
    private int indexOfTrucker;
    private CardState state;

    private Goods currentGood;
    private ArrayList<Goods> currentGoodsList;
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
        planets = new ArrayList<>();
        goodsPassed = new ArrayList<>();
        planetsOccupied = new ArrayList<>();
        for (int i = 0; i < planets.size(); i++) {
            this.planetsOccupied.add(null);
        }
        truckersLanded = new ArrayList<>();
        this.daysLoss = daysLoss;
        this.planets = planets;
        this.currGoodIndex = 0;
        this.indexOfPlanet = 0;
        this.indexOfTrucker = 0;
        this.state=CardState.ATTESA_PIANETI;
        this.currentGood = null;
        this.currentGoodsList = new ArrayList<>();
        this.goodsPassed = new ArrayList<>();

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
                        this.getState()
                );
        startCardEvent.setBank(Game.getIstance().getBank());
        return startCardEvent;
    }

    public ServerEvent playerLanding(int indexOfPlanet) throws GeneralError, PlanetNotFreeException {
        if(state != CardState.ATTESA_PIANETI){
            throw new GeneralError("Can't land in the state: " + this.state);
        }
        if(indexOfPlanet < -1 || indexOfPlanet >= this.planets.size()){
            throw new GeneralError("No planets at the index: " + indexOfPlanet);
        }
        if(planetsOccupied.contains(Game.getIstance().getTrucker(indexOfTrucker))){
            throw new GeneralError("This trucker has already chosen");
        }
        if(planetsOccupied.get(indexOfPlanet)!=null){
            throw new PlanetNotFreeException("You choose a planet that was already occupied");
        }
        if(indexOfPlanet != -1){
            planetsOccupied.set(indexOfPlanet, Game.getIstance().getTrucker(indexOfTrucker));
            truckersLanded.add(Game.getIstance().getTrucker(indexOfTrucker));
        }
        if(indexOfTrucker == Game.getIstance().getNumPlayers()){
            indexOfTrucker=0;
            currentGoodsList.addAll(planets.get(planetsOccupied.indexOf(truckersLanded.get(indexOfTrucker))));
            currentGoodsList.addAll(goodsPassed);
            while(indexOfTrucker < truckersLanded.size()){
                for(Goods currentGood : planets.get(planetsOccupied.indexOf(truckersLanded.get(indexOfTrucker)))){
                    if(Game.getIstance().getBank().getGoodsInBank(currentGood.getValue())>0){
                        // abbiamo la merce corrente
                        this.state = CardState.GOODSTOADD;
                        return new PlotGood(true, this.getState(), truckersLanded.get(indexOfTrucker).getNickname(), currentGood);
                    }
                }
                indexOfTrucker++;
            }
            return cardEffect();
        }
        indexOfTrucker++;
        return new PlanetDecision(true, planetsOccupied, Game.getIstance().getTrucker(indexOfTrucker).getNickname());
    }

    public ServerEvent setGoodsCoordinates(Boolean decision, int[] goodsCoordinates, int value, Bank bank) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (state != CardState.GOODSTOADD) {
            throw new GeneralError("Card state is not GOODSTOADD");
        }
        if(decision) {
            if (goodsCoordinates == null || !truckersLanded.get(indexOfTrucker).getSpaceShip().checkLoad(goodsCoordinates)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                Goods g; // bene che mi viene passato dalla nave di trucker se nel load c'era già qualcosa
                g=truckersLanded.get(indexOfTrucker).getSpaceShip().addGood(currentGood, goodsCoordinates, value, bank);
                if (g!=null){
                    goodsPassed.add(g);
                }
            } catch (InvalidPositionException e) {
                state = CardState.GOODSTOADD;
                throw e;
            }
        }else{
            goodsPassed.add(currentGood);
        }
        if(currGoodIndex >= currentGoodsList.size()){
            state = CardState.REARRANGE;
            return new RearrangeEvent(true, this.getState(), this.truckersLanded.get(indexOfTrucker).getNickname());
        }
        else{
            currGoodIndex++;
            //TODO: Check che non ci sia loop infinito
            do {
                currentGood = currentGoodsList.get(currGoodIndex);
                if (bank.getGoodsInBank(currentGood.getValue()) >= 1) {
                    return new PlotGood(true, this.getState(), truckersLanded.get(indexOfTrucker).getNickname(), currentGood);
                } else {
                    currGoodIndex++;
                    if(currGoodIndex>=currentGoodsList.size()) {
                        state = CardState.REARRANGE;
                        return new RearrangeEvent(true, this.getState(), this.truckersLanded.get(indexOfTrucker).getNickname());
                    }
                }
            }while(true);
        }
    }

    public ServerEvent rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean stop) throws GeneralError, InvalidCoordinates, InvalidPositionException{
        if(state!= CardState.REARRANGE){
            throw new GeneralError("Card state is not REARRANGE");
        }
        if (!stop) {
            if (coord1 == null && !truckersLanded.get(indexOfTrucker).getSpaceShip().checkLoad(coord1) && coord2 == null && !truckersLanded.get(indexOfTrucker).getSpaceShip().checkLoad(coord2)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                truckersLanded.get(indexOfTrucker).getSpaceShip().swapGoods(coord1, value1, coord2, value2);
            } catch (InvalidPositionException e) {
                state = CardState.REARRANGE;
                throw new InvalidPositionException("invalid positions");
            }
        }
        if (indexOfTrucker + 1 >= truckersLanded.size()) {
            // Tutti hanno finito
            state = CardState.ESECUZIONE;
            return cardEffect();
        } else {
            // Passa al prossimo giocatore
            indexOfTrucker++;
            //creao nuova lista
            currentGoodsList.addAll(planets.get(planetsOccupied.indexOf(truckersLanded.get(indexOfTrucker))));
            currentGoodsList.addAll(goodsPassed);
            state = CardState.GOODSTOADD;
            return new PlotGood(true, this.getState(), truckersLanded.get(indexOfTrucker).getNickname(), currentGood);
        }
    }

    public CardState getState() {
        return state;
    }

    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if (this.state != CardState.ESECUZIONE) {
            throw new GeneralError("CardEffect not callable in the state: " + this.state);
        }
        // muovo in dietro i giocatori ma partendo dal più indietro nella plancia non dal Leader
        for(int i = truckersLanded.size() - 1; i>=0; i--){
            Game.getIstance().moveBackward(truckersLanded.get(i), daysLoss);
        }
        state = CardState.EXIT;
        return new CardFinished(true, this.getState(), Game.getIstance().getTruckerList());
    }

}

