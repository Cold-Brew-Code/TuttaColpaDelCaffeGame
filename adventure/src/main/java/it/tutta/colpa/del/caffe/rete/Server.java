package it.tutta.colpa.del.caffe.rete;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La classe Server rappresenta il punto di ingresso principale per l'applicazione lato server.
 * Si occupa di mettersi in ascolto su una porta specifica e di accettare le connessioni
 * dai client. Per ogni client connesso, crea e avvia un nuovo thread {@link ClientHandler}
 * per gestire la comunicazione in modo isolato.
 *
 * @author giovanni
 */
public class Server {

    /**
     * La porta sulla quale il server si mette in ascolto per le connessioni in ingresso.
     */
    private final int port = 49152;

    /**
     * Avvia il server.
     * Crea un {@link ServerSocket} sulla porta specificata ed entra in un ciclo infinito
     * in attesa di connessioni dai client. Per ogni connessione accettata,
     * istanzia un nuovo {@link ClientHandler} e avvia il suo thread per gestire
     * la comunicazione con quel client specifico.
     * Gestisce le eccezioni di I/O che possono verificarsi durante l'inizializzazione.
     */
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

    /**
     * Il metodo main dell'applicazione server.
     * Crea un'istanza della classe Server e invoca il metodo {@link #start()}
     * per avviare il processo di ascolto.
     *
     * @param args gli argomenti passati dalla riga di comando (non utilizzati).
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}