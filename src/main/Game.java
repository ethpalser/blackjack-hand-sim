package main;

import blackjack.DeckType;
import blackjack.GameMode;
import blackjack.Table;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {

    private GameMode gameMode; // Not needed?
    private Table table;

    public Game(int numPlayers, int numDecks) {
        this.table = new Table(numPlayers, DeckType.SEGMENTED, numDecks);
    }

    public Game(int numPlayers, int numDecks, GameMode gameMode) {
        this.gameMode = gameMode;
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
                    printSettingMenu();

                    int settingChoice = 0;
                    do {
                        settingChoice = readChoice(br, 1, 3);
                    } while (settingChoice == 0);
                    if (settingChoice == -1)
                        break;

                    if (settingChoice == 1) {
                        printSetting1Menu(gameMode);

                        int modeChoice = 0;
                        do {
                            modeChoice = readChoice(br, 1, 2);
                        } while (modeChoice == 0);
                        if (modeChoice == -1)
                            break;
                        gameMode = modeChoice == 1 ? GameMode.ALL_PLAYERS_VISIBLE : GameMode.NO_PLAYERS_VISIBLE;
                    } else if (settingChoice == 2) {
                        printSetting2Menu(deckType);

                        int typeChoice = 0;
                        do {
                            typeChoice = readChoice(br, 1, 2);
                        } while (typeChoice == 0);
                        if (typeChoice == -1)
                            break;
                        deckType = typeChoice == 1 ? DeckType.SEGMENTED : DeckType.RANDOM;
                    }
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

    private static void printSetting1Menu(GameMode currentSetting) {
        println("Choose your game mode:");
        println("1. All player hands are visible" + (GameMode.ALL_PLAYERS_VISIBLE.equals(currentSetting) ?
                " (Current)" : ""));
        println("2. Nod player hands are visible" + (GameMode.NO_PLAYERS_VISIBLE.equals(currentSetting) ?
                " (Current)" : ""));
    }

    private static void printSetting2Menu(DeckType currentSetting) {
        println("Choose your deck type:");
        println("1. Multiple decks randomized in deck segments" + (DeckType.SEGMENTED.equals(currentSetting) ?
                " (Current)" : ""));
        println("2. Multiple decks completely randomized" + (DeckType.RANDOM.equals(currentSetting) ?
                " (Current)" : ""));
    }

    private static void printUnavailable() {
        println("This feature is not available.");
    }

    private static void printInvalid() {
        println("Invalid choice.");
    }

    // endregion

}
