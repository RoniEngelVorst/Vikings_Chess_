import java.util.LinkedList;

public abstract class ConcretePiece implements Piece{

    public Player owner;
    public int name;
    public Position position;
    public String type;

    public LinkedList<Position> movesHistory = new LinkedList<>();
    public int numOfKills = 0;


    @Override
    public Player getOwner() {
        return this.owner;
    }



    public int getName() {
        return name;
    }
    public Position getPosition(){
        return this.position;
    }
    public String getType() {return this.type;}
    public void setName(int name) {
        this.name = name;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
    public void setType(String type) {this.type = type;}
    public String toString() {
        if (this.owner.isPlayerOne()) {
            if (this.name == 7) {
                return "K" + this.name + ": ";
            } else {
                return "D" + this.name + ": ";
            }
        }
        return "A" + this.name + ": ";
    }

    public String printMovesHistory(){
        String s = "[";
        for(Position p: movesHistory){
            s = s + p.toString() +", ";
        }
        s = s.substring(0, s.length()-2) + "]";
        return s;
    }

    public int calcSquares(){
        int num = 0;
        for(int i =0; i< movesHistory.size()-1; i++){
            Position current = movesHistory.get(i);
            Position next = movesHistory.get(i+1);
            num = num + Math.abs(current.getX()-next.getX()) + Math.abs((current.getY())- next.getY());
        }
        return num;
    }

}
