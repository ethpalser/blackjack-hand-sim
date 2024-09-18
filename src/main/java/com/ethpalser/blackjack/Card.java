package com.ethpalser.blackjack;

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
        this(cardType, CardSuit.SPADES, true);
    }

    /**
     * Construct a Card given its type and suit
     *
     * @param cardType Type of card from Two to Ace. This determines the card's value in Blackjack.
     * @param cardSuit Suit of card (Spades, Hearts, Diamonds, Clubs). This affects the card's order in the deck
     */
    public Card(CardType cardType, CardSuit cardSuit) {
        this(cardType, cardSuit, true);
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
        this(cardId, CardSuit.SPADES, false);
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
            throw new IndexOutOfBoundsException();
        } else {
            this.cardType = CardType.values()[cardId];
        }
        this.cardSuit = cardSuit;
        this.isVisible = isVisible;
    }

    /**
     * @return CardType of this card.
     * @see CardType
     */
    public CardType getType() {
        return this.cardType;
    }

    /**
     * @return CardSuit of this card.
     * @see CardSuit
     */
    public CardSuit getSuit() {
        return this.cardSuit;
    }

    /**
     * @return The value of this card in the game of Blackjack
     */
    public int getValue() {
        return this.cardType.getValue();
    }

    /**
     * @return The value of this card in a deck of cards
     */
    public int getOrdinalValue() {
        return 13 * this.cardSuit.ordinal() + this.cardType.ordinal();
    }

    /**
     * A card's visibility determines if all players can see the card or only the person holding the card can.
     *
     * @return True or false
     */
    public boolean isVisible() {
        return this.isVisible;
    }

    /**
     * Reveals or hides the card for all players, except the person holding this card.
     *
     * @param isVisible True if the card is revealed to all, false if the card is revealed only to the holder
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }


    @Override
    public int compareTo(Card o) {
        return this.getOrdinalValue() - o.getOrdinalValue();
    }

    @Override
    public String toString() {
        return this.cardType.toString();
    }

    /**
     * A poor card is one that has a low value and is likely to make a hand bust on another hit.
     *
     * @return True if Four, Five or Six; else false
     */
    public boolean isPoor() {
        return switch (this.getType()) {
            case FOUR, FIVE, SIX -> true;
            default -> false;
        };
    }

    /**
     * A fair card is one that is unlikely to make a hand bust on another hit, but will require a hit.
     *
     * @return True if Two or Three; else false
     */
    public boolean isFair() {
        return switch (this.getType()) {
            case TWO, THREE -> true;
            default -> false;
        };
    }

    /**
     * A good card is one that is likely to win without a hit.
     *
     * @return True if Ace, Seven, Eight, Nine, Ten, Jack, Queen or King; else false
     */
    public boolean isGood() {
        return switch (this.getType()) {
            case ACE, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING -> true;
            default -> false;
        };
    }

    public static Card parseCard(String str) {
        CardType type;
        CardSuit suit = CardSuit.SPADES;
        if (str.length() == 1) {
            type = CardType.fromString(str.substring(0, 1));
        } else if (str.length() == 2) {
            type = CardType.fromString(str.substring(0, 1));
            suit = CardSuit.fromString(str.substring(1, 2));
        } else {
            throw new IllegalArgumentException("String format is invalid. String must have only one or two characters");
        }
        boolean isVisible = !"x".equals(str.substring(0, 1));
        return new Card(type, suit, isVisible);
    }
}
