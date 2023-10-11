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
    public int choose(int handIndex, Hand dealerHand) {
        Hand playerHand = this.getHand(handIndex);
        Card dealerUpCard = dealerHand.getCard(1);

        // This is a winning hand, unless the dealer also has blackjack
        if (playerHand.getValue() == 21) {
            return 2;
        }
        if (canSplitHands()) {
            CardType type = playerHand.getCard(0).getType();
            // Always split Ace and Eight, split Two, Three and Seven if up card is not good and Six if up card is poor
            if (CardType.ACE.equals(type) || CardType.EIGHT.equals(type)
                    || !dealerUpCard.getType().isGood() && (CardType.TWO.equals(type) || CardType.THREE.equals(type)
                    || CardType.SEVEN.equals(type))
                    || dealerUpCard.getType().isPoor() && CardType.SIX.equals(type)) {
                return 3;
            }
        }
        // This is a soft hand, so it is okay to hit in any case
        if (playerHand.getValue() == 17 && (playerHand.getCard(0).getType().equals(CardType.ACE)
                || playerHand.getCard(1).getType().equals(CardType.ACE))) {
            return 1;
        }
        // If below 17 and the dealer's up card is good, you are likely to lose, so it is better to try higher
        if (playerHand.getValue() < 17 && (dealerUpCard.getValue() == 7 || dealerUpCard.getValue() == 8
                || dealerUpCard.getValue() == 9 || dealerUpCard.getValue() == 10
                || dealerUpCard.getType().equals(CardType.ACE))) {
            return 1;
        }
        // The dealer is likely to bust trying to reach 17
        if (playerHand.getValue() < 12 && dealerUpCard.getType().isPoor()) {
            return 1;
        }
        // The dealer is likely to bust trying to reach 17
        if (playerHand.getValue() < 13 && dealerUpCard.getType().isFair()) {
            return 1;
        }
        return 2;
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
