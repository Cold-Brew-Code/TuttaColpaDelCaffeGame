package it.tutta.colpa.del.caffe.rete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Gestisce la comunicazione con un singolo client su un thread dedicato.
 * Questa classe è responsabile di leggere le richieste inviate dal client,
 * elaborarle interrogando il database tramite un'istanza di {@link DataBaseManager},
 * e inviare una risposta serializzata. Gestisce anche le disconnessioni
 * e gli errori di comunicazione o di parsing in modo robusto.
 *
 * @author giovav
 */
public class ClientHandler extends Thread {

    /**
     * Il socket che rappresenta la connessione con il client.
     */
    private final Socket clientSocket;

    /**
     * Costruisce un nuovo gestore per un client specifico.
     *
     * @param socket il {@link Socket} del client appena connesso.
     */
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        System.out.println("[Debug rete/ClientHandler] Nuovo client connesso: " + clientSocket.getInetAddress());
    }

    /**
     * Il corpo principale del thread, che gestisce l'intero ciclo di vita
     * della comunicazione con il client.
     * <p>
     * Inizializza i flussi di input/output e una connessione al database.
     * Entra in un ciclo di ascolto infinito, leggendo le richieste testuali del client.
     * A seconda del comando ricevuto, interroga il database e invia l'oggetto
     * risultante al client. Il ciclo termina quando il client invia il comando "end".
     * <p>
     * La gestione delle eccezioni è implementata per catturare errori di I/O,
     * problemi con il database (SQLException) e disconnessioni anomale (NullPointerException),
     * garantendo che il server non si blocchi a causa di un singolo client difettoso.
     */
    @Override
    public void run() {
        try (
                this.clientSocket;
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            DataBaseManager dataBase = new DataBaseManager();
            String richiesta;
            while (true) {
                richiesta = in.readLine();
                System.out.println("[Debug rete/ClientHandler] Richiesta da " + clientSocket.getInetAddress() + ": " + richiesta);
                if (richiesta.equals("comandi")) {
                    out.writeObject(dataBase.askForCommands());
                } else if (richiesta.equals("mappa")) {
                    out.writeObject(dataBase.askForGameMap());
                } else if (richiesta.startsWith("descrizione-aggiornata-")) {
                    try {
                        String[] tk = richiesta.split("-");
                        out.writeObject(dataBase.askForNewRoomLook(Integer.parseInt(tk[2])));
                    } catch (Exception e) {
                        out.writeObject("Oops, qualcosa è andato storto!");
                    }
                }else if (richiesta.startsWith("oggetti")) {
                    out.writeObject(dataBase.askForItems());
                }else if (richiesta.startsWith("NPCs")) {
                    out.writeObject(dataBase.askForNonPlayerCharacters());
                } else if (richiesta.startsWith("oggetto-")) {
                    try {
                        String[] tk = richiesta.split("-");
                        out.writeObject(dataBase.askForItem(Integer.parseInt(tk[1])));
                    } catch (Exception e) {
                        System.err.println("[server] " +e.getMessage()+" "+e.getStackTrace());
                        out.writeObject("Oops, qualcosa è andato storto!");
                    }
                }else if (richiesta.equals("end")) {
                    break;
                } else {
                    out.writeObject("[Debug rete/ClientHandler] said: Errore - Comando non riconosciuto");
                }
            }
            dataBase.closeConnection();

        } catch (IOException e) {
            System.err.println("Errore di comunicazione con il client: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore database durante la gestione del client: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("Il client ha terminato la connessione in modo anomalo: "+e.getMessage());
        }

        System.out.println("Connessione con " + clientSocket.getInetAddress() + " terminata.");
    }
}