package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private final GameMode gameMode;
    private final Deck deck;
    private final Player dealer;
    private final List<Player> players;

    /**
     * Initialize a table with the minimum number of requirements to play Blackjack with a dealer.
     */
    public Table() {
        this(1, 1, GameMode.ALL_PLAYERS_VISIBLE, DeckType.SEGMENTED);
    }

    /**
     * Initialize a table with a given number of players, decks and game settings.
     *
     * @param numPlayers How many players there are.
     * @param numDecks   How many decks there are.
     * @param gameMode   If all player cards are visible or not
     * @param deckType   If the whole deck is drawn from randomly among multiple decks or from one deck
     */
    public Table(int numPlayers, int numDecks, GameMode gameMode, DeckType deckType) {
        this.gameMode = gameMode;
        this.dealer = new Player();
        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }

        this.deck = new Deck(deckType, numDecks);
    }

    public Player getDealer() {
        return dealer;
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Updates all players of the table to have cards to play the game of Blackjack.
     */
    public void setup() {
        boolean isPlayerVisible = GameMode.ALL_PLAYERS_VISIBLE.equals(this.gameMode);

        deck.draw(); // The burn card, which is never used in blackjack
        for (Player player : players) {
            player.setHand(new Hand(deck.draw(isPlayerVisible)));
        }
        dealer.setHand(new Hand(deck.draw(false)));

        for (Player player : players) {
            player.getHand(0).addCard(deck.draw(isPlayerVisible));
        }
        dealer.getHand(0).addCard(deck.draw(true));
    }

    public void autoplay(Player player, int handNum, Card dealerUpCard) {
        if (player == null || dealerUpCard == null) {
            return;
        }

        Hand hand = player.getHand(handNum);
        if (hand == null) {
            hand = player.getHand(0);
        }

        PlayerChoice choice = player.choose(hand, dealerUpCard);
        boolean canPlay = this.play(player, handNum, choice);
        if (canPlay) {
            if (choice == PlayerChoice.SPLIT) {
                autoplay(player, handNum, dealerUpCard);
                autoplay(player, handNum + 1, dealerUpCard);
            } else {
                autoplay(player, handNum, dealerUpCard);
            }
        }
    }

    /**
     * For a select player that has made a choice, and action at the table will be performed based on that choice.
     *
     * @param playerPos  The position of the player at the table in the list of players.
     * @param handNum The index of the hand of the player.
     * @param choice  The choice made by the player that will be performed.
     * @return True if the player chooses to and can continue play more, otherwise false
     */
    public boolean play(int playerPos, int handNum, PlayerChoice choice) {
        if (playerPos < 0 || playerPos >= players.size()) {
            return false;
        }
        Player player = players.get(playerPos);

        if (handNum < 0 || handNum >= player.getHandQty()) {
            return false;
        }
        Hand hand = player.getHand(handNum);
        return play(player, handNum, choice);
    }

    /**
     * For a select player that has made a choice, and action at the table will be performed based on that choice.
     *
     * @param player  The Player whose hand will be played
     * @param handNum The index of the hand of the player.
     * @param choice  The choice made by the player that will be performed.
     * @return True if the player chooses to and can continue play more, otherwise false
     */
    public boolean play(Player player, int handNum, PlayerChoice choice) {
        Hand hand = player.getHand(handNum);
        switch (choice) {
            case HIT -> {
                hand.addCard(deck.draw());
                return !hand.isBust();
            }
            case SPLIT -> {
                if (hand.canSplit()) {
                    player.splitHand(handNum);
                    player.getHand(handNum).addCard(deck.draw());
                    player.getHand(handNum + 1).addCard(deck.draw());
                    return true;
                }
            }
            default -> {
                return false;
            }
        }
        return false;
    }

    /**
     * For every player, compare each player's hands with the dealer.
     */
    public void resolve() {
        // Dealer stands on 17, this can be updated in the future to have dealer hit on soft 17
        while (dealer.getHand(0).getValue() < 17) {
            dealer.getHand(0).addCard(deck.draw());
        }
        for (Player player : players) {
            player.resolve(dealer);
        }
    }

    public void reset() {
        int cardsToUndo = 0;
        Hand hand;
        for (Player player : players) {
            for (int h = 0; h < player.getHandQty(); h++) {
                for (int c = 0; c < player.getHand(h).size(); c++) {
                    if (h > 0 || c > 2) {
                        cardsToUndo++;
                    }
                }
            }
            // The first hand was split
            if (player.getHandQty() > 1) {
                hand = new Hand(player.getHand(0).getCard(0), player.getHand(1).getCard(0));
            } else {
                hand = new Hand(player.getHand(0).getCard(0), player.getHand(0).getCard(1));
            }
            player.setHand(hand);
        }
        for (int c = 2; c < dealer.getHand(0).size(); c++) {
            dealer.getHand(0).removeCard(c);
            cardsToUndo++;
        }
        for (int i = 0; i < cardsToUndo; i++) {
            deck.undoDraw();
        }
    }

    public Hand randomizeHand(Hand hand, int cardsToRandomize) {
        int iterations = Math.min(hand.size(), cardsToRandomize);
        for (int i = 0; i < iterations; i++) {
            deck.add(hand.getCard(i));
        }

        Hand newHand = new Hand();
        for (int i = 0; i < iterations; i++) {
            newHand.addCard(deck.draw());
        }
        for (int i = iterations; i < hand.size(); i++) {
            newHand.addCard(hand.getCard(i));
        }
        return newHand;
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

    private String displayDealer(boolean showAll) {
        if (showAll) {
            dealer.showHands();
        }
        return "Dealer: " + dealer.toString() + (dealer.getHand(0).isBust() ? " (BUST!)" : "");
    }

    private String displayPlayer(int playerNum, boolean isPlayer, boolean showAll) {
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
