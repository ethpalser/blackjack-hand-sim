package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private final GameMode gameMode;
    private final Player dealer;
    private List<Player> players;
    private Deck deck;

    public Table() {
        this(1, 1, GameMode.ALL_PLAYERS_VISIBLE, DeckType.SEGMENTED);
    }

    public Table(int numPlayers, int numDecks, GameMode gameMode, DeckType deckType) {
        this.gameMode = gameMode;
        this.dealer = new Player();
        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }

        this.deck = new Deck(deckType, numDecks);
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

    public Deck getDeck () {
        return deck;
    }

    public Player getDealer() {
        return dealer;
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if (dealer != null) {
            sb.append("Dealer: ").append(dealer).append("\n");
        }
        if (players != null) {
            int pos = 1;
            for (Player player : players) {
                sb.append("Player ").append(pos).append(": ").append(player).append("\n");
                pos++;
            }
        }
        return sb.toString();
    }

    /**
     * Print the table, but display the current player's cards regardless of those card's visibility.
     *
     * @param playerNum The player number the user has been assigned
     * @return String representing the table with all cards visible to the player
     */
    public String toString(int playerNum){
        StringBuilder sb = new StringBuilder();
        if (dealer != null) {
            sb.append("Dealer: ").append(dealer).append("\n");
        }
        if (players != null) {
            int pos = 1;
            for (Player player : players) {
                sb.append("Player ").append(pos);
                if (playerNum == pos) {
                    sb.append(" (You)");
                    player.showHands();
                }
                sb.append(": ").append(player).append("\n");

                if (playerNum == pos && GameMode.NO_PLAYERS_VISIBLE.equals(gameMode)) {
                    player.hideHands();
                }
                pos++;
            }
        }
        return sb.toString();
    }

}
