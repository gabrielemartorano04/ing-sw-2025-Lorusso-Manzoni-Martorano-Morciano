package it.polimi.ingsw.view.graphic.scenes;

import javafx.scene.image.Image;

import java.util.Objects;

public class GoodsImagesGetter {

    private final String redFrontImagePath;
    private final String yellowFrontImagePath;
    private final String greenFrontImagePath;
    private final String blueFrontImagePath;

    public GoodsImagesGetter() {
        this.redFrontImagePath = "/it/polimi/ingsw/view/redgood.png";
        this.yellowFrontImagePath = "/it/polimi/ingsw/view/yellowgood.png";
        this.greenFrontImagePath = "/it/polimi/ingsw/view/greengood.png";
        this.blueFrontImagePath = "/it/polimi/ingsw/view/bluegood.png";
    }

    /**
     * Returns the image of the good of a specific color and side (front or back)
     * @param goodsValue the value of the good (1 = blue, 2 = green, 3 = yellow, 4 = red)
     * @return the corresponding Image
     */
    public Image getGoodImage(int goodsValue) {
        String path = switch (goodsValue) {
            case 1 -> blueFrontImagePath;
            case 2 -> greenFrontImagePath;
            case 3 -> yellowFrontImagePath;
            case 4 -> redFrontImagePath;
            default -> throw new IllegalArgumentException("Invalid goods value: " + goodsValue);
        };
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }
}
