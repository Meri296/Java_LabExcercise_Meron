package app;

// File: NotepadApp.java
// Simple JavaFX Notepad Application

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NotepadApp extends Application {

    private TextArea textArea;

    @Override
    public void start(Stage primaryStage) {

        textArea = new TextArea();

        // Menu Bar
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");

        MenuItem newFile = new MenuItem("New");
        MenuItem openFile = new MenuItem("Open");
        MenuItem saveFile = new MenuItem("Save");
        MenuItem exitApp = new MenuItem("Exit");

        fileMenu.getItems().addAll(newFile, openFile, saveFile, new SeparatorMenuItem(), exitApp);

        // Edit Menu
        Menu editMenu = new Menu("Edit");

        MenuItem cutText = new MenuItem("Cut");
        MenuItem copyText = new MenuItem("Copy");
        MenuItem pasteText = new MenuItem("Paste");

        editMenu.getItems().addAll(cutText, copyText, pasteText);

        // Add Menus to MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu);

        // Actions

        // New File
        newFile.setOnAction(e -> textArea.clear());

        // Open File
        openFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                try {
                    String content = Files.readString(file.toPath());
                    textArea.setText(content);
                } catch (IOException ex) {
                    showError("Could not open file.");
                }
            }
        });

        // Save File
        saveFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                try {
                    Files.writeString(file.toPath(), textArea.getText());
                } catch (IOException ex) {
                    showError("Could not save file.");
                }
            }
        });

        // Exit
        exitApp.setOnAction(e -> primaryStage.close());

        // Edit Actions
        cutText.setOnAction(e -> textArea.cut());
        copyText.setOnAction(e -> textArea.copy());
        pasteText.setOnAction(e -> textArea.paste());

        // Layout
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(textArea);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("JavaFX Notepad");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}