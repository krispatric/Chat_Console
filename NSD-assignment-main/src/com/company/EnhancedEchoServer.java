package com.company;

import java.net.*;
import java.io.*;
import java.util.*;  // required for IOs and List
import java.util.Scanner;
import java.util.NoSuchElementException;

class EnhancedEchoServer
{
    static class ClientHandler extends Thread {
        // shared message board
        private static List<String> board = new ArrayList<String>();
        private static long total = 0;
        private Socket client;
        private PrintWriter out;
        private BufferedReader in;
        private String clientID;

        public ClientHandler(Socket socket, String id) throws IOException {
            this.client = socket;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.clientID = id;
        }

        public void run() {
            try {


                int count = 0;
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // logging request to server console

                    System.out.print("Request " + ++count + "/" + ++total + " from client " + clientID + ": ");
                    System.out.println(inputLine);

                    // parse request
                    Scanner sc = new Scanner(inputLine);
                    String command = "";
                    String argument = "";
                    String user = "";
                    try {
                        command = sc.next();
                        argument = sc.skip(" ").nextLine();
                        user = sc.skip(" ").nextLine();
                    } catch (NoSuchElementException e) {
                    }
                    if (command.equals("username")) {
                        // synchronized access to the shared message board
                        synchronized (ClientHandler.class) {
                            board.add(user);
                            user=clientID;
                        }
                        // response acknowledging the post request
                        out.println(0);
                        continue;
                    }
                    // post request
                    if (command.equals("post")) {
                        // synchronized access to the shared message board
                        synchronized (ClientHandler.class) {
                            board.add(argument);

                        }
                        // response acknowledging the post request
                        out.println(0);
                        continue;
                    }

                    // read request
                    if (command.equals("read")) {
                        // synchronized access to the shared message board
                        synchronized (ClientHandler.class) {
                            // response: number of messages, followed by messages
                            out.println(board.size());
                            for (String msg : board)
                                out.println(msg);
                        }
                        continue;
                    }

                    // quit request
                    if (command.equals("quit")) {
                        in.close();
                        out.close();
                        return;
                    }

                    // illegal request; response: 1, followed by error message
                    out.println(1);
                    out.println("ILLEGAL REQUEST");
                }
            } catch (IOException e) {
                System.out.println("Exception while connected");
                System.out.println(e.getMessage());
            }

        }
    }
    public static void main(String[] args) {


        int portNumber = 12356;
        System.out.println("connection on port " +
                portNumber);

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);

        ) {
            String client = "";
            while (true) {
                Socket clientSocket = serverSocket.accept();

                new Thread(new ClientHandler(clientSocket, client)).start();

            }
        } catch (IOException e) {
            System.out.println("Exception listening for connection on port " +
                    portNumber);
            System.out.println(e.getMessage());
        }
    }
            }

