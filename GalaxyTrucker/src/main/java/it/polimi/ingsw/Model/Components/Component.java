package it.polimi.ingsw.Model.Components;

import javax.swing.ImageIcon;

public abstract class Component {
    protected int link[];
    protected boolean selected;
    protected boolean covered;
    protected String type;
    protected final ImageIcon frontImage, rearImage;

    public Component(int linkU, int linkR, int linkD, int linkL, String type, String frontImagePath) {
        this.link = new int[] {linkU, linkR, linkD, linkL};
        this.selected = false;
        this.covered = false;
        this.type = type;
        this.frontImage = new ImageIcon(frontImagePath);
        this.rearImage = new ImageIcon("src/main/resources/components/tiles/GT-new_tiles_16_for web157.jpg");
    }

    protected String getType() {
        return this.type;
    }

    public ImageIcon getFrontImage() {
        return frontImage;
    }

    public ImageIcon getRearImage() {
        return rearImage;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public int[] getLink() {
        return link; //standard: il primo elemento si riferisce ai link superiori, il secondo ai link di dx e così via
    }

    public boolean getSelected() {
        return this.selected;
    }

    public boolean getCovered() {
        return this.covered;
    }

    public void turn() {
        int tmp=link[3];
        link[3]=link[2];
        link[2]=link[1];
        link[1]=link[0];
        link[0]=tmp;
    }

    public static void main(String[] args) {
        Component com=new Engine(2, 0, 0, "Engine", "src/main/resources/components/tiles/GT-new_tiles_16_for web77.jpg", false);
        System.out.println(com.getType());
    }
}
