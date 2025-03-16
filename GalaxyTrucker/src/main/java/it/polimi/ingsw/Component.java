package it.polimi.ingsw;

public class Component {
    protected int link[];
    protected boolean selected;
    protected boolean covered;
    protected String type;

    public Component(int linkU, int linkR, int linkD, int linkL, String type) {
        this.link = new int[] {linkU, linkR, linkD, linkL};
        this.selected = false;
        this.covered = false;
        this.type = type;
    }

    protected String getType() {
        return this.type;
    }

    public void setLink(int[] link) {
        this.link = link;
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
        for (int i = 0; i < 3; i++) {
            int tmp = link[i];
            link[i] = link[i+1];
            link[i+1] = tmp;
        }
    }
}