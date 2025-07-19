package it.tutta.colpa.del.caffe.game.control;

/**
 * Interfaccia che definisce il contratto per il controller del gioco.
 * Implementa i metodi necessari per la gestione delle interazioni
 * tra l'utente e la logica di gioco.
 *
 * @author giovav
 * @since 15/07/25
 */
public interface Controller {

    /**
     * Notifica al controller un nuovo comando inserito dall'utente.
     *
     * @param command Il comando testuale da elaborare.
     */
    void notifyNewCommand(String command);

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