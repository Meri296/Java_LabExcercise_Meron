package com.poker.game;

import com.poker.model.Card;
import com.poker.model.Deck;
import com.poker.model.Player;
import java.util.ArrayList;
import java.util.List;

public class PokerTable {

    private final List<Player> players =
            new ArrayList<>();

    private final List<Card> communityCards =
            new ArrayList<>();

    private final Deck deck;

    private int pot = 0;

    private GameStage stage =
            GameStage.PRE_FLOP;

    public PokerTable(Deck deck) {
        this.deck = deck;
    }

    public void addPlayer(Player player) {

        players.add(player);

        player.addCard(deck.draw());
        player.addCard(deck.draw());
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public int getPot() {
        return pot;
    }

    public GameStage getStage() {
        return stage;
    }

    public void addToPot(int amount) {
        pot += amount;
    }

    public void nextStage() {

        switch (stage) {

            case PRE_FLOP -> {

                communityCards.clear();

                communityCards.add(deck.draw());
                communityCards.add(deck.draw());
                communityCards.add(deck.draw());

                stage = GameStage.FLOP;
            }

            case FLOP -> {

                communityCards.add(deck.draw());

                stage = GameStage.TURN;
            }

            case TURN -> {

                communityCards.add(deck.draw());

                stage = GameStage.RIVER;
            }

            case RIVER -> {

                stage = GameStage.SHOWDOWN;
            }

            case SHOWDOWN -> {

                resetRound();
            }
        }
    }

    private void resetRound() {

        communityCards.clear();

        pot = 0;

        deck.shuffle();

        for (Player p : players) {

            p.clearHand();

            p.addCard(deck.draw());
            p.addCard(deck.draw());
        }

        stage = GameStage.PRE_FLOP;
    }
}