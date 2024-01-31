public class ConcretePlayer implements Player{

    private boolean player;
    private int countWins = 0;

    public ConcretePlayer(boolean player){
        this.player = player;
    }
    @Override
    public boolean isPlayerOne(){
        return this.player;
    }

    @Override
    public int getWins() {
        return this.countWins;
    }
}
