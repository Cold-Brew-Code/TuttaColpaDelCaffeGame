package it.tutta.colpa.del.caffe.game.control;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.tutta.colpa.del.caffe.game.utility.Clock;
import it.tutta.colpa.del.caffe.game.utility.TimeObserver;
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
import it.tutta.colpa.del.caffe.game.utility.*;
import it.tutta.colpa.del.caffe.start.control.MainPageController;


/**
 * Classe principale che orchestra la logica del gioco, agendo come motore centrale.
 * Gestisce l'intero ciclo di vita di una partita: inizializzazione (da server o, in futuro, da salvataggio),
 * elaborazione dei comandi del giocatore, aggiornamento dello stato del gioco e interazione con l'interfaccia grafica.
 * <p>
 * Implementa:
 * <ul>
 * <li>{@link GameController}: per esporre i metodi di controllo principali (es. esecuzione comandi, chiusura).</li>
 * <li>{@link GameObservable}: per implementare il pattern Observer, notificando gli osservatori (che gestiscono comandi specifici)
 * delle azioni del giocatore.</li>
 * <li>{@link TimeObserver}: per reagire agli eventi del timer di gioco (aggiornamento del tempo, scadenza).</li>
 * </ul>
 *
 * @author giovav
 * @since 11/07/25
 */
public class Engine implements GameController, GameObservable, TimeObserver {

    /**
     * Riferimento all'interfaccia grafica (GUI) del gioco, utilizzato per aggiornare la vista
     * in base ai cambiamenti del modello.
     */
    private GameGUI GUI;

    /**
     * Contiene l'intero stato della partita corrente, inclusi la mappa, l'inventario, lo stato del giocatore
     * e i messaggi da visualizzare.
     */
    private final GameDescription description;

    /**
     * Lista degli osservatori che reagiscono ai comandi del giocatore. Ogni osservatore
     * è specializzato nella gestione di uno o più tipi di comandi (es. movimento, interazione).
     */
    private final List<GameObserver> observers = new ArrayList<>();
    /**
     * L'analizzatore sintattico (parser) che interpreta i comandi testuali del giocatore
     * e li trasforma in un formato strutturato ({@link ParserOutput}) comprensibile dal motore di gioco.
     */
    private final Parser parser;
    /**
     * Controller della pagina principale, utilizzato per tornare al menu iniziale
     * al termine della partita.
     */
    private final MainPageController mpc;


    /**
     * Costruttore per avviare una nuova partita.
     * Inizializza lo stato del gioco recuperando i dati necessari (mappa, comandi, oggetti, NPC)
     * da un server. In caso di errore durante l'inizializzazione (es. server non raggiungibile),
     * interrompe l'avvio del gioco e notifica l'utente tramite un messaggio di errore.
     * Se l'inizializzazione ha successo, avvia il timer di gioco e mostra la schermata iniziale.
     *
     * @param mpc Il controller della pagina principale, per gestire il ritorno al menu.
     * @param GUI L'interfaccia grafica del gioco da controllare.
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
            //[debug] description.setStatus(GameStatus.ESAME_DA_FARE);
            //[debug] description.getGameMap().setCurrentRoom(description.getGameMap().getRoom(29));
            //init first scenario
            this.description.setTimer(new Clock(20, this, this.GUI));// passo il tempo e l'engine corrente
            this.description.getTimer().setRemainingTime(2100);
            this.GUI.initProgressBar(2100, false);
            this.description.getTimer().start();// starto l'orologio
            GUI.out(description.getWelcomeMsg());
            GUI.out(description.getCurrentRoom().getDescription().translateEscapes());
            try {
                GUI.setImage(description.getCurrentRoom().getImagePath());
            } catch (ImageNotFoundException e) {
                GUI.notifyWarning("Attenzione!", "Risorsa immagine non trovata!");
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

    /**
     * Inizializza il parser recuperando dal server le liste di oggetti e NPC,
     * che costituiscono il vocabolario del gioco.
     *
     * @param description Lo stato del gioco, necessario per ottenere l'elenco dei comandi.
     * @return Un'istanza di {@link Parser} configurata e pronta all'uso.
     * @throws IOException Se si verifica un errore durante la lettura del file delle stopwords.
     * @throws ServerCommunicationException Se fallisce la comunicazione con il server.
     */
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
     * Inizializza lo stato del gioco ({@link GameDescription}) contattando il server locale.
     * Recupera la mappa di gioco e l'elenco dei comandi disponibili.
     *
     * @return una {@link GameDescription} contenente la mappa e i comandi.
     * @throws ServerCommunicationException se la comunicazione fallisce o i dati ricevuti sono incompleti.
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
     * Costruttore per caricare una partita da un file di salvataggio.
     * Funzionalità non ancora implementata.
     *
     * @param filePath il percorso del file di salvataggio.
     * @param mpc Il controller della pagina principale.
     */
    @SuppressWarnings("unused")
    public Engine(String filePath, it.tutta.colpa.del.caffe.start.control.Engine mpc) {
        StringBuilder err = new StringBuilder();
        parser = null;
        description = null;
        this.mpc = mpc;
    }


    /**
     * Inizializza lo stato di una partita a partire da un file di salvataggio.
     * Metodo non implementato.
     *
     * @param savePath il percorso del file di salvataggio.
     */
    @SuppressWarnings("unused")
    private void initGame(String savePath) {
        // returns GameDescription
    }

    /**
     * Tenta di muovere il giocatore nella direzione specificata.
     * Metodo di utilità, non più utilizzato direttamente poiché la logica è gestita da {@link MoveObserver}.
     *
     * @param direction la direzione in cui muoversi.
     * @return true se il movimento è possibile, false altrimenti.
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
     * Tenta di utilizzare l'ascensore per spostarsi a un piano specifico.
     * Metodo di utilità, non più utilizzato direttamente poiché la logica è gestita da {@link LiftObserver}.
     *
     * @param floor il piano da raggiungere.
     * @return true se lo spostamento è riuscito, false altrimenti.
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

    /**
     * Imposta il riferimento all'interfaccia grafica.
     * @param bo L'istanza di {@link GameGUI}.
     */
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

    /**
     * Esegue un nuovo comando fornito dal giocatore.
     * Il comando viene prima analizzato dal parser. Se valido, notifica tutti gli osservatori,
     * che tenteranno di eseguirlo. Aggiorna l'output e l'immagine nella GUI.
     * Contiene anche la logica per gestire la transizione di stato dopo l'uso del bagno.
     *
     * @param command la stringa di comando inserita dal giocatore.
     */
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

        if (description.getStatus() == GameStatus.BAGNO_USATO) {
            GUI.showInformation("OTTIMO LAVORO", "<html><p>Hai usato finalmente il bagno, liberando i tuoi impellenti bisogni.</p><p>Adesso non ti resta che sostenere il tuo esame.</p><p><b>Corri!!!!</b></p></html>");
            description.setStatus(GameStatus.ESAME_DA_FARE);
            this.GUI.initProgressBar(600, true);
            this.description.getTimer().setRemainingTime(600);
        }
    }

    /**
     * Termina la partita corrente.
     * Chiede conferma all'utente e, in caso affermativo, chiude la finestra di gioco
     * e mostra la schermata di fine partita.
     */
    @Override
    public void endGame() {
        if (GUI.notifySomething("Chiusura", "<html><p><b>Vuoi davvero chiudere il gioco?</b></p><p>ATTENZIONE, I PROGRESSI <b>NON</b> VERRANNO SALVATI</p></html>") == 0) {
            this.GUI.close();
            new GameEndedPage(this.description.getStatus(), mpc).setVisible(true);
        }
    }

    /**
     * Salva lo stato attuale della partita.
     * Funzionalità non ancora implementata.
     */
    @Override
    public void saveGame() {
        //chiamare LoadSave.save();
    }

    /**
     * Mostra l'inventario del giocatore in una finestra separata.
     */
    @Override
    public void showInventory() {
        GUI inventory = new InventoryPage((Frame) this.GUI, this.description.getInventory());
        inventory.open();
    }


    /**
     * Aggiunge un osservatore alla lista, se non è già presente.
     * @param o L'osservatore da aggiungere.
     */
    @Override
    public void attach(GameObserver o) {
        if (!observers.contains(o))
            observers.add(o);
    }

    /**
     * Rimuove un osservatore dalla lista.
     * @param o L'osservatore da rimuovere.
     */
    @Override
    public void detach(GameObserver o) {
        observers.remove(o);
    }

    /**
     * Notifica a tutti gli osservatori registrati un nuovo comando da processare.
     * Ogni osservatore riceve il comando analizzato e, se competente, lo esegue,
     * potenzialmente modificando lo stato del gioco.
     * Al termine, controlla se il gioco è terminato (vittoria/sconfitta) per gestirne la conclusione.
     *
     * @param po L'output del parser, contenente il comando strutturato.
     */
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
        if (Set.of(GameStatus.BOCCIATO, GameStatus.PROMOSSO).contains(description.getStatus())) {
            handleGameEnding();
        }
    }

    /**
     * Aggiorna il tempo visualizzato nell'interfaccia grafica.
     * Questo metodo viene chiamato ad ogni "tick" del timer di gioco.
     *
     * @param timeFormatted il tempo residuo formattato come stringa (es. "mm:ss").
     */
    @Override
    public void onTimeUpdate(String timeFormatted) {
        GUI.setDisplayedClock(timeFormatted);
    }

    /**
     * Gestisce l'evento di scadenza del tempo.
     * Imposta lo stato del gioco su "tempo esaurito" e avvia la sequenza di fine partita.
     */
    @Override
    public void onTimeExpired() {
        if (description.getStatus() == GameStatus.BAGNO_USATO) {
            description.setStatus(GameStatus.BAGNO_USATO_TEMPO_ESAURITO);
        } else {
            description.setStatus(GameStatus.TEMPO_ESAURITO);
        }
        handleGameEnding();
    }

    /**
     * Gestisce la conclusione della partita.
     * Chiude la finestra di gioco principale e mostra la schermata finale
     * con il risultato ottenuto.
     */
    private void handleGameEnding() {
        this.GUI.close();
        new GameEndedPage(description.getStatus(), mpc).setVisible(true);
    }
}