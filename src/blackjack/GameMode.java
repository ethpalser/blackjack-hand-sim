package blackjack;

public enum GameMode {
    ALL_PLAYERS_VISIBLE("All player hands are visible"),
    NO_PLAYERS_VISIBLE("No player hands are visible");

    private String displayValue;

    GameMode(String displayValue){
        this.displayValue = displayValue;
    }

    public String getDisplay() {
        return this.displayValue;
    }
}
