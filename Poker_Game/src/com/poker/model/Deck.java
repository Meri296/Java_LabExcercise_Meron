package com.poker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> cards = new ArrayList<>();

    public Deck() {

        for (Suit suit : Suit.values()) {

            for (Rank rank : Rank.values()) {

                cards.add(new Card(suit, rank));
            }
        }

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {

        if (cards.isEmpty()) {
            return null;
        }

        return cards.remove(0);
    }
}