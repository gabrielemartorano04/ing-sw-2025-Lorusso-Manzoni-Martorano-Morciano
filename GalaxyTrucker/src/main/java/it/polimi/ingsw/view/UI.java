package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.AlienCabinCoord;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.FlightStartEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.AvailableColorsEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.ColorAlreadyInError;
import it.polimi.ingsw.net.events.serverevents.setupevents.NicknameAlreadyInError;

import java.util.*;

/**
 * Interface representing the User Interface (UI) component of the application.
 * It provides methods to interact with the user across different stages of the game,
 * such as player setup, building phase, flight phase, and end game scenarios.
 * It's implemented by TUI and GUI
 */
public interface UI {

    /**
     * Displays the welcome screen for previously assigned players with their chosen colors and nicknames.
     * @param colors A list of colors chosen by the players. Each color corresponds to a player.
     * @param nicknames A list of nicknames provided by the players. Each nickname corresponds to a player.
     */
    void welcomeOldScene(ArrayList<PlayerColors> colors, ArrayList<String> nicknames);

    // WELCOME + CREATION

    /**
     * Displays a screen or prompt that allows the user to choose whether to resume a previously started game
     * or proceed with a new game
     */
    void showChooseResume();

    /**
     * Displays a prompt or screen to let the user input/select the number of players
     * who will participate in the game session.
     */
    void showInsertNumPlayers();

    /**
     * Displays the welcome scene, allowing the user to select their preferred
     * color from the available options and proceed with initialization.
     * @param event An object containing the set of available colors and a message
     *              prompting the user to choose a color.
     */
    void welcomeScene(AvailableColorsEvent event);


    /**
     * Handles the event when a player attempts to select a nickname that is already in use by another player     *
     * @param event An instance of NicknameAlreadyInError containing the details of the conflict,
     *              including the already-taken nickname and the set of available colors.
     */
    void nickNameAlreadyTaken(NicknameAlreadyInError event);

    /**
     * Handles the event triggered when a player attempts to select a color that is already in use by another player
     * @param event An instance of ColorAlreadyInError containing the details of the error, such as the color
     *              already in use and the set of available colors.
     */
    void colorAlreadyTaken(ColorAlreadyInError event);

    // BUILDING

    /**
     * Handles the initiation of the building phase for constructing a spaceship
     * @param event An instance of StartBuildingEvent containing the details of the building phase,
     *              including the player's spaceship, the time remaining on the hourglass,
     *              the list of available components, the player's nickname, the list of all players,
     *              and the game level.
     */
    void startBuildingScene(StartBuildingEvent event);

    /**
     * Handles the event triggered when a component is successfully linked
     * to a specific position on the game board.
     * @param event An instance of ComponentLinkedEvent containing details
     *              about the linked component, the position (row and column),
     *              and the list of all available components.
     */
    void componentLinked(ComponentLinkedEvent event);

    /**
     * Handles the event triggered when a specific component becomes unavailable.
     * @param event An instance of ComponentNotAvailableError containing details
     *              about the unavailability of the component.
     */
    void componentNotAvailable(ComponentNotAvailableError event);

    /**
     * Handles the event triggered when a player selects a component.
     * @param event An instance of ComponentSelectedEvent containing details
     *              about the selected component, such as its index in the list
     *              and the nickname of the player who made the selection.
     */
    void componentSelected(ComponentSelectedEvent event);

    /**
     * Handles the event triggered when a player provides invalid coordinates.
     * @param event An instance of InvalidCoordinatesError containing details about the error,
     *              including the username of the player who inserted the invalid coordinates.
     */
    void invalidCoordinates(InvalidCoordinatesError event);

    /**
     * Handles the event triggered when the hourglass cannot be turned
     * at the current moment in the game.
     * @param event An instance of TurnHourglassError containing the details
     *              of why the hourglass cannot be turned, such as the current
     *              game state and the associated error message.
     */
    void turnHourGlassError(TurnHourglassError event);


    /**
     * Handles the action of turning the hourglass in the game for a specified duration
     * @param durationSeconds The duration, in seconds, for which the hourglass will run.
     */
    void turnHourGlass(int durationSeconds);

    /**
     * Handles the event triggered when the building phase of the game is completed
     * @param event An instance of BuildCompleteEvent containing the details of the building phase completion,
     *              including the list of all players (truckers) involved.
     */
    void buildingComplete(BuildCompleteEvent event);

    /**
     * Handles the event triggered when a specific component is kept by a player
     * from the available components.
     * @param event An instance of KeptComponentEvent containing details about
     *              the kept component and the list of available components.
     */
    void keptComponent(KeptComponentEvent event);

    /**
     * Handles the event triggered when a component is successfully rotated.
     * @param event An instance of TurnedComponentSuccessEvent containing details
     *              about the rotated component.
     */
    void componentTurned(TurnedComponentSuccessEvent event);

    /**
     * Handles the event triggered when a player successfully selects a kept component.
     * @param event An instance of SuccessSelectKept containing details about the selected component,
     *              including its index in the kept list and the component itself.
     */
    void keptSelected(SuccessSelectKept event);

    /**
     * Handles the event related to the coordinates where aliens can be placed in the spaceship
     * @param event An instance of AlienCabinCoord that contains the list of available positions
     *              for placing aliens in the spaceship and other associated details.
     */
    void askAlienCabin(AlienCabinCoord event);

    /**
     * Displays the details of a card stack
     * @param stack The stack to be displayed, containing a collection of cards
     *              and associated attributes such as selection status, size,
     *              and index.
     */
    void showStack(Stack stack);

    /**
     * Handles the event triggered when a stack drop action is successfully processed.
     * @param event An instance of StackDropOk containing details about the stack drop
     *              action, including whether the event is targeted for a specific client
     *              or all clients.
     */
    void dropStack(StackDropOk event);

    /**
     * Refreshes the list of available components that can be used by the players
     */
    void refreshAvailableComponents();

    // FLIGHT

    /**
     * Handles the initiation of the flight phase in the game, transitioning
     * players and their spaceships into this new stage.
     * @param event An instance of FlightStartEvent containing details about the start
     *              of the flight phase, such as the list of players, their spaceships,
     *              the flight conditions, and other relevant data.
     */
    void flightScene(FlightStartEvent event);

    /**
     * Displays the current condition affecting the game or its components
     * @param condition The current condition to be displayed, based on the predefined set
     *                  of conditions (e.g., HUMANS, ENGINES, CANNONS).
     * @param isLeader  A boolean value indicating whether the current player is the leader
     *                  (true if they are the leader, false otherwise). This may tailor the
     *                  display or emphasize specific visuals for the leader.
     */
    void showCurrCondition(Condition condition, boolean isLeader);

    /**
     * Handles the event triggered when a card is finished or resolved during the game.
     * @param event An instance of CardFinished containing details about the finished card,
     *              such as the card type, the associated game effects, and any pertinent
     *              data related to the card resolution.
     */
    void cardFinished(CardFinished event);

    /**
     * Handles the event triggered when an epidemic card is drawn or an epidemic-related effect occurs during the game
     * @param event An instance of EpidemicEvent containing details about the epidemic, such as the type of epidemic,
     *              its effects on the players or game components, and any associated game state changes.
     */
    void epidemic(EpidemicEvent event);

    /**
     * Displays the format in which the user is expected to input data
     * @param parserState An instance of ParserState representing the current state of the parser.
     *                    It contains the necessary details to determine and display the correct
     *                    input format based on the application context.
     */
    void showInputFormat(ParserState parserState);

    /**
     * Handles the given StarDustEvent, performing the necessary operations related to the event
     * @param event the StarDustEvent object containing the details of the event to be processed
     */
    void stardust(StarDustEvent event);

    /**
     * Displays an error message to the user
     * @param message the error message to be displayed
     */
    void showError(String message);

    /**
     * Displays information or a message related to a trucker who has withdrawn
     * @param nickname the nickname of the trucker who has withdrawn
     */
    void showTruckerWithdrawn(String nickname);

    /**
     * Displays the outcome of a dice roll event
     * @param event an instance of RollDiceResult containing the result data of the dice roll
     */
    void showResult(RollDiceResult event);

    /**
     * Displays information about dead entities or processes
     */
    void showDead();

    /**
     * Updates the list of planets occupied based on a given planetary decision event
     * @param event the PlanetDecision object containing details about the decision or action
     *              that may affect the occupied status of planets
     */
    void updatePlanetsOccupied(PlanetDecision event);

    /**
     * Displays the tracking details of truckers along with their associated level.
     * @param truckers a map containing trucker names as keys and their respective track counts as values
     * @param level an integer representing the detail level of the tracking information to display
     */
    void showTrack(Map<String, Integer> truckers, int level);

    // USED IN MORE SCENE

    /**
     * Displays the specified message
     * @param message the message to be displayed
     */
    void showMessage(String message);

    //END GAME

    /**
     * Displays the end-of-game state to the user
     */
    void showGameEnded();

    /**
     * Displays the scores from the provided map of player names and their corresponding scores
     * @param finalScores a map where keys are player names (String) and values are their scores (Integer)
     */
    void showScores(Map<String, Integer> finalScores);

    //SETUP

    /**
     * Displays a message or performs actions when a player joins using their nickname
     * @param nickname The nickname of the player who joined.
     */
    void showPlayerJoined(String nickname);

    //BUILDING

    /**
     * Displays the available components from the provided list
     * @param components the list of components to be displayed
     */
    void showAvailableComponent(ArrayList<Component> components);

    /**
     * Displays the list of components that are kept for further processing or viewing
     * @param kept An ArrayList containing the components to be displayed.
     */
    void showKeptComponents(ArrayList<Component> kept);

    /**
     * Displays the available build options for the application or project
     */
    void showBuildOptions();

    /**
     * Displays a discard component dialog, allowing the user to confirm
     * or cancel the discard action for the specified component
     * @param component the component to be discarded or affected by the discard action
     */
    void showDiscardComponent(Component component);

    /**
     * Displays a notification or performs an action when the time has expired
     * @param expirationCount the number of times the expiration event has occurred
     */
    void showTimeExpired(int expirationCount);

    /**
     * Displays a waiting indicator or message to inform the user
     * that the system is waiting for other participants or processes
     * to complete their actions
     */
    void showWaitForOthers();

    /**
     * Displays the ship's layout based on the provided nickname and layout grid
     * @param nickname the nickname of the user or entity viewing the ship layout
     * @param layout a 2D array of Component objects representing the ship's layout grid
     */
    void showShipLayout(String nickname, Component[][] layout);

    //FLIGTH

    /**
     * Displays the details of the card that has been drawn
     * @param event the event containing information about the drawn card
     */
    void showCardDrawn(StartCardEvent event);

    /**
     * Displays the Stardust card for the specified event
     * @param event the StarDustEvent containing information about the Stardust card to be displayed
     */
    void showStardustCard(StarDustEvent event);

    /**
     * Updates the current state of the provided goods object
     * @param good the Goods object to be updated with the current state
     */
    void updateCurrGood(Goods good);

    /**
     * Displays the details of an epidemic event card
     * @param event the EpidemicEvent object containing information about the epidemic to be displayed
     */
    void showEpidemicCard(EpidemicEvent event);

    //INPUT

    //PLAYER DATA

    /**
     * Displays the current status of the player's ship
     */
    void showPlayerShipStatus();

    /**
     * Displays the layout of another ship based on provided information
     * @param nickname a string representing the nickname of the ship's owner or identifier
     * @param layout a 2D array of Component objects representing the ship's layout
     */
    void showOtherShipLayout(String nickname, Component[][] layout);

    /**
     * Updates the goods list to mark specific items for removal
     * @param goodsList the list of goods to be marked for removal
     */
    void updateGoodsListToRemove(ArrayList<Goods> goodsList);

    /**
     * Resumes the flight scene for the given players
     * @param playerNames a list of player names who are participating in the flight scene
     */
    void resumeFlightScene(ArrayList<String> playerNames);

    /**
     * Resumes the process of building the scene with the provided player names
     * @param playerNames a list of player names involved in the scene building process
     */
    void resumeBuildingScene(ArrayList<String> playerNames);
}
