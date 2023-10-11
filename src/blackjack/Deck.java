package blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deck {

    private final DeckType deckType;
    private final int numDecks;
    private List<Card> cardList;
    private List<Card> cardsDrawnList;
    private int cardsDrawn;
    private boolean hasSplitCard; // This card is inserted to the deck randomly to reduce card counting
    private int splitCard;
    private List<Integer> prevSplitCard;

    public Deck() {
        this(DeckType.SEGMENTED, 1);
    }

    public Deck(DeckType deckType, Card... cards) {
        this.deckType = deckType;
        this.numDecks = (int) Math.ceil(cards.length / 52.0);
        this.cardList = new ArrayList<>();
        this.cardList.addAll(Arrays.asList(cards));
        this.cardsDrawnList = new ArrayList<>(this.cardList.size() / 2);
        this.cardsDrawn = 52 * this.numDecks - cardList.size();
        this.hasSplitCard = false;
        this.splitCard = 52 * this.numDecks;
        this.prevSplitCard = new ArrayList<>();
    }

    public Deck(DeckType deckType, int numDecks) {
        this(deckType, numDecks, false);
    }

    public Deck(DeckType deckType, int numDecks, boolean hasSplitCard) {
        if (numDecks < 1) {
            numDecks = 1;
        }
        if (numDecks > 8) {
            numDecks = 8;
        }
        this.deckType = deckType;
        this.numDecks = numDecks;
        this.hasSplitCard = hasSplitCard;
        this.cardList = this.setup(numDecks, hasSplitCard);
        this.cardsDrawnList = new ArrayList<>(this.cardList.size() / 2);
        this.prevSplitCard = new ArrayList<>();
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
        this.cardsDrawn = 0;
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
        if (cardList == null || cardList.isEmpty() || cardsDrawn >= splitCard) {
            this.prevSplitCard.add(splitCard);
            this.cardList = this.setup(this.numDecks);
        }
        int bounds = this.deckType.equals(DeckType.RANDOM) ? cardList.size() : 52 - Math.floorMod(cardsDrawn, 52);
        int index = (int) (Math.random() * bounds);
        Card card = cardList.remove(index);
        this.cardsDrawnList.add(card);
        this.cardsDrawn++;
        card.setVisible(true);
        return card;
    }

    public Card draw(boolean setVisible) {
        Card drawn = this.draw();
        drawn.setVisible(setVisible);
        return drawn;
    }

    public void undoDraw() {
        if (this.cardsDrawnList == null || this.cardsDrawnList.isEmpty()) {
            return;
        }
        // Add back the card that the dealer last drew
        this.cardList.add(this.cardsDrawnList.remove(this.cardsDrawnList.size() - 1));
        cardsDrawn--;

        // Newly reshuffled deck
        if (this.cardsDrawnList.size() > cardsDrawn && cardsDrawn == 0) {
            int prevDeckSize = 52 * numDecks - prevSplitCard.get(prevSplitCard.size() - 1);
            // If 0 is larger, the drawn deck is smaller than prev deck, otherwise drawn cards has multiple shuffles
            int stopIndex = Math.max(0, this.cardsDrawnList.size() - prevDeckSize);
            // Remove the card recently drawn
            this.cardsDrawnList.remove(this.cardsDrawnList.size() - 1);
            // reset deck
            setup(numDecks, hasSplitCard);
            this.splitCard = prevSplitCard.remove(prevSplitCard.size() - 1);
            this.cardsDrawn = prevDeckSize - this.splitCard;

            for (int i = this.cardsDrawnList.size() - 1; i >= stopIndex; i--) {
                cardList.remove(cardsDrawnList.get(i));
            }
        }
    }

    public int[] countCards() {
        int[] count = new int[13];
        for (int i = 0; i < this.size(); i++) {
            count[this.cardList.get(i).getType().ordinal()]++;
        }
        return count;
    }

    public int getSplitCard() {
        return this.splitCard;
    }
}
