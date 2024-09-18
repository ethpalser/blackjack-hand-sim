package com.ethpalser.blackjack;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class HandTest {

    @Test
    void evaluate_sevenAndTen_equalsSeventeen() {
        // evaluate is executed when the hand is created
        Hand test = HandTestCases.hardSeventeen();
        int value = test.getValue();
        assertEquals(17, value);
    }

    @Test
    void evaluate_aceAndSix_equalsSeventeen() {
        // evaluate is executed when the hand is created
        Hand test = HandTestCases.softSeventeen();
        int value = test.getValue();
        assertEquals(17, value);
    }

    @Test
    void evaluate_aceAndJack_equalsTwentyOne() {
        // evaluate is executed when the hand is created
        Hand test = HandTestCases.aceJack();
        int value = test.getValue();
        assertEquals(21, value);
    }

    @Test
    void evaluate_aceAndAce_equalsTwelve() {
        // evaluate is executed when the hand is created
        Hand test = HandTestCases.acePair();
        int value = test.getValue();
        assertEquals(12, value);
    }

    @Test
    void evaluate_aceFiveSix_equalsTwelve() {
        // evaluate is executed when the hand is created
        Hand test = HandTestCases.aceFiveSix();
        int value = test.getValue();
        assertEquals(12, value);
    }

    @Test
    void add_aceToAceSix_equalsEighteen() {
        Hand test = HandTestCases.softSeventeen();
        Card card = new Card(CardType.ACE, CardSuit.SPADES);
        // evaluate is executed when the new card is added
        test.addCard(card);
        int value = test.getValue();
        assertEquals(18, value);
    }

    @Test
    void add_sevenToAceFiveSix_equalsNineteen() {
        Hand test = HandTestCases.aceFiveSix();
        // evaluate is executed when the new card is added
        test.addCard(new Card(CardType.SEVEN));
        int value = test.getValue();
        assertEquals(19, value);
    }

    @Test
    void isBust_sevenAndTen_false() {
        Hand test = HandTestCases.hardSeventeen();
        assertFalse(test.isBust());
    }

    @Test
    void isBust_aceAndSix_false() {
        Hand test = HandTestCases.softSeventeen();
        assertFalse(test.isBust());
    }

    @Test
    void isBust_aceJack_false() {
        Hand test = HandTestCases.aceJack();
        assertFalse(test.isBust());
    }

    @Test
    void isBust_sevenEightNine_true() {
        Hand test = HandTestCases.sevenEightNine();
        assertTrue(test.isBust());
    }

    @Test
    void isWin_aceFiveSixVsSevenTen_false() {
        Hand test = HandTestCases.aceFiveSix();
        Hand dealer = HandTestCases.hardSeventeen();
        assertFalse(test.isWin(dealer));
    }

    @Test
    void isWin_aceSixVsSevenTen_false() {
        Hand test = HandTestCases.softSeventeen();
        Hand dealer = HandTestCases.hardSeventeen();
        assertFalse(test.isWin(dealer));
    }

    @Test
    void isWin_aceJackVsAceJack_false() {
        Hand test = HandTestCases.aceJack();
        Hand dealer = HandTestCases.aceJack();
        assertFalse(test.isWin(dealer));
    }

    @Test
    void isWin_aceJackVsSevenTen_true() {
        Hand test = HandTestCases.aceJack();
        Hand dealer = HandTestCases.hardSeventeen();
        assertTrue(test.isWin(dealer));
    }

    @Test
    void isWin_aceFiveSixVsSevenEightNine_true() {
        Hand test = HandTestCases.aceFiveSix();
        Hand dealer = HandTestCases.sevenEightNine();
        assertTrue(test.isWin(dealer));
    }

    @Test
    void compare_aceFiveSixVsSevenTen_isLoss() {
        Hand test = HandTestCases.aceFiveSix();
        Hand dealer = HandTestCases.hardSeventeen();
        HandResult result = test.result(dealer);
        assertEquals(HandResult.LOSS, result);
    }

    @Test
    void compare_aceSixVsSevenTen_isDraw() {
        Hand test = HandTestCases.softSeventeen();
        Hand dealer = HandTestCases.hardSeventeen();
        HandResult result = test.result(dealer);
        assertEquals(HandResult.DRAW, result);
    }

    @Test
    void compare_aceJackVsSevenTen_isWin() {
        Hand test = HandTestCases.aceJack();
        Hand dealer = HandTestCases.hardSeventeen();
        HandResult result = test.result(dealer);
        assertEquals(HandResult.WIN, result);
    }

}
