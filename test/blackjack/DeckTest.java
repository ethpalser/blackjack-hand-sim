package blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class DeckTest {

    @Test
    public void setup_oneDeck_shouldHaveFourOfEach() {
        Deck test = new Deck(DeckType.SEGMENTED, 1);

        int[] cardCount = new int[13];
        List<Card> cards = test.getCards();
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
        List<Card> cards = test.getCards();
        cards.forEach(card -> {
            // Using the ordinal position of the card to count each card
            cardCount[card.getType().ordinal()]++;
        });

        Assert.assertEquals(156, test.size());
        Assert.assertArrayEquals(new int[]{12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12}, cardCount);
    }

    @Test
    public void draw_oneCardInDeck_shouldBeLastCard() {
        Deck test = new Deck(DeckType.SEGMENTED, Card.ACE());
        Card card = test.draw();
        Assert.assertEquals(Card.ACE().getType(), card.getType());
        Assert.assertEquals(0, test.size());
    }

    @Test
    public void draw_noCardInDeck_shouldBeAnyCard() {
        Deck test = new Deck(DeckType.SEGMENTED, Card.ACE());
        test.draw(); // Empty the deck
        Card card = test.draw();
        Assert.assertNotNull(card);
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
        int[] cardCount = test.countCards();
        // When undo draw
        test.undoDraw();
        int[] newCardCount = test.countCards();
        for (int i = 0; i < cardCount.length; i++) {
            newCardCount[i] -= cardCount[i];
        }
        // Then expect the only card difference is the last drawn card
        int[] expected = new int[13];
        expected[lastDrawn.getType().ordinal()]++;

        String expect = "";
        String actual = "";
        for(int i = 0; i < 13; i++){
            expect += expected[i] + " ";
            actual += newCardCount[i] + " ";
        }
        System.out.println(expect);
        System.out.println(actual);
        Assert.assertArrayEquals(expected, newCardCount);
    }

    @Test
    public void undoDraw_fromNewlyShuffledDeck_shouldAddBackLastDrawnBeforeShuffle() {
        // Given an arbitrary amount (13) cards are drawn from the deck, and it is not shuffled
        Deck test = new Deck(DeckType.RANDOM, 2, true);
        int cardsToDraw = test.size() - test.getSplitCard();
        test.draw();
        // Draw until just before the deck will be shuffled
        for (int i = 0; i < cardsToDraw - 1; i++) {
            test.draw();
        }
        int[] cardCount = test.countCards();
        // Shuffle the deck
        test.draw();
        // When undo draw
        test.undoDraw();
        int[] newCardCount = test.countCards();

        Assert.assertArrayEquals(cardCount, newCardCount);
    }

}
