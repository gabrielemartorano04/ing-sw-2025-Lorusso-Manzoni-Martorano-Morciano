package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.PlayerColors;

import it.polimi.ingsw.view.ViewFlow;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

/**
 * The LoginScene class is responsible for managing the login and setup screen for a multiplayer game
 */
public class LoginScene {

    /**
     * A map associating each player's color with a corresponding StackPane box
     */
    private final Map<PlayerColors, StackPane> colorBoxes;

    /**
     * Array of already selected colors
     */
    private final PlayerColors[] selectedColor;

    /**
     * A TextField used for entering or displaying the player's nickname
     */
    private TextField nicknameField;

    /**
     * Bbutton used to complete or finalize an action within the login process
     */
    private Button doneButton;

    /**
     * Represents an instance of ViewFlow used to manage and control the navigation
     * and transitions between different views or scenes in the application
     */
    private final ViewFlow viewFlow;

    /**
     * The root of the layout for the scene
     */
    private StackPane root;

    /**
     * Set of player's colors representing colors that are not available
     * for selection in the login process
     */
    private final Set<PlayerColors> unavailableColors;

    /**
     * Represents the main container for organizing and arranging the graphical components
     * within the login scene
     */
    private VBox content;

    /**
     * A graphical overlay component displayed to the user to indicate a waiting state
     */
    private StackPane waitOverlay;

    /**
     * A private Timeline object responsible for managing the animation
     * that indicates a waiting state in the login scene
     */
    private Timeline waitAnimation;

    /**
     * Event constructor
     * @param viewFlow represents the flow control mechanism allowing navigation between different scenes
     */
    public LoginScene(ViewFlow viewFlow) {
        this.viewFlow = viewFlow;
        this.unavailableColors = EnumSet.noneOf(PlayerColors.class);
        colorBoxes = new HashMap<>();
        selectedColor = new PlayerColors[]{null};
    }

    /**
     * Creates a new scene for the login process based on the provided configuration
     * @param resume a boolean value indicating whether to resume a game (true) or start a new game (false)
     * @param nicknames a list of player nicknames to be used when resuming a game
     * @param colors a list of player colors to be used when resuming a game
     * @return a Scene object representing the generated scene for the login or game setup
     */
    public Scene createScene(boolean resume, List<String> nicknames, List<PlayerColors> colors) {
        if (resume) {
            return createResumeGameScene(nicknames, colors);
        } else {
            return createNewGameScene();
        }
    }

    /**
     * Creates a new game scene where the user can input a nickname and select a player color
     * @return a Scene object representing the new game setup screen
     */
    private Scene createNewGameScene(){
        double width = 1600;
        double height = 900;

        ImageView backgroundView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg")).toExternalForm()));
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(true);
        backgroundView.setMouseTransparent(true);

        Label nicknameLabel = new Label("Insert your nickname and choose a color");
        nicknameLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 30));
        nicknameLabel.setTextFill(Color.web("#FFDE69"));
        nicknameLabel.setAlignment(Pos.CENTER);

        nicknameField = new TextField();
        nicknameField.setPromptText("Enter your nickname");
        nicknameField.setMaxWidth(300);
        nicknameField.setAlignment(Pos.CENTER);
        nicknameField.setStyle("-fx-padding: 10 16 10 16; -fx-font-size: 16px;");

        TilePane colorPane = new TilePane(25, 25);
        colorPane.setAlignment(Pos.CENTER);

        doneButton = new Button("DONE");
        doneButton.setFont(Font.font("Bangers", 20));
        doneButton.setDisable(true);
        doneButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
        doneButton.setCursor(Cursor.HAND);

        for (PlayerColors color : EnumSet.allOf(PlayerColors.class)) {
            Rectangle rect = new Rectangle(80, 80, getColorForEnum(color));
            rect.setArcWidth(20);
            rect.setArcHeight(20);

            Label checkmark = new Label("✓");
            checkmark.setTextFill(Color.WHITE);
            checkmark.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            checkmark.setVisible(false);

            StackPane stack = new StackPane(rect, checkmark);
            stack.setPrefSize(80, 80);
            stack.setCursor(Cursor.HAND);

            stack.setOnMouseClicked(e -> {
                if (unavailableColors.contains(color)) {
                    showColorTakenPopup();
                    return;
                }

                colorBoxes.values().forEach(box -> {
                    for (javafx.scene.Node node : box.getChildren()) {
                        if (node.getClass() == Label.class) {
                            Label lbl = (Label) node;
                            if ("✓".equals(lbl.getText())) {
                                lbl.setVisible(false);
                            }
                        }
                    }
                });

                checkmark.setVisible(true);
                selectedColor[0] = color;

                boolean nicknameOk = !nicknameField.getText().trim().isEmpty();
                doneButton.setDisable(!nicknameOk);
                doneButton.setStyle(nicknameOk
                        ? "-fx-background-color: #FFDE69; -fx-text-fill: black;"
                        : "-fx-background-color: gray; -fx-text-fill: white;");
            });

            colorBoxes.put(color, stack);
            colorPane.getChildren().add(stack);
        }

        nicknameField.textProperty().addListener((obs, oldText, newText) -> {
            boolean nicknameOk = !newText.trim().isEmpty();
            boolean colorOk = selectedColor[0] != null;
            doneButton.setDisable(!(nicknameOk && colorOk));
            doneButton.setStyle((nicknameOk && colorOk) ? "-fx-background-color: #FFDE69; -fx-text-fill: black;" : "-fx-background-color: gray; -fx-text-fill: white;");
        });

        this.content = new VBox(30, nicknameLabel, nicknameField, colorPane, doneButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        doneButton.setOnAction(e -> {
            String nickname = nicknameField.getText().trim();
            String input = nickname + " " + selectedColor[0];
            viewFlow.handleInput(input);
        });

        this.root = new StackPane(backgroundView, content);
        return new Scene(root, width, height);
    }

    /**
     * Creates a new scene for resuming a game, where users can select their profiles based on provided nicknames and colors
     * @param nicknames a list of player nicknames available for selection when resuming a game
     * @param colors a list of corresponding player colors associated with the nicknames
     * @return a Scene object representing the generated UI for profile selection to resume the game
     */
    private Scene createResumeGameScene(List<String> nicknames, List<PlayerColors> colors) {
        double width = 1600;
        double height = 900;

        ImageView backgroundView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg")).toExternalForm()));
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(true);
        backgroundView.setMouseTransparent(true);

        Label titleLabel = new Label("Welcome back! Select your profile:");
        titleLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 30));
        titleLabel.setTextFill(Color.web("#FFDE69"));

        TilePane playerPane = new TilePane(30, 30);
        playerPane.setAlignment(Pos.CENTER);

        final StackPane[] selectedBox = {null};
        final String[] selectedNickname = {null};
        final PlayerColors[] selectedColor = {null};

        Map<StackPane, Rectangle> boxToRect = new HashMap<>();
        Map<StackPane, PlayerColors> boxToColor = new HashMap<>();
        Map<StackPane, Rectangle> boxToOverlay = new HashMap<>();

        for (int i = 0; i < colors.size(); i++) {
            PlayerColors color = colors.get(i);
            String nickname = nicknames.get(i);

            Rectangle baseRect = new Rectangle(180, 70);
            baseRect.setArcWidth(20);
            baseRect.setArcHeight(20);
            baseRect.setFill(switch (color) {
                case RED -> Color.RED;
                case GREEN -> Color.LIMEGREEN;
                case BLUE -> Color.DODGERBLUE;
                case YELLOW -> Color.GOLD;
            });

            Rectangle overlay = new Rectangle(180, 70);
            overlay.setArcWidth(20);
            overlay.setArcHeight(20);
            overlay.setFill(Color.BLACK);
            overlay.setOpacity(0.5);
            overlay.setVisible(false);

            Label nameLabel = new Label(nickname);
            nameLabel.setTextFill(Color.BLACK);
            nameLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 20));

            StackPane box = new StackPane(baseRect, overlay, nameLabel);
            box.setCursor(Cursor.HAND);

            boxToRect.put(box, baseRect);
            boxToColor.put(box, color);
            boxToOverlay.put(box, overlay);

            box.setOnMouseClicked(e -> {
                for (StackPane otherBox : boxToRect.keySet()) {
                    otherBox.setStyle("");
                    boxToOverlay.get(otherBox).setVisible(true);
                }

                box.setStyle("-fx-border-color: transparent; -fx-border-width: 3px;");
                boxToOverlay.get(box).setVisible(false);

                selectedBox[0] = box;
                selectedNickname[0] = nickname;
                selectedColor[0] = color;
            });

            playerPane.getChildren().add(box);
        }

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setFont(Font.font("Bangers", 20));
        confirmBtn.setPrefWidth(120);
        confirmBtn.setTextFill(Color.BLACK);
        confirmBtn.setBackground(new Background(new BackgroundFill(Color.web("#FFDE69"), new CornerRadii(10), Insets.EMPTY)));
        confirmBtn.setCursor(Cursor.HAND);

        confirmBtn.setOnAction(e -> {
            if (selectedNickname[0] != null && selectedColor[0] != null) {
                String input = selectedNickname[0] + " " + selectedColor[0];
                viewFlow.handleInput(input);
                root.getChildren().remove(content);
            }
        });

        VBox content = new VBox(40, titleLabel, playerPane, confirmBtn);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        root = new StackPane(backgroundView, content);
        StackPane.setAlignment(content, Pos.CENTER);

        return new Scene(root, width, height);
    }

    /**
     * @param color the player's color representing the selected player color
     * @return the color corresponding to the given player's color
     */
    private Color getColorForEnum(PlayerColors color) {
        return switch (color) {
            case RED -> Color.RED;
            case GREEN -> Color.LIMEGREEN;
            case BLUE -> Color.DODGERBLUE;
            case YELLOW -> Color.GOLD;
        };
    }

    /**
     * Marks a specific player color as unavailable, visually indicating its unavailability
     * on the UI and disabling its selection.
     * @param color the player color to be marked as unavailable
     */
    public void markColorAsUnavailable(PlayerColors color) {
        unavailableColors.add(color);

        StackPane box = colorBoxes.get(color);
        if (box != null && !box.getChildren().isEmpty()) {
            Rectangle grayOverlay = new Rectangle(80, 80);
            grayOverlay.setArcWidth(20);
            grayOverlay.setArcHeight(20);
            grayOverlay.setFill(Color.rgb(50, 50, 50, 0.5));
            grayOverlay.setMouseTransparent(true);

            Label cross = new Label("✖");
            cross.setTextFill(Color.WHITE);
            cross.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 35));
            cross.setMouseTransparent(true);

            if (box.getChildren().size() == 2) {
                box.getChildren().add(1, grayOverlay);
                box.getChildren().add(2, cross);
            } else {
                box.getChildren().addAll(grayOverlay, cross);
            }

            Tooltip.install(box, new Tooltip("Color already taken"));
        }
    }

    /**
     * Displays a popup message indicating that the chosen nickname is already taken
     */
    public void showNicknameTakenPopup() {
        double width = root.getWidth();
        double height = root.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label errorLabel = new Label("ERROR! Nickname already taken");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 35));
        errorLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        StackPane popup = new StackPane(overlay, errorLabel);
        popup.setMouseTransparent(true);
        StackPane.setAlignment(errorLabel, Pos.CENTER);

        Platform.runLater(() -> {
            if (!root.getChildren().contains(popup)) {
                root.getChildren().add(popup);
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> root.getChildren().remove(popup));
                delay.play();
            }
        });
    }

    /**
     * Displays a popup message indicating that the selected color is already taken
     */
    public void showColorTakenPopup() {
        double width = root.getWidth();
        double height = root.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label errorLabel = new Label("ERROR! Color already taken");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 35));
        errorLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        StackPane popup = new StackPane(overlay, errorLabel);
        popup.setMouseTransparent(true);
        StackPane.setAlignment(errorLabel, Pos.CENTER);

        Platform.runLater(() -> {
            if (!root.getChildren().contains(popup)) {
                root.getChildren().add(popup);
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> root.getChildren().remove(popup));
                delay.play();
            }
        });
    }

    /**
     * Displays a wait overlay with an animated message indicating that the user is waiting
     * for other players to join
     */
    public void showWaitForOthers() {
        Label message = new Label("Waiting for other truckers to join");
        message.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 32));
        message.setTextFill(Color.web("#FFDE69"));

        Label dots = new Label(".");
        dots.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 32));
        dots.setTextFill(Color.web("#FFDE69"));

        HBox textBox = new HBox(5, message, dots);
        textBox.setAlignment(Pos.CENTER);

        waitOverlay = new StackPane(textBox);
        waitOverlay.setMouseTransparent(true);
        waitOverlay.setAlignment(Pos.CENTER);

        waitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> dots.setText("")),
                new KeyFrame(Duration.seconds(0.5), e -> dots.setText(".")),
                new KeyFrame(Duration.seconds(1.0), e -> dots.setText("..")),
                new KeyFrame(Duration.seconds(1.5), e -> dots.setText("...")),
                new KeyFrame(Duration.seconds(2.0), e -> dots.setText(""))
        );
        waitAnimation.setCycleCount(Animation.INDEFINITE);
        waitAnimation.play();

        Platform.runLater(() -> {
            if (content != null) {
                root.getChildren().remove(content);
            }

            if (!root.getChildren().contains(waitOverlay)) {
                root.getChildren().add(waitOverlay);
            }
        });
    }

    /**
     * Removes the wait overlay and stops any associated wait animation in the application
     */
    public void hideWaitForOthers() {
        Platform.runLater(() -> {
            if (waitOverlay != null) {
                root.getChildren().remove(waitOverlay);
                waitOverlay = null;
            }
            if (waitAnimation != null) {
                waitAnimation.stop();
                waitAnimation = null;
            }
        });
    }

    /**
     * Displays a popup notification indicating that a player has joined
     * @param nickname the nickname of the player who joined
     */
    public void showPlayerJoined(String nickname) {
        Label message = new Label(nickname + " has joined");
        message.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.8); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-background-radius: 10px;"
        );
        message.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        StackPane overlay = new StackPane(message);
        overlay.setMouseTransparent(true);
        StackPane.setAlignment(message, Pos.TOP_RIGHT);
        StackPane.setMargin(message, new Insets(15, 15, 0, 0));

        Platform.runLater(() -> {
            if (!root.getChildren().contains(overlay)) {
                root.getChildren().add(overlay);
                PauseTransition delay = new PauseTransition(Duration.seconds(5));
                delay.setOnFinished(e -> root.getChildren().remove(overlay));
                delay.play();
            }
        });
    }

    /**
     * Displays an error message as a popup overlay on the current scene
     * @param message the error message to be displayed in the popup
     */
    public void showError(String message) {
        double width = root.getWidth();
        double height = root.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label errorLabel = new Label(message);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 35));
        errorLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 4, 0.5, 0, 0);");

        StackPane popup = new StackPane(overlay, errorLabel);
        popup.setMouseTransparent(true);
        StackPane.setAlignment(errorLabel, Pos.CENTER);

        Platform.runLater(() -> {
            if (!root.getChildren().contains(popup)) {
                root.getChildren().add(popup);
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> root.getChildren().remove(popup));
                delay.play();
            }
        });
    }
}
