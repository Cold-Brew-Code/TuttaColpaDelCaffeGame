package it.tutta.colpa.del.caffe.game.control;

import java.util.concurrent.Callable;

import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GameMap;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.NPC;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author giovav
 * @since 17/07/25
 */
public class ServerInterface {
    private Socket connection;
    private PrintWriter out;
    private ObjectInputStream in;

    public ServerInterface(String IP, int porta) {
        try {
            connection = new Socket(IP, porta);
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
        } catch (Exception e) {
            this.connection = null;
            this.out = null;
            this.in = null;
        }
    }

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
     * Restituisce l'azione di richiesta al server come un oggetto Callable,
     * basandosi sul RequestType.
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

    private Callable<?> getRequestAction(RequestType rt, int id) throws Exception {
        return switch (rt) {
            case ITEM -> ()-> this.requestItem(id);
            case UPDATED_LOOK -> ()->this.requestUpdatedLook(id);
            default ->
                    throw new ServerCommunicationException("Non puoi effettuare questo tipo di richiesta in questo modo");
        };
    }

    private GameMap requestGameMap() throws IOException, ClassNotFoundException {
        GameMap gameMap = null;
        out.println("mappa");
        Object answer = in.readObject();
        if (answer instanceof GameMap) {
            gameMap = (GameMap) answer;
        } else {
            if (answer instanceof String) {
                throw new ServerException((String) answer);
            }
        }
        return gameMap;
    }

    @SuppressWarnings("unchecked")
    private List<NPC> requestNPCs() throws Exception {
        List<NPC> NPCs = new ArrayList<>();
        out.println("NPCs");
        Object answer = in.readObject();
        if (answer instanceof List<?> answerList) {
            boolean areAllNPC = answerList.stream()
                    .allMatch(e -> e instanceof NPC);
            if (areAllNPC) {
                NPCs = (List<NPC>) answerList;
            } else {
                throw new Exception("Il server risponde in modo anomalo.");
            }
        } else {
            if (answer instanceof String) {
                throw new ServerException((String) answer);
            }
        }
        return NPCs;
    }

    @SuppressWarnings("unchecked")
    private List<GeneralItem> requestItems() throws Exception {
        List<GeneralItem> items = null;
        out.println("oggetti");
        Object answer = in.readObject();
        if (answer instanceof List<?> answerList) {
            boolean areAllItems = answerList.stream()
                    .allMatch(e -> e instanceof GeneralItem);
            if (areAllItems) {
                items = (List<GeneralItem>) answerList;
            } else {
                throw new Exception("Il server risponde in modo anomalo.");
            }
        } else {
            if (answer instanceof String) {
                throw new ServerException((String) answer);
            }
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    private List<Command> requestCommands() throws Exception {
        List<Command> commands = null;
        out.println("comandi");
        Object answer = in.readObject();
        if (answer instanceof List<?> answerList) {
            boolean areAllCommands = answerList.stream()
                    .allMatch(e -> e instanceof Command);
            if (areAllCommands) {
                commands = (List<Command>) answerList;
            } else {
                throw new Exception("Il server risponde in modo anomalo.");
            }
        } else {
            if (answer instanceof String) {
                throw new ServerException((String) answer);
            }
        }
        return commands;
    }

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

    private String requestUpdatedLook(int eventID) throws IOException, ClassNotFoundException {
        String look = null;
        out.println("descrizione-aggiornata-" + eventID);
        Object answer = in.readObject();
        if (answer instanceof String) {
            look = (String) answer;
        }
        return look;
    }

    private void closeConnection() throws IOException{
        out.println("end");
        connection.close();
    }
    enum RequestType {
        GAME_MAP,
        NPCs,
        ITEMS,
        CLOSE_CONNECTION,
        COMMANDS,
        UPDATED_LOOK,
        ITEM
    }
}
