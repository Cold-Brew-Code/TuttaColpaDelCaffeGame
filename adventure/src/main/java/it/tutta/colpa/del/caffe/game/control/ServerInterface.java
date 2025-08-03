package it.tutta.colpa.del.caffe.game.control;

import java.util.concurrent.Callable;

import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GameMap;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.NPC;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.RequestType;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;


/**
 * Gestisce la comunicazione di basso livello con il server di gioco.
 * Questa classe si occupa di stabilire una connessione, inviare richieste
 * e ricevere risposte, implementando una logica di tentativi multipli
 * per gestire errori di comunicazione temporanei.
 *
 * @author giovav
 * @since 17/07/25
 */
public class ServerInterface {
    private Socket connection;
    private PrintWriter out;
    private ObjectInputStream in;

    /**
     * Inizializza l'interfaccia con il server stabilendo la connessione.
     *
     * @param IP    L'indirizzo IP del server.
     * @param porta La porta su cui il server è in ascolto.
     */
    public ServerInterface(String IP, int porta) throws ServerCommunicationException {
        try {
            connection = new Socket(IP, porta);
            out = new PrintWriter(connection.getOutputStream(), true);
            in = new ObjectInputStream(connection.getInputStream());
        } catch (Exception e) {
            this.connection = null;
            this.out = null;
            this.in = null;
            throw new ServerCommunicationException("Il server è spento/non risponde alla richiesta");
        }
    }

    /**
     * Invia una richiesta senza parametri al server, gestendo una logica di tentativi.
     *
     * @param rt  Il tipo di richiesta da inviare, definito in {@link RequestType}.
     * @param <T> Il tipo di dato atteso come risposta dal server.
     * @return L'oggetto ricevuto dal server, castato al tipo T.
     * @throws ServerCommunicationException se la comunicazione fallisce definitivamente.
     */
    @SuppressWarnings("unchecked")
    public <T> T requestToServer(RequestType rt) throws ServerCommunicationException {
        boolean reCheck = true;
        int timesChecked = 0;
        T answer = null;
        while (reCheck && timesChecked < 4) {
            try {
                reCheck = false;
                answer = (T) getRequestAction(rt).call();
            } catch (ServerCommunicationException e) {
                throw e;
            } catch (Exception e) {
                reCheck = true;
            }
            timesChecked++;
        }
        if (reCheck) {
            try {
                answer = (T) getRequestAction(rt).call();
            } catch (ServerException e) {
                throw new ServerCommunicationException("Il server non ha elaborato correttamente la richiesta: " + e.getMessage() + ".");
            } catch (IOException e) {
                throw new ServerCommunicationException("Il server ha chiuso la connessione.");
            } catch (ClassNotFoundException e) {
                throw new ServerCommunicationException("Il server ha inviato un oggetto sconosciuto.");
            } catch (Exception e) {
                throw new ServerCommunicationException("Errore generico: " + e.getMessage());
            }
        }

        return answer;
    }

    /**
     * Invia una richiesta con un parametro ID al server, gestendo una logica di tentativi.
     *
     * @param rt  Il tipo di richiesta da inviare, definito in {@link RequestType}.
     * @param id  L'identificatore numerico da inviare con la richiesta.
     * @param <T> Il tipo di dato atteso come risposta dal server.
     * @return L'oggetto ricevuto dal server, castato al tipo T.
     * @throws ServerCommunicationException se la comunicazione fallisce definitivamente.
     */
    @SuppressWarnings("unchecked")
    public <T> T requestToServer(RequestType rt, int id) throws ServerCommunicationException {
        boolean reCheck = true;
        int timesChecked = 0;
        T answer = null;
        while (reCheck && timesChecked < 4) {
            try {
                reCheck = false;
                answer = (T) getRequestAction(rt, id).call();
            } catch (ServerCommunicationException e) {
                throw e;
            } catch (Exception e) {
                reCheck = true;
            }
            timesChecked++;
        }
        if (reCheck) {
            try {
                answer = (T) getRequestAction(rt, id).call();
            } catch (ServerException e) {
                throw new ServerCommunicationException("Il server non ha elaborato correttamente la richiesta: " + e.getMessage() + ".");
            } catch (IOException e) {
                throw new ServerCommunicationException("Il server ha chiuso la connessione.");
            } catch (ClassNotFoundException e) {
                throw new ServerCommunicationException("Il server ha inviato un oggetto sconosciuto.");
            } catch (Exception e) {
                throw new ServerCommunicationException("Errore generico: " + e.getMessage());
            }
        }

        return answer;
    }


    /**
     * Metodo dispatcher che restituisce un'azione {@link Callable} basata sul {@link RequestType}.
     * Questa versione gestisce le richieste senza parametri aggiuntivi.
     *
     * @param rt Il tipo di richiesta.
     * @return Un Callable che, quando eseguito, effettua la chiamata al server.
     * @throws ServerCommunicationException se il tipo di richiesta non è valido.
     */
    private Callable<?> getRequestAction(RequestType rt) throws ServerCommunicationException {
        return switch (rt) {
            case GAME_MAP -> this::requestGameMap;
            case NPCs -> this::requestNPCs;
            case ITEMS -> this::requestItems;
            case COMMANDS -> this::requestCommands;
            case CLOSE_CONNECTION -> () -> {
                this.closeConnection();
                return null;
            };
            default -> throw new ServerCommunicationException("Tipo di richiesta non gestito: " + rt);
        };
    }

    /**
     * Metodo dispatcher che restituisce un'azione {@link Callable} basata sul {@link RequestType}.
     * Questa versione gestisce le richieste che richiedono un parametro ID.
     *
     * @param rt Il tipo di richiesta.
     * @param id L'ID da includere nell'azione Callable.
     * @return Un Callable che, quando eseguito, chiamerà il metodo appropriato con l'ID fornito.
     * @throws Exception se il tipo di richiesta non è valido per questa firma.
     */
    private Callable<?> getRequestAction(RequestType rt, int id) throws Exception {
        return switch (rt) {
            case ITEM -> () -> this.requestItem(id);
            case UPDATED_LOOK -> () -> this.requestUpdatedLook(id);
            default ->
                    throw new ServerCommunicationException("Non puoi effettuare questo tipo di richiesta in questo modo");
        };
    }

    /**
     * Richiede la mappa di gioco principale al server.
     *
     * @return L'oggetto {@link GameMap}.
     * @throws IOException            se avviene un errore di I/O.
     * @throws ClassNotFoundException se il server invia un oggetto di tipo inatteso.
     * @throws ServerException        se il server restituisce un errore logico.
     */
    private GameMap requestGameMap() throws IOException, ClassNotFoundException {
        GameMap gameMap = null;
        out.println("mappa");
        Object answer = in.readObject();
        if (answer instanceof GameMap) {
            gameMap = (GameMap) answer;
        } else if (answer instanceof String) {
            throw new ServerException((String) answer);
        }
        return gameMap;
    }

    /**
     * Richiede la lista degli NPC nella locazione corrente.
     *
     * @return Una lista di oggetti {@link NPC}.
     * @throws Exception se la risposta del server è anomala o si verifica un errore.
     */
    @SuppressWarnings("unchecked")
    private List<NPC> requestNPCs() throws Exception {
        List<NPC> NPCs = new ArrayList<>();
        out.println("NPCs");
        Object answer = in.readObject();
        if (answer instanceof List<?> answerList) {
            if (answerList.stream().allMatch(e -> e instanceof NPC)) {
                NPCs = (List<NPC>) answerList;
            } else {
                throw new Exception("Il server risponde in modo anomalo.");
            }
        } else if (answer instanceof String) {
            throw new ServerException((String) answer);
        }
        return NPCs;
    }

    /**
     * Richiede la lista degli oggetti nella locazione corrente.
     *
     * @return Una lista di oggetti {@link GeneralItem}.
     * @throws Exception se la risposta del server è anomala o si verifica un errore.
     */
    @SuppressWarnings("unchecked")
    private List<GeneralItem> requestItems() throws Exception {
        List<GeneralItem> items = null;
        out.println("oggetti");
        Object answer = in.readObject();
        if (answer instanceof List<?> answerList) {
            if (answerList.stream().allMatch(e -> e instanceof GeneralItem)) {
                items = (List<GeneralItem>) answerList;
            } else {
                throw new Exception("Il server risponde in modo anomalo.");
            }
        } else if (answer instanceof String) {
            throw new ServerException((String) answer);
        }
        return items;
    }

    /**
     * Richiede la lista dei comandi disponibili per il giocatore.
     *
     * @return Una lista di oggetti {@link Command}.
     * @throws Exception se la risposta del server è anomala o si verifica un errore.
     */
    @SuppressWarnings("unchecked")
    private List<Command> requestCommands() throws Exception {
        List<Command> commands = null;
        out.println("comandi");
        Object answer = in.readObject();
        if (answer instanceof List<?> answerList) {
            if (answerList.stream().allMatch(e -> e instanceof Command)) {
                commands = (List<Command>) answerList;
            } else {
                throw new Exception("Il server risponde in modo anomalo.");
            }
        } else if (answer instanceof String) {
            throw new ServerException((String) answer);
        }
        return commands;
    }

    /**
     * Richiede un oggetto specifico basandosi sul suo ID.
     *
     * @param itemID L'ID dell'oggetto da richiedere.
     * @return L'oggetto {@link GeneralItem} corrispondente.
     * @throws Exception se la risposta del server è anomala o si verifica un errore.
     */
    private GeneralItem requestItem(int itemID) throws Exception {
        GeneralItem item = null;
        out.println("oggetto-" + itemID);
        Object answer = in.readObject();
        if (answer instanceof GeneralItem) {
            item = (GeneralItem) answer;
        } else if (answer instanceof String) {
            throw new ServerException((String) answer);
        }
        return item;
    }

    /**
     * Richiede una descrizione testuale aggiornata a seguito di un evento.
     *
     * @param eventID L'ID dell'evento che ha causato la richiesta.
     * @return La stringa contenente la descrizione aggiornata.
     * @throws IOException            se avviene un errore di I/O.
     * @throws ClassNotFoundException se il server invia un oggetto di tipo inatteso.
     */
    private String requestUpdatedLook(int eventID) throws IOException, ClassNotFoundException {
        String look = null;
        out.println("descrizione-aggiornata-" + eventID);
        Object answer = in.readObject();
        if (answer instanceof String) {
            look = (String) answer;
        }
        return look;
    }

    /**
     * Invia il comando di terminazione al server e chiude la connessione.
     *
     * @throws IOException se si verifica un errore durante la chiusura del socket.
     */
    private void closeConnection() throws IOException {
        out.println("end");
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}