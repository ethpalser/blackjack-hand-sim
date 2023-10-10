package blackjack;

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
        if (index < 0 || cardList == null || index >= cardList.size())
            return null;
        return cardList.get(index);
    }

    public void addCard(Card card) {
        cardList.add(card);
        this.evaluate();
    }

    public void showHand() {
        for (Card card : cardList) {
            card.setVisible(true);
        }
    }

    public void hideHand() {
        for (Card card : cardList) {
            card.setVisible(false);
        }
    }

    public int getValue() {
        return this.bestValue;
    }

    private void evaluate() {
        int numAces = 0;
        int totalValue = 0;
        for (Card card : cardList) {
            // Add aces later, as it can have the value of 1 or 11
            if (card.compareTo(Card.ACE()) == 0) {
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

    public boolean isWin(Hand dealer) {
        return !isBust() && (dealer.isBust() || bestValue > dealer.getValue());
    }

    public boolean canSplit() {
        return cardList.size() == 2 && cardList.get(0).compareTo(cardList.get(1)) == 0;
    }

    public List<Hand> split() {
        if (!canSplit()) {
            return List.of(this);
        }
        List<Hand> newHands = new ArrayList<>();
        newHands.add(new Hand(getCard(0)));
        newHands.add(new Hand(getCard(1)));
        return newHands;
    }

    public int compare(Hand dealer) {
        if (this.getValue() == dealer.getValue()) {
            return 0;
        } else if (isWin(dealer)) {
            return 1;
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Card card : cardList) {
            sb.append(card.getVisible() ? card.toString() : "x");
        }
        return sb.toString();
    }

}
