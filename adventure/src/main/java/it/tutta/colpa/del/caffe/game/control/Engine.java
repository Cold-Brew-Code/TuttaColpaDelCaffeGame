
package it.tutta.colpa.del.caffe.game.control;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.tutta.colpa.del.caffe.adventure.control.BuildObserver;
import it.tutta.colpa.del.caffe.adventure.control.LeaveObserver;
import it.tutta.colpa.del.caffe.adventure.control.LiftObserver;
import it.tutta.colpa.del.caffe.adventure.control.LookAtObserver;
import it.tutta.colpa.del.caffe.adventure.control.MoveObserver;
import it.tutta.colpa.del.caffe.adventure.control.OpenObserver;
import it.tutta.colpa.del.caffe.adventure.control.PickUpObserver;
import it.tutta.colpa.del.caffe.adventure.control.ReadObserver;
import it.tutta.colpa.del.caffe.adventure.control.TalkObserver;
import it.tutta.colpa.del.caffe.adventure.control.UseObserver;
import it.tutta.colpa.del.caffe.adventure.other.Clock;
import it.tutta.colpa.del.caffe.adventure.other.TimeObserver;
import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.boundary.GameEndedPage;
import it.tutta.colpa.del.caffe.game.boundary.GameGUI;
import it.tutta.colpa.del.caffe.game.boundary.InventoryPage;
import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameMap;
import it.tutta.colpa.del.caffe.game.entity.GameObservable;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.game.exception.ImageNotFoundException;
import it.tutta.colpa.del.caffe.game.exception.ParserException;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.Direzione;
import it.tutta.colpa.del.caffe.game.utility.Parser;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import it.tutta.colpa.del.caffe.game.utility.RequestType;
import it.tutta.colpa.del.caffe.game.utility.Utils;
import it.tutta.colpa.del.caffe.start.control.MainPageController;


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
public class Engine implements GameController, GameObservable, TimeObserver {

    /**
     * Riferimento alla GUI, utile per eventuali interazioni con l'interfaccia utente.
     */
    private GameGUI GUI;

    /**
     * Descrizione dello stato attuale della partita, contenente mappa e comandi.
     */
    private final GameDescription description;

    private final List<GameObserver> observers = new ArrayList<>();
    private final Parser parser;
    private final MainPageController mpc;
    private Clock timer;

    /**
     * Costruttore predefinito.
     * Tenta di inizializzare una nuova partita comunicando con il server.
     * In caso di errore di comunicazione, dovrebbe gestire l’eccezione mostrando
     * un dialogo informativo all’utente (da implementare).
     */
    public Engine(MainPageController mpc, GameGUI GUI) {
        this.GUI = GUI;
        this.mpc = mpc;
        Parser tmpParser = null;
        GameDescription tmpDescription = null;
        StringBuilder err = new StringBuilder("<html>");
        try {
            tmpDescription = initDescriptionFromServer();
            tmpParser = initParserFromServer(tmpDescription);
            //timer
        } catch (ServerCommunicationException e) {
            err.append("<p><b>Errore di comunicazione con il server</b>:</p><p>").append(e.getMessage()).append("</p>");
        } catch (IOException e) {
            err.append("<p><b>Errore verificato nel reperimento del file stopwords</b></p><p>")
                    .append(e.getMessage())
                    .append("</p>");
        } catch (NullPointerException e) {
            err.append("<p><b>Errore generico</b>:</p> <p> ")
                    .append(e.getMessage())
                    .append("</p>");
        }
        err.append("</html>");
        this.description = tmpDescription;
        this.parser = tmpParser;
        if (!err.toString().equals("<html></html>")) {
            mpc.openGUI();
            GUI.close();
            GUI.notifyError("Errore", err.toString());
        } else {
            //init first scenario
            this.timer = new Clock(20, this);// passo il tempo e l'engine corrente 
            timer.start();// starto l'orologio 
            GUI.out(description.getWelcomeMsg());
            GUI.out(description.getCurrentRoom().getDescription().translateEscapes());
            try {
                GUI.setImage(description.getCurrentRoom().getImagePath());
            } catch (ImageNotFoundException e) {
                try {
                    System.err.println("entrfa");
                    GUI.setImage("images/resource_not_found.png");
                } catch (ImageNotFoundException e2) {
                    GUI.notifyWarning("Attenzione!", "Risorsa immagine non trovata!");
                }
            }
        }
        this.attach(new BuildObserver());
        this.attach(new LookAtObserver());
        this.attach(new MoveObserver());
        this.attach(new OpenObserver());
        this.attach(new PickUpObserver());
        this.attach(new ReadObserver());
        this.attach(new TalkObserver());
        this.attach(new UseObserver());
        this.attach(new LeaveObserver());
        this.attach(new LiftObserver());
    }

    private Parser initParserFromServer(GameDescription description) throws IOException, ServerCommunicationException {
        Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
        ServerInterface si = new ServerInterface("localhost", 49152);
        Parser p = new Parser(
                stopwords,
                description.getCommands(),
                si.requestToServer(RequestType.ITEMS),
                si.requestToServer(RequestType.NPCs)
        );
        si.requestToServer(RequestType.CLOSE_CONNECTION);
        return p;
    }

    /**
     * Inizializza una nuova partita contattando il server locale.
     * Recupera la mappa e l'elenco dei comandi, verificandone la correttezza.
     *
     * @return una GameDescription contenente la mappa e i comandi
     * @throws ServerCommunicationException se la comunicazione fallisce o i dati sono incompleti
     */

    private GameDescription initDescriptionFromServer() throws ServerCommunicationException {
        ServerInterface si = new ServerInterface("localhost", 49152);
        GameMap gm = si.requestToServer(RequestType.GAME_MAP);
        List<Command> c = si.requestToServer(RequestType.COMMANDS);
        GameDescription gd = new GameDescription(
                si.requestToServer(RequestType.GAME_MAP),
                si.requestToServer(RequestType.COMMANDS));
        si.requestToServer(RequestType.CLOSE_CONNECTION);
        return gd;
    }


    /**
     * Costruttore per l'inizializzazione da file di salvataggio.
     * Deve essere implementato.
     *
     * @param filePath percorso del file di salvataggio
     */
    @SuppressWarnings("unused")
    public Engine(String filePath, it.tutta.colpa.del.caffe.start.control.Engine mpc) {
        StringBuilder err = new StringBuilder();
        parser = null;
        description = null;
        this.mpc = mpc;
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

    public void setGUI(GameGUI bo) {
        this.GUI = bo;
    }

    @Override
    public void openGUI() {
        GUI.open();
    }

    @Override
    public void closeGUI() {
        GUI.close();
    }

    @Override
    public void executeNewCommand(String command) {
        if (!command.isEmpty()) {
            GUI.executedCommand();

            try {
                notifyObservers(parser.parse(command));
                GUI.setImage(description.getCurrentRoom().getImagePath());
            } catch (ImageNotFoundException e) {
                try {
                    GUI.setImage("/images/resource_not_found.png");
                } catch (ImageNotFoundException e2) {
                    GUI.notifyWarning("Attenzione!", "Risorsa immagine non trovata!");
                }
            } catch (ParserException e) {
                description.getMessages().add(e.getMessage());
            }
            GUI.out(description.getMessages().getLast().translateEscapes());
        }
    }

    @Override
    public void endGame() {
        if (GUI.notifySomething("Chiusura", "Vuoi davvero chiudere il gioco?") == 0) {
            this.GUI.close();
            new GameEndedPage(this.description.getStatus(), mpc).setVisible(true);
        }
    }

    @Override
    public void saveGame() {
        //chiamare LoadSave.save();
    }

    @Override
    public void showInventory() {
        GUI inventory = new InventoryPage((Frame) this.GUI, this.description.getInventory());
        inventory.open();
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
    public void notifyObservers(ParserOutput po) {
        for (GameObserver o : observers) {
            try {
                String out = o.update(description, po);
                if (!out.isEmpty()) {
                    description.getMessages().add(out);
                }
            } catch (ServerCommunicationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void finishGame() {
        GUI.out("Tempo esaurito! La partita è finita.");
        GUI.notifyWarning("Tempo scaduto", "Hai esaurito il tempo a disposizione!");
        GUI.close();
    }

    @Override
    public void onTimeExpired() {
        // chiama il metodo esistente
        finishGame();
    }
}
