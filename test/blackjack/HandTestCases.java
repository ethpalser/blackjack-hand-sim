package blackjack;

public class HandTestCases {

    public static Hand hardSeventeen() {
        return new Hand(Card.SEVEN(), Card.TEN());
    }

    public static Hand softSeventeen() {
        return new Hand(Card.ACE(), Card.SIX());
    }

    // Blackjack
    public static Hand aceJack() {
        return new Hand(Card.ACE(), Card.JACK());
    }

    public static Hand acePair() {
        return new Hand(Card.ACE(), Card.ACE());
    }

    public static Hand aceFiveSix() {
        return new Hand(Card.ACE(), Card.FIVE(), Card.SIX());
    }

}
