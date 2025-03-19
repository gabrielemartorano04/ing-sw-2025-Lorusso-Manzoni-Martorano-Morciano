package it.polimi.ingsw.Model.Components;

public class Pipes extends Component{

    public Pipes(int linkU, int linkR, int linkD, int linkL, String type, String imagePath){
        super(linkU, linkR, linkD, linkL, type, imagePath);
    }

    public String getType() {
        return type;
    }
}
