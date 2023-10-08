package game;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final GameMode gameMode;
    private final Player dealer;
    private List<Player> players;
    private DeckType deckType;
    private List<Card> cardList;

    public Game() {
        this(GameMode.ALL_VISIBLE, 1, DeckType.SEGMENTED, 1);
    }

    public Game(GameMode gameMode, int numPlayers, DeckType deckType, int numDecks) {
        this.gameMode = gameMode;

        this.dealer = new Player();
        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }

        this.deckType = deckType;
        this.cardList = this.setupDeck(numDecks);
    }

    private List<Card> setupDeck(int numDecks) {
        List<Card> cards = new ArrayList<>(52 * numDecks);
        for (int n = 0; n < numDecks; n++) {
            for (int i = 0; i < Card.values().length; i++) {
                for (int j = 0; j < 4; j++) {
                    cards.add(Card.values()[i]);
                }
            }
        }
        return cards;
    }

}
