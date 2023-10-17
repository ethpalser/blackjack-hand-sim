package main;

import blackjack.DeckType;
import blackjack.GameMode;
import blackjack.Hand;
import blackjack.HandResult;
import blackjack.Player;
import blackjack.Table;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

public class Game {

    public static void main(String[] args) throws IOException {
        Table table;
        int numPlayers;
        int numDecks;
        int playerPos;
        GameMode gameMode = GameMode.ALL_PLAYERS_VISIBLE;
        DeckType deckType = DeckType.SEGMENTED;

        Random random = new Random();
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

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
                    println("How many players are there? (max 8)");
                    numPlayers = readChoice(br, 8);

                    println("How many decks are used? (max 8)");
                    numDecks = readChoice(br, 8);

                    playerPos = random.nextInt(numPlayers);

                    table = new Table(numPlayers, numDecks, gameMode, deckType);
                    // Start main loop of game
                    while (true) {
                        table.setup();
                        println(table.toString(playerPos));
                        // run through players before you
                        for (int i = 0; i < playerPos; i++) {
                            Player player = table.getPlayer(i);
                            for (int h = 0; h < player.getHandQty(); h++) {
                                int npcChoice = player.choose(h, table.getDealer().getHand(0));
                                table.play(player, h, npcChoice);
                            }
                        }
                        boolean exit = playerAction(br, table, playerPos);
                        if (exit) {
                            break;
                        }
                        // run through the remaining players
                        for (int i = playerPos + 1; i < numPlayers; i++) {
                            Player player = table.getPlayer(i);
                            for (int h = 0; h < player.getHandQty(); h++) {
                                int npcChoice = player.choose(h, table.getDealer().getHand(0));
                                table.play(player, h, npcChoice);
                            }
                        }

                        table.resolve();
                        println(table.toString(playerPos, true));
                        println("------------------------------");
                    }
                    // Todo: Add bets to hand, and double down as an option
                }
                // Simulate a hand
                case 2 -> {
                    int readFile = readChoice(br, 2);
                    if (readFile == 1) {
                        println("How many players are there? (max 8)");
                        numPlayers = readChoice(br, 8);
                        println("How many decks are used? (max 8)");
                        numDecks = readChoice(br, 8);
                    } else {
                        printUnavailable();
                        continue;
                    }
                    playerPos = random.nextInt(numPlayers);
                    table = new Table(numPlayers, numDecks, gameMode, deckType);
                    println(table.toString(playerPos));

                    println("How many times do you want to simulate your hand? (max 1,000,000)");
                    int numSimulations = readChoice(br, 1000000);

                    int[] wins = new int[numPlayers];
                    int[] draws = new int[numPlayers];
                    int[] losses = new int[numPlayers];
                    List<Player> players = table.getPlayers();
                    for (int n = 0; n < numSimulations; n++) {
                        for (Player player : players) {
                            for (int h = 0; h < player.getHandQty(); h++) {
                                int npcChoice = player.choose(h, table.getDealer().getHand(0));
                                table.play(player, h, npcChoice);
                            }
                        }
                        table.resolve();
                        for (int p = 0; p < players.size(); p++) {
                            Player player = players.get(p);
                            for (int h = 0; h < player.getHandQty(); h++) {
                                HandResult result = player.getHand(h).getResult();
                                switch (result) {
                                    case WIN -> wins[p]++;
                                    case DRAW -> draws[p]++;
                                    case LOSS -> losses[p]++;
                                }
                            }
                        }
                        // Todo: implement undo
                        table.undo();
                    }
                    // Todo: Execute simulations in multiple threads, providing #iterations and table state
                    // For multiple threads, communicate result with MessageQueue which will communicate with master
                    // May want to use a thread pool with max of 32 threads?
                    // Either divide simulations between few threads or have many threads (thread starvation risk)

                    double rate = wins[playerPos] / (double) (wins[playerPos] + draws[playerPos] + losses[playerPos]);
                    println("Chance of this hand winning: " + rate * 100 + "%");
                }
                // Change settings
                case 3 -> {
                    int[] settings = new int[]{gameMode.ordinal(), deckType.ordinal()};
                    int[] newSettings = menuSettings(br, settings);
                    gameMode = GameMode.values()[newSettings[0]];
                    deckType = DeckType.values()[newSettings[1]];
                }
                default -> {
                    printInvalid();
                }
            }
        }

    }

    private static void println(String output) {
        System.out.println(output);
    }

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

    private static void printSimulationMenu() {
        println("How will you run the simulation:");
        println("1. Manual (New Table)");
        println("2. Read File (Custom Table)");
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

    private static boolean playerAction(BufferedReader br, Table table, int playerNum) throws IOException {
        Player player = table.getPlayer(playerNum);
        for (int i = 0; i < player.getHandQty(); i++) {
            Hand currentHand = player.getHand(i);
            boolean canSplit = player.canSplit() && currentHand.canSplit();
            boolean canSurrender = i == 0 && player.getHandQty() == 1 && currentHand.size() == 2;

            int pChoiceQty = canSplit ? 4 : 3;
            int choice;
            boolean playHand;
            do {
                printPlayerMenu(currentHand.toString(), canSplit, canSurrender);
                choice = readChoice(br, pChoiceQty);
                // Offset other choices by one for when Split is not displayed
                if (!canSplit && choice >= 3) {
                    choice++;
                }
                if (choice == -1) {
                    return true;
                }
                playHand = table.play(player, i, choice);
                // Add a card to each hand before the player makes another decision
                if (canSplit && choice == 3) {
                    table.play(player, i, 1);
                    table.play(player, i + 1, 1);
                }
                canSurrender = false;
                pChoiceQty = 2;
                println("Result: " + currentHand + "\n");
            } while (playHand);
        }
        return false;
    }

    /**
     * Accept user input until they back out, and return an array of settings. Each value's index in the array of
     * settings corresponds to the setting menu options and its value is the sub-menu's options.
     *
     * @param originalSettings Original setting values
     * @return New setting values
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

}
