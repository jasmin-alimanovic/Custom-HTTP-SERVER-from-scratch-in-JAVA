package com.jasmin.httpserver;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter  out;
    private BufferedReader  in;

    public void startConnection(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        System.out.println("Connected to " + ip + ":" + port);
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(System.in));
    }
    public void sendMessage() throws IOException {
        String line = "";

        // reads message from client until "Over" is sent
        //while (!line.equals("Over"))
        //{
            try
            {
                line = in.readLine();
                out.println(line);
                out.flush();
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        //}
        System.out.println("Closing connection");

        //System.out.println("response:");
    }
    public void stopConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        Client klijent = new Client();
        klijent.startConnection("127.0.0.1", 6666);
        klijent.sendMessage();
        klijent.stopConnection();
    }

}
