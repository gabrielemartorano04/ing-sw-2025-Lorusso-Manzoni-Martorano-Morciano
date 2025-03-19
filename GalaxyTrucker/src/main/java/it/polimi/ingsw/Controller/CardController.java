package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.List;

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
                    ArrayList<int[]> coordinates = askController.askAbandonedShip(trucker, currCard.getNumHuman());
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
                ((OpenSpace) card).cardEffect(game);
            }
            case "Pirates" -> {
                ((Pirates) card).cardEffect(game);
            }
            case "Planets" -> {
                ((Planets) card).cardEffect(game);
            }
            case "Slavers" -> {
                ((Slavers) card).cardEffect(game);
            }
            case "Smugglers" -> {
                ((Smugglers) card).cardEffect(game);
            }
            case "StarDust" -> {
                ((StarDust) card).cardEffect(game);
            }
            case "WarZone" -> {
                ((WarZone) card).cardEffect(game);
            }
            //lo metto per sicurezza ma penso sia inutile
            default -> throw new IllegalArgumentException("Nome carta non riconosciuto: " + cardName);
        }
    }
}
