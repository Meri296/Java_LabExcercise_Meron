package server;

import client.DBConnection;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ClientHandler implements Runnable {

    Socket socket;

    DataInputStream dis;

    public DataOutputStream dos;

    public String username;

    public ClientHandler(Socket socket) {

        try {

            this.socket = socket;

            dis = new DataInputStream(
                    socket.getInputStream());

            dos = new DataOutputStream(
                    socket.getOutputStream());

            username = dis.readUTF();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {

            while (true) {

                String type = dis.readUTF();

                // TEXT
                if (type.equals("TEXT")) {

                    String receiver =
                            dis.readUTF();

                    String msg =
                            dis.readUTF();

                    saveMessage(
                            username,
                            receiver,
                            "TEXT",
                            msg,
                            null
                    );

                    sendText(
                            receiver,
                            username + ": " + msg
                    );
                }

                // IMAGE
                else if (type.equals("IMAGE")) {

                    String receiver =
                            dis.readUTF();

                    int size =
                            dis.readInt();

                    byte[] imageBytes =
                            new byte[size];

                    dis.readFully(imageBytes);

                    saveMessage(
                            username,
                            receiver,
                            "IMAGE",
                            null,
                            imageBytes
                    );

                    sendImage(
                            receiver,
                            imageBytes
                    );
                }
            }

        } catch (Exception e) {

            ServerUI.log(username +
                    " disconnected");
        }
    }

    void sendText(String receiver,
                  String msg) {

        try {

            ClientHandler target =
                    ServerUI.clients.get(receiver);

            if (target != null) {

                target.dos.writeUTF("TEXT");

                target.dos.writeUTF(msg);

                target.dos.flush();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    void sendImage(String receiver,
                   byte[] imageBytes) {

        try {

            ClientHandler target =
                    ServerUI.clients.get(receiver);

            if (target != null) {

                target.dos.writeUTF("IMAGE");

                target.dos.writeInt(
                        imageBytes.length
                );

                target.dos.write(imageBytes);

                target.dos.flush();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    void saveMessage(String sender,
                     String receiver,
                     String type,
                     String msg,
                     byte[] image) {

        try {

            Connection con =
                    DBConnection.connect();

            PreparedStatement ps =
                    con.prepareStatement(

                            "INSERT INTO messages(sender, receiver, type, message, image) VALUES (?,?,?,?,?)"
                    );

            ps.setString(1, sender);

            ps.setString(2, receiver);

            ps.setString(3, type);

            ps.setString(4, msg);

            if (image != null) {

                ps.setBytes(5, image);

            } else {

                ps.setNull(
                        5,
                        java.sql.Types.BLOB
                );
            }

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}