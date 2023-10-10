package blackjack;

public enum CardSuit {
    SPADE,
    CLUBS,
    HEART,
    DIAMOND;

    @Override
    public String toString() {
        return switch (this.ordinal()) {
            case 1 -> "c";
            case 2 -> "h";
            case 3 -> "d";
            default -> "s";
        };
    }
}
