package blackjack;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class DeckTest {

    @Test
    public void setup_oneDeck_shouldHaveFourOfEach() {
        Deck test = new Deck(DeckType.SEGMENTED, 1);

        int[] cardCount = new int[13];
        List<Card> cards = test.getAll();
        cards.forEach(card -> {
            // Using the ordinal position of the card to count each card
            cardCount[card.getType().ordinal()]++;
        });

        Assert.assertEquals(52, test.size());
        Assert.assertArrayEquals(new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, cardCount);
    }

    @Test
    public void setup_threeDecks_shouldHaveTwelveOfEach() {
        Deck test = new Deck(DeckType.SEGMENTED, 3);

        int[] cardCount = new int[13];
        List<Card> cards = test.getAll();
        cards.forEach(card -> {
            // Using the ordinal position of the card to count each card
            cardCount[card.getType().ordinal()]++;
        });

        Assert.assertEquals(156, test.size());
        Assert.assertArrayEquals(new int[]{12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12}, cardCount);
    }

    @Test
    public void draw_oneCardInDeck_shouldBeLastCard() {
        Card card = new Card(CardType.ACE, CardSuit.SPADES);
        Deck test = new Deck(DeckType.SEGMENTED, card);
        Card drawn = test.draw();
        Assert.assertEquals(card.getType(), drawn.getType());
        Assert.assertEquals(0, test.size());
    }

    @Test
    public void draw_noCardInDeck_shouldBeAnyCard() {
        Card card = new Card(CardType.ACE, CardSuit.SPADES);
        Deck test = new Deck(DeckType.SEGMENTED, card);
        test.draw(); // Empty the deck
        Card drawn = test.draw();
        Assert.assertNotNull(drawn);
        Assert.assertEquals(51, test.size());
    }

    @Test
    public void draw_52FromSegmentedDeck_shouldHaveFourOfEach() {
        Deck test = new Deck(DeckType.SEGMENTED, 4);

        int[] cardCount = new int[13];
        for (int i = 0; i < 52; i++) {
            cardCount[test.draw().getType().ordinal()]++;
        }
        Assert.assertArrayEquals(new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, cardCount);
    }

    @Test
    public void undoDraw_fromNewDeck_shouldDoNothing() {
        Deck test = new Deck(DeckType.RANDOM, 2, true);
        int size = test.size();

        test.undoDraw();
        Assert.assertEquals(size, test.size());
    }

    @Test
    public void undoDraw_fromPlayedDeck_shouldAddBackLastDrawn() {
        // Given an arbitrary amount (13) cards are drawn from the deck, and it is not shuffled
        Deck test = new Deck(DeckType.RANDOM, 2, false);
        for (int i = 0; i < 12; i++) {
            test.draw();
        }
        Card lastDrawn = test.draw();
        int[] cardCount = test.count();
        // When undo draw
        test.undoDraw();
        int[] newCardCount = test.count();
        for (int i = 0; i < cardCount.length; i++) {
            newCardCount[i] -= cardCount[i];
        }
        // Then expect the only card difference is the last drawn card
        int[] expected = new int[13];
        expected[lastDrawn.getType().ordinal()]++;

        String expect = "";
        String actual = "";
        for (int i = 0; i < 13; i++) {
            expect += expected[i] + " ";
            actual += newCardCount[i] + " ";
        }
        Assert.assertArrayEquals(expected, newCardCount);
    }

    // Has issue
    @Test
    public void undoDraw_fromNewlyShuffledDeck_shouldAddBackLastDrawnBeforeShuffle() {
        // Given an arbitrary amount (13) cards are drawn from the deck, and it is not shuffled
        Deck test = new Deck(DeckType.RANDOM, 2, true);
        int cardsToDraw = test.size() - test.getPosInsert();
        test.draw();
        // Draw until just before the deck will be shuffled
        for (int i = 0; i < cardsToDraw - 1; i++) {
            test.draw();
        }
        int[] cardCount = test.count();
        // Shuffle the deck
        test.draw();
        // When undo draw
        test.undoDraw();
        int[] newCardCount = test.count();

        Assert.assertArrayEquals(cardCount, newCardCount);
    }

    @Test
    public void findCard_kingOfSpadesFromNewDeck_shouldExist() {
        // Given a new deck
        Card card = new Card(CardType.KING, CardSuit.SPADES);
        Deck test = new Deck(DeckType.RANDOM, 4, true);
        // When findCard (King of Spades)
        int cardIndex = test.find(card);
        // Should return 12
        Assert.assertEquals(12, cardIndex);
    }

    @Test
    public void findCard_aceOfSpadesFromDeckWithoutAce_shouldNotExist() {
        // Given a new deck
        Card card = new Card(CardType.ACE, CardSuit.SPADES);
        Deck test = new Deck(DeckType.RANDOM, 1, true);
        test.remove(card);
        // When findCard (Ace of Spades)
        int cardIndex = test.find(card);
        // Should return -1
        Assert.assertEquals(-1, cardIndex);
    }

    @Test
    public void findCard_aceOfSpadesFromDeckWithOne_shouldExist() {
        int numDecks = 4;
        // Given a new deck with 4 decks
        Card card = new Card(CardType.ACE, CardSuit.SPADES);
        Deck test = new Deck(DeckType.RANDOM, numDecks, true);
        for (int i = 0; i < numDecks - 1; i++) {
            // Remove all but one ace
            test.remove(card);
        }
        // When removeCard (Ace of Spades)
        int cardIndex = test.find(card);
        // Should not return -1
        Assert.assertNotEquals(-1, cardIndex);
    }

    @Test
    public void removeCard_kingOfSpadesFromNewDeck_shouldExist() {
        // Given a new deck
        Card card = new Card(CardType.KING, CardSuit.SPADES);
        Deck test = new Deck(DeckType.RANDOM, 4, true);
        int originalSize = test.size();
        // When removeCard (King of Spades)
        Card removed = test.remove(card);
        int updatedSize = test.size();
        // Should return 12
        Assert.assertEquals(originalSize - 1, updatedSize);
        Assert.assertEquals(card.getType(), removed.getType());
    }

    @Test
    public void removeCard_aceOfSpadesFromDeckWithoutAce_shouldNotExist() {
        // Given a new deck without an Ace of Spades
        Deck test = new Deck(DeckType.RANDOM, 1, true);
        Card card = new Card(CardType.ACE, CardSuit.SPADES);
        test.remove(card);
        // When removeCard (Ace of Spades)
        Card removed = test.remove(card);
        // Should return -1
        Assert.assertNull(removed);
    }

    @Test
    public void removeCard_aceOfSpadesFromDeckWithOne_shouldExist() {
        // Given a new deck with 4 decks without 3 Ace of Spades
        int numDecks = 4;
        Deck test = new Deck(DeckType.RANDOM, numDecks, true);
        Card card = new Card(CardType.ACE, CardSuit.SPADES);
        for (int i = 0; i < numDecks - 1; i++) {
            // Remove all but one ace
            test.remove(card);
        }
        // When removeCard (Ace of Spades)
        Card removed = test.remove(card);
        // Should not return -1
        Assert.assertNotNull(removed);
    }

    @Test
    public void addCard_queenOfSpadesToNewDeck_shouldFail() {
        // Given new deck
        Deck test = new Deck(DeckType.RANDOM, 1, true);
        int originalSize = test.size();
        // When addCard
        Card card = new Card(CardType.QUEEN, CardSuit.SPADES);
        test.add(card);
        int updatedSize = test.size();
        // Should be equal
        Assert.assertEquals(originalSize, updatedSize);
    }

    @Test
    public void addCard_queenOfSpadesToDeckWithoutQueen_shouldAdd() {
        // Given new deck
        Deck test = new Deck(DeckType.RANDOM, 1, true);
        Card card = new Card(CardType.QUEEN, CardSuit.SPADES);
        test.remove(card);
        int originalSize = test.size();
        // When addCard
        test.add(card);
        int updatedSize = test.size();
        int index = test.find(card);
        // Should be equal
        Assert.assertEquals(originalSize + 1, updatedSize);
        Assert.assertEquals(11, index); // Queens should be the 12th card (11th index) in a complete deck
    }

}
