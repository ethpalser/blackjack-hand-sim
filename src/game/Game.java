package game;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final GameMode gameMode;
    private final Player dealer;
    private List<Player> players;
    private Deck deck;

    public Game() {
        this(GameMode.NO_PLAYERS_VISIBLE, 1, DeckType.SEGMENTED, 1);
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


}
