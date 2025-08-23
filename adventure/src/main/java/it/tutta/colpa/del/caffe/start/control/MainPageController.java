package it.tutta.colpa.del.caffe.start.control;

import it.tutta.colpa.del.caffe.game.control.Controller;

/**
 * Interfaccia {@code MainPageController} che definisce le azioni principali
 * gestibili dalla pagina iniziale del gioco.
 * <p>
 * Estende l'interfaccia {@link Controller} e fornisce i metodi di controllo per
 * avviare una nuova partita, caricare un salvataggio ed uscire
 * dall'applicazione.
 * </p>
 *
 * @author giovav
 * @since 19/07/25
 */
public interface MainPageController extends Controller {

    /**
     * Termina l'applicazione chiudendola completamente.
     */
    public void quit();

    /**
     * Carica una partita salvata.
     */
    public void loadGame();

    /**
     * Avvia una nuova partita.
     */
    public void startGame();
}
