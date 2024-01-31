import java.util.LinkedList;

public class Position {
    private int x;
    private int y;
    public int numOfPieces = 0;
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString(){
        return ("(" + this.x + ", " + this.y + ")");
    }
}
