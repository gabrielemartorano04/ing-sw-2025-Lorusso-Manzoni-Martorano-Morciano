package it.polimi.ingsw;

public class Component {
    protected int link[];
    protected boolean selected;
    protected boolean covered;
    protected String type;

    public Component() {
        this.link = new int[4];
        this.selected = false;
        this.covered = false;
        this.type = "";
    }

    protected void setType(String type){
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

    public int getLink() {
        for (int i = 0; i < 4; i++) {
            return link[i];
        }
        return 0;
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