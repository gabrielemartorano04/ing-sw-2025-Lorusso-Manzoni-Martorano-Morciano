package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Components.Goods;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.SpaceShip;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Il Card controller ha la funzione di gestire le chiamate ai vari CardEffect delle carte girate dal Game controller
 * Card controller deve gestire le ask all'utente per tutte le scelte da fare nelle carte, salvare i dati ottenuti con
 * queste ask dentro le variabili messe nelle carte tramite i set di modo che una volta salvate le variabili relative
 * alle scelte dell'utente posso chiamare direttamente il cardEffect senza dover preoccuparmi di altro
 */
public class CardController {
    private final AskController askController;
    private final Game game;

    public CardController(AskController askController, Game game) {
        this.askController = askController;
        this.game = game;
    }
    public void applyCardEffect(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("La carta passata è nulla!");
        }

        String cardName = card.getName();

        switch (cardName) {

            case "AbandonedShip" -> {
                AbandonedShip currCard = (AbandonedShip) card;
                boolean decisionTaken = false;
                List<Trucker> truckers = game.getTruckerList();
                for (Trucker trucker : truckers) {
                    //non chiedo a un trucker che in ogni caso non avrebbe umani a sufficienza
                    if(trucker.getSpaceShip().getNumHuman() <= currCard.getNumHuman()) {
                        continue;
                    }
                    ArrayList<int[]> coordinates = askController.askCabinToRemovePeople(trucker, currCard.getNumHuman());
                    if(!coordinates.isEmpty()) {
                        decisionTaken = true;
                        currCard.setTruckerThatHasDecided(trucker);
                        currCard.setCoordinates(coordinates);
                        currCard.cardEffect(game);
                    }
                    if(decisionTaken){
                        break;
                    }
                }
            }

            case "AbandonedStation" -> {
                AbandonedStation currCard = (AbandonedStation) card;
                boolean decisionTaken = false;
                List<Trucker> truckers = game.getTruckerList();
                for (Trucker trucker : truckers) {
                    //non chiedo a un trucker che non ha almeno il numero di umani indicati dalla carta
                    //NOTA: è diverso da sopra perchè sopra se ho lo stesso numero di umani non posso altrimenti
                    //mi devo ritirare, mentre qui non mi vengono rimossi
                    if(trucker.getSpaceShip().getNumHuman() < currCard.getNumHuman()) {
                        continue;
                    }
                    ArrayList<int[]> coordinates = askController.askAbandonedStation(trucker, currCard.getGoods());
                    if(!coordinates.isEmpty()) {
                        decisionTaken = true;
                        currCard.setTruckerThatHasDecided(trucker);
                        currCard.setCoord(coordinates);
                        currCard.cardEffect(game);
                    }
                    if(decisionTaken){
                        break;
                    }
                }
            }

            case "Epidemic" -> {
                ((Epidemic) card).cardEffect(game);
            }

            case "MeteorShower" -> {
                ((MeteorShower) card).cardEffect(game);
            }

            case "OpenSpace" -> {

                OpenSpace currCard = (OpenSpace) card;
                List<Trucker> truckers = game.getTruckerList();
                //lista che contiene per ogni trucker la lista di coordinates dei motori doppi che vuole attivare
                //la lista esterna associa ogni puntatore a un trucker, quindi la lunghezza della lista esterna è quella di truckers.
                //mantiene l'ordine dei truckers nel fare questa associazione
                ArrayList<ArrayList<int[]>> truckersCoordinates = new ArrayList<>(truckers.size());

                // batteryCoordinates andrà passato a una funzione di SpaceShip che qui chiamo removeBatteries che deve
                // ancora essere implementata
                ArrayList<int[]> batteryCoordinates = new ArrayList<>();
                int i = 0;
                for (Trucker trucker : truckers) {
                    truckersCoordinates.add(askController.askDoubleEnginesToActivate());
                    batteryCoordinates = askController.askBatteryStorageToRemoveFrom(truckersCoordinates.get(i).size());
                    trucker.getSpaceShip().removeBatteries(batteryCoordinates);
                    i++;
                }
                currCard.setCoordinates(truckersCoordinates);
                currCard.cardEffect(game);
            }

            case "Pirates" -> {
                // fase iniziale capire se qualcuno può o meno sconfiggere i pirati e se si chi li sconfigge
                // devo quindi calcolare la potenza di fuoco per ognuno
                Pirates currCard = (Pirates) card;
                ArrayList<Trucker> defeatedTruckers = new ArrayList<>();
                ArrayList<Trucker> truckers = game.getTruckerList();
                ArrayList<int[]> coordinates;
                boolean piratesDefeated = false;
                int currTruckerCannonPower;
                for (Trucker trucker : truckers) {
                    coordinates = askController.askDoubleCannonToActivate();
                    currTruckerCannonPower = trucker.getSpaceShip().getTotalCannonPower(coordinates);
                    if(currTruckerCannonPower<currCard.getPiratesPower()){
                        defeatedTruckers.add(trucker);
                    }else if(currTruckerCannonPower>currCard.getPiratesPower()){
                        piratesDefeated = true;
                        if(askController.askPirates(trucker)){
                            currCard.setWinnerTrucker(trucker);
                        }
                        currCard.setDefeatedTruckers(defeatedTruckers);
                        break;
                    }else if (currTruckerCannonPower==currCard.getPiratesPower()){
                        continue;
                    }
                }
                //se non ho sconfitto i pirati e sono uscito dal for vuol dire che tutti i giocatori hanno perso
                //o che tutti i giocatori avevano la stessa potenza di fuoco, in ogni caso devo settare defeatedTruckers
                //della carta pirati corrente per poter chiamare il suo cardEffect
                if(!piratesDefeated){
                    currCard.setDefeatedTruckers(defeatedTruckers);
                }
                currCard.cardEffect(game);
            }

            case "Planets" -> {
                Planets currCard = (Planets) card;
                ArrayList<ArrayList<Goods>> availablePlanets = currCard.getPlanets();
                ArrayList<Trucker> truckers = game.getTruckerList();
                ArrayList<ArrayList<int[]>> truckersCoordinates = new ArrayList<>(truckers.size());
                int indexOfPlanetChoosen;
                int[] playersLanded = new int[truckers.size()];
                int i = 0;

                // planetsLanded[i] = 1 se quel pianeta è già stato scelto altrimenti vale 0 sono già sicuro che la dimensione
                // non sarà 0 perché creiamo carte pianeti che hanno almeno 2 pianeti
                int[] planetsLanded = new int[availablePlanets.get(0).size()];
                for(int i = 0; i < planetsLanded.length; i++){
                    planetsLanded[i] = 0;
                }

                for(Trucker trucker : truckers) {
                    indexOfPlanetChoosen = askController.askPlanets(trucker, availablePlanets, planetsLanded);
                    //se la risposta è un indice diverso da -1 segno che quel trucker vuole atterrare
                    // e dopo chiedo le coordinate di dove caricare le merci
                    // NOTA: faccio la richiesta delle coordinate dopo perché posso occupare un pianeta anche senza caricare merci
                    // quindi restituendo una lista di coordinates nulla
                    if(indexOfPlanetChoosen!=-1){
                        playersLanded[i] = 1;
                        planetsLanded[indexOfPlanetChoosen] = 1;
                        truckersCoordinates.add(askController.askWhereToAddGoodsList(trucker, availablePlanets.get(indexOfPlanetChoosen)));
                    }
                    if(indexOfPlanetChoosen == -1){
                        playersLanded[i] = 0;
                    }
                }
                currCard.setPlayersLanded(playersLanded);
                currCard.setTruckersCoordinates(truckersCoordinates);
                currCard.cardEffect(game);
            }

            case "Slavers" -> {
                Slavers currCard = (Slavers) card;
                ArrayList<Trucker> defeatedTruckers = new ArrayList<>();
                ArrayList<Trucker> truckers = game.getTruckerList();
                ArrayList<int[]> coordinates;
                ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveHumans = new ArrayList<>(truckers.size());
                boolean slaversDefeated = false;
                int currTruckerCannonPower;

                for (Trucker trucker : truckers) {
                    coordinates = askController.askDoubleCannonToActivate();
                    currTruckerCannonPower = trucker.getSpaceShip().getTotalCannonPower(coordinates);
                    if(currTruckerCannonPower<currCard.getSlaversPower()){
                        defeatedTruckers.add(trucker);
                        truckersCoordinatesToRemoveHumans.add(askController.askCabinToRemovePeople(trucker, currCard.getAstronautsLost()));
                    }else if(currTruckerCannonPower>currCard.getSlaversPower()){
                        slaversDefeated = true;
                        if(askController.askSlavers(trucker)){
                            currCard.setWinnerTrucker(trucker);
                        }
                        currCard.setDefeatedTruckers(defeatedTruckers);
                        currCard.setTruckersCoordinatesToRemoveHumans(truckersCoordinatesToRemoveHumans);
                        break;
                    }else if (currTruckerCannonPower==currCard.getSlaversPower()){
                        continue;
                    }
                }
                //se non ho sconfitto gli slavers e sono uscito dal for vuol dire che tutti i giocatori hanno perso
                //o che tutti i giocatori avevano la stessa potenza di fuoco, in ogni caso devo settare defeatedTruckers
                //della carta pirati corrente per poter chiamare il suo cardEffect
                if(!slaversDefeated){
                    currCard.setDefeatedTruckers(defeatedTruckers);
                    currCard.setTruckersCoordinatesToRemoveHumans(truckersCoordinatesToRemoveHumans);
                }
                currCard.cardEffect(game);
            }

            case "Smugglers" -> {
                // fase iniziale capire se qualcuno può o meno sconfiggere i pirati e se si chi li sconfigge
                // devo quindi calcolare la potenza di fuoco per ognuno
                Smugglers currCard = (Smugglers) card;
                ArrayList<Trucker> defeatedTruckers = new ArrayList<>();
                ArrayList<Trucker> truckers = game.getTruckerList();
                ArrayList<int[]> coordinates;
                //solita convenzione per liste di trucker con liste di coordinate
                ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveGoods = new ArrayList<>(truckers.size());
                boolean piratesDefeated = false;
                int currTruckerCannonPower;

                for (Trucker trucker : truckers) {
                    coordinates = askController.askDoubleCannonToActivate();
                    currTruckerCannonPower = trucker.getSpaceShip().getTotalCannonPower(coordinates);
                    if(currTruckerCannonPower<currCard.getSmugglersPower()){
                        defeatedTruckers.add(trucker);
                        truckersCoordinatesToRemoveGoods.add(askController.askWhereToRemoveGoods(trucker, currCard.getGoodsLost()));
                    }else if(currTruckerCannonPower>currCard.getSmugglersPower()){
                        piratesDefeated = true;
                        if(askController.askPirates(trucker)){
                            currCard.setWinnerTrucker(trucker);
                            currCard.setWinnerCoordinatesToAddGoods(askController.askWhereToAddGoods(trucker, currCard.getGoodsGained()));
                        }
                        currCard.setDefeatedTruckers(defeatedTruckers);
                        currCard.setTruckersCoordinatesToRemoveGoods(truckersCoordinatesToRemoveGoods);
                        break;
                    }else if (currTruckerCannonPower==currCard.getSmugglersPower()){
                        continue;
                    }
                }
                //se non ho sconfitto i pirati e sono uscito dal for vuol dire che tutti i giocatori hanno perso
                //o che tutti i giocatori avevano la stessa potenza di fuoco, in ogni caso devo settare defeatedTruckers
                //della carta pirati corrente per poter chiamare il suo cardEffect
                if(!piratesDefeated){
                    currCard.setDefeatedTruckers(defeatedTruckers);
                    currCard.setTruckersCoordinatesToRemoveGoods(truckersCoordinatesToRemoveGoods);
                }
                currCard.cardEffect(game);
            }

            case "StarDust" -> {
                ((StarDust) card).cardEffect(game);
            }

            case "WarZone" -> {
                //da finire
                WarZone currCard = (WarZone) card;
                ArrayList<Trucker> truckers = game.getTruckerList();
                ArrayList<Condition> conditionsOrder = currCard.getConditionsOrder();
                Trucker weakestTrucker = null;
                Map<Condition, Effect> penalties = currCard.getPenalties();
                ArrayList<int[]> shoots = currCard.getShoots();

                //variabile usata per salvare per ogni trucker le coordinate dei cannoni che i giocatori vogliono attivare
                ArrayList<ArrayList<int[]>> truckersCannonsCoordinates = new ArrayList<>(truckers.size());

                //variabile usata per salvare per ogni trucker le coordinate dei motori che i giocatori vogliono attivare
                ArrayList<ArrayList<int[]>> truckersEnigesCoordinates = new ArrayList<>(truckers.size());


                // zona di guerra non viene fatta se c'è solo un giocatore rimasto
                if(truckers.size()==1){
                    return;
                }
                for (Condition condition : conditionsOrder) {
                    switch (condition) {
                        case HUMANS ->
                            weakestTrucker = currCard.findWeakestTrucker(truckers, condition, new ArrayList<>());

                        case ENGINES -> {
                            for (Trucker trucker : truckers) {
                                truckersEnigesCoordinates.add(askController.askDoubleEnginesToActivate());
                            }
                            weakestTrucker = currCard.findWeakestTrucker(truckers, condition, truckersEnigesCoordinates);
                        }


                        case CANNONS -> {
                            for (Trucker trucker : truckers) {
                                truckersEnigesCoordinates.add(askController.askDoubleCannonToActivate());
                            }
                            weakestTrucker = currCard.findWeakestTrucker(truckers, condition, truckersCannonsCoordinates);
                        }
                    }

                    if (weakestTrucker != null) {
                        // devo gestire le ulteriori richieste per i casi dove vieni sparato perché devo chiedere al giocatore
                        // se vuole attivare lo scudo ogni volta che il colpo è piccolo
                        // e per il caso dove devi perdere equipaggio che devi scegliere da dove perdere umani o alieni
                        Effect effect = penalties.get(condition);
                        switch(effect.getType()){
                            case LOSE_DAYS -> game.moveBackward(weakestTrucker, effect.getValue());
                            case LOSE_HUMANS -> {
                                weakestTrucker.getSpaceShip().removeNumHuman(askController.askCabinToRemovePeople(weakestTrucker, effect.getValue()));
                            }
                            case LOSE_GOODS -> weakestTrucker.getSpaceShip().removeGoods(effect.getValue(), askController.askWhereToRemoveGoods(weakestTrucker, effect.getValue()));
                            case GET_SHOT -> {
                                SpaceShip ship = weakestTrucker.getSpaceShip();
                                for (int[] shot : shoots) {
                                    boolean isHeavy = (shot[0] == 1);
                                    int direction = shot[1];
                                    int roll = game.rollDice();
                                    if (ship.isHit(roll, direction)) {
                                        if (isHeavy) {
                                            ship.destroyComponent(roll, direction);
                                        } else {
                                            if (!ship.blockLightCannonShot(direction, roll, askController.askToUseShield(weakestTrucker, roll, direction))) {
                                                ship.destroyComponent(roll, direction);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }

            //lo metto per sicurezza ma penso sia inutile
            default -> throw new IllegalArgumentException("Nome carta non riconosciuto: " + cardName);
        }
    }
}
