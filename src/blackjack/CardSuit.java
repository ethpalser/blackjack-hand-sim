package blackjack;

public enum CardSuit {
    SPADE,
    HEART,
    DIAMOND,
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
}
