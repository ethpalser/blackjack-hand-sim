package game;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private final DeckType deckType;
    private final int numDecks;
    private List<Card> cardList;
    private int cardsDealt;

    public Deck() {
        this(DeckType.SEGMENTED, 1);
    }

    public Deck(DeckType deckType, int numDecks) {
        if (numDecks < 1) {
            numDecks = 1;
        }
        if (numDecks > 4) {
            numDecks = 4;
        }
        this.deckType = deckType;
        this.numDecks = numDecks;
        cardList = this.setup(numDecks);
    }

    private List<Card> setup(int numDecks) {
        List<Card> cards = new ArrayList<>(52 * numDecks);
        for (int n = 0; n < numDecks; n++) {
            for (int i = 0; i < Card.values().length; i++) {
                for (int j = 0; j < 4; j++) {
                    cards.add(Card.values()[i]);
                }
            }
        }
        return cards;
    }

    public Card draw() {
        // "reshuffle" the deck by resetting it
        if (cardList.isEmpty()) {
            this.cardList = this.setup(this.numDecks);
        }
        int bounds = this.deckType.equals(DeckType.RANDOM) ? cardList.size() : 52 - Math.floorMod(cardsDealt, 52);
        int index = (int) (Math.random() * bounds);
        cardsDealt++;
        return cardList.remove(index);
    }
}
