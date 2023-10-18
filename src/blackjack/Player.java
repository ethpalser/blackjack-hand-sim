package blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 * The representation of a person at a table participating in a game of Blackjack. Each player always has one hand, and
 * may split that hand and subsequent hands (if possible) to have more hands for a round of Blackjack.
 */
public class Player {

    private List<Hand> handList;

    public Player() {
        this.handList = new ArrayList<>();
    }

    /**
     * Get a hand from the player's list of hands. The player should always have at least one hand while playing.
     *
     * @param index Index of hand in the list
     * @return A Hand of cards
     * @see Hand
     */
    public Hand getHand(int index) {
        if (index < 0 || handList == null || index >= handList.size())
            return null;
        return handList.get(index);
    }

    /**
     * Gives the player a hand. All other hands are discarded, as a player may only have more hands by splitting.
     *
     * @param hand A Hand of cards
     * @see Hand
     */
    public void setHand(Hand hand) {
        handList = new ArrayList<>();
        handList.add(hand);
    }

    /**
     * @return The quantity of hands held by the player.
     */
    public int getHandQty() {
        return handList.size();
    }

    /**
     * Reveals all cards in all hands held by the player.
     */
    public void showHands() {
        for (Hand hand : handList) {
            hand.showHand();
        }
    }

    /**
     * Hides all cards in all hands held by the player.
     */
    public void hideHands() {
        for (Hand hand : handList) {
            hand.hideHand();
        }
    }

    /**
     * A player is allowed to split their hands, if any can be split, until they have four hands.
     *
     * @return True if the player has less than four hands, otherwise false
     */
    public boolean canSplit() {
        return handList.size() < 4;
    }

    /**
     * The hand at the given index will be split if it can. If the hand can be split one card will be removed
     * and used for the next hand. These hands will only have one card, and will need to be given a new card to be
     * valid.
     *
     * @param index Index in the player's list of hands
     */
    public void splitHand(int index) {
        if (index < 0 || index >= getHandQty()) {
            return;
        }

        Hand hand = getHand(index);
        if (!hand.canSplit()) {
            return;
        }

        Card card = null;
        try {
            card = hand.removeCard(1);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        if (card != null) {
            Hand splitHand = new Hand(card);
            handList.add(splitHand);
        }
    }

    /**
     * This will return a choice using known information from the player's hand and the dealer's hand. Choices are
     * the following:
     * 1. Hit
     * 2. Stand
     * 3. Split
     * 4. Surrender
     *
     * @param handIndex  The hand that the choice will be made for. In most cases this value is 0.
     * @param dealerHand The dealer's hand that decisions will be based off of.
     * @return A choice that will be performed
     */
    public PlayerChoice choose(int handIndex, Hand dealerHand) {
        Hand playerHand = this.getHand(handIndex);
        Card dealerUpCard = dealerHand.getCard(1);
        return this.choose(playerHand, dealerUpCard);
    }

    /**
     * This will return a choice using known information from the player's hand and the dealer's hand. Choices are
     * the following:
     * 1. Hit
     * 2. Stand
     * 3. Split
     * 4. Surrender
     *
     * @param playerHand  The hand that the choice will be made for.
     * @param dealerUpCard The dealer's only visible card used to for making a choice.
     * @return A choice that will be performed
     */
    public PlayerChoice choose(Hand playerHand, Card dealerUpCard) {
        // This is a winning hand, unless the dealer also has blackjack
        if (playerHand.getValue() == 21) {
            return PlayerChoice.STAND;
        }

        CardType playerCardType = playerHand.getCard(0).getType();
        if (canSplit()
                && (CardType.ACE.equals(playerCardType) || CardType.EIGHT.equals(playerCardType)
                || CardType.SIX.equals(playerCardType) && dealerUpCard.isPoor()
                || (CardType.TWO.equals(playerCardType) || CardType.THREE.equals(playerCardType)
                || CardType.SEVEN.equals(playerCardType)) && !dealerUpCard.isGood())) {
            // Always split Ace and Eight, split Two, Three and Seven if up card is not good and Six if up card is poor
            return PlayerChoice.SPLIT;
        }

        // This is a soft hand, so it is always safe to hit, or
        // You are likely to lose, so it is better to try higher, or
        // The dealer is likely to bust trying to reach 17
        if (playerHand.isSoftSeventeen()
                || playerHand.getValue() < 17 && dealerUpCard.isGood()
                || playerHand.getValue() < 13 && dealerUpCard.isFair()
                || playerHand.getValue() < 12 && dealerUpCard.isPoor()) {
            return PlayerChoice.HIT;
        }
        return PlayerChoice.STAND;
    }

    /**
     * Resolves all of this player's hands with the dealer's hand.
     *
     * @param dealer Player representing the dealer
     */
    public void resolve(Player dealer) {
        for (Hand hand : handList) {
            hand.result(dealer.getHand(0));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Hand hand : handList) {
            sb.append(hand.toString()).append(" ");
        }
        return sb.toString();
    }
}
