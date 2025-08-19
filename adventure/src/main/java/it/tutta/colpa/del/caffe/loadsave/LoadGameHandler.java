package it.tutta.colpa.del.caffe.loadsave;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.start.control.MainPageController;
import it.tutta.colpa.del.caffe.loadsave.boundary.ChoseSavePage;
import it.tutta.colpa.del.caffe.loadsave.control.LoadEngine;

/**
 * Handler specifico per il caricamento dal menu principale
 */
public class LoadGameHandler {
    public LoadGameHandler(MainPageController mainController) {
        GUI choseSavePage = new ChoseSavePage();
        LoadEngine loadController = new LoadEngine(mainController, choseSavePage);
        choseSavePage.linkController(loadController);
    }
}