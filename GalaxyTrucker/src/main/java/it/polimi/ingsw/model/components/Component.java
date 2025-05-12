package it.polimi.ingsw.model.components;

import javafx.scene.image.Image;
import java.io.Serializable;

/**
 * Class that manages the single component
 */
public abstract sealed class Component implements Serializable  permits BatteryStorage, Cabin, Cannon, EmptySpace, Engine, LifeSupport,Load,Pipes,Shield {
    protected Integer[] link;
    protected boolean selected;
    protected boolean covered;
    private boolean visited;
    protected String type;
    private final String frontImagePath;
    private final String backImagePath;
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
    }

    /**
     * Returns the rear image of the component
     * @return rear image
     */
    public Image getBackImage() {
        return new Image(getClass().getResourceAsStream(backImagePath));
    }


    /**
     * Returns the front image of the component
     * @return front image
     */
    public Image getFrontImage() {
        return new Image(getClass().getResourceAsStream(frontImagePath));
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
    }

    /**
     * Returns the type of component
     * @return type of component
     */
    public String getType() {
        return this.type;
    }

    public static void main(String[] args) {
        Component com=new Engine(2, 0, 0, "Engine", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web77.jpg", false);
        System.out.println(com.getType());
    }

    @Override
    public String toString(){
        String s;
        s="tipo: "+type.toString()+" link alto: "+link[0].toString()+" link dx: "+ link[1].toString()+" link lx: "+ link[2].toString()+" link basso: "+ link[3].toString();
        return s;
    }
}
