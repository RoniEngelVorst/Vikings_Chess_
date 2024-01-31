public class Pawn extends ConcretePiece{

    public Pawn(Player owner,int name){
        this.owner = owner;
        if (owner.isPlayerOne()){this.type = "♙";}
        if (!owner.isPlayerOne()){this.type = "♟";}
        this.name = name;

    }


}
