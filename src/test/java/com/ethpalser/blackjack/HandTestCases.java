package com.ethpalser.blackjack;

public class HandTestCases {

    public static Hand hardSeventeen() {
        return new Hand(new Card(CardType.SEVEN), new Card(CardType.TEN));
    }

    public static Hand softSeventeen() {
        return new Hand(new Card(CardType.ACE), new Card(CardType.SIX));
    }

    // Blackjack
    public static Hand aceJack() {
        return new Hand(new Card(CardType.ACE), new Card(CardType.JACK));
    }

    public static Hand acePair() {
        return new Hand(new Card(CardType.ACE), new Card(CardType.ACE));
    }

    public static Hand aceFiveSix() {
        return new Hand(new Card(CardType.ACE), new Card(CardType.FIVE), new Card(CardType.SIX));
    }

    public static Hand sevenEightNine() {
        return new Hand(new Card(CardType.SEVEN), new Card(CardType.EIGHT), new Card(CardType.NINE));
    }

}
