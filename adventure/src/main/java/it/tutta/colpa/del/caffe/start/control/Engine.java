package it.tutta.colpa.del.caffe.start.control;

import it.tutta.colpa.del.caffe.game.GameHandler;
import it.tutta.colpa.del.caffe.loadsave.ChoseSaveHandler;
import it.tutta.colpa.del.caffe.start.boundary.MainPage;

/**
 * @author giovav
 * @since 16/07/25
 *
 * La classe {@code Engine} rappresenta il motore principale dell'applicazione
 * e implementa l'interfaccia {@link MainPageController}.
 * <p>
 * Si occupa di gestire il flusso del gioco, permettendo di avviare una nuova partita,
 * caricare un salvataggio, chiudere l'applicazione e controllare l'apertura/chiusura
 * della finestra principale (GUI).
 * </p>
 */
public class Engine implements MainPageController {

    /**
     * Riferimento al frame principale dell'applicazione.
     */
    private MainPage frame;

    /**
     * Costruisce un nuovo oggetto {@code Engine} associato al frame specificato.
     *
     * @param frame il frame principale dell'applicazione da utilizzare per la GUI
     */
    public Engine(MainPage frame) {
        this.frame = frame;
    }

    /**
     * Avvia una nuova partita creando un {@link GameHandler}.
     * <p>
     * Il game handler utilizza questo motore come controller per
     * interagire con il resto dell'applicazione.
     * </p>
     */
    public void startGame() {
        GameHandler gameHandler = new GameHandler(this);
    }

    /**
     * Carica una partita salvata aprendo un {@link ChoseSaveHandler}.
     * <p>
     * Dopo aver scelto il salvataggio, la GUI principale viene chiusa.
     * </p>
     */
    public void loadGame() {
        ChoseSaveHandler csv = new ChoseSaveHandler(this);
        closeGUI();
    }

    /**
     * Termina l'applicazione chiudendola completamente.
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * Apre la GUI principale chiamando il metodo {@code open()} del frame.
     */
    public void openGUI() {
        frame.open();
    }

    /**
     * Chiude la GUI principale chiamando il metodo {@code close()} del frame.
     */
    public void closeGUI() {
        frame.close();
    }
}
