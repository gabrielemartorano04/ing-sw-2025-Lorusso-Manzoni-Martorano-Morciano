package it.polimi.ingsw.model;

public enum GameState {
    SETUP, // Attesa giocatori
    WAITING,
    BUILDING,    // Setup in corso
    FLIGHT, // Partita in corso
    ENDED     // Partita terminata
}

