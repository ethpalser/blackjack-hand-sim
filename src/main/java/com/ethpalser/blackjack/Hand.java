package com.ethpalser.blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hand {

    private final List<Card> cardList;
    private int bestValue;
    private HandResult result;
    private int bet;

    /**
     * Initialize a hand with no cards.
     */
    public Hand() {
        cardList = new ArrayList<>();
        bestValue = 0;
        result = null;
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
        result = null;
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
        result = null;
    }

    /**
     * Initialize a hand with the given cards.
     *
     * @param cards A list of one or more cards
     */
    public Hand(Card... cards) {
        cardList = new ArrayList<>(8);
        cardList.addAll(Arrays.asList(cards));
        this.evaluate();
        result = null;
    }

    public Hand(List<Card> cards) {
        cardList = cards;
        this.evaluate();
        result = null;
    }

    /**
     * @return The number of cards in the hand
     */
    public int size() {
        return cardList.size();
    }

    /**
     * Get a card at the given index, if it exists.
     *
     * @param index A number from 0 to the hand's size.
     * @return A card if it exists, otherwise null
     */
    public Card getCard(int index) {
        if (index < 0 || cardList == null || index >= cardList.size())
            return null;
        return cardList.get(index);
    }

    /**
     * Adds a card to the hand and recalculates the hand's value.
     *
     * @param card A card
     */
    public void addCard(Card card) {
        cardList.add(card);
        this.evaluate();
    }

    /**
     * Remove a card from a hand. This should only be used for splitting a hand.
     *
     * @return The card removed from the hand.
     */
    public Card removeCard(int index) {
        if (index < 0 || cardList == null || index >= cardList.size())
            throw new IndexOutOfBoundsException();
        return cardList.remove(index);
    }

    /**
     * Updates all cards in the hand to visible.
     */
    public void showHand() {
        for (Card card : cardList) {
            card.setVisible(true);
        }
    }

    /**
     * Updates all cards in the hand to not visible.
     */
    public void hideHand() {
        for (Card card : cardList) {
            card.setVisible(false);
        }
    }

    /**
     * Gets the value of the hand. This value is compared to the dealer's value to determine if the hand is a win,
     * draw or loss.
     *
     * @return Best possible value of the hand.
     */
    public int getValue() {
        return this.bestValue;
    }

    /**
     * Gets the money wagered on the hand.
     *
     * @return The amount wagered on the hand.
     */
    public int getBet() {
        return this.bet;
    }

    /**
     * Replaces the hand's wagered amount with a new amount. In Blackjack, this bet amount should only be
     * modified when the player acquires this hand, doubles down (doubles bet) or surrenders (halves bet).
     *
     * @param bet New amount wagered on the hand.
     */
    public void setBet(int bet) {
        this.bet = bet;
    }

    /**
     * <p>Determine the best value of the hand. The value of the hand depends on the value of each card. Calculating
     * the hand's value is the sum of each card and using any Ace as its best value without exceeding 21.</p>
     * <p>Example: Ace, Seven, Queen</p>
     * <p>Seven: 7, Queen: 10, Ace: 1 or 11</p>
     * <p>Since 17 + 11 > 21 Ace will have the value 1.</p>
     * <p>Therefore, the best value is 18.</p>
     */
    private void evaluate() {
        int numAces = 0;
        int totalValue = 0;
        for (Card card : cardList) {
            // Add aces later, as it can have the value of 1 or 11
            if (CardType.ACE.equals(card.getType())) {
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

    /**
     * @return True if the best value is greater than 21, otherwise false
     */
    public boolean isBust() {
        return bestValue > 21;
    }

    /**
     * Compares this hand's value with another hand's value. The other hand should be the dealer's hand.
     *
     * @param dealer The hand of the dealer
     * @return True if this hand and better than dealer and not bust, otherwise false
     */
    public boolean isWin(Hand dealer) {
        return !isBust() && (dealer.isBust() || bestValue > dealer.getValue());
    }

    /**
     * A hand can only be split if there are only two cards, and they have the same type. Some games can allow cards
     * of the same value be split (i.e. Ten and Face cards).
     *
     * @return True if the hand has only two cards of the same type, otherwise false
     */
    public boolean canSplit() {
        return cardList.size() == 2 && getCard(0).getType().equals(getCard(1).getType());
    }

    /**
     * Compares this hand with the dealer's hand to determine if the result is a win, draw or loss.
     *
     * @param dealer Hand of the dealer's
     * @return HandResult of this hand compared to the dealer
     */
    public HandResult result(Hand dealer) {
        if (!isBust() && this.getValue() == dealer.getValue()) {
            this.result = HandResult.DRAW;
        } else if (isWin(dealer)) {
            this.result = HandResult.WIN;
        } else {
            this.result = HandResult.LOSS;
        }
        return this.result;
    }

    public HandResult getResult() {
        return this.result;
    }

    /**
     * A soft seventeen is a safe hand for requesting another card. It is large enough that it matches the dealer's
     * minimum stand value, but if the player wants to get a greater value they can safely.
     *
     * @return True if the hand's value equals 17 and includes one Ace and equals Seventeen
     */
    public boolean isSoftSeventeen() {
        return size() == 2 && getValue() == 17
                && (CardType.ACE.equals(getCard(0).getType()) || CardType.ACE.equals(getCard(1).getType()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cardList.size(); i++) {
            sb.append(cardList.get(i).isVisible() ? cardList.get(i).toString() : "x");
            if (i < cardList.size() - 1) {
                sb.append(" ");
            }
        }
        if (result != null) {
            sb.append("(").append(result).append(")");
        }
        return sb.toString();
    }
}
