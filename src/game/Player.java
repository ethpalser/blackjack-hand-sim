package game;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Hand> handList;

    public Player() {
        this.handList = new ArrayList<>();
    }

    public void dealHand(Hand hand) {
        handList = new ArrayList<>();
        handList.add(hand);
    }

    public boolean splitHand(int index) {
        // Restricting splits to a maximum of 4
        if (handList.size() >= 4) {
            return false;
        }
        Hand hand = handList.get(index);
        if (hand.size() != 2 || hand.getCard(0).compareTo(hand.getCard(1)) != 0) {
            return false;
        }

        handList.add(new Hand(hand.getCard(0)));
        handList.add(new Hand(hand.getCard(1)));
        handList.remove(index);
        return true;
    }
}
