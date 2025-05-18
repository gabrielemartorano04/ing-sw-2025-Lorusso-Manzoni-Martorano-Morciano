package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.PlanetDecision;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.exceptions.PlanetNotFreeException;

import java.io.Serializable;
import java.util.ArrayList;

public class Planets extends Card implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<ArrayList<Goods>> planets;
    private ArrayList<Goods> goodsPassed;
    private ArrayList<Trucker> planetsOccupied;
    private int daysLoss;
    private ArrayList<Trucker> truckersLanded;
    private int currGoodFromPassed;
    private int currGoodFromPlanet;
    private int indexOfPlanet; // se -1 => non atterra
    private int indexOfTrucker;
    private CardState state;








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
        this.currGoodFromPassed = 0;
        this.currGoodFromPlanet = 0;
        this.indexOfPlanet = 0;
        this.indexOfTrucker = 0;
        this.state=CardState.ATTESA_PIANETI;

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
            if(Game.getIstance().getBank().getGoodsInBank(planets.get(planetsOccupied.indexOf(truckersLanded.get(indexOfTrucker))).get(currGoodFromPlanet).getValue())){}

            this.state = CardState.;
            return ;
        }
        indexOfTrucker++;
        return new PlanetDecision(true, planetsOccupied, Game.getIstance().getTrucker(indexOfTrucker).getNickname());
    }

    public CardState getState() {
        return state;
    }

    @Override
    public ServerEvent cardEffect(){
        return null;

    }
}
