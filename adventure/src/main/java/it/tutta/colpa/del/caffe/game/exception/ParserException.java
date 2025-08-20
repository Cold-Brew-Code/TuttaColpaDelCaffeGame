package it.tutta.colpa.del.caffe.game.exception;

/**
 * Eccezione personalizzata che viene lanciata durante errori di parsing.
 * <p>
 * Estende {@link Exception}, quindi è un'eccezione controllata che deve essere
 * dichiarata o gestita.
 *  </p>
 *
 * @author giovav
 * @since 05/08/25
 */
public class ParserException extends Exception {

    /**
     * Costruisce una nuova ParserException con un messaggio descrittivo.
     *
     * @param message il messaggio che descrive l'errore di parsing
     */
    public ParserException(String message) {
        super(message);
    }
}
