package com.poker.view;

import com.poker.model.Card;
import com.poker.model.Rank;
import com.poker.model.Suit;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CardView {

    public static StackPane create(Card card) {

        Rectangle background =
                new Rectangle(90, 130);

        background.setArcWidth(15);
        background.setArcHeight(15);

        background.setFill(Color.WHITE);

        background.setStroke(Color.BLACK);

        String rank = convertRank(card.getRank());

        String suit = convertSuit(card.getSuit());

        Text text =
                new Text(rank + suit);

        text.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        24
                )
        );

        if (
                card.getSuit() == Suit.HEARTS
                        ||
                        card.getSuit() == Suit.DIAMONDS
        ) {
            text.setFill(Color.RED);
        } else {
            text.setFill(Color.BLACK);
        }

        StackPane pane =
                new StackPane();

        pane.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(
                background,
                text
        );

        pane.setStyle("""
                -fx-effect:
                dropshadow(
                    gaussian,
                    rgba(0,0,0,0.4),
                    10,
                    0,
                    2,
                    2
                );
                """);

        return pane;
    }

    private static String convertRank(Rank rank) {

        return switch (rank) {

            case ACE -> "A";
            case KING -> "K";
            case QUEEN -> "Q";
            case JACK -> "J";
            case TEN -> "10";
            case NINE -> "9";
            case EIGHT -> "8";
            case SEVEN -> "7";
            case SIX -> "6";
            case FIVE -> "5";
            case FOUR -> "4";
            case THREE -> "3";
            case TWO -> "2";
        };
    }

    private static String convertSuit(Suit suit) {

        return switch (suit) {

            case SPADES -> "♠";
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
        };
    }
}