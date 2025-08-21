package it.tutta.colpa.del.caffe.game.exception;

/**
 * Eccezione personalizzata che viene lanciata quando un'immagine richiesta non
 * viene trovata.
 * <p>
 * Estende {@link Exception}, quindi è un'eccezione controllata.
 * </p>
 *
 * @author giovav
 * @since 16/07/25
 */
public class ImageNotFoundException extends Exception {

    /**
     * Costruisce una nuova ImageNotFoundException con un messaggio descrittivo.
     *
     * @param message il messaggio che descrive l'errore relativo all'immagine
     * mancante
     */
    public ImageNotFoundException(String message) {
        super(message);
    }

    /**
     * Costruisce una nuova ImageNotFoundException a partire da un'altra istanza
     * della stessa eccezione.
     *
     * @param e un'altra ImageNotFoundException da cui copiare le informazioni
     */
    public ImageNotFoundException(ImageNotFoundException e) {
        super(e);
    }
}
