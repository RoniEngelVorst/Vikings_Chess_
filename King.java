public class King extends ConcretePiece {
    public King(Player player, int name){
        this.owner = player;
        this.type = "♔";
        this.name = name;
    }

}