package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.loadsave.boundary.ChoseSavePage;

/**
 * @author giovav
 * @since 19/07/25
 */
public class Engine implements LoadController {
    private Controller mainPageController;
    private GUI csp;

    public Engine(Controller mainPageController, GUI choseSavePage) {
        this.mainPageController=mainPageController;
        this.csp=choseSavePage;
        csp.open();
    }

    @Override
    public void load(String save) {

    }

    @Override
    public void deleteSaves() {

    }

    @Override
    public void takeSaves() {

    }

    @Override
    public void cancelOperation() {
        closeGUI();
    }

    @Override
    public void openGUI() {

    }

    @Override
    public void closeGUI() {
        mainPageController.openGUI();
        csp.close();
    }
}
