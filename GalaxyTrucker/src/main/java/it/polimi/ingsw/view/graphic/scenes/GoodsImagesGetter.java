package it.polimi.ingsw.view.graphic.scenes;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * The GoodsImagesGetter class is responsible for providing images
 * for goods based on their color and specific value
 */
public class GoodsImagesGetter {

    /**
     * File path to the image for the red-colored good displayed
     */
    private final String redImagePath;

    /**
     * File path to the image for the yellow-colored good displayed
     */
    private final String yellowImagePath;

    /**
     * File path to the image for the green-colored good displayed
     */
    private final String greenImagePath;

    /**
     * File path to the image for the blue-colored good displayed
     */
    private final String blueImagePath;

    /**
     * Constructs that initialize the file paths for images
     */
    public GoodsImagesGetter() {
        this.redImagePath = "/it/polimi/ingsw/view/redgood.png";
        this.yellowImagePath = "/it/polimi/ingsw/view/yellowgood.png";
        this.greenImagePath = "/it/polimi/ingsw/view/greengood.png";
        this.blueImagePath = "/it/polimi/ingsw/view/bluegood.png";
    }

    /**
     * @param goodsValue the value of the good
     * @return the corresponding Image
     */
    public Image getGoodImage(int goodsValue) {
        String path = switch (goodsValue) {
            case 1 -> blueImagePath;
            case 2 -> greenImagePath;
            case 3 -> yellowImagePath;
            case 4 -> redImagePath;
            default -> throw new IllegalArgumentException("Invalid goods value: " + goodsValue);
        };
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }
}
