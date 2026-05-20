package com.poker.controller;

import com.poker.engine.BettingEngine;
import com.poker.game.PokerTable;
import com.poker.model.Player;

public class GameController {

    private final BettingEngine engine;
    private final PokerTable table;

    public GameController(
            BettingEngine engine,
            PokerTable table
    ) {
        this.engine = engine;
        this.table = table;
    }

    public void call() {

        Player p = engine.currentPlayer();

        engine.call(p);
    }

    public void raise() {

        Player p = engine.currentPlayer();

        engine.raise(p, 50);
    }

    public void fold() {

        Player p = engine.currentPlayer();

        engine.fold(p);
    }

    public void nextStage() {

        engine.resetRound();

        table.nextStage();
    }
}