package main;

import blackjack.Deck;
import blackjack.DeckType;
import blackjack.GameMode;
import blackjack.Hand;
import blackjack.Player;
import blackjack.Table;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {

    public static void main(String[] args) throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        Table table = null;
        int choice = 0;
        int numPlayers = 0;
        int numDecks = 0;
        int playerPos = 0;
        GameMode gameMode = GameMode.ALL_PLAYERS_VISIBLE;
        DeckType deckType = DeckType.SEGMENTED;

        while (choice != -1) {
            printMainMenu();
            println("Respond with 'exit' to quit.");
            // Choose in Menu
            do {
                choice = readChoice(br, 1, 3);
            } while (choice == 0);
            if (choice == -1)
                break;

            switch (choice) {
                case 1 -> {
                    println("How many players are there? (max 8)");
                    numPlayers = readChoice(br, 1, 8);
                    println("How many decks are used? (max 4)");
                    numDecks = readChoice(br, 1, 4);
                    // This is currently decided by the player, but may be assigned randomly or by join priority
                    println("What player are you?");
                    playerPos = readChoice(br, 1, numPlayers);
                    table = new Table(numPlayers, numDecks, gameMode, deckType);
                    table.setup();
                    println(table.toString(playerPos));
                    // Start main loop of game
                    Player player = table.getPlayer(playerPos - 1);
                    boolean forceQuit = playerAction(br, player, table.getDeck());
                    if (forceQuit) {
                        break;
                    }
                    
                }
                case 2 -> {
                    printUnavailable();
                    choice = 0;
                }
                case 3 -> {
                    int[] settings = new int[]{gameMode.ordinal(), deckType.ordinal()};
                    int[] newSettings = menuSettings(br, settings);
                    gameMode = GameMode.values()[newSettings[0]];
                    deckType = DeckType.values()[newSettings[1]];
                }
                default -> {
                    printInvalid();
                    choice = 0;
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

    private static int readChoice(BufferedReader br, int choiceMin, int choiceMax) throws IOException {
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
            }

            isValidChoice = choiceMin <= choice && choice <= choiceMax;
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
            println(option + ". Split (Split this hand)");
        }
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

    private static boolean playerAction(BufferedReader br, Player player, Deck deck) throws IOException {
        for (int i = 0; i < player.getHandQty(); i++) {
            Hand currentHand = player.getHand(i);
            boolean canSplit = player.canSplitHands() && currentHand.canSplit();
            boolean canSurrender = i == 0 && player.getHandQty() == 1 && currentHand.size() == 2;
            int pChoiceQty = canSplit && canSurrender ? 4 : canSplit || canSurrender ? 3 : 2;

            int choice;
            boolean playHand;
            do {
                printPlayerMenu(currentHand.toString(), canSplit, canSurrender);
                choice = readChoice(br, 1, pChoiceQty);
                if (choice == -1) {
                    return true;
                }
                playHand = player.action(i, choice, deck);
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

        int settingChoice = readChoice(br, 1, 3);

        int choiceMax = 1;
        if (settingChoice == 1) {
            printSetting1Menu(originalSettings[0]);
            choiceMax = GameMode.values().length;
        } else if (settingChoice == 2) {
            printSetting2Menu(originalSettings[1]);
            choiceMax = DeckType.values().length;
        }

        if (0 < settingChoice && settingChoice <= 2) {
            int choice = readChoice(br, 1, choiceMax);
            if (choice > 0) {
                newSettings[settingChoice - 1] = choice - 1;
            }
        }
        return newSettings;
    }

}
