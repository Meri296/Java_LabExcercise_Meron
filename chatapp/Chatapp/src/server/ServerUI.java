package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerUI extends Application {

    public static TextArea logArea = new TextArea();

    public static HashMap<String, ClientHandler> clients =
            new HashMap<>();

    TextField serverMsg = new TextField();

    @Override
    public void start(Stage stage) {

        logArea.setEditable(false);

        serverMsg.setPromptText("Server message");

        Button sendBtn = new Button("Broadcast");

        sendBtn.setOnAction(e -> {

            String msg = serverMsg.getText();

            broadcast("SERVER: " + msg);

            log("SERVER: " + msg);

            serverMsg.clear();
        });

        VBox root = new VBox(10,
                logArea,
                serverMsg,
                sendBtn);

        Scene scene = new Scene(root, 500, 500);

        stage.setScene(scene);

        stage.setTitle("Chat Server");

        stage.show();

        startServer();
    }

    void startServer() {

        new Thread(() -> {

            try {

                ServerSocket serverSocket =
                        new ServerSocket(6000);

                log("Server Started...");

                while (true) {

                    Socket socket =
                            serverSocket.accept();

                    ClientHandler handler =
                            new ClientHandler(socket);

                    clients.put(handler.username, handler);

                    new Thread(handler).start();

                    log(handler.username +
                            " connected");
                }

            } catch (Exception e) {

                log(e.getMessage());
            }

        }).start();
    }

    public static void broadcast(String msg) {

        try {

            for (ClientHandler c : clients.values()) {

                c.dos.writeUTF("TEXT");

                c.dos.writeUTF(msg);

                c.dos.flush();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void log(String msg) {

        Platform.runLater(() -> {

            logArea.appendText(msg + "\n");
        });
    }

    public static void main(String[] args) {

        launch(args);
    }
}