package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deck {

    private final DeckType deckType;
    private final int numDecks;
    private List<Card> cardList;
    private int cardsDealt;

    public Deck() {
        this(DeckType.SEGMENTED, 1);
    }

    public Deck(DeckType deckType, Card... cards) {
        this.deckType = deckType;
        this.numDecks = (int) Math.ceil(cards.length / 52.0);
        this.cardList = new ArrayList<>();
        this.cardList.addAll(Arrays.asList(cards));
        this.cardsDealt = 52 * this.numDecks - cardList.size();
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
        this.cardList = this.setup(numDecks);
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

    public List<Card> getCards() {
        return this.cardList;
    }

    public int size() {
        return this.cardList.size();
    }

    public Card draw() {
        // "reshuffle" the deck by resetting it
        if (cardList == null || cardList.isEmpty()) {
            this.cardsDealt = 0;
            this.cardList = this.setup(this.numDecks);
        }
        int bounds = this.deckType.equals(DeckType.RANDOM) ? cardList.size() : 52 - Math.floorMod(cardsDealt, 52);
        int index = (int) (Math.random() * bounds);
        this.cardsDealt++;
        return cardList.remove(index);
    }
}
