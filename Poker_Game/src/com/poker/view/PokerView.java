package com.poker.view;

import com.poker.controller.GameController;
import com.poker.game.HandEvaluator;
import com.poker.game.HandRank;
import com.poker.game.PokerTable;
import com.poker.model.Card;
import com.poker.model.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class PokerView {

    private final VBox root;

    private final Label potLabel;

    private final Label stageLabel;

    private final Label winnerLabel;

    private final HBox cardsBox;

    private final HBox playerCards;

    public PokerView(
            PokerTable table,
            GameController controller
    ) {

        stageLabel = new Label();

        stageLabel.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                """);

        potLabel = new Label();

        potLabel.setStyle("""
                -fx-font-size: 20px;
                -fx-text-fill: gold;
                """);

        winnerLabel = new Label();

        winnerLabel.setStyle("""
                -fx-font-size: 22px;
                -fx-text-fill: cyan;
                -fx-font-weight: bold;
                """);

        cardsBox = new HBox(20);

        cardsBox.setAlignment(Pos.CENTER);

        playerCards = new HBox(20);

        playerCards.setAlignment(Pos.CENTER);

        Button call =
                createButton("Call");

        Button raise =
                createButton("Raise");

        Button fold =
                createButton("Fold");

        Button next =
                createButton("Next Stage");

        call.setOnAction(e -> {

            controller.call();

            update(table);
        });

        raise.setOnAction(e -> {

            controller.raise();

            update(table);
        });

        fold.setOnAction(e -> {

            controller.fold();

            update(table);
        });

        next.setOnAction(e -> {

            controller.nextStage();

            update(table);
        });

        HBox buttons =
                new HBox(
                        15,
                        call,
                        raise,
                        fold,
                        next
                );

        buttons.setAlignment(Pos.CENTER);

        root =
                new VBox(
                        30,
                        stageLabel,
                        potLabel,
                        winnerLabel,
                        cardsBox,
                        playerCards,
                        buttons
                );

        root.setAlignment(Pos.CENTER);

        root.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.web("#0B6623"),
                                CornerRadii.EMPTY,
                                null
                        )
                )
        );

        update(table);
    }

    public VBox getRoot() {
        return root;
    }

    private Button createButton(String text) {

        Button button =
                new Button(text);

        button.setStyle("""
                -fx-font-size: 16px;
                -fx-background-color: black;
                -fx-text-fill: white;
                -fx-padding: 10 20 10 20;
                -fx-background-radius: 10;
                """);

        return button;
    }

    public void update(PokerTable table) {

        stageLabel.setText(
                "Stage: " + table.getStage()
        );

        potLabel.setText(
                "Pot: $" + table.getPot()
        );

        cardsBox.getChildren().clear();

        List<Card> cards =
                table.getCommunityCards();

        for (Card c : cards) {

            cardsBox
                    .getChildren()
                    .add(
                            CardView.create(c)
                    );
        }

        playerCards.getChildren().clear();

        Player player =
                table.getPlayers().get(0);

        for (Card c : player.getHand()) {

            playerCards
                    .getChildren()
                    .add(
                            CardView.create(c)
                    );
        }

        if (
                table.getStage().toString()
                        .equals("SHOWDOWN")
        ) {

            Player winner =
                    findWinner(table);

            HandRank rank =
                    HandEvaluator.evaluate(
                            winner
                    );

            winnerLabel.setText(
                    "Winner: "
                            + winner.getName()
                            + " ("
                            + rank
                            + ")"
            );
        } else {

            winnerLabel.setText("");
        }
    }

    private Player findWinner(
            PokerTable table
    ) {

        Player best =
                table.getPlayers().get(0);

        HandRank bestRank =
                HandEvaluator.evaluate(best);

        for (Player p : table.getPlayers()) {

            HandRank current =
                    HandEvaluator.evaluate(p);

            if (
                    current.ordinal()
                            >
                            bestRank.ordinal()
            ) {

                best = p;

                bestRank = current;
            }
        }

        return best;
    }
}