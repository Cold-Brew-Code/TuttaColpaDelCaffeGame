package it.tutta.colpa.del.caffe.game.control;

import it.tutta.colpa.del.caffe.game.boundary.GamePage;
import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameMap;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.Direzione;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;

/**
 * Classe principale che gestisce la logica di gioco.
 * Comunica con il server per inizializzare lo stato del gioco,
 * consente il movimento tra stanze e l'uso dell'ascensore.
 *
 * Implementa Serializable per supportare il salvataggio dello stato.
 *
 * @author giovav
 * @since 11/07/25
 */
public class Engine implements Serializable {

    /**
     * Riferimento alla GUI, utile per eventuali interazioni con l'interfaccia utente.
     */
    private GamePage frame;

    /**
     * Descrizione dello stato attuale della partita, contenente mappa e comandi.
     */
    private GameDescription description;

    /**
     * Costruttore predefinito.
     * Tenta di inizializzare una nuova partita comunicando con il server.
     * In caso di errore di comunicazione, dovrebbe gestire l’eccezione mostrando
     * un dialogo informativo all’utente (da implementare).
     */
    @SuppressWarnings("unused")
    public Engine() {
        try {
            this.description = initGame();
        } catch (ServerCommunicationException e) {
            // apre JDialogPane
        }
    }

    /**
     * Costruttore per l'inizializzazione da file di salvataggio.
     * Deve essere implementato.
     *
     * @param filePath percorso del file di salvataggio
     */
    @SuppressWarnings("unused")
    public Engine(String filePath) {

    }

    /**
     * Inizializza una nuova partita contattando il server locale.
     * Recupera la mappa e l'elenco dei comandi, verificandone la correttezza.
     *
     * @return una GameDescription contenente la mappa e i comandi
     * @throws ServerCommunicationException se la comunicazione fallisce o i dati sono incompleti
     */
    @SuppressWarnings("unchecked")
    private GameDescription initGame() throws ServerCommunicationException {
        GameMap gm = null;
        List<Command> commands = null;
        try (Socket socket = new Socket("localhost", 49152)) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.println("mappa");
            Object answer = in.readObject();

            boolean reCheck = true;
            int timesChecked = 0;

            // ask for GameMap
            while ((reCheck) && (timesChecked < 5)) {
                if (answer instanceof GameMap) {
                    gm = (GameMap) answer;
                    reCheck = false;
                } else if (answer instanceof String) {
                    System.err.println("[Server] Si è verificato un errore: " + answer);
                } else {
                    System.err.println("Si è verificato un errore.");
                }
                timesChecked++;
            }

            reCheck = true;
            timesChecked = 0;

            out.println("comandi");
            answer = in.readObject();

            // asks for Commands
            while ((gm != null) && (reCheck) && (timesChecked < 5)) {
                if (answer instanceof List<?> answerList) {
                    boolean areAllCommands = answerList.stream()
                            .allMatch(e -> e instanceof Command);
                    if (areAllCommands) {
                        commands = (List<Command>) answerList;
                        reCheck = false;
                    } else {
                        System.err.println("L'oggetto ricevuto non è una lista di comandi.");
                    }
                } else if (answer instanceof String) {
                    System.err.println("[Server] Si è verificato un errore: " + answer);
                } else {
                    System.err.println("Si è verificato un errore.");
                }
                timesChecked++;
            }

            if (gm == null || commands == null) {
                throw new ServerCommunicationException("Comunicazione client-server fallita");
            } else {
                System.out.println("[debug] tutto okay amo, funziono!!!");
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to connect to the server.");
        }

        GameDescription gd = new GameDescription(gm, commands);
        // observer attachment
        return gd;
    }

    /**
     * Inizializza una partita a partire da un file di salvataggio.
     * Metodo da implementare.
     *
     * @param savePath percorso del file di salvataggio
     */
    @SuppressWarnings("unused")
    private void initGame(String savePath) {
        // returns GameDescription
    }

    /**
     * Tenta di muoversi nella direzione specificata a partire dalla posizione corrente.
     *
     * @param direction la direzione in cui muoversi
     * @return true se il movimento è valido, false in caso contrario
     */
    @SuppressWarnings("unused")
    private boolean moveFromHere(Direzione direction) {
        try {
            description.getGameMap().getStanzaArrivo(direction);
        } catch (GameMapException e) {
            return false;
        }
        return true;
    }

    /**
     * Tenta di utilizzare l’ascensore per raggiungere un piano specifico.
     *
     * @param floor il piano da raggiungere
     * @return true se l’operazione è riuscita, false se non è possibile
     */
    @SuppressWarnings("unused")
    private boolean moveWithElevator(int floor) {
        try {
            description.getGameMap().prendiAscensore(floor);
        } catch (GameMapException e) {
            return false;
        }
        return true;
    }
}