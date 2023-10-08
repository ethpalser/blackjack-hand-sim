package game;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private List<Card> cards;

    public Hand(Card first, Card second) {
        cards = new ArrayList<>(5);
        cards.add(first);
        cards.add(second);
    }

    public int size() {
        return cards.size();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

}
