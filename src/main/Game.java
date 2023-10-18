package main;

import blackjack.Card;
import blackjack.DeckType;
import blackjack.GameMode;
import blackjack.Hand;
import blackjack.HandResult;
import blackjack.Player;
import blackjack.PlayerChoice;
import blackjack.Table;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

public class Game {

    public static void main(String[] args) throws IOException {
        GameMode gameMode = GameMode.ALL_PLAYERS_VISIBLE;
        DeckType deckType = DeckType.RANDOM;

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        Random rng = new Random();

        int choice;
        while (true) {
            printMainMenu();
            println("Respond with 'exit' to quit.");
            // Choose in Menu
            choice = readChoice(br, 3);
            if (choice == -1)
                break;

            switch (choice) {
                // Play Blackjack (Single Player)
                case 1 -> {
                    Table table = setupGame(br, gameMode, deckType);
                    playGame(br, rng, table);
                }
                // Simulate a hand
                case 2 -> {
                    Table table = setupGame(br, gameMode, deckType);
                    simulateGame(br, rng, table);
                }
                // Change settings
                case 3 -> {
                    int[] settings = new int[]{gameMode.ordinal(), deckType.ordinal()};
                    int[] newSettings = menuSettings(br, settings);
                    gameMode = GameMode.values()[newSettings[0]];
                    deckType = DeckType.values()[newSettings[1]];
                }
                default -> printInvalid();
            }
        }
    }

    /**
     * This asks how the game will be initialized and will execute that option. For new games, the number of players
     * and decks are requested, and then a new table is set up with this information. For loading from a file, this
     * file will be in a specific format to initialize the table.
     *
     * @param br BufferedReader for reading input
     * @param gameMode GameMode that determines how cards are viewed
     * @param deckType DeckType that determines how the deck is structured
     * @return Table containing players and deck for playing Blackjack
     * @throws IOException Runtime exception while reading an input
     */
    private static Table setupGame(BufferedReader br, GameMode gameMode, DeckType deckType) throws IOException {
        printSetupMenu();
        int readFile = readChoice(br, 2);
        // 0 = From File, 1 = Manual (Random Start)
        Table table;
        if (readFile == 1) {
            int numPlayers = askPlayerCount(br);
            int numDecks = askDeckCount(br);
            table = new Table(numPlayers, numDecks, gameMode, deckType);
            table.setup();
            return table;
        } else {
            printUnavailable();
            return null;
        }
    }

    /**
     * Endlessly plays Blackjack for one player and zero or more non-player entities. The player is the person
     * interacting with the input console. While playing the game the entities before the player will automatically
     * play, then the player, then all other entities. The player will make choices to determine how their player-entity
     * will act.
     *
     * @param br BufferedReader for reading input
     * @param rng Random for generating a random number
     * @param table Table containing all players and deck
     * @throws IOException Runtime exception while reading an input
     */
    private static void playGame(BufferedReader br, Random rng, Table table) throws IOException {
        if (rng == null || table == null) {
            return;
        }
        int numPlayers = table.getPlayers().size();
        int playerPos = rng.nextInt(numPlayers);
        // Start main loop of game
        while (true) {
            table.setup();
            println(table.toString(playerPos));

            Card dealerUpCard = table.getDealer().getHand(0).getCard(1);
            // run through players before you
            for (int i = 0; i < playerPos; i++) {
                Player player = table.getPlayer(i);
                table.autoplay(player, 0, dealerUpCard);
            }
            boolean exit = playerAction(br, table, playerPos);
            if (exit) {
                break;
            }
            // run through the remaining players
            for (int i = playerPos + 1; i < numPlayers; i++) {
                Player player = table.getPlayer(i);
                table.autoplay(player, 0, dealerUpCard);
            }

            table.resolve();
            println(table.toString(playerPos, true));
            println("------------------------------");
        }
        // Todo: Add bets to hand, and double down as an option
    }

    /**
     * Requests how many times the game will be simulated for the given table, and displays the result.
     *
     * @param br BufferedReader for reading input
     * @param rng Random for generating a random number
     * @param table Table containing all players and deck
     * @throws IOException Runtime exception while reading an input
     */
    private static void simulateGame(BufferedReader br, Random rng, Table table) throws IOException {
        if (rng == null || table == null) {
            return;
        }
        int numPlayers = table.getPlayers().size();
        int playerPos = rng.nextInt(numPlayers);
        println(table.toString(playerPos));

        println("How many times do you want to simulate your hand? (max 1,000,000)");
        int numSimulations = readChoice(br, 1000000);

        double[] results = simulateTable(table, numSimulations);
        println("Chance of this hand winning: " + results[playerPos] * 100 + "%");
        println("------------------------------");
    }

    /**
     * Automatically plays every player at the table by making a generally good choice while playing Blackjack. This
     * will execute the given number of simulations with the same table state.
     *
     * @param table Contains state of all players and deck playing Blackjack
     * @param numSimulations Number of times the game will be run
     * @return Array of all percentages of winning their hand
     */
    private static double[] simulateTable(Table table, int numSimulations) {
        List<Player> players = table.getPlayers();
        int numPlayers = table.getPlayers().size();
        Card dealerUpCard = table.getDealer().getHand(0).getCard(1);

        int[] wins = new int[numPlayers];
        int[] draws = new int[numPlayers];
        int[] losses = new int[numPlayers];
        for (int n = 0; n < numSimulations; n++) {
            for (Player player : players) {
                table.autoplay(player, 0, dealerUpCard);
            }
            // Randomize dealer's unrevealed card for more uncertainty in probability
            Hand dealerHand = table.getDealer().getHand(0);
            table.getDealer().setHand(table.randomizeHand(dealerHand, 1));
            table.resolve();

            for (int p = 0; p < players.size(); p++) {
                Player player = players.get(p);
                for (int q = 0; q < player.getHandQty(); q++) {
                    HandResult result = player.getHand(q).getResult();
                    switch (result) {
                        case WIN -> wins[p]++;
                        case DRAW -> draws[p]++;
                        case LOSS -> losses[p]++;
                    }
                }
            }
            table.reset();
        }

        double[] rates = new double[numPlayers];
        for (int p = 0; p < numPlayers; p++) {
            rates[p] = wins[p] / (double) (wins[p] + draws[p] + losses[p]);
        }
        return rates;
    }

    private static void println(String output) {
        System.out.println(output);
    }

    // region Dialogue Menus

    private static void printMainMenu() {
        println("Welcome to Blackjack!");
        println("1. Play Game");
        println("2. Simulate Hand");
        println("3. Update Settings");
    }

    private static void printPlayerMenu(String hand, boolean showSplit, boolean showSurrender) {
        println("What will you do for this hand (" + hand + ")?");
        println("1. Hit (Add another card)");
        println("2. Stand (Pass to next hand/player)");
        int option = 3;
        if (showSplit) {
            println(option + ". Split (Split this hand)");
            option++;
        }
        if (showSurrender) {
            println(option + ". Surrender (Forfeit hand, but only lose half your bet)");
        }
    }

    private static void printSetupMenu() {
        println("How will you start the game?");
        println("1. New Game");
        println("2. Load File");
    }

    private static void printSettingMenu() {
        println("What do you want to change?");
        println("1. Game Mode");
        println("2. Deck Type");
        println("3. Back");
    }

    private static void printSetting1Menu(int currentSetting) {
        println("Choose your game mode:");
        int pos = 1;
        for (GameMode gameMode : GameMode.values()) {
            println(pos + ". " + gameMode.getDisplay() + (gameMode.ordinal() == currentSetting ? " (Current)" : ""));
            pos++;
        }
    }

    private static void printSetting2Menu(int currentSetting) {
        println("Choose your deck type:");
        int pos = 1;
        for (DeckType deckType : DeckType.values()) {
            println(pos + ". " + deckType.getDisplay() + (deckType.ordinal() == currentSetting ? " (Current)" : ""));
            pos++;
        }
    }

    private static void printUnavailable() {
        println("This feature is not available.");
    }

    private static void printInvalid() {
        println("Invalid choice.");
    }

    // endregion

    // region Input Menus

    private static boolean isExit(String response) {
        return response.equalsIgnoreCase("exit") || response.equalsIgnoreCase("quit");
    }

    private static int readChoice(BufferedReader br, int choiceMax) throws IOException {
        int choice = 0;
        boolean isValidChoice = false;
        do {
            String response = br.readLine();
            if (isExit(response)) {
                return -1;
            }

            try {
                choice = Integer.parseInt(response);
            } catch (NumberFormatException ex) {
                printInvalid();
                continue;
            }

            isValidChoice = 1 <= choice && choice <= choiceMax;
            if (!isValidChoice) {
                printInvalid();
            }
        } while (!isValidChoice);
        return choice;
    }

    private static int askPlayerCount(BufferedReader br) throws IOException {
        println("How many players are there? (max 8)");
        return readChoice(br, 8);
    }

    private static int askDeckCount(BufferedReader br) throws IOException {
        println("How many decks are used? (max 8)");
        return readChoice(br, 8);
    }

    /**
     * The player that is playing Blackjack will perform an action at the table. This player is the given player
     * number. They will be shown the table and their hand and will be asked for their choice. This choice will
     * then be performed at the table.
     *
     * @param br BufferedReader for reading input
     * @param table Table containing all players and the deck of cards for playing the game
     * @param playerNum Index of the player at the table
     * @return True if the player chooses to and can continue making a choice, otherwise false
     * @throws IOException Runtime exception while reading an input
     */
    private static boolean playerAction(BufferedReader br, Table table, int playerNum) throws IOException {
        Player player = table.getPlayer(playerNum);
        for (int i = 0; i < player.getHandQty(); i++) {
            Hand currentHand = player.getHand(i);
            boolean canSplit = player.canSplit() && currentHand.canSplit();
            boolean canSurrender = i == 0 && player.getHandQty() == 1 && currentHand.size() == 2;

            int pChoiceQty = canSplit ? 4 : 3;
            int choice;
            boolean canPlay;
            do {
                printPlayerMenu(currentHand.toString(), canSplit, canSurrender);
                choice = readChoice(br, pChoiceQty);
                // "exit" was inputted
                if (choice == -1) {
                    return true;
                }
                // Offset other choices by one for when Split is not displayed
                if (!canSplit && choice >= 3) {
                    choice++;
                }
                PlayerChoice playerChoice = switch (choice) {
                    case 1 -> PlayerChoice.HIT;
                    case 3 -> PlayerChoice.SPLIT;
                    case 4 -> PlayerChoice.SURRENDER;
                    default -> PlayerChoice.STAND;
                };

                canPlay = table.play(playerNum, i, playerChoice);
                canSurrender = false;
                pChoiceQty = 2;
                println("Result: " + currentHand + "\n");
            } while (canPlay);
        }
        return false;
    }

    /**
     * Accept user input until they back out, and return an array of settings. Each value's index in the array of
     * settings corresponds to the setting menu options and its value is the sub-menu's options.
     *
     * @param br BufferedReader for reading input
     * @param originalSettings Original setting values
     * @return New setting values
     * @throws IOException Runtime exception while reading an input
     */
    private static int[] menuSettings(BufferedReader br, int[] originalSettings) throws IOException {
        printSettingMenu();
        int[] newSettings = new int[originalSettings.length];
        System.arraycopy(originalSettings, 0, newSettings, 0, newSettings.length);

        int settingChoice = readChoice(br, 3);

        int choiceMax = 1;
        if (settingChoice == 1) {
            printSetting1Menu(originalSettings[0]);
            choiceMax = GameMode.values().length;
        } else if (settingChoice == 2) {
            printSetting2Menu(originalSettings[1]);
            choiceMax = DeckType.values().length;
        }

        if (0 < settingChoice && settingChoice <= 2) {
            int choice = readChoice(br, choiceMax);
            if (choice > 0) {
                newSettings[settingChoice - 1] = choice - 1;
            }
        }
        return newSettings;
    }

    // endregion

}
