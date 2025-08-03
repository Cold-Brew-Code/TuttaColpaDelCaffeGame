package it.tutta.colpa.del.caffe.game.exception;

/**
 * @author giovav
 * @since 16/07/25
 */
public class ImageNotFoundException extends Exception {
    public ImageNotFoundException(String message) {
        super(message);
    }
    public ImageNotFoundException(ImageNotFoundException e) {
        super(e);
    }
}
