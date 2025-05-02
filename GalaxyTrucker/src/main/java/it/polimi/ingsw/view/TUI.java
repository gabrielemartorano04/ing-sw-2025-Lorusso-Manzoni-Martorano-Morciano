package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameState;

import java.io.PrintStream;
import java.nio.charset.Charset;

import static it.polimi.ingsw.model.PlayerColors.YELLOW;

/**
 * Implementation of the TUI
 */
public class TUI extends UI{

    public TUI(GameState gameState) {

    }

    private <__TMP__> __TMP__ ansi() {
        return null;
    }

    private <__TMP__> __TMP__ clearScreen() {
        return null;
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
                        ,----..                                   ___                                            \\s
                       /   /   \\\\                                ,--.'|_    ,--,                                  \\s
                      |   :     :  __  ,-.                      |  | :,' ,--.'|    ,---.        ,---,            \\s
                      .   |  ;. /,' ,'/ /|                      :  : ' : |  |,    '   ,'\\\\   ,-+-. /  | .--.--.   \\s
                      .   ; /--` '  | |' | ,---.     ,--.--.  .;__,'  /  `--'_   /   /   | ,--.'|'   |/  /    '  \\s
                      ;   | ;    |  |   ,'/     \\\\   /       \\\\ |  |   |   ,' ,'| .   ; ,. :|   |  ,"' |  :  /`./  \\s
                      |   : |    '  :  / /    /  | .--.  .-. |:__,'| :   '  | | '   | |: :|   | /  | |  :  ;_    \\s
                      .   | '___ |  | ' .    ' / |  \\\\__\\\\/: . .  '  : |__ |  | : '   | .; :|   | |  | |\\\\  \\\\    `. \\s
                      '   ; : .'|;  : | '   ;   /|  ," .--.; |  |  | '.'|'  : |_|   :    ||   | |  |/  `----.   \\\\\\s
                      '   | '/  :|  , ; '   |  / | /  /  ,.  |  ;  :    ;|  | '.'\\\\   \\\\  / |   | |--'  /  /`--'  /\\s
                      |   :    /  ---'  |   :    |;  :   .'   \\\\ |  ,   / ;  :    ;`----'  |   |/     '--'.     / \\s
                       \\\\   \\\\ .'          \\\\   \\\\  / |  ,     .-./  ---`-'  |  ,   /         '---'        `--'---'  \\s
                        `---`             `----'   `--`---'               ---`-'                                 \\s
                      ""\").reset());
      
          try {
              Thread.sleep(DefaultValue.time_publisher_showing_seconds * 1000);
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
          this.show_titleMyShelfie();
          }
          
         

        
    }
}

