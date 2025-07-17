package it.tutta.colpa.del.caffe.game.control;

import it.tutta.colpa.del.caffe.game.boundary.BoundaryOutput;
import it.tutta.colpa.del.caffe.game.entity.*;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.game.exception.ImageNotFoundException;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.Direzione;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import it.tutta.colpa.del.caffe.game.utility.Parser;
import it.tutta.colpa.del.caffe.game.utility.Utils;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Classe principale che gestisce la logica di gioco.
 * Comunica con il server per inizializzare lo stato del gioco,
 * consente il movimento tra stanze e l'uso dell'ascensore.
 * <p>
 * Implementa Serializable per supportare il salvataggio dello stato.
 *
 * @author giovav
 * @since 11/07/25
 */
public class Engine implements Controller, GameObservable {

    /**
     * Riferimento alla GUI, utile per eventuali interazioni con l'interfaccia utente.
     */
    private BoundaryOutput bo;

    /**
     * Descrizione dello stato attuale della partita, contenente mappa e comandi.
     */
    private GameDescription description;

    private ParserOutput parserOutput;
    private final List<GameObserver> observers = new ArrayList<>();
    private final List<String> messages = new ArrayList<>();
    private final Parser parser;
    private final MainPageController mpc;

    /**
     * Costruttore predefinito.
     * Tenta di inizializzare una nuova partita comunicando con il server.
     * In caso di errore di comunicazione, dovrebbe gestire l’eccezione mostrando
     * un dialogo informativo all’utente (da implementare).
     */
    public Engine(MainPageController mpc, BoundaryOutput bo) {
        this.bo = bo;
        this.mpc = mpc;
        Parser tmpParser = null;
        StringBuilder err = new StringBuilder();
        try {
            this.description = initGame();
            Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
            //tmpParser = new Parser(stopwords);
        } catch (ServerCommunicationException e) {
            err.append("Errore di comunicazione con il server: ").append(e.getMessage()).append("\n");
        } catch (NullPointerException ignored) {
        } catch (IOException e) {
            err.append("Errore verificato nel reperimento del file stopwords")
                    .append(e.getMessage())
                    .append("\n");
        }
        parser = tmpParser;
        if (!err.isEmpty()) {
            bo.closeWindow();
            mpc.openWindow();
            bo.notifyError("Errore", err.toString());
        } else {
            bo.out(description.getWelcomeMsg());
            bo.out(description.getGameMap().getCurrentRoom().getDescription().replace("\\n","\n"));
            try {
                bo.setImage(description.getGameMap().getCurrentRoom().getImagePath());
            }catch (ImageNotFoundException e){
                System.err.println("image not found");
            }
        }
    }

    /**
     * Costruttore per l'inizializzazione da file di salvataggio.
     * Deve essere implementato.
     *
     * @param filePath percorso del file di salvataggio
     */
    @SuppressWarnings("unused")
    public Engine(String filePath, MainPageController mpc) {
        StringBuilder err = new StringBuilder();
        parser = null;
        this.mpc = mpc;
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
        ServerInterface si = new ServerInterface("localhost",49152);
        GameDescription gd= new GameDescription(si.requestToServer(ServerInterface.RequestType.GAME_MAP),
                                                si.requestToServer(ServerInterface.RequestType.COMMANDS));
        si.requestToServer(ServerInterface.RequestType.CLOSE_CONNECTION);
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

    public void setBo(BoundaryOutput bo) {
        this.bo = bo;
    }

    @Override
    public void notifyNewCommand(String command) {
        //parserOutput = parser.parse(command,description.getCommands(),description.getInventory(), description.getGameMap());
        notifyObservers();
        bo.out(messages.getLast());
    }

    @Override
    public void endGame() {
        if (bo.notifySomething("Chiusura", "Vuoi davvero chiudere il gioco?") == 0) {
            this.bo.closeWindow();
            mpc.openWindow();
        }
    }

    @Override
    public void getInventory() {
        // description.getInventory();
    }

    @Override
    public void saveGame() {
        //chiamare LoadSave.save();
    }

    @Override
    public void attach(GameObserver o) {
        if (!observers.contains(o))
            observers.add(o);
    }

    @Override
    public void detach(GameObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (GameObserver o : observers) {
            messages.add(o.update(description, parserOutput).toString());
        }
    }


}