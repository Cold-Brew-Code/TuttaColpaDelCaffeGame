package it.tutta.colpa.del.caffe.game.exception;



/**
 *
 * @author giova
 */
public class ConnectionError extends Exception {
    public ConnectionError(String msg, Throwable cause) {
        super(msg, cause);
    }
    public ConnectionError(String msg) {
        super(msg);
    }
}
