package blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deck {

    private final DeckType deckType;
    private final int numDecks;
    private final boolean hasInsert;

    private List<Card> cardList;
    private int numDrawn;
    private int posInsert;

    private List<Card> drawnList;
    private List<Integer> prevInsertList;

    public Deck() {
        this(DeckType.SEGMENTED, 1);
    }

    public Deck(DeckType deckType, int numDecks) {
        this(deckType, numDecks, false);
    }

    public Deck(DeckType deckType, int numDecks, boolean hasInsert) {
        if (numDecks < 1) {
            numDecks = 1;
        }
        if (numDecks > 8) {
            numDecks = 8;
        }
        this.deckType = deckType;
        this.numDecks = numDecks;
        this.hasInsert = hasInsert;
        this.cardList = this.setup();
        this.drawnList = new ArrayList<>(this.cardList.size() / 2);
        this.prevInsertList = new ArrayList<>();
    }

    public Deck(DeckType deckType, Card... cards) {
        this.deckType = deckType;
        this.numDecks = (int) Math.ceil(cards.length / 52.0);
        this.cardList = new ArrayList<>();
        this.cardList.addAll(Arrays.asList(cards));
        this.drawnList = new ArrayList<>(this.cardList.size() / 2);
        this.numDrawn = 52 * this.numDecks - cardList.size();
        this.hasInsert = false;
        this.posInsert = 52 * this.numDecks;
        this.prevInsertList = new ArrayList<>();
    }

    public int find(Card card) {
        int cardOrdinalValue = card.getOrdinalValue();
        int minBound = 0;
        int maxBound;
        int p;
        int q;
        int r;

        while (minBound < cardList.size()) {
            maxBound = this.findDeckBounds(minBound);
            // binary search the bounds of this deck
            p = minBound;
            q = maxBound;
            r = (p + q) / 2;
            while (q >= p) {
                // Equal card values
                if (cardOrdinalValue == cardList.get(r).getOrdinalValue()) {
                    return r;
                } // Card is to the right
                else if (cardOrdinalValue > cardList.get(r).getOrdinalValue()) {
                    p = r + 1;
                } // Card is to the left
                else if (cardOrdinalValue < cardList.get(r).getOrdinalValue()) {
                    q = r - 1;
                }
                r = (p + q) / 2;
            }
            minBound = maxBound + 1;
        }
        return -1;
    }

    public Card get(Card card) {
        int index = this.find(card);
        if (index < 0 || index > cardList.size()) {
            return null;
        }
        return this.cardList.get(index);
    }

    public List<Card> getAll() {
        return this.cardList;
    }

    public void add(Card card) {
        // Do not add any more cards to the deck, as this will exceed the number of decks there should be
        if (cardList.size() >= 52 * numDecks) {
            return;
        }
        int cardOrdinalValue = card.getOrdinalValue();
        int minBound = 0;
        int maxBound;
        int p;
        int q;
        int r;

        while (minBound < cardList.size()) {
            maxBound = this.findDeckBounds(minBound);
            // binary search the bounds of this deck
            p = minBound;
            q = maxBound;
            r = (p + q) / 2;
            while (q >= p) {
                if (cardOrdinalValue == cardList.get(r).getOrdinalValue()) {
                    break;
                } else if (cardOrdinalValue > cardList.get(r).getOrdinalValue()) {
                    p = r + 1;
                } else if (cardOrdinalValue < cardList.get(r).getOrdinalValue()) {
                    q = r - 1;
                }
                r = (p + q) / 2;
            }
            // This is where the card is less than all elements to the right and greater than those to the left
            if (cardOrdinalValue != cardList.get(r).getOrdinalValue()) {
                cardList.add(r + 1, card);
                break;
            }
            minBound = maxBound + 1;
        }
    }

    public Card remove(Card card) {
        int index = this.find(card);
        if (index < 0 || index > cardList.size()) {
            return null;
        }
        return cardList.remove(index);
    }

    public int size() {
        return this.cardList.size();
    }

    public int[] count() {
        int[] count = new int[13];
        for (int i = 0; i < this.size(); i++) {
            count[this.cardList.get(i).getType().ordinal()]++;
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            sb.append(cardList.get(i)).append(" ");
        }
        return sb.toString();
    }

    private List<Card> setup() {
        if (this.numDecks < 1) {
            throw new IllegalArgumentException("Deck must have at least 1 deck of cards.");
        }
        List<Card> cards = new ArrayList<>(52 * this.numDecks);
        for (int n = 0; n < this.numDecks; n++) {
            // Adding every card by suit
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 13; i++) {
                    cards.add(new Card(i, CardSuit.values()[j], false));
                }
            }
        }
        // Add a random split card to the deck that the dealer will stop at
        if (this.hasInsert) {
            this.posInsert = (int) (cards.size() / 6.0 + Math.random() * cards.size() * 4.0 / 6.0);
        } else {
            this.posInsert = 52 * numDecks;
        }
        this.numDrawn = 0;
        return cards;
    }

    public Card draw() {
        // "reshuffle" the deck by resetting it, either when it is empty or when the split card is reached
        if (cardList == null || cardList.isEmpty() || numDrawn >= posInsert) {
            this.prevInsertList.add(posInsert);
            this.cardList = this.setup();
        }
        int bounds = this.deckType.equals(DeckType.RANDOM) ? cardList.size() : 52 - Math.floorMod(numDrawn, 52);
        int index = (int) (Math.random() * bounds);
        Card card = cardList.remove(index);
        this.drawnList.add(card);
        this.numDrawn++;
        card.setVisible(true);
        return card;
    }

    public Card draw(boolean setVisible) {
        Card drawn = this.draw();
        drawn.setVisible(setVisible);
        return drawn;
    }

    public void undoDraw() {
        if (this.drawnList == null || this.drawnList.isEmpty()) {
            return;
        }
        // Add back the card that the dealer last drew
        this.add(this.drawnList.remove(this.drawnList.size() - 1));
        numDrawn--;

        // Newly reshuffled deck
        if (this.drawnList.size() > numDrawn && numDrawn == 0) {
            int prevDeckSize = 52 * numDecks - prevInsertList.get(prevInsertList.size() - 1);
            // If 0 is larger, the drawn deck is smaller than prev deck, otherwise drawn cards has multiple shuffles
            int stopIndex = Math.max(0, this.drawnList.size() - prevDeckSize);
            // Remove the card recently drawn
            this.drawnList.remove(this.drawnList.size() - 1);
            // reset deck
            this.setup();
            this.posInsert = prevInsertList.remove(prevInsertList.size() - 1);
            this.numDrawn = prevDeckSize - this.posInsert;

            for (int i = this.drawnList.size() - 1; i >= stopIndex; i--) {
                cardList.remove(drawnList.get(i));
            }
        }
    }

    public int getPosInsert() {
        return this.posInsert;
    }

    private int findDeckBounds(int start) {
        if (start < 0 || start >= cardList.size()) {
            throw new IndexOutOfBoundsException();
        }

        int startValue = cardList.get(start).getOrdinalValue();
        int[] seenValues = new int[52]; // There are only 52 possible values for a card in a deck

        int pos = start;
        int peek;
        while (pos < cardList.size()) {
            peek = cardList.get(pos).getOrdinalValue();
            // Already seen or this value is not within this deck
            if (seenValues[peek] > 0 || peek < startValue) {
                return pos - 1;
            }
            seenValues[peek]++;
            pos++;
        }
        return cardList.size() - 1;
    }
}
