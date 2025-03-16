package it.polimi.ingsw;

public class Pipes extends Component{

    public Pipes(int linkU, int linkR, int linkD, int linkL, String type){
        super(linkU, linkR, linkD, linkL, type);
    }

    public String getType() {
        return type;
    }
}
