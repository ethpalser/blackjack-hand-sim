package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Hand> handList;

    public Player() {
        this.handList = new ArrayList<>();
    }

    public int getHandQty() {
        return handList.size();
    }

    public Hand getHand(int index) {
        if (index < 0 || handList == null || index >= handList.size())
            return null;
        return handList.get(index);
    }

    public void dealHand(Hand hand) {
        handList = new ArrayList<>();
        handList.add(hand);
    }

    public boolean canSplitHands() {
        return handList.size() < 4;
    }

    public void splitHand(int index) {
        if (index < 0 || index >= getHandQty()) {
            return;
        }
        Hand hand = getHand(index);
        handList.remove(index);
        handList.addAll(hand.split());
    }

    public void showHands() {
        for (Hand hand : handList) {
            hand.showHand();
        }
    }

    public void hideHands() {
        for (Hand hand : handList) {
            hand.hideHand();
        }
    }

    public void resolveHands(Player dealer) {
        for (Hand hand : handList) {
            hand.setResult(dealer.getHand(0));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Hand hand : handList) {
            sb.append(hand.toString()).append("\t");
        }
        return sb.toString();
    }
}
