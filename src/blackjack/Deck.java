package blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deck {

    private final DeckType deckType;
    private final int numDecks;
    private List<Card> cardList;
    private int cardsDealt;
    private boolean hasSplitCard;
    private int splitCard;

    public Deck() {
        this(DeckType.SEGMENTED, 1);
    }

    public Deck(DeckType deckType, Card... cards) {
        this.deckType = deckType;
        this.numDecks = (int) Math.ceil(cards.length / 52.0);
        this.cardList = new ArrayList<>();
        this.cardList.addAll(Arrays.asList(cards));
        this.cardsDealt = 52 * this.numDecks - cardList.size();
        this.hasSplitCard = false;
        this.splitCard = 52 * this.numDecks;
    }

    public Deck(DeckType deckType, int numDecks) {
        this(deckType, numDecks, false);
    }

    public Deck(DeckType deckType, int numDecks, boolean hasSplitCard) {
        if (numDecks < 1) {
            numDecks = 1;
        }
        if (numDecks > 4) {
            numDecks = 4;
        }
        this.deckType = deckType;
        this.numDecks = numDecks;
        this.hasSplitCard = hasSplitCard;
        this.cardList = this.setup(numDecks, hasSplitCard);
    }

    private List<Card> setup(int numDecks) {
        return this.setup(numDecks, this.hasSplitCard);
    }

    private List<Card> setup(int numDecks, boolean hasSplitCard) {
        List<Card> cards = new ArrayList<>(52 * numDecks);
        for (int n = 0; n < numDecks; n++) {
            for (int i = 0; i < 13; i++) {
                for (int j = 0; j < 4; j++) {
                    cards.add(new Card(i, CardSuit.values()[j], false));
                }
            }
        }
        // Add a random split card to the deck that the dealer will stop at
        if (hasSplitCard) {
            this.splitCard = (int) (cards.size() / 6.0 + Math.random() * cards.size() * 4.0 / 6.0);
        } else {
            this.splitCard = 52 * numDecks;
        }
        this.cardsDealt = 0;
        return cards;
    }

    public List<Card> getCards() {
        return this.cardList;
    }

    public int size() {
        return this.cardList.size();
    }

    public Card draw() {
        // "reshuffle" the deck by resetting it, either when it is empty or when the split card is reached
        if (cardList == null || cardList.isEmpty() || cardsDealt >= splitCard) {
            this.cardList = this.setup(this.numDecks);
        }
        int bounds = this.deckType.equals(DeckType.RANDOM) ? cardList.size() : 52 - Math.floorMod(cardsDealt, 52);
        int index = (int) (Math.random() * bounds);
        this.cardsDealt++;
        return cardList.remove(index);
    }

    public Card draw(boolean setVisible) {
        Card drawn = this.draw();
        drawn.setVisible(setVisible);
        return drawn;
    }
}