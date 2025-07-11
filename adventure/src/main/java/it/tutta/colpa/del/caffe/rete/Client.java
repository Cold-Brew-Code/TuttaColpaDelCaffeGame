package it.tutta.colpa.del.caffe.rete;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;


import it.tutta.colpa.del.caffe.game.entity.GameMap;

public class Client {

    private final String serverAddress;
    private final int serverPort;

    public Client(String address, int port) {
        this.serverAddress = address;
        this.serverPort = port;
    }

    /**
     * Si connette al server, richiede la mappa e la restituisce.
     *
     * @return L'oggetto GameMap ricevuto dal server, o null in caso di errore.
     */
    public GameMap richiediMappa() {
        try (Socket socket = new Socket(serverAddress, serverPort)) {

            System.out.println("[Debug rete/Client] Connesso al server: " + serverAddress);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            String richiesta = "mappa";
            System.out.println("[Debug rete/Client] Invio richiesta: " + richiesta);
            out.println(richiesta);

            Object risposta = in.readObject();

            if (risposta instanceof GameMap) {
                System.out.println("[Debug rete/Client] Mappa ricevuta con successo!");
                return (GameMap) risposta;
            } else {
                System.err.println("[Debug rete/Client] Risposta inattesa dal server: " + risposta);
                return null;
            }

        } catch (Exception e) {
            System.err.println("[Debug rete/Client] Errore durante la comunicazione con il server: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 49152);
        GameMap mappaDelGioco = client.richiediMappa();

        if (mappaDelGioco != null) {
            System.out.println("[Debug rete/Client] L'oggetto mappa è stato caricato correttamente.");
            mappaDelGioco.debug();
        } else {
            System.out.println("[Debug rete/Client] Impossibile caricare la mappa.");
        }
    }
}