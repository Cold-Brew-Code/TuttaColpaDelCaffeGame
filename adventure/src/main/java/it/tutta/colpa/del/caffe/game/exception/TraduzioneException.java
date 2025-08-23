package it.tutta.colpa.del.caffe.game.exception;

/**
 *
 * @author giova 
 * Eccezione personalizzata che viene lanciata quando si verifica
 * un errore durante la traduzione di un testo tramite l'API di traduzione.
 * <p>
 * Estende {@link RuntimeException}, quindi è un'eccezione non controllata.
 * </p>
 *
 * <p>
 * Può essere utilizzata per gestire errori derivanti da:
 * <ul>
 * <li>Problemi di connessione con l'API di traduzione.</li>
 * <li>Risposte non valide o incomplete dall'API.</li>
 * <li>Altri problemi legati al processo di traduzione.</li>
 * </ul>
 * </p>
 */
public class TraduzioneException extends RuntimeException {

    public TraduzioneException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TraduzioneException(String message) {
        super(message);
    }
}
