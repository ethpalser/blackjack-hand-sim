package blackjack;

/**
 * The representation of a playing card within the game of Blackjack.
 * <br/>
 * In the game of Blackjack:
 * <ul>
 *     <li>
 *         Every player is dealt two cards, and may request more until they exceed 21. The total value is determined
 *         by the value of all cards a player possesses. The value is tied to the card's type. If any cards are ACEs
 *         then only one will be treated as 11 if the sum is below 21, and will otherwise be treated as 1.
 *     </li>
 *     <li>
 *         Every card has a suit but has no impact in the game of Blackjack. This exists as a visual property.
 *     </li>
 *     <li>
 *         Each card can be visible to every player, except the dealer has only one card visible. Additionally, one
 *         card is always burned and not visible to players. All played cards are visible until the deck is shuffled.
 *     </li>
 * </ul>
 *
 * @see CardType
 */
public class Card implements Comparable<Card> {

    private final CardType cardType;
    private final CardSuit cardSuit;
    private boolean isVisible;

    /**
     * Construct a Card given only its type.
     *
     * @param cardType Type of card from Two to Ace. This determines the card's value in Blackjack.
     */
    public Card(CardType cardType) {
        this(cardType, CardSuit.SPADE, false);
    }

    /**
     * Construct a card given all its variables.
     *
     * @param cardType  Type of card from Two to Ace. This determines the card's value in Blackjack.
     * @param cardSuit  The suit of a card to visually distinguish it.
     * @param isVisible The visibility of this card to all players.
     */
    public Card(CardType cardType, CardSuit cardSuit, boolean isVisible) {
        this.cardType = cardType;
        this.cardSuit = cardSuit;
        this.isVisible = isVisible;
    }

    /**
     * Construct a card given an id related to a card's index in a sorted deck of cards. I.e. 0 is Ace, 12 is King.
     * Anything outside these bounds will be treated as an Ace.
     *
     * @param cardId Represents the ordinal value of the card in a deck of playing cards.
     */
    public Card(int cardId) {
        this(cardId, CardSuit.SPADE, false);
    }


    /**
     * Construct a card given an id related to a card's index in a sorted deck of cards (I.e. 0 is Ace, 12 is King.
     * Anything outside these bounds will be treated as an Ace.), and all its other properties.
     *
     * @param cardId    Represents the ordinal value of the card in a deck of playing cards.
     * @param cardSuit  The suit of a card to visually distinguish it.
     * @param isVisible The visibility of this card to all players.
     */
    public Card(int cardId, CardSuit cardSuit, boolean isVisible) {
        if (cardId < 0 || cardId > 12) {
            this.cardType = CardType.ACE;
        } else {
            this.cardType = CardType.values()[cardId];
        }
        this.cardSuit = cardSuit;
        this.isVisible = isVisible;
    }

    public CardType getType() {
        return this.cardType;
    }

    public CardSuit getSuit() {
        return this.cardSuit;
    }

    public int getValue() {
        return this.cardType.getValue();
    }

    public boolean getVisible() {
        return this.isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public int compareTo(Card o) {
        return this.cardType.ordinal() - o.cardType.ordinal();
    }

    // region Static Cards

    public static Card ACE() {
        return new Card(CardType.ACE);
    }

    public static Card TWO() {
        return new Card(CardType.TWO);
    }

    public static Card THREE() {
        return new Card(CardType.THREE);
    }

    public static Card FOUR() {
        return new Card(CardType.FOUR);
    }

    public static Card FIVE() {
        return new Card(CardType.FIVE);
    }

    public static Card SIX() {
        return new Card(CardType.SIX);
    }

    public static Card SEVEN() {
        return new Card(CardType.SEVEN);
    }

    public static Card EIGHT() {
        return new Card(CardType.EIGHT);
    }

    public static Card NINE() {
        return new Card(CardType.NINE);
    }

    public static Card TEN() {
        return new Card(CardType.TEN);
    }

    public static Card JACK() {
        return new Card(CardType.JACK);
    }

    public static Card QUEEN() {
        return new Card(CardType.QUEEN);
    }

    public static Card KING() {
        return new Card(CardType.KING);
    }

    // endregion
}
