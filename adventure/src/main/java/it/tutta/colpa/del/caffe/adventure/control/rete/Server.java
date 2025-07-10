package it.tutta.colpa.del.caffe.adventure.control.rete;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import it.tutta.colpa.del.caffe.adventure.other.GestioneDB;


public class Server {

    private ServerSocket serverSocket;
    private final int port=49152;

    public Server() {
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server in ascolto sulla porta " + port);

            socket = serverSocket.accept(); // accetta la connessione dal client
            System.out.println("Connessione accettata da " + socket.getInetAddress());
            GestioneDB dataBase = new GestioneDB();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                String richiesta = in.readLine();
                if (richiesta.equals("comandi")) {
                    out.writeObject(dataBase.getComandi());
                }else if (richiesta.equals("mappa")){
                    out.writeObject(dataBase.creaMappa());
                } else if (richiesta.contains("descrizione-aggiornata-")) {
                    String[] tk = richiesta.split("-");
                    out.writeObject(dataBase.aggiornaDialoghi(Integer.parseInt(tk[2])));
                }else if (richiesta.contains("oggetto-")) {
                    String[] tk = richiesta.split("-");
                    //out.writeObject(dataBase.(Integer.parseInt(tk[1])));

                }else if (richiesta.equals("fine")) {
                    break;      // chiudo la connessione
                } else {
                    out.writeObject(new Exception("Non trovato"));
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
