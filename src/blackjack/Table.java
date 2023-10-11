package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private final GameMode gameMode;
    private final Deck deck;
    private final Player dealer;
    private final List<Player> players;

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

    public boolean play(int playerNum, int handNum, int choice) {
        if (playerNum < 0 || playerNum >= players.size()) {
            return false;
        }
        Player player = getPlayer(playerNum);

        if (handNum < 0 || handNum >= player.getHandQty()) {
            return false;
        }
        Hand hand = player.getHand(handNum);
        switch (choice) {
            // hit
            case 1 -> {
                hand.addCard(deck.draw());
                return !hand.isBust();
            }
            // split
            case 3 -> {
                if (hand.canSplit()) {
                    player.splitHand(handNum);
                }
                // The current hand is updated when it is split, which the player can continue to play.
                return true;
            }
            // stand and surrender (for now)
            default -> {
                return false;
            }
        }
    }

    public void resolve() {
        // Dealer stands on 17, this can be updated in the future to have dealer hit on soft 17
        while (dealer.getHand(0).getValue() < 17) {
            dealer.getHand(0).addCard(deck.draw());
        }
        for (Player player : players) {
            player.resolveHands(dealer);
        }
    }

    // Todo
    public void undo() {
        // This is likely a new object with references to the previous table's data
        // Ie. Update constructor to do a true copy, or make an elegant undo
    }

    public Deck getDeck() {
        return deck;
    }

    public Player getDealer() {
        return dealer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    @Override
    public String toString() {
        return toString(-1, true);
    }

    public String toString(int playerNum) {
        return toString(playerNum, false);
    }

    /**
     * Print the table, but display the current player's cards regardless of those card's visibility.
     *
     * @param playerNum The player number the user has been assigned
     * @return String representing the table with all cards visible to the player
     */
    public String toString(int playerNum, boolean showResult) {
        StringBuilder sb = new StringBuilder();
        if (dealer != null) {
            sb.append(displayDealer(showResult)).append("\n");
        }
        if (players != null) {
            // The displayed and assigned player numbers start at 1
            for (int p = 0; p < players.size(); p++) {
                sb.append(displayPlayer(p + 1, p == playerNum, showResult)).append("\n");
            }
        }
        return sb.toString();
    }

    public String displayDealer(boolean showAll) {
        if (showAll) {
            dealer.showHands();
        }
        return "Dealer: " + dealer.toString() + (dealer.getHand(0).isBust() ? " (BUST!)" : "");
    }

    public String displayPlayer(int playerNum, boolean isPlayer, boolean showAll) {
        Player player = getPlayer(playerNum - 1);
        if (isPlayer || showAll) {
            player.showHands();
        }
        String str = "Player " + playerNum + (isPlayer ? " (You)" : "") + ": " + player.toString();
        if (GameMode.NO_PLAYERS_VISIBLE.equals(gameMode)) {
            player.hideHands();
        }
        return str;
    }

}
