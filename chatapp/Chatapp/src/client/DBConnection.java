package client;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection connect() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3307/chat_app",
                    "root",
                    ""
            );

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}