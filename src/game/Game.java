package game;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final GameMode gameMode;
    private final Player dealer;
    private List<Player> players;
    private Deck deck;

    public Game() {
        this(GameMode.ALL_PLAYERS_VISIBLE, 1, DeckType.SEGMENTED, 1);
    }

    public Game(GameMode gameMode, int numPlayers, DeckType deckType, int numDecks) {
        this.gameMode = gameMode;

        this.dealer = new Player();
        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }

        this.deck = new Deck(deckType, numDecks);
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public void setup() {
        boolean isPlayerVisible = GameMode.ALL_PLAYERS_VISIBLE.equals(this.gameMode);

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
