package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientUI extends Application {

    DataInputStream dis;
    DataOutputStream dos;

    VBox chatBox = new VBox(10);

    TextField usernameField = new TextField();
    TextField receiverField = new TextField();
    TextField messageField = new TextField();

    @Override
    public void start(Stage stage) {

        usernameField.setPromptText("Username");

        receiverField.setPromptText("Receiver");

        messageField.setPromptText("Message");

        Button connectBtn = new Button("Connect");

        Button sendBtn = new Button("Send");

        Button imageBtn = new Button("Image");

        connectBtn.setOnAction(e -> connect());

        sendBtn.setOnAction(e -> sendMessage());

        imageBtn.setOnAction(e -> sendImage());

        ScrollPane scrollPane =
                new ScrollPane(chatBox);

        scrollPane.setFitToWidth(true);

        VBox root = new VBox(
                10,
                usernameField,
                receiverField,
                connectBtn,
                scrollPane,
                messageField,
                new HBox(
                        10,
                        sendBtn,
                        imageBtn
                )
        );

        Scene scene =
                new Scene(root, 500, 600);

        stage.setScene(scene);

        stage.setTitle("Client");

        stage.show();
    }

    void connect() {

        try {

            Socket socket =
                    new Socket(
                            "localhost",
                            6000
                    );

            dis = new DataInputStream(
                    socket.getInputStream()
            );

            dos = new DataOutputStream(
                    socket.getOutputStream()
            );

            dos.writeUTF(
                    usernameField.getText()
            );

            dos.flush();

            loadHistory();

            receiveMessages();

            chatBox.getChildren().add(
                    new Label("Connected")
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    void sendMessage() {

        try {

            String msg =
                    messageField.getText();

            dos.writeUTF("TEXT");

            dos.writeUTF(
                    receiverField.getText()
            );

            dos.writeUTF(msg);

            dos.flush();

            chatBox.getChildren().add(

                    new Label(
                            "Me: " + msg
                    )
            );

            messageField.clear();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    void sendImage() {

        try {

            FileChooser chooser =
                    new FileChooser();

            File file =
                    chooser.showOpenDialog(null);

            if (file != null) {

                byte[] imageBytes =
                        Files.readAllBytes(
                                file.toPath()
                        );

                dos.writeUTF("IMAGE");

                dos.writeUTF(
                        receiverField.getText()
                );

                dos.writeInt(
                        imageBytes.length
                );

                dos.write(imageBytes);

                dos.flush();

                Image image =
                        new Image(
                                new ByteArrayInputStream(
                                        imageBytes
                                )
                        );

                ImageView view =
                        new ImageView(image);

                view.setFitWidth(200);

                view.setPreserveRatio(true);

                chatBox.getChildren().add(view);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    void receiveMessages() {

        new Thread(() -> {

            try {

                while (true) {

                    String type =
                            dis.readUTF();

                    // TEXT
                    if (type.equals("TEXT")) {

                        String msg =
                                dis.readUTF();

                        Platform.runLater(() -> {

                            chatBox.getChildren().add(

                                    new Label(msg)
                            );
                        });
                    }

                    // IMAGE
                    else if (type.equals("IMAGE")) {

                        int size =
                                dis.readInt();

                        byte[] imageBytes =
                                new byte[size];

                        dis.readFully(imageBytes);

                        Image image =
                                new Image(
                                        new ByteArrayInputStream(
                                                imageBytes
                                        )
                                );

                        ImageView view =
                                new ImageView(image);

                        view.setFitWidth(200);

                        view.setPreserveRatio(true);

                        Platform.runLater(() -> {

                            chatBox.getChildren().add(view);
                        });
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }).start();
    }

    void loadHistory() {

        try {

            Connection con =
                    DBConnection.connect();

            PreparedStatement ps =
                    con.prepareStatement(

                            "SELECT * FROM messages " +
                            "WHERE (sender=? AND receiver=?) " +
                            "OR (sender=? AND receiver=?) " +
                            "ORDER BY id ASC"
                    );

            ps.setString(
                    1,
                    usernameField.getText()
            );

            ps.setString(
                    2,
                    receiverField.getText()
            );

            ps.setString(
                    3,
                    receiverField.getText()
            );

            ps.setString(
                    4,
                    usernameField.getText()
            );

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                String type =
                        rs.getString("type");

                // TEXT HISTORY
                if (type.equals("TEXT")) {

                    String sender =
                            rs.getString("sender");

                    String msg =
                            rs.getString("message");

                    chatBox.getChildren().add(

                            new Label(
                                    sender + ": " + msg
                            )
                    );
                }

                // IMAGE HISTORY
                else if (type.equals("IMAGE")) {

                    byte[] imageBytes =
                            rs.getBytes("image");

                    if (imageBytes != null) {

                        Image image =
                                new Image(
                                        new ByteArrayInputStream(
                                                imageBytes
                                        )
                                );

                        ImageView view =
                                new ImageView(image);

                        view.setFitWidth(200);

                        view.setPreserveRatio(true);

                        chatBox.getChildren().add(view);
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}