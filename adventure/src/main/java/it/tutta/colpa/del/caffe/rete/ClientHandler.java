package it.tutta.colpa.del.caffe.rete;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler extends Thread {

    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        System.out.println("Nuovo client connesso: " + clientSocket.getInetAddress());
    }

    @Override
    public void run() {
        try (
                this.clientSocket;
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            DataBaseManager dataBase = new DataBaseManager();
            String richiesta;
            while ((richiesta = in.readLine()) != null) {
                System.out.println("[Debug rete/ClientHandler]Richiesta da " + clientSocket.getInetAddress() + ": " + richiesta);

                if (richiesta.equals("comandi")) {
                    out.writeObject(dataBase.askForCommands());
                } else if (richiesta.equals("mappa")) {
                    out.writeObject(dataBase.askForGameMap());
                } else if (richiesta.startsWith("descrizione-aggiornata-")) {
                    String[] tk = richiesta.split("-");
                    out.writeObject(dataBase.askForNewRoomLook(Integer.parseInt(tk[2])));
                } else if (richiesta.startsWith("oggetto-")) {
                    String[] tk = richiesta.split("-");
                } else {
                    out.writeObject("[Debug rete/ClientHandler] said: Errore - Comando non riconosciuto");
                }
            }

        } catch (IOException e) {
            System.err.println("Errore di comunicazione con il client: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore database durante la gestione del client: " + e.getMessage());
        }

        System.out.println("Connessione con " + clientSocket.getInetAddress() + " terminata.");
    }
}