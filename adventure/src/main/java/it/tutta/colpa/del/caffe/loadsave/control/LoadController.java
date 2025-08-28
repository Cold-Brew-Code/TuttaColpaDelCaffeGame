package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.control.Controller;

/**
 * Interfaccia per il controller di caricamento salvataggi.
 */
public interface LoadController extends Controller {
    void load(String save);

    void deleteSave(String fileName);

    void takeSaves();

    void cancelOperation();
}