package it.tutta.colpa.del.caffe.game.exception;



/**
 * Eccezione personalizzata che viene lanciata quando si verifica un errore di
 * connessione, ad esempio durante richieste HTTP a servizi esterni.
 * <p>
 * Estende {@link Exception}, quindi è un'eccezione controllata.
 * </p>
 *
 * @author giova
 */
public class ConnectionError extends Exception {

    /**
     * Costruisce una nuova ConnectionError con un messaggio descrittivo e la
     * causa dell'errore.
     *
     * @param msg il messaggio che descrive l'errore di connessione
     * @param cause la causa originale dell'eccezione
     */
    public ConnectionError(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Costruisce una nuova ConnectionError con un messaggio descrittivo
     *
     * @param msg il messaggio che descrive l'errore di connessione
     */
    public ConnectionError(String msg) {
        super(msg);
    }
}
