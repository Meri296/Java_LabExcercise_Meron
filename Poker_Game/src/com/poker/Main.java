package com.poker;

import com.poker.controller.GameController;
import com.poker.engine.BettingEngine;
import com.poker.game.PokerTable;
import com.poker.model.Deck;
import com.poker.model.Player;
import com.poker.view.PokerView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Deck deck = new Deck();

        PokerTable table = new PokerTable(deck);

        table.addPlayer(new Player("You", 1000));
        table.addPlayer(new Player("Bot 1", 1000));
        table.addPlayer(new Player("Bot 2", 1000));

        BettingEngine engine = new BettingEngine(table);

        GameController controller =
                new GameController(engine, table);

        PokerView view =
                new PokerView(table, controller);

        Scene scene = new Scene(view.getRoot(), 1000, 700);

        stage.setTitle("Poker Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}