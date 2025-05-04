package it.polimi.ingsw.view;

import java.io.IOException;

/**
 * All methods used to print something on the screen, implemented by the TUI or the GUI
 */
public abstract class UI {

    protected abstract void show_publisher() throws IOException, InterruptedException;
}
