package it.tutta.colpa.del.caffe.rete;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port = 49152;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[Debug rete/Server]Server in ascolto sulla porta " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("[Debug rete/Server]Errore nell'avvio del server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}