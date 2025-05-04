package it.polimi.ingsw.view;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;

import static it.polimi.ingsw.model.PlayerColors.YELLOW;

/**
 * Implementation of the TUI
 */
public abstract class TUI extends UI{

    public TUI() {

    }

    @Override
    public void show_publisher() {
        this.resize();


        clearScreen();
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset()
        ).println(ansi().cursor(1, 1).fg(YELLOW).a("""
                                                                                                           \s
                                                                                                           \s
                  ,----..                                                                                  \s
                 /   /   \\                                   ,--,                                          \s
                |   :     :  __  ,-.                 ,---, ,--.'|    ,---.                                 \s
                .   |  ;. /,' ,'/ /|             ,-+-. /  ||  |,    '   ,'\\                                \s
                .   ; /--` '  | |' | ,--.--.    ,--.'|'   |`--'_   /   /   |                               \s
                ;   | ;    |  |   ,'/       \\  |   |  ,"' |,' ,'| .   ; ,. :                               \s
                |   : |    '  :  / .--.  .-. | |   | /  | |'  | | '   | |: :                               \s
                .   | '___ |  | '   \\__\\/: . . |   | |  | ||  | : '   | .; :                               \s
                '   ; : .'|;  : |   ," .--.; | |   | |  |/ '  : |_|   :    |                               \s
                '   | '/  :|  , ;  /  /  ,.  | |   | |--'  |  | '.'\\   \\  /                                \s
                |   :    /  ---'  ;  :   .'   \\|   |/      ;  :    ;`----'                                 \s
                 \\   \\ .'         |  ,     .-./'---'       |  ,   /                                        \s
                  `---`            `--`---'                 ---`-'                                         \s
                  ,----..                                   ___                                            \s
                 /   /   \\                                ,--.'|_    ,--,                                  \s
                |   :     :  __  ,-.                      |  | :,' ,--.'|    ,---.        ,---,            \s
                .   |  ;. /,' ,'/ /|                      :  : ' : |  |,    '   ,'\\   ,-+-. /  | .--.--.   \s
                .   ; /--` '  | |' | ,---.     ,--.--.  .;__,'  /  `--'_   /   /   | ,--.'|'   |/  /    '  \s
                ;   | ;    |  |   ,'/     \\   /       \\ |  |   |   ,' ,'| .   ; ,. :|   |  ,"' |  :  /`./  \s
                |   : |    '  :  / /    /  | .--.  .-. |:__,'| :   '  | | '   | |: :|   | /  | |  :  ;_    \s
                .   | '___ |  | ' .    ' / |  \\__\\/: . .  '  : |__ |  | : '   | .; :|   | |  | |\\  \\    `. \s
                '   ; : .'|;  : | '   ;   /|  ," .--.; |  |  | '.'|'  : |_|   :    ||   | |  |/  `----.   \\\s
                '   | '/  :|  , ; '   |  / | /  /  ,.  |  ;  :    ;|  | '.'\\   \\  / |   | |--'  /  /`--'  /\s
                |   :    /  ---'  |   :    |;  :   .'   \\ |  ,   / ;  :    ;`----'  |   |/     '--'.     / \s
                 \\   \\ .'          \\   \\  / |  ,     .-./  ---`-'  |  ,   /         '---'        `--'---'  \s
                  `---`             `----'   `--`---'               ---`-'                                 \s
                """).reset());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.show_titleGalaxyTrucker();
    }

    protected abstract void clearScreen();


    public void show_titleGalaxyTrucker() {
        new PrintStream(System.out, true, System.console() != null
                ? System.console().charset()
                : Charset.defaultCharset()
        ).println(ansi().fg(YELLOW).a("""

       ██████╗░░█████╗░██╗░░░░░░█████╗░██╗░░░██╗██╗░░░██╗  ████████╗██████╗░██╗░░░██╗██████╗░██╗░░░██╗███████╗██████╗░
       ██╔══██╗██╔══██╗██║░░░░░██╔══██╗██║░░░██║╚██╗░██╔╝  ╚══██╔══╝██╔══██╗██║░░░██║██╔══██╗██║░░░██║██╔════╝██╔══██╗
       ██████╦╝██║░░██║██║░░░░░██║░░██║██║░░░██║░╚████╔╝░  ░░░██║░░░██████╔╝██║░░░██║██████╦╝██║░░░██║█████╗░░██████╔╝
       ██╔══██╗██║░░██║██║░░░░░██║░░██║██║░░░██║░░╚██╔╝░░  ░░░██║░░░██╔═══╝░██║░░░██║██╔══██╗██║░░░██║██╔══╝░░██╔═══╝░
       ██████╦╝╚█████╔╝███████╗╚█████╔╝╚██████╔╝░░░██║░░░  ░░░██║░░░██║░░░░░╚██████╔╝██████╦╝╚██████╔╝███████╗██║░░░░░
       ╚═════╝░░╚════╝░╚══════╝░╚════╝░░╚═════╝░░░░╚═╝░░░  ░░░╚═╝░░░╚═╝░░░░░░╚═════╝░╚═════╝░░╚═════╝░╚══════╝╚═╝░░░░░
            """).reset());
    }

    protected abstract Object ansi();

    public void resize() {
        try {
            new ProcessBuilder("cmd", "/c", "mode con:cols=160 lines=50").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            //couldn't resize the terminal window
        }
    }

}

