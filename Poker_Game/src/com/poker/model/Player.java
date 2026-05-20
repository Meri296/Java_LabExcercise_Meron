package com.poker.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String name;

    private int chips;

    private final List<Card> hand =
            new ArrayList<>();

    public Player(String name, int chips) {

        this.name = name;
        this.chips = chips;
    }

    public String getName() {
        return name;
    }

    public int getChips() {
        return chips;
    }

    public void removeChips(int amount) {
        chips -= amount;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void clearHand() {
        hand.clear();
    }

    public void addCard(Card card) {
        hand.add(card);
    }
}