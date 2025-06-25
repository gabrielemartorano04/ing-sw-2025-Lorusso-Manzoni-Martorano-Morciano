package it.polimi.ingsw.view;

/**
 * Enumeration representing the different possible states of the parser during the game.
 * Each state corresponds to a specific point in the game where user input or specific actions
 * are required by the system.
 */
public enum ParserState {
    STATE,
    HUMAN_COORD,
    PLOT_GOOD,
    ROLL_DICE,
    PLANET_DECISION,
    ACTIVATE_CANNONS,
    ACTIVATE_ENGINES,
    ACTIVATE_SHIELD,
    DRAW_CARD,
    LAND, COSMIC_CREDITS,
    REMOTION_CONFIRMED,
    REARRANGE, REMOVE_BATTERIES,
    REMOVE_GOODS,
    WARZONE,
    DEAD,
    CURRENT_CONDITION,
    HUMAN_REMOVED, ASK_WITHDRAW, DO_NOTHING
}
