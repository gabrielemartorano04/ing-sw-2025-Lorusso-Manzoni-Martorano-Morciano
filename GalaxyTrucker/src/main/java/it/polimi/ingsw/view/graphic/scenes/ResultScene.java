package it.polimi.ingsw.view.graphic.scenes;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the result scene for displaying final scores and a podium layout.
 * This class is responsible for creating and managing the display of the final scores
 * and relevant visuals in a graphical user interface
 */
public class ResultScene {

    /**
     * Represents the base layout component used to organize and display
     * the graphical content of the scene
     */
    private final StackPane root;

    /**
     * The Scene instance used to define and manage the graphical interface
     * of the application, including the organization of nodes and visual
     * hierarchy
     */
    private Scene scene;

    /**
     * Event constructor
     * @param root the root layout for the scene
     */
    public ResultScene(StackPane root) {
        this.root = root;
    }

    /**
     * Creates and returns a scene displaying the final scores, including a visual representation
     * such as a background image, title, podium image, and top scores with player names
     * @param finalScores a map containing player names as keys and their corresponding scores as values
     * @return the constructed Scene instance showing final scores
     */
    public Scene createScene(Map<String, Integer> finalScores) {
        double width = 1600;
        double height = 900;

        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg")).toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(true);
        backgroundView.setMouseTransparent(true);

        root.getChildren().clear();
        root.getChildren().add(backgroundView);

        Label title = new Label("FINAL SCORES");
        title.setFont(Font.font("Bangers", FontWeight.EXTRA_BOLD, 50));
        title.setTextFill(Color.web("#FFDE69"));
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        StackPane.setMargin(title, new Insets(100, 0, 0, 0));
        root.getChildren().add(title);

        Image podiumImage = new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/podium.png")).toExternalForm());
        ImageView podiumView = new ImageView(podiumImage);
        podiumView.setPreserveRatio(true);
        podiumView.setFitWidth(700);
        StackPane.setAlignment(podiumView, Pos.CENTER);
        StackPane.setMargin(podiumView, new Insets(250, 0, 0, 0));
        root.getChildren().add(podiumView);

        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(finalScores.entrySet());
        sortedScores.sort((a, b) -> b.getValue() - a.getValue());

        double[][] positions = {
                {0, -80},
                {-180, -10},
                {170, 10},
                {360, 150}
        };

        for (int i = 0; i < Math.min(4, sortedScores.size()); i++) {
            Map.Entry<String, Integer> entry = sortedScores.get(i);
            String name = entry.getKey();
            int score = entry.getValue();

            VBox scoreBox = new VBox(5);
            scoreBox.setAlignment(Pos.CENTER);
            scoreBox.setTranslateX(positions[i][0]);
            scoreBox.setTranslateY(positions[i][1]);

            Label scoreLabel = new Label(String.valueOf(score));
            scoreLabel.setFont(Font.font("Bangers", FontWeight.BOLD, 48));
            scoreLabel.setTextFill(Color.web("#FFDE69"));

            Label nameLabel = new Label(name);
            nameLabel.setFont(Font.font("Bangers", FontWeight.NORMAL, 28));
            nameLabel.setTextFill(Color.WHITE);

            scoreBox.getChildren().addAll(scoreLabel, nameLabel);

            scoreBox.setLayoutX(positions[i][0]);
            scoreBox.setLayoutY(positions[i][1]);

            root.getChildren().add(scoreBox);
        }

        scene = new Scene(root, width, height);
        return scene;
    }

    /**
     * Displays an end-of-game message within the application's root layout
     */
    public void showEndMessage() {
        double width = root.getWidth();
        double height = root.getHeight();

        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.setMouseTransparent(true);

        Label errorLabel = new Label("Game ended, thanks for playing!");
        errorLabel.setTextFill(Color.web("#FFDE69"));
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
