package blackjack;


import org.junit.Assert;
import org.junit.Test;

public class HandTest {

    @Test
    public void evaluate_sevenAndTen_equalsSeventeen() {
        // evaluate is executed when the hand is created
        Hand test = HandTestCases.hardSeventeen();
        int value = test.getValue();
        Assert.assertEquals(17, value);
    }

    @Test
    public void evaluate_aceAndSix_equalsSeventeen() {
        // evaluate is executed when the hand is created
        Hand test = HandTestCases.softSeventeen();
        int value = test.getValue();
        Assert.assertEquals(17, value);
    }

    @Test
    public void evaluate_aceAndJack_equalsTwentyOne() {
        // evaluate is executed when the hand is created
        Hand test = HandTestCases.aceJack();
        int value = test.getValue();
        Assert.assertEquals(21, value);
    }

    @Test
    public void evaluate_aceAndAce_equalsTwelve() {
        // evaluate is executed when the hand is created
        Hand test = HandTestCases.acePair();
        int value = test.getValue();
        Assert.assertEquals(12, value);
    }

    @Test
    public void evaluate_aceFiveSix_equalsTwelve() {
        // evaluate is executed when the hand is created
        Hand test = HandTestCases.aceFiveSix();
        int value = test.getValue();
        Assert.assertEquals(12, value);
    }

    @Test
    public void add_aceToAceSix_equalsEighteen() {
        Hand test = HandTestCases.softSeventeen();
        // evaluate is executed when the new card is added
        test.addCard(Card.ACE());
        int value = test.getValue();
        Assert.assertEquals(18, value);
    }

    @Test
    public void add_sevenToAceFiveSix_equalsNineteen() {
        Hand test = new Hand(Card.ACE(), Card.FIVE(), Card.SIX());
        // evaluate is executed when the new card is added
        test.addCard(Card.SEVEN());
        int value = test.getValue();
        Assert.assertEquals(19, value);
    }

    @Test
    public void isBust_sevenAndTen_false() {
        Hand test = HandTestCases.hardSeventeen();
        Assert.assertFalse(test.isBust());
    }

    @Test
    public void isBust_aceAndSix_false() {
        Hand test = HandTestCases.softSeventeen();
        Assert.assertFalse(test.isBust());
    }

    @Test
    public void isBust_aceJack_false() {
        Hand test = HandTestCases.aceJack();
        Assert.assertFalse(test.isBust());
    }

    @Test
    public void isBust_sevenEightNine_true() {
        Hand test = new Hand(Card.SEVEN(), Card.EIGHT(), Card.NINE());
        Assert.assertTrue(test.isBust());
    }

    @Test
    public void isWin_aceFiveSixVsSevenTen_false() {
        Hand test = HandTestCases.aceFiveSix();
        Hand dealer = HandTestCases.hardSeventeen();
        Assert.assertFalse(test.isWin(dealer));
    }

    @Test
    public void isWin_aceSixVsSevenTen_false() {
        Hand test = HandTestCases.softSeventeen();
        Hand dealer = HandTestCases.hardSeventeen();
        Assert.assertFalse(test.isWin(dealer));
    }

    @Test
    public void isWin_aceJackVsAceJack_false() {
        Hand test = HandTestCases.aceJack();
        Hand dealer = HandTestCases.aceJack();
        Assert.assertFalse(test.isWin(dealer));
    }

    @Test
    public void isWin_aceJackVsSevenTen_true() {
        Hand test = HandTestCases.aceJack();
        Hand dealer = HandTestCases.hardSeventeen();
        Assert.assertTrue(test.isWin(dealer));
    }

    @Test
    public void isWin_aceFiveSixVsSevenEightNine_true() {
        Hand test = HandTestCases.aceFiveSix();
        Hand dealer = new Hand(Card.SEVEN(), Card.EIGHT(), Card.NINE());
        Assert.assertTrue(test.isWin(dealer));
    }

    @Test
    public void compare_aceFiveSixVsSevenTen_isLoss() {
        Hand test = HandTestCases.aceFiveSix();
        Hand dealer = HandTestCases.hardSeventeen();
        test.setResult(dealer);
        Assert.assertEquals(HandResult.LOSS, test.getResult());
    }

    @Test
    public void compare_aceSixVsSevenTen_isDraw() {
        Hand test = HandTestCases.softSeventeen();
        Hand dealer = HandTestCases.hardSeventeen();
        test.setResult(dealer);
        Assert.assertEquals(HandResult.DRAW, test.getResult());
    }

    @Test
    public void compare_aceJackVsSevenTen_isWin() {
        Hand test = HandTestCases.aceJack();
        Hand dealer = HandTestCases.hardSeventeen();
        test.setResult(dealer);
        Assert.assertEquals(HandResult.WIN, test.getResult());
    }

}
