package game;

/**
 * The representation of a card in a deck of playing cards. Each type has one intrinsic value within the game of
 * Blackjack, except ACE having two values. ACE's values will be considered in computations instead of stored.
 */
public enum CardType {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10);

    // This is the card's value in blackjack
    private final int value;

    CardType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
