package com.ethpalser.game;

import com.ethpalser.blackjack.Table;

public class Simulation implements Runnable {

    private Table table;

    private Simulation(Table table) {
        this.table = table;
    }

    @Override
    public void run() {

    }
}
