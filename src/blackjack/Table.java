package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private final Player dealer;
    private List<Player> players;
    private Deck deck;

    public Table() {
        this(1, DeckType.SEGMENTED, 1);
    }

    public Table(int numPlayers, DeckType deckType, int numDecks) {
        this.dealer = new Player();
        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }

        this.deck = new Deck(deckType, numDecks);
    }

    public void setup() {
        this.setup(GameMode.ALL_PLAYERS_VISIBLE);
    }

    public void setup(GameMode gameMode) {
        boolean isPlayerVisible = GameMode.ALL_PLAYERS_VISIBLE.equals(gameMode);

        deck.draw(); // The burn card, which is never used in blackjack
        for (Player player : players) {
            player.dealHand(new Hand(deck.draw(isPlayerVisible)));
        }
        dealer.dealHand(new Hand(deck.draw(false)));

        for (Player player : players) {
            player.getHand(0).addCard(deck.draw(isPlayerVisible));
        }
        dealer.getHand(0).addCard(deck.draw(true));
    }


}
