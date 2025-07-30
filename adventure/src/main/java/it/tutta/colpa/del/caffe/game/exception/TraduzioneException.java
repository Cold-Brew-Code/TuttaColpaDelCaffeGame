package it.tutta.colpa.del.caffe.game.exception;



/**
 *
 * @author giova
 */
public class TraduzioneException extends RuntimeException {
    public TraduzioneException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TraduzioneException(String message) {
        super(message);
    }
}