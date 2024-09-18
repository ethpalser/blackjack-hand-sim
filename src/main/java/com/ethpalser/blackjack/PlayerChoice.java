package com.ethpalser.blackjack;

public enum PlayerChoice {
    HIT("Hit"),
    SPLIT("Split"),
    STAND("Stand"),
    SURRENDER("Surrender"),
    DOUBLE_DOWN("Double Down");

    private final String display;

    PlayerChoice(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
