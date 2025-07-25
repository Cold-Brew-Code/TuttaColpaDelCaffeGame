package it.tutta.colpa.del.caffe.game.control;

/**
 * @author giovav
 * @since 19/07/25
 */
public interface GameController extends Controller{
    /**
     * Notifica al controller un nuovo comando inserito dall'utente.
     *
     * @param command Il comando testuale da elaborare.
     */
    void executeNewCommand(String command);

    /**
     * Termina la partita in corso, eventualmente eseguendo
     * operazioni di pulizia o salvataggio.
     */
    void endGame();

    /**
     * Salva lo stato corrente della partita.
     */
    void saveGame();

    void showInventory();
}
