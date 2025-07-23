package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.control.Controller;

/**
 * @author giovav
 * @since 19/07/25
 */
public interface LoadController extends Controller {
    void load(String save);
    void deleteSaves();
    void takeSaves();
    void cancelOperation();
}
