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

    public boolean action(int hIndex, int choice, Deck deck) {
        if (hIndex < 0 || this.getHandQty() <= hIndex) {
            return false;
        }

        Hand hand = this.getHand(hIndex);
        switch (choice) {
            // hit
            case 1 -> {
                hand.addCard(deck.draw());
                return !hand.isBust();
            }
            // split
            case 3 -> {
                if (!hand.canSplit()) {
                    return true;
                }
                handList.remove(hIndex);
                handList.addAll(hand.split());
                return true;
            }
            // stand and surrender (for now)
            default -> {
                return false;
            }
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
