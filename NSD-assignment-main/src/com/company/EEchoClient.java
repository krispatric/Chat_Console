package com.company;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class EEchoClient  extends Application{


    Socket client;
    PrintWriter writer;
    Scanner reader;
    TextField chatTextField;
    TextArea display;
    Label prompt;


    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        startClient();
        BorderPane rootPane;
        FlowPane flowPane;
        Scene scene;
        Button sendMessage = new Button("Send");
        Button delete = new Button("Delete");
        Button user = new Button("user");
        rootPane = new BorderPane();
        flowPane = new FlowPane();
        flowPane.setHgap(10);
        prompt = new Label("Enter Chat");

        chatTextField = new TextField();
        flowPane.getChildren().add(prompt);
        flowPane.getChildren().add(chatTextField);
        flowPane.getChildren().add(sendMessage);
        flowPane.getChildren().add(delete);
        flowPane.getChildren().add(user);
        delete.setOnAction(event->deleteText());
        sendMessage.setOnAction(event->sendMessage());
        user.setOnAction(event->user());
        rootPane.setTop(flowPane);
        flowPane.setAlignment(Pos.CENTER);
        display = new TextArea();
        display.setWrapText(true);
        display.setEditable(false);
        rootPane.setCenter(display);
        scene = new Scene(rootPane, 500, 300);
        stage.setScene(scene);
        stage.setTitle("SHU CHAT");
        stage.show();


    }

    public void deleteText()
    {
        display.setText("");
    }

    public void startClient()
    {
        int port = 12356;
        String hostName = "localhost";

        try
        {
            this.client = new Socket( hostName, port);
            this.writer = new PrintWriter(client.getOutputStream(), true);
            this.reader = new Scanner(client.getInputStream());


        }catch(IOException ie)
        {
            System.out.println(ie.getMessage());


        }
    }

    public void sendMessage()
    {
        String message;
        String username;
        if((message = chatTextField.getText())!=null)
        {
            writer.println(message);

            display.appendText(message +"\n");
            int n = reader.nextInt();
            reader.nextLine();
            chatTextField.setText("");

            for(int i=0; i<n; i++)
            {
                display.appendText(reader.nextLine() +"\n");
            }
        }
    }
public void user()
{
    String username;
    if((username = chatTextField.getText())!=null)
    {
        writer.println(username);

        display.appendText(username +"\n");
        int n = reader.nextInt();
        reader.nextLine();
        chatTextField.setText("");

        for(int i=0; i<n; i++)
        {
            display.appendText(reader.nextLine() +"\n");
        }
    }
}
    }

