package game;

/**
 * Cards represent the playing cards from One to Ten, Jack, Queen, King and Ace with their associated value in the
 * game of Blackjack.
 */
public enum Card {
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

    private final int value;

    Card(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
