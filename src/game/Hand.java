package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hand {

    private final List<Card> cardList;
    private int bestValue;

    /**
     * Initialize a hand with no cards.
     */
    public Hand() {
        cardList = new ArrayList<>();
        bestValue = 0;
    }

    /**
     * Initialize a hand with one card, which is done when cards are being deal one at a time, or when a new hand is
     * created by splitting another hand.
     *
     * @param card First card dealt by dealer or split from another hand
     * @see Card
     */
    public Hand(Card card) {
        cardList = new ArrayList<>(8);
        cardList.add(card);
        this.evaluate();
    }

    /**
     * Initialize a hand with two cards, which is often done at the start of the game.
     *
     * @param first  First card dealt by dealer
     * @param second Second card dealt by dealer
     * @see Card
     */
    public Hand(Card first, Card second) {
        cardList = new ArrayList<>(8);
        cardList.add(first);
        cardList.add(second);
        this.evaluate();
    }

    public Hand(Card... cards) {
        cardList = new ArrayList<>(8);
        cardList.addAll(Arrays.asList(cards));
        this.evaluate();
    }

    public int size() {
        return cardList.size();
    }

    public Card getCard(int index) {
        return cardList.get(index);
    }

    public void addCard(Card card) {
        cardList.add(card);
        this.evaluate();
    }

    public int getValue() {
        return this.bestValue;
    }

    private void evaluate() {
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
        this.bestValue = totalValue;
    }

    public boolean isBust() {
        return bestValue > 21;
    }

    public boolean isWin(int dealerValue) {
        return !isBust() && bestValue > dealerValue;
    }

}
