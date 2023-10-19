package blackjack;

/**
 * The category of card that visually distinguishes a card from another of the same type in a deck of cards.
 * This has no purpose in the game of Blackjack, thus only being used to make a card unique.
 */
public enum CardSuit {
    SPADES,
    HEARTS,
    DIAMONDS,
    CLUBS;

    @Override
    public String toString() {
        return switch (this.ordinal()) {
            case 1 -> "h";
            case 2 -> "d";
            case 3 -> "c";
            default -> "s";
        };
    }

    public static CardSuit fromString(String str) {
        return switch (str) {
            case "s" -> SPADES;
            case "h" -> HEARTS;
            case "d" -> DIAMONDS;
            case "c" -> CLUBS;
            default -> null;
        };
    }
}
