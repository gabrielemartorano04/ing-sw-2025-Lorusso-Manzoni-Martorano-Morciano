package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.PlanetDecision;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.PlotGood;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.StartCardEvent;
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
            planetsOccupied.add(indexOfPlanet, Game.getIstance().getTrucker(indexOfTrucker));
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
        if (goodsCoordinates==null || !truckersLanded.get(indexOfTrucker).getSpaceShip().checkLoad(goodsCoordinates)) { //check load deve controllare il singolo componente
            throw new InvalidCoordinates("Not goods deposit");
        }
        try{
            truckersLanded.get(indexOfTrucker).getSpaceShip().addGood(currentGood, goodsCoordinates, value, bank);
            bank.removeGoodsInBank(currentGood.getValue());
        }catch (InvalidPositionException e) {
            state = CardState.GOODSTOADD;
            throw e;
        }
        if(currGoodIndex == currentGoodsList.size()){
            state = CardState.REARRANGE;
            return new BooleanDecision(ServerEventType.REARRANGE, false, this.getState(), "Vuoi riarrangiare le merci dentro la nave?", truckersLanded.get(indexOfTrucker).getNickname());
        }
        else{
            currGoodIndex++;
            //TODO: Check che non ci sia loop infinito
            do {
                if (bank.getGoodsInBank(currentGood.getValue()) >= 1) {
                    state = CardState.CHOOSE_TO_GAIN;
                    return new PlotGood(true, this.getState(), truckersLanded.get(indexOfTrucker).getNickname(), currentGood);
                } else {
                    currGoodIndex++;
                    if(currGoodIndex>=currentGoodsList.size()) {
                        state = CardState.REARRANGE;
                        return new BooleanDecision(ServerEventType.REARRANGE, false, this.getState(), "Vuoi riarrangiare le merci dentro la nave?", truckersLanded.get(indexOfTrucker).getNickname());
                    }
                }
            }while(true);
        }
    }

    public CardState getState() {
        return state;
    }

    @Override
    public ServerEvent cardEffect(){
        return null;

    }
}
