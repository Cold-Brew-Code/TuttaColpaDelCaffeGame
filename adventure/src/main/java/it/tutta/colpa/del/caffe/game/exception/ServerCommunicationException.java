package it.tutta.colpa.del.caffe.game.exception;

/**
 * Eccezione personalizzata che viene lanciata quando si verifica un errore
 * durante la comunicazione con il server.
 * <p>
 * Estende {@link Exception}, quindi è un'eccezione controllata che deve essere
 * dichiarata o gestita.
 * </p>
 *
 * @author giovav
 * @since 14/07/25
 */
public class ServerCommunicationException extends Exception {

    /**
     * Costruisce una nuova ServerCommunicationException con un messaggio
     * descrittivo.
     *
     * @param message il messaggio descrittivo dell'errore
     */
    public ServerCommunicationException(String message) {
        super(message);
    }

    /**
     * Costruisce una nuova ServerCommunicationException a partire da un'altra
     * ServerCommunicationException.
     *
     * @param exception l'eccezione originale da incapsulare
     */
    public ServerCommunicationException(ServerCommunicationException message) {
        super(message);
    }
}
