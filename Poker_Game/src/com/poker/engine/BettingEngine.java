package com.poker.engine;

import com.poker.game.PokerTable;
import com.poker.model.Player;

public class BettingEngine {

    private final PokerTable table;

    private int turnIndex = 0;

    public BettingEngine(PokerTable table) {
        this.table = table;
    }

    public Player currentPlayer() {
        return table.getPlayers().get(turnIndex);
    }

    public void call(Player player) {

        player.removeChips(10);

        table.addToPot(10);

        nextTurn();
    }

    public void raise(Player player, int amount) {

        player.removeChips(amount);

        table.addToPot(amount);

        nextTurn();
    }

    public void fold(Player player) {

        nextTurn();
    }

    private void nextTurn() {

        turnIndex++;

        if (turnIndex >= table.getPlayers().size()) {
            turnIndex = 0;
        }
    }

    public void resetRound() {
        turnIndex = 0;
    }
}