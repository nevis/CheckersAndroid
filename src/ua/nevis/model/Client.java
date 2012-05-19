package ua.nevis.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	private PrintWriter output;
    private BufferedReader input;
    private boolean connected = false;
    private String name = null;
    
    public Client(String serverName, String name) throws IOException {
        InetAddress ia = InetAddress.getByName(serverName);
        this.name = name;
        Socket socket = new Socket(ia, 5869);
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    public void sendMessage(String message) {
        output.println(message);
    }
    public BufferedReader getInput() {
        return input;
    }
    public boolean isConnected() {
        return connected;
    }
    public void setConnected(boolean conn) {
        connected = conn;
    }
    public String getName() {
        return name;
    }
}
