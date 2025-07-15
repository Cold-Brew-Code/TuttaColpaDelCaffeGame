package it.tutta.colpa.del.caffe.game.exception;

/**
 * @author giovav
 * @since 14/07/25
 */
public class ServerCommunicationException extends Exception {
    public ServerCommunicationException(String message) {
        super(message);
    }
    public ServerCommunicationException(ServerCommunicationException message) {
        super(message);
    }
}
