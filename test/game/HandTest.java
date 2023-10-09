package game;


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
        test.addCard(Card.ACE);
        int value = test.getValue();
        Assert.assertEquals(18, value);
    }

    @Test
    public void add_sevenToAceFiveSix_equalsNineteen() {
        Hand test = new Hand(Card.ACE, Card.FIVE, Card.SIX);
        // evaluate is executed when the new card is added
        test.addCard(Card.SEVEN);
        int value = test.getValue();
        Assert.assertEquals(19, value);
    }


}
