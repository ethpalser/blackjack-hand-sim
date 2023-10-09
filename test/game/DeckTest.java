package game;

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
            cardCount[card.ordinal()]++;
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
            cardCount[card.ordinal()]++;
        });

        Assert.assertEquals(156, test.size());
        Assert.assertArrayEquals(new int[]{12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12}, cardCount);
    }

    @Test
    public void draw_oneCardInDeck_shouldBeLastCard() {
        Deck test = new Deck(DeckType.SEGMENTED, Card.ACE);
        Card card = test.draw();
        Assert.assertEquals(Card.ACE, card);
        Assert.assertEquals(0, test.size());
    }

    @Test
    public void draw_noCardInDeck_shouldBeAnyCard() {
        Deck test = new Deck(DeckType.SEGMENTED, Card.ACE);
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
            cardCount[test.draw().ordinal()]++;
        }
        Assert.assertArrayEquals(new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, cardCount);
    }

}
