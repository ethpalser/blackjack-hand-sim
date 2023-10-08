package game;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private final List<Card> cardList;
    private int bestValue;

    public Hand(Card first, Card second) {
        cardList = new ArrayList<>(8);
        cardList.add(first);
        cardList.add(second);
        bestValue = this.evaluate();
    }

    public int size() {
        return cardList.size();
    }

    public void add(Card card) {
        cardList.add(card);
        this.bestValue = this.evaluate();
    }

    public int evaluate() {
        int numAces = 0;
        int totalValue = 0;
        for (Card card : cardList) {
            // Add aces later, as it can have the value of 1 or 11
            if (card.compareTo(Card.ACE) == 0) {
                numAces++;
            } else {
                totalValue += card.getValue();
            }
        }
        // Treat all aces, except one, having the value 1, as counting two aces with 11 will be a bust.
        for (int i = 0; i < numAces - 1; i++) {
            totalValue += 1;
        }
        // Count the last ace as either 1 or 11
        if (numAces > 0) {
            if (totalValue + 11 <= 21) {
                totalValue += 11;
            } else {
                totalValue += 1;
            }
        }
        return totalValue;
    }

    public boolean isBust() {
        return bestValue > 21;
    }

    public boolean isWin(int dealerValue) {
        return !isBust() && bestValue > dealerValue;
    }

}
