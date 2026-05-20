package com.poker.game;

import com.poker.model.Card;
import com.poker.model.Player;
import com.poker.model.Rank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandEvaluator {

    public static HandRank evaluate(Player player) {

        Map<Rank, Integer> counts =
                new HashMap<>();

        for (Card c : player.getHand()) {

            counts.put(
                    c.getRank(),
                    counts.getOrDefault(
                            c.getRank(),
                            0
                    ) + 1
            );
        }

        int pairs = 0;

        boolean three = false;

        boolean four = false;

        for (int value : counts.values()) {

            if (value == 2) {
                pairs++;
            }

            if (value == 3) {
                three = true;
            }

            if (value == 4) {
                four = true;
            }
        }

        if (four) {
            return HandRank.FOUR_OF_A_KIND;
        }

        if (three && pairs == 1) {
            return HandRank.FULL_HOUSE;
        }

        if (three) {
            return HandRank.THREE_OF_A_KIND;
        }

        if (pairs == 2) {
            return HandRank.TWO_PAIR;
        }

        if (pairs == 1) {
            return HandRank.PAIR;
        }

        return HandRank.HIGH_CARD;
    }
}