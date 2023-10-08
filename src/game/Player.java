package game;

public class Player {

    private Hand mainHand;
    private Hand splitHand;

    public Player() {
        this.mainHand = null;
        this.splitHand = null;
    }

    public Player (Hand initialHand) {
        this.mainHand = initialHand;
        this.splitHand = null;
    }
}
