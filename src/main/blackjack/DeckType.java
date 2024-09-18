package blackjack;

public enum DeckType {
    RANDOM("Deck has all cards randomized together"),
    SEGMENTED("Deck has all cards randomized within each deck segment");

    private String displayValue;

    DeckType(String displayValue){
        this.displayValue = displayValue;
    }

    public String getDisplay() {
        return this.displayValue;
    }
}
