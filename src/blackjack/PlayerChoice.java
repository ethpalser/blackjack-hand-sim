package blackjack;

public enum PlayerChoice {
    HIT("Hit"),
    SPLIT("Split"),
    STAND("Stand"),
    SURRENDER("Surrender");

    private final String display;

    PlayerChoice(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
