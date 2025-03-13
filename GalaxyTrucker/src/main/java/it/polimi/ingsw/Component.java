package it.polimi.ingsw;

public class Component {
    protected int link[];
    protected boolean selected;
    protected boolean covered;
    protected String type;

    public Component() {
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

    public void setLink() {
        ;
    }

    public void setSelected() {
        ;
    }

    public void setCovered() {
        ;
    }

    public void getLink() {
        ;
    }

    public void getSelected() {
        ;
    }

    public void getCovered() {
        ;
    }

    public void turn() {
        ;
    }
}