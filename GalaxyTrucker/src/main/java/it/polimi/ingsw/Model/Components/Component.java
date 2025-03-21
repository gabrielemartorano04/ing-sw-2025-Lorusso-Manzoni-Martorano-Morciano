package it.polimi.ingsw.Model.Components;

import javax.swing.ImageIcon;

/**
 * Class that manages the single component
 */
public abstract class Component {
    protected int[] link;
    protected boolean selected;
    protected boolean covered;
    protected String type;
    protected final ImageIcon frontImage, rearImage;

    /**
     * Component constructor, it initialised the attributes of this class
     * @param linkU number of link on the top
     * @param linkR number of link on the right side
     * @param linkD number of link on the bottom
     * @param linkL number of link on the left side
     * @param type type of component
     * @param frontImagePath front image of the component
     */
    public Component(int linkU, int linkR, int linkD, int linkL, String type, String frontImagePath) {
        this.link = new int[] {linkU, linkR, linkD, linkL};
        this.selected = false;
        this.covered = false;
        this.type = type;
        this.frontImage = new ImageIcon(frontImagePath);
        this.rearImage = new ImageIcon("src/main/resources/components/tiles/GT-new_tiles_16_for web157.jpg");
    }

    /**
     * Return the front image of the component
     * @return front image
     */
    public ImageIcon getFrontImage() {
        return frontImage;
    }

    /**
     * Return the rear image of the component
     * @return rear image
     */
    public ImageIcon getRearImage() {
        return rearImage;
    }

    /**
     * Set if the component has been selected from the table to be added to a spaceship
     * @param selected if the component is selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Set if the component has been already uncovered on the table or not
     * @param covered if the component is covered
     */
    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    /**
     * Return an array of the number of link on each of the four side
     * position 0 = top
     * position 1 = right side
     * position 2 = bottom
     * position 3 = left side
     * @return array of number of link
     */
    public int[] getLink() {
        return link; //standard: il primo elemento si riferisce ai link superiori, il secondo ai link di dx e così via
    }

    /**
     * Return if the component has been selected from the table to be added to a spaceship
     * @return if the component is selected
     */
    public boolean getSelected() {
        return this.selected;
    }

    /**
     * Return if the component has been already uncovered on the table or not
     * @return if the component is covered
     */
    public boolean getCovered() {
        return this.covered;
    }

    /**
     * Turn clockwise the component, thus the link
     */
    public void turn() {
        int tmp=link[3];
        link[3]=link[2];
        link[2]=link[1];
        link[1]=link[0];
        link[0]=tmp;
    }

    /**
     * Return the type of component
     * @return type of component
     */
    public String getType() {
        return this.type;
    }

    public static void main(String[] args) {
        Component com=new Engine(2, 0, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web77.jpg", false);
        System.out.println(com.getType());
    }
}
