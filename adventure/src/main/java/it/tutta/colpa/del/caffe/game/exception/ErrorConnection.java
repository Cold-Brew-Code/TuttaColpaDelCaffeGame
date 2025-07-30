package it.tutta.colpa.del.caffe.game.exception;



/**
 *
 * @author giova
 */
public class ErrorConnection extends RuntimeException {
    public ErrorConnection(String msg, Throwable cause) {
        super(msg, cause);
    }
}
