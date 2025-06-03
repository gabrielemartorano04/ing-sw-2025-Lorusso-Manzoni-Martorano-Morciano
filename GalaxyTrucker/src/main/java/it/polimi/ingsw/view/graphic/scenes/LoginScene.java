package it.polimi.ingsw.view.graphic.scenes;

import it.polimi.ingsw.model.PlayerColors;

import it.polimi.ingsw.net.events.serverevents.buildingevents.*;

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

import java.awt.*;
import java.util.*;

public class LoginScene {
    private final EnumSet<PlayerColors> availableColors;
    private final Map<PlayerColors, StackPane> colorBoxes = new HashMap<>();
    private final PlayerColors[] selectedColor = {null};
    private TextField nicknameField;
    private Button doneButton;
    private final ViewFlow viewFlow;
    private StackPane root;


    public LoginScene(EnumSet<PlayerColors> availableColors, ViewFlow viewFlow) {
        this.availableColors = availableColors;
        this.viewFlow = viewFlow;
    }

    public Scene createScene() {
        double width = 850;
        double height = 550;

        ImageView backgroundView = new ImageView(new Image(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg").toExternalForm()));
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        backgroundView.setMouseTransparent(true);

        Label nicknameLabel = new Label("Insert your nickname and choose a color");
        nicknameLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 24));
        nicknameLabel.setTextFill(Color.web("#FFDE69"));
        nicknameLabel.setAlignment(Pos.CENTER);

        nicknameField = new TextField();
        nicknameField.setPromptText("Enter your nickname");
        nicknameField.setMaxWidth(200);
        nicknameField.setAlignment(Pos.CENTER);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", 12));

        TilePane colorPane = new TilePane(15, 15);
        colorPane.setAlignment(Pos.CENTER);

        doneButton = new Button("DONE");
        doneButton.setFont(Font.font("Bangers", 16));
        doneButton.setDisable(true);
        doneButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

        for (PlayerColors color : availableColors) {
            Rectangle rect = new Rectangle(60, 60, getColorForEnum(color));
            rect.setArcWidth(18);
            rect.setArcHeight(18);

            Label checkmark = new Label("✓");
            checkmark.setTextFill(Color.WHITE);
            checkmark.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            checkmark.setVisible(false);

            StackPane stack = new StackPane(rect, checkmark);
            stack.setPrefSize(60, 60);
            stack.setCursor(Cursor.HAND);

            stack.setOnMouseClicked(e -> {
                colorBoxes.values().forEach(box -> ((Label) box.getChildren().get(1)).setVisible(false));
                checkmark.setVisible(true);
                selectedColor[0] = color;

                boolean nicknameOk = !nicknameField.getText().trim().isEmpty();
                doneButton.setDisable(!nicknameOk);
                doneButton.setStyle(nicknameOk ? "-fx-background-color: #FFDE69; -fx-text-fill: black;" : "-fx-background-color: gray; -fx-text-fill: white;");
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

        doneButton.setOnAction(e -> {
            String nickname = nicknameField.getText().trim();
            String input = nickname + " " + selectedColor[0];
            viewFlow.handleInput(input);
        });

        VBox content = new VBox(30, nicknameLabel, nicknameField, colorPane, doneButton, errorLabel);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        this.root = new StackPane(backgroundView, content);
        return new Scene(root, width, height);
    }

    private Color getColorForEnum(PlayerColors color) {
        return switch (color) {
            case RED -> Color.RED;
            case GREEN -> Color.LIMEGREEN;
            case BLUE -> Color.DODGERBLUE;
            case YELLOW -> Color.GOLD;
            default -> Color.GRAY;
        };
    }

    public void markColorAsUnavailable(PlayerColors color) {
        StackPane box = colorBoxes.get(color);
        if (box != null) {
            Rectangle rect = (Rectangle) box.getChildren().get(0);
            rect.setFill(Color.GRAY);
            box.setDisable(true);
            Tooltip.install(box, new Tooltip("Color already taken"));
        }
    }

    public void showNicknameTakenPopup() {
        double width = root.getWidth();
        double height = root.getHeight();

        // Overlay grigio trasparente
        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        // Messaggio di errore
        Label errorLabel = new Label("ERROR! Nickname already taken");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 28));
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
