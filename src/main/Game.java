package main;

import blackjack.DeckType;
import blackjack.GameMode;
import blackjack.Table;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {

    private Table table;

    public Game(int numPlayers, int numDecks) {
        this.table = new Table(numPlayers, DeckType.SEGMENTED, numDecks);
    }

    public static void main(String[] args) throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        int choice = 0;
        int numPlayers = 0;
        int numDecks = 0;
        GameMode gameMode = GameMode.ALL_PLAYERS_VISIBLE;
        DeckType deckType = DeckType.SEGMENTED;

        while (choice != -1) {
            printMainMenu();
            println("Respond with 'exit' at any point to quit.");
            // Choose in Menu
            do {
                choice = readChoice(br, 1, 3);
            } while (choice == 0);
            if (choice == -1)
                break;

            switch (choice) {
                case 1 -> {
                    printUnavailable();
                    choice = 0;
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
        String response = br.readLine();
        if (isExit(response)) {
            return -1;
        }

        int choice = 0;
        try {
            choice = Integer.parseInt(response);
        } catch (NumberFormatException ex) {
            printInvalid();
        }

        boolean isValidChoice = choiceMin <= choice && choice <= choiceMax;
        if (!isValidChoice) {
            printInvalid();
        }

        return isValidChoice ? choice : 0;
    }

    // region Dialogue Menus

    private static void printMainMenu() {
        println("Welcome to Blackjack!");
        println("1. Play Game");
        println("2. Simulate Hand");
        println("3. Update Settings");
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

        int settingChoice;
        do {
            settingChoice = readChoice(br, 1, 3);
        } while (settingChoice == 0);

        int choiceMax = 1;
        if (settingChoice == 1) {
            printSetting1Menu(originalSettings[0]);
            choiceMax = GameMode.values().length;
        } else if (settingChoice == 2) {
            printSetting2Menu(originalSettings[1]);
            choiceMax = DeckType.values().length;
        }

        if (0 < settingChoice && settingChoice <= 2) {
            int choice = 0;
            do {
                choice = readChoice(br, 1, choiceMax);
            } while (choice <= 0);
            newSettings[settingChoice - 1] = choice - 1;
        }
        return newSettings;
    }

}
