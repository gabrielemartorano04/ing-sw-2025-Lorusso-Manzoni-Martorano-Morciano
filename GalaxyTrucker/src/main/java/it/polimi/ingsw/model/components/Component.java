package it.polimi.ingsw.model.components;

import javafx.scene.image.Image;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class that manages the generic component
 */
public abstract sealed class Component implements Serializable  permits BatteryStorage, Cabin, Cannon, EmptySpace, Engine, LifeSupport,Load,Pipes,Shield {
    /**
     * Component's links:
     * link[0]: upper link
     * link[1]: right link
     * link[2]: lower link
     * link[3]: left link
     */
    private final Integer[] link;

    /**
     * It tells if the component is selected
     */
    private boolean selected;

    /**
     * It tells if the component is covered
     */
    private boolean covered;

    /**
     * It tells if the component is linked to the main cabin, through different links
     */
    private boolean visited;

    /**
     * Component's type
     */
    private final String type;

    /**
     * Component's front image path
     */
    private final String frontImagePath;

    /**
     * Component's back image path
     */
    private final String backImagePath;

    /**
     * Component's rotation angle
     */
    private int rotationAngle;

    /**
     * Serial version
     */
    @Serial
    private static final long serialVersionUID = 1L;



    /**
     * Component constructor, initialise the attributes of this class
     * @param linkU number of link on the top
     * @param linkR number of link on the right side
     * @param linkD number of link on the bottom
     * @param linkL number of link on the left side
     * @param type type of component
     * @param frontImagePath front image of the component
     */
    public Component(int linkU, int linkR, int linkD, int linkL, String type, String frontImagePath) {
        this.link = new Integer[] {linkU, linkR, linkD, linkL};
        this.selected = false;
        this.covered = true;
        this.visited = false;
        this.type = type;
        this.frontImagePath = frontImagePath;
        this.backImagePath = "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web157.jpg";
        this.rotationAngle = 0;
    }

    /**
     * Returns the rear image of the component
     * @return rear image
     */
    public Image getBackImage() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(backImagePath)));
    }


    /**
     * Returns the front image of the component
     * @return front image
     */
    public Image getFrontImage() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(frontImagePath)));
    }

    /**
     * Sets if the component is already connected to the spaceship after the removal of a component
     * @param visited if the component is already connected
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Returns the front image of the component
     * @return front image
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Sets if the component has been selected from the table to be added to a spaceship
     * @param selected if the component is selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Sets if the component has been already uncovered on the table or not
     * @param covered if the component is covered
     */
    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    /**
     * Returns an array of the number of link on each of the four side
     * position 0 = top
     * position 1 = right side
     * position 2 = bottom
     * position 3 = left side
     * @return array of number of link
     */
    public Integer[] getLink() {
        return link; //standard: il primo elemento si riferisce ai link superiori, il secondo ai link di dx e così via
    }

    /**
     * Returns if the component has been selected from the table to be added to a spaceship
     * @return if the component is selected
     */
    public boolean getSelected() {
        return this.selected;
    }

    /**
     * Returns if the component has been already uncovered on the table or not
     * @return if the component is covered
     */
    public boolean getCovered() {
        return this.covered;
    }

    /**
     * Turns clockwise the component, thus the link
     */
    public void turn() {
        int tmp=link[3];
        link[3]=link[2];
        link[2]=link[1];
        link[1]=link[0];
        link[0]=tmp;
        rotationAngle = (rotationAngle + 90) % 360;
    }

    /**
     * Component's rotation angle getter
     * @return component's rotation angle
     */
    public int getRotationAngle() {
        return rotationAngle;
    }

    /**
     * Returns the type of component
     * @return type of component
     */
    public String getType() {
        return this.type;
    }

    /**
     * This method returns the component description
     * @return component text description
     */
    @Override
    public String toString(){
        String s;
        s="tipo: "+ type +" link alto: "+link[0].toString()+" link dx: "+ link[1].toString()+" link lx: "+ link[2].toString()+" link basso: "+ link[3].toString();
        return s;
    }
}
