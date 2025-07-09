package it.tutta.colpa.del.caffe.adventure.control.rete;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import it.tutta.colpa.del.caffe.adventure.other.GestioneDB;


public class server {

    private ServerSocket serverSocket;
    private final int port =12345;

    public server() {
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server in ascolto sulla porta " + port);

            socket = serverSocket.accept(); // accetta la connessione dal client
            System.out.println("Connessione accettata da " + socket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            while (true) {
                String richiesta = in.readLine();
                if (richiesta.equals("inizio")) {
                    // p = GestioneDB.creaMappa(); // inizializzo il gioco ritorna il grafo 
                    // c=lista di comandi 
                    //poi mando l'oggetto al client
                } else if (richiesta.equals("fine")) {
                    break;// chiudo la connessione
                } else {
                    out.println("Richiesta sconosciuta");
                }
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            } finally {
            try {
                if (socket != null) socket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
