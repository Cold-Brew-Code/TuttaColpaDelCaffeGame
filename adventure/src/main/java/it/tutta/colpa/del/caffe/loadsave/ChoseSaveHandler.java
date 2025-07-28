package it.tutta.colpa.del.caffe.loadsave;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.loadsave.boundary.ChoseSavePage;
import it.tutta.colpa.del.caffe.loadsave.control.Engine;

/**
 * @author giovav
 * @since 19/07/25
 */
public class ChoseSaveHandler {
    public ChoseSaveHandler(Controller mpc){
        GUI choseSavePage = new ChoseSavePage();
        Controller lc = new Engine(mpc,choseSavePage);
        choseSavePage.linkController(lc);
    }
}
