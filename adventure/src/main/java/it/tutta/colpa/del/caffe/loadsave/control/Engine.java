package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.loadsave.boundary.ChoseSavePage;

import java.util.List;

/**
 * @author giovav
 * @since 19/07/25
 */
public class Engine implements LoadController {
    private Controller mainPageController;
    private GUI csp;
    private final SaveManager saveManager = new SaveManager();

    public Engine(Controller mainPageController, GUI choseSavePage) {
        this.mainPageController = mainPageController;
        this.csp = choseSavePage;
        csp.open();
    }

    @Override
    public GameDescription load(String saveName) throws Exception {
        return SaveLoad.loadGame(saveName);
    }

    @Override
    public boolean deleteSave(String saveName) {
        return saveManager.deleteSave(saveName);
    }

    @Override
    public List<String> getSaveList() throws Exception {
        return saveManager.listSaves();
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
