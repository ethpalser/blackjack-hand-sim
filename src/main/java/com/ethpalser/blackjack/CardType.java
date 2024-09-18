package com.ethpalser.blackjack;

/**
 * The representation of a card in a deck of playing cards. Each type has one intrinsic value within the game of
 * Blackjack, except ACE having two values. ACE's values will be considered in computations instead of stored.
 */
public enum CardType {
    ACE(1, "A"),
    TWO(2, "2"),
    THREE(3, "3"),
    FOUR(4, "4"),
    FIVE(5, "5"),
    SIX(6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8"),
    NINE(9, "9"),
    TEN(10, "10"),
    JACK(10, "J"),
    QUEEN(10, "Q"),
    KING(10, "K");

    // This is the card's value in blackjack
    private final int value;
    private final String str;

    CardType(int value, String str) {
        this.value = value;
        this.str = str;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.str;
    }

    public static CardType fromString(String string) {
        return switch (string) {
            case "A" -> ACE;
            case "2" -> TWO;
            case "3" -> THREE;
            case "4" -> FOUR;
            case "5" -> FIVE;
            case "6" -> SIX;
            case "7" -> SEVEN;
            case "8" -> EIGHT;
            case "9" -> NINE;
            case "10" -> TEN;
            case "J" -> JACK;
            case "Q" -> QUEEN;
            case "K" -> KING;
            case "x" -> CardType.values()[(int) Math.floor(Math.random() * 13)];
            default -> null;
        };
    }
}
