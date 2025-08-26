package it.tutta.colpa.del.caffe.game.boundary;

import it.tutta.colpa.del.caffe.game.control.Controller;

/**
 * @author giovav
 * @since 18/07/25
 */
public interface GUI {
    void open();

    void close();

    void linkController(Controller c);

    void notifyError(String string, String string2);

    void showInformation(String string, String string2);
}
